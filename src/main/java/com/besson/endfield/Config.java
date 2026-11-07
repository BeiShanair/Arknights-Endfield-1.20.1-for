package com.besson.endfield;

import net.minecraftforge.common.ForgeConfigSpec;

public final class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final int DEFAULT_EQUIPMENT_RENDER_DISTANCE = 256;

    public static final int MAX_EQUIPMENT_RENDER_DISTANCE = 1024;

    private static final ForgeConfigSpec.IntValue EQUIPMENT_RENDER_DISTANCE = BUILDER
            .comment("---- 中文 ----",
                    "工业设备的渲染距离（单位：方块）。",
                    "数值越大，离玩家越远的设备模型也会被渲染；调低可减轻大型工厂场景的渲染压力。",
                    "设为 0 表示只渲染紧邻玩家的设备。默认值：256",
                    "---- English ----",
                    "Render distance, in blocks, for industrial equipment.",
                    "Higher values keep machine models visible from further away; lower values cut down the render cost of large factories.",
                    "0 renders equipment only when directly adjacent. Default: 256")
            .defineInRange("equipmentRenderDistance", DEFAULT_EQUIPMENT_RENDER_DISTANCE,
                    0, MAX_EQUIPMENT_RENDER_DISTANCE);

    private static final ForgeConfigSpec.EnumValue<BeltRenderLayer> BELT_RENDER_LAYER = BUILDER
            .comment("---- 中文 ----",
                    "传送带方块的渲染层级。",
                    "TRANSLUCENT：半透明渲染（默认，与 Fabric 版本一致），支持半透明贴图。",
                    "CUTOUT：镂空渲染，不做透明混合，性能略好，可避免传送带密集处的透明排序问题。",
                    "该选项在客户端启动时读取，修改后需要重启游戏才会生效。",
                    "---- English ----",
                    "Render layer used by the belt block.",
                    "TRANSLUCENT (default, same as the Fabric version) supports translucent textures.",
                    "CUTOUT skips alpha blending, costs slightly less and avoids transparency sorting issues in dense belt networks.",
                    "Read once at client startup - a game restart is required after changing it.")
            .defineEnum("beltRenderLayer", BeltRenderLayer.TRANSLUCENT);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    private Config() {
        
    }

    public static int equipmentRenderDistance() {
        return Math.max(0, read(EQUIPMENT_RENDER_DISTANCE, DEFAULT_EQUIPMENT_RENDER_DISTANCE));
    }

    public static BeltRenderLayer beltRenderLayer() {
        return read(BELT_RENDER_LAYER, BeltRenderLayer.TRANSLUCENT);
    }

    private static <T> T read(ForgeConfigSpec.ConfigValue<T> value, T fallback) {
        try
        {
            return value.get();
        }
        catch (IllegalStateException e)
        {
            return fallback;
        }
    }

    public enum BeltRenderLayer {
        TRANSLUCENT,
        CUTOUT
    }
}
