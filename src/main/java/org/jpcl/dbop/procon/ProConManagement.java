package org.jpcl.dbop.procon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 生产者消费者管理器 用来统一规划
 * @author Administrator
 */
public class ProConManagement<T> {

    private final static Logger logger = LoggerFactory.getLogger(ProConManagement.class);
    private static CopyOnWriteArrayList<AbstractConsumer> consumers;
    private static CopyOnWriteArrayList<AbstractProducer> producers;
    private static JcCache cache = null;

    static {
        consumers = new CopyOnWriteArrayList<>();
        producers = new CopyOnWriteArrayList<>();
    }

    public ProConManagement(JcCache<T> cache) {
        ProConManagement.cache = cache;
    }

    /**
     * 添加一个消费者
     * @param consumer
     * @return
     */
    public static int addConsumer(AbstractConsumer consumer) {
        addProCom(consumer);
        // 消费者添加到容器中
        consumers.add(consumer);
        int size = consumers.size();
        logger.info("添加了一个消费者 [{}], 消费者数量: [{}]", consumer.getName(), size);
        return size;
    }

    /**
     * 添加一个生产者
     * @param producer
     * @return
     */
    public static int addProducer(AbstractProducer producer) {
        addProCom(producer);
        // 生产者添加到容器中
        producers.add(producer);
        int size = producers.size();
        logger.info("添加了一个生产者 [{}], 生产者数量: [{}]", producer.getName(), size);
        return size;
    }

    private static void addProCom(AbstractProCon proCon) {
        // 设置使用的仓库
        proCon.setJcCache(cache);
        // 消费者开始动作
        new Thread(proCon).start();
    }


    /**
     * 去掉一个消费者
     * @param consumer
     * @return
     */
    public static int removeConsumer(AbstractConsumer consumer) {
        removeProCon(consumer);
        consumers.remove(consumer);
        int size = consumers.size();
        logger.info("删除了一个消费者 [{}], 消费者数量: [{}]", consumer.getName(), size);
        return size;
    }

    /**
     * 去掉一个生产者
     * @param producer
     * @return
     */
    public static int removeProducer(AbstractProducer producer) {
        removeProCon(producer);
        producers.remove(producer);
        int size = producers.size();
        logger.info("删除了一个生产者 [{}], 生产者数量: [{}]", producer.getName(), size);
        return size;
    }

    private static void removeProCon(AbstractProCon proCon) {
        // 使得该生产者执行结束
        proCon.cancel();
        while (!proCon.isClose()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 取消对从库的使用权
        proCon.setJcCache(null);
    }

    /**
     * 取得生产者的数量
     * @return
     */
    public static int getSizeOfProducer() {
        return producers.size();
    }

    /**
     * 取得消费者的数量
     * @return
     */
    public static int getSizeOfConsumer() {
        return consumers.size();
    }

    /**
     * 取得所有的消费者
     * @return
     */
    public static CopyOnWriteArrayList<AbstractConsumer> getConsumers() {
        return consumers;
    }

    /**
     * 取得所有的生产者
     * @return
     */
    public static CopyOnWriteArrayList<AbstractProducer> getProducers() {
        return producers;
    }

    /**
     * 取得缓存容器中 当前数据的多少
     * @return
     */
    public static int getCacheSize() {
        if (ProConManagement.cache == null) {
            logger.error("缓存没有初始化");
            throw new RuntimeException("缓存没有初始化");
        }
        return ProConManagement.cache.getSize();
    }
}
