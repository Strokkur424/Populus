/*
 * This file is part of Populus, licensed under the MIT License.
 *
 * Copyright (c) 2024 Strokkur24
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.strokkur.populus.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.configuration.PluginMeta;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

@SuppressWarnings("UnstableApiUsage")
public class PopulusCommand {
    private final Component PLUGIN_INFORMATION;

    @SuppressWarnings("DataFlowIssue")
    public PopulusCommand(final @NotNull PluginMeta meta) {
        final Component VERSION_INFORMATION = Component.textOfChildren(
            text(" Plugin Version: ").color(NamedTextColor.AQUA),
            text(meta.getVersion() + " ").color(NamedTextColor.GREEN),
            text("@ ").color(NamedTextColor.AQUA),
            text(meta.getAPIVersion()).color(NamedTextColor.GREEN),
            text("\n")
        );

        final Component AUTHOR_INFORMATION = Component.textOfChildren(
            text(" Brought to you by: ").color(NamedTextColor.AQUA),
            text(String.join(", ", meta.getAuthors()) + "\n").color(NamedTextColor.GREEN)
        );

        final Component CONTRIBUTOR_INFORMATION = Component.textOfChildren(
            text(" Special thanks to: ").color(NamedTextColor.AQUA),
            text(String.join(", ", meta.getContributors()) + "\n").color(NamedTextColor.GREEN)
        );

        PLUGIN_INFORMATION = Component.textOfChildren(
            miniMessage().deserialize("<aqua><st>============</st> [POPULUS] <st>============\n\n"),
            VERSION_INFORMATION,
            AUTHOR_INFORMATION,
            CONTRIBUTOR_INFORMATION,
            miniMessage().deserialize("\n<aqua><st>============</st> [POPULUS] <st>============")
        );
    }

    public void register(final @NotNull Commands commands) {
        final LiteralCommandNode<CommandSourceStack> commandNode = Commands.literal("populus")
            .then(new SubDebug().buildSubCommand())
            .executes(this::printInfo)
            .build();

        commands.register(commandNode, "Command root for the Populus plugin", List.of("pop"));
    }

    private int printInfo(final @NotNull CommandContext<CommandSourceStack> ctx) {
        final CommandSender sender = ctx.getSource().getSender();
        sender.sendMessage(PLUGIN_INFORMATION);
        return Command.SINGLE_SUCCESS;
    }
}
