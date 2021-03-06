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
package com.github.glasspane.mesh.mixin;

import com.github.glasspane.mesh.api.annotation.CalledByReflection;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@CalledByReflection
public class MeshMixinConfig implements IMixinConfigPlugin {
    private static final String MIXIN_PACKAGE = "com.github.glasspane.mesh.mixin";
    private static final boolean DEBUG_MODE = Boolean.getBoolean("mesh.debug");
    private static final boolean DEVELOPMENT = Boolean.getBoolean("fabric.development");

    @Override
    public void onLoad(String mixinPackage) {
        if(!mixinPackage.startsWith(MIXIN_PACKAGE)) {
            throw new IllegalArgumentException("Invalid Package: " + mixinPackage + ", expected: " + MIXIN_PACKAGE);
        }
    }

    @Override
    public String getRefMapperConfig() {
        return null; //fall back to default value
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        switch(mixinClassName) {
            case "com.github.glasspane.mesh.mixin.client.MixinMinecraftClient":
            case "com.github.glasspane.mesh.mixin.server.MixinMinecraftDedicatedServer":
                return DEBUG_MODE;
        }
        if(mixinClassName.startsWith(MIXIN_PACKAGE)) {
            return true;
        }
        else {
            throw new IllegalArgumentException("Invalid Package for Class " + mixinClassName + ", expected: " + MIXIN_PACKAGE);
        }
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
        //NO-OP
    }

    @Override
    public List<String> getMixins() {
        return Collections.emptyList(); //NO-OP
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        //NO-OP
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        //NO-OP
    }
}
