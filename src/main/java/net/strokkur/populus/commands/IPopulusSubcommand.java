package net.strokkur.populus.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;

@SuppressWarnings("UnstableApiUsage")
public interface IPopulusSubcommand {
    LiteralArgumentBuilder<CommandSourceStack> buildSubCommand();
}
