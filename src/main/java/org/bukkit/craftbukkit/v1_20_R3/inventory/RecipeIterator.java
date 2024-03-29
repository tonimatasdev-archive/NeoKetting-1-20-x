package org.bukkit.craftbukkit.v1_20_R3.inventory;

import com.google.common.base.Preconditions;

import java.util.Iterator;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import org.bukkit.inventory.Recipe;

public class RecipeIterator implements Iterator<Recipe> {
    private final Iterator<Map.Entry<RecipeType<?>, Map<ResourceLocation, RecipeHolder<?>>>> recipes;
    private Iterator<RecipeHolder<?>> current;

    public RecipeIterator() {
        this.recipes = MinecraftServer.getServer().getRecipeManager().recipes.entrySet().iterator();
    }

    @Override
    public boolean hasNext() {
        if (current != null && current.hasNext()) {
            return true;
        }

        if (recipes.hasNext()) {
            current = recipes.next().getValue().values().iterator();
            return hasNext();
        }

        return false;
    }

    @Override
    public Recipe next() {
        if (current == null || !current.hasNext()) {
            current = recipes.next().getValue().values().iterator();
            return next();
        }
        var recipe = current.next();
        try {
            return recipe.toBukkitRecipe();
        } catch (Throwable e) {
            throw new RuntimeException("Error converting recipe " + recipe.id(), e);
        }
    }

    @Override
    public void remove() {
        Preconditions.checkState(current != null, "next() not yet called");
        current.remove();
    }
}
