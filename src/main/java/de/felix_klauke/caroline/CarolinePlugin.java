package de.felix_klauke.caroline;

import com.google.inject.Guice;
import com.google.inject.Injector;
import de.felix_klauke.caroline.module.CarolineModule;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The bukkit plugin used when this library is run as a plugin.
 *
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class CarolinePlugin extends JavaPlugin {

    /**
     * The injector to create our instances.
     */
    private Injector injector;

    /**
     * The instance of the caroline application.
     */
    private CarolineApplication carolineApplication;

    @Override
    public void onLoad() {
        injector = Guice.createInjector(new CarolineModule(this));
        carolineApplication = injector.getInstance(CarolineApplication.class);
    }

    @Override
    public void onEnable() {
        carolineApplication.initialize();
    }

    @Override
    public void onDisable() {
        carolineApplication.destroy();
    }
}
