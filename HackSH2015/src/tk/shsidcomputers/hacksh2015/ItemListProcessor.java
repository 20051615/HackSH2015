package tk.shsidcomputers.hacksh2015;

import java.util.List;
import java.util.Date;
import java.util.Calendar;
import java.util.ArrayList;

final class ItemListProcessor {
	private ItemListProcessor(){}
	
	static List<Item> getDueTomorrow(Date today, Storage dueList) {
		List<Item> dueTomorrow = new ArrayList<>();
		for(Item processing: dueList) {
			if (dateDifference(processing.getDue(), today) == 1)
				dueTomorrow.add(processing);
		}
		return dueTomorrow;
	}
	
	static List<Item> getOnGoing(Date today, Storage dueList) {
		List<Item> OnGoing = new ArrayList<>();
		for(Item processing: dueList) {
			if (processing.hasStartDate() &&
				dateDifference(processing.getDue(), today) > 1 &&
				dateDifference(processing.getStartDate(), today) <= 0)
				OnGoing.add(processing);
		}
		return OnGoing;
	}
	
	public static int dateDifference(Date date1, Date date2) {
		return (int) ((date1.getTime() - date2.getTime()) / (1000 * 3600 * 24));
	}
	
	public static Date getDayStartDate(int year, int month, int day) {
		Calendar temp = Calendar.getInstance();
		temp.clear();
		temp.set(year, month, day);
		return temp.getTime();
	}
}
