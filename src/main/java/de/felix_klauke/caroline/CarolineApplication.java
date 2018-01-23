package de.felix_klauke.caroline;

import io.reactivex.Scheduler;
import io.reactivex.plugins.RxJavaPlugins;
import org.bukkit.plugin.Plugin;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.logging.Level;

/**
 * The central management application.
 *
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class CarolineApplication {

    /**
     * The plugin instance needed to run tasks.
     */
    private final Plugin plugin;

    /**
     * The synchronous scheduler.
     */
    private final Scheduler syncScheduler;

    /**
     * The asynchronous scheduler.
     */
    private final Scheduler asyncScheduler;

    /**
     * Create a new caroline application.
     *
     * @param plugin         The plugin.
     * @param syncScheduler  The synchronous scheduler.
     * @param asyncScheduler The asynchronous scheduler.
     */
    @Inject
    public CarolineApplication(Plugin plugin, @Named("syncScheduler") Scheduler syncScheduler, @Named("asyncScheduler") Scheduler asyncScheduler) {
        this.plugin = plugin;
        this.syncScheduler = syncScheduler;
        this.asyncScheduler = asyncScheduler;
    }

    /**
     * Initialize the caroline application.
     * <p>
     * We have to setup some rx java bindings such as error logging and scheduler provision.
     */
    public void initialize() {
        RxJavaPlugins.setErrorHandler(throwable -> plugin.getLogger().log(Level.SEVERE, "Unhandled exception. ", throwable));

        RxJavaPlugins.setInitComputationSchedulerHandler(schedulerCallable -> syncScheduler);
        RxJavaPlugins.setInitIoSchedulerHandler(schedulerCallable -> asyncScheduler);
        RxJavaPlugins.setInitNewThreadSchedulerHandler(schedulerCallable -> asyncScheduler);
        RxJavaPlugins.setComputationSchedulerHandler(scheduler -> syncScheduler);
        RxJavaPlugins.setIoSchedulerHandler(scheduler -> asyncScheduler);
        RxJavaPlugins.setNewThreadSchedulerHandler(scheduler -> asyncScheduler);
    }

    /**
     * Destroy the application and clean da shit up.
     */
    public void destroy() {
        RxJavaPlugins.reset();
    }
}
