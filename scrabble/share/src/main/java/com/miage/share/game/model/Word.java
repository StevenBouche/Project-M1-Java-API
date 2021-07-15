package com.miage.share.game.model;

public class Word {
    private int startX;
    private int startY;
    private int size;
    private int dirX;
    private int dirY;
    private String value;

    public Word(int startX, int startY, int size, int dirX, int dirY, String value){
        this.startX = startX;
        this.startY = startY;
        this.size = size;
        this.dirX = dirX;
        this.dirY = dirY;
        this.value = value;
    }

    public Word(){

    }

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getDirX() {
        return dirX;
    }

    public void setDirX(int dirX) {
        this.dirX = dirX;
    }

    public int getDirY() {
        return dirY;
    }

    public void setDirY(int dirY) {
        this.dirY = dirY;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isMiddle() {
        if(dirX==1&&dirY==0) return startX <= 7 && startX+(size-1)>=7;
        else if(dirX==0&&dirY==1) return startY <= 7 && startY+(size-1)>=7;
        else return false;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("Word : ").append(this.value).append("\n");
        builder.append("Pos X : ").append(this.startX).append("\n");
        builder.append("Pos Y : ").append(this.startY).append("\n");
        builder.append("Dir X : ").append(this.dirX).append("\n");
        builder.append("Dir Y : ").append(this.dirY).append("\n");
        return builder.toString();
    }
}
