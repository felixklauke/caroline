/*
 * MIT License
 *
 * Copyright (c) 2017 Felix Klauke
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

package de.felixklauke.caroline.core.scheduler;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import javax.inject.Inject;

/**
 * The scheduler implementation that will schedule synchronous tasks.
 *
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class SynchronousScheduler extends AbstractScheduler {

    /**
     * Create a new synchronous scheduler.
     *
     * @param plugin          The bukkit plugin we schedule tasks for.
     * @param bukkitScheduler The underlying bukkit scheduler.
     */
    @Inject
    public SynchronousScheduler(Plugin plugin, BukkitScheduler bukkitScheduler) {
        super(plugin, bukkitScheduler);
    }

    @Override
    protected BukkitTask schedule(Runnable runnable) {
        return getBukkitScheduler().runTask(getPlugin(), runnable);
    }

    @Override
    protected BukkitTask schedule(Runnable runnable, int delay) {
        return getBukkitScheduler().runTaskLater(getPlugin(), runnable, delay);
    }

    @Override
    protected BukkitTask schedule(Runnable runnable, int delay, int interval) {
        return getBukkitScheduler().runTaskTimerAsynchronously(getPlugin(), runnable, delay, interval);
    }
}
