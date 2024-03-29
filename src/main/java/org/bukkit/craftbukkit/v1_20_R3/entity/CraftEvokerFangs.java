package org.bukkit.craftbukkit.v1_20_R3.entity;

import com.google.common.base.Preconditions;
import org.bukkit.craftbukkit.v1_20_R3.CraftServer;
import org.bukkit.entity.EvokerFangs;
import org.bukkit.entity.LivingEntity;

public class CraftEvokerFangs extends CraftEntity implements EvokerFangs {

    public CraftEvokerFangs(CraftServer server, net.minecraft.world.entity.projectile.EvokerFangs entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.projectile.EvokerFangs getHandle() {
        return (net.minecraft.world.entity.projectile.EvokerFangs) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftEvokerFangs";
    }

    @Override
    public LivingEntity getOwner() {
        net.minecraft.world.entity.LivingEntity owner = getHandle().getOwner();

        return (owner == null) ? null : (LivingEntity) owner.getBukkitEntity();
    }

    @Override
    public void setOwner(LivingEntity owner) {
        getHandle().setOwner(owner == null ? null : ((CraftLivingEntity) owner).getHandle());
    }

    @Override
    public int getAttackDelay() {
        return getHandle().warmupDelayTicks;
    }

    @Override
    public void setAttackDelay(int delay) {
        Preconditions.checkArgument(delay >= 0, "Delay must be positive");

        getHandle().warmupDelayTicks = delay;
    }
}
