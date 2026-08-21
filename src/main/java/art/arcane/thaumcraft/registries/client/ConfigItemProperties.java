package art.arcane.thaumcraft.registries.client;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterConditionalItemModelPropertyEvent;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.api.ThaumcraftData;
import art.arcane.thaumcraft.items.ItemModelProperties;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, value =  Dist.CLIENT)
public final class ConfigItemProperties {

    /* -------------------------------------------------------------------------------------------------------------- */

    public static final Pair<Identifier, MapCodec<? extends ItemModelProperties.HasData>> HAS_ASPECT = Pair.of(ThaumcraftData.ItemProperties.HAS_ASPECT, ItemModelProperties.HasData.CODEC);

    /* -------------------------------------------------------------------------------------------------------------- */

    @SubscribeEvent
    public static void onConditionalPropertyRegisterEvent(RegisterConditionalItemModelPropertyEvent event) {
        event.register(HAS_ASPECT.getFirst(), HAS_ASPECT.getSecond());
    }
}
