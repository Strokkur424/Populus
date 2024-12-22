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
            .then(spawnNpc())
            .then(checkPermission());
    }

    private LiteralArgumentBuilder<CommandSourceStack> checkPermission() {
        return Commands.literal("check-perm")
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
            .requires(ctx -> ctx.getExecutor() != null)
            .executes(ctx -> {
                final Entity entity = ctx.getSource().getExecutor();
                Preconditions.checkNotNull(entity, "Found no executing player!");

                entity.sendRichMessage("<green>Just play it as if an NPC just spawned at your location :3");

                return Command.SINGLE_SUCCESS;
            });
    }

}
