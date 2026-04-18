package com.besson.endfield.blockEntity.custom.logicitis;

import com.besson.endfield.block.ModBlockEntityWithFacing;
import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.util.storage.GlobalStorageManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class DepotLoaderBlockEntity extends BlockEntity implements GeoBlockEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private boolean working = false;
    private int tickNum = 0;

    public DepotLoaderBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DEPOT_LOADER.get(), pos, state);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public static void tick(Level world, BlockPos pos, BlockState state, DepotLoaderBlockEntity be) {
        if (world.isClientSide()) return;

        be.tickNum++;

        if (be.tickNum % 20 == 0) {
            be.tickNum = 0;
            Direction facing = state.getValue(ModBlockEntityWithFacing.FACING);
            BlockEntity backEn = world.getBlockEntity(pos.relative(facing.getOpposite()));
            be.working = backEn instanceof DepotBusSectionBlockEntity || backEn instanceof DepotBusSectionSideBlockEntity;
            be.setChanged();
        }

    }

    public boolean sendItemToGlobalStorage(Level world, ItemStack stack) {
        if (world.isClientSide()) return false;

        if (world instanceof ServerLevel serverWorld && this.working) {
            GlobalStorageManager manager = GlobalStorageManager.get(serverWorld);
            long in = manager.insert(stack);
            return in > 0;
        }
        return false;
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putBoolean("working", this.working);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.working = pTag.getBoolean("working");
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithFullMetadata();
    }
}
