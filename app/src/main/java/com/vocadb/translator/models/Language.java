package com.vocadb.translator.models;

/**
 * Created by User on 4/5/2016.
 */
public class Language {

    private int id;
    private String languageName;
    private int selected;
    private String languageCode;
    private String time;

    public Language() {
        // TODO Auto-generated constructor stub
    }

    public Language(String _languageName, String _languageCode, int _selected) {
        this.languageName = _languageName;
        this.languageCode = _languageCode;
        this.selected = _selected;
    }

    public Language(int _id, String _languageName, String _languageCode, int _selected) {
        this.id = _id;
        this.languageName = _languageName;
        this.languageCode = _languageCode;
        this.selected = _selected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }
}
