import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Scanner;

public class Driver {
	// File to load Calendar from.
	final static File DANIELS_CALENDAR = new File("DANIEL_Calendar.txt");
	final static File JONATHANS_CALENDAR = new File("JONATHAN_Calendar.txt");
	final static File DANIEL_PASSWORD_FILE = new File("DANIEL_PASSWORD.txt");
	final static File JONATHAN_PASSWORD_FILE = new File("JONATHAN_PASSWORD.txt");
	static String DANIEL_PASSWORD = "";
	static String JONATHAN_PASSWORD = "";
	static String PASSWORD = "";
	private static Scanner input = new Scanner(System.in);
	
	public static void main(String[] args) {
		// MainMenu
		Calendar MainMenu = new Calendar("Private Calendars");
		
		// SubMenus for the MainMenu

		Calendar user = new Calendar("Daniel Holmberg");
		
		try {
			DANIEL_PASSWORD = readPassword(new FileInputStream(DANIEL_PASSWORD_FILE));
			JONATHAN_PASSWORD = readPassword(new FileInputStream(JONATHAN_PASSWORD_FILE));
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
		Calendar EDIT_CALENDAR = new Calendar("EDIT Calendar");
		EDIT_CALENDAR.addItem(new AbstractCalendarItem("Add New Event") {
			public void execute() {
				user.addNewEvent();
			}

		});
		EDIT_CALENDAR.addItem(new AbstractCalendarItem("Remove Event") {
			public void execute() {
				Event event = user.getEventToRemove();
				if(!event.getEventName().isEmpty()){
				System.out.println(" [Event to remove]: ");
				event.printEvent();
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~");
				user.removeEvent(event);
				}
			}
		});
		
		Calendar EDIT_EVENT = new Calendar("EDIT Event");
		EDIT_EVENT.addItem(new AbstractCalendarItem("EDIT Name"){
			public void execute(){
				user.editEventName();
				
			}
		});
		EDIT_EVENT.addItem(new AbstractCalendarItem("EDIT Info"){
			public void execute() {
				user.editEventInfo();
			}
			
		});
		EDIT_EVENT.addItem(new AbstractCalendarItem("EDIT Date"){
			public void execute() {
				user.editEventDate();
			}
			
		});
		EDIT_EVENT.addItem(new AbstractCalendarItem("EDIT Time"){
			public void execute() {
				user.editEventTime();
			}
			
		});
		EDIT_EVENT.addItem(new AbstractCalendarItem("EDIT Location"){
			public void execute() {
				user.editEventLoca();
			}
			
		});
		EDIT_CALENDAR.addItem(EDIT_EVENT);
		user.addItem(EDIT_CALENDAR);

		Calendar UPCOMING = new Calendar("Upcoming Events");
		UPCOMING.addItem(new AbstractCalendarItem("Today") {
			public void execute() {
				System.out.println(user.getTodaysEvents());
			}
		});
		UPCOMING.addItem(new AbstractCalendarItem("This Month") {
			public void execute() {
				System.out.println(user.getThisMonth());
			}
		});
		UPCOMING.addItem(new AbstractCalendarItem("List All Events") {
			public void execute() {
				System.out.println(user.getAllEvents());
			}
		});
		user.addItem(UPCOMING);
		
		Calendar SEARCH = new Calendar("Search Event");
		SEARCH.addItem(new AbstractCalendarItem("Search by Name"){
			public void execute(){
				System.out.println("NAME TO SEARCH FOR:");
				user.findEvent("name");
			}
		});
		SEARCH.addItem(new AbstractCalendarItem("Search for word in Info"){
			public void execute(){
				System.out.println("WORD TO SEARCH FOR:");
				user.findEvent("info");
			}
		});
		SEARCH.addItem(new AbstractCalendarItem("Search for a Date"){
			public void execute() {
				System.out.println(user.getDateEvents());
			}
		});
		user.addItem(SEARCH);
		
		if(isPasswordCorrect()) {
			if (PASSWORD.equals(DANIEL_PASSWORD)) {
				try {
					System.out.println(user.load(new FileInputStream(DANIELS_CALENDAR)));
				} catch (FileNotFoundException e1) {
					System.out.println("*** NO CALENDAR FILE FOUND! ***");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				user.setTitle("Daniel Holmberg");
				user.setName("Daniel Holmberg");
				MainMenu.addItem(user);
			} else if (PASSWORD.equals(JONATHAN_PASSWORD)) {
				try {
					System.out.println(user.load(new FileInputStream(JONATHANS_CALENDAR)));
				} catch (FileNotFoundException e1) {
					System.out.println("*** NO CALENDAR FILE FOUND! ***");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				user.setTitle("Jonathan Brage");
				user.setName("Jonathan Brage");
				MainMenu.addItem(user);
			}
		} 
		
		MainMenu.addItem(new AbstractCalendarItem("Change password") {
			public void execute() {
				try {
					System.out.println(changePassword());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		MainMenu.addItem(new AbstractCalendarItem("Save & Close") {
			public void execute() {
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" + "\n"
						+ "||| Thanks You for using our Calendar Program |||" + "\n"
						+ "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" + "\n"
						+ "Made by:	Daniel Holmberg & Jonathan Brage" + "\n"
						+ "E-mail:		danielkurtholmberg@gmail.com" + "\n" + "		jonathanbrage95@gmail.com"
						+ "\n" + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				
				
				user.save(user.getTitle());
				System.exit(0);
			}

		});
		MainMenu.execute();

		MainMenu.execute();

	}
	/**
	 * Method for changing password by asking for the old one and then a new password.
	 * Overwrites the old password in the .txt-file with the new password.
	 * @return Message about what the new password is.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static String changePassword() throws FileNotFoundException, IOException {
		String temp = "";
		int counter = 0;
		
		do{
			System.out.println("Type in OLD password:");
			temp = input.next();
			counter++;
			if(counter==5){
				System.out.println("Type 'quit' if you want to go BACK!");
				counter=0;
			}
		} while(!(temp.equals(PASSWORD) || temp.toLowerCase().equals("quit")));
		
		if(!(temp.toLowerCase().equals("quit"))){
			System.out.println("Type in NEW password");
			temp = input.next();
			savePassword(temp);
			return "Password Changed to: " + temp;
		} else {
			return ("Forgotten password?" + "\n" + "Send an e-mail to: danielkurtholmberg@gmail.com");
		}
	}
	/**
	 * Saves the new password to the password.txt depending on witch user it is done in.
	 * @param temp
	 * 			The new password added from changePassword()
	 * @throws FileNotFoundException
	 */
	private static void savePassword(String temp) throws FileNotFoundException {
		FileOutputStream file = null;
		if (PASSWORD.equals(DANIEL_PASSWORD)) {
			file = new FileOutputStream(DANIEL_PASSWORD_FILE);

		} else if (PASSWORD.equals(JONATHAN_PASSWORD)) {
			file = new FileOutputStream(JONATHAN_PASSWORD_FILE);
		}
		PASSWORD = temp;
		PrintStream output = new PrintStream(file);
		output.println(temp);
		output.close();
	}
	/**
	 * Reads in the content of the password-text-file.
	 * @param is
	 * 			The InputStream from the password.txt-file
	 * @return t
	 * 			The current password in the .txt-file.
	 * @throws IOException
	 */
	private static String readPassword(FileInputStream is) throws IOException {
		BufferedReader text = new BufferedReader(new InputStreamReader(is));
		String t = "";

		while (is.available() != 0) {
			t += text.readLine();
		}
		return t;
	}
	/**
	 * Method for protecting the Calendars with a password.
	 * @return true
	 * 			If and only if the password is valid.
	 */
	private static boolean isPasswordCorrect() {
		boolean correctPassword = false;
		String temp = "";
		int counter = 0;

		do {
			System.out.println(
					"~~~~~~~~~~~~~~~~~~~~~~" + "\n" + "PLEASE ENTER PASSWORD:" + "\n" + "~~~~~~~~~~~~~~~~~~~~~~");
			temp = input.nextLine();
			PASSWORD = temp;
			counter++;
			if (temp.equals(DANIEL_PASSWORD)) {
				correctPassword = true;
			} else if (temp.equals(JONATHAN_PASSWORD)) {
				correctPassword = true;
			} else {
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~" + "\n" + "[ERROR: WRONG PASSWORD]");
				if (counter == 3)
					System.out.println("\n" + "Don't know the password?" + "\n"
							+ "Send an e-mail to: danielkurtholmberg@gmail.com" + "\n");
			}
		} while (correctPassword == false);

		return correctPassword;
	}
}