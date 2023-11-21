import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    static Connection connection;
    static Scanner scanner;

    public static void main(String[] args) throws SQLException, ClassNotFoundException, ParseException {
        Class.forName("org.postgresql.Driver");
        connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/postgres",
                "postgres",
                "123456");

        scanner = new Scanner(System.in);
        boolean isNext = true;
        while (isNext) {
            System.out.println("""
                        Which of the tables do you want to work in?\s
                        1. Table Company\s
                        2. Table Employee\s
                    """);
            int tableNum = scanner.nextInt();
            System.out.println("Which of the operations do you want to choose?");
            switch (tableNum) {
                case 1 -> {
                    boolean isCompany = true;
                    while (isCompany) {
                        System.out.println("""
                                \nSelect one of them:\s
                                1. Show all companies\s
                                2. Insert a new company\s
                                3. Delete a company\s
                                4. Update company data\s
                                5. None\s
                                """);
                        int choice = scanner.nextInt();
                        Company company = new Company(connection);
                        switch (choice) {
                            case 1 -> company.showAllCompanies();
                            case 2 -> {
                                System.out.println("Select company name");
                                String companyName = scanner.next();
                                System.out.println("Select company industry");
                                String industry = scanner.next();
                                System.out.println("Select company founding date: dd-MM-yyyy");
                                java.sql.Date foundingDate = parseDate(scanner.next());
                                System.out.println("Select the company's revenue: per billion");
                                Double revenue = scanner.nextDouble();
                                System.out.println("Select the location of the company's headquarters");
                                String headquartersLocation = scanner.next();
                                company = new Company(null, companyName, industry, foundingDate, revenue,
                                        headquartersLocation, connection);
                                company.insertCompany(company);
                            }
                            case 3 -> {
                                System.out.println("Select the company id: ");
                                int companyId = scanner.nextInt();
                                System.out.println("Are you sure?");
                                if (scanner.next().equalsIgnoreCase("Yes")) {
                                    company.deleteCompany(companyId);
                                }
                            }
                            case 4 -> {
                                boolean isUpdate = true;
                                while (isUpdate) {
                                    System.out.println("Select the company id: ");
                                    int companyId = scanner.nextInt();
                                    while (isUpdate) {
                                        System.out.println("Which company data do you want to update?" +
                                                "\n1. Company name" +
                                                "\n2. Industry" +
                                                "\n3. Founding date" +
                                                "\n4. Revenue" +
                                                "\n5. Headquarters location");
                                        int updateData = scanner.nextInt();

                                        switch (updateData) {
                                            case 1, 2, 5 -> {
                                                String name = scanner.next();
                                                company.updateCompany(companyId, updateData, name);
                                            }
                                            case 3 -> {
                                                System.out.println("Date format: dd-MM-yyyy");
                                                java.sql.Date date = parseDate(scanner.next());
                                                company.updateCompany(companyId, date);
                                            }
                                            case 4 -> {
                                                Integer revenue = scanner.nextInt();
                                                company.updateCompany(companyId, revenue);
                                            }
                                            default ->
                                                    throw new IllegalArgumentException("Invalid updateData value: " + updateData);

                                        }

                                        System.out.println("Anything else?");
                                        isUpdate = scanner.next().equalsIgnoreCase("Yes");
                                    }
                                    System.out.println("Do you want to update another employee's data?");
                                    isUpdate = scanner.next().equalsIgnoreCase("Yes");
                                }

                                System.out.println("Do you want to continue?");
                                isCompany = scanner.next().equalsIgnoreCase("Yes");
                            }
                            case 5 -> isCompany = false;
                        }
                    }

                    System.out.println("Would you like to continue working with the tables?");
                    isNext = scanner.next().equalsIgnoreCase("Yes");

                }

                case 2 -> {
                    boolean isEmployee = true;
                    while (isEmployee) {
                        System.out.println("""
                                1. Show all employees\s
                                2. Insert a new employee\s
                                3. Delete a employee\s
                                4. Update employee data\s
                                5. None\s
                                """);
                        int choice = scanner.nextInt();
                        Employee employee = new Employee(connection);
                        switch (choice) {
                            case 1 -> employee.selectAllEmployee();
                            case 2 -> {
                                System.out.println("Select first name");
                                String firstName = scanner.next();
                                System.out.println("Select lastname");
                                String lastName = scanner.next();
                                System.out.println("Select date of birth: dd-MM-yyyy");
                                java.sql.Date birthDate = parseDate(scanner.next());
                                System.out.println("Select date of hire: dd-MM-yyyy");
                                java.sql.Date hireDate = parseDate(scanner.next());
                                System.out.println("Select salary");
                                Integer salary = scanner.nextInt();

                                employee = new Employee(null, firstName, lastName, birthDate, hireDate, salary, connection);
                                employee.insertEmployee((employee));
                            }
                            case 3 -> {
                                System.out.println("Select the employee id: ");
                                int employeeId = scanner.nextInt();
                                System.out.println("Are you sure?");
                                if (scanner.next().equalsIgnoreCase("Yes")) {
                                    employee.deleteEmployee(employeeId);
                                }
                            }
                            case 4 -> {
                                boolean isUpdate = true;
                                while (isUpdate) {
                                    System.out.println("Select the employee id: ");
                                    int employeeId = scanner.nextInt();
                                    while (isUpdate) {
                                        System.out.println("Which employee data do you want to update?" +
                                                "\n1. Firstname" +
                                                "\n2. Lastname" +
                                                "\n3. Birthdate" +
                                                "\n4. Hire date" +
                                                "\n5. Salary");
                                        int updateData = scanner.nextInt();

                                        switch (updateData) {
                                            case 1, 2 -> {
                                                String name = scanner.next();
                                                employee.updateEmployee(employeeId, updateData, name);
                                            }
                                            case 3, 4 -> {
                                                System.out.println("Date format: dd-MM-yyyy");
                                                Date date = parseDate(scanner.next());
                                                employee.updateEmployee(employeeId, updateData, date);
                                            }
                                            case 5 -> {
                                                Integer salary = scanner.nextInt();
                                                employee.updateEmployee(employeeId, updateData, salary);
                                            }
                                        }

                                        System.out.println("Anything else?");
                                        isUpdate = scanner.next().equalsIgnoreCase("Yes");
                                    }
                                    System.out.println("Do you want to update another employee's data?");
                                    isUpdate = scanner.next().equalsIgnoreCase("Yes");
                                }

                                System.out.println("Do you want to continue?");
                                isEmployee = scanner.next().equalsIgnoreCase("Yes");

                            }
                            case 5 -> isEmployee = false;
                        }

                    }
                    System.out.println("Would you like to continue working with the tables?");
                    isNext = scanner.next().equalsIgnoreCase("Yes");
                }

            }

        }

    }

    private static java.sql.Date parseDate(String dateStr) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        java.util.Date parsedDate = dateFormat.parse(dateStr);
        return new java.sql.Date(parsedDate.getTime());
    }


}