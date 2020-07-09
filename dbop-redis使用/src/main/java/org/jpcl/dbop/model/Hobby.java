package org.jpcl.dbop.model;

import java.io.Serializable;

/**
 * @author Administrator
 */
public class Hobby implements Serializable  {
    private String des;

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    @Override
    public String toString() {
        return "Hobby{" +
                "des='" + des + '\'' +
                '}';
    }
}
