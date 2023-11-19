package com.example.chickens;

public class ChickenModel {

    private int id;
    private String name;
    private int egg_num;
    private String type;

    public ChickenModel(int id, String name, int egg_num, String type) {
        this.id = id;
        this.name = name;
        this.egg_num = egg_num;
        this.type = type;
    }

    public ChickenModel() {
    }

    @Override
    public String toString() {
        return "ChickenModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", egg_num=" + egg_num +
                ", type='" + type + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEgg_num() {
        return egg_num;
    }

    public void setEgg_num(int egg_num) {
        this.egg_num = egg_num;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
