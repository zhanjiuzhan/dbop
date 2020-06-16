package org.jpcl.dbop.procon.consumer;

import org.jpcl.dbop.procon.AbstractConsumer;

/**
 * @author Administrator
 */
public class DemoConsumer<Auth> extends AbstractConsumer<Auth> {
    public DemoConsumer() {
        super(DemoConsumer.class.getName());
    }

    @Override
    protected void consumer(Auth auth) {
        System.out.println("我进行了消费: " + auth.toString());
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
