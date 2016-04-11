package com.vocadb.translator.models;

/**
 * Idioms Model
 *
 * Created by Adah Valeña on 4/5/2016.
 */
public class Idioms1 {

    private String voca;
    private String level;
    private String text;
    private int iLevel;
    private boolean isDeleted;

    public Idioms1(String _voca, String _level, String _text, int _iLevel) {
        this.voca = _voca;
        this.level = _level;
        this.text = _text;
        this.iLevel = _iLevel;
    }

    public String getVoca() {
        return voca;
    }

    public void setVoca(String voca) {
        this.voca = voca;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getiLevel() {
        return iLevel;
    }

    public void setiLevel(int iLevel) {
        this.iLevel = iLevel;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
