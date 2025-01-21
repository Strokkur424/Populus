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
package net.strokkur.populus.util;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.query.QueryOptions;
import net.strokkur.populus.Populus;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class PermissionsUtil {
    private static @Nullable Boolean hasNaturalPermission(final CommandSender sender, final String permission) {
        if (sender instanceof ConsoleCommandSender || sender.hasPermission(permission)) {
            // If the player has the exact permission or is the console, we can just return true
            return true;
        }

        if (Populus.luckPerms() == null || !(sender instanceof Player)) {
            // If LuckPerms is null or the sender is not a player, we cannot perform a LuckPerms lookup
            return false;
        }

        // Not enough information found...
        return null;
    }

    private static boolean hasParentedPermission(final @NotNull User user, final String permission) {
        final Optional<Node> foundNode = user.resolveInheritedNodes(QueryOptions.nonContextual()).stream()
            .filter(NodeType.PERMISSION::matches)
            .filter(node -> node.getKey().startsWith(permission) && node.getKey().charAt(permission.length()) == '.')
            .findFirst();

        return foundNode.isPresent();
    }

    /**
     * This method allows for checking a "parented" permission. This means that if you want to check
     * for a permission "root.node", but the sender has "root.node.child", the traditional
     * {@link CommandSender#hasPermission(String)} would return false. This method aims to fix that by
     * manually checking the LuckPerms permission tree. If LuckPerms is not loaded, we don't care ¯\_(ツ)_/¯
     * <p>
     * This method will <strong>not</strong> attempt to load the LuckPerms user if they aren't loaded. Instead,
     * it just returns false.
     *
     * @param sender     The CommandSender for checking their permission
     * @param permission The permission node we want to check for
     * @return Whether this CommandSender has the required, parented permission
     */
    public static boolean hasParentedPermission(final CommandSender sender, final String permission) {
        final Boolean naturalResult;
        if ((naturalResult = hasNaturalPermission(sender, permission)) != null) {
            return naturalResult;
        }

        final LuckPerms luckPerms = Populus.luckPerms();
        if (luckPerms == null) {
            return false;
        }

        final Player player = (Player) sender;
        final User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        if (user == null) {
            return false;
        }

        return hasParentedPermission(user, permission);
    }

    /**
     * This method allows for checking a "parented" permission. This means that if you want to check
     * for a permission "root.node", but the sender has "root.node.child", the traditional
     * {@link CommandSender#hasPermission(String)} would return false. This method aims to fix that by
     * manually checking the LuckPerms permission tree. If LuckPerms is not loaded, we don't care ¯\_(ツ)_/¯.
     * <p>
     * This method will attempt to load the LuckPerms user if they aren't loaded.
     *
     * @param sender     The CommandSender for checking their permission
     * @param permission The permission node we want to check for
     * @return Whether this CommandSender has the required, parented permission
     */
    public static CompletableFuture<Boolean> hasParentedPermissionAsync(final CommandSender sender, final String permission) {
        final Boolean naturalResult;
        if ((naturalResult = hasNaturalPermission(sender, permission)) != null) {
            return CompletableFuture.completedFuture(naturalResult);
        }

        final LuckPerms luckPerms = Populus.luckPerms();
        if (luckPerms == null) {
            return CompletableFuture.completedFuture(false);
        }

        final Player player = (Player) sender;
        return luckPerms.getUserManager()
            .loadUser(player.getUniqueId())
            .thenApply(user -> hasParentedPermission(user, permission));
    }

    private PermissionsUtil() {
        throw new UnsupportedOperationException("You can not initialize a utility class!");
    }
}