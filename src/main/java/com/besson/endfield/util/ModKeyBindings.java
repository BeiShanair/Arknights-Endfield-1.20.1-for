package com.besson.endfield.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

public class ModKeyBindings {
    public static KeyMapping OPEN_STORAGE;
    public static void register(RegisterKeyMappingsEvent event) {
        OPEN_STORAGE = new KeyMapping(
                "key.endfield.open_storage",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                "category.endfield"
        );
        event.register(OPEN_STORAGE);
    }
}
