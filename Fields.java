package it.polito.oop.futsal;

import java.util.ArrayList;
import static java.util.Comparator.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Represents a infrastructure with a set of playgrounds, it allows teams
 * to book, use, and  leave fields.
 *
 */
public class Fields {
	//          1 ++    
	private Map<Integer,Field> fields = new HashMap<>();
	private Map<Integer,Associate> associates = new HashMap<>();
	private Map<Integer,Book> books = new HashMap<>();
	

	
	private int fnum=1;
	private int bnum=1;
	private String OpeningTime;
	private String ClosingTime;
   
    public static class Features {
        public final boolean indoor; // otherwise outdoor
        public final boolean heating;
        public final boolean ac;
        public Features(boolean i, boolean h, boolean a) {
            this.indoor=i; this.heating=h; this.ac = a;
        }
		public boolean isIndoor() {
			return indoor;
		}
		public boolean isHeating() {
			return heating;
		}
		public boolean isAc() {
			return ac;
		}
        
    }
    // read features and put the field into MAP "fields"
    public void defineFields(Features... features) throws FutsalException {
    	for(Features f:features) {
        if(((f.indoor==false)&&(f.heating==true))||((f.indoor==false)&&(f.ac==true)))throw new
        FutsalException("");
        Field fi = new Field(f.indoor,f.heating,f.ac);
        fields.put(fnum, fi);
        fi.setFnum(fnum);
        fnum++;
    	}
    }
    
    public long countFields() {
    	long cnt=0;
    	for(Field f:fields.values()) {
    	//while(fields.size()-cnt!=0) {// alternative solution
    		cnt++;
    	}
        return cnt;
    	//return fields.size();// the previous one also worked though this way is simpler
    }

    public long countIndoor() {
    	long cnt=0;
    	for(Field f:fields.values()) {
    		if(f.isIndoor()==true)
    		cnt++;
    	}
        return cnt;
    }
    
    public String getOpeningTime() {
        return OpeningTime;
    }
    
    public void setOpeningTime(String time) {
    	this.OpeningTime=time;
    }
    
    public String getClosingTime() {
        return ClosingTime;
    }
    
    public void setClosingTime(String time) {
    	this.ClosingTime=time;
    }
    
    int anum=1;
    public int newAssociate(String first, String last, String mobile) {
    	//Field fi = new Field(f.indoor,f.heating,f.ac);
    	Associate a = new Associate(first,last,mobile,anum);
    	associates.put(anum, a);
    	anum++;
        return anum-1;
    }
    
    public String getFirst(int partyId) throws FutsalException {
    	if((!associates.containsKey(partyId))||(associates.get(partyId).getFirst()==null)) throw new FutsalException("");
        return associates.get(partyId).getFirst();
    }
    
    public String getLast(int associate) throws FutsalException {
    	if((!associates.containsKey(associate))||(associates.get(associate).getLast()==null)) throw new FutsalException("");
        return associates.get(associate).getLast();
    }
    
    public String getPhone(int associate) throws FutsalException {
    	if((!associates.containsKey(associate))||(associates.get(associate).getPhone()==null)) throw new FutsalException("");
        return associates.get(associate).getPhone();
    }
    
    public int countAssociates() {
    	int cnt=0;
    	for(Associate a:associates.values())
    	//while(associates.size()-cnt!=0)
    		cnt++;
        return cnt;
    	//return associates.size();// the previous one also worked though this way is simpler
    }
    
    public void bookField(int field, int associate, String time) throws FutsalException {	 
    	String[] hhmm = OpeningTime.split(":");
        String mm = hhmm[1];// read minutes of opening time
    	String[] Hhmm = time.split(":");
    	String Mm = Hhmm[1];//read minutes of time which we receive
    	if((!fields.containsKey(field))||((!associates.containsKey(associate))||(!mm.equals(Mm))))throw new FutsalException();
    	Book b = new Book(field, associate, time, bnum);
    	books.put(bnum,b);
    	fields.get(field).increaseOccupation();
    	bnum++;
    }

    public boolean isBooked(int field, String time) {
    	for(Book b:books.values()) //check two conditions -> if satisfied return true, if not false 
    		if((b.getFnum()==field)&&(b.getStartingTime().equals(time)))return true;return false;
    }
 
    public int getOccupation(int field) {
    	return fields.get(field).getOccupation();
    }

    List<FieldOption> ans = new ArrayList<>();
    public List<FieldOption> findOptions(String time, Features required){
    	int cnt=0, flag=0;
    	List<Integer> tmp = new ArrayList<>();// put the field number which satisfied condition of feature
    	for(Field f:fields.values()) {cnt++;// check the condition of Features
    		if(required.isIndoor()==true){if(f.isIndoor()==false) flag=1;}
    		if(required.isHeating()==true){if(f.isHeating()==false) flag=1;}
    		if(required.isAc()==true){if(f.isAc()==false) flag=1;}
    		if(flag==0) tmp.add(cnt);flag=0;}//once we get flag =1, we don't register them
    	for(int i:tmp) {// check each field number whether it satisfies time or not
    		for(Book b:books.values())                                //not to have duplicate field number 
   			if((b.getFnum()==i)&&(!b.getStartingTime().equals(time)&&(!ans.contains(fields.get(i)))))
   			    ans.add(fields.get(i));	  //field which satisfies condition of feature without any other booking has to be also registerd 
   			if(!books.values().stream().map(bb->bb.getFnum()).anyMatch(bb -> i==bb))
   				ans.add(fields.get(i));}
    	return ans.stream().sorted(comparing(FieldOption::getOccupation).thenComparingInt(FieldOption::getField)).collect(Collectors.toList());
    }

    
    public long countServedAssociates() {
    	return books.values().stream().map(b->b.getAid()).distinct().count();
    }
    
    public Map<Integer,Long> fieldTurnover() {// collect the field number from booking map
    	Map<Integer,Long> ans= books.values().stream().collect(Collectors.groupingBy(Book::getFnum, TreeMap::new, Collectors.counting()));
    	for(Field f:fields.values())// add field which doesn't have any booking, count as zero time  
    		if(!ans.containsKey(f.getField())) ans.put(f.getField(), (long) 0);
    	return ans;		
    }
    
    public double occupation() {
    String[] HHmm = OpeningTime.split(":"),cHHmm = ClosingTime.split(":");
   	String oHH = HHmm[0],cHH = cHHmm[0];;
   	Double o = Double.parseDouble(oHH),c = Double.parseDouble(cHH);  ;
   	return books.entrySet().stream().count()/((c-o)*fields.entrySet().stream().count());
    }// sum / (close - open)* number of all field   
}
