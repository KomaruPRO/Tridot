package pro.komaru.tridot.client.gfx.particle;

import net.minecraftforge.api.distmarker.*;
import pro.komaru.tridot.client.gfx.particle.data.*;
import net.minecraft.client.*;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.*;
import net.minecraft.core.particles.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;
import net.minecraftforge.registries.*;
import org.joml.Math;
import pro.komaru.tridot.client.gfx.particle.behavior.ParticleBehavior;
import pro.komaru.tridot.client.gfx.particle.options.GenericParticleOptions;
import pro.komaru.tridot.util.*;
import pro.komaru.tridot.util.math.*;

import java.util.*;
import java.util.function.*;

public class ParticleBuilder extends AbstractParticleBuilder<ParticleBuilder>{
    public static final ArcRandom random = Tmp.rnd;
    public final GenericParticleOptions options;
    boolean force = true;
    boolean distanceSpawn = true;
    double distance = 100;

    public Collection<ParticleBuilder> additionalBuilders = new ArrayList<>();

    protected ParticleBuilder(GenericParticleOptions options){
        this.options = options;
    }

    public GenericParticleOptions getParticleOptions(){
        return options;
    }

    public ParticleBuilder setRenderType(RenderType renderType){
        options.renderType = renderType;
        return this;
    }

    public ParticleBuilder setParticleRenderType(ParticleRenderType particleRenderType){
        options.renderType = null;
        options.particleRenderType = particleRenderType;
        return this;
    }

    public ParticleBuilder setBehavior(ParticleBehavior behavior){
        options.behavior = behavior;
        return this;
    }

    public ParticleBuilder setColorData(ColorParticleData colorData){
        options.colorData = colorData;
        return this;
    }

    public ParticleBuilder setTransparencyData(GenericParticleData transparencyData){
        options.transparencyData = transparencyData;
        return this;
    }

    public ParticleBuilder setScaleData(GenericParticleData scaleData){
        options.scaleData = scaleData;
        return this;
    }

    public ParticleBuilder setSpinData(SpinParticleData spinData){
        options.spinData = spinData;
        return this;
    }

    public ParticleBuilder setLightData(LightParticleData lightData){
        options.lightData = lightData;
        return this;
    }

    public ParticleBuilder setSpriteData(SpriteParticleData spriteData){
        options.spriteData = spriteData;
        return this;
    }

    public ParticleBuilder addTickActor(Consumer<GenericParticle> particleActor){
        getParticleOptions().tickActors.add(particleActor);
        return this;
    }

    public ParticleBuilder addSpawnActor(Consumer<GenericParticle> particleActor){
        getParticleOptions().spawnActors.add(particleActor);
        return this;
    }

    public ParticleBuilder addRenderActor(Consumer<GenericParticle> particleActor){
        getParticleOptions().renderActors.add(particleActor);
        return this;
    }

    public ParticleBuilder clearActors(){
        return clearTickActor().clearSpawnActors().clearRenderActors();
    }

    public ParticleBuilder clearTickActor(){
        getParticleOptions().tickActors.clear();
        return this;
    }

    public ParticleBuilder clearSpawnActors(){
        getParticleOptions().spawnActors.clear();
        return this;
    }

    public ParticleBuilder clearRenderActors(){
        getParticleOptions().renderActors.clear();
        return this;
    }

    public ParticleBuilder setDiscardFunction(GenericParticleOptions.DiscardFunctionType discardFunctionType){
        options.discardFunctionType = discardFunctionType;
        return this;
    }

    public ParticleBuilder setLifetime(int lifetime){
        options.lifetime = lifetime;
        return this;
    }

    public ParticleBuilder setLifetime(int lifetime, int additionalLifetime){
        options.lifetime = lifetime;
        options.additionalLifetime = additionalLifetime;
        return this;
    }

    public ParticleBuilder setGravity(float gravity){
        options.gravity = gravity;
        return this;
    }

    public ParticleBuilder setGravity(float gravity, float additionalGravity){
        options.gravity = gravity;
        options.additionalGravity = additionalGravity;
        return this;
    }

    public ParticleBuilder setFriction(float friction){
        options.friction = friction;
        return this;
    }

    public ParticleBuilder setFriction(float friction, float additionalFriction){
        options.friction = friction;
        options.additionalFriction = additionalFriction;
        return this;
    }

    public ParticleBuilder enableCull(){
        return setShouldCull(true);
    }

    public ParticleBuilder disableCull(){
        return setShouldCull(false);
    }

    public ParticleBuilder setShouldCull(boolean shouldCull){
        options.shouldCull = shouldCull;
        return this;
    }

    public ParticleBuilder enableRenderTraits(){
        return setShouldRenderTraits(true);
    }

    public ParticleBuilder disableRenderTraits(){
        return setShouldRenderTraits(false);
    }

    public ParticleBuilder setShouldRenderTraits(boolean shouldRenderTraits){
        options.shouldRenderTraits = shouldRenderTraits;
        return this;
    }

    public ParticleBuilder enablePhysics(){
        return setHasPhysics(true);
    }

    public ParticleBuilder disablePhysics(){
        return setHasPhysics(false);
    }

    public ParticleBuilder setHasPhysics(boolean hasPhysics){
        options.hasPhysics = hasPhysics;
        return this;
    }

    public ParticleBuilder flatRandomOffset(double fdx, double fdy, double fdz){
        this.fdx = fdx;
        this.fdy = fdy;
        this.fdz = fdz;
        return this;
    }

    public ParticleBuilder enableForce(){
        return setForce(true);
    }

    public ParticleBuilder disableForce(){
        return setForce(false);
    }

    public ParticleBuilder setForce(boolean force){
        this.force = force;
        return this;
    }

    public ParticleBuilder setDistance(double distance){
        this.distance = distance;
        return this;
    }

    public ParticleBuilder enableDistanceSpawn(){
        return setDistanceSpawn(true);
    }

    public ParticleBuilder disableDistanceSpawn(){
        return setDistanceSpawn(false);
    }

    public ParticleBuilder setDistanceSpawn(boolean distanceSpawn){
        this.distanceSpawn = distanceSpawn;
        return this;
    }

    public ParticleBuilder addAdditionalBuilder(ParticleBuilder builder){
        additionalBuilders.add(builder);
        return this;
    }

    public ParticleBuilder clearAdditionalBuilders(){
        additionalBuilders.clear();
        return this;
    }

    public ParticleBuilder spawn(Level level, Vec3 pos){
        double yaw = random.nextFloat() * Math.PI * 2, pitch = random.nextFloat() * Math.PI - Math.PI / 2,
        xSpeed = random.nextFloat() * maxXSpeed, ySpeed = random.nextFloat() * maxYSpeed, zSpeed = random.nextFloat() * maxZSpeed;
        double vx = this.vx + Math.sin(yaw) * Math.cos(pitch) * xSpeed;
        double vy = this.vy + Math.sin(pitch) * ySpeed;
        double vz = this.vz + Math.cos(yaw) * Math.cos(pitch) * zSpeed;
        double yaw2 = random.nextFloat() * Math.PI * 2, pitch2 = random.nextFloat() * Math.PI - Math.PI / 2,
        xDist = random.nextGaussian() * maxXDist, yDist = random.nextGaussian() * maxYDist, zDist = random.nextGaussian() * maxZDist;
        double dx = Math.sin(yaw2) * Math.cos(pitch2) * xDist;
        double dy = Math.sin(pitch2) * yDist;
        double dz = Math.cos(yaw2) * Math.cos(pitch2) * zDist;
        double fdx = (random.nextFloat() * 2 - 1f) * this.fdx;
        double fdy = (random.nextFloat() * 2 - 1f) * this.fdy;
        double fdz = (random.nextFloat() * 2 - 1f) * this.fdz;
        double fvx = (random.nextFloat() * 2 - 1f) * this.fvx;
        double fvy = (random.nextFloat() * 2 - 1f) * this.fvy;
        double fvz = (random.nextFloat() * 2 - 1f) * this.fvz;

        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        double xx = pos.x() + dx + fdx;
        double yy = pos.y() + dy + fdy;
        double zz = pos.z() + dz + fdz;
        if(!distanceSpawn || Math.sqrt(camera.getPosition().distanceToSqr(xx, yy, zz)) <= distance){
            level.addParticle(options, force, xx, yy, zz, vx + fvx, vy + fvy, vz + fvz);
            for(ParticleBuilder builder : additionalBuilders){
                level.addParticle(builder.getParticleOptions(), builder.force, xx, yy, zz, vx + fvx, vy + fvy, vz + fvz);
            }
        }

        return this;
    }

    public ParticleBuilder repeat(Level level, Vec3 pos, int count){
        for(int i = 0; i < count; i++) spawn(level, pos);
        return this;
    }

    public ParticleBuilder repeat(Level level, Vec3 pos, int count, float chance){
        for(int i = 0; i < count; i++){
            if(random.nextFloat() < chance) spawn(level, pos);
        }
        return this;
    }

    public ParticleBuilder spawn(Level level, double x, double y, double z){
        return spawn(level, new Vec3(x, y, z));
    }

    public ParticleBuilder repeat(Level level, double x, double y, double z, int n){
        return repeat(level, new Vec3(x, y, z), n);
    }

    public ParticleBuilder repeat(Level level, double x, double y, double z, int n, float chance){
        return repeat(level, new Vec3(x, y, z), n, chance);
    }

    public ParticleBuilder spawnLine(Level level, Vec3 from, Vec3 to){
        Vec3 pos = from.lerp(to, random.nextFloat());
        spawn(level, pos);
        return this;
    }

    public ParticleBuilder repeatLine(Level level, Vec3 from, Vec3 to, int n){
        for(int i = 0; i < n; i++) spawnLine(level, from, to);
        return this;
    }

    public ParticleBuilder repeatLine(Level level, Vec3 from, Vec3 to, int n, float chance){
        for(int i = 0; i < n; i++){
            if(random.nextFloat() < chance) spawnLine(level, from, to);
        }
        return this;
    }

    public ParticleBuilder spawnVoxelShape(Level level, Vec3 pos, VoxelShape voxelShape, int n, float chance){
        voxelShape.forAllBoxes(
        (x1, y1, z1, x2, y2, z2) -> {
            Vec3 v = pos;
            Vec3 b = pos.add(x1, y1, z1);
            Vec3 e = pos.add(x2, y2, z2);
            repeatLine(level, b, v.add(x2, y1, z1), n, chance);
            repeatLine(level, b, v.add(x1, y2, z1), n, chance);
            repeatLine(level, b, v.add(x1, y1, z2), n, chance);
            repeatLine(level, v.add(x1, y2, z1), v.add(x2, y2, z1), n, chance);
            repeatLine(level, v.add(x1, y2, z1), v.add(x1, y2, z2), n, chance);
            repeatLine(level, e, v.add(x2, y2, z1), n, chance);
            repeatLine(level, e, v.add(x1, y2, z2), n, chance);
            repeatLine(level, e, v.add(x2, y1, z2), n, chance);
            repeatLine(level, v.add(x2, y1, z1), v.add(x2, y1, z2), n, chance);
            repeatLine(level, v.add(x1, y1, z2), v.add(x2, y1, z2), n, chance);
            repeatLine(level, v.add(x2, y1, z1), v.add(x2, y2, z1), n, chance);
            repeatLine(level, v.add(x1, y1, z2), v.add(x1, y2, z2), n, chance);
        }
        );
        return this;
    }

    public ParticleBuilder spawnVoxelShape(Level level, Vec3 pos, VoxelShape voxelShape, int n){
        return spawnVoxelShape(level, pos, voxelShape, n, 1);
    }

    public ParticleBuilder spawnVoxelShape(Level level, Vec3 pos, VoxelShape voxelShape){
        return spawnVoxelShape(level, pos, voxelShape, 5, 1);
    }

    public ParticleBuilder spawnVoxelShape(Level level, double x, double y, double z, VoxelShape voxelShape, int n, float chance){
        return spawnVoxelShape(level, new Vec3(x, y, z), voxelShape, n, chance);
    }

    public ParticleBuilder spawnVoxelShape(Level level, double x, double y, double z, VoxelShape voxelShape, int n){
        return spawnVoxelShape(level, new Vec3(x, y, z), voxelShape, n, 1);
    }

    public ParticleBuilder spawnVoxelShape(Level level, double x, double y, double z, VoxelShape voxelShape){
        return spawnVoxelShape(level, new Vec3(x, y, z), voxelShape, 5, 1);
    }

    public static ParticleBuilder create(ParticleType<?> type){
        return new ParticleBuilder(new GenericParticleOptions(type));
    }

    public static ParticleBuilder create(RegistryObject<?> type){
        return new ParticleBuilder(new GenericParticleOptions((ParticleType<?>)type.get()));
    }

    public static ParticleBuilder create(GenericParticleOptions options){
        return new ParticleBuilder(options);
    }
}
