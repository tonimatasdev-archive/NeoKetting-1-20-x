package org.bukkit.craftbukkit.v1_20_R3.entity;

import org.bukkit.entity.Enemy;

public interface CraftEnemy extends Enemy {

    net.minecraft.world.entity.monster.Enemy getHandle();
}
