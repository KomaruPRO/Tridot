package pro.komaru.tridot.client.sound;

import pro.komaru.tridot.client.gui.screen.*;
import net.minecraft.client.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.tags.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.structure.*;
import pro.komaru.tridot.client.gui.screen.TridotModsHandler;
import pro.komaru.tridot.client.gui.screen.TridotPanorama;

public class MusicModifier{

    public boolean isCanPlay(Music defaultMusic, Minecraft minecraft){
        return true;
    }

    public Music play(Music defaultMusic, Minecraft minecraft){
        return null;
    }

    public boolean isMenu(Music defaultMusic, Minecraft minecraft){
        return false;
    }

    public boolean isBiome(Biome biome, Minecraft minecraft){
        if(minecraft.player != null){
            Holder<Biome> holder = minecraft.player.level().getBiome(minecraft.player.blockPosition());
            return (holder.get().equals(biome));
        }
        return false;
    }

    public boolean isBiome(TagKey<Biome> biome, Minecraft minecraft){
        if(minecraft.player != null){
            Holder<Biome> holder = minecraft.player.level().getBiome(minecraft.player.blockPosition());
            return (holder.is(biome));
        }
        return false;
    }

    public static class Dungeon extends MusicModifier {
        public SoundEvent music;
        public ResourceKey<Structure> structureKey;

        public Dungeon(SoundEvent music, ResourceKey<Structure> structureKey){
            this.music = music;
            this.structureKey = structureKey;
        }

        public boolean isPlayerInStructure(Player player, ServerLevel serverLevel) {
            var structure = serverLevel.structureManager().getStructureWithPieceAt(
            player.blockPosition(), structureKey);
            return structure.getStructure() != null && structure.getBoundingBox().isInside(player.getBlockX(), player.getBlockY(), player.getBlockZ());
        }
    }

    public static class Panorama extends MusicModifier{
        public Music music;
        public TridotPanorama panorama;

        public Panorama(Music music, TridotPanorama panorama){
            this.music = music;
            this.panorama = panorama;
        }

        @Override
        public boolean isCanPlay(Music defaultMusic, Minecraft minecraft){
            TridotPanorama panorama = TridotModsHandler.getPanorama();
            return panorama != null && panorama == this.panorama;
        }

        @Override
        public Music play(Music defaultMusic, Minecraft minecraft){
            return music;
        }

        @Override
        public boolean isMenu(Music defaultMusic, Minecraft minecraft){
            return true;
        }
    }
}
