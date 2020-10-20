/**
 * Gränssnitt för vad en Meny måste använda sig av.
 */
public interface CalendarItem {
/**
* Returnerar menyvalets rubrik.
*/
public String getTitle();
/**
* Exekverar/väljer menyvalet.
*/
public void execute();
}