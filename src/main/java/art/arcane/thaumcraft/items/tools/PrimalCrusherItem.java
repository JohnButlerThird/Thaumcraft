package art.arcane.thaumcraft.items.tools;

import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import art.arcane.thaumcraft.api.enums.InfusionEnchantments;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.api.components.InfusionEnchantmentComponent;
import art.arcane.thaumcraft.registries.ConfigItemComponents;
import org.jspecify.annotations.Nullable;

import java.util.Map;

public class PrimalCrusherItem extends Item {

    private static final ToolMaterial TIER = new ToolMaterial(ThaumcraftData.Tags.NOT_MINEABLE_WITH_CRUSHER, 500, 8.0F, 4.0F, 20, TagKey.create(Registries.ITEM, ThaumcraftData.Items.INGOT_VOID));
    
    public PrimalCrusherItem(Properties props) {
        super(props
				.tool(TIER, ThaumcraftData.Tags.MINEABLE_WITH_CRUSHER, TIER.attackDamageBonus(), TIER.speed(), 0)
				.durability(TIER.durability())
				.component(
                        ConfigItemComponents.INFUSION_ENCHANTMENT.value(), new InfusionEnchantmentComponent(Map.of(
                                InfusionEnchantments.REFINING, (byte)1,
                                InfusionEnchantments.DESTRUCTIVE, (byte)1)))
                        .component(ConfigItemComponents.WARPING.value(), 2));
    }

    //TODO: Maybe - if not in tags, take default break time from block state
	@Override
	public void inventoryTick(ItemStack pStack, ServerLevel level, Entity pEntity, @Nullable EquipmentSlot slot) {
		if(pEntity instanceof LivingEntity && pStack.isDamaged() && pEntity.tickCount % 20 == 0)
			pStack.setDamageValue(pStack.getDamageValue() - 1);
	}
}
