package art.arcane.thaumcraft.api.capabilities;

import net.minecraft.resources.Identifier;

import java.util.Map;

public interface IInfusionAttachment {

    boolean hasEnchantment(Identifier id);

    int getEnchantmentLevel(Identifier id);

    Map<Identifier, Integer> enchantments();

    IInfusionAttachment addEnchantment(Identifier id, int level);

    boolean removeEnchantment(Identifier id);
}
