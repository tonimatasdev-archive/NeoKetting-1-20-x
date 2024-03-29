package org.bukkit.craftbukkit.v1_20_R3.block;

import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Lectern;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftInventoryLectern;
import org.bukkit.inventory.Inventory;

public class CraftLectern extends CraftBlockEntityState<LecternBlockEntity> implements Lectern {

    public CraftLectern(World world, LecternBlockEntity tileEntity) {
        super(world, tileEntity);
    }

    protected CraftLectern(CraftLectern state) {
        super(state);
    }

    @Override
    public int getPage() {
        return getSnapshot().getPage();
    }

    @Override
    public void setPage(int page) {
        getSnapshot().setPage(page);
    }

    @Override
    public Inventory getSnapshotInventory() {
        return new CraftInventoryLectern(this.getSnapshot().bookAccess);
    }

    @Override
    public Inventory getInventory() {
        if (!this.isPlaced()) {
            return this.getSnapshotInventory();
        }

        return new CraftInventoryLectern(this.getTileEntity().bookAccess);
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result && this.getType() == Material.LECTERN && getWorldHandle() instanceof net.minecraft.world.level.Level) {
            LecternBlock.signalPageChange(this.world.getHandle(), this.getPosition(), this.getHandle());
        }

        return result;
    }

    @Override
    public CraftLectern copy() {
        return new CraftLectern(this);
    }
}
