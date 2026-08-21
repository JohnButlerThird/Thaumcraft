package art.arcane.thaumcraft.data.attachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import lombok.AllArgsConstructor;
import net.minecraft.resources.Identifier;
import art.arcane.thaumcraft.api.capabilities.IInfusionAttachment;

import java.util.Map;

public record InfusionAttachment(Map<Identifier, Integer> enchantments) implements IInfusionAttachment {

    @Override
    public boolean hasEnchantment(Identifier id) {
        return getEnchantmentLevel(id) > 0;
    }

    @Override
    public int getEnchantmentLevel(Identifier id) {
        return enchantments.getOrDefault(id, 0);
    }

    @Override
    public IInfusionAttachment addEnchantment(Identifier id, int level) {
        enchantments.put(id, level);
        return this;
    }

    @Override
    public boolean removeEnchantment(Identifier id) {
        return enchantments.remove(id) != null;
    }

    public static final MapCodec<InfusionAttachment> CODEC = Codec.unboundedMap(Identifier.CODEC, Codec.INT).xmap(InfusionAttachment::new, a -> a.enchantments).fieldOf("infusions");
}
