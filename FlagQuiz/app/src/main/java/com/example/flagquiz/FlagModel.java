package com.example.flagquiz;

public class FlagModel {

    int index;
    String name;
    String flag;

    public FlagModel() {

    }

    public FlagModel(int index, String name, String flag) {
        this.index = index;
        this.name = name;
        this.flag = flag;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
