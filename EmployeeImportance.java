// Time Complexity : O(N + E), where N = number of employees, E = total number of subordinate links
//                   We build a map in O(N) and visit each employee/subordinate once.
// Space Complexity : O(N) for the hashmap and queue in the worst case.
// Did this code successfully run on Leetcode : Yes
// Any problem you faced while coding this : No
//
// Approach (BFS with ID → Employee map):
// - Build a HashMap from employee id to Employee object for O(1) lookups.
// - Start BFS from the given id; add importance of each visited employee.
// - For each employee, enqueue all of their direct subordinates (by id → employee via the map).
// - Sum importance across the connected component reachable from the starting id.

import java.util.*;

class EmployeeImportance {
    public int getImportance(List<Employee> employees, int id) {
        if (employees == null || employees.isEmpty()) return 0;

        // Map id -> Employee for quick lookup
        Map<Integer, Employee> map = new HashMap<>();
        for (Employee emp : employees) {
            map.put(emp.id, emp);
        }

        Employee start = map.get(id);
        if (start == null) return 0; // id not found

        int result = 0;
        Queue<Employee> q = new LinkedList<>();
        q.add(start);

        while (!q.isEmpty()) {
            Employee cur = q.poll();
            result += cur.importance;

            // Enqueue subordinates
            if (cur.subordinates != null) {
                for (int subId : cur.subordinates) {
                    Employee sub = map.get(subId);
                    if (sub != null) q.add(sub);
                }
            }
        }
        return result;
    }

    // --- Main method for testing ---
    public static void main(String[] args) {
        /*
           Example:
           Employee 1: importance = 5, subs = [2,3]
           Employee 2: importance = 3, subs = []
           Employee 3: importance = 3, subs = []

           getImportance(employees, 1) -> 11
        */
        Employee e1 = new Employee(1, 5, Arrays.asList(2, 3));
        Employee e2 = new Employee(2, 3, Collections.emptyList());
        Employee e3 = new Employee(3, 3, Collections.emptyList());

        List<Employee> employees = Arrays.asList(e1, e2, e3);

        EmployeeImportance sol = new EmployeeImportance();
        System.out.println("Importance starting at 1: " + sol.getImportance(employees, 1)); // 11
        System.out.println("Importance starting at 2: " + sol.getImportance(employees, 2)); // 3
        System.out.println("Importance starting at 3: " + sol.getImportance(employees, 3)); // 3
        System.out.println("Importance starting at 99 (not found): " + sol.getImportance(employees, 99)); // 0
    }
}

// Definition for Employee.
class Employee {
    public int id;
    public int importance;
    public List<Integer> subordinates;
    public Employee(int id, int importance, List<Integer> subs) {
        this.id = id;
        this.importance = importance;
        this.subordinates = subs;
    }
}