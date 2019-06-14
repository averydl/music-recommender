package org.derek.assignment4;

public class Person {
    private int id;
    private String name;

    public Person(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Person) {
            return ((Person)obj).getId() == this.id;
        } else {
            return false;
        }
    }
}
