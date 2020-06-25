using System.Collections.Generic;
using System.Linq;

namespace Juh.List
{
    public static class StudentsList
    {
        public static IEnumerable<Student> Filter(this DoublyLinkedList<Student> list)
        {
            return from node in list.Nodes()
                where node.Value.Grade == 2 && !node.Value.Army
                select node.Value;
        }
    }
}