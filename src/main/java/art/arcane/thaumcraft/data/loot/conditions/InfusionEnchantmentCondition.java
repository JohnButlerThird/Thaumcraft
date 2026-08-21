package art.arcane.thaumcraft.data.loot.conditions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import art.arcane.thaumcraft.api.enums.InfusionEnchantments;

@AllArgsConstructor
public class InfusionEnchantmentCondition implements LootItemCondition {

    public static final MapCodec<InfusionEnchantmentCondition> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            Identifier.CODEC.fieldOf("enchantment").forGetter(c -> c.enchantmentId)
    ).apply(i, InfusionEnchantmentCondition::new));

    private final Identifier enchantmentId;

    @Override
    public MapCodec<? extends LootItemCondition> codec() {
        return CODEC;
    }

    @Override
    public boolean test(LootContext lootContext) {
        ItemInstance tool;
        if(lootContext.hasParameter(LootContextParams.BLOCK_STATE)) {
            if(!lootContext.hasParameter(LootContextParams.THIS_ENTITY) || !(lootContext.getParameter(LootContextParams.THIS_ENTITY) instanceof Player))
                return false;
            tool = lootContext.getParameter(LootContextParams.TOOL);
        } else {
            Entity e = lootContext.getOptionalParameter(LootContextParams.ATTACKING_ENTITY);
            if(e instanceof Player p)
                tool = p.getMainHandItem();
            else
                return false;
        }
        return InfusionEnchantments.hasEnchantment(tool, InfusionEnchantments.getFromId(enchantmentId));
    }
}
