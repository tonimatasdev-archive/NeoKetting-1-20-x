package org.bukkit.craftbukkit.v1_20_R3.entity;

import org.bukkit.craftbukkit.v1_20_R3.CraftServer;
import org.bukkit.entity.Drowned;

public class CraftDrowned extends CraftZombie implements Drowned {

    public CraftDrowned(CraftServer server, net.minecraft.world.entity.monster.Drowned entity) {
        super(server, entity);
    }

    public net.minecraft.world.entity.monster.Drowned getHandle() {
        return (net.minecraft.world.entity.monster.Drowned) this.entity;
    }

    @Override
    public String toString() {
        return "CraftDrowned";
    }
}
