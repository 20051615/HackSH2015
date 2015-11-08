/**
 * Copyright (C) 2015 SHSIDComputerClub
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package tk.shsidcomputers.hacksh2015;

import java.util.Date;

import org.json.JSONObject;

/**
 * @author SHSIDComputerClub
 * Note: this class has a natural ordering that is inconsistent with equals.
 */
final class Item implements Comparable<Item> {
	private final String title, details;
	private final Date mustStartDate, dueDate;
	private final Priority priority;
	
	Item(String title, String details, Date mustStartDate, Date dueDate, Priority priority) {
		this.title = title;
		this.details = details;
		this.mustStartDate = mustStartDate;
		this.dueDate = dueDate;
		this.priority = priority;
	}
	
	@Override
	public String toString() {
		if (mustStartDate != null && ItemListProcessor.dateDifference(mustStartDate, dueDate) < -1) {
			return priority + ": " + title 
					+ " (Due " + ItemListProcessor.getYMD(dueDate)[1] + "/"
					+ ItemListProcessor.getYMD(dueDate)[2] + ")";
		}
		return priority + ": " + title;
	}
	
	@Override
	public int compareTo(Item otherItem) {
		int comparison = dueDate.compareTo(otherItem.dueDate);
		if (comparison != 0) return comparison;
		return priority.ordinal() - otherItem.priority.ordinal();
		
	}
	
	String getTitle() {
		return title;
	}

	String getDetails() {
		return details;
	}
	
	Date getDue() {
		return (Date) dueDate.clone();
	}
	
	Priority getPriority() {
		return priority;
	}
	
	boolean hasStartDate() {
		return mustStartDate != null;
	}
	
	Date getStartDate() {
		return hasStartDate() ? (Date) mustStartDate.clone() : null;
	}
	
	String toJSON() {
		JSONObject obj = new JSONObject();
		obj.put("title", title);
		obj.put("details", details);
		obj.put("mustStartDate", hasStartDate() ? mustStartDate.getTime() : null);
		obj.put("dueDate", dueDate.getTime());
		obj.put("priority", priority.ordinal());
		return obj.toString();
	}
	
	static Item fromJSON(String json) {
		JSONObject obj = new JSONObject(json);
		String title = (String) obj.get("title");
		String details = (String) obj.get("details");
		Date mustStartDate = obj.opt("mustStartDate") == null ? null : new Date((long) obj.get("mustStartDate"));
		Date dueDate = new Date((long) obj.get("dueDate"));
		Priority priority = Priority.values()[(int) obj.get("priority")];
		return new Item(title, details, mustStartDate, dueDate, priority);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + details.hashCode();
		result = prime * result + dueDate.hashCode();
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
