package github.iri.tridot.client.sound;

import github.iri.tridot.client.gui.screen.*;
import net.minecraft.client.*;
import net.minecraft.core.*;
import net.minecraft.sounds.*;
import net.minecraft.tags.*;
import net.minecraft.world.level.biome.*;

public class MusicModifier{

    public boolean isCanPlay(Music defaultMisic, Minecraft minecraft){
        return true;
    }

    public Music play(Music defaultMisic, Minecraft minecraft){
        return null;
    }

    public boolean isMenu(Music defaultMisic, Minecraft minecraft){
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

    public static class Panorama extends MusicModifier{
        public Music music;
        public TridotPanorama panorama;

        public Panorama(Music music, TridotPanorama panorama){
            this.music = music;
            this.panorama = panorama;
        }

        @Override
        public boolean isCanPlay(Music defaultMisic, Minecraft minecraft){
            TridotPanorama panorama = TridotModsHandler.getPanorama();
            return panorama != null && panorama == this.panorama;
        }

        @Override
        public Music play(Music defaultMisic, Minecraft minecraft){
            return music;
        }

        @Override
        public boolean isMenu(Music defaultMisic, Minecraft minecraft){
            return true;
        }
    }
}
