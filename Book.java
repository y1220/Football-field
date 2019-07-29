package it.polito.oop.futsal;


public class Book {
	private int fnum;
	private int aid;
	private String startingTime;
	private int bid;
	/**
	 * @param fnum
	 * @param aid
	 * @param startingTime
	 */
	
	public Book(int fnum, int aid, String startingTime, int bid) {
		super();
		this.fnum = fnum;
		this.aid = aid;
		this.startingTime = startingTime;
		this.bid = bid;
	}
	public int getFnum() {
		return fnum;
	}
	public int getAid() {
		return aid;
	}
	public String getStartingTime() {
		return startingTime;
	}
	public int getBid() {
		return bid;
	}
	
}
