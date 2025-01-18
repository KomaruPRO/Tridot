package pro.komaru.tridot.utilities.client;

import net.minecraft.core.particles.*;
import net.minecraft.server.level.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import org.joml.*;

import java.lang.Math;
import java.util.Random;
import java.util.*;

public class ParticleUtils{
    /**
     * Spawns particles line
     * @param pType Particle that will spawn line
     * @param pFrom Position From
     * @param pTo Position To
     */
    public static void line(Level pLevel, Vec3 pFrom, Vec3 pTo, ParticleOptions pType){
        double distance = pFrom.distanceTo(pTo);
        double distanceInBlocks = Math.floor(distance);
        for(int i = 0; i < distanceInBlocks; i++){
            double dX = pFrom.x - pTo.x;
            double dY = pFrom.y - pTo.y;
            double dZ = pFrom.z - pTo.z;
            float x = (float)(dX / distanceInBlocks);
            float y = (float)(dY / distanceInBlocks);
            float z = (float)(dZ / distanceInBlocks);

            if(!pLevel.isClientSide() && pLevel instanceof ServerLevel pServer){
                pServer.sendParticles(pType, pFrom.x - (x * i), pFrom.y - (y * i), pFrom.z - (z * i), 1, 0, 0, 0, 0);
            }
        }
    }

    /**
     * Spawns particle lines to nearby Mobs
     * @param pPlayer Player for reciving pos from
     * @param pType Particle type to spawn
     * @param hitEntities list of Entities
     * @param pos Position in Vec3
     * @param radius Radius to spawn
     */
    public static void lineToNearbyMobs(Level pLevel, Player pPlayer, ParticleOptions pType, List<LivingEntity> hitEntities, Vec3 pos, float pitchRaw, float yawRaw, float radius){
        double pitch = ((pitchRaw + 90) * Math.PI) / 180;
        double yaw = ((yawRaw + 90) * Math.PI) / 180;

        double X = Math.sin(pitch) * Math.cos(yaw) * radius;
        double Y = Math.cos(pitch) * radius;
        double Z = Math.sin(pitch) * Math.sin(yaw) * radius;
        AABB boundingBox = new AABB(pos.x, pos.y - 8 + ((Math.random() - 0.5D) * 0.2F), pos.z, pos.x + X, pos.y + Y + ((Math.random() - 0.5D) * 0.2F), pos.z + Z);
        List<Entity> entities = pLevel.getEntitiesOfClass(Entity.class, boundingBox);
        for(Entity entity : entities){
            if(entity instanceof LivingEntity livingEntity && !hitEntities.contains(livingEntity) && !livingEntity.equals(pPlayer)){
                hitEntities.add(livingEntity);
                if(!livingEntity.isAlive()){
                    return;
                }

                Vec3 pTo = new Vec3(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
                double distance = pos.distanceTo(pTo);
                double distanceInBlocks = Math.floor(distance);
                for(int i = 0; i < distanceInBlocks; i++){
                    double dX = pos.x - pTo.x;
                    double dY = pos.y - pTo.y;
                    double dZ = pos.z - pTo.z;

                    float x = (float)(dX / distanceInBlocks);
                    float y = (float)(dY / distanceInBlocks);
                    float z = (float)(dZ / distanceInBlocks);

                    if(!pLevel.isClientSide() && pLevel instanceof ServerLevel pServer){
                        pServer.sendParticles(pType, pos.x - (x * i), pos.y - (y * i), pos.z - (z * i), 1, 0, 0, 0, 0);
                    }
                }

                for(int i = 0; i < 3; i++){
                    pLevel.addParticle(ParticleTypes.CLOUD, pos.x, pos.y, pos.z, 0, 0, 0);
                }
            }
        }
    }


    /**
     * Spawns particles line to attacked mob position
     * @param pPlayer Player pos for calculating Attacked mob and positions
     * @param pType Particle that will spawn line
     * @param pDuration cooldown
     */
    public static void attackedMobLineDelayed(Level pLevel, Player pPlayer, ParticleOptions pType, int pDuration){
        LivingEntity lastHurtMob = pPlayer.getLastAttacker();
        if(!pLevel.isClientSide() && pLevel instanceof ServerLevel pServer){
            if(lastHurtMob == null){
                return;
            }

            Vec3 pos = new Vec3(pPlayer.getX(), pPlayer.getY() + 0.5f, pPlayer.getZ());
            Vec3 EndPos = new Vec3(lastHurtMob.getX(), lastHurtMob.getY() + 0.5f, lastHurtMob.getZ());
            double distance = pos.distanceTo(EndPos);
            double distanceInBlocks = Math.floor(distance);
            for(pDuration = 0; pDuration < distanceInBlocks; pDuration++){
                double dX = pos.x - EndPos.x;
                double dY = pos.y - EndPos.y;
                double dZ = pos.z - EndPos.z;
                float x = (float)(dX / distanceInBlocks);
                float y = (float)(dY / distanceInBlocks);
                float z = (float)(dZ / distanceInBlocks);

                pServer.sendParticles(pType, pos.x - (x * pDuration), pos.y - (y * pDuration), pos.z - (z * pDuration), 1, 0, 0, 0, 0);
            }
        }
    }

    /**
     * Spawns particles line to attacked mob position
     * @param pPlayer Player pos for calculating Attacked mob and positions
     * @param pType Particle that will spawn line
     */
    public static void attackedMobLine(Level pLevel, Player pPlayer, ParticleOptions pType){
        LivingEntity lastHurtMob = pPlayer.getLastAttacker();
        if(!pLevel.isClientSide() && pLevel instanceof ServerLevel pServer){
            if(lastHurtMob == null){
                return;
            }

            Vec3 pos = new Vec3(pPlayer.getX(), pPlayer.getY() + 0.5f, pPlayer.getZ());
            Vec3 EndPos = new Vec3(lastHurtMob.getX(), lastHurtMob.getY() + 0.5f, lastHurtMob.getZ());
            double distance = pos.distanceTo(EndPos);
            double distanceInBlocks = Math.floor(distance);
            for(int i = 0; i < distanceInBlocks; i++){
                double dX = pos.x - EndPos.x;
                double dY = pos.y - EndPos.y;
                double dZ = pos.z - EndPos.z;
                float x = (float)(dX / distanceInBlocks);
                float y = (float)(dY / distanceInBlocks);
                float z = (float)(dZ / distanceInBlocks);

                pServer.sendParticles(pType, pos.x - (x * i), pos.y - (y * i), pos.z - (z * i), 1, 0, 0, 0, 0);
            }
        }
    }

    /**
     * Spawns particles around position
     * @param distance Distance in blocks
     * @param options Particle that will spawn at radius
     * @param speed Speed of particles
     * @param pos Position
     */
    public static void surroundingParticles(Vector3d pos, float distance, float speed, Level level, ParticleOptions options){
        Random rand = new Random();
        RandomSource source = RandomSource.create();
        for(int i = 0; i < 360; i += 10){
            double X = ((rand.nextDouble() - 0.5D) * distance);
            double Y = ((rand.nextDouble() - 0.5D) * distance);
            double Z = ((rand.nextDouble() - 0.5D) * distance);

            double dX = -X;
            double dY = -Y;
            double dZ = -Z;
            if(!level.isClientSide() && level instanceof ServerLevel pServer){
                for(int ii = 0; ii < 1 + Mth.nextInt(source, 0, 2); ii += 1){
                    double yaw = Math.atan2(dZ, dX) + i;
                    double pitch = Math.atan2(Math.sqrt(dZ * dZ + dX * dX), dY) + Math.PI;
                    double XX = Math.sin(pitch) * Math.cos(yaw) * speed / (ii + 1);
                    double YY = Math.sin(pitch) * Math.sin(yaw) * speed / (ii + 1);
                    double ZZ = Math.cos(pitch) * speed / (ii + 1);

                    pServer.sendParticles(options, pos.x + X, pos.y + Y, pos.z + Z, 1, XX, YY, ZZ, 0);
                }
            }
        }
    }


    /**
     * @param hitEntities List for damaged entities
     * @param type Particle that will appear at marked mobs
     * @param pos Position
     * @param radius Distance in blocks
     */
    public static void particleMark(Level level, Player player, List<LivingEntity> hitEntities, ParticleOptions type, Vector3d pos, float pitchRaw, float yawRaw, float radius){
        for(int i = 0; i < 360; i += 10){
            double pitch = ((pitchRaw + 90) * Math.PI) / 180;
            double yaw = ((i + yawRaw + 90) * Math.PI) / 180;

            double X = Math.sin(pitch) * Math.cos(yaw) * radius;
            double Y = Math.cos(pitch) * radius;
            double Z = Math.sin(pitch) * Math.sin(yaw) * radius;

            AABB boundingBox = new AABB(pos.x, pos.y - 8 + ((Math.random() - 0.5D) * 0.2F), pos.z, pos.x + X, pos.y + Y + ((Math.random() - 0.5D) * 0.2F), pos.z + Z);
            List<Entity> entities = level.getEntitiesOfClass(Entity.class, boundingBox);
            for(Entity entity : entities){
                if(entity instanceof LivingEntity livingEntity && !hitEntities.contains(livingEntity) && !livingEntity.equals(player)){
                    hitEntities.add(livingEntity);
                    if(!livingEntity.isAlive()){
                        return;
                    }

                    if(!level.isClientSide() && level instanceof ServerLevel pServer){
                        pServer.sendParticles(type, livingEntity.getX(), livingEntity.getY() + 2 + ((Math.random() - 0.5D) * 0.2F), livingEntity.getZ(), 1, 0, 0, 0, 0);
                    }
                }
            }
        }
    }

    /**
     * Spawns particles in radius like in radiusHit
     * @param radius Distance in blocks
     * @param type Particle that will spawn at radius
     * @param pos Position
     */
    public static void particleRadius(Level level, ParticleOptions type, Vector3d pos, float pitchRaw, float yawRaw, float radius){
        for(int i = 0; i < 360; i += 10){
            double pitch = ((pitchRaw + 90) * Math.PI) / 180;
            double yaw = ((i + yawRaw + 90) * Math.PI) / 180;
            double X = Math.sin(pitch) * Math.cos(yaw) * radius * 0.75F;
            double Y = Math.cos(pitch) * radius * 0.75F;
            double Z = Math.sin(pitch) * Math.sin(yaw) * radius * 0.75F;
            if(!level.isClientSide() && level instanceof ServerLevel pServer){
                pServer.sendParticles(type, pos.x + X, pos.y + Y + ((Math.random() - 0.5D) * 0.2F), pos.z + Z, 1, 0, 0, 0, 0);
            }
        }
    }
}
