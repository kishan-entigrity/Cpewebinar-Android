package com.entigrity.model;

public class SaveTopicsSignUpModel {

    private int id = 0;
    private boolean ischecked=false;

    public int getId() {
        return id;
    }

    public boolean isIschecked() {
        return ischecked;
    }

    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTopicsofinterest() {
        return topicsofinterest;
    }

    public void setTopicsofinterest(String topicsofinterest) {
        this.topicsofinterest = topicsofinterest;
    }

    private String topicsofinterest = "";

}
