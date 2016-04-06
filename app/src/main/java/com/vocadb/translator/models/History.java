package com.vocadb.translator.models;

public class History {

	private String slanCode;
	private String tlanCode;
	private String source_text;
	private String target_text;
	private boolean isfav;
	private int id;
	private String time;

	public History(int history_id, String slanCode, String tlanCode, String source_text,
			String target_text,boolean isfav, String time) {
		this.id = history_id;
		this.slanCode = slanCode;
		this.tlanCode = tlanCode;
		this.source_text = source_text;
		this.target_text = target_text;
		this.isfav = isfav;
		this.time = time;
	}

	public History() {
		// TODO Auto-generated constructor stub
	}

	public String getHistorySL() {
		// TODO Auto-generated method stub
		return slanCode;
	}

	public String getHistoryTL() {
		// TODO Auto-generated method stub
		return tlanCode;
	}

	public String getHistorySLData() {
		// TODO Auto-generated method stub
		return source_text;
	}

	public String getHistoryTLData() {
		// TODO Auto-generated method stub
		return target_text;
	}

	public boolean isFav() {
		// TODO Auto-generated method stub
		return isfav;
	}

	public void setID(int id) {
		this.id = id;
	}

	public void setTL(String tl) {
		// TODO Auto-generated method stub
		this.tlanCode = tl;
	}

	public void setSL(String sl) {
	this.slanCode = sl;	
	}

	public void setSLData(String sl_data) {
		// TODO Auto-generated method stub
		this.source_text = sl_data;
	}

	public void setTLData(String tl_data) {
		// TODO Auto-generated method stub
		this.target_text = tl_data;
	}

	public void setFav(boolean isfav) {
		// TODO Auto-generated method stub
		this.isfav = isfav;
	}

	public int getId() {
		// TODO Auto-generated method stub
		return id;
	}

	public String getTime() {
		// TODO Auto-generated method stub
		return time;
	}

	public void setTime(String time) {
		// TODO Auto-generated method stub
		this.time = time;
	}

}
