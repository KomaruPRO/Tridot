package pro.komaru.tridot.util.math.raycast;

import net.minecraft.core.*;
import net.minecraft.world.entity.*;
import pro.komaru.tridot.util.phys.Vec3;

import java.util.*;

public class RayHitResult{
    public Vec3 pos;
    public Vec3 hitPos;
    public BlockPos blockPos = BlockPos.ZERO;
    public Direction direction = Direction.UP;
    public boolean block = false;
    public List<Entity> entities = new ArrayList<>();

    public RayHitResult(Vec3 pos){
        this.pos = pos;
        this.hitPos = pos;
    }

    public RayHitResult setPos(Vec3 pos){
        this.pos = pos;
        return this;
    }

    public RayHitResult setHitPos(Vec3 pos){
        this.hitPos = pos;
        return this;
    }

    public RayHitResult setBlockPos(BlockPos blockPos){
        this.blockPos = blockPos;
        return this;
    }

    public RayHitResult setDirection(Direction direction){
        this.direction = direction;
        return this;
    }

    public RayHitResult setBlock(boolean block){
        this.block = block;
        return this;
    }

    public RayHitResult setEntities(List<Entity> entities){
        this.entities = entities;
        return this;
    }

    public Vec3 getPos(){
        return pos;
    }

    public Vec3 getHitPos(){
        return hitPos;
    }

    public BlockPos getBlockPos(){
        return blockPos;
    }

    public Direction getDirection(){
        return direction;
    }

    public List<Entity> getEntities(){
        return entities;
    }

    public boolean hasBlock(){
        return block;
    }

    public boolean hasEntities(){
        return !entities.isEmpty();
    }
}
