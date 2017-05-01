package com.virjar.vscrawler.event;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by virjar on 17/4/30.
 */
@Slf4j
public class EventLoop {
    private static EventLoop instance = new EventLoop();
    private AtomicBoolean isRunning = new AtomicBoolean(false);

    public static EventLoop getInstance() {
        return instance;
    }

    private ConcurrentMap<String, List<EventHandler>> allhandlers = Maps.newConcurrentMap();

    private EventLoop() {
    }

    private ConcurrentLinkedQueue<Event> eventQueue = new ConcurrentLinkedQueue<>();

    public void offerEvent(Event event) {
        /*
         * Preconditions.checkArgument( allhandlers.containsKey(event.getTopic()) &&
         * allhandlers.get(event.getTopic()).size() > 0, "cannot find handle for event:{}", event.getTopic());
         */
        if (!allhandlers.containsKey(event.getTopic()) || allhandlers.get(event.getTopic()).size() < 0) {
            log.warn("cannot find handle for event:{}", event.getTopic());
            return;
        }
        if (event.isSync()) {
            disPatch(event);
        } else {
            eventQueue.offer(event);
        }
    }

    public synchronized static void registerHandler(String topic, EventHandler eventHandler) {
        ConcurrentMap<String, List<EventHandler>> allhandlers = instance.allhandlers;
        List<EventHandler> eventHandlers = allhandlers.get(topic);
        if (eventHandler == null) {
            eventHandlers = Lists.newArrayList();
            allhandlers.put(topic, eventHandlers);
        }
        eventHandlers.add(eventHandler);

    }

    public void loop() {
        if (isRunning.compareAndSet(false, true)) {
            new Thread("vsCrawlerEventLoop") {
                @Override
                public void run() {
                    while (isRunning.get()) {
                        Event poll = eventQueue.poll();
                        disPatch(poll);
                    }
                }
            }.start();
        }
    }

    public void disPatch(Event event) {
        String topic = event.getTopic();
        for (EventHandler eventHandler : allhandlers.get(topic)) {
            try {
                eventHandler.handEvent(event);
                if (event.isHandled()) {
                    break;
                }
            } catch (Exception e) {
                log.error("error when hand event:{}", topic, e);
            }
        }

    }
}