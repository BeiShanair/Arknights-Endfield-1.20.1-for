package com.besson.endfield.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MaterialBottleItem extends Item {
    private final Map<Supplier<? extends Fluid>, Supplier<? extends Item>> bottledVariants = new LinkedHashMap<>();

    public MaterialBottleItem(Properties properties) {
        super(properties);
    }
    
    public MaterialBottleItem addBottled(Supplier<? extends Fluid> fluid, Supplier<? extends Item> bottled) {
        this.bottledVariants.put(fluid, bottled);
        return this;
    }
    
    public @Nullable Item getBottledItem(Fluid fluid) {
        for (Map.Entry<Supplier<? extends Fluid>, Supplier<? extends Item>> entry : bottledVariants.entrySet()) {
            if (entry.getKey().get() == fluid) {
                return entry.getValue().get();
            }
        }
        return null;
    }

    public Map<Supplier<? extends Fluid>, Supplier<? extends Item>> getBottledVariants() {
        return bottledVariants;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (hit.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(stack);
        }
        BlockPos pos = hit.getBlockPos();
        if (!level.mayInteract(player, pos)) {
            return InteractionResultHolder.fail(stack);
        }
        FluidState fluidState = level.getFluidState(pos);
        Item bottled = getBottledItem(fluidState.getType());
        
        if (bottled == null || !fluidState.isSource()
                || !(level.getBlockState(pos).getBlock() instanceof LiquidBlock)) {
            return InteractionResultHolder.fail(stack);
        }
        ItemStack bottledStack = new ItemStack(bottled);
        if (!level.isClientSide()) {
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            level.gameEvent(player, net.minecraft.world.level.gameevent.GameEvent.FLUID_PICKUP, pos);
            player.awardStat(Stats.ITEM_USED.get(this));
            
            if (!player.getInventory().add(bottledStack)) {
                player.drop(bottledStack, false);
            }
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }
        level.playSound(player, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
        
        return InteractionResultHolder.consume(stack);
    }
}
