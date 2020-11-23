package org.jpcl.dbop;

import java.io.Serializable;
import java.util.Arrays;

public class Test1 implements Serializable {
    private static final long serialVersionUID = -8509186803781510379L;

    private String test1 = "对象作为参数";

    private Test test2;

    private String[] strings = {"aa", "bb"};

    private Test[] tests;

    public String getTest1() {
        return test1;
    }

    public void setTest1(String test1) {
        this.test1 = test1;
    }

    public Test getTest2() {
        return test2;
    }

    public void setTest2(Test test2) {
        this.test2 = test2;
    }

    public String[] getStrings() {
        return strings;
    }

    public void setStrings(String[] strings) {
        this.strings = strings;
    }

    public Test[] getTests() {
        return tests;
    }

    public void setTests(Test[] tests) {
        this.tests = tests;
    }

    @Override
    public String toString() {
        return "Test1{" +
                "test1='" + test1 + '\'' +
                ", test2=" + test2 +
                ", strings=" + Arrays.toString(strings) +
                ", tests=" + Arrays.toString(tests) +
                '}';
    }
}
