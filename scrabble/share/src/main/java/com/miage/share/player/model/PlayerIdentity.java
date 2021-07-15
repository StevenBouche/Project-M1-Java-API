package com.miage.share.player.model;

public class PlayerIdentity {

    private String id;
    private String name;
    private String URL;

    public PlayerIdentity(){

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getURL() {
        return URL;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    @Override
    public String toString(){
        return String.format("{ id : %s, name : %s, URL : %s }",this.id,this.name,this.URL);
    }


}
