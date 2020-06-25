package cf.juh9870.lists;

public class StudentsList extends DoublyLinkedList<Student> {
	public StudentsList() {
	}

	public StudentsList(Iterable<Student> source) {
		super(source);
	}

	public StudentsList(Student... elements) {
		super(elements);
	}

	public StudentsList filter() {
		StudentsList result = new StudentsList();

		this.forEach((c) -> {
			if (c.birthMonth > 2 && c.birthMonth < 6) {
				result.add(c);
			}
		});

		return result;
	}

	@Override
	public StudentsList fromLasts(int amount) {
		return new StudentsList(super.fromLasts(amount));
	}
}
