package Entity;

import java.io.Serializable;

/**
 * Created by CharlesGao on 15-08-16.
 */
public class Student implements Serializable {

    private int id;
    private int age;
    private String name;

    public Student(int id, String name, int age){
        this.age = age;
        this.id = id;
        this.name = name;
    }

    private Student(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }



}
