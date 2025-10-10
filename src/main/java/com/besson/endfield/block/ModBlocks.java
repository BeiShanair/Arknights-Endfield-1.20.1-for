package com.besson.endfield.block;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.custom.*;
import com.besson.endfield.item.ModItems;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, ArknightsEndfield.MOD_ID);

    public static final RegistryObject<Block> CRAFTER = registerBlocks("crafter",
            () -> new CrafterBlock(BlockBehaviour.Properties.of().strength(3f).noOcclusion()));
    public static final RegistryObject<Block> PORTABLE_ORIGINIUM_RIG = registerBlocksWithoutItem("portable_originium_rig",
            () -> new PortableOriginiumRigBlock(BlockBehaviour.Properties.of().strength(3f, 5f).noOcclusion()));
    public static final RegistryObject<Block> PROTOCOL_ANCHOR_CORE = registerBlocksWithoutItem("protocol_anchor_core",
            () -> new ProtocolAnchorCoreBlock(BlockBehaviour.Properties.of().strength(3f, 5f).noOcclusion()));

    public static final RegistryObject<Block> THERMAL_BANK = registerBlocksWithoutItem("thermal_bank",
            () -> new ThermalBankBlock(BlockBehaviour.Properties.of().strength(3f, 5f).noOcclusion()));
    public static final RegistryObject<Block> THERMAL_BANK_SIDE = registerBlocks("thermal_bank_side",
            () -> new ThermalBankSideBlock(BlockBehaviour.Properties.of().strength(3f, 5f).noOcclusion()));

    public static final RegistryObject<Block> AMETHYST_MINERAL_VEIN_BLOCK = registerBlocks("amethyst_mineral_vein_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f, 5f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> COAL_MINERAL_VEIN_BLOCK = registerBlocks("coal_mineral_vein_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f, 5f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> COPPER_MINERAL_VEIN_BLOCK = registerBlocks("copper_mineral_vein_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f, 5f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> DIAMOND_MINERAL_VEIN_BLOCK = registerBlocks("diamond_mineral_vein_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f, 5f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> GOLD_MINERAL_VEIN_BLOCK = registerBlocks("gold_mineral_vein_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f, 5f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> IRON_MINERAL_VEIN_BLOCK = registerBlocks("iron_mineral_vein_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f, 5f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> LAPIS_MINERAL_VEIN_BLOCK = registerBlocks("lapis_mineral_vein_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f, 5f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> REDSTONE_MINERAL_VEIN_BLOCK = registerBlocks("redstone_mineral_vein_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f, 5f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> EMERALD_MINERAL_VEIN_BLOCK = registerBlocks("emerald_mineral_vein_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f, 5f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> ORIGINIUM_MINERAL_VEIN_BLOCK = registerBlocks("originium_mineral_vein_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f, 5f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> AMETHYST_ORE_BLOCK = registerBlocks("amethyst_ore_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f, 5f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> CUPRIUM_MINERAL_VEIN_BLOCK = registerBlocks("cuprium_mineral_vein_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f, 5f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> CUPRIUM_ORE_BLOCK = registerBlocks("cuprium_ore_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f, 5f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> DEEPSLATE_AMETHYST_ORE = registerBlocks("deepslate_amethyst_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f, 5f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> DEEPSLATE_CUPRIUM_ORE = registerBlocks("deepslate_cuprium_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f, 5f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> DEEPSLATE_FERRIUM_ORE = registerBlocks("deepslate_ferrium_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f, 5f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> DEEPSLATE_ORIGINIUM_ORE = registerBlocks("deepslate_originium_ore",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f, 5f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> FERRIUM_MINERAL_VEIN_BLOCK = registerBlocks("ferrium_mineral_vein_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f, 5f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> FERRIUM_ORE_BLOCK = registerBlocks("ferrium_ore_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f, 5f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> ORIGINIUM_ORE_BLOCK = registerBlocks("originium_ore_block",
            () -> new Block(BlockBehaviour.Properties.of().strength(5f, 5f).requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> AKETINE_BLOCK = registerBlocksWithoutItem("aketine_block",
            () -> new FlowerBlock(() -> MobEffects.JUMP, 100, BlockBehaviour.Properties.of().strength(0.5f).noOcclusion().instabreak().noCollission()));
    public static final RegistryObject<Block> POTTED_AKETINE_BLOCK = registerBlocksWithoutItem("potted_aketine_block",
            () -> new FlowerPotBlock(ModBlocks.AKETINE_BLOCK.get(), BlockBehaviour.Properties.of().strength(0.5f).noOcclusion()));
    public static final RegistryObject<Block> AMBER_RICE_BLOCK = registerBlocksWithoutItem("amber_rice_block",
            () -> new FlowerBlock(() -> MobEffects.MOVEMENT_SPEED, 100, BlockBehaviour.Properties.of().strength(0.5f).noOcclusion().instabreak().noCollission()));
    public static final RegistryObject<Block> POTTED_AMBER_RICE_BLOCK = registerBlocksWithoutItem("potted_amber_rice_block",
            () -> new FlowerPotBlock(ModBlocks.AMBER_RICE_BLOCK.get(), BlockBehaviour.Properties.of().strength(0.5f).noOcclusion()));
    public static final RegistryObject<Block> BUCKFLOWER_BLOCK = registerBlocksWithoutItem("buckflower_block",
            () -> new FlowerBlock(() -> MobEffects.REGENERATION, 100, BlockBehaviour.Properties.of().strength(0.5f).noOcclusion().instabreak().noCollission()));
    public static final RegistryObject<Block> POTTED_BUCKFLOWER_BLOCK = registerBlocksWithoutItem("potted_buckflower_block",
            () -> new FlowerPotBlock(ModBlocks.BUCKFLOWER_BLOCK.get(), BlockBehaviour.Properties.of().strength(0.5f).noOcclusion()));
    public static final RegistryObject<Block> CITROME_BLOCK = registerBlocksWithoutItem("citrome_block",
            () -> new FlowerBlock(() -> MobEffects.SATURATION, 100, BlockBehaviour.Properties.of().strength(0.5f).noOcclusion().instabreak().noCollission()));
    public static final RegistryObject<Block> POTTED_CITROME_BLOCK = registerBlocksWithoutItem("potted_citrome_block",
            () -> new FlowerPotBlock(ModBlocks.CITROME_BLOCK.get(), BlockBehaviour.Properties.of().strength(0.5f).noOcclusion()));
    public static final RegistryObject<Block> FIREBUCKLE_BLOCK = registerBlocksWithoutItem("firebuckle_block",
            () -> new FlowerBlock(() -> MobEffects.FIRE_RESISTANCE, 100, BlockBehaviour.Properties.of().strength(0.5f).noOcclusion().instabreak().noCollission()));
    public static final RegistryObject<Block> POTTED_FIREBUCKLE_BLOCK = registerBlocksWithoutItem("potted_firebuckle_block",
            () -> new FlowerPotBlock(ModBlocks.FIREBUCKLE_BLOCK.get(), BlockBehaviour.Properties.of().strength(0.5f).noOcclusion()));
    public static final RegistryObject<Block> FLUFFED_JINCAO_BLOCK = registerBlocksWithoutItem("fluffed_jincao_block",
            () -> new FlowerBlock(() -> MobEffects.MOVEMENT_SLOWDOWN, 100, BlockBehaviour.Properties.of().strength(0.5f).noOcclusion().instabreak().noCollission()));
    public static final RegistryObject<Block> POTTED_FLUFFED_JINCAO_BLOCK = registerBlocksWithoutItem("potted_fluffed_jincao_block",
            () -> new FlowerPotBlock(ModBlocks.FLUFFED_JINCAO_BLOCK.get(), BlockBehaviour.Properties.of().strength(0.5f).noOcclusion()));
    public static final RegistryObject<Block> JINCAO_BLOCK = registerBlocksWithoutItem("jincao_block",
            () -> new FlowerBlock(() -> MobEffects.HEAL, 100, BlockBehaviour.Properties.of().strength(0.5f).noOcclusion().instabreak().noCollission()));
    public static final RegistryObject<Block> POTTED_JINCAO_BLOCK = registerBlocksWithoutItem("potted_jincao_block",
            () -> new FlowerPotBlock(ModBlocks.JINCAO_BLOCK.get(), BlockBehaviour.Properties.of().strength(0.5f).noOcclusion()));
    public static final RegistryObject<Block> REDJADE_GINSENG_BLOCK = registerBlocksWithoutItem("redjade_ginseng_block",
            () -> new FlowerBlock(() -> MobEffects.DAMAGE_BOOST, 100, BlockBehaviour.Properties.of().strength(0.5f).noOcclusion().instabreak().noCollission()));
    public static final RegistryObject<Block> POTTED_REDJADE_GINSENG_BLOCK = registerBlocksWithoutItem("potted_redjade_ginseng_block",
            () -> new FlowerPotBlock(ModBlocks.REDJADE_GINSENG_BLOCK.get(), BlockBehaviour.Properties.of().strength(0.5f).noOcclusion()));
    public static final RegistryObject<Block> REED_RYE_BLOCK = registerBlocksWithoutItem("reed_rye_block",
            () -> new FlowerBlock(() -> MobEffects.DIG_SPEED, 100, BlockBehaviour.Properties.of().strength(0.5f).noOcclusion().instabreak().noCollission()));
    public static final RegistryObject<Block> POTTED_REED_RYE_BLOCK = registerBlocksWithoutItem("potted_reed_rye_block",
            () -> new FlowerPotBlock(ModBlocks.REED_RYE_BLOCK.get(), BlockBehaviour.Properties.of().strength(0.5f).noOcclusion()));
    public static final RegistryObject<Block> SANDLEAF_BLOCK = registerBlocksWithoutItem("sandleaf_block",
            () -> new FlowerBlock(() -> MobEffects.WATER_BREATHING, 100, BlockBehaviour.Properties.of().strength(0.5f).noOcclusion().instabreak().noCollission()));
    public static final RegistryObject<Block> POTTED_SANDLEAF_BLOCK = registerBlocksWithoutItem("potted_sandleaf_block",
            () -> new FlowerPotBlock(ModBlocks.SANDLEAF_BLOCK.get(), BlockBehaviour.Properties.of().strength(0.5f).noOcclusion()));
    public static final RegistryObject<Block> TARTPEPPER_BLOCK = registerBlocksWithoutItem("tartpepper_block",
            () -> new FlowerBlock(() -> MobEffects.CONFUSION, 100, BlockBehaviour.Properties.of().strength(0.5f).noOcclusion().instabreak().noCollission()));
    public static final RegistryObject<Block> POTTED_TARTPEPPER_BLOCK = registerBlocksWithoutItem("potted_tartpepper_block",
            () -> new FlowerPotBlock(ModBlocks.TARTPEPPER_BLOCK.get(), BlockBehaviour.Properties.of().strength(0.5f).noOcclusion()));
    public static final RegistryObject<Block> THORNY_YAZHEN_BLOCK = registerBlocksWithoutItem("thorny_yazhen_block",
            () -> new FlowerBlock(() -> MobEffects.POISON, 100, BlockBehaviour.Properties.of().strength(0.5f).noOcclusion().instabreak().noCollission()));
    public static final RegistryObject<Block> POTTED_THORNY_YAZHEN_BLOCK = registerBlocksWithoutItem("potted_thorny_yazhen_block",
            () -> new FlowerPotBlock(ModBlocks.THORNY_YAZHEN_BLOCK.get(), BlockBehaviour.Properties.of().strength(0.5f).noOcclusion()));
    public static final RegistryObject<Block> UMBRALINE_BLOCK = registerBlocksWithoutItem("umbraline_block",
            () -> new FlowerBlock(() -> MobEffects.INVISIBILITY, 100, BlockBehaviour.Properties.of().strength(0.5f).noOcclusion().instabreak().noCollission()));
    public static final RegistryObject<Block> POTTED_UMBRALINE_BLOCK = registerBlocksWithoutItem("potted_umbraline_block",
            () -> new FlowerPotBlock(ModBlocks.UMBRALINE_BLOCK.get(), BlockBehaviour.Properties.of().strength(0.5f).noOcclusion()));
    public static final RegistryObject<Block> YAZHEN_BLOCK = registerBlocksWithoutItem("yazhen_block",
            () -> new FlowerBlock(() -> MobEffects.REGENERATION, 100, BlockBehaviour.Properties.of().strength(0.5f).noOcclusion().instabreak().noCollission()));
    public static final RegistryObject<Block> POTTED_YAZHEN_BLOCK = registerBlocksWithoutItem("potted_yazhen_block",
            () -> new FlowerPotBlock(ModBlocks.YAZHEN_BLOCK.get(), BlockBehaviour.Properties.of().strength(0.5f).noOcclusion()));


    private static <T extends Block>RegistryObject<T> registerBlocksWithoutItem(String name, Supplier<T> blocks) {
        return BLOCKS.register(name, blocks);
    }

    private static <T extends Block>RegistryObject<T> registerBlocks(String name, Supplier<T> blocks){
        RegistryObject<T> block = BLOCKS.register(name, blocks);
        registerBlockItems(name, block);
        return block;
    }

    private static <T extends Block> void registerBlockItems(String name, RegistryObject<T> block){
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
    
    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
