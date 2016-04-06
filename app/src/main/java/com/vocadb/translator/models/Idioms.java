package com.vocadb.translator.models;

public class Idioms {
    public String Voca;
    public String Level;
    public String Text;
	private boolean is_deleted;
	private int i_level;
    public Idioms(String _voca, String _level, String _text, int i_level){
        this.Voca = _voca;
        this.Level = _level;
        this.Text = _text;
        this.i_level = i_level;
    }
	public Idioms() {
		// TODO Auto-generated constructor stub
	}
	public String getVoca() {
		// TODO Auto-generated method stub
		return Voca;
	}
	
	public String getLevel() {
		// TODO Auto-generated method stub
		return Level;
	}
	public String getTLData() {
		// TODO Auto-generated method stub
		return Text;
	}
	public boolean isDeleted() {
		// TODO Auto-generated method stub
		return is_deleted;
	}
	public void setIsDeleted(boolean is_deleted) {
		// TODO Auto-generated method stub
		this.is_deleted = is_deleted;
	}
	public int getiLevel() {
		// TODO Auto-generated method stub
		return i_level;
	}
	
}