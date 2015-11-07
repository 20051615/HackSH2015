package tk.shsidcomputers.hacksh2015;

import java.util.Date;

public class Item {
	private String title, desc;
	private Date dueDate;
	private boolean isDone;
	private Priority priority;
	
	public Item(String title, Date date) {
		this.title = title;
		dueDate = date;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public Date getDue() {
		return (Date) dueDate.clone();
	}
	
	public void setDue(long time) {
		dueDate.setTime(time);
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
}
