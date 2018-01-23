package de.felix_klauke.caroline.scheduler;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import javax.inject.Inject;

/**
 * The scheduler implementation that will schedule asynchronous tasks.
 *
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class AsynchronousScheduler extends AbstractScheduler {

    /**
     * Create a new asynchronous scheduler.
     *
     * @param plugin          The bukkit plugin we schedule tasks for.
     * @param bukkitScheduler The underlying bukkit scheduler.
     */
    @Inject
    public AsynchronousScheduler(Plugin plugin, BukkitScheduler bukkitScheduler) {
        super(plugin, bukkitScheduler);
    }

    @Override
    protected BukkitTask schedule(Runnable runnable) {
        return getBukkitScheduler().runTaskAsynchronously(getPlugin(), runnable);
    }

    @Override
    protected BukkitTask schedule(Runnable runnable, int delay) {
        return getBukkitScheduler().runTaskLaterAsynchronously(getPlugin(), runnable, delay);
    }

    @Override
    protected BukkitTask schedule(Runnable runnable, int delay, int interval) {
        return getBukkitScheduler().runTaskTimerAsynchronously(getPlugin(), runnable, delay, interval);
    }
}
