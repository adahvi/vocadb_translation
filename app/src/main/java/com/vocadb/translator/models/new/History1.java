package com.vocadb.translator.models;

/**
 * History Model
 *
 * Created by Adah Valeña on 4/5/2016.
 */
public class History1 {

    private int id;
    private String sLangCode;
    private String tLangCode;
    private String sourceText;
    private String targetText;
    private boolean isFavorite;
    private String time;

    public History1() {
        // TODO Auto-generated constructor stub
    }

    public History1(int _id, String _sLangCode, String _tLangCode, String _sourceText,
                   String _targetText, boolean _isFavorite, String _time) {
        this.id = _id;
        this.sLangCode = _sLangCode;
        this.tLangCode = _tLangCode;
        this.sourceText = _sourceText;
        this.targetText = _targetText;
        this.isFavorite = _isFavorite;
        this.time = _time;
    }

    public String getsLangCode() {
        return sLangCode;
    }

    public void setsLangCode(String sLangCode) {
        this.sLangCode = sLangCode;
    }

    public String gettLangCode() {
        return tLangCode;
    }

    public void settLangCode(String tLangCode) {
        this.tLangCode = tLangCode;
    }

    public String getSourceText() {
        return sourceText;
    }

    public void setSourceText(String sourceText) {
        this.sourceText = sourceText;
    }

    public String getTargetText() {
        return targetText;
    }

    public void setTargetText(String targetText) {
        this.targetText = targetText;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
