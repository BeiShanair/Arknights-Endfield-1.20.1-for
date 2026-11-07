package com.besson.endfield.compat.custom;

import com.besson.endfield.ArknightsEndfield;
import com.besson.endfield.block.ModBlocks;
import com.besson.endfield.blockEntity.custom.resourcing.HydroMiningRigBlockEntity;
import com.besson.endfield.recipe.custom.OreRigRecipe;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

public class HydroMiningRigRecipeCategory extends AbstractMachineRecipeCategory<OreRigRecipe> {
    public static final RecipeType<OreRigRecipe> HYDRO_MINING_RIG =
            new RecipeType<>(ResourceLocation.fromNamespaceAndPath(ArknightsEndfield.MOD_ID, "hydro_mining_rig"), OreRigRecipe.class);

    private static final int INPUTS = 2;

    private static final Component VEIN_HINT = Component.translatable("jei.hydro_mining_rig.vein_hint");

    public HydroMiningRigRecipeCategory(IGuiHelper helper) {
        super(HYDRO_MINING_RIG, "blockEntity.hydro_mining_rig", helper, ModBlocks.HYDRO_MINING_RIG.get());
    }

    @Override
    protected int inputSlotCount(OreRigRecipe recipe) {
        return INPUTS;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, OreRigRecipe recipe, IFocusGroup focuses) {
        int inputX = inputStartX(INPUTS, 1);

        builder.addSlot(RecipeIngredientRole.INPUT, inputX, SLOT_Y)
                .setStandardSlotBackground()
                .addIngredients(recipe.getInput())
                .addRichTooltipCallback((slotsView, tooltip) ->
                        tooltip.add(VEIN_HINT.copy().withStyle(ChatFormatting.GRAY)));

        builder.addSlot(RecipeIngredientRole.INPUT, inputX + PITCH, SLOT_Y)
                .setStandardSlotBackground()
                .setFluidRenderer(HydroMiningRigBlockEntity.WATER_COST_PER_CRAFT, false, SLOT_SIZE - 2, SLOT_SIZE - 2)
                .addIngredient(ForgeTypes.FLUID_STACK,
                        new FluidStack(Fluids.WATER, HydroMiningRigBlockEntity.WATER_COST_PER_CRAFT));

        builder.addSlot(RecipeIngredientRole.OUTPUT, outputStartX(INPUTS, 1), SLOT_Y)
                .setStandardSlotBackground()
                .addItemStack(recipe.getResultItem(null));
    }
}
