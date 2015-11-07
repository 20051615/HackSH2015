package tk.shsidcomputers.hacksh2015;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
	
	static int dateDifference(Date date1, Date date2) {
		return (int) Math.round(((double)
				(date1.getTime() - date2.getTime() + 500) / (1000 * 3600 * 24)));
	}
	
	static Date getDayStartDate(int year, int month, int day) {
		Calendar temp = Calendar.getInstance();
		temp.clear();
		temp.set(year, month - 1, day);
		return temp.getTime();
	}
	
	static Date getTodayDate() {
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH);
		int day = now.get(Calendar.DAY_OF_MONTH);
		now.clear();
		now.set(year, month, day);
		return now.getTime();
	}
	
	static Date getNextDate(Date current) {
		return new Date(current.getTime() + 24 * 3600000);
	}
	
	static int[] getYMD(Date input) {
		Calendar temp = Calendar.getInstance();
		temp.clear();
		temp.setTime(input);
		int[] YMD = new int[3];
		YMD[0] = temp.get(Calendar.YEAR);
		YMD[1] = temp.get(Calendar.MONTH) + 1;
		YMD[2] = temp.get(Calendar.DAY_OF_MONTH);
		return YMD;
	}
	
}
