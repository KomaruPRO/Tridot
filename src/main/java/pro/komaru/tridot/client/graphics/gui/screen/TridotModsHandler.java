package pro.komaru.tridot.client.graphics.gui.screen;

import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.*;
import net.minecraft.client.renderer.*;
import net.minecraft.resources.*;
import pro.komaru.tridot.TridotLibClient;
import pro.komaru.tridot.client.graphics.gui.components.TridotLogoRenderer;
import pro.komaru.tridot.core.config.ClientConfig;

import java.util.*;

public class TridotModsHandler{
    public static Map<String, TridotMod> mods = new HashMap<>();
    public static Map<String, TridotPanorama> panoramas = new HashMap<>();

    public static void registerMod(TridotMod mod){
        mods.put(mod.getId(), mod);
    }

    public static TridotMod getMod(String id){
        return mods.get(id);
    }

    public static List<TridotMod> getMods(){
        return mods.values().stream().toList();
    }

    public static void registerPanorama(TridotPanorama panorama){
        panoramas.put(panorama.getId(), panorama);
    }

    public static TridotPanorama getPanorama(String id){
        return panoramas.get(id);
    }

    public static List<TridotPanorama> getPanoramas(){
        return new ArrayList<>(panoramas.values());
    }

    public static List<TridotMod> getSortedMods(){
        List<TridotMod> sorted = new ArrayList<>();

        List<String> main = new ArrayList<>();
        List<String> sort = new ArrayList<>();

        for(String id : mods.keySet()){
            if(!id.equals(TridotLibClient.MOD_INSTANCE.getId())){
                if(getMod(id).getDev().equals("MaxBogomol")){
                    main.add(id);
                }else{
                    sort.add(id);
                }
            }
        }

        Collections.sort(main);
        Collections.sort(sort);

        sorted.add(TridotLibClient.MOD_INSTANCE);
        for(String id : main){
            sorted.add(getMod(id));
        }
        for(String id : sort){
            sorted.add(getMod(id));
        }

        return sorted;
    }

    public static List<TridotPanorama> getSortedPanoramas(){
        List<TridotPanorama> sorted = new ArrayList<>();
        List<TridotPanorama> panoramas = getPanoramas();
        List<TridotMod> sortedMods = getSortedMods();
        List<TridotPanorama> added = new ArrayList<>();

        for(TridotMod mod : sortedMods){
            List<TridotPanorama> modPanoramas = new ArrayList<>();
            List<Integer> sorts = new ArrayList<>();
            for(TridotPanorama panorama : panoramas){
                if(panorama.getMod() == mod){
                    sorts.add(panorama.getSort());
                    modPanoramas.add(panorama);
                    added.add(panorama);
                }
            }
            Collections.sort(sorts);
            for(int sort : sorts){
                for(TridotPanorama panorama : modPanoramas){
                    if(panorama.getSort() == sort){
                        sorted.add(panorama);
                    }
                }
            }
        }
        sorted.add(0, TridotLibClient.VANILLA_PANORAMA);
        for(TridotPanorama panorama : panoramas){
            if(!added.contains(panorama)){
                if(panorama != TridotLibClient.VANILLA_PANORAMA){
                    sorted.add(panorama);
                }
            }
        }

        return sorted;
    }

    public static TridotPanorama getPanorama(){
        return getPanorama(ClientConfig.PANORAMA.get());
    }

    public static void setPanorama(TridotPanorama panorama){
        ClientConfig.PANORAMA.set(panorama.getId());
    }

    public static void setOpenPanorama(TitleScreen titleScreen, TridotPanorama panorama){
        float spin = titleScreen.panorama.spin;
        float bob = titleScreen.panorama.bob;
        ResourceLocation base = new ResourceLocation("textures/gui/title/background/panorama");
        ResourceLocation overlay = new ResourceLocation("textures/gui/title/background/panorama_overlay.png");
        if(panorama.getTexture() != null){
            base = panorama.getTexture();
        }
        TitleScreen.CUBE_MAP = new CubeMap(base);
        titleScreen.panorama = new PanoramaRenderer(TitleScreen.CUBE_MAP);
        TitleScreen.PANORAMA_OVERLAY = overlay;
        if(panorama.getLogo() != null){
            titleScreen.logoRenderer = new TridotLogoRenderer(panorama.getLogo(), titleScreen.logoRenderer.keepLogoThroughFade);
        }else{
            if(titleScreen.logoRenderer instanceof TridotLogoRenderer){
                titleScreen.logoRenderer = new LogoRenderer(titleScreen.logoRenderer.keepLogoThroughFade);
            }
        }
        titleScreen.panorama.spin = spin;
        titleScreen.panorama.bob = bob;
    }
}
