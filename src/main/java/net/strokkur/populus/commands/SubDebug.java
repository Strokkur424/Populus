package net.strokkur.populus.commands;

import com.google.common.base.Preconditions;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Entity;

@SuppressWarnings("UnstableApiUsage")
public class SubDebug implements IPopulusSubcommand {

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> buildSubCommand() {
        return Commands.literal("debug")
            .then(spawnNpc());
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
