package com.besson.endfield.recipe;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Objects;

public record ReactorResource(Type type, ItemStack itemStack, FluidStack fluidStack) {

    public static final ReactorResource EMPTY = new ReactorResource(Type.ITEM, ItemStack.EMPTY, FluidStack.EMPTY);

    public ReactorResource {
        if (type == null) {
            type = Type.ITEM;
        }
        if (itemStack == null) {
            itemStack = ItemStack.EMPTY;
        }
        if (fluidStack == null) {
            fluidStack = FluidStack.EMPTY;
        }

        if (type == Type.ITEM) {
            fluidStack = FluidStack.EMPTY;
            if (itemStack.isEmpty()) {
                itemStack = ItemStack.EMPTY;
            } else if (itemStack.getCount() != 1) {
                ItemStack single = itemStack.copy();
                single.setCount(1);
                itemStack = single;
            }
        } else {
            itemStack = ItemStack.EMPTY;
            if (fluidStack.isEmpty()) {
                fluidStack = FluidStack.EMPTY;
            } else if (fluidStack.getAmount() != 1) {
                FluidStack single = fluidStack.copy();
                single.setAmount(1);
                fluidStack = single;
            }
        }
    }

    public static ReactorResource item(ItemStack stack) {
        return new ReactorResource(Type.ITEM, stack, FluidStack.EMPTY);
    }

    public static ReactorResource fluid(FluidStack stack) {
        return new ReactorResource(Type.FLUID, ItemStack.EMPTY, stack);
    }

    public boolean isItem() {
        return type == Type.ITEM;
    }

    public boolean isFluid() {
        return type == Type.FLUID;
    }

    public boolean isBlank() {
        return switch (type) {
            case ITEM -> itemStack.isEmpty();
            case FLUID -> fluidStack.isEmpty();
        };
    }

    public long getMaxAmount() {
        return switch (type) {
            case ITEM -> itemStack.isEmpty() ? 64 : itemStack.getMaxStackSize();
            case FLUID -> 50_000;
        };
    }

    public ItemStack asStack(int count) {
        if (!isItem() || itemStack.isEmpty() || count <= 0) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = itemStack.copy();
        stack.setCount(count);
        return stack;
    }

    public FluidStack asFluidStack(int amount) {
        if (!isFluid() || fluidStack.isEmpty() || amount <= 0) {
            return FluidStack.EMPTY;
        }
        FluidStack stack = fluidStack.copy();
        stack.setAmount(amount);
        return stack;
    }

    public CompoundTag toNbt() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("type", type.asString());
        switch (type) {
            case ITEM -> {
                if (!itemStack.isEmpty()) {
                    nbt.put("variant", itemStack.save(new CompoundTag()));
                }
            }
            case FLUID -> {
                if (!fluidStack.isEmpty()) {
                    nbt.put("variant", fluidStack.writeToNBT(new CompoundTag()));
                }
            }
        }
        return nbt;
    }

    public static ReactorResource fromNbt(CompoundTag nbt) {
        if (nbt == null || !nbt.contains("type", Tag.TAG_STRING)) {
            return EMPTY;
        }

        Type type = Type.fromString(nbt.getString("type"));
        CompoundTag variant = nbt.getCompound("variant");

        return switch (type) {
            case ITEM -> item(ItemStack.of(variant));
            case FLUID -> new ReactorResource(Type.FLUID, ItemStack.EMPTY, FluidStack.loadFluidStackFromNBT(variant));
        };
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ReactorResource other) || type != other.type) {
            return false;
        }
        return switch (type) {
            case ITEM -> ItemStack.isSameItemSameTags(itemStack, other.itemStack);
            case FLUID -> fluidStack.getFluid() == other.fluidStack.getFluid()
                    && Objects.equals(fluidStack.getTag(), other.fluidStack.getTag());
        };
    }

    @Override
    public int hashCode() {
        return switch (type) {
            case ITEM -> Objects.hash(type, itemStack.getItem(), itemStack.getTag());
            case FLUID -> Objects.hash(type, fluidStack.getFluid(), fluidStack.getTag());
        };
    }

    public enum Type {
        ITEM("item"),
        FLUID("fluid");

        private final String id;

        Type(String id) {
            this.id = id;
        }

        public String asString() {
            return id;
        }

        public static Type fromString(String id) {
            for (Type type : values()) {
                if (type.id.equalsIgnoreCase(id)) {
                    return type;
                }
            }
            return ITEM;
        }
    }
}
