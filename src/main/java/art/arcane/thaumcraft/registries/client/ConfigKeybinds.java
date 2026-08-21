package art.arcane.thaumcraft.registries.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import art.arcane.thaumcraft.Thaumcraft;

@EventBusSubscriber(modid = Thaumcraft.MOD_ID, value = Dist.CLIENT)
public final class ConfigKeybinds {

	private static final KeyMapping.Category CATEGORY = new KeyMapping.Category(Thaumcraft.id("thaumcraft"));

    public static final KeyMapping RESEARCH_DEBUG_SCREEN = new KeyMapping("key.chaumtraft.debug.research",
            KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.KEY_O,
            CATEGORY);

    public static final KeyMapping TUBE_DEBUG_RENDERER = new KeyMapping("key.chaumtraft.debug.tubes",
            KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.KEY_M,
            CATEGORY);

    public static final KeyMapping CYCLE_TOOL_MODE = new KeyMapping("key.thaumcraft.cycle_tool_mode",
            KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.KEY_G,
            CATEGORY);

    @SubscribeEvent
    public static void keyRegisterEvent(RegisterKeyMappingsEvent e) {
		e.registerCategory(CATEGORY);
        e.register(CYCLE_TOOL_MODE);
        if(Thaumcraft.isDev())
            registerDebugKeys(e);
    }

    private static void registerDebugKeys(RegisterKeyMappingsEvent e) {
        e.register(RESEARCH_DEBUG_SCREEN);
        e.register(TUBE_DEBUG_RENDERER);
    }
}
