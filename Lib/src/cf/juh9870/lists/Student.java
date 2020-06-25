package cf.juh9870.lists;

public class Student {
	private static final int[] DAYS = new int[]{
			-1,
			31,
			29,
			31,
			30,
			31,
			30,
			31,
			31,
			30,
			31,
			30,
			31
	};
	public String name;
	public int birthDay;
	public int birthMonth;

	public Student(String name, int birthDay, int birthMonth) {
		this.name = name;
		this.birthMonth = gate(birthMonth, 1, 12);
		this.birthDay = gate(birthDay, 0, DAYS[this.birthMonth]);
	}

	private static int gate(int number, int min, int max) {
		return Math.max(Math.min(number, max), min);
	}

	public String monthName() {
		return new String[]{
				"null",
				"January",
				"February",
				"March",
				"April",
				"May",
				"June",
				"July",
				"August",
				"September",
				"October",
				"November",
				"December"
		}[birthMonth];
	}
}
