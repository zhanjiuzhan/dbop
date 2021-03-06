package org.jpcl.dbop.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
public class Student implements Serializable {
    private String name;
    private int age;
    private long phone;
    private Date update;
    private String[] des;
    private Hobby hobby;
    private List<Parent> parents;
    private Map<String, Hobby> map;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public Date getUpdate() {
        return update;
    }

    public void setUpdate(Date update) {
        this.update = update;
    }

    public String[] getDes() {
        return des;
    }

    public void setDes(String[] des) {
        this.des = des;
    }


    public Hobby getHobby() {
        return hobby;
    }

    public void setHobby(Hobby hobby) {
        this.hobby = hobby;
    }

    public List<Parent> getParents() {

        return parents;
    }

    public void setParents(List<Parent> parents) {
        this.parents = parents;
    }

    public Map<String, Hobby> getMap() {
        return map;
    }

    public void setMap(Map<String, Hobby> map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", phone=" + phone +
                ", update=" + update +
                ", des=" + Arrays.toString(des) +
                ", hobby=" + hobby.toString() +
                ", parents=" + Arrays.toString(parents.toArray()) +
                ", map=" + map.toString() +
                '}';
    }
}
