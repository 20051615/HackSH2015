package tk.shsidcomputers.hacksh2015;

import java.util.Date;

import org.json.JSONObject;

/**
 * @author SHSIDComputerClub
 * Note: this class has a natural ordering that is inconsistent with equals.
 */
public class Item implements Comparable<Item>{
	private String title, details;
	private Date mustStartDate, dueDate;
	private boolean isDone;
	private Priority priority;
	
	public Item(String title, Date date) {
		this.title = title;
		dueDate = date;
	}
	
	public int compareTo(Item otherItem) {
		return priority.ordinal() - otherItem.priority.ordinal();
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
	
	public Date getDue() {
		return (Date) dueDate.clone();
	}
	
	public void setDue(Date newDate) {
		dueDate.setTime(newDate.getTime());
	}

	public void finished() {
		isDone = true;
	}

	public void unfinished() {
		isDone = false;
	}
	
	public boolean isFinished() {
		return isDone;
	}
	
	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}
	
	public boolean hasStartDate() {
		return mustStartDate == null;
	}
	
	public Date getStartDate(Date newDate) {
		return hasStartDate() ? (Date) dueDate.clone() : getDue();
	}
	
	public void setStartDate(Date newDate) {
		mustStartDate.setTime(newDate.getTime());
	}
	
	public String toJSON() {
		JSONObject obj = new JSONObject();
		obj.put("title", title);
		obj.put("details", details);
		obj.put("mustStartDate", mustStartDate.getTime());
		obj.put("dueDate", dueDate.getTime());
		obj.put("isDone", isDone);
		obj.put("priority", priority);
		return obj.toString();
	}
}
