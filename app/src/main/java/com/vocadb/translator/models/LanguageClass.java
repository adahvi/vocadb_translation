package com.vocadb.translator.models;

public class LanguageClass {

	private int id;
	private String language_name;
	private boolean selected;
	private String lang_code;
	private String time;

	
	public LanguageClass(String languageName, String langCode, boolean selected) {
		  this.language_name = languageName;
		  this.lang_code= langCode;
		  this.selected = selected;
	}

	public LanguageClass(int id, String languageName, String langCode, boolean selected) {
		this.id = id;
		  this.language_name = languageName;
		  this.lang_code= langCode;
		  this.selected = selected;
	}


	public LanguageClass() {
		// TODO Auto-generated constructor stub
	}


	public void setID(int id) {
	this.id = id;
	}
	public int getID() {
		// TODO Auto-generated method stub
		return id;
	}
	public void setLanguageName(String language_name) {
		this.language_name =language_name;
	}

	

	public String getLanguageName() {
		// TODO Auto-generated method stub
		return language_name;
	}
	
		  
		
		
		 
		 public boolean isSelected() {
		  return selected;
		 }
		 public void setSelected(boolean selected) {
		  this.selected = selected;
		 }


		public String getLangCode() {
			// TODO Auto-generated method stub
			return lang_code;
		}


		public void setLanCode(String lan_code) {
			// TODO Auto-generated method stub
			this.lang_code = lan_code;
		}


		public String getTime() {
			// TODO Auto-generated method stub
			return time;
		}


		public void setTime(String time) {
			// TODO Auto-generated method stub
			this.time  = time;
		}

}
