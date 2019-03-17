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
package com.github.glasspane.mesh.impl.multiblock;

import com.github.glasspane.mesh.Mesh;
import com.github.glasspane.mesh.api.annotation.AutoRegistry;
import com.github.glasspane.mesh.api.multiblock.MultiblockManager;
import com.github.glasspane.mesh.api.multiblock.MultiblockTemplate;
import com.github.glasspane.mesh.api.registry.AutoRegistryHook;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;

/**
 * @deprecated will be moved to a unified registry class at some point<br/>
 * use {@link MultiblockManager#getRegistry()} instead!
 */
@Deprecated
@AutoRegistry(value = Registry.class, modid = Mesh.MODID, registry = "registries", modsLoaded = "fabric")
final class MultiblockRegistry implements AutoRegistryHook {

    public static final MutableRegistry<MultiblockTemplate<?>> MULTIBLOCKS = new SimpleRegistry<>();
}
