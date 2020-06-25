package cf.juh9870;

import cf.juh9870.lists.Student;
import cf.juh9870.lists.StudentsList;

public class GUI {
	private static final int[] COL_WIDTHS = {2, 30, 14, 12};
	private Charset[] charsets;
	private int curSet = 0;
	private StudentsList list;

	public static String padRight(String s, int n) {
		return String.format("%-" + n + "s", s);
	}

	public static String padLeft(String s, int n) {
		return String.format("%" + n + "s", s);
	}

	public Charset getChars() {
		return charsets[curSet % charsets.length];
	}

	public void start(StudentsList list) {
		charsets = new Charset[]
				{
						new Charset('─', '│',
								'┌', '┬', '┐',
								'├', '┼', '┤',
								'└', '┴', '┘'),
						new Charset('-', '|', '+'),
						new Charset(' ', '─', ' ',
								' ', '│', ' ',
								' ', '│', ' ',
								'─', '┼', '─',
								' ', '│', ' '),
						new Charset(' ', '-', ' ',
								' ', '|', ' ',
								' ', '|', ' ',
								'-', '+', '-',
								' ', '|', ' ')
				};

		if (list != null) this.list = list;
		else this.list = new StudentsList();

		Console.start();

		Console.clear();
		writeListContent(list);
		waitForInput();
	}

	public void waitForInput() {
		Console.printLine("Input a character");
		Console.printLine("1: Add element.");
		Console.printLine("2: Remove element.");
		Console.printLine("3: Get element.");
		Console.printLine("4: Show Filtered List.");
		Console.printLine("5: Show Reverse list.");
		Console.printLine("6: Close the program.");
		Console.printLine("0: Change border style.");

		char ch = '\u0000';
		int num = -1;
		do {
			ch = Console.getKeyPress();
			try {
				num = Integer.parseInt(Character.toString(ch));
			} catch (NumberFormatException ignored) {

			}
		} while (num < 0 || num > 6);

		Console.clear();
		writeListContent(list);

		switch (num) {
			case 1:
				add();
				break;
			case 2:
				remove();
				break;
			case 3:
				get();
				break;
			case 4:
				filtered();
				break;
			case 5:
				reverse();
				break;
			case 6:
				break;
			case 0:
				curSet++;
				Console.clear();
				writeListContent(list);
				waitForInput();
				break;
		}
	}

	private void add() {
		Console.printLine("Adding new student");
		String name = readString("Input name");
		int monthBorn = readNumber("Input month of birth");
		int dayBirth = readNumber("Input day of birth");
		Console.printLine("");

		Student student = new Student(name, dayBirth, monthBorn);
		StudentsList listCopy = new StudentsList(list);
		listCopy.add(student);

		listPrompt(listCopy, null, "Add new element?", new StudentsList(student));
	}

	private void remove() {
		Console.printLine("Removing student");
		int id = readNumber("Input ID of element to remove");
		StudentsList listCopy = new StudentsList(list);
		try {
			listCopy.delete(id);

			listPrompt(listCopy, null, "Delete this element?", new StudentsList(list.get(id)));
		} catch (IndexOutOfBoundsException e) {
			cancel("Index out of range. ");
		}
	}

	private void get() {
		Console.printLine("Getting element");
		int id = readNumber("Input ID of element to show");
		Student student;
		try {
			student = list.get(id);

			writeListContent(new StudentsList(student));
			listPrompt(null, "", null, null);
		} catch (IndexOutOfBoundsException e) {
			cancel("Index out of range. ");
		}
	}

	private void filtered() {
		Console.printLine("Filtering list");
		listPrompt(new StudentsList(list.filter()), null, null, null);
	}

	private void reverse() {
		Console.printLine("Reversing");
		int amount = readNumber("Input amount of elements to include into reversed array");
		try {
			listPrompt(list.fromLasts(amount), null, null, null);
		} catch (IllegalArgumentException e) {
			if (amount <= 0) cancel("Too small amount of elements specified. ");
			else cancel("too large amount is specified. ");
		}
	}

	private void cancel(String reason) {
		listPrompt(null, reason + "Operation canceled. ", null, null);
	}

	private void listPrompt(StudentsList list,
							String text, String replaceText,
							StudentsList visualList) {
		if (text == null) text = "Changes Applied. ";
		if (replaceText == null) replaceText = "Replace current list with a new one?";
		if (visualList == null) visualList = list;

		Console.printLine("");
		if (list != null) {
			writeListContent(visualList);
			if (readBool(replaceText)) {
				this.list = list;
				Console.printLine("");
			} else {
				cancel("");
			}
		}

		Console.printLine(text + "Press Enter to continue.");
		Console.getKeyPress();
		Console.clear();
		writeListContent(this.list);
		waitForInput();
	}

	private boolean readBool(String prompt) {
		Console.printLine(prompt);
		Console.print("Press " + Console.Color.GREEN + "y" + Console.Color.RESET +
				" or " + Console.Color.GREEN + "enter" + Console.Color.RESET + " for yes, or "
				+ Console.Color.RED + "n" + Console.Color.RESET + " for no: ");

		char ch;
		do {
			ch = Console.getKeyPress();

			switch (ch) {
				case 'y':
				case 'Y':
				case '\r':
					return true;
				case 'n':
				case 'N':
					return false;
			}
		} while (true);
	}

	private int readNumber(String prompt) {
		int num = -1;
		Console.printLine(prompt + ": ");
		String text = Console.readLine();
		try {
			num = Integer.parseUnsignedInt(text);
		} catch (NumberFormatException e) {
			while (num < 0) {
				Console.printLine("Invalid number. " + prompt + ": ");
				text = Console.readLine();
				try {
					num = Integer.parseUnsignedInt(text);
				} catch (NumberFormatException ignored) {
				}
			}
		}

		return num;
	}

	private String readString(String prompt) {
		Console.printLine(prompt + ": ");
		String text = Console.readLine();
		while (text == null || (text = text.trim()).length() == 0) {
			Console.printLine("Input String empty. " + prompt + ": ");
			text = Console.readLine();
		}

		return text;
	}

	private String repeat(char c, int amout) {
		return new String(new char[amout]).replace('\0', c);
	}

	private String formatString(String name, int length, boolean right) {
		if (name.length() > length) {
			name = name.substring(0, length - 4);
			name += "...";
		} else {
			name = right ? padLeft(name, length) : padRight(name, length);
		}

		return name;
	}


	private String splitterLine(char[] line) {
		return line[0] + repeat(line[3], COL_WIDTHS[0] + 2) +
				line[1] + repeat(line[3], COL_WIDTHS[1] + 2) +
				line[1] + repeat(line[3], COL_WIDTHS[2] + 2) +
				line[1] + repeat(line[3], COL_WIDTHS[3] + 2) + line[2] + "\n";
	}

	private String textLine(String idCol, String nameCol, String monthCol, String dayCol) {
		return getChars().LeftVertical + " " + formatString(idCol, COL_WIDTHS[0], true) + " " +
				getChars().CentralVertical + " " + formatString(nameCol, COL_WIDTHS[1], false) + " " +
				getChars().CentralVertical + " " + formatString(monthCol, COL_WIDTHS[2], true) + " " +
				getChars().CentralVertical + " " + formatString(dayCol, COL_WIDTHS[3], false) + " " +
				getChars().RightVertical + "\n";
	}

	private void writeListContent(StudentsList list) {
		StringBuilder text = new StringBuilder();
		text.append(splitterLine(getChars().TopRow()));
		text.append(textLine("Id", "Name", "Month of Birth", "Day of Birth"));
		text.append(splitterLine(getChars().MiddleRow()));

		int id = 0;

		for (Student student : list)
			text.append(textLine(Integer.toString(id++), student.name,
					student.monthName(),
					Integer.toString(student.birthDay)));

		text.append(splitterLine(getChars().BottomRow()));

		Console.printLine(text.toString());
	}

	public static class Charset {
		public char TopHorizontal;
		public char CentralHorizontal;
		public char BottomHorizontal;
		public char LeftVertical;
		public char CentralVertical;
		public char RightVertical;
		public char TopLeftCorner;
		public char TopIntersection;
		public char TopRightCorner;
		public char LeftIntersection;
		public char CentralIntersection;
		public char RightIntersection;
		public char BottomLeftCorner;
		public char BottomIntersection;
		public char BottomRightCorner;

		public Charset(char topHorizontal, char centralHorizontal, char bottomHorizontal,
					   char leftVertical, char centralVertical, char rightVertical,
					   char topLeftCorner, char topIntersection, char topRightCorner,
					   char leftIntersection, char centralIntersection, char rightIntersection,
					   char bottomLeftCorner, char bottomIntersection, char bottomRightCorner) {
			TopHorizontal = topHorizontal;
			CentralHorizontal = centralHorizontal;
			BottomHorizontal = bottomHorizontal;
			LeftVertical = leftVertical;
			CentralVertical = centralVertical;
			RightVertical = rightVertical;
			TopLeftCorner = topLeftCorner;
			TopIntersection = topIntersection;
			TopRightCorner = topRightCorner;
			LeftIntersection = leftIntersection;
			CentralIntersection = centralIntersection;
			RightIntersection = rightIntersection;
			BottomLeftCorner = bottomLeftCorner;
			BottomIntersection = bottomIntersection;
			BottomRightCorner = bottomRightCorner;
		}

		public Charset(char horizontal, char vertical, char topLeftCorner, char topIntersection, char topRightCorner,
					   char leftIntersection, char centralIntersection, char rightIntersection, char bottomLeftCorner,
					   char bottomIntersection, char bottomRightCorner) {
			this(horizontal, horizontal, horizontal,
					vertical, vertical, vertical,
					topLeftCorner, topIntersection, topRightCorner,
					leftIntersection, centralIntersection, rightIntersection,
					bottomLeftCorner, bottomIntersection, bottomRightCorner);

		}

		public Charset(char horizontal, char vertical, char intersection) {
			this(horizontal, vertical,
					intersection, intersection, intersection,
					intersection, intersection, intersection,
					intersection, intersection, intersection);

		}

		public Charset(char topHorizontal, char centralHorizontal, char bottomHorizontal,
					   char leftVertical, char centralVertical, char rightVertical, char intersection) {
			this(topHorizontal, centralHorizontal, bottomHorizontal,
					leftVertical, centralVertical, rightVertical,
					intersection, intersection, intersection,
					intersection, intersection, intersection,
					intersection, intersection, intersection);

		}

		public char[] TopRow() {
			return TableArray()[0];
		}

		public char[] MiddleRow() {
			return TableArray()[1];
		}

		public char[] BottomRow() {
			return TableArray()[2];
		}

		public char[][] TableArray() {
			return new char[][]
					{
							new char[]{
									TopLeftCorner, TopIntersection, TopRightCorner, TopHorizontal
							},
							new char[]{
									LeftIntersection, CentralIntersection, RightIntersection, CentralHorizontal
							},
							new char[]{
									BottomLeftCorner, BottomIntersection, BottomRightCorner, BottomHorizontal
							}
					};
		}
	}
}

