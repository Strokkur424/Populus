package net.strokkur.populus.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class PopulusCommand {

    public void register(final Commands commands) {
        final LiteralCommandNode<CommandSourceStack> commandNode = Commands.literal("populus")
            .then(new SubDebug().buildSubCommand())
            .build();

        commands.register(commandNode, "Command root for the Populus plugin", List.of("pop"));
    }

}
