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

package de.felixklauke.caroline.core.emitter;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

/**
 * A custom event emitter that will emit bukkit events into observables.
 *
 * @param <EventType> The type of the event.
 */
public class EventEmitter<EventType> implements ObservableOnSubscribe<EventType> {

    /**
     * The bukkit skeleton listener.
     */
    private final Listener listener = new Listener() {
    };

    /**
     * The clazz of the event.
     */
    private final Class<? extends Event> eventClazz;

    /**
     * if the listener should ignore cancelled events.
     */
    private final boolean ignoreCancelled;

    /**
     * The priority of the listener.
     */
    private final EventPriority eventPriority;

    /**
     * The bukkit plugin manager.
     */
    private final PluginManager pluginManager;

    /**
     * The bukkit plugin instance.
     */
    private final Plugin plugin;

    /**
     * Create a new event emitter.
     *
     * @param eventClazz      The class of the event..
     * @param ignoreCancelled If cancelled events should be ignored.
     * @param eventPriority   The event priority.
     * @param pluginManager   The bukkit pluginManager manager.
     * @param plugin          The bukkit plugin instance.
     */
    public EventEmitter(Class<? extends Event> eventClazz, boolean ignoreCancelled, EventPriority eventPriority, PluginManager pluginManager, Plugin plugin) {
        this.eventClazz = eventClazz;
        this.ignoreCancelled = ignoreCancelled;
        this.eventPriority = eventPriority;
        this.pluginManager = pluginManager;
        this.plugin = plugin;
    }

    @Override
    public void subscribe(ObservableEmitter<EventType> observableEmitter) {
        pluginManager.registerEvent(eventClazz, listener, eventPriority, (listener1, event) -> {
            if (eventClazz.isAssignableFrom(event.getClass())) {
                observableEmitter.onNext((EventType) event);
            }
        }, plugin, ignoreCancelled);
    }

    /**
     * Get the skeleton listener.
     *
     * @return The listener.
     */
    public Listener getListener() {
        return listener;
    }
}
