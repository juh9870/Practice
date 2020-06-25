using System;
using System.Text;
using Juh.List;

namespace Practice
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.InputEncoding = Encoding.Unicode;
            Console.OutputEncoding = Encoding.Unicode;
            var list = new DoublyLinkedList<Student>(
                new Student("Галина Ляшко",1,false),
                new Student("Артем Загородський",2,false),
                new Student("Константин Лубецький",3,false),
                new Student("Давид Абанін",4,true),
                new Student("Олександра ДУбрівная",2,false),
                new Student("Лі Джу",3,true),
                new Student("Оксана Сирецька",1,true),
                new Student("Олег Кузнецов",4,true));

            new GUI().Start(list);
        }
    }
}