package com.besson.endfield.blockEntity.custom.logicitis;

import com.besson.endfield.blockEntity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class DepotBusSectionBlockEntity extends BlockEntity implements GeoBlockEntity {
    
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public DepotBusSectionBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DEPOT_BUS_SECTION.get(), pos, state);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
    
}
