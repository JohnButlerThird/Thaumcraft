package art.arcane.thaumcraft.items.tools;

import art.arcane.thaumcraft.api.ThaumcraftMaterials;
import art.arcane.thaumcraft.registries.ConfigItemComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import art.arcane.thaumcraft.api.ThaumcraftData;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class CrimsonBladeItem extends Item {

    private static final String COMPONENT_GREATER_SAP = "enchantment.thaumcraft.special.sapgreat";

    private static final ToolMaterial CRIMSON_VOID = new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 200, 8.0F, 3.5F, 20, TagKey.create(Registries.ITEM, ThaumcraftData.Items.INGOT_VOID));
    
    public CrimsonBladeItem(Properties props) {
        super(props
				.sword(CRIMSON_VOID, CRIMSON_VOID.attackDamageBonus(), CRIMSON_VOID.speed())
                .durability(CRIMSON_VOID.durability())
                .rarity(Rarity.EPIC)
                .component(ConfigItemComponents.WARPING.value(), 2));
    }

	@Override
	public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
		builder.accept(Component.translatable(COMPONENT_GREATER_SAP).withStyle(ChatFormatting.GOLD));
	}

	@Override
	public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
		if(owner instanceof LivingEntity && itemStack.isDamaged() && owner.tickCount % 20 == 0)
			itemStack.setDamageValue(itemStack.getDamageValue() - 1);
	}

	@Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if(!player.level().isClientSide()) {
            if(entity instanceof Player p && player.level().getServer().overworld().isPvpAllowed()) {
                p.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60), player);
                p.addEffect(new MobEffectInstance(MobEffects.HUNGER, 120), player);
            }
        }
        return super.onLeftClickEntity(stack, player, entity);
    }
    
    public int getWarp(ItemStack itemstack, Player player) {
        return 2;
    }
}
