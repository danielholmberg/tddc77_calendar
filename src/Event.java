import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Event {
	// Name = Notes info.
	private String eventName = "", eventInfo = "", eventDate = "", eventTime = "", eventLoca = "";
	private String[] event = new String[5]; // [Name, Note, Date, Time, Location]
	// All events in one place.
	private ArrayList<String[]> allEvents = new ArrayList<String[]>();
	private Scanner input = new Scanner(System.in);
	
	/**
	 * Gets all the essentials about the event and samples them to an String[]/Event.
	 * Then adding the event to the ArrayList<String[]> to hold all events in one place.
	 * @param NAME
	 * 			The name of the event.
	 * @param INFO
	 * 			The information about the event.
	 * @param DATE
	 * 			The date of the event.
	 * @param TIME
	 * 			The time of the event.
	 * @param LOCA
	 * 			The location of the event.
	 */
	public String[] addNewEventInfo(String NAME, String INFO, String DATE, String TIME, String LOCA) {
		eventName = NAME.toUpperCase();
		eventInfo = INFO;
		eventDate = DATE;
		eventTime = TIME;
		eventLoca = LOCA;
		
		String[] event = {eventName, eventInfo, eventDate, eventTime, eventLoca};
		this.event = event;
		
		addEventToList();
		return this.event;
	}
	/**
	 * Adds the event to the list.
	 */
	private void addEventToList() {
		allEvents.add(event);
	}
	/**
	 * Reads in a new name of the event.
	 * @return name
	 */
	public String getNewName() {
		String name = input.nextLine();
		if(name.isEmpty()){
			System.out.println("You MUST name your Event!");
			return "EMPTY";
		} else {
			return name;
		}
	}
	/**
	 * Reads in new info of the event.
	 * @return notes
	 */
	public String getNewInfo() {
		String notes = input.nextLine();

		if(notes.isEmpty()){
			return "Unknown";
		} else {
			return notes;
		}
	}
	/**
	 * Metod för att ta in en korrekt datumangivelse.
	 * @return date 
	 * 			Datumet som skrivs in i siffror.
	 */
	public int getNewDate() {
		String dateString = ""; 
		int dateTemp = 0;
		int date = 0; 
		boolean inputValid; 

		do {
			inputValid = false;
			try {
				dateTemp = input.nextInt();
				date = dateTemp;
				dateString = date + "";
				inputValid = true;
			} catch (InputMismatchException e) {
				input.nextLine();
			}
			if (!inputValid) {
				System.out
						.println("Input needs to be numbers only! Try again:");
			} else if (dateString.length() != 8) {
				System.out.println(
						"Input needs to consist of 8 numbers! Try again:");
			}
		} while (!inputValid || dateString.length() != 8);
		// om något är fel med inputen så körs do-while loopen om.

		return date;
	}
	/**
	 * Metod för att ta in en korrekt tidsangivelse.
	 * @return time 
	 * 			Tiden som skrivs in i siffror.
	 */
	public String getNewTime() {
		String time = ""; 
		boolean inputValid; 
		input = new Scanner(System.in);
		do {
			inputValid = false;
			try {
				time = input.nextLine();
				inputValid = true;
			} catch (InputMismatchException e) {
				input.nextLine();
			}
			if (!inputValid) {
				System.out
						.println("Input needs to be numbers only! Try again:");
			} else if (time.length() != 4) {
				System.out.println(
						"Input needs to consist of 4 numbers! Try again:");
			}
		} while (!inputValid || time.length() != 4);
		// om något är fel med inputen så körs do-while loopen om.

		return time;
	}
	/**
	 * Reads in the new location of the event.
	 * @return Location
	 */
	public String getNewLocation(){
		String Location = input.nextLine();

		if(Location.isEmpty()){
			return "Unkown";
		} else {
			return Location;
		}
	}
	/**
	 * Returns the name of the event.
	 * @return eventName
	 */
	public String getEventName() {
		return eventName;
	}
	/**
	 * Returns the info of the event.
	 * @return eventInfo
	 */
	public String getEventInfo(){
		return eventInfo;
	}
	/**
	 * Returns the date of the event.
	 * @return eventDate
	 */
	public String getEventDate(){
		return eventDate;
	}
	/**
	 * Returns the time of the event.
	 * @return eventTime
	 */
	public String getEventTime(){
		return eventTime;
	}
	/**
	 * Returns the location of the event.
	 * @return eventLoca
	 */
	public String getEventLoca(){
		return eventLoca;
	}
	/**
	 * Returns the date of the event as an int-value.
	 * @return eventInt
	 */
	public int[] getEventDateAsInt(){
		String eventDate = "";
		int[] eventInt = new int[2];

		if(this.eventDate.length()<14){
			eventDate += this.eventDate.substring(0, 4);
			eventDate += this.eventDate.substring(5, 7);
			eventDate += this.eventDate.substring(8, 10);
			eventInt[0] = Integer.parseInt(eventDate);
		} else {
			if(this.eventDate.length()>14){
				eventDate += this.eventDate.substring(0, 4);
				eventDate += this.eventDate.substring(5, 7);
				eventDate += this.eventDate.substring(8, 10);
				eventInt[0] = Integer.parseInt(eventDate);
				eventDate = "";
				eventDate += this.eventDate.substring(11, 15);
				eventDate += this.eventDate.substring(16, 18);
				eventDate += this.eventDate.substring(19, 21);
				eventInt[1] = Integer.parseInt(eventDate);
			}
		}
		return eventInt;
	}
	/**
	 * Returns the size of the event.
	 * @return this.event.length
	 */
	public int size(){
		return this.event.length;
	}
	/**
	 * Sets the name of the event to the incoming String name.
	 * @param name
	 */
	public void setEventName(String name){
		eventName=name.toUpperCase();
	}
	/**
	 * Sets the info about the event to the incoming String info.
	 * @param info
	 */
	public void setEventInfo(String info){
		eventInfo=info;
	}
	/**
	 * Sets the date of the event to the incoming String date.
	 * @param date
	 */
	public void setEventDate(String date){
		eventDate=date;
	}
	/**
	 * Sets the time of the event to the incoming String time.
	 * @param time
	 */
	public void setEventTime(String time){
		eventTime=time;
	}
	/**
	 * Sets the location of the event to the incoming String loca.
	 * @param loca
	 */
	public void setEventLoca(String loca){
		eventLoca=loca;
	}
	/**
	 * Prints out the event in a predefined format.
	 */
	public void printEvent(){
		String output = "";
		output += (	
				"~~~~~~~~~~~~~~~~~~~~~~~\n" + 
				"Name: " + eventName + "\n" + 
				"Info: " + eventInfo + "\n" + 
				"Date: " + eventDate + "\n" + 
				"Time: " + eventTime + "\n" +
				"Loca: " + eventLoca);
		System.out.println(output);
	}
}