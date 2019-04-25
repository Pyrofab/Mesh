/*
 * Mesh
 * Copyright (C) 2019 GlassPane
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; If not, see <https://www.gnu.org/licenses>.
 */
package com.github.glasspane.mesh.template.blockentity;

import com.github.glasspane.mesh.Mesh;
import com.github.glasspane.mesh.api.multiblock.Multiblock;
import com.github.glasspane.mesh.api.multiblock.MultiblockTemplate;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public abstract class MultiblockBlockEntity<T extends BlockEntity> extends LockableContainerBlockEntity implements SidedInventory, Tickable {

    protected static final Random RANDOM = new Random();
    private static final String MULTIBLOCK_NBT_KEY = Mesh.MODID + "_multiblock";
    protected final int randomTickOffset = RANDOM.nextInt(20);
    protected final MultiblockTemplate<T> multiblockTemplate;
    protected Multiblock<T> multiblock = null;
    protected CompoundTag multiblockData = null;
    protected int updateCounter;
    private boolean firstTick = true;

    public MultiblockBlockEntity(BlockEntityType<?> type, MultiblockTemplate<T> multiblockTemplate) {
        super(type);
        this.multiblockTemplate = multiblockTemplate;
    }

    @Override
    public void tick() {
        if(!this.world.isClient) {
            //TODO is there a better way to do this?
            if(firstTick) {
                this.onLoad();
                this.validateMultiblock();
                this.multiblockData = null;
                firstTick = false;
            }
            //check the integrity of the multiblock every 20 ticks
            if((MathHelper.abs(updateCounter++) % 20 == randomTickOffset)) {
                this.validateMultiblock();
            }
        }
    }

    /**
     * called at the beginning of the first tick, to allow for updating values that require the world and position of the {@link BlockEntity} to be set
     */
    public void onLoad() {
        Direction orientation = this.getOrientation();
        if(this.multiblockData != null && this.multiblockTemplate.isValidMultiblock((ServerWorld) this.world, this.pos, orientation)) {
            this.multiblock = this.multiblockTemplate.newInstance((ServerWorld) this.world, this.pos, orientation);
            this.multiblock.fromTag(this.multiblockData);
        }
    }

    protected void validateMultiblock() {
        if(this.multiblock != null && !this.multiblockTemplate.isValidMultiblock((ServerWorld) this.world, this.pos, this.getOrientation())) {
            this.multiblock.invalidate();
            this.multiblock = null;
            this.markDirty();
            Mesh.getLogger().trace("invalidated multiblock at {}", this::getPos);
        }
    }

    public Direction getOrientation() {
        BlockState state = this.getCachedState();
        return state.getProperties().contains(HorizontalFacingBlock.FACING) ? state.get(HorizontalFacingBlock.FACING) : Direction.NORTH;
    }

    @Override
    public void fromTag(CompoundTag compoundTag_1) {
        super.fromTag(compoundTag_1);
        if(compoundTag_1.containsKey(MULTIBLOCK_NBT_KEY, NbtType.COMPOUND)) {
            this.multiblockData = compoundTag_1.getCompound(MULTIBLOCK_NBT_KEY);
        }
        this.firstTick = true;
    }

    @Override
    public CompoundTag toTag(CompoundTag compoundTag_1) {
        compoundTag_1 = super.toTag(compoundTag_1);
        this.validateMultiblock();
        if(this.multiblock != null) {
            compoundTag_1.put(MULTIBLOCK_NBT_KEY, this.multiblock.toTag(new CompoundTag()));
        }
        return compoundTag_1;
    }
}
