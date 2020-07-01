package org.jpcl.dbop.model;

import java.io.Serializable;

/**
 * @author Administrator
 */
public class Parent implements Serializable {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Parent{" +
                "name='" + name + '\'' +
                '}';
    }
}
