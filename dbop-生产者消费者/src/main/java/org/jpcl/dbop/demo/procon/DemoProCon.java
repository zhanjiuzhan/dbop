package org.jpcl.dbop.demo.procon;

import org.jpcl.dbop.demo.model.Auth;
import org.jpcl.dbop.procon.JcCache;
import org.jpcl.dbop.procon.ProConManagement;
import org.jpcl.dbop.procon.cache.queue.LinkedBlockingQueueCache;
import org.jpcl.dbop.procon.consumer.DemoConsumer;
import org.jpcl.dbop.procon.producer.DemoProducer;

/**
 * @author Administrator
 */
public class DemoProCon {
    public static void main(String[] args) {
        JcCache<Auth> cache = new LinkedBlockingQueueCache();
        ProConManagement.initProConManagement(cache);
        DemoProducer p = new DemoProducer();
        ProConManagement.addProducer(p);
        DemoConsumer c = new DemoConsumer();
        ProConManagement.addConsumer(c);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ProConManagement.removeProducer(p);
        ProConManagement.removeConsumer(c);
        System.out.println("程序运行结束");
        System.out.println(cache.toString() + ":" + ProConManagement.getCacheSize());
    }
}
