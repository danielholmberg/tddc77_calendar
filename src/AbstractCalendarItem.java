/*
 * Abstrakt klass som implementerar metoder som sats upp av gränssnittet.
 */
public abstract class AbstractCalendarItem implements CalendarItem {
	private String title;

	/**
	 * Tar in en titel och sparar undan den i en privat variabel.
	 * @param title Titel på den menyn som skapats.
	 */
	public AbstractCalendarItem(String title) {
		this.title = title;
	}
	/*
	 * (non-Javadoc)
	 * @see MenuItem#execute()
	 */
	public abstract void execute();
	/*
	 * (non-Javadoc)
	 * @see MenuItem#getTitle()
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * Ändrar Titeln på Kalendern.
	 * @param title
	 */
	public void setTitle(String title){
		this.title = title;		
	}
}
