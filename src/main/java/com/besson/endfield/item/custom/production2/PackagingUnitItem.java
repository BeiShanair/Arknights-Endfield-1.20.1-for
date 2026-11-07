package com.besson.endfield.item.custom.production2;

import com.besson.endfield.item.custom.AbstractMultiBlockItem;
import com.besson.endfield.renderer.item.PackagingUnitItemRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.function.Consumer;

public class PackagingUnitItem extends AbstractMultiBlockItem implements GeoItem {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public PackagingUnitItem(Block block, Properties settings) {
        super(block, settings);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private BlockEntityWithoutLevelRenderer renderer = null;
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (renderer == null)
                    renderer = new PackagingUnitItemRenderer();
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

    @Override
    protected BlockPos[] getOccupiedPositions(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection().getOpposite();
        Direction left = facing.getCounterClockWise();
        Direction right = facing.getClockWise();
        Direction back = facing.getOpposite();
        Direction backLeft = back.getClockWise();
        Direction backRight = back.getCounterClockWise();

        BlockPos pos = context.getClickedPos();
        return new BlockPos[]{pos.relative(facing),
                pos.relative(facing).relative(left), pos.relative(facing).relative(right),
                pos.relative(facing).relative(left, 2), pos.relative(facing).relative(right, 2), pos.relative(facing).relative(right, 3),
                pos.relative(right), pos.relative(left),
                pos.relative(right, 2), pos.relative(right, 3), pos.relative(left, 2),
                pos.relative(back), pos.relative(back, 2),
                pos.relative(back).relative(backLeft), pos.relative(back).relative(backRight),
                pos.relative(back).relative(backLeft, 2), pos.relative(back).relative(backRight, 2), pos.relative(back).relative(backRight, 3),
                pos.relative(back, 2).relative(backLeft), pos.relative(back, 2).relative(backRight),
                pos.relative(back, 2).relative(backLeft, 2), pos.relative(back, 2).relative(backRight, 2), pos.relative(back, 2).relative(backRight, 3)};
    }
}
