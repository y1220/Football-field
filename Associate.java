package it.polito.oop.futsal;

public class Associate {
	private String First; 
	private String Last;
	private String Phone;
	private int Code;
	
	public Associate(String first, String last, String phone, int code) {
		super();
		First = first;
		Last = last;
		Phone = phone;
		Code = code; 
		
	}
	public String getFirst() {
		return First;
	}
	public String getLast() {
		return Last;
	}
	public String getPhone() {
		return Phone;
	}
	public int getCode() {
		return Code;
	}
	
	
}
