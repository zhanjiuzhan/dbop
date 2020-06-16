package org.jpcl.dbop.procon;

/**
 * 消费者接口
 * @author Administrator
 */
public abstract class AbstractConsumer<T> extends AbstractProCon<T> {

    public AbstractConsumer(String name) {
        super(name);
    }

    @Override
    void execute() {
        T t = jcCache.poll();
        consumer(t);
    }

    /**
     * 进行消费
     * @param t
     */
    protected abstract void consumer(T t);
}
