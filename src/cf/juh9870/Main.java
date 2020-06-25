package cf.juh9870;

import cf.juh9870.lists.Student;
import cf.juh9870.lists.StudentsList;

public class Main {

	public static void main(String[] args) {
		StudentsList list = new StudentsList(
				new Student("Галина Ляшко", 11, 3),
				new Student("Артем Загородський", 23, 5),
				new Student("Константин Лубецький", 3, 2),
				new Student("Давид Абанін", 15, 8),
				new Student("Олександра ДУбрівная", 28, 10),
				new Student("Лі Джу", 17, 1),
				new Student("Оксана Сирецька", 31, 12),
				new Student("Олег Кузнецов", 8, 7));
		new GUI().start(list);
	}
}
