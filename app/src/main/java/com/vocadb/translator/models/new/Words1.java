package com.vocadb.translator.models;

/**
 * Created by Adah Valeña on 4/5/2016.
 */
public class Words1 {

    private String voca;
    private String part;
    private String level;
    private String text;
    private boolean isDeleted;
    private int eLevel;
    private int id;

    public Words1(String _voca, String _part, String _level, String _text ,int _eLevel) {
        this.voca = _voca;
        this.part = _part;
        this.level = _level;
        this.text = _text;
        this.eLevel = _eLevel;
    }

    public String getVoca() {
        return voca;
    }

    public void setVoca(String voca) {
        this.voca = voca;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        level = level;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        text = text;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public int geteLevel() {
        return eLevel;
    }

    public void seteLevel(int eLevel) {
        this.eLevel = eLevel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
