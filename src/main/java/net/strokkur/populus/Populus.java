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
package net.strokkur.populus;

import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

public final class Populus extends JavaPlugin {
    private static Populus instance = null;
    private static Path directory = null;
    private static ComponentLogger logger = null;
    private static LuckPerms luckPerms = null;

    public static @NotNull Populus instance() {
        if (instance == null) {
            throw new RuntimeException("Tried to access plugin instance whilst plugin is disabled");
        }

        return instance;
    }

    public static @NotNull Path directory() {
        if (directory == null) {
            throw new RuntimeException("Tried to access plugin instance whilst plugin is disabled");
        }

        return directory;
    }

    public static @NotNull ComponentLogger logger() {
        if (logger == null) {
            throw new RuntimeException("Tried to access plugin instance whilst plugin is disabled");
        }

        return logger;
    }

    public static @Nullable LuckPerms luckPerms() {
        return luckPerms;
    }

    Populus(final Path directory, final ComponentLogger logger) {
        Populus.instance = this;
        Populus.directory = directory;
        Populus.logger = logger;
    }


    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("LuckPerms") != null) {
            Populus.luckPerms = LuckPermsProvider.get();
        }
    }

    @Override
    public void onDisable() {

    }


    public void reloadPlugin() {
        if (!directory().toFile().exists()) {
            //noinspection ResultOfMethodCallIgnored
            directory().toFile().mkdir();
        }

        // Reload rest of stuff
    }
}