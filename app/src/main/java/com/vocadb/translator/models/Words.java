package com.vocadb.translator.models;


public class Words {
    public String Voca;
    public String Part;
    public String Level;
    public String Text;
	private boolean is_deleted;
	private int e_level;
	private int id;
    public Words(String _voca, String _part, String _level, String _text ,int e_level){
        this.Voca = _voca;
        this.Part = _part;
        this.Level = _level;
        this.Text = _text;
        this.e_level = e_level;
    }
	public Words() {
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
	public String getPart() {
		// TODO Auto-generated method stub
		return Part;
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
	public int getilevel() {
		// TODO Auto-generated method stub
		return e_level;
	}
	public void setID(int id) {
		// TODO Auto-generated method stub
		this.id = id;
	}
	public void setVoca(String voca) {
		// TODO Auto-generated method stub
		this.Voca = voca;
	}
	public void setTdata(String t_data) {
		// TODO Auto-generated method stub
		this.Text = t_data;
	}
	public void setPart(String part) {
		// TODO Auto-generated method stub
		this.Part = part;
	}
	public void setLevel(String level) {
		// TODO Auto-generated method stub
		this.Level = level;
	}
	public void setilabel(int ilebel) {
		// TODO Auto-generated method stub
		this.e_level = ilebel;
	}
	
	
}
