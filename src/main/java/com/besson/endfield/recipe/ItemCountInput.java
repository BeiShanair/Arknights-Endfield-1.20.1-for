package com.besson.endfield.recipe;

import net.minecraft.world.level.ItemLike;

public record ItemCountInput(ItemLike itemConvertible, int count) {
}
