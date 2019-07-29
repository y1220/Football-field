package it.polito.oop.futsal;

public class Field implements FieldOption{	
	private boolean Indoor;
	private boolean Heating;
	private boolean Ac;
	private int fnum;
	private int occupation=0;
	public Field(boolean indoor, boolean heating, boolean ac) {
		super();
		Indoor = indoor;
		Heating = heating;
		Ac = ac;
		
	}

	public boolean isIndoor() {
		return Indoor;
	}
	public boolean isAc() {
		return Ac;
	}
	public boolean isHeating() {
		return Heating;
	}
	public void setFnum(int fnum) {
		this.fnum = fnum;
	}
	
	@Override
	public int getField() {
		// TODO Auto-generated method stub
		return fnum;
	}
	@Override
	public int getOccupation() {
		// TODO Auto-generated method stub
		return occupation;
	}
	
	public void increaseOccupation() {
		occupation++;
	}

}


