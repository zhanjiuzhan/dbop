package org.jpcl.dbop;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Test3 implements Serializable {
    private static final long serialVersionUID = -9000921713746830606L;

    private Map<String, String> map1 = new HashMap<String, String>(1){{put("1", "2");}};
    private Map<String, Test> map2 = new HashMap<String, Test>(1){{put("1", new Test());}};

    public Map<String, String> getMap1() {
        return map1;
    }

    public void setMap1(Map<String, String> map1) {
        this.map1 = map1;
    }


    public Map<String, Test> getMap2() {
        return map2;
    }

    public void setMap2(Map<String, Test> map2) {
        this.map2 = map2;
    }

    @Override
    public String toString() {
        return "Test3{" +
                "map1=" + map1 +
                ", map2=" + map2 +
                '}';
    }
}
