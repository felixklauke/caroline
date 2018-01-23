package de.felix_klauke.caroline.module;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import de.felix_klauke.caroline.CarolinePlugin;
import de.felix_klauke.caroline.scheduler.AsynchronousScheduler;
import de.felix_klauke.caroline.scheduler.SynchronousScheduler;
import io.reactivex.Scheduler;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * The google guice dependency module.
 *
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class CarolineModule extends AbstractModule {

    /**
     * The caroline plugin instance.
     */
    private final CarolinePlugin carolinePlugin;

    /**
     * Create a new caroline dependency module.
     *
     * @param carolinePlugin The caroline plugin instance.
     */
    public CarolineModule(CarolinePlugin carolinePlugin) {
        this.carolinePlugin = carolinePlugin;
    }

    @Override
    protected void configure() {
        bind(Plugin.class).toInstance(carolinePlugin);
        bind(BukkitScheduler.class).toInstance(carolinePlugin.getServer().getScheduler());

        bind(Scheduler.class).annotatedWith(Names.named("syncScheduler")).to(SynchronousScheduler.class).asEagerSingleton();
        bind(Scheduler.class).annotatedWith(Names.named("asyncScheduler")).to(AsynchronousScheduler.class).asEagerSingleton();
    }
}
