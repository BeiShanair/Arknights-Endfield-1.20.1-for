package com.besson.endfield.blockEntity.custom;

import com.besson.endfield.blockEntity.ModBlockEntities;
import com.besson.endfield.screen.custom.ProtocolAnchorCoreScreenHandler;
import com.besson.endfield.util.ProtocolAnchorCoreStatus;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

// TODO: 全局电网管理器
public class ProtocolAnchorCoreBlockEntity extends BlockEntity implements GeoBlockEntity, MenuProvider {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private int buffer = 0;
    private final int baseMaxBuffer = 100000;
    private final int basePower = 150;
    private int extraPower = 0;
    private int loadNode = 0;

    protected final ContainerData propertyDelegate;

    public ProtocolAnchorCoreBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.PROTOCOL_ANCHOR_CORE.get(), pos, state);
        this.propertyDelegate = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index){
                    case 0 -> ProtocolAnchorCoreBlockEntity.this.buffer;
                    case 1 -> ProtocolAnchorCoreBlockEntity.this.getMaxBuffer();
                    case 2 -> ProtocolAnchorCoreBlockEntity.this.basePower;
                    case 3 -> ProtocolAnchorCoreBlockEntity.this.getExtraPower();
                    case 4 -> ProtocolAnchorCoreBlockEntity.this.loadNode;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {

            }

            @Override
            public int getCount() {
                return 5;
            }
        };
    }

    public static void tick(Level world, BlockPos pos, BlockState state, ProtocolAnchorCoreBlockEntity entity) {
        if (world.isClientSide()) return;
        entity.buffer = Math.min(entity.buffer + entity.getTotalPower(), entity.getMaxBuffer());
        entity.setChanged();
    }

    private int getNearbyThermalBankPower() {
        int sum = 0;
        int loadNode = 0;
        BlockPos blockPos = this.getBlockPos();
        if (level != null) {
            for (BlockPos pos : BlockPos.betweenClosed(blockPos.offset(-30, -10, -30), blockPos.offset(30, 10, 30))) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof ThermalBankBlockEntity blockEntity) {
                    sum += blockEntity.getPowerOutput();
                    loadNode += 1;
                }
            }
        }
        this.loadNode = loadNode;
        return sum;
    }

    public int getMaxBuffer() {
        return baseMaxBuffer + getNearbyThermalBankPower();
    }

    private int getExtraPower() {
        this.extraPower = getNearbyThermalBankPower();
        return extraPower;
    }

    public int getTotalPower() {
        return basePower + getExtraPower();
    }

    public ProtocolAnchorCoreStatus getStatus() {
        return new ProtocolAnchorCoreStatus(buffer, getMaxBuffer(), basePower, getExtraPower(), loadNode);
    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0,
                state -> state.setAndContinue(RawAnimation.begin().thenLoop("idle"))));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("blockEntity.protocol_anchor_core");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new ProtocolAnchorCoreScreenHandler(pContainerId, pPlayerInventory, this, this.propertyDelegate);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("buffer", this.buffer);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.buffer = pTag.getInt("buffer");
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithFullMetadata();
    }

    public int getStoredPower() {
        return (int) this.buffer;
    }

    public boolean canSupplyPower() {
        return this.buffer >= 100;
    }

    public void consumePower(int i) {
        this.buffer = Math.max(0, this.buffer - i);
        setChanged();
    }
}
