package com.miage.share.game.model.board;

public enum CaseType {

    LD("LD",1),
    LT("LT",2),
    MD("MD",3),
    MT("MT",4),
    BA("BA",0),
    ST("ST",5);

    private String type;
    private int id;

    CaseType(String type, int id){
        this.setType(type);
        this.setId(id);
    }

    public static CaseType getCaseType(int id){
        for(CaseType type: CaseType.values()){
            if(type.getId() ==id) return type;
        }
        return null;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
