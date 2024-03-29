package org.bukkit.craftbukkit.v1_20_R3.entity;

import net.minecraft.world.entity.projectile.ThrownEgg;
import org.bukkit.craftbukkit.v1_20_R3.CraftServer;
import org.bukkit.entity.Egg;

public class CraftEgg extends CraftThrowableProjectile implements Egg {

    public CraftEgg(CraftServer server, ThrownEgg entity) {
        super(server, entity);
    }

    public ThrownEgg getHandle() {
        return (ThrownEgg) this.entity;
    }

    @Override
    public String toString() {
        return "CraftEgg";
    }
}
