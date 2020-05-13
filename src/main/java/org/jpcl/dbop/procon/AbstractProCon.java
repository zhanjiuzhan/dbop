package org.jpcl.dbop.procon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Administrator
 */
public abstract class AbstractProCon<T> implements Runnable {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    JcCache<T> jcCache;
    private boolean running = false;
    private boolean status = false;

    private String name;

    AbstractProCon(String name) {
        this.name = name;
    }

    protected String getName() {
        return name;
    }

    @Override
    public void run() {
        this.running = true;
        this.status = true;
        long start = System.currentTimeMillis();
        while (this.running) {
            execute();
        }
        long end = System.currentTimeMillis();
        logger.info("执行结束。[{}] 的执行时间是: [{}]", this.getName(), end -start);
        this.status = false;
    }

    protected void cancel() {
        this.running = false;
    }

    protected boolean isClose() {
        return !this.status;
    }

    protected void setJcCache(JcCache<T> jcCache) {
        this.jcCache = jcCache;
    }

    /**
     * 执行任务
     */
    abstract void execute();
}
