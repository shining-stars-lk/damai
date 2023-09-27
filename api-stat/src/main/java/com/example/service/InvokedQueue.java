package com.example.service;


import com.example.handler.InvokedHandler;
import com.example.model.InvokedInfo;
import com.example.util.Context;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

public class InvokedQueue {
    private static Logger log = Logger.getLogger(InvokedQueue.class.toString());

    private volatile static ConcurrentLinkedQueue<InvokedInfo> queue = new ConcurrentLinkedQueue();

    public static void add(InvokedInfo invokedInfo) {
        queue.add(invokedInfo);
    }

    public static void pause() {
        try {
            synchronized (queue) {
                queue.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void wake() {
        try {
            synchronized (queue) {
                queue.notifyAll();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void onInveked() {
        int n = 0;
        while (true) {
            try {
                if (queue.isEmpty()) {
                    n++;
                    if (n > 20) {
                        n = 0;
                        pause();
                    }
                    continue;
                }
                InvokedInfo poll = queue.poll();
                if (poll == null) {
                    continue;
                }
                for (InvokedHandler invokedHandler : Context.getInvokedHandlers()) {
                    invokedHandler.onInvoked(poll.getCurrent(), poll.getParent(), poll.getNames(), poll.getValues());
                    if (null != poll.getException()) {
                        invokedHandler.onException(poll.getCurrent(), poll.getParent(), poll.getException(), poll.getNames(), poll.getValues());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
