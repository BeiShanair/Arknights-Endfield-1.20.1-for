package com.besson.endfield.command;

import com.besson.endfield.util.storage.GlobalStorageManager;
import com.besson.endfield.util.storage.StorageState;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.stream.IntStream;

@Mod.EventBusSubscriber
public class ModCommands {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        register(event.getDispatcher(), event.getBuildContext());
    }
    
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandRegistryAccess) {
        dispatcher.register(Commands.literal("storage")
                .then(Commands.literal("deposit")
                        .then(Commands.argument(
                                "item", ItemArgument.item(commandRegistryAccess))
                            .then(Commands.argument(
                                    "amount", IntegerArgumentType.integer(1))
                                        .executes(ModCommands::deposit)
                            )
                        )
                )
                .then(Commands.literal("withdraw")
                        .then(Commands.argument(
                                "item", ItemArgument.item(commandRegistryAccess))
                            .then(Commands.argument(
                                    "amount", IntegerArgumentType.integer(1))
                                    .executes(ModCommands::withdraw)
                            )
                        )
                )
                .then(Commands.literal("setcap")
                        .requires(s -> s.hasPermission(2))
                        .then(Commands.argument(
                                "amount", LongArgumentType.longArg(1))
                                .executes(ModCommands::setCap)
                        )
                )
        );
    }

    private static int deposit(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {

        ServerPlayer player = ctx.getSource().getPlayer();

        ItemInput itemArg = ItemArgument.getItem(ctx, "item");
        int amount = IntegerArgumentType.getInteger(ctx, "amount");

        Item item = itemArg.getItem();
        
        int availableAmount = IntStream.range(0, player.getInventory().getContainerSize())
                .mapToObj(i -> player.getInventory().getItem(i))
                .filter(stack -> !stack.isEmpty() && stack.getItem() == item)
                .mapToInt(ItemStack::getCount).sum();

        if (availableAmount < amount) {
            ctx.getSource().sendFailure(
                    Component.translatable("commands.endfield.deposit.warn", availableAmount, amount)
            );
            return 0;
        }
        
        int remainingToRemove = amount;
        for (int i = 0; i < player.getInventory().getContainerSize() && remainingToRemove > 0; i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!stack.isEmpty() && stack.getItem() == item) {
                int removeCount = Math.min(stack.getCount(), remainingToRemove);
                stack.shrink(removeCount);
                remainingToRemove -= removeCount;
                if (stack.isEmpty()) {
                    player.getInventory().setItem(i, ItemStack.EMPTY);
                }
            }
        }

        GlobalStorageManager manager = new GlobalStorageManager(player.serverLevel());

        ItemStack stack = itemArg.createItemStack(amount, false);
        long inserted = manager.insert(stack);

        ctx.getSource().sendSuccess(
                () -> Component.translatable("commands.endfield.deposit", inserted),
                false
        );

        return 1;
    }

    private static int withdraw(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {

        ServerPlayer player = ctx.getSource().getPlayer();

        ItemInput itemArg = ItemArgument.getItem(ctx, "item");
        int amount = IntegerArgumentType.getInteger(ctx, "amount");

        Item item = itemArg.getItem();
        GlobalStorageManager manager = new GlobalStorageManager(player.serverLevel());
        ItemStack extracted = manager.extract(item, amount);
        int inserted = extracted.getCount();
        player.getInventory().add(extracted);
        ctx.getSource().sendSuccess(
                () -> Component.translatable("commands.endfield.withdraw", inserted),
                false
        );

        return 1;
    }

    private static int setCap(CommandContext<CommandSourceStack> ctx) {
        long cap = LongArgumentType.getLong(ctx, "amount");
        if (cap < 0) {
            ctx.getSource().sendFailure(Component.translatable("commands.endfield.cap.negative"));
            return 0;
        }
        ServerLevel world = ctx.getSource().getLevel();
        StorageState state = GlobalStorageManager.get(world).getState();
        state.setGlobalCapacity(cap);
        ctx.getSource().sendSuccess(
                () -> Component.translatable("commands.endfield.cap", cap),
                true
        );

        return 1;
    }
}
