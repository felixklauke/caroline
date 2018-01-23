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
