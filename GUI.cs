using System;
using System.Text;
using Juh.List;

namespace Practice
{
    public class GUI
    {
        private static readonly int[] COL_WIDTHS = {2, 30, 5, 5};
        private Charset[] _charsets;
        private int _curSet;
        private DoublyLinkedList<Student> List;

        private Charset Chars => _charsets[_curSet % _charsets.Length];

        public void Start(DoublyLinkedList<Student> list = null)
        {
            _charsets = new[]
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

            if (list != null) List = list;
            else List = new DoublyLinkedList<Student>();

            Console.Clear();
            WriteListContent(list);
            WaitForInput();
        }

        public void WaitForInput()
        {
            Console.WriteLine("Input a character");
            Console.WriteLine("1: Add element.");
            Console.WriteLine("2: Remove element.");
            Console.WriteLine("3: Get element.");
            Console.WriteLine("4: Show Filtered List.");
            Console.WriteLine("5: Show Reverse list.");
            Console.WriteLine("6: Close the program.");
            Console.WriteLine("0: Change border style.");

            var ch = '\u0000';
            var num = -1;
            do
            {
                var key = Console.ReadKey();
                ch = key.KeyChar;
            } while (!int.TryParse(ch.ToString(), out num) || num > 6);

            Console.Clear();
            WriteListContent(List);

            switch (num)
            {
                case 1:
                    Add();
                    break;
                case 2:
                    Remove();
                    break;
                case 3:
                    Get();
                    break;
                case 4:
                    Filtered();
                    break;
                case 5:
                    Reverse();
                    break;
                case 6:
                    break;
                case 0:
                    _curSet++;
                    Console.Clear();
                    WriteListContent(List);
                    WaitForInput();
                    break;
            }
        }

        private void Add()
        {
            Console.WriteLine("Adding new student");
            var name = ReadString("Input name");
            var grade = ReadNumber("Input grade");
            var wasInArmy = ReadBool("Was in army?");
            Console.WriteLine("");

            var student = new Student(name, grade, wasInArmy);
            var listCopy = new DoublyLinkedList<Student>(List);
            listCopy.Add(student);

            ListPrompt(listCopy, replaceText: "Add new element?",
                visualList: new DoublyLinkedList<Student>(student));
        }

        private void Remove()
        {
            Console.WriteLine("Removing student");
            var id = ReadNumber("Input ID of element to remove");
            var listCopy = new DoublyLinkedList<Student>(List);
            try
            {
                listCopy.Delete(id);

                ListPrompt(listCopy, replaceText: "Delete this element?",
                    visualList: new DoublyLinkedList<Student>(List.Get(id)));
            }
            catch (IndexOutOfRangeException)
            {
                Cancel("Index out of range. ");
            }
        }

        private void Get()
        {
            Console.WriteLine("Getting element");
            var id = ReadNumber("Input ID of element to show");
            Student student;
            try
            {
                student = List.Get(id);

                WriteListContent(new DoublyLinkedList<Student>(student));
                ListPrompt(text: "");
            }
            catch (IndexOutOfRangeException)
            {
                Cancel("Index out of range. ");
            }
        }

        private void Filtered()
        {
            Console.WriteLine("Filtering list");
            ListPrompt(new DoublyLinkedList<Student>(List.Filter()));
        }

        private void Reverse()
        {
            Console.WriteLine("Reversing");
            var amount = ReadNumber("Input amount of elements to include into reversed array");
            try
            {
                ListPrompt(List.FromLasts(amount));
            }
            catch (ArgumentOutOfRangeException)
            {
                if (amount <= 0) Cancel("Too small amount of elements specified. ");
                else Cancel("too large amount is specified. ");
            }
        }

        private void Cancel(string reason)
        {
            ListPrompt(text: reason + "Operation canceled. ");
        }

        private void ListPrompt(DoublyLinkedList<Student> list = null,
            string text = "Changes Applied. ", string replaceText = "Replace current list with a new one?",
            DoublyLinkedList<Student> visualList = null)
        {
            visualList ??= list;

            if (Console.CursorLeft != 0) Console.WriteLine("");
            if (list! != null)
            {
                WriteListContent(visualList);
                if (ReadBool(replaceText))
                {
                    List = list;
                    Console.WriteLine("");
                }
                else
                {
                    Cancel("");
                }
            }

            Console.WriteLine(text + "Press any key to continue.");
            Console.ReadKey();
            Console.Clear();
            WriteListContent(List);
            WaitForInput();
        }

        private bool ReadBool(string prompt)
        {
            ClearLine();
            Console.WriteLine(prompt);
            Console.Write("Press ");
            Console.ForegroundColor = ConsoleColor.Green;
            Console.Write("y");
            Console.ResetColor();
            Console.Write(" for yes or ");
            Console.ForegroundColor = ConsoleColor.Red;
            Console.Write("n");
            Console.ResetColor();
            Console.Write(" for no: ");

            char ch;
            var pos = Console.CursorLeft;
            do
            {
                if (Console.CursorLeft != pos)
                {
                    Console.CursorLeft = pos;
                    Console.Write(" ");
                    Console.CursorLeft = pos;
                }

                var key = Console.ReadKey();
                ch = key.KeyChar;

                switch (ch)
                {
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

        private int ReadNumber(string prompt)
        {
            ClearLine();
            int num;
            Console.Write(prompt + ": ");
            var text = Console.ReadLine();
            while (!int.TryParse(text, out num))
            {
                Console.CursorTop--;
                ClearLine();
                Console.Write("Invalid number. " + prompt + ": ");
                text = Console.ReadLine();
            }

            return num;
        }

        private string ReadString(string prompt)
        {
            ClearLine();
            Console.Write(prompt + ": ");
            var text = Console.ReadLine();
            while (text == null || (text = text.Trim()).Length == 0)
            {
                Console.CursorTop--;
                ClearLine();
                Console.Write("Input string empty. " + prompt + ": ");
                text = Console.ReadLine();
            }

            return text;
        }

        private void ClearLine()
        {
            var curLine = Console.CursorTop;
            Console.CursorLeft = 0;
            Console.Write(new string(' ', Console.WindowWidth));
            Console.SetCursorPosition(0, curLine);
        }

        private string FormatString(string name, int length, bool right = false)
        {
            if (name.Length > length)
            {
                name = name.Substring(0, length - 4);
                name += "...";
            }
            else
            {
                name = right ? name.PadLeft(length) : name.PadRight(length);
            }

            return name;
        }

        private string SplitterLine(char[] line)
        {
            return line[0] + new string(line[3], COL_WIDTHS[0] + 2) +
                   line[1] + new string(line[3], COL_WIDTHS[1] + 2) +
                   line[1] + new string(line[3], COL_WIDTHS[2] + 2) +
                   line[1] + new string(line[3], COL_WIDTHS[3] + 2) + line[2];
        }

        private string TextLine(string idCol, string nameCol, string gradeCol, string armyCol)
        {
            return Chars.LeftVertical + $" {FormatString(idCol, COL_WIDTHS[0], true)} " +
                   Chars.CentralVertical + $" {FormatString(nameCol, COL_WIDTHS[1])} " +
                   Chars.CentralVertical + $" {FormatString(gradeCol, COL_WIDTHS[2], true)} " +
                   Chars.CentralVertical + $" {FormatString(armyCol, COL_WIDTHS[3])} " + Chars.RightVertical;
        }

        private void WriteListContent(DoublyLinkedList<Student> list)
        {
            var text = new StringBuilder();
            text.AppendLine(SplitterLine(Chars.TopRow()));
            text.AppendLine(TextLine("Id", "Name", "Grade", "Army"));
            text.AppendLine(SplitterLine(Chars.MiddleRow()));

            var id = 0;

            foreach (var student in list)
                text.AppendLine(TextLine((id++).ToString(), student.Name, student.Grade.ToString(),
                    student.Army.ToString()));

            text.AppendLine(SplitterLine(Chars.BottomRow()));

            Console.WriteLine(text.ToString());
        }
    }

    public struct Charset
    {
        public Charset(char topHorizontal, char centralHorizontal, char bottomHorizontal,
            char leftVertical, char centralVertical, char rightVertical,
            char topLeftCorner, char topIntersection, char topRightCorner,
            char leftIntersection, char centralIntersection, char rightIntersection,
            char bottomLeftCorner, char bottomIntersection, char bottomRightCorner)
        {
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
            char bottomIntersection, char bottomRightCorner) :
            this(horizontal, horizontal, horizontal,
                vertical, vertical, vertical,
                topLeftCorner, topIntersection, topRightCorner,
                leftIntersection, centralIntersection, rightIntersection,
                bottomLeftCorner, bottomIntersection, bottomRightCorner)
        {
        }

        public Charset(char horizontal, char vertical, char intersection) : this(horizontal, vertical,
            intersection, intersection, intersection,
            intersection, intersection, intersection,
            intersection, intersection, intersection)
        {
        }

        public Charset(char topHorizontal, char centralHorizontal, char bottomHorizontal,
            char leftVertical, char centralVertical, char rightVertical, char intersection) :
            this(topHorizontal, centralHorizontal, bottomHorizontal,
                leftVertical, centralVertical, rightVertical,
                intersection, intersection, intersection,
                intersection, intersection, intersection,
                intersection, intersection, intersection)
        {
        }

        public char[] TopRow()
        {
            return TableArray()[0];
        }

        public char[] MiddleRow()
        {
            return TableArray()[1];
        }

        public char[] BottomRow()
        {
            return TableArray()[2];
        }

        public char[][] TableArray()
        {
            return new[]
            {
                new[] {TopLeftCorner, TopIntersection, TopRightCorner, TopHorizontal},
                new[] {LeftIntersection, CentralIntersection, RightIntersection, CentralHorizontal},
                new[] {BottomLeftCorner, BottomIntersection, BottomRightCorner, BottomHorizontal}
            };
        }

        public char TopHorizontal { get; }
        public char CentralHorizontal { get; }
        public char BottomHorizontal { get; }
        public char LeftVertical { get; }
        public char CentralVertical { get; }
        public char RightVertical { get; }
        public char TopLeftCorner { get; }
        public char TopIntersection { get; }
        public char TopRightCorner { get; }
        public char LeftIntersection { get; }
        public char CentralIntersection { get; }
        public char RightIntersection { get; }
        public char BottomLeftCorner { get; }
        public char BottomIntersection { get; }
        public char BottomRightCorner { get; }
    }
}