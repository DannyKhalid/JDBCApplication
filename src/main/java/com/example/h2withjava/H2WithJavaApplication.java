package com.example.h2withjava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.plaf.nimbus.State;
import java.sql.*;

@SpringBootApplication
public class H2WithJavaApplication {
    private static String JDBC_DRIVER = "org.h2.Driver";
    private static String DB_URL = "jdbc:h2:mem:testdb";
    private static String USER = "sa";
    private static String PASSWORD = "";

    private static Connection conn = null;
    private static Statement stmt = null;

    public static void main(String[] args) {
        SpringApplication.run(H2WithJavaApplication.class, args);

        //Step 1:  Set up the driver
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        createRegistrationTable();

        addDataToRegistrationTable();

        retrieveDataFromRegistrationTable();

        updateAndRetrieveDataFromRegistration();

        retrieveDataFromEmployees();

        retrieveDataFromEmployeesWithTableJoin();
    }

    private static void createRegistrationTable() {

        String sql = "CREATE TABLE   REGISTRATION " +
                "(id INTEGER AUTO_INCREMENT  PRIMARY KEY, " +
                " first VARCHAR(255), " +
                " last VARCHAR(255), " +
                " age INTEGER)";

        //Step 2:  Open a connection to the database
        //Step 3 Prepare a SQL query
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();){

            //Step 4:  Execute the SQL statement
            stmt.executeUpdate(sql);

            //Step 5:  Handle the response.  We don't have to do this for a CREATE statement

            //Steps 6 and 7:  Close open things
            //don't need this anymore with try-with-resources
            //stmt.close();
            //conn.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

        public static void addDataToRegistrationTable()  {
            String sql = "insert into REGISTRATION(first, last, age)  VALUES" +
                    "('Bill', 'Fairfield', 71)";

            //Step 2:  Open a connection to the database
            //Step 3 Prepare a SQL query
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
                 Statement stmt = conn.createStatement();){

                //Step 4:  Execute the SQL statement
                stmt.executeUpdate(sql);

                //Step 5:  Handle the response.  We don't have to do this for a CREATE statement

                //Steps 6 and 7:  Close open things
                //don't need this anymore with try-with-resources
                //stmt.close();
                //conn.close();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

        public static void retrieveDataFromRegistrationTable()  {
            String sql = "SELECT * FROM REGISTRATION";

            //Step 2:  Open a connection to the database
            //Step 3 Prepare a SQL query
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
                 Statement stmt = conn.createStatement();
                 //Step 4:  Execute the SQL statement
                 ResultSet rs = stmt.executeQuery(sql);){

                //Step 5:  Handle the response.
                while(rs.next()) {
                    int id = rs.getInt("id");
                    String first = rs.getString("first");
                    String last = rs.getString("last");
                    int age = rs.getInt("age");

                    System.out.print("ID: " + id);
                    System.out.print(", First: " + first);
                    System.out.print(", Last: " + last);
                    System.out.println(", Age: " + age);
                }

                //Steps 6 and 7:  Close open things
                //don't need this anymore with try-with-resources
                //rs.close();
                //stmt.close();
                //conn.close();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

    public static void updateAndRetrieveDataFromRegistration() {
        String sql = "UPDATE Registration " + "SET age = 30 WHERE first='Bill'";
        //Step 2:  Open a connection to the database
        //Step 3 Prepare a SQL query
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();){

            //Step 4:  Execute the SQL statement
            stmt.executeUpdate(sql);

            String selectSql = "SELECT id, first, last, age FROM REGISTRATION";

            try (ResultSet rs = stmt.executeQuery(selectSql)) {
                // STEP 5: Extract data from result set
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String first = rs.getString("first");
                    String last = rs.getString("last");
                    int age = rs.getInt("age");

                    System.out.print("ID: " + id);
                    System.out.print(", First: " + first);
                    System.out.print(", Last: " + last);
                    System.out.println(", Age: " + age);
                }
            }

            // STEP 6 and 7: Clean-up environment
            //don't need this anymore with try-with-resources
            //rs.close();
            //stmt.close();
            //conn.close();
        } catch(SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        }
    }

    public static void retrieveDataFromEmployees()  {
        String sql = "SELECT * FROM EMPLOYEES";
        //Step 2:  Open a connection to the database
        //Step 3 Prepare a SQL query
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();){

            //Step 4:  Execute the SQL statement
            try (ResultSet rs = stmt.executeQuery(sql);) {

                //Step 5:  Handle the response.
                while (rs.next()) {
                    int id = rs.getInt("employee_id");
                    String first_name = rs.getString("first_name");
                    String last_name = rs.getString("last_name");
                    String email = rs.getString("email");
                    int company_id = rs.getInt("company_id");

                    System.out.print("ID: " + id);
                    System.out.print(", First: " + first_name);
                    System.out.print(", Last: " + last_name);
                    System.out.print(", Email: " + email);
                    System.out.println(", Company ID: " + company_id);
                }
            }

            //Steps 6 and 7:  Close open things
            //don't need this anymore with try-with-resources
            //stmt.close();
            //conn.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void retrieveDataFromEmployeesWithTableJoin()  {
        String sql = "SELECT first_name, last_name, email, company_name FROM EMPLOYEES" +
                " JOIN COMPANIES ON COMPANIES.company_id = EMPLOYEES.company_id";
        //Step 2:  Open a connection to the database
        //Step 3 Prepare a SQL query
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             //Step 4:  Execute the SQL statement
             ResultSet rs = stmt.executeQuery(sql);){

            //Step 5:  Handle the response.
            while(rs.next()) {
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                String email = rs.getString("email");
                String company_name = rs.getString("company_name");

                System.out.print("First: " + first_name);
                System.out.print(", Last: " + last_name);
                System.out.print(", Email: " + email);
                System.out.println(", Company Name: " + company_name);
            }

            //Steps 6 and 7:  Close open things
            //don't need this anymore with try-with-resources
            //stmt.close();
            //conn.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}

