package org.jpcl.dbop.procon.cache.queue;


import org.jpcl.dbop.demo.model.Auth;
import org.jpcl.dbop.procon.JcCache;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Administrator
 */
public class LinkedBlockingQueueCache implements JcCache<Auth> {

    private static LinkedBlockingDeque<Auth> queue = new LinkedBlockingDeque<>(10);
    private static volatile AtomicInteger atomicInteger = new AtomicInteger(0);


    @Override
    public int getSize() {
        return atomicInteger.get() == queue.size() ? atomicInteger.get() : -1;
    }

    @Override
    public Auth poll() {
        try {
            Auth auth = queue.takeLast();
            atomicInteger.decrementAndGet();
            return auth;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void put(Auth auth) {
        queue.offerFirst(auth);
        atomicInteger.incrementAndGet();
    }
}
