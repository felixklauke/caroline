package de.felix_klauke.caroline.scheduler;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.TimeUnit;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public abstract class AbstractScheduler extends Scheduler {

    /**
     * The bukkit plugin we schedule tasks for.
     */
    private final Plugin plugin;

    /**
     * The underlying bukkit scheduler.
     */
    private final BukkitScheduler bukkitScheduler;

    /**
     * Create a new abstract scheduler.
     *
     * @param plugin          The bukkit plugin we schedule tasks for.
     * @param bukkitScheduler The underlying bukkit scheduler.
     */
    public AbstractScheduler(Plugin plugin, BukkitScheduler bukkitScheduler) {
        this.plugin = plugin;
        this.bukkitScheduler = bukkitScheduler;
    }

    /**
     * Get the bukkit plugin we schedule tasks for.
     *
     * @return The bukkit plugin we schedule tasks for.
     */
    protected Plugin getPlugin() {
        return plugin;
    }

    /**
     * Get the underlying bukkit scheduler.
     *
     * @return The bukkit scheduler.
     */
    protected BukkitScheduler getBukkitScheduler() {
        return bukkitScheduler;
    }

    @Override
    public Worker createWorker() {
        return new SpigotRxWorker();
    }

    /**
     * Schedule  task with the given runnable.
     *
     * @param runnable The runnable.
     * @return The bukkit task instance.
     */
    protected abstract BukkitTask schedule(Runnable runnable);

    /**
     * Schedule a task with a specific delay given in minecraft server ticks.
     *
     * @param runnable The runnable.
     * @param delay    The delay.
     * @return The bukkit task instance.
     */
    protected abstract BukkitTask schedule(Runnable runnable, int delay);

    /**
     * Schedule a task with a specific delay created by the initial delay and the following intervals.
     *
     * @param runnable The runnable.
     * @param delay    The delay.
     * @param interval The interval
     * @return The bukkit task instance.
     */
    protected abstract BukkitTask schedule(Runnable runnable, int delay, int interval);

    /**
     * The worker implementation.
     */
    private final class SpigotRxWorker extends Worker {

        /**
         * The compostie disposable for all workers.
         */
        private final CompositeDisposable compositeDisposable = new CompositeDisposable();

        @Override
        public Disposable schedule(Runnable runnable, long delay, TimeUnit unit) {
            BukkitTask bukkitTask = AbstractScheduler.this.schedule(runnable, convertTimeToTicks(delay, unit));
            Disposable disposable = new DisposableBukkitTask(bukkitTask);
            compositeDisposable.add(disposable);
            return disposable;
        }

        @Override
        public Disposable schedulePeriodically(Runnable runnable, long initialDelay, long period, TimeUnit unit) {
            BukkitTask bukkitTask = AbstractScheduler.this.schedule(runnable, convertTimeToTicks(initialDelay, unit), convertTimeToTicks(period, unit));
            Disposable disposable = new DisposableBukkitTask(bukkitTask);
            compositeDisposable.add(disposable);
            return disposable;
        }

        @Override
        public Disposable schedule(Runnable runnable) {
            BukkitTask bukkitTask = AbstractScheduler.this.schedule(runnable);
            Disposable disposable = new DisposableBukkitTask(bukkitTask);
            compositeDisposable.add(disposable);
            return disposable;
        }

        /**
         * Convert java time to bukkit ticks.
         *
         * @param time     The time.
         * @param timeUnit The time unit.
         * @return The time in bukkit ticks.
         */
        private int convertTimeToTicks(long time, TimeUnit timeUnit) {
            return (int) Math.round((double) timeUnit.toMillis(time) / 50D);
        }

        @Override
        public void dispose() {
            compositeDisposable.dispose();
        }

        @Override
        public boolean isDisposed() {
            return compositeDisposable.isDisposed();
        }

        /**
         * Wrap a bukkit task in a disposable.
         */
        private final class DisposableBukkitTask implements Disposable {

            /**
             * The bukkit task we want to dispose.
             */
            private final BukkitTask bukkitTask;

            /**
             * If the task is disposed.
             */
            private boolean disposed;

            /**
             * Create a new disposable bukkit task.
             *
             * @param bukkitTask The bukkit task.
             */
            DisposableBukkitTask(BukkitTask bukkitTask) {
                this.bukkitTask = bukkitTask;
            }

            @Override
            public void dispose() {
                disposed = true;
                this.bukkitTask.cancel();
            }

            @Override
            public boolean isDisposed() {
                return disposed && !bukkitTask.getOwner().getServer().getScheduler().isCurrentlyRunning(bukkitTask.getTaskId());
            }
        }
    }
}
