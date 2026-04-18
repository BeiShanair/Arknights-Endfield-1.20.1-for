package com.besson.endfield.item.custom.logicitis;

import com.besson.endfield.renderer.item.logicitis.DepotBusSectionItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.function.Consumer;

public class DepotBusSectionItem extends BlockItem implements GeoItem {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    public DepotBusSectionItem(Block block, Properties settings) {
        super(block, settings);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private final DepotBusSectionItemRenderer renderer = new DepotBusSectionItemRenderer();
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
