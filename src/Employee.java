import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Employee {
    private Integer id;
    private String  firstName;
    private String lastName;
    private Date birthDate;
    private Date hireDate;
    private Integer salary;
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public Integer getSalary() {
        return salary;
    }

    public Employee(Connection connection) {
        this.connection = connection;
    }

    public Employee(Integer id, String firstName, String lastName, Date birthDate, Date hireDate, Integer salary,
                    Connection connection) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.hireDate = hireDate;
        this.salary = salary;
        this.connection = connection;
    }

    public void updateEmployee(Integer id, Integer updateData, String name) throws SQLException {
        PreparedStatement preparedStatement = null;
        switch (updateData) {
            case 1 -> preparedStatement = connection.prepareStatement(
                    "UPDATE employee SET first_name = ? where employee_id = ?"
            );
            case 2 -> preparedStatement = connection.prepareStatement(
                    "UPDATE employee SET last_name = ? where employee_id = ?"
            );
            default -> throw new IllegalArgumentException("Invalid updateData value: " + updateData);
        }

        preparedStatement.setString(1, name);
        preparedStatement.setInt(2, id);
        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Employee with ID " + id + " updated successfully.");
        } else {
            System.out.println("No employee found with ID " + id + ". No update performed.");
        }
    }

    public void updateEmployee(Integer id, Integer updateDate, java.util.Date date) throws SQLException {
        PreparedStatement preparedStatement = null;
        if (date!= null) {
            switch (updateDate) {
                case 3 -> preparedStatement = connection.prepareStatement(
                        "UPDATE employee SET birth_date = ? where employee_id = ?"
                );
                case 4 -> preparedStatement = connection.prepareStatement(
                        "UPDATE employee SET hire_date = ? where employee_id = ?"
                );
                default -> throw new IllegalStateException("Unexpected value: " + updateDate);
            }
            preparedStatement.setDate(1, (java.sql.Date) date);
            preparedStatement.setInt(2, id);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Employee with ID " + id + " updated successfully.");
            } else {
                System.out.println("No employee found with ID " + id + ". No update performed.");
            }}
        else {
            System.out.println("Invalid date input. Please try again.");
        }
    }

    public void updateEmployee(Integer id, Integer updateDate, Integer salary) throws SQLException {
        PreparedStatement preparedStatement = null;
        if (updateDate == 5) {
            preparedStatement = connection.prepareStatement(
                    "UPDATE employee SET salary = ? where employee_id = ?"
            );
        } else {
            throw new IllegalArgumentException("Invalid updateData value: " + updateDate);
        }
        preparedStatement.setInt(1, salary);
        preparedStatement.setInt(2, id);
        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Employee with ID " + id + " updated successfully.");
        } else {
            System.out.println("No employee found with ID " + id + ". No update performed.");
        }

    }


    public void deleteEmployee(Integer id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "delete from employee where employee_id = ?"
        );
        preparedStatement.setInt(1, id);
        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Employee with ID " + id + " deleted successfully.");
        } else {
            System.out.println("No employee found with ID " + id + ". No deletion performed.");
        }
    }

    public void insertEmployee(Employee employee) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "insert into employee(first_name, last_name, birth_date, hire_date, salary) values(?,?,?,?,?)"
        );
        preparedStatement.setString(1, employee.getFirstName());
        preparedStatement.setString(2, employee.getLastName());
        preparedStatement.setDate(3, employee.getBirthDate());
        preparedStatement.setDate(4, employee.getHireDate());
        preparedStatement.setInt(5, employee.getSalary());
        preparedStatement.execute();
    }

    public void selectAllEmployee() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(
                "select * from employee");

        List<Employee> employeesList = new ArrayList<>();

        while (rs.next()) {
            Employee employee = new Employee
                    (rs.getInt("employee_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getDate("birth_date"),
                            rs.getDate("hire_date"),
                            rs.getInt("salary"),
                            getConnection());

            employeesList.add(employee);
        }

        employeesList.stream()
                .map(employee -> String.format("%d %s %s %s %s %d",
                        employee.getId(),
                        employee.getFirstName(),
                        employee.getLastName(),
                        employee.getBirthDate(),
                        employee.getHireDate(),
                        employee.getSalary()))
                .forEach(System.out::println);
    }
}
