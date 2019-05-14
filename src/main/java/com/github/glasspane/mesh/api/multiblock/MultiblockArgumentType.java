package com.github.glasspane.mesh.api.multiblock;

import com.google.common.collect.Lists;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableTextComponent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class MultiblockArgumentType implements ArgumentType<MultiblockTemplate<?>> {

    private static final DynamicCommandExceptionType INVALID_MULTIBLOCK_TYPE = new DynamicCommandExceptionType((obj) -> new TranslatableTextComponent("command.mesh.argument.multiblock.invalid", obj));
    private static final Collection<String> EXAMPLES = Lists.newArrayList("test:test_multiblock", "modid:multiblock_furnace");

    /**
     * use the factory method ({@link #create()}) instead!
     */
    private MultiblockArgumentType() {
        //NO-OP
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        Registry<MultiblockTemplate<?>> registry = MultiblockManager.getInstance().getRegistry();
        return CommandSource.suggestIdentifiers(registry.stream().map(registry::getId), builder);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public static MultiblockTemplate<?> getMultiblockArgument(CommandContext<ServerCommandSource> context, String name) {
        return (MultiblockTemplate<?>) context.getArgument(name, MultiblockTemplate.class);
    }

    @Override
    public MultiblockTemplate<?> parse(StringReader reader) throws CommandSyntaxException {
        Identifier id = Identifier.parse(reader);
        return MultiblockManager.getInstance().getRegistry().getOrEmpty(id).orElseThrow(() -> INVALID_MULTIBLOCK_TYPE.create(id));
    }

    public static MultiblockArgumentType create() {
        return new MultiblockArgumentType();
    }
}
