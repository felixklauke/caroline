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

import de.felixklauke.caroline.core.emitter.EventEmitter;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import javax.inject.Inject;

/**
 * @author Felix Klauke <fklauke@itemis.de>
 */
public class RxCaroline {

    /**
     * The composite disposable to clean up when the plugin gets disabled.
     */
    private static CompositeDisposable compositeDisposable = new CompositeDisposable();

    /**
     * The bukkit plugin instance.
     */
    @Inject
    private static Plugin plugin;

    /**
     * The bukkit plugin manager.
     */
    @Inject
    private static PluginManager pluginManager;

    /**
     * Observe on a specific spigot event.
     *
     * @param eventClazz  The class of the event.
     * @param <EventType> The type of the event.
     * @return The observable.
     */
    public static <EventType extends Event> Observable<EventType> observeEvent(Class<EventType> eventClazz) {
        return observeEvent(eventClazz, EventPriority.NORMAL);
    }

    /**
     * Observe on a specific event with a given event priority.
     *
     * @param eventClazz    The class of the event.
     * @param eventPriority The event priority.
     * @param <EventType>   The type of the event.
     * @return The observable.
     */
    public static <EventType extends Event> Observable<EventType> observeEvent(Class<EventType> eventClazz, EventPriority eventPriority) {
        return observeEvent(eventClazz, eventPriority, false);
    }

    /**
     * Observe on a specific event and ignore cancelled events or not.
     *
     * @param eventClazz      The class of the event.
     * @param ignoreCancelled If we should ignore cancelled events.
     * @param <EventType>     The type of the event.
     * @return The observable.
     */
    public static <EventType extends Event> Observable<EventType> observeEvent(Class<EventType> eventClazz, boolean ignoreCancelled) {
        return observeEvent(eventClazz, EventPriority.NORMAL, ignoreCancelled);
    }

    /**
     * Observe on a specific event with a given event priority and ignore cancelled events or not.
     *
     * @param eventClazz      The class of the event.
     * @param eventPriority   The event priority.
     * @param ignoreCancelled If we should ignore cancelled events.
     * @param <EventType>     The type of the event.
     * @return The observable.
     */
    public static <EventType extends Event> Observable<EventType> observeEvent(Class<EventType> eventClazz, EventPriority eventPriority, boolean ignoreCancelled) {
        return observeEvent(new EventEmitter<EventType>(eventClazz, ignoreCancelled, eventPriority, pluginManager, plugin));
    }

    /**
     * Create an observable based on the given emitter.
     *
     * @param eventEmitter The emitter.
     * @param <EventType>  The type of the event.
     * @return The observable.
     */
    private static <EventType extends Event> Observable<EventType> observeEvent(EventEmitter<EventType> eventEmitter) {
        return Observable.create(eventEmitter)
                .doOnSubscribe(compositeDisposable::add)
                .doOnDispose(() -> HandlerList.unregisterAll(eventEmitter.getListener()));
    }

    /**
     * Clean da shiat up.
     */
    public static void reset() {
        compositeDisposable.dispose();
    }
}
