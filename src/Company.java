import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Company {
    private Integer companyId;
    private String companyName;
    private String industry;
    private Date foundingDate;
    private Double revenue;
    private String headquartersLocation;
    private Connection connection;

    public Company(Connection connection) {
        this.connection = connection;
    }

    public Company(Integer companyId, String companyName, String industry,
                   Date foundingDate, Double revenue,
                   String headquartersLocation, Connection connection) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.industry = industry;
        this.foundingDate = foundingDate;
        this.revenue = revenue;
        this.headquartersLocation = headquartersLocation;
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getIndustry() {
        return industry;
    }

    public Date getFoundingDate() {
        return foundingDate;
    }

    public Double getRevenue() {
        return revenue;
    }

    public String getHeadquartersLocation() {
        return headquartersLocation;
    }

    public void showAllCompanies() {

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("SELECT * FROM company")) {

            List<Company> companyList = new ArrayList<>();

            while (rs.next()) {
                Company company = new Company(
                        rs.getInt("company_id"),
                        rs.getString("company_name"),
                        rs.getString("industry"),
                        rs.getDate("founding_date"),
                        rs.getDouble("revenue"),
                        rs.getString("headquarters_location"),
                        getConnection());
                companyList.add(company);
            }

            companyList.stream()
                    .map(company -> String.format("%d %s %s %s %f %s",
                            company.getCompanyId(),
                            company.getCompanyName(),
                            company.getIndustry(),
                            company.getFoundingDate(),
                            company.getRevenue(),
                            company.getHeadquartersLocation()))
                    .forEach(System.out::println);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertCompany(Company company) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO company(company_name, industry, founding_date, revenue, headquarters_location) values (?,?,?,?,?)"
        );

        preparedStatement.setString(1, company.getCompanyName());
        preparedStatement.setString(2, company.getIndustry());
        preparedStatement.setDate(3, company.getFoundingDate());
        preparedStatement.setDouble(4, company.getRevenue());
        preparedStatement.setString(5, company.getHeadquartersLocation());

        preparedStatement.execute();
    }

    public void deleteCompany(int companyId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "delete from company where company_id = ?"
        );
        preparedStatement.setInt(1, companyId);
        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Company with ID " + companyId + " deleted successfully.");
        } else {
            System.out.println("No company found with ID " + companyId + ". No deletion performed.");
        }
    }

    public void updateCompany(int companyId, int updateData, String name) throws SQLException {
        PreparedStatement preparedStatement = null;
        switch (updateData) {
            case 1 -> preparedStatement = connection.prepareStatement(
                    "UPDATE company SET company_name = ? where employee_id = ?"
            );
            case 2 -> preparedStatement = connection.prepareStatement(
                    "UPDATE company SET industry = ? where employee_id = ?"
            );
            case 5 -> preparedStatement = connection.prepareStatement(
                    "UPDATE company SET headquarters_location = ? where employee_id = ?"
            );
            default -> throw new IllegalArgumentException("Invalid updateData value: " + updateData);
        }

        preparedStatement.setString(1, name);
        preparedStatement.setInt(2, companyId);
        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Company with ID " + companyId + " updated successfully.");
        } else {
            System.out.println("No company found with ID " + companyId + ". No update performed.");
        }
    }

    public void updateCompany(int companyId, Date date) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE company SET founding_date = ? where employee_id = ?"
        );
        preparedStatement.setString(1, String.valueOf(date));
        preparedStatement.setInt(2, companyId);
        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Company with ID " + companyId + " updated successfully.");
        } else {
            System.out.println("No company found with ID " + companyId + ". No update performed.");
        }
    }

    public void updateCompany(int companyId, Integer revenue) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE company SET revenue = ? where employee_id = ?"
        );
        preparedStatement.setInt(1, revenue);
        preparedStatement.setInt(2, companyId);
        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Company with ID " + companyId + " updated successfully.");
        } else {
            System.out.println("No company found with ID " + companyId + ". No update performed.");
        }
    }
}
