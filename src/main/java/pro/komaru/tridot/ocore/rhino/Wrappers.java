package pro.komaru.tridot.ocore.rhino;

import com.mojang.brigadier.exceptions.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraftforge.registries.*;
import pro.komaru.tridot.ocore.math.*;
import pro.komaru.tridot.ocore.struct.Structs;
import pro.komaru.tridot.ocore.struct.data.Pair;
import pro.komaru.tridot.ocore.struct.func.Prov;
import pro.komaru.tridot.rhino.*;
import pro.komaru.tridot.rhino.util.wrap.*;
import pro.komaru.tridot.outilities.*;

import java.util.*;
import java.util.function.*;

public class Wrappers {
    public static void register(TypeWrappers wraps) {
        wraps.register(Vec3.class,(ctx, obj) -> {
            if(obj instanceof Vec3 vec) return vec;
            if(obj instanceof Entity e) return Vec3.from(e);
            if(obj instanceof NativeArray d) return new Vec3((float) (double) (Double) d.get(0),(float) (double) (Double) d.get(1),(float) (double) (Double) d.get(2));
            return null;
        });
        wraps.register(Component.class,(ctx, obj) -> {
            if(obj instanceof Component c) return c;
            if(obj instanceof String a) return Component.translatable(a);
            return Component.literal(String.valueOf(obj));
        });
        wraps.register(UUID.class,(ctx, obj) -> obj == null ? null : UUID.fromString(String.valueOf(obj)));
        wraps.register(BlockPos.class,(ctx, obj) -> {
            if(obj instanceof BlockPos pos) return pos;
            if(obj instanceof Vec3 vec) return BlockPos.containing(vec.x,vec.y,vec.z);
            if(obj instanceof Entity e) return e.getOnPos();
            if(obj instanceof NativeArray d) return BlockPos.containing((Double) d.get(0),(Double) d.get(1),(Double) d.get(2));
            return null;
        });
        wraps.register(Player.class,(ctx,obj) -> {
            if(obj == null) return Utils.players().first();
            if(obj instanceof Player p) return p;
            if(obj instanceof String str) return Utils.players().find(e -> e.getName().getString().equals(str));
            if(obj instanceof Prov<?> prov) return (Player) prov.get();
            if(obj instanceof Supplier<?> supp) return (Player) supp.get();
            return null;
        });
        wraps.register(ServerPlayer.class,(ctx, obj) -> {
            if(obj == null) return Utils.players().first();
            if(obj instanceof ServerPlayer p) return p;
            if(obj instanceof String str) return Utils.players().find(e -> e.getName().getString().equals(str));
            if(obj instanceof Prov<?> prov) return (ServerPlayer) prov.get();
            if(obj instanceof Supplier<?> supp) return (ServerPlayer) supp.get();
            return null;
        });
        wraps.register(ItemStack.class,(ctx, obj) -> {
            if(obj instanceof ItemStack stack) return stack;
            if(obj instanceof String str) {
                if(ForgeRegistries.ITEMS.getValue(ResourceLocation.of(str, ':')) == null) return null;
                return new ItemStack(ForgeRegistries.ITEMS.getValue(ResourceLocation.of(str, ':')));
            }
            if(obj instanceof Pair pair && pair.first() instanceof String s && pair.second() instanceof Integer i) {
                if(ForgeRegistries.ITEMS.getValue(ResourceLocation.of(s, ':')) == null) return null;
                return new ItemStack(ForgeRegistries.ITEMS.getValue(ResourceLocation.of(s, ':')),i);
            }
            return null;
        });
        wraps.register(Item.class,(ctx,obj) -> {
            if(obj instanceof Item i) return i;
            if(obj instanceof String str)
                return ForgeRegistries.ITEMS.getValue(ResourceLocation.of(str, ':'));
            return null;
        });
        wraps.register(Block.class,(ctx, obj) -> {
            if(obj instanceof Block b) return b;
            if(obj instanceof String str)
                return ForgeRegistries.BLOCKS.getValue(ResourceLocation.of(str, ':'));
            return null;
        });
        wraps.register(ResourceLocation.class,(ctx,obj) -> {
            if(obj instanceof ResourceLocation res) return res;
            if(obj instanceof String str) return new ResourceLocation(str);
            if(obj instanceof Pair p && p.first() instanceof String s1 && p.second() instanceof String s2)
                return new ResourceLocation(s1,s2);
            return null;
        });
        wraps.register(CompoundTag.class,(ctx, obj) -> {
            if (obj instanceof CompoundTag nbt) return nbt;
            else if (obj instanceof CharSequence c) try {
                    return TagParser.parseTag(c.toString());
                } catch (Exception ex) {
                    return null;
                }
            HashMap<String,Object> tag =
                    Structs.map(String.class,Object.class,(Object[]) obj);
            CompoundTag nbt = new CompoundTag();
            tag.forEach((k,v) -> {
                try {
                    nbt.put(k, TagParser.parseTag(String.valueOf(v)));
                } catch (CommandSyntaxException ignored) {

                }
            });
            return nbt;
        });
    }
}
