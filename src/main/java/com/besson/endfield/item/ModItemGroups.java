package com.besson.endfield.item;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModItemGroups {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ArknightsEndfield.MOD_ID);

    public static final RegistryObject<CreativeModeTab> ARKNIGHTS_ENDFIELD =
            CREATIVE_MODE_TAB.register("arknights_endfield", () -> CreativeModeTab.builder()
                    .icon(() -> ModBlocks.PROTOCOL_ANCHOR_CORE.get().asItem().getDefaultInstance())
                    .title(Component.translatable("itemGroup.arknights_endfield"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModBlocks.ORIGINIUM_MINERAL_VEIN_BLOCK.get());
                        pOutput.accept(ModBlocks.ORIGINIUM_ORE_BLOCK.get());
                        pOutput.accept(ModBlocks.DEEPSLATE_ORIGINIUM_ORE.get());
                        pOutput.accept(ModBlocks.AMETHYST_MINERAL_VEIN_BLOCK.get());
                        pOutput.accept(ModBlocks.AMETHYST_ORE_BLOCK.get());
                        pOutput.accept(ModBlocks.DEEPSLATE_AMETHYST_ORE.get());
                        pOutput.accept(ModBlocks.FERRIUM_MINERAL_VEIN_BLOCK.get());
                        pOutput.accept(ModBlocks.FERRIUM_ORE_BLOCK.get());
                        pOutput.accept(ModBlocks.DEEPSLATE_FERRIUM_ORE.get());
                        pOutput.accept(ModBlocks.CUPRIUM_MINERAL_VEIN_BLOCK.get());
                        pOutput.accept(ModBlocks.CUPRIUM_ORE_BLOCK.get());
                        pOutput.accept(ModBlocks.DEEPSLATE_CUPRIUM_ORE.get());

                        pOutput.accept(ModBlocks.COAL_MINERAL_VEIN_BLOCK.get());
                        pOutput.accept(ModBlocks.COPPER_MINERAL_VEIN_BLOCK.get());
                        pOutput.accept(ModBlocks.DIAMOND_MINERAL_VEIN_BLOCK.get());
                        pOutput.accept(ModBlocks.EMERALD_MINERAL_VEIN_BLOCK.get());
                        pOutput.accept(ModBlocks.GOLD_MINERAL_VEIN_BLOCK.get());
                        pOutput.accept(ModBlocks.IRON_MINERAL_VEIN_BLOCK.get());
                        pOutput.accept(ModBlocks.LAPIS_MINERAL_VEIN_BLOCK.get());
                        pOutput.accept(ModBlocks.REDSTONE_MINERAL_VEIN_BLOCK.get());

                        pOutput.accept(ModBlocks.CRAFTER.get());
                        pOutput.accept(ModItems.PROTOCOL_ANCHOR_CORE_ITEM.get());
                        pOutput.accept(ModItems.RELAY_TOWER_ITEM.get());
                        pOutput.accept(ModItems.ELECTRIC_PYLON_ITEM.get());
                        pOutput.accept(ModItems.ELECTRIC_MINING_RIG_ITEM.get());
                        pOutput.accept(ModItems.ELECTRIC_MINING_RIG_MK_II_ITEM.get());
                        pOutput.accept(ModItems.PORTABLE_ORIGINIUM_RIG_ITEM.get());
                        pOutput.accept(ModItems.REFINING_UNIT_ITEM.get());
//                        pOutput.accept(ModItems.SHREDDING_UNIT_ITEM);
//                        pOutput.accept(ModItems.FILLING_UNIT_ITEM);
//                        pOutput.accept(ModItems.FITTING_UNIT_ITEM);
//                        pOutput.accept(ModItems.GEARING_UNIT_ITEM);
//                        pOutput.accept(ModItems.GRINDING_UNIT_ITEM);
//                        pOutput.accept(ModItems.MOULDING_UNIT_ITEM);
//                        pOutput.accept(ModItems.PACKAGING_UNIT_ITEM);
//                        pOutput.accept(ModItems.PLANTING_UNIT_ITEM);
//                        pOutput.accept(ModItems.SEED_PICKING_UNIT_ITEM);
                        pOutput.accept(ModItems.THERMAL_BANK_ITEM.get());

                        pOutput.accept(ModItems.ORIGINIUM_ORE.get());
                        pOutput.accept(ModItems.ORIGINIUM_POWDER.get());
                        pOutput.accept(ModItems.ORIGOCRUST.get());
                        pOutput.accept(ModItems.ORIGOCRUST_POWDER.get());
                        pOutput.accept(ModItems.PACKED_ORIGOCRUST.get());
                        pOutput.accept(ModItems.AMETHYST_ORE.get());
                        pOutput.accept(ModItems.AMETHYST_POWDER.get());
                        pOutput.accept(ModItems.AMETHYST_FIBER.get());
                        pOutput.accept(ModItems.AMETHYST_BOTTLE.get());
                        pOutput.accept(ModItems.AMETHYST_PART.get());
                        pOutput.accept(ModItems.AMETHYST_COMPONENT.get());
                        pOutput.accept(ModItems.FERRIUM_ORE.get());
                        pOutput.accept(ModItems.FERRIUM_POWDER.get());
                        pOutput.accept(ModItems.FERRIUM.get());
                        pOutput.accept(ModItems.FERRIUM_BOTTLE.get());
                        pOutput.accept(ModItems.FERRIUM_PART.get());
                        pOutput.accept(ModItems.FERRIUM_COMPONENT.get());
                        pOutput.accept(ModItems.CUPRIUM_ORE.get());
                        pOutput.accept(ModItems.CUPRIUM_POWDER.get());
                        pOutput.accept(ModItems.CUPRIUM.get());
                        pOutput.accept(ModItems.CUPRIUM_PART.get());
                        pOutput.accept(ModItems.CUPRIUM_COMPONENT.get());
                        pOutput.accept(ModItems.CUPRIUM_JAR.get());
                        pOutput.accept(ModItems.CRYSTON_PART.get());
                        pOutput.accept(ModItems.CRYSTON_POWDER.get());
                        pOutput.accept(ModItems.CRYSTON_FIBER.get());
                        pOutput.accept(ModItems.CRYSTON_BOTTLE.get());
                        pOutput.accept(ModItems.CRYSTON_COMPONENT.get());
                        pOutput.accept(ModItems.STEEL.get());
                        pOutput.accept(ModItems.STEEL_BOTTLE.get());
                        pOutput.accept(ModItems.STEEL_PART.get());
                        pOutput.accept(ModItems.STEEL_JAR.get());
                        pOutput.accept(ModItems.DENSE_ORIGINIUM_POWDER.get());
                        pOutput.accept(ModItems.DENSE_ORIGOCRUST_POWDER.get());
                        pOutput.accept(ModItems.DENSE_FERRIUM_POWDER.get());
                        pOutput.accept(ModItems.DENSE_CARBON_POWDER.get());

                        pOutput.accept(ModItems.BUCKFLOWER.get());
                        pOutput.accept(ModItems.BUCKFLOWER_SEED.get());
                        pOutput.accept(ModItems.BUCKFLOWER_POWDER.get());
                        pOutput.accept(ModItems.BUCK_CAPSULE_C.get());
                        pOutput.accept(ModItems.BUCK_CAPSULE_B.get());
                        pOutput.accept(ModItems.BUCK_CAPSULE_A.get());
                        pOutput.accept(ModItems.BUCKPILL_S.get());
                        pOutput.accept(ModItems.BUCKPILL_L.get());
                        pOutput.accept(ModItems.BUCKPILL_RF.get());
                        pOutput.accept(ModItems.GROUND_BUCKFLOWER_POWDER.get());

                        pOutput.accept(ModItems.CITROME.get());
                        pOutput.accept(ModItems.CITROME_SEED.get());
                        pOutput.accept(ModItems.CITROME_POWDER.get());
                        pOutput.accept(ModItems.CANNED_CITROME_C.get());
                        pOutput.accept(ModItems.CANNED_CITROME_B.get());
                        pOutput.accept(ModItems.CANNED_CITROME_A.get());
                        pOutput.accept(ModItems.CITROME_JAM.get());
                        pOutput.accept(ModItems.CITROME_JELLY.get());
                        pOutput.accept(ModItems.CITROME_PUDDING.get());
                        pOutput.accept(ModItems.CITROBUCKY_MIX.get());
                        pOutput.accept(ModItems.UMBRALINE.get());
                        pOutput.accept(ModItems.CITROMIX.get());
                        pOutput.accept(ModItems.CITROMIX_S.get());
                        pOutput.accept(ModItems.CITROMIX_L.get());
                        pOutput.accept(ModItems.CITROMIX_RF.get());
                        pOutput.accept(ModItems.FIREBUCKLE.get());
                        pOutput.accept(ModItems.FIREBUCKLE_POWDER.get());
                        pOutput.accept(ModItems.GROUND_CITROME_POWDER.get());

                        pOutput.accept(ModItems.JINCAO.get());
                        pOutput.accept(ModItems.JINCAO_SEED.get());
                        pOutput.accept(ModItems.JINCAO_POWDER.get());
                        pOutput.accept(ModItems.JINCAO_DRINK.get());
                        pOutput.accept(ModItems.JINCAO_INFUSION.get());
                        pOutput.accept(ModItems.JINCAO_TEA.get());
                        pOutput.accept(ModItems.JINCAO_TISANE.get());
                        pOutput.accept(ModItems.FLUFFED_JINCAO.get());
                        pOutput.accept(ModItems.FLUFFED_JINCAO_POWDER.get());

                        pOutput.accept(ModItems.YAZHEN.get());
                        pOutput.accept(ModItems.YAZHEN_SEED.get());
                        pOutput.accept(ModItems.YAZHEN_POWDER.get());
                        pOutput.accept(ModItems.YAZHEN_SPRAY_S.get());
                        pOutput.accept(ModItems.YAZHEN_SPRAY_L.get());
                        pOutput.accept(ModItems.YAZHEN_SYRINGE_C.get());
                        pOutput.accept(ModItems.YAZHEN_SYRINGE_A.get());
                        pOutput.accept(ModItems.THORNY_YAZHEN.get());
                        pOutput.accept(ModItems.THORNY_YAZHEN_POWDER.get());

                        pOutput.accept(ModItems.AKETINE.get());
                        pOutput.accept(ModItems.AKETINE_SEED.get());
                        pOutput.accept(ModItems.AKETINE_POWDER.get());
                        pOutput.accept(ModItems.AMBER_RICE.get());
                        pOutput.accept(ModItems.AMBER_RICE_SEED.get());
                        pOutput.accept(ModItems.REDJADE_GINSENG.get());
                        pOutput.accept(ModItems.REDJADE_GINSENG_SEED.get());
                        pOutput.accept(ModItems.REED_RYE.get());
                        pOutput.accept(ModItems.REED_RYE_SEED.get());
                        pOutput.accept(ModItems.SANDLEAF.get());
                        pOutput.accept(ModItems.SANDLEAF_SEED.get());
                        pOutput.accept(ModItems.SANDLEAF_POWDER.get());

                        pOutput.accept(ModItems.TARTPEPPER.get());
                        pOutput.accept(ModItems.TARTPEPPER_SEED.get());
                        pOutput.accept(ModItems.TARTPEPPER_PICKLE.get());
                        pOutput.accept(ModItems.TARTPEPPER_SALAD.get());

                        pOutput.accept(ModItems.CARBON.get());
                        pOutput.accept(ModItems.CARBON_POWDER.get());
                        pOutput.accept(ModItems.STABILIZED_CARBON.get());

                        pOutput.accept(ModItems.AGGAGRIT.get());
                        pOutput.accept(ModItems.AGGAGRIT_BLOCK.get());
                        pOutput.accept(ModItems.AGGAGRIT_CLUSTER.get());

                        pOutput.accept(ModItems.LC_BATTERY.get());
                        pOutput.accept(ModItems.SC_BATTERY.get());
                        pOutput.accept(ModItems.HC_BATTERY.get());
                        pOutput.accept(ModItems.INDUSTRIAL_EXPLOSIVE.get());

                        pOutput.accept(ModItems.ARTS_TUBE.get());
                        pOutput.accept(ModItems.ARTS_VIAL.get());
                        pOutput.accept(ModItems.KUNST_TUBE.get());
                        pOutput.accept(ModItems.KUNST_VIAL.get());
                        pOutput.accept(ModItems.PERPLEXING_MEDICATION.get());

                        pOutput.accept(ModItems.ASHPIN_REMEDY.get());
                        pOutput.accept(ModItems.BLANCHED_REMEDY.get());
                        pOutput.accept(ModItems.BIZARROTACK.get());
                        pOutput.accept(ModItems.BIZARRO_CHILI.get());
                        pOutput.accept(ModItems.BUGTACK.get());
                        pOutput.accept(ModItems.CARTILAGE_TACK.get());

                        pOutput.accept(ModItems.COARSE_FLATBREAD.get());
                        pOutput.accept(ModItems.CORRECTIVE_REMEDY.get());

                        pOutput.accept(ModItems.FILLET.get());
                        pOutput.accept(ModItems.FILLET_CONFIT.get());
                        pOutput.accept(ModItems.FIRESTOVE_RICE.get());
                        pOutput.accept(ModItems.FIRETACK.get());
                        pOutput.accept(ModItems.FORTIFYING_INFUSION.get());
                        pOutput.accept(ModItems.GARDEN_FRIED_RICE.get());
                        pOutput.accept(ModItems.GARDEN_STIR_FRY.get());
                        pOutput.accept(ModItems.GINSENG_MEAT_STEW.get());
                        pOutput.accept(ModItems.GRASS_CHAFF.get());
                        pOutput.accept(ModItems.CHITIN_BIT.get());
                        pOutput.accept(ModItems.CARTILAGE_BIT.get());
                        pOutput.accept(ModItems.HOLLOW_BONE.get());
                        pOutput.accept(ModItems.HOLLOW_BONECHIP.get());
                        pOutput.accept(ModItems.HOT_CRUNCHY_RIBS.get());

                        pOutput.accept(ModItems.MEAT_STIR_FRY.get());
                        pOutput.accept(ModItems.MOSSFIELD_PIE.get());
                        pOutput.accept(ModItems.PRESERVE_STEW.get());
                        pOutput.accept(ModItems.SAVORY_FILLET.get());
                        pOutput.accept(ModItems.SAVORY_TANGBAO.get());
                        pOutput.accept(ModItems.SAVORY_TANGMIAN.get());
                        pOutput.accept(ModItems.SCORCHBUG.get());
                        pOutput.accept(ModItems.GLOWBUG.get());
                        pOutput.accept(ModItems.SENSORY_REMEDY.get());
                        pOutput.accept(ModItems.SMOKED_RICEBALL.get());
                        pOutput.accept(ModItems.WOOD.get());
                        pOutput.accept(ModItems.SOAKED_WOOD.get());
                        pOutput.accept(ModItems.SOAKED_WOODCHIP.get());
                        pOutput.accept(ModItems.VALLEY_GRAYBREAD.get());
                        pOutput.accept(ModItems.WULING_FRIED_RICE.get());
                    }).build()
            );

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
