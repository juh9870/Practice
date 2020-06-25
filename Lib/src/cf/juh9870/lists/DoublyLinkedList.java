package cf.juh9870.lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DoublyLinkedList<T> implements Iterable<T> {
	protected Node<T> last;
	private Node<T> first;
	private int length;

	public DoublyLinkedList() {
	}

	public DoublyLinkedList(Iterable<T> source) {
		addAll(source);
	}

	public DoublyLinkedList(T... elements) {
		addAll(Arrays.asList(elements));
	}

	protected Node<T> getFirst() {
		return first;
	}

	public Node<T> getLast() {
		return last;
	}

	public int getLength() {
		return length;
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<>(this, false);
	}

	public Iterator<T> reverseIterator() {
		return new Iterator<>(this, true);
	}

	public Iterable<Node<T>> nodes() {
		return nodes(false);
	}

	public Iterable<Node<T>> nodes(boolean reverse) {
		Node<T> node = reverse ? last : first;
		List<Node<T>> result = new ArrayList<>();
		while (node != null) {
			result.add(node);
			node = reverse ? node.prev : node.next;
		}
		return result;
	}

	private Node<T> getNode(int index) {
		return getNode(index, false);
	}

	private Node<T> getNode(int index, boolean reverse) {
		if (index < 0 || index >= length)
			throw new IndexOutOfBoundsException("Index " + index + " is out of [0," + (length - 1) + "] range");
		Node<T> node = reverse ? last : first;
		while (index-- > 0) node = reverse ? node.prev : node.next;
		return node;
	}

	public T get(int index) {
		return getNode(index).value;
	}

	public void set(T value, int index) {
		getNode(index).value = value;
	}

	public void add(T element) {
		last = new Node<T>(element, last, null);
		if (first == null) first = last;
		length++;
	}

	public void addAll(Iterable<T> source) {
		for (T e : source) add(e);
	}

	public void delete(int index) {
		Node<T> node = getNode(index);
		if (node.hasNext())
			node.next.prev = node.prev;
		else
			last = node.prev;

		if (node.hasPrev())
			node.prev.next = node.next;
		else
			first = node.next;

		length--;
	}

	public DoublyLinkedList<T> fromLasts(int amount) {
		if (amount < 1 || amount > length)
			throw new IllegalArgumentException("Amount " + amount + " is out of [1," + (length - 1) + "] range");
		Iterator<T> enumerator = reverseIterator();
		DoublyLinkedList<T> list = new DoublyLinkedList<T>();

		while (amount-- > 0) {
			list.add(enumerator.next());
		}

		return list;
	}

	static class Node<T> {
		public T value;
		public Node<T> prev;
		public Node<T> next;

		public Node(T value, Node<T> prev,
					Node<T> next) {
			if (value == null) throw new IllegalArgumentException("Value must be non null");
			this.value = value;
			this.prev = prev;
			if (this.prev != null) this.prev.next = this;
			this.next = next;
			if (this.next != null) this.next.prev = this;
		}

		public boolean hasNext() {
			return next != null;
		}

		public boolean hasPrev() {
			return prev != null;
		}
	}

	class Iterator<T> implements java.util.Iterator<T> {
		Node<T> cur;
		boolean reverse;

		private Iterator(DoublyLinkedList<T> list, boolean reverse) {
			this.reverse = reverse;
			if (reverse) cur = list.last;
			else cur = list.first;
		}

		@Override
		public boolean hasNext() {
			return cur != null;
		}

		@Override
		public T next() {
			T data = cur.value;
			cur = reverse ? cur.prev : cur.next;
			return data;
		}
	}
}