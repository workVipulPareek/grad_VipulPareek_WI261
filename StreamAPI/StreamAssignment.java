import java.util.*;
import java.util.stream.Collectors;

class Employee {
    private String name;
    private int age;
    private String gender;
    private double salary;
    private String designation;
    private String department;

    public Employee(String name, int age, String gender,
                    double salary, String designation, String department) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.salary = salary;
        this.designation = designation;
        this.department = department;
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public double getSalary() { return salary; }
    public String getDesignation() { return designation; }
    public String getDepartment() { return department; }

    public void setSalary(double salary) { this.salary = salary; }

    @Override
    public String toString() {
        return name + " | " + age + " | " + gender + " | "
                + salary + " | " + designation + " | " + department;
    }
}

public class StreamAssignment {

    static List<Employee> employees = new ArrayList<>();

    public static void main(String[] args) {

        generateEmployees(); 
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n==== COMPANY MENU ====");
            System.out.println("1. Highest Paid Employee");
            System.out.println("2. Count Male & Female Employees");
            System.out.println("3. Department Wise Total Expense");
            System.out.println("4. Top 5 Senior Employees (Age)");
            System.out.println("5. List Managers");
            System.out.println("6. Hike Salary by 20% (Except Managers)");
            System.out.println("7. Total Number of Employees");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            switch (choice) {

                case 1:
                    employees.stream()
                            .max(Comparator.comparing(Employee::getSalary))
                            .ifPresent(System.out::println);
                    break;

                case 2:
                    Map<String, Long> genderCount =
                            employees.stream()
                                    .collect(Collectors.groupingBy(
                                            Employee::getGender,
                                            Collectors.counting()));
                    System.out.println(genderCount);
                    break;

                case 3:
                    Map<String, Double> deptExpense =
                            employees.stream()
                                    .collect(Collectors.groupingBy(
                                            Employee::getDepartment,
                                            Collectors.summingDouble(Employee::getSalary)));
                    deptExpense.forEach((k, v) ->
                            System.out.println(k + " -> " + v));
                    break;

                case 4:
                    employees.stream()
                            .sorted(Comparator.comparing(Employee::getAge).reversed())
                            .limit(5)
                            .forEach(System.out::println);
                    break;

                case 5:
                    employees.stream()
                            .filter(e -> e.getDesignation().equalsIgnoreCase("Manager"))
                            .map(Employee::getName)
                            .forEach(System.out::println);
                    break;

                case 6:
                    employees.stream()
                            .filter(e -> !e.getDesignation().equalsIgnoreCase("Manager"))
                            .forEach(e -> e.setSalary(e.getSalary() * 1.20));
                    System.out.println("Salary hiked successfully!");
                    break;

                case 7:
                    System.out.println("Total Employees: " + employees.size());
                    break;

                case 0:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid Choice!");
            }

        } while (choice != 0);

        sc.close();
    }

    private static void generateEmployees() {

        String[] names = {"Amit","Riya","Rahul","Sneha","Vikram",
                "Anjali","Karan","Neha","Rohit","Priya",
                "Arjun","Megha","Sahil","Pooja","Nikhil",
                "Simran","Abhishek","Kriti","Varun","Divya",
                "Manish","Tanya","Akash","Isha","Deepak",
                "Lavanya","Harsh","Naina","Yash","Kavya",
                "Ramesh","Geeta","Tarun","Asha","Mohit"};

        String[] genders = {"Male","Female"};
        String[] designations = {"Developer","Tester","Manager","HR","Analyst"};
        String[] departments = {"IT","HR","Finance","Sales"};

        Random random = new Random();

        for (int i = 0; i < 35; i++) {

            employees.add(new Employee(
                    names[i],
                    22 + random.nextInt(25),
                    genders[random.nextInt(2)],
                    30000 + random.nextInt(70000),
                    designations[random.nextInt(designations.length)],
                    departments[random.nextInt(departments.length)]
            ));
        }
    }
}
