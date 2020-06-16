package org.jpcl.dbop.procon;

/**
 * 缓存器
 * @author Administrator
 */
public interface JcCache<T> {

    /**
     * 取得缓存中数据量 产品的实际数量
     * @return
     */
    int getSize();

    /**
     * 取到一个产品 取不到的话等待
     * @return
     */
    T poll();

    /**
     * 添加一个产品 满了的话等待
     * @param t
     */
    void put(T t);
}
