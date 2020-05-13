package org.jpcl.dbop.procon;

/**
 * 生产者接口
 * @author Administrator
 */
public abstract class AbstractProducer<T> extends AbstractProCon<T> {

    public AbstractProducer(String name) {
        super(name);
    }

    @Override
    void execute() {
        T t = producer();
        jcCache.put(t);
    }

    /**
     * 进行生产
     * @return
     */
    protected abstract T producer();
}
