package art.arcane.thaumcraft.items.tools;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import art.arcane.thaumcraft.api.enums.InfusionEnchantments;
import art.arcane.thaumcraft.api.ThaumcraftMaterials;
import art.arcane.thaumcraft.api.components.InfusionEnchantmentComponent;
import art.arcane.thaumcraft.registries.ConfigItemComponents;

import java.util.Map;

public class ElementalPickaxeItem extends Item {

    public ElementalPickaxeItem(Properties props) {
        super(props
				.pickaxe(ThaumcraftMaterials.Tools.ELEMENTAL, ThaumcraftMaterials.Tools.ELEMENTAL.attackDamageBonus(), ThaumcraftMaterials.Tools.ELEMENTAL.speed())
				.rarity(Rarity.RARE)
				.component(
                        ConfigItemComponents.INFUSION_ENCHANTMENT.value(), new InfusionEnchantmentComponent(Map.of(
                                InfusionEnchantments.SOUNDING, (byte)2,
                                InfusionEnchantments.REFINING, (byte)1))));
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if(!player.level().isClientSide() && (!(entity instanceof Player) || player.level().getServer().overworld().isPvpAllowed()))
            entity.igniteForSeconds(2);
        return super.onLeftClickEntity(stack, player, entity);
    }
}
