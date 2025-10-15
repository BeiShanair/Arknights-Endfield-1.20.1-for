package com.besson.endfield.power;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

public class PowerState extends SavedData {
    public int storedEnergy = 0;

    public PowerState() {}

    @Override
    public CompoundTag save(CompoundTag nbt) {
        nbt.putInt("StoredEnergy", storedEnergy);
        return nbt;
    }

    public static PowerState fromNbt(CompoundTag nbt) {
        PowerState state = new PowerState();
        state.storedEnergy = nbt.getInt("StoredEnergy");
        return state;
    }
}
