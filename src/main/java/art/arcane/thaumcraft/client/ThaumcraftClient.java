package art.arcane.thaumcraft.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import art.arcane.thaumcraft.Thaumcraft;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, value = Dist.CLIENT)
public class ThaumcraftClient {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
    }
}
