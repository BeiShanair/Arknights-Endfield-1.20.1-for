package com.besson.endfield.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    public static final FoodProperties SAVORY_TANGBAO = new FoodProperties.Builder().nutrition(5).saturationMod(2f)
            .effect(() -> new MobEffectInstance(MobEffects.HEALTH_BOOST, 10800), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 10800), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 10800), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 10800), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties CITROME_PUDDING = new FoodProperties.Builder().nutrition(3).saturationMod(0.5f)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 6000), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties VALLEY_GRAYBREAD = new FoodProperties.Builder().nutrition(4).saturationMod(1.2f)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 3000), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties CITROME_JELLY = new FoodProperties.Builder().nutrition(2).saturationMod(0.3f)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 2000), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties SAVORY_TANGMIAN = new FoodProperties.Builder().nutrition(4).saturationMod(1f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 10800), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties CITROME_JAM = new FoodProperties.Builder().nutrition(2).saturationMod(0.2f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 10800), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties SAVORY_FILLET = new FoodProperties.Builder().nutrition(4).saturationMod(1.5f)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 3000), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties FILLET_CONFIT = new FoodProperties.Builder().nutrition(6).saturationMod(2f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 10800), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 10800), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties BUCKPILL_S = new FoodProperties.Builder().nutrition(2).saturationMod(0.2f)
            .effect(() -> new MobEffectInstance(MobEffects.HEAL, 1000), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties BUCKPILL_L = new FoodProperties.Builder().nutrition(2).saturationMod(0.3f)
            .effect(() -> new MobEffectInstance(MobEffects.HEAL, 2000), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties BUCKPILL_RF = new FoodProperties.Builder().nutrition(2).saturationMod(0.4f)
            .effect(() -> new MobEffectInstance(MobEffects.HEAL, 4000), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties CITROMIX_S = new FoodProperties.Builder().nutrition(3).saturationMod(0.3f)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 1000), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties CITROMIX_L = new FoodProperties.Builder().nutrition(5).saturationMod(0.5f)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 2000), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties CITROMIX_RF = new FoodProperties.Builder().nutrition(8).saturationMod(0.7f)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 4000), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties CANNED_CITROME_C = new FoodProperties.Builder().nutrition(6).saturationMod(0.6f)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 1000), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties CANNED_CITROME_B = new FoodProperties.Builder().nutrition(10).saturationMod(1f)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 2000), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties CANNED_CITROME_A = new FoodProperties.Builder().nutrition(14).saturationMod(1.5f)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 4000), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties JINCAO_TISANE = new FoodProperties.Builder().nutrition(4).saturationMod(0.5f)
            .effect(() -> new MobEffectInstance(MobEffects.HEAL, 2000), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties JINCAO_INFUSION = new FoodProperties.Builder().nutrition(4).saturationMod(0.5f)
            .effect(() -> new MobEffectInstance(MobEffects.HEAL, 4000), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties ART_VIAL = new FoodProperties.Builder().nutrition(2).saturationMod(0.2f)
            .effect(() -> new MobEffectInstance(MobEffects.HEAL, 1000), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties ART_TUBE = new FoodProperties.Builder().nutrition(2).saturationMod(0.2f)
            .effect(() -> new MobEffectInstance(MobEffects.HEAL, 4000), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties KUNST_VIAL = new FoodProperties.Builder().nutrition(2).saturationMod(0.2f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 5400), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties KUBST_TUBE = new FoodProperties.Builder().nutrition(2).saturationMod(0.2f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 10800), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties PERPLEXING_MEDICATION = new FoodProperties.Builder().nutrition(2).saturationMod(0.2f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 10800), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 10800), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties BIZARRO_CHILI = new FoodProperties.Builder().nutrition(4).saturationMod(0.5f)
            .effect(() -> new MobEffectInstance(MobEffects.HEAL, 4000), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties CITROBUCKY_MIX = new FoodProperties.Builder().nutrition(2).saturationMod(0.2f)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 4000), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties BUCK_CAPSULE_C = new FoodProperties.Builder().nutrition(2).saturationMod(0.2f)
            .effect(() -> new MobEffectInstance(MobEffects.HEAL, 1000), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties BUCK_CAPSULE_B = new FoodProperties.Builder().nutrition(2).saturationMod(0.3f)
            .effect(() -> new MobEffectInstance(MobEffects.HEAL, 2000), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties BUCK_CAPSULE_A = new FoodProperties.Builder().nutrition(2).saturationMod(0.4f)
            .effect(() -> new MobEffectInstance(MobEffects.HEAL, 4000), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties YAZHEN_SPRAY_S = new FoodProperties.Builder().nutrition(2).saturationMod(0.2f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 3000), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties YAZHEN_SPRAY_L = new FoodProperties.Builder().nutrition(2).saturationMod(0.3f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 6000), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties MOSSFIELD_PIE = new FoodProperties.Builder().nutrition(5).saturationMod(1f)
            .effect(() -> new MobEffectInstance(MobEffects.HEAL, 2000), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties COARSE_FLATBREAD = new FoodProperties.Builder().nutrition(3).saturationMod(0.5f)
            .effect(() -> new MobEffectInstance(MobEffects.HEAL, 1000), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties MEAT_STIR_FRY = new FoodProperties.Builder().nutrition(6).saturationMod(1.5f)
            .effect(() -> new MobEffectInstance(MobEffects.HEAL, 2000), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties TARTPEPPER_SALAD = new FoodProperties.Builder().nutrition(4).saturationMod(1f)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 2000), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties GINSENG_MEAT_STEW = new FoodProperties.Builder().nutrition(7).saturationMod(2f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 10800), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 10800), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties FORTIFYING_INFUSION = new FoodProperties.Builder().nutrition(5).saturationMod(1f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 10800), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties WULING_FRIED_RICE = new FoodProperties.Builder().nutrition(6).saturationMod(1.5f)
            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 10800), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties GARDEN_FRIED_RICE = new FoodProperties.Builder().nutrition(5).saturationMod(1.2f)
            .effect(() -> new MobEffectInstance(MobEffects.HEAL, 3000), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties HOT_CRUNCHY_RIBS = new FoodProperties.Builder().nutrition(7).saturationMod(2f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 600), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties CARTILAGE_TACK = new FoodProperties.Builder().nutrition(4).saturationMod(1f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 2000), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties FIRETACK = new FoodProperties.Builder().nutrition(6).saturationMod(1.5f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 10800), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties BUGTACK = new FoodProperties.Builder().nutrition(5).saturationMod(1.2f)
            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 5400), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties TARTPEPPER_PICKLE = new FoodProperties.Builder().nutrition(2).saturationMod(0.2f)
            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 10800), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties BIZARROTACK = new FoodProperties.Builder().nutrition(7).saturationMod(2f)
            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 5400), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties CORRECTIVE_REMEDY = new FoodProperties.Builder().nutrition(2).saturationMod(0.2f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 6000), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties BLANCHED_REMEDY = new FoodProperties.Builder().nutrition(2).saturationMod(0.2f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 6000), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties SENSORY_REMEDY = new FoodProperties.Builder().nutrition(2).saturationMod(0.2f)
            .effect(() -> new MobEffectInstance(MobEffects.DIG_SPEED, 6000), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties ASHPIN_REMEDY = new FoodProperties.Builder().nutrition(2).saturationMod(0.2f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 2000), 1.0f)
            .alwaysEat().build();
    public static  final FoodProperties SMOKED_RICEBALL = new FoodProperties.Builder().nutrition(3).saturationMod(0.5f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 10800), 1.0f)
            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 10800), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties GARDEN_STIR_FRY = new FoodProperties.Builder().nutrition(5).saturationMod(1.2f)
            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 5400), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties FIRESTOVE_RICE = new FoodProperties.Builder().nutrition(7).saturationMod(2f)
            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 10800), 1.0f)
            .alwaysEat().build();
    public static final FoodProperties PRESERVE_STEW = new FoodProperties.Builder().nutrition(6).saturationMod(1.5f)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 10800), 1.0f)
            .alwaysEat().build();
}
