package pro.komaru.tridot.client.graphics.shader.postprocess;

import com.mojang.blaze3d.preprocessor.*;
import pro.komaru.tridot.*;
import net.minecraft.client.*;
import net.minecraft.resources.*;
import net.minecraft.server.packs.resources.*;
import org.apache.commons.io.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.nio.charset.*;

public class TridotGlslPreprocessor extends GlslPreprocessor{

    public static final TridotGlslPreprocessor PREPROCESSOR = new TridotGlslPreprocessor();

    @Nullable
    @Override
    public String applyImport(boolean useFullPath, String directory){
        ResourceLocation resourcelocation = new ResourceLocation(directory);
        ResourceLocation resourcelocation1 = new ResourceLocation(resourcelocation.getNamespace(), "shaders/include/" + resourcelocation.getPath());
        try{
            Resource resource1 = Minecraft.getInstance().getResourceManager().getResource(resourcelocation1).get();
            return IOUtils.toString(resource1.open(), StandardCharsets.UTF_8);
        }catch(IOException ioexception){
            Log.error("Could not open GLSL import {}: {}", directory, ioexception.getMessage());
            return "#error " + ioexception.getMessage();
        }
    }
}
