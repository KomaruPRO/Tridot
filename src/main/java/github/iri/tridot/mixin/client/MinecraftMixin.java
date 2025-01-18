package github.iri.tridot.mixin.client;

import github.iri.tridot.client.sound.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.screens.*;
import net.minecraft.sounds.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin{

    @Unique
    Random tridot$random = new Random();

    @Inject(method = "getSituationalMusic", at = @At("TAIL"), cancellable = true)
    private void tridot$getSituationalMusic(final CallbackInfoReturnable<Music> cir){
        Minecraft minecraft = Minecraft.getInstance();
        if(minecraft.screen instanceof WinScreen){
            return;
        }

        List<Music> possibleMusic = new ArrayList<>();
        List<Music> menuMusic = new ArrayList<>();
        Music defaultMusic = cir.getReturnValue();

        for(MusicModifier modifier : MusicHandler.getModifiers()){
            if(modifier.isCanPlay(defaultMusic, minecraft)){
                Music music = modifier.play(defaultMusic, minecraft);
                if(music != null){
                    if(modifier.isMenu(defaultMusic, minecraft)){
                        menuMusic.add(music);
                    }else{
                        possibleMusic.add(music);
                    }
                }
            }
        }

        if(menuMusic.size() > 0){
            if(minecraft.screen instanceof TitleScreen || defaultMusic == Musics.MENU){
                if(!menuMusic.contains(defaultMusic)){
                    cir.setReturnValue(menuMusic.get(tridot$random.nextInt(0, menuMusic.size())));
                }
            }
        }

        if(possibleMusic.size() > 0){
            cir.setReturnValue(possibleMusic.get(tridot$random.nextInt(0, possibleMusic.size())));
        }
    }
}
