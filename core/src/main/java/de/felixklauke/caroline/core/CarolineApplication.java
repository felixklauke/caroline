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

package de.felixklauke.caroline.core;

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
        RxCaroline.reset();
    }
}
