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
package com.github.glasspane.mesh.impl.serialization;

import com.google.gson.*;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.crafting.ShapedRecipe;
import net.minecraft.util.registry.Registry;

import java.lang.reflect.Type;

public class ItemStackJsonSerializer implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {
    @Override
    public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return ShapedRecipe.deserializeItemStack(json.getAsJsonObject());
    }

    @Override
    public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject ret = new JsonObject();
        ret.addProperty("item", Registry.ITEM.getId(src.getItem()).toString());
        if(src.getAmount() > 1) {
            ret.addProperty("count", src.getAmount());
        }
        return ret;
    }
}