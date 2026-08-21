package art.arcane.thaumcraft.api.aspects;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.util.StringRepresentable;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.data.aspects.PrimalAspects;
import art.arcane.thaumcraft.registries.ConfigDataRegistries;
import art.arcane.thaumcraft.util.Colour;
import art.arcane.thaumcraft.util.FallbackHolder;

import java.util.ArrayList;
import java.util.List;

public record Aspect(Colour colour, List<ResourceKey<Aspect>> components) {

    public static final Aspect UNKNOWN = new Aspect(Colour.fromInteger(0xFFFFFF, false), new ArrayList<>());
    public static final Holder<Aspect> UNKNOWN_HOLDER = new FallbackHolder<>(ThaumcraftData.Aspects.UNKNOWN, UNKNOWN);

    public static Component getName(HolderLookup.Provider provider, ResourceKey<Aspect> key, boolean pureColor, boolean primalColor) {
        return getName(key == null ? null : ConfigDataRegistries.ASPECTS.getHolder(provider, key), pureColor, primalColor);
    }

    public static Component getName(Holder<Aspect> aspect, boolean pureColor, boolean primalColor) {
        if (aspect == null)
            return Component.translatable("aspect.thaumcraft.untyped");
        Identifier id = aspect.getKey().identifier();
        MutableComponent c = Component.translatable(id.toLanguageKey("aspect"));
        if (pureColor || primalColor) {
            Aspect a = aspect.value();
            if (pureColor)
                return c.setStyle(Style.EMPTY.withColor(a.colour().argb32(false)));
            else
                return c.withStyle(PrimalAspects.getPrimalFormatting(aspect.getKey()));
        }
        return c;
    }

    public static final MapCodec<Aspect> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Colour.CODEC.fieldOf("colour").forGetter(Aspect::colour),
            ResourceKey.codec(ThaumcraftData.Registries.ASPECT).listOf().optionalFieldOf("origin", new ArrayList<>()).forGetter(Aspect::components)).apply(i, Aspect::new));
    public static final RegistryFileCodec<Aspect> REGISTRY_CODEC = RegistryFileCodec.create(ThaumcraftData.Registries.ASPECT, CODEC.codec());

    public static final StreamCodec<RegistryFriendlyByteBuf, Aspect> STREAM_CODEC = StreamCodec.composite(
            Colour.STREAM_CODEC, Aspect::colour, ResourceKey.streamCodec(ThaumcraftData.Registries.ASPECT).apply(ByteBufCodecs.list()), Aspect::components, Aspect::new);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Aspect>> REGISTRY_STREAM_CODEC = ByteBufCodecs.holder(ThaumcraftData.Registries.ASPECT, STREAM_CODEC);

    @Getter
    @AllArgsConstructor
    public enum Primal implements StringRepresentable {
        CHAOS(ThaumcraftData.Aspects.CHAOS),
        ORDER(ThaumcraftData.Aspects.ORDER),
        WATER(ThaumcraftData.Aspects.WATER),
        AIR(ThaumcraftData.Aspects.AIR),
        FIRE(ThaumcraftData.Aspects.FIRE),
        EARTH(ThaumcraftData.Aspects.EARTH);

        private final ResourceKey<Aspect> id;

        @Override
        public String getSerializedName() {
            return id.identifier().getPath();
        }
    }
}
