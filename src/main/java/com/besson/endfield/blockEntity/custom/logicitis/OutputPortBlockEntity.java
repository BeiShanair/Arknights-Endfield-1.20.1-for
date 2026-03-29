package com.besson.endfield.blockEntity.custom.logicitis;

import com.besson.endfield.block.custom.logicitis.OutputPortBlock;
import com.besson.endfield.blockEntity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

public class OutputPortBlockEntity extends BlockEntity {
    public OutputPortBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.OUTPUT_PORT.get(), pos, state);
    }

    public static void tick(Level world, BlockPos pos, BlockState state, OutputPortBlockEntity be) {

        if (world.isClientSide()) return;

        // 机器方向
        Direction facing = state.getValue(OutputPortBlock.FACING);

        // 获取机器 Storage
        BlockPos machinePos = pos.relative(facing.getOpposite());

        BlockEntity machineBe = world.getBlockEntity(machinePos);
        if (machineBe == null) return;

        LazyOptional<IItemHandler> optional =
                machineBe.getCapability(ForgeCapabilities.ITEM_HANDLER, facing);

        if (!optional.isPresent()) return;

        IItemHandler handler = optional.orElse(null);

        // 传送带方向（反方向）
        BlockPos beltPos = pos.relative(facing);

        BlockEntity targetBe = world.getBlockEntity(beltPos);

        if (!(targetBe instanceof BeltBlockEntity belt)) return;

        if (!belt.canAcceptFrom(facing.getOpposite())) return;

        for (int i = 0; i < handler.getSlots(); i++) {

            ItemStack simulated = handler.extractItem(i, 1, true);

            if (!simulated.isEmpty()) {

                ItemStack extracted = handler.extractItem(i, 1, false);

                if (!extracted.isEmpty()) {
                    belt.acceptItem(extracted, facing.getOpposite());
                    return;
                }
            }
        }
    }
}
