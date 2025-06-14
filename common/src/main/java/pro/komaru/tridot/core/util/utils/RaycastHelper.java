package pro.komaru.tridot.core.util.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import pro.komaru.tridot.core.phys.Vec3;
import pro.komaru.tridot.core.struct.Seq;
import pro.komaru.tridot.core.struct.func.Boolf;
import pro.komaru.tridot.core.struct.func.Func2;

public class RaycastHelper {
    private static final RaycastHelper instance = new RaycastHelper();
    public static RaycastHelper get() {
        return instance;
    }

    public HitResult raycast(Level level, Context ctx) {
        Vec3 start = ctx.start, end = ctx.end;
        float length = start.dst(end);
        float steps = length * 160;
        Vec3 stepDir = end.cpy().sub(start).scale(1f/steps);
        Vec3 pos = start.cpy();

        float lkrad = ctx.lookupEntityRadius;

        Seq<Entity> entities = Seq.empty();
        for (int i = 0; i < steps; i++) {
            BlockPos blockPos = BlockPos.containing(pos.toMC());

            if(ctx.checkEntities) {
                AABB box = new AABB(pos.x - lkrad, pos.y - lkrad, pos.z - lkrad,
                        pos.x + lkrad, pos.y + lkrad, pos.z + lkrad);
                for (Entity entity : level.getEntitiesOfClass(Entity.class, box)) {
                    if (ctx.entityFilter.get(entity) && !entities.contains(entity)) {
                        entities.add(entity);
                        if (ctx.stopOnMaxEntities && entities.size >= ctx.maxEntities)
                            return new HitResult(start, pos, entities);
                    }
                }
            }
            if(ctx.checkBlocks) {
                BlockHitResult hit = ctx.blockShape(level.getBlockState(blockPos), level, blockPos)
                        .clip(start.toMC(), end.toMC(), blockPos);
                if(hit != null && ctx.blockPosFilter.get(level,blockPos)) {
                    boolean isBlock = !level.getBlockState(hit.getBlockPos()).isAir();
                    return new HitResult(start,pos,hit.getBlockPos(),hit.getDirection(),isBlock,entities);
                }
            }
            if(ctx.checkFluids) {
                BlockHitResult hit = ctx.fluidShape(level.getFluidState(blockPos), level, blockPos)
                        .clip(start.toMC(), end.toMC(), blockPos);
                if(hit != null)
                    return new HitResult(start, pos, hit.getBlockPos(), hit.getDirection(), true, entities);
            }

            pos.add(stepDir);
        }
        return new HitResult(start, end, BlockPos.ZERO, Direction.UP, false, entities);
    }
    public HitResult raycastBlocks(Level level, Vec3 start, Vec3 end) {
        return raycast(level, new Context(start, end));
    }
    public HitResult raycastBlocks(Level level, Vec3 start, Vec3 end, Func2<Level,BlockPos,Boolean> blockPosFilter) {
        return raycast(level, new Context(start, end, blockPosFilter));
    }
    public HitResult raycastEntity(Level level, Vec3 start, Vec3 end, Boolf<Entity> entityFilter, float lookupEntityRadius) {
        return raycast(level, new Context(start, end, entityFilter, lookupEntityRadius));
    }
    public HitResult raycastEntities(Level level, Vec3 start, Vec3 end, Boolf<Entity> entityFilter, float lookupEntityRadius, int maxCount) {
        return raycast(level, new Context(start, end, entityFilter, lookupEntityRadius, maxCount));
    }
    public HitResult raycastEntities(Level level, Vec3 start, Vec3 end, Boolf<Entity> entityFilter, float lookupEntityRadius) {
        return raycast(level, new Context(start, end, entityFilter, lookupEntityRadius, -1));
    }
    public HitResult raycastEntityBlocking(Level level, Vec3 start, Vec3 end, Boolf<Entity> entityFilter, float lookupEntityRadius) {
        var ctx = new Context(start, end, entityFilter, lookupEntityRadius).withBlockfilter((w, b) -> !w.getBlockState(b).isAir());
        return raycast(level, ctx);
    }
    public HitResult raycastEntitiesBlocking(Level level, Vec3 start, Vec3 end, Boolf<Entity> entityFilter, float lookupEntityRadius, int maxCount) {
        var ctx = new Context(start, end, entityFilter, lookupEntityRadius, maxCount).withBlockfilter((w, b) -> !w.getBlockState(b).isAir());
        return raycast(level, ctx);
    }
    public HitResult raycastEntitiesBlocking(Level level, Vec3 start, Vec3 end, Boolf<Entity> entityFilter, float lookupEntityRadius) {
        var ctx = new Context(start, end, entityFilter, lookupEntityRadius, -1).withBlockfilter((w, b) -> !w.getBlockState(b).isAir());
        return raycast(level, ctx);
    }

    public record HitResult(Vec3 origin, Vec3 hit,
                            BlockPos block, Direction dir, boolean notAirBlock,
                            Seq<Entity> entities) {
        public HitResult(Vec3 originPos, Vec3 hitPos) {
            this(originPos, hitPos, BlockPos.ZERO, Direction.UP, false, Seq.empty());
        }
        public HitResult(Vec3 originPos, Vec3 hitPos, BlockPos blockPos, Direction direction, boolean isSolid) {
            this(originPos, hitPos, blockPos, direction, isSolid, Seq.empty());
        }
        public HitResult(Vec3 originPos, Vec3 hitPos, Seq<Entity> entities) {
            this(originPos, hitPos, BlockPos.ZERO, Direction.UP, false, entities);
        }
    }
    public record Context(Vec3 start, Vec3 end,
                          Func2<Level,BlockPos,Boolean> blockPosFilter, Boolf<FluidState> fluidFilter, Boolf<Entity> entityFilter,
                          boolean checkBlocks, boolean checkFluids, boolean checkEntities,
                          int maxEntities, float lookupEntityRadius, boolean stopOnMaxEntities,
                          Block clipShape, CollisionContext collisionCtx) {
        public Context(){
            this(
                    Vec3.zero(),Vec3.zero(),
                    (w,b) -> true, Fluid.NONE, e -> false,
                    true, false, false,
                    0,0,false,
                    Block.COLLIDER,
                    CollisionContext.empty()
            );
        }
        public Context(Vec3 start, Vec3 end) {
            this(start, end,
                    (w,b) -> true, f -> false, e -> false,
                    true, false, false,
                    0, 0, false,
                    Block.COLLIDER,
                    CollisionContext.empty());
        }
        public Context(Vec3 start, Vec3 end, Boolf<Entity> entityFilter, float lookupEntityRadius) {
            this(start, end,
                    (w,b) -> false, Fluid.NONE, entityFilter,
                    false, false, true,
                    1, lookupEntityRadius, true,
                    Block.COLLIDER,
                    CollisionContext.empty());
        }
        public Context(Vec3 start, Vec3 end, Boolf<Entity> entityFilter, float lookupEntityRadius, int maxEntities) {
            this(start, end,
                    (w,b) -> false, Fluid.NONE, entityFilter,
                    false, false, true,
                    maxEntities, lookupEntityRadius, maxEntities != -1,
                    Block.COLLIDER,
                    CollisionContext.empty());
        }
        public Context(Vec3 start, Vec3 end, Func2<Level,BlockPos,Boolean> blockFilter) {
            this(start, end,
                    blockFilter, Fluid.NONE, e -> false,
                    true, false, false,
                    0, 0f, false,
                    Block.COLLIDER,
                    CollisionContext.empty());
        }
        public VoxelShape blockShape(BlockState blockState, BlockGetter level, BlockPos pos){
            return clipShape.get(blockState, level, pos, collisionCtx);
        }

        public VoxelShape fluidShape(FluidState state, BlockGetter level, BlockPos pos){
            return fluidFilter.get(state) ? state.getShape(level, pos) : Shapes.empty();
        }
        public Context withBlockfilter(Func2<Level, BlockPos, Boolean> blockPosFilter) {
            return new Context(start, end, blockPosFilter, fluidFilter, entityFilter,
                    true, checkFluids, checkEntities,
                    maxEntities, lookupEntityRadius, stopOnMaxEntities,
                    clipShape, collisionCtx);
        }
        public Context withFluidFilter(Boolf<FluidState> fluidFilter) {
            return new Context(start, end, blockPosFilter, fluidFilter, entityFilter,
                    checkBlocks, true, checkEntities,
                    maxEntities, lookupEntityRadius, stopOnMaxEntities,
                    clipShape, collisionCtx);
        }
        public Context withEntityFilter(Boolf<Entity> entityFilter) {
            return new Context(start, end, blockPosFilter, fluidFilter, entityFilter,
                    checkBlocks, checkFluids, true,
                    maxEntities, lookupEntityRadius, stopOnMaxEntities,
                    clipShape, collisionCtx);
        }
    }



    public static class Block implements ShapeGetter{
        public static Block COLLIDER = new Block(BlockBehaviour.BlockStateBase::getCollisionShape);
        public static Block OUTLINE = new Block(BlockBehaviour.BlockStateBase::getShape);
        public static Block VISUAL = new Block(BlockBehaviour.BlockStateBase::getVisualShape);

        public final ShapeGetter shapeGetter;

        public Block(ShapeGetter shapeGetter){
            this.shapeGetter = shapeGetter;
        }

        public VoxelShape get(BlockState state, BlockGetter block, BlockPos pos, CollisionContext collisionContext){
            return this.shapeGetter.get(state, block, pos, collisionContext);
        }
    }
    public static class Fluid{
        public static Boolf<FluidState> NONE = (f) -> false;
        public static Boolf<FluidState> SOURCE_ONLY = (FluidState::isSource);
        public static Boolf<FluidState> ANY = (f) -> !f.isEmpty();
        public static Boolf<FluidState> WATER = (f) -> f.is(FluidTags.WATER);
    }
    public interface ShapeGetter{
        VoxelShape get(BlockState state, BlockGetter block, BlockPos pos, CollisionContext collisionContext);
    }
}
