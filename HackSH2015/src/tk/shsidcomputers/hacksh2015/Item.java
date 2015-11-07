package tk.shsidcomputers.hacksh2015;

import java.util.Date;

import org.json.JSONObject;

/**
 * @author SHSIDComputerClub
 * Note: this class has a natural ordering that is inconsistent with equals.
 */
public class Item implements Comparable<Item> {
	private String title, details;
	private Date mustStartDate, dueDate;
	private boolean isDone;
	private Priority priority;
	
	public Item(String title, String details, Date mustStartDate, Date dueDate, boolean isDone, Priority priority) {
		this.title = title;
		this.details = details;
		this.mustStartDate = mustStartDate;
		this.dueDate = dueDate;
		this.isDone = isDone;
		this.priority = priority;
	}
	
	@Override
	public String toString() {
		if (mustStartDate != null) {
			return title + " (due " + ItemListProcessor.getYMD(dueDate)[1] + "/"
					+ ItemListProcessor.getYMD(dueDate)[2];
		}
		return title;
	}
	
	public int compareTo(Item otherItem) {
		int n = priority.ordinal() - otherItem.priority.ordinal();
		if (n != 0) return n;
		return dueDate.compareTo(otherItem.dueDate);
		
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
	
	public void setDue(long newTime) {
		dueDate.setTime(newTime);
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
		return mustStartDate != null;
	}
	
	public Date getStartDate() {
		return hasStartDate() ? (Date) mustStartDate.clone() : null;
	}
	
	public void setStartDate(long newTime) {
		if (mustStartDate == null)
			mustStartDate = new Date(newTime);
		else
			mustStartDate.setTime(newTime);
	}
	
	public String toJSON() {
		JSONObject obj = new JSONObject();
		obj.put("title", title);
		obj.put("details", details);
		obj.put("mustStartDate", hasStartDate() ? mustStartDate.getTime() : null);
		obj.put("dueDate", dueDate.getTime());
		obj.put("isDone", isDone);
		obj.put("priority", priority.ordinal());
		return obj.toString();
	}
	
	public static Item fromJSON(String json) {
		JSONObject obj = new JSONObject(json);
		String title = (String) obj.get("title");
		String details = (String) obj.get("details");
		Date mustStartDate = obj.opt("mustStartDate") == null ? null : new Date((long) obj.get("mustStartDate"));
		Date dueDate = new Date((long) obj.get("dueDate"));
		boolean isDone = (boolean) obj.get("isDone");
		Priority priority = Priority.values()[(int) obj.get("priority")];
		return new Item(title, details, mustStartDate, dueDate, isDone, priority);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + details.hashCode();
		result = prime * result + dueDate.hashCode();
		result = prime * result + (isDone ? 1231 : 1237);
		result = prime * result + ((mustStartDate == null) ? 0 : mustStartDate.hashCode());
		result = prime * result + priority.hashCode();
		result = prime * result + title.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (!details.equals(other.details))
			return false;
		if (!dueDate.equals(other.dueDate))
			return false;
		if (isDone != other.isDone)
			return false;
		if (mustStartDate == null) {
			if (other.mustStartDate != null)
				return false;
		} else if (!mustStartDate.equals(other.mustStartDate))
			return false;
		if (priority != other.priority)
			return false;
		if (!title.equals(other.title))
			return false;
		return true;
	}
}
