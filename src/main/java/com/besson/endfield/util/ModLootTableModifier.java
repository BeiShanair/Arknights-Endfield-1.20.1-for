package com.besson.endfield.util;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.item.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ArknightsEndfield.MOD_ID)
public class ModLootTableModifier {
    private static final ResourceLocation COW = ResourceLocation.fromNamespaceAndPath("minecraft", "entities/cow");
    private static final ResourceLocation SHEEP = ResourceLocation.fromNamespaceAndPath("minecraft", "entities/sheep");
    private static final ResourceLocation PIG = ResourceLocation.fromNamespaceAndPath("minecraft", "entities/pig");
    private static final ResourceLocation GRASS = ResourceLocation.fromNamespaceAndPath("minecraft", "blocks/grass");
    private static final ResourceLocation SKELETON = ResourceLocation.fromNamespaceAndPath("minecraft", "entities/skeleton");
    private static final ResourceLocation WITHER_SKELETON = ResourceLocation.fromNamespaceAndPath("minecraft", "entities/wither_skeleton");

    @SubscribeEvent
    public static void modifierLootTables(LootTableLoadEvent event) {
        if (event.getName().equals(COW)) {
            LootPool.Builder poolBuilder = LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .when(LootItemRandomChanceCondition.randomChance(0.5f))
                    .add(LootItem.lootTableItem(ModItems.FILLET.get()))
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)));

            event.getTable().addPool(poolBuilder.build());
        }

        if (event.getName().equals(SHEEP)) {
            LootPool.Builder poolBuilder = LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .when(LootItemRandomChanceCondition.randomChance(0.5f))
                    .add(LootItem.lootTableItem(ModItems.FILLET.get()))
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)));

            event.getTable().addPool(poolBuilder.build());
        }

        if (event.getName().equals(PIG)) {
            LootPool.Builder poolBuilder = LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .when(LootItemRandomChanceCondition.randomChance(0.5f))
                    .add(LootItem.lootTableItem(ModItems.FILLET.get()))
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)));

            event.getTable().addPool(poolBuilder.build());
        }

        if (event.getName().equals(GRASS)) {
            LootPool.Builder poolBuilder = LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .when(LootItemRandomChanceCondition.randomChance(0.125f))
                    .add(LootItem.lootTableItem(ModItems.SCORCHBUG.get()))
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)));

            event.getTable().addPool(poolBuilder.build());
        }
        if (event.getName().equals(GRASS)) {
            LootPool.Builder poolBuilder = LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .when(LootItemRandomChanceCondition.randomChance(0.125f))
                    .add(LootItem.lootTableItem(ModItems.GLOWBUG.get()))
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)));

            event.getTable().addPool(poolBuilder.build());
        }

        if (event.getName().equals(SKELETON)) {
            LootPool.Builder poolBuilder = LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .when(LootItemRandomChanceCondition.randomChance(0.3f))
                    .add(LootItem.lootTableItem(ModItems.AGGAGRIT.get()))
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)));

            event.getTable().addPool(poolBuilder.build());
        }
        if (event.getName().equals(SKELETON)) {
            LootPool.Builder poolBuilder = LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .when(LootItemRandomChanceCondition.randomChance(0.2f))
                    .add(LootItem.lootTableItem(ModItems.AGGAGRIT_BLOCK.get()))
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)));

            event.getTable().addPool(poolBuilder.build());
        }
        if (event.getName().equals(SKELETON)) {
            LootPool.Builder poolBuilder = LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .when(LootItemRandomChanceCondition.randomChance(0.1f))
                    .add(LootItem.lootTableItem(ModItems.AGGAGRIT_CLUSTER.get()))
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)));

            event.getTable().addPool(poolBuilder.build());
        }

        if (event.getName().equals(WITHER_SKELETON)) {
            LootPool.Builder poolBuilder = LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .when(LootItemRandomChanceCondition.randomChance(0.3f))
                    .add(LootItem.lootTableItem(ModItems.AGGAGRIT.get()))
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)));

            event.getTable().addPool(poolBuilder.build());
        }
        if (event.getName().equals(WITHER_SKELETON)) {
            LootPool.Builder poolBuilder = LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .when(LootItemRandomChanceCondition.randomChance(0.2f))
                    .add(LootItem.lootTableItem(ModItems.AGGAGRIT_BLOCK.get()))
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)));

            event.getTable().addPool(poolBuilder.build());
        }
        if (event.getName().equals(WITHER_SKELETON)) {
            LootPool.Builder poolBuilder = LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .when(LootItemRandomChanceCondition.randomChance(0.1f))
                    .add(LootItem.lootTableItem(ModItems.AGGAGRIT_CLUSTER.get()))
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)));

            event.getTable().addPool(poolBuilder.build());
        }
    }
}
