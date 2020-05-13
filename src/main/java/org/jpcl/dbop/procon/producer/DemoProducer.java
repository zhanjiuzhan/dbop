package org.jpcl.dbop.procon.producer;

import org.jpcl.dbop.model.Auth;
import org.jpcl.dbop.procon.AbstractProducer;

/**
 * @author Administrator
 */
public class DemoProducer extends AbstractProducer<Auth> {
    public DemoProducer() {
        super(DemoProducer.class.getName());
    }

    @Override
    protected Auth producer() {
        Auth auth = new Auth();
        System.out.println("我进行了生产: " + auth.toString());
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return auth;
    }
}
