package art.arcane.thaumcraft.items.tools;

import art.arcane.thaumcraft.registries.ConfigItemComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import net.minecraft.world.level.Level;
import art.arcane.thaumcraft.api.ThaumcraftMaterials;
import org.jspecify.annotations.Nullable;

public class VoidPickaxeItem extends Item {

    public VoidPickaxeItem(Properties props) {
        super(props
				.pickaxe(ThaumcraftMaterials.Tools.VOID, ThaumcraftMaterials.Tools.VOID.attackDamageBonus(), ThaumcraftMaterials.Tools.VOID.speed())
				.component(ConfigItemComponents.WARPING.value(), 1));
    }

	@Override
	public void inventoryTick(ItemStack pStack, ServerLevel level, Entity pEntity, @Nullable EquipmentSlot slot) {
		if(pEntity instanceof LivingEntity && pStack.isDamaged() && pEntity.tickCount % 20 == 0)
			pStack.setDamageValue(pStack.getDamageValue() - 1);
	}

	@Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if(!player.level().isClientSide()) {
            if(entity instanceof Player p && player.level().getServer().overworld().isPvpAllowed()) {
                p.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80), player);
            }
        }
        return super.onLeftClickEntity(stack, player, entity);
    }
}
