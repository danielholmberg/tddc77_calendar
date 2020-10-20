import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;

public class Calendar extends AbstractCalendarItem {
	// Param for the Constructor Calendar(); and the method execute();
	boolean goBack = false;
	private ArrayList<CalendarItem> items = new ArrayList<CalendarItem>();
	// To hold all the Events.
	protected static ArrayList<Event> allEvents = new ArrayList<Event>();
	protected ArrayList<Event> eventsWithSameName = new ArrayList<Event>();
	// File to load Calendar from.
	final static String DATE_FORMAT = "yyyymmdd";
	final static File DANIELS_CALENDAR = new File("DANIEL_Calendar.txt");
	final static File JONATHANS_CALENDAR = new File("JONATHAN_Calendar.txt");
	final static File DANIELS_OLD_EVENTS = new File("DANIEL_Old_Events.txt");
	final static File JONATHANS_OLD_EVENTS = new File("JONATHAN_Old_Events.txt");
	private Scanner input = new Scanner(System.in);
	private String name = "";

	/**
	 * Constructor that sets the Title for the Calendar and also handles the button BACK for all the menus.
	 * @param title
	 * 			Title for the Calendar.
	 */
	public Calendar(String title) {
		super(title);

		// Här skapas ett menyval för att återgå/avsluta menyn.
		CalendarItem zeroMenu = new AbstractCalendarItem("BACK") {
			@Override
			public void execute() {
				goBack = true;
			}
		};

		if (items.size() == 0 && !title.equals("Private Calendars")) {
			items.add(zeroMenu);
		}
		
	}
	/**
	 * Adds a menu to the menu ArrayList.
	 * @param item
	 * 			A menu.
	 */
	public void addItem(CalendarItem item) {
		items.add(item);
	}
	/**
	 * Makes it possible to add an event to the Calendar. Asks the user for all the essentials.
	 * Name, Info, Date, Time, Location.
	 */
	public void addNewEvent() {
		String date = "", startDate = "", endDate = "";
		String time = "", startTime = "", endTime = "";
		String name = "", note = "", location = "";
		
		boolean timeOK = false; // Variabel som ska ändras för att kunna sätta tid över olika dygn.
		
		Event newEvent = new Event();
		
		do{
			System.out.println("NAME THE EVENT:");
			name = newEvent.getNewName().toUpperCase();
		} while(name.equals("EMPTY"));
		
		System.out.println("ADD NOTES ABOUT THE EVENT:");
		note = newEvent.getNewInfo();
		
		do{
			System.out.println("START DATE (yyyymmdd):");
			startDate = Integer.toString(newEvent.getNewDate());
		} while(!getValidDate(startDate));
		
		do {
			System.out.println("END DATE (yyyymmdd):");
			endDate = Integer.toString(newEvent.getNewDate());
		} while(!(Integer.parseInt(endDate) >= Integer.parseInt(startDate) && getValidDate(endDate)));
		
		date = cleanUpDate(startDate, endDate);
		
		System.out.println();
		
		do{ 
			System.out.println("START TIME (hhmm):");
			startTime = newEvent.getNewTime();
		} while(!(getValidTime(startTime)));

		do {
			System.out.println("END TIME (hhmm):");
			endTime = newEvent.getNewTime();
			timeOK = !((Integer.parseInt(endTime) > Integer.parseInt(startTime)));
			if(date.length()>10) {
				timeOK = false;
			}
		} while (timeOK && (getValidTime(endTime)));
		
		time = cleanUpTime(startTime, endTime);
		
		System.out.println("LOCATION: ");
		location = newEvent.getNewLocation();
		
		newEvent.addNewEventInfo(name, note, date, time, location);
		allEvents.add(newEvent);
		System.out.println("\n*** Event ADDED || DON'T FORGET TO SAVE! *** " + "\n");
	}	
	/**
	 * Reformats the time from hhmm to hh:mm.
	 * @param startTime
	 * 			String of the Start time.
	 * @param endTime
	 * 			String of the End Time.
	 * @return time
	 * 			String of the reformatted time.
	 */
	private static String cleanUpTime(String startTime, String endTime) {
		String time = "";
		time =  startTime.substring(0, 2) + ":" +
				startTime.substring(2, 4) + "-" +
				endTime.substring(0, 2) + ":" +
				endTime.substring(2, 4);
		return time;
	}
	/** 
	* Reformats the date from yyyymmdd to yyyy/mm/dd.
	 * @param startDate
	 * 			String of the Start date.
	 * @param endDate
	 * 			String of the End date.
	 * @return time
	 * 			String of the reformatted date.
	 */
	private static String cleanUpDate(String startDate, String endDate) {
		String date = "";
		if(endDate.equals(startDate)){
			date = 	startDate.substring(0, 4) + "/" +
					startDate.substring(4, 6) + "/" +
					startDate.substring(6, 8);
		} else {
			date =  startDate.substring(0, 4) + "/" +
					startDate.substring(4, 6) + "/" +
					startDate.substring(6, 8) + "-" +
					endDate.substring(0, 4) + "/" +
					endDate.substring(4, 6) + "/" +
					endDate.substring(6, 8);
		}
		return date;
	}
	/**
	 * Method that validates if the input is of a legit date-format.
	 * @param date
	 * 			String of the events date.
	 * @return true 
	 * 			If and only if the date is a valid date after year 2015.
	 */
	private static boolean getValidDate(String date){
		int year = Integer.parseInt(date.substring(0, 4));
		int month = Integer.parseInt(date.substring(4, 6));
		int day = Integer.parseInt(date.substring(6, 8));
		boolean valid = false;
		boolean DAYS_ODD = (day<32 && day>0);
		boolean MONTH = (month<13 && month>0);
		boolean YEAR = (year>2014);
		boolean DAYS_EVEN = (day>0 && day<31);
		boolean isOdd = false;
		boolean isEven = false;
		int[] ODDMonths = {1,3,5,7,8,10,12};
		int[] EVENMonths = {2,4,6,9,11};

		if(YEAR && MONTH && DAYS_ODD){
			for(int i: ODDMonths) {
				if(i == month)
					isOdd = true;
			} // end of for
			for(int i: EVENMonths) {
				if(i == month)
					if(i == 2 && day>28) {
						isEven = false;
					} else {
						isEven = true;
					}
			} // end of for
			if(isOdd && DAYS_ODD) {
				valid = true;
			} else if(isEven && DAYS_EVEN) {
				valid = true;
			} else {
				valid = false;
			}
		}
		return valid;
	}
	/**
	 * Method that validates if the input is of a legit time.
	 * @param time 
	 * 			String of the 4 digit Time that was typed in and read as a String in addNewEvent().
	 * @return true 
	 * 			If and only if the 4 digits corresponds with a valid time.
	 */
	private static boolean getValidTime(String time){
		int hours = Integer.parseInt(time.substring(0, 2));
		int minutes = Integer.parseInt(time.substring(2, 4));
		boolean HOURS = (hours<=24 && hours>0);
		boolean MINUTES = (minutes<60 && minutes>=0);
		
		if(HOURS && MINUTES){
			return true;
		} else{
			return false;
		}
	}
	/**
	 * FUNGERAR TILL 99%, KAN EJ HANTERA TOM RAD MELLAN EVENTEN.
	 * 
	 * Reads the Calendar text-file and saves all the events and its information to the ArrayList allEvents.
	 * This makes it much easier to control the events throughout the program. 
	 * It also checks if there are any events old as 1 month, and thereby saving them to another .txt-file instead os adding them to allEvents.
	 * @param is
	 * 			.txt-File that contains all the events in the Calendar.
	 * @return everything
	 * 			String that returns a message if there are no events in the Calendar-file.
	 * @throws IOException
	 */
	public String load(FileInputStream is) throws IOException {
		String everything = "";
		ArrayList<Event> bucket = new ArrayList<Event>();
		int pastMonth = Integer.parseInt(dateOfToday().substring(5, 7)) - 1;
		
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			
			everything = sb.toString();
			
			if(!everything.isEmpty()) {
				String[] rows = everything.split("\n");
				Event event = new Event();
				int counter = 0;

				for(String x: rows) {
					String[] info = x.split(": ");
					String wanted = info[1];
					if(counter==0)
						event.setEventName(wanted);
					if(counter==1)
						event.setEventInfo(wanted);
					if(counter==2)
						event.setEventDate(wanted);
					if(counter==3)
						event.setEventTime(wanted);
					if(counter==4)
						event.setEventLoca(wanted);
				
					counter++;
					if(counter==5) {
						counter = 0;
						allEvents.add(event);
						event = new Event();
	
					}
				} // end of for

				if(pastMonth == 0) {
					pastMonth = 12;
				}
				System.out.println("Dates Occupied:" + "\n" + "=====================");
				for(Event e: allEvents) {
					System.out.println(e.getEventDate());
					if(Integer.parseInt(e.getEventDate().substring(5, 7)) == pastMonth) {
						saveOldEvent(e);
						bucket.add(e);
					}
				} // end of for 
				for(Event x: bucket) {
					removeEvent(x);
				}
				everything = "";
			} else {
				everything = "*** No Upcoming Events! ***";
			}
		} catch (FileNotFoundException e){
			System.out.println("*** NO CALENDAR FILE FOUND ***");
		}

			return everything;
	}
	/**
	 * Saves the incoming event to a new text-file than the Calendar-file.
	 * @param event
	 * 			The old event that wants to be saved.
	 */
	private void saveOldEvent(Event event) {
		try {
			if (!DANIELS_OLD_EVENTS.exists()) {
				System.err.println("We had to make a new file.");
				DANIELS_OLD_EVENTS.createNewFile();
			}
			
			FileWriter fileWriter = new FileWriter(DANIELS_OLD_EVENTS, true);

			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(
						"Name: " + event.getEventName().toUpperCase() + "\n" + 
						"Info: " + event.getEventInfo() + "\n" + 
						"Date: " + event.getEventDate() + "\n" + 
						"Time: " + event.getEventTime() + "\n" +
						"Loca: " + event.getEventLoca() + "\n"); 
						//+ "~~~~~~~~~~~~~~~~~~~" + "\n");
			bufferedWriter.close();
			System.out.println("      *** OLD EVENTS SUCCESFULLY SAVED ***");
		} catch (IOException e) {
			System.err.println("COULD NOT LOG!!");
		}
	}
	/**
	 * Saves all the events in allEvents to the Calendar-File, buy first clearing the text-file and then adding them in a nice format.
	 * The format that it saves it to, goes hand-in-hand with the load() method.
	 */
	public void save(String name) {
		this.name = name;
		PrintWriter writer = null;
		FileWriter fileWriter = null;
		if (name.equals("Daniel Holmberg")) {
			try {
				writer = new PrintWriter(DANIELS_CALENDAR);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else if (name.equals("Jonathan Brage")) {
			try {
				writer = new PrintWriter(JONATHANS_CALENDAR);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		writer.print("");
		writer.close();
		try {
			if (name.equals("Daniel Holmberg")) {
				if (!DANIELS_CALENDAR.exists()) {
					System.err.println("We had to make a new file.");
					DANIELS_CALENDAR.createNewFile();
				}

				fileWriter = new FileWriter(DANIELS_CALENDAR, true);
			} else if (name.equals("Jonathan Brage")) {
				if (!JONATHANS_CALENDAR.exists()) {
					System.err.println("We had to make a new file.");
					JONATHANS_CALENDAR.createNewFile();
				}

				fileWriter = new FileWriter(JONATHANS_CALENDAR, true);
			}

			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			for (Event event : allEvents) {
				bufferedWriter.write(// printOutEvents(allEvents));
						"Name: " + event.getEventName().toUpperCase() + "\n" + "Info: " + event.getEventInfo() + "\n"
								+ "Date: " + event.getEventDate() + "\n" + "Time: " + event.getEventTime() + "\n"
								+ "Loca: " + event.getEventLoca() + "\n");
				// + "~~~~~~~~~~~~~~~~~~~" + "\n");
			}
			bufferedWriter.close();
			System.out.println("      *** CALENDARS SUCCESFULLY SAVED ***");
			System.out.println("           Number of Events Saved: " + allEvents.size());
		} catch (IOException e) {
			System.err.println("COULD NOT LOG!!");
		}
	}
	/**
	 * Method that returns a String of all the events of today.
	 * @return todaysEvents
	 * 			All the events of today.
	 */
	public String getTodaysEvents(){
		String todaysEvents = "";
		ArrayList<Event> EVENTS = new ArrayList<Event>();
		for(Event event: allEvents) {
			if(event.getEventDate().contains(dateOfToday())) {
				EVENTS.add(event);
			}
		} // end of for
		if(EVENTS.size()==0) {
			todaysEvents = "    *** No Events for Today ***" + "\n";
		} else {
			System.out.println(" [TODAYS EVENTS]: [" + EVENTS.size() + "]");
			todaysEvents += printOutEvents(EVENTS);
		}
		if(!(EVENTS.size()==0))
			todaysEvents += "~~~~~~~~~~~~~~~~~~~~~~\n";
		return todaysEvents;
	}
	/**
	 * Method that returns a String of all the events in current month.
	 * @return
	 */
	public String getThisMonth(){
		String eventsOfThisMonth = "";
		ArrayList<Event> EVENTS = new ArrayList<Event>();
		int currentMonth = Integer.parseInt(dateOfToday().substring(5, 7));
		
		for(Event event: allEvents) {
			if(Integer.parseInt(event.getEventDate().substring(5, 7)) == currentMonth) {
				EVENTS.add(event);
			}
		} // end of for
		if(EVENTS.size()==0) {
			eventsOfThisMonth = "  *** No Events for This Month ***" + "\n";
		} else {
			System.out.println(" [THIS MONTH | " + currentMonth + "]: [" + EVENTS.size() + "]");
			eventsOfThisMonth += printOutEvents(EVENTS);
		}
		if(!(EVENTS.size()==0))
			eventsOfThisMonth += "~~~~~~~~~~~~~~~~~~~~~~~\n";
		return eventsOfThisMonth;
	}
	/**
	 * Method that returns a String of ALL the events in the Calendar.
	 * @return allEvents
	 * 			Returns all the events in the Calendar.
	 */
	public String getAllEvents() {
		String ALL_EVENTS = "";
		if(!(allEvents.size() == 0)) {
			System.out.println("                  [ALL EVENTS]: [" + allEvents.size() + "]");
			ALL_EVENTS += ("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" + "\n" + 
			"[DATE]"+ "               " + "[TIME]" + "         " + "[NAME]" + "\n"); 
			for(Event today: allEvents){
				if(today.getEventDate().length() == 21) {
					ALL_EVENTS += ("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" + "\n" + 
							today.getEventDate().substring(0, 10) + "-" + "       " + 
							today.getEventTime() + "        " + 
							today.getEventName() + "\n" +
							today.getEventDate().substring(10, 21) + "\n");
				} else {
					ALL_EVENTS += ("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" + "\n" + 
							today.getEventDate() + "        " + 
							today.getEventTime() + "        " + 
							today.getEventName() + "\n");
				}
			} // end of for
		} else {
			ALL_EVENTS = "     *** NO EVENTS EXISTS ***" + "\n";
		}
		if(!(allEvents.size()==0))
			ALL_EVENTS += "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n";
		return ALL_EVENTS;
	}
	/**
	 * Method that asks the user for an event to be removed.
	 * @return eventNameToRemove
	 * 			The name of the event to be removed.
	 */
	public Event getEventToRemove() {
		Event eventToRemove = new Event();
		String eventNameToRemove = "";
		input = new Scanner(System.in);

		System.out.println("Type in the NAME of the event:");
		eventNameToRemove = input.nextLine();
		if(doesEventNameExist(eventNameToRemove.toUpperCase())){
			if(eventsWithSameNameExists(eventNameToRemove.toUpperCase())){
				eventToRemove = getEventToEdit();
			} else {
				for(Event event: allEvents){
					if(event.getEventName().equals(eventNameToRemove.toUpperCase())){
						eventToRemove = event;
					}
				}
			}
		} else {
			System.out.println("*** NO SUCH EVENT EXISTS ***"+ "\n");
		}
		
		return eventToRemove;
	}
	/**
	 * Asks the user for an event to remove.
	 * @param eventName
	 * 			The name of the event to remove form the Calendar.
	 */
	public void removeEvent(Event event) {
		if(allEvents.size() > 1) {
			Iterator<Event> it = allEvents.iterator();
			while (it.hasNext()) {
				Event e = it.next();
				if (e.equals(event)) {
					it.remove();
					System.out.println("\n*** Event REMOVED || DON'T FORGET TO SAVE! *** " + "\n");
				}
			}
		} else {
			allEvents = new ArrayList<Event>();
		}
	}
	/**
	 * Prints out the Menus and sub-menus.
	 */
	public void execute() {
		do {
			String overscore = "";
			String underscore = "";
			String Title = "|| " + getTitle().toUpperCase() + " || " + dateOfToday() + " ||";
			for (int i = 0; i < Title.length(); i++) {
				overscore += "=";
			} // end of for.
			System.out.println(overscore);
			System.out.println(Title);
			
			for (int i = 0; i < Title.length(); i++) {
				underscore += "=";
			} // end of for.
			System.out.println(underscore);

			for (int j = 0; j < getItems().size(); j++) {
				System.out.println(" " + j + ". " + getItems().get(j).getTitle());
			} // end of for.
			System.out.println(underscore);
			System.out.println("Choose between 0" + "-" + (items.size() - 1) + ": ");
			getChoice(); // Som det låter, kollar även att valet är Valid.

		} while (!goBack);
		goBack = false;
	}
	/**
	 * Method for reading what menu choice that is chosen.
	 */
	private void getChoice() {
		int menuSelect = 0; // för att hålla siffran som valts.
		boolean inputValid; // för att kunna kolla om en korrekt int-värde har
							// valts.

		do {
			inputValid = false;
			try {
				menuSelect = input.nextInt();
				inputValid = true;
			} catch (InputMismatchException e) {
				input.nextLine();
			}
			if (!inputValid) {
				System.err.println("Input needs to be a number");
			} else if (menuSelect >= items.size() || menuSelect < 0) {
				System.err.println("Input needs to be between 0 and " + (items.size() - 1));
			}
		} while (!inputValid || menuSelect >= items.size() || menuSelect < 0);
		// om något är fel med inputen så körs do-while loopen om.

		System.out.println();
		items.get(menuSelect).execute();
	}
	/**
	 * Method for collecting a list of all the menus that has been added,
	 * @return items
	 * 			List with all the menus.
	 */
	private ArrayList<CalendarItem> getItems() {
		return items;
	}
	/**
	 * Method for checking date of today.
	 * This can be used for making it easier for the user to know the date of the event that he is adding to the Calendar.
	 * @return dateOfToday
	 * 			The date of today.
	 */
	private String dateOfToday(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		String dateOfToday = dateFormat.format(date);
		
		return dateOfToday;
	}
	/**
	 * Method that lets the user to search for a date, and thereby printing out the events on that date.
	 * @return
	 */
	public String getDateEvents() {
		String eventsWithRequestedDate = "";
		int dateRequested = 0;
		String date = "";
		Event extra = new Event();
		do{
			System.out.println("DATE to search for (yyyymmdd):");
			dateRequested = extra.getNewDate();
			date = Integer.toString(dateRequested);
		} while(!getValidDate(date));
		date = cleanUpDate(date, date);
		
		System.out.println(date);
		System.out.println();
		if(doesEventDateExist(date)){
			System.out.println("\n" + "   [EVENT(S) FOUND]:");
			for(Event event: allEvents){
				if((event.getEventDateAsInt()[0] == dateRequested) || (event.getEventDateAsInt()[1] == dateRequested)){
					event.printEvent();
				}			
			}
			eventsWithRequestedDate += "~~~~~~~~~~~~~~~~~~~~~~~\n";
		} else {
			eventsWithRequestedDate = "*** NO SUCH EVENT EXISTS ***"+ "\n";
		}
		
		return eventsWithRequestedDate;
	}
	/**
	 * Checks if an event has the incoming date.
	 * @return	true 
	 * 				If an event occurs on that date.
	 */
	private static boolean doesEventDateExist(String eventDate) {
		boolean found = false;
		String quit = "QUIT";
		for(Event event: allEvents) {
			if(event.getEventDate().contains(eventDate)) {
				found = true;
			} 
		}
		if(eventDate.toUpperCase().equals(quit)){
			found = true;
		}
		return found;
	}
	/**
	 * Finds a specific event that the user is searching for and prints it out for the user to see.
	 * @param category
	 * 			If category = "name" it searches for an event with next typed in name, else it searches for the word in the info.
	 */		 
	public void findEvent(String category) {
		input = new Scanner(System.in);
		String word = input.nextLine();
		ArrayList<Event> EVENTS = new ArrayList<Event>();
		
		if(category.equals("name")) {
			if(doesEventNameExist(word)) {
				for(Event event: allEvents) {
					if(event.getEventName().matches(word.toUpperCase())) {
						EVENTS.add(event);
					}
				} // end of for
				System.out.println("\n" + " [EVENT(S) FOUND]: " + "[" + EVENTS.size() + "]");
				System.out.println(printOutEvents(EVENTS) + "~~~~~~~~~~~~~~~~~~~~~~~");
			} else {
				System.out.println("*** NO SUCH EVENT EXISTS ***"+ "\n");
			}
		} else {
			if(doesEventInfoContain(word)) {
				for(Event event: allEvents) {
					if(event.getEventInfo().toLowerCase().contains(word.toLowerCase())) {
						EVENTS.add(event);
					} 
				} // end of for
				System.out.println("\n" + " [EVENT(S) FOUND]: " + "[" + EVENTS.size() + "]");
				System.out.println(printOutEvents(EVENTS) + "~~~~~~~~~~~~~~~~~~~~~~~");
			} else {
				System.out.println("*** NO SUCH EVENT EXISTS ***" + "\n");
			}
		}
	}
	/**
	 * Checks if an event exists with the incoming name.
	 * @param eventName
	 * 			String of the name to check.
	 * @return found
	 * 			Returns true if there is an event with that name, false otherwise.
	 */
	private boolean doesEventNameExist(String eventName) {
		boolean found = false;
		String quit = "QUIT";
		for(Event event: allEvents) {
			if(event.getEventName().equals(eventName.toUpperCase())) {
				found = true;
			} 
		}
		if(eventName.toUpperCase().equals(quit)){
			found = true;
		}
		return found;
	}
	/**
	 * Checks if an event exists with the incoming info.
	 * @param infoWord
	 * 			String of the word or letter to check.
	 * @return found
	 * 			Returns true if an event contains that word, false otherwise.
	 */
	private boolean doesEventInfoContain(String infoWord) {
		boolean contains = false;
		String quit = "QUIT";
		for(Event event: allEvents) {
			if(event.getEventInfo().toLowerCase().contains(infoWord.toLowerCase())) {
				contains = true;
			} 
		} // end of for
		if(infoWord.toUpperCase().equals(quit)){
			contains = true;
		}
		
		return contains;
	}
	/**
	 * Method that takes in an ArrayList<Events> that contains the events to print out.
	 * Then formatting them to a nice format and printing them as such.
	 * @param eventHolder
	 * 			An ArrayList of all the events to print out.
	 * @return output
	 * 			A String of the formatted events.
	 */
	private String printOutEvents(ArrayList<Event> eventHolder) {
		String output = "";
		for(Event event: eventHolder){
			event.printEvent();
		} // end of for
		
		return output;
	}
	/**
	 * Method for getting the user to choose one on the events that has similar informations, such as:
	 * Name and time.
	 * Uses in the search methods.
	 * @return eventToEdit
	 * 			The Event that the user choose.
	 * @throws InputMismatchException
	 */
	public Event getEventToEdit() throws InputMismatchException{
		Event eventToEdit = new Event();
		
		System.out.println("Event with what date?"); 
		for(int i=0; i<eventsWithSameName.size(); i++){
			System.out.println(" " + i + ". " + eventsWithSameName.get(i).getEventDate());
		}
		System.out.println("Choose between 0-" + (eventsWithSameName.size()-1));
		eventToEdit = eventsWithSameName.get(dateChoosen());
		
		return eventToEdit;
	}
	/**
	 * Method for controling that the user is typing in a legit number.
	 * @return
	 */
	private int dateChoosen(){
		int choice = 0;
		boolean inputValid; 

		do {
			inputValid = false;
			try {
				choice = input.nextInt();
				inputValid = true;
			} catch (InputMismatchException e) {
				input.nextLine();
			}
			if (!inputValid) {
				System.err.println("Input needs to be a number");
			} else if (choice >= eventsWithSameName.size() || choice < 0) {
				System.err.println("Input needs to be between 0 and " + (eventsWithSameName.size() - 1));
			}
		} while (!inputValid || choice >= eventsWithSameName.size() || choice < 0);
		// om något är fel med inputen så körs do-while loopen om.

		System.out.println();
		return choice;
	}
	/**
	 * Prints out all the existing events in a list for the user to see.
	 * And then edits the name of whatever event that he chooses.
	 */
	public void editEventName() {
		System.out.println(getAllEvents());
		String eventName = getEventNameToEdit();	
		if(eventsWithSameNameExists(eventName)){
				setNewName(getEventToEdit(), getNewName());
		} else {
			for(Event event: allEvents){
				if(event.getEventName().equals(eventName)){
					event.setEventName(getNewName());
				}
			}
		}
	}
	/**
	 * Prints out all the existing events in a list for the user to see.
	 * And then edits the info of whatever event that he chooses.
	 */
	public void editEventInfo() {
		System.out.println(getAllEvents());
		String eventName = getEventNameToEdit();
		if(eventsWithSameNameExists(eventName)){
			setNewInfo(getEventToEdit(), getNewInfo());
		} else {
			for(Event event: allEvents){
				if(event.getEventName().equals(eventName)){
					event.setEventInfo(getNewInfo());
				}
			}
		}
	}
	/**
	 * Prints out all the existing events in a list for the user to see.
	 * And then edits the date of whatever event that he chooses.
	 */
	public void editEventDate() {
		System.out.println(getAllEvents());
		String eventName = getEventNameToEdit();
		if(eventsWithSameNameExists(eventName)){
			setNewDate(getEventToEdit(), getNewDate());
		} else {
			for(Event event: allEvents){
				if(event.getEventName().equals(eventName)){
					event.setEventDate(getNewDate());
				}
			}
		}
	}
	/**
	 * Prints out all the existing events in a list for the user to see.
	 * And then edits the time of whatever event that he chooses.
	 */
	public void editEventTime() {
		System.out.println(getAllEvents());
		String eventName = getEventNameToEdit();
		if(eventsWithSameNameExists(eventName)){
			setNewTime(getEventToEdit(), getNewTime());
		} else {
			for(Event event: allEvents){
				if(event.getEventName().equals(eventName)){
					setNewTime(event, getNewTime());
				}
			}
		}
	}
	/**
	 * Prints out all the existing events in a list for the user to see.
	 * And then edits the location of whatever event that he chooses.
	 */
	public void editEventLoca() {
		System.out.println(getAllEvents());
		String eventName = getEventNameToEdit();
		if(eventsWithSameNameExists(eventName)){
			setNewLoca(getEventToEdit(), getNewLoca());
		} else {
			for(Event event: allEvents){
				if(event.getEventName().equals(eventName)){
					setNewLoca(event, getNewLoca());
				}
			}
		}
	}
	/**
	 * Asks the user for the name of the event to edit.
	 * @return event
	 * 			Returns the input.toUpperCase() if an event has that name
	 */
	private String getEventNameToEdit() {
		String event = "";
		input = new Scanner(System.in);
		System.out.println("NAME of the Event to EDIT:");
		event = input.nextLine();
		if(doesEventNameExist(event)){
			return event.toUpperCase();
		} else {
			System.out.println("*** NO SUCH EVENT EXISTS ***"+ "\n");
			return null;
		}
		
	}
	/**
	 * Takes in an String of the name of a possible existing event and checks if there is events with the same name.
	 * Then adding thoose events to the ArrayList eventsWithSameName.
	 * @param eventName
	 * 			String of the name to check.
	 * @return true
	 * 			If there is events with same name, false otherwise.
	 */
	private boolean eventsWithSameNameExists(String eventName) {
		ArrayList<Event> bucket = new ArrayList<Event>();
		boolean itDoes = false;
		for(Event event: allEvents) {
			if(event.getEventName().equals(eventName)) {
				bucket.add(event);
			}
		}
		
		if(bucket.size() > 1) {
			itDoes = true;
		} else {
			itDoes = false;
		}
		
		if(itDoes == true)
			eventsWithSameName=bucket;
		
		return itDoes;
	}
	/**
	 * Asks the user for a new name.
	 * @return newName
	 * 			String of whatever that is typed in.
	 */
	private String getNewName() {
		String newName = "";
		input = new Scanner(System.in);
		System.out.println("NEW Name of the Event:");
		newName = input.nextLine();
		return newName;
	}
	/**
	 * Asks the user for new info.
	 * @return newInfo
	 * 			String of whatever that is typed in.
	 */
	private String getNewInfo() {
		String newInfo = "";
		input = new Scanner(System.in);
		System.out.println("NEW Info about the Event:");
		newInfo = input.nextLine();
		return newInfo;
	}
	/**
	 * Asks the user for a new date.
	 * @return newDate
	 * 			String of whatever that is typed in.
	 */
	private String getNewDate() {
		String newDate = "";
		String newStartDate = "";
		String newEndDate = "";
		input = new Scanner(System.in);

		do{
			System.out.println("NEW Start Date of the Event:");
			newStartDate = input.nextLine();
		} while(!getValidDate(newStartDate));
		do{
			System.out.println("NEW End Date of the Event:");
			newEndDate = input.nextLine();
		} while(!(Integer.parseInt(newEndDate) >= Integer.parseInt(newStartDate)) && (getValidDate(newEndDate)));
		
		newDate = cleanUpDate(newStartDate, newEndDate);
		return newDate;
	}
	/**
	 * Asks the user for a new time.
	 * @return newTime
	 * 			String of whatever that is typed in.
	 */
	private String getNewTime() {
		String newTime = "";
		String newStartTime = "";
		String newEndTime = "";
		boolean timeOK = false;
		input = new Scanner(System.in);
		
		do{
			System.out.println("NEW Start Time of the Event:");
			newStartTime = input.nextLine();
		} while(!getValidTime(newStartTime));
		do{
			System.out.println("NEW End Time of the Event:");
			newEndTime = input.nextLine();
			timeOK = !((Integer.parseInt(newEndTime) > Integer.parseInt(newStartTime)));
		} while (timeOK && (getValidTime(newEndTime)));
		
		newTime = cleanUpTime(newStartTime, newEndTime);
		return newTime;
	}
	/**
	 * Asks the user for a new location.
	 * @return newLoca
	 * 			String of whatever that is typed in.
	 */
	private String getNewLoca() {
		String newLoca = "";
		input = new Scanner(System.in);
		System.out.println("NEW Location of the Event:");
		newLoca = input.nextLine();
		return newLoca;
	}
	/**
	 * Sets the new incoming name to the incoming event.
	 * @param e
	 * 			Event to edit.
	 * @param newName
	 * 			Name to releace the existing name with.
	 */
	private void setNewName(Event e, String newName) {
		 e.setEventName(newName);
	}
	/**
	 * Sets the new incoming info to the incoming event.
	 * @param e
	 * 			Event to edit.
	 * @param newInfo
	 * 			Info to replace the existing info with.
	 */
	private void setNewInfo(Event e, String newInfo) {
		e.setEventInfo(newInfo);
	}
	/**
	 * Sets the new incoming date to the incoming event.
	 * @param e
	 * 			Event to edit.
	 * @param newDate
	 * 			Date to replace the existing date with.
	 */
	private void setNewDate(Event e, String newDate) {
		e.setEventDate(newDate);
	}
	/**
	 * Sets the new incoming time to the incoming event.
	 * @param e
	 * 			Event to edit.
	 * @param newTime
	 * 			Time to replace the existing time with.
	 */
	private void setNewTime(Event e, String newTime) {
		e.setEventTime(newTime);
	}
	/**
	 * Sets the new incoming location to the incoming event.
	 * @param e
	 * 			Event to edit.
	 * @param newLoca
	 * 			Location to replace the existing location with.
	 */
	private void setNewLoca(Event e, String newLoca) {
		e.setEventLoca(newLoca);
	}
	/**
	 * Sets the name of the Calendar
	 * @param name
	 */
	public void setName(String name){
		this.name = name;
	}
}
