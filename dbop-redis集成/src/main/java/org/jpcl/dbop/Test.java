package org.jpcl.dbop;

import java.io.Serializable;

/**
 * @author Administrator
 */
public class Test  {
    private char chr = 'a';
    private byte by = 1;
    private short snum = 1;

    private transient int num = 2;
    private long lnum = 3L;
    private float fnum = 32.1f;
    private double dnum = 122.2;
    private boolean flag = false;

    public char getChr() {
        return chr;
    }

    public void setChr(char chr) {
        this.chr = chr;
    }

    public byte getBy() {
        return by;
    }

    public void setBy(byte by) {
        this.by = by;
    }

    public short getSnum() {
        return snum;
    }

    public void setSnum(short snum) {
        this.snum = snum;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public long getLnum() {
        return lnum;
    }

    public void setLnum(long lnum) {
        this.lnum = lnum;
    }

    public float getFnum() {
        return fnum;
    }

    public void setFnum(float fnum) {
        this.fnum = fnum;
    }

    public double getDnum() {
        return dnum;
    }

    public void setDnum(double dnum) {
        this.dnum = dnum;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "Test{" +
                "chr=" + chr +
                ", by=" + by +
                ", snum=" + snum +
                ", num=" + num +
                ", lnum=" + lnum +
                ", fnum=" + fnum +
                ", dnum=" + dnum +
                ", flag=" + flag +
                '}';
    }
}
