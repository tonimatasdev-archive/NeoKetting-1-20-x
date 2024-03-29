package org.bukkit.craftbukkit.v1_20_R3.block.data.type;

import org.bukkit.block.data.type.Crafter;
import org.bukkit.craftbukkit.v1_20_R3.block.data.CraftBlockData;

public abstract class CraftCrafter extends CraftBlockData implements Crafter {

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty CRAFTING = getBoolean("crafting");
    private static final net.minecraft.world.level.block.state.properties.BooleanProperty TRIGGERED = getBoolean("triggered");
    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> ORIENTATION = getEnum("orientation");

    @Override
    public boolean isCrafting() {
        return get(CRAFTING);
    }

    @Override
    public void setCrafting(boolean crafting) {
        set(CRAFTING, crafting);
    }

    @Override
    public boolean isTriggered() {
        return get(TRIGGERED);
    }

    @Override
    public void setTriggered(boolean triggered) {
        set(TRIGGERED, triggered);
    }

    @Override
    public org.bukkit.block.data.type.Crafter.Orientation getOrientation() {
        return get(ORIENTATION, org.bukkit.block.data.type.Crafter.Orientation.class);
    }

    @Override
    public void setOrientation(org.bukkit.block.data.type.Crafter.Orientation orientation) {
        set(ORIENTATION, orientation);
    }
}
