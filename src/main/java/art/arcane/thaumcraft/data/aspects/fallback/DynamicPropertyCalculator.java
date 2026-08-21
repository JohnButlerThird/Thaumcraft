package art.arcane.thaumcraft.data.aspects.fallback;

import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.data.aspects.AspectList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.common.Tags;

public final class DynamicPropertyCalculator {

    public static AspectList calculate(ItemStack stack) {
        AspectList list = new AspectList();
        Item item = stack.getItem();

        ItemAttributeModifiers modifiers = stack.get(DataComponents.ATTRIBUTE_MODIFIERS);

        if (stack.is(Tags.Items.ARMORS)) {
            int defense = getAttributeValue(modifiers, Attributes.ARMOR);
            if (defense > 0)
                list.add(ThaumcraftData.Aspects.ARMOR, defense * 4);
        }

        if (stack.is(Tags.Items.MELEE_WEAPON_TOOLS) || stack.is(Tags.Items.RANGED_WEAPON_TOOLS)) {
            int damage = getAttributeValue(modifiers, Attributes.ATTACK_DAMAGE);
            if (damage > 0)
                list.add(ThaumcraftData.Aspects.AVERSION, damage * 3);
        }

        if (stack.is(Tags.Items.MINING_TOOL_TOOLS)) {
            int damage = getAttributeValue(modifiers, Attributes.ATTACK_DAMAGE);
            list.add(ThaumcraftData.Aspects.TOOL, Math.max(4, damage * 2));
        }

        if (item instanceof BowItem) {
            list.add(ThaumcraftData.Aspects.AVERSION, 10);
            list.add(ThaumcraftData.Aspects.FLIGHT, 5);
        }

        if (item instanceof CrossbowItem) {
            list.add(ThaumcraftData.Aspects.AVERSION, 15);
            list.add(ThaumcraftData.Aspects.FLIGHT, 5);
        }

        if (item instanceof TridentItem) {
            list.add(ThaumcraftData.Aspects.AVERSION, 12);
            list.add(ThaumcraftData.Aspects.WATER, 8);
        }

        if (item instanceof ShieldItem) {
            list.add(ThaumcraftData.Aspects.ARMOR, 10);
        }

        FoodProperties food = stack.get(DataComponents.FOOD);
        if (food != null && food.nutrition() > 0) {
            list.add(ThaumcraftData.Aspects.LIFE, food.nutrition() * 2);
        }

        return list;
    }

    private static int getAttributeValue(ItemAttributeModifiers modifiers, net.minecraft.core.Holder<net.minecraft.world.entity.ai.attributes.Attribute> attribute) {
        if (modifiers == null)
            return 0;
        double total = 0;
        for (ItemAttributeModifiers.Entry entry : modifiers.modifiers()) {
            if (entry.attribute().equals(attribute)) {
                total += entry.modifier().amount();
            }
        }
        return (int) total;
    }
}
