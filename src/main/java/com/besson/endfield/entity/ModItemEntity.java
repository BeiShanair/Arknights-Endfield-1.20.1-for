package com.besson.endfield.entity;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.custom.IndustrialExplosiveEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItemEntity {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ArknightsEndfield.MOD_ID);

    public static final RegistryObject<EntityType<IndustrialExplosiveEntity>> INDUSTRIAL_EXPLOSIVE =
            ENTITY_TYPES.register("industrial_explosive", () ->
                    EntityType.Builder.<IndustrialExplosiveEntity>of(
                            IndustrialExplosiveEntity::new, MobCategory.MISC
                            )
                            .sized(0.25f, 0.25f)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build("industrial_explosive"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
