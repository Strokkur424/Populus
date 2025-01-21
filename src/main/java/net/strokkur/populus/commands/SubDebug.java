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

import com.google.common.base.Preconditions;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.strokkur.populus.util.PermissionsUtil;
import org.bukkit.entity.Entity;

@SuppressWarnings("UnstableApiUsage")
public class SubDebug implements IPopulusSubcommand {

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> buildSubCommand() {
        return Commands.literal("debug")
            .requires(ctx -> PermissionsUtil.hasParentedPermission(ctx.getSender(), "populus.debug"))
            .then(spawnNpc())
            .then(checkPermission());
    }

    private LiteralArgumentBuilder<CommandSourceStack> checkPermission() {
        return Commands.literal("check-perm")
            .requires(ctx -> PermissionsUtil.hasParentedPermission(ctx.getSender(), "populus.debug.check-perm"))
            .then(Commands.argument("permission", StringArgumentType.string())
                .executes(ctx -> {
                    final String permission = ctx.getArgument("permission", String.class);

                    PermissionsUtil.hasParentedPermissionAsync(ctx.getSource().getSender(), permission).thenAccept(result -> {
                        ctx.getSource().getSender().sendRichMessage("<green>Result: <result>",
                            Placeholder.component("result", Component.text(result))
                        );
                    });

                    return Command.SINGLE_SUCCESS;
                }));
    }

    private LiteralArgumentBuilder<CommandSourceStack> spawnNpc() {
        return Commands.literal("spawn-npc")
            .requires(ctx -> PermissionsUtil.hasParentedPermission(ctx.getSender(), "populus.debug.spawn-npc"))
            .requires(ctx -> ctx.getExecutor() != null)
            .executes(ctx -> {
                final Entity entity = ctx.getSource().getExecutor();
                Preconditions.checkNotNull(entity, "Found no executing player!");

                entity.sendRichMessage("<green>Just play it as if an NPC just spawned at your location :3");

                return Command.SINGLE_SUCCESS;
            });
    }

}
