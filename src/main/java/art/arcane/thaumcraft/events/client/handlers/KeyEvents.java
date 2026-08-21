package art.arcane.thaumcraft.events.client.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import art.arcane.thaumcraft.Thaumcraft;
import art.arcane.thaumcraft.items.tools.ElementalShovelItem;
import art.arcane.thaumcraft.networking.packets.ServerboundCycleToolModePacket;
import art.arcane.thaumcraft.registries.client.ConfigKeybinds;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, value = Dist.CLIENT)
public class KeyEvents {

    @SubscribeEvent
    public static void onKeyPressed(ClientTickEvent.Pre event) {
        if (ConfigKeybinds.CYCLE_TOOL_MODE.consumeClick()) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null && player.getMainHandItem().getItem() instanceof ElementalShovelItem) {
                Minecraft.getInstance().getConnection().send(new ServerboundCycleToolModePacket());
                player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.5f, 1.2f);
            }
        }
    }
}
