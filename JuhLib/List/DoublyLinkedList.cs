using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;

namespace Juh.List
{
    public class DoublyLinkedList<T> : IEnumerable<T>
    {
        public DoublyLinkedList()
        {
        }

        public DoublyLinkedList(IEnumerable<T> source) : this()
        {
            AddAll(source);
        }

        public DoublyLinkedList(params T[] elements) : this()
        {
            AddAll(elements);
        }

        public DoublyLinkedListNode<T> First { get; protected set; }
        public DoublyLinkedListNode<T> Last { get; protected set; }

        public int Length { get; private set; }

        public IEnumerator<T> GetEnumerator()
        {
            return (from node in Nodes() select node.Value).GetEnumerator();
        }

        IEnumerator IEnumerable.GetEnumerator()
        {
            return GetEnumerator();
        }

        public IEnumerator<T> GetReverseEnumerator()
        {
            return (from node in Nodes(true) select node.Value).GetEnumerator();
        }

        public IEnumerable<DoublyLinkedListNode<T>> Nodes(bool reverse = false)
        {
            var node = reverse ? Last : First;
            while (node != null)
            {
                yield return node;
                node = reverse ? node.Prev : node.Next;
            }
        }

        private DoublyLinkedListNode<T> GetNode(int index, bool reverse = false)
        {
            if (index < 0 || index >= Length)
                throw new IndexOutOfRangeException($"Index {index} is out of [0,{Length - 1}] range");
            var node = reverse ? Last : First;
            while (index-- > 0) node = reverse ? node.Prev : node.Next;
            return node;
        }

        public T Get(int index)
        {
            return GetNode(index).Value;
        }

        public void Set(T value, int index)
        {
            GetNode(index).Value = value;
        }

        public void Add(T element)
        {
            Last = new DoublyLinkedListNode<T>(element, Last);
            if (First == null) First = Last;
            Length++;
        }

        public void AddAll(IEnumerable<T> source)
        {
            foreach (var e in source) Add(e);
        }

        public void Delete(int index)
        {
            var node = GetNode(index);
            if (node.HasNext())
                node.Next.Prev = node.Prev;
            else
                Last = node.Prev;

            if (node.HasPrev())
                node.Prev.Next = node.Next;
            else
                First = node.Next;

            Length--;
        }

        public DoublyLinkedList<T> FromLasts(int amount)
        {
            if (amount < 1 || amount > Length)
                throw new ArgumentOutOfRangeException(nameof(amount), $"{amount} is out of [1,{Length - 1}] range");
            var enumerator = GetReverseEnumerator();
            var list = new DoublyLinkedList<T>();

            while (amount-- > 0)
            {
                enumerator.MoveNext();
                list.Add(enumerator.Current);
            }

            return list;
        }
    }

    public class DoublyLinkedListNode<T>
    {
        public DoublyLinkedListNode(T value, DoublyLinkedListNode<T> prev = null,
            DoublyLinkedListNode<T> next = null)
        {
            Value = value ?? throw new ArgumentNullException(nameof(value));
            Prev = prev;
            if (Prev != null) Prev.Next = this;
            Next = next;
            if (Next != null) Next.Prev = this;
        }

        public T Value { get; set; }
        public DoublyLinkedListNode<T> Prev { get; set; }
        public DoublyLinkedListNode<T> Next { get; set; }

        public bool HasNext()
        {
            return Next != null;
        }

        public bool HasPrev()
        {
            return Prev != null;
        }
    }
}