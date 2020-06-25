using System;

namespace Juh.List
{
    public struct Student
    {
        public Student(string name, int grade, bool army)
        {
            Name = name ?? throw new ArgumentNullException(nameof(name));
            Grade = grade;
            Army = army;
        }

        public string Name { get; }
        public int Grade { get; }
        public bool Army { get; }
    }
}