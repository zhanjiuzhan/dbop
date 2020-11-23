package org.jpcl.dbop;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Test2 implements Serializable {
    private static final long serialVersionUID = -5314309248532388232L;

    private List list = new ArrayList<String>(2){{add("aa"); add("bb");}};

    private List list2 = new ArrayList<Test>(2){{add(new Test()); add(new Test());}};

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public List getList2() {
        return list2;
    }

    public void setList2(List list2) {
        this.list2 = list2;
    }

    @Override
    public String toString() {
        return "Test2{" +
                "list=" + list +
                ", list2=" + list2 +
                '}';
    }
}
