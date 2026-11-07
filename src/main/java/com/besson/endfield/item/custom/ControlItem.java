package com.besson.endfield.item.custom;

import com.besson.endfield.block.custom.logicitis.FluidPipeBlock;
import com.besson.endfield.block.custom.powering.BasePylonBlock;
import com.besson.endfield.block.custom.powering.BaseRelayBlock;
import com.besson.endfield.block.custom.powering.ProtocolAnchorCoreBlock;
import com.besson.endfield.block.custom.production2.BaseCrucibleSideBlock;
import com.besson.endfield.blockEntity.custom.logicitis.FluidPipeBlockEntity;
import com.besson.endfield.blockEntity.custom.powering.BasePylonBlockEntity;
import com.besson.endfield.blockEntity.custom.powering.BaseRelayBlockEntity;
import com.besson.endfield.blockEntity.custom.production2.BaseCrucibleSideBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ControlItem extends Item {
    private static BlockPos startPos = null;

    public ControlItem(Properties properties) {
        super(properties);
    }

    private static boolean canStartPowerTransport(Block block) {
        return block instanceof BaseRelayBlock
                || block instanceof BasePylonBlock
                || block instanceof ProtocolAnchorCoreBlock;
    }

    private static boolean canEndPowerTransport(Block block) {
        return block instanceof BaseRelayBlock || block instanceof BasePylonBlock;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        if (player == null) return InteractionResult.CONSUME;
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        Block clickedBlock = state.getBlock();

        if (level.isClientSide()) {
            return InteractionResult.CONSUME;
        }
        
        if (clickedBlock instanceof FluidPipeBlock) {
            if (level.getBlockEntity(pos) instanceof FluidPipeBlockEntity pipe) {
                pipe.mode = pipe.mode.next();
                pipe.setChanged();
                level.sendBlockUpdated(pos, state, state, 3);

                String msgKey = switch (pipe.mode) {
                    case TRANSFER -> "message.control_item.pipe_mode.transfer";
                    case EXTRACT -> "message.control_item.pipe_mode.extract";
                    case INSERT -> "message.control_item.pipe_mode.insert";
                };
                player.sendSystemMessage(Component.translatable(msgKey));
            }
            return InteractionResult.CONSUME;
        }
        
        if (clickedBlock instanceof BaseCrucibleSideBlock) {
            if (level.getBlockEntity(pos) instanceof BaseCrucibleSideBlockEntity side) {
                side.cycleFluidPort();
                int portIndex = side.getFluidPortIndex();
                if (portIndex < 0) {
                    player.sendSystemMessage(Component.translatable("message.control_item.crucible_port.unbound"));
                } else {
                    player.sendSystemMessage(Component.translatable("message.control_item.crucible_port.bound",
                            portIndex + 1, portIndex + 1));
                }
            }
            return InteractionResult.CONSUME;
        }
        
        if (player.isShiftKeyDown()) {
            if (canStartPowerTransport(clickedBlock)) {
                startPos = pos;
                player.sendSystemMessage(Component.translatable("message.control_item.start_recorded",
                        pos.getX(), pos.getY(), pos.getZ()));
            }
            return InteractionResult.CONSUME;
        }
        
        if (canEndPowerTransport(clickedBlock)) {
            if (startPos == null) {
                player.sendSystemMessage(Component.translatable("message.control_item.no_start"));
                return InteractionResult.CONSUME;
            }

            BlockPos recorded = startPos;
            startPos = null;
            
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof BaseRelayBlockEntity relay) {
                relay.setConnectedNode(recorded);
            } else if (be instanceof BasePylonBlockEntity pylon) {
                pylon.setConnectedNode(recorded);
            }
            if (be != null) {
                be.setChanged();
                level.sendBlockUpdated(pos, state, state, 3);
            }

            player.sendSystemMessage(Component.translatable("message.control_item.completed",
                    recorded.getX(), recorded.getY(), recorded.getZ(),
                    pos.getX(), pos.getY(), pos.getZ()));
            return InteractionResult.CONSUME;
        }

        return InteractionResult.CONSUME;
    }
}
