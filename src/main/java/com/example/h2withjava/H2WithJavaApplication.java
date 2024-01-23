package com.example.h2withjava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

@SpringBootApplication
public class H2WithJavaApplication {


    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(H2WithJavaApplication.class, args);

        DataSource dataSource = context.getBean(DataSource.class);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        createRegistrationTable(jdbcTemplate);

        addDataToRegistrationTable(jdbcTemplate);

        retrieveDataFromRegistrationTable(jdbcTemplate);

        updateAndRetrieveDataFromRegistration(jdbcTemplate);

        retrieveDataFromEmployees(jdbcTemplate);

        retrieveDataFromEmployeesWithTableJoin(jdbcTemplate);
    }

    private static void createRegistrationTable(JdbcTemplate jdbcTemplate) {

        String sql = "CREATE TABLE   REGISTRATION " +
                "(id INTEGER AUTO_INCREMENT  PRIMARY KEY, " +
                " first VARCHAR(255), " +
                " last VARCHAR(255), " +
                " age INTEGER)";

        jdbcTemplate.execute(sql);

    }

        public static void addDataToRegistrationTable(JdbcTemplate jdbcTemplate)  {

            String sql = "insert into REGISTRATION(first, last, age)  VALUES" +
                        "('Bill', 'Fairfield', 71)";

            jdbcTemplate.execute(sql);
        }

        public static void retrieveDataFromRegistrationTable(JdbcTemplate jdbcTemplate)  {
            String sql = "SELECT * FROM REGISTRATION";

            List<Person> peeps = jdbcTemplate.query(sql, new RowMapper<Person>() {
                public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Person person = new Person();

                    person.setId(rs.getInt("id"));
                    person.setFirstName(rs.getString("first"));
                    person.setLastName(rs.getString("last"));
                    person.setAge(rs.getInt("age"));
                    return person;
                }
            });

            peeps.forEach(System.out::println);

        }

    public static void updateAndRetrieveDataFromRegistration(JdbcTemplate jdbcTemplate) {
        String sql = "UPDATE Registration " +
                "SET age = 30 WHERE first='Bill'";


        jdbcTemplate.execute(sql);

        sql = "SELECT id, first, last, age FROM REGISTRATION";

        List<Person> peeps = jdbcTemplate.query(sql, new RowMapper<Person>() {
            public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
                Person person = new Person();

                person.setId(rs.getInt("id"));
                person.setFirstName(rs.getString("first"));
                person.setLastName(rs.getString("last"));

                return person;
            }
        });

        peeps.forEach(person -> {
                System.out.println(person.getId());
                System.out.println(person.getFirstName());
                System.out.println(person.getLastName());
        });
    }

    public static void retrieveDataFromEmployees(JdbcTemplate jdbcTemplate)  {

        String sql = "SELECT * FROM EMPLOYEES";

        List<Person> peeps = jdbcTemplate.query(sql, new RowMapper<Person>() {
            public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
                Person person = new Person();

                person.setId(rs.getInt("employee_id"));
                person.setFirstName(rs.getString("first_name"));
                person.setLastName(rs.getString("last_name"));
                person.setEmailAddress(rs.getString("email"));
                person.setCompanyName(rs.getString("company_id"));

                return person;
            }
        });

        peeps.forEach(person -> {
            System.out.println(person.getId());
            System.out.println(person.getFirstName());
            System.out.println(person.getLastName());
            System.out.println(person.getEmailAddress());
            System.out.println(person.getCompanyName());
        });
    }

    public static void retrieveDataFromEmployeesWithTableJoin(JdbcTemplate jdbcTemplate)  {

        String sql = ("SELECT first_name, last_name, email, company_name FROM EMPLOYEES" +
                " JOIN COMPANIES ON COMPANIES.company_id = EMPLOYEES.company_id");

        List<Person> peeps = jdbcTemplate.query(sql, new RowMapper<Person>() {
            public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
                Person person = new Person();

                person.setFirstName(rs.getString("first_name"));
                person.setLastName(rs.getString("last_name"));
                person.setEmailAddress(rs.getString("email"));
                person.setCompanyName(rs.getString("company_name"));

                return person;
            }
        });

        peeps.forEach(person -> {
            System.out.println(person.getFirstName());
            System.out.println(person.getLastName());
            System.out.println(person.getEmailAddress());
            System.out.println(person.getCompanyName());
        });
    }
}
class Person {

    private int id;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String companyName;

    private int age;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}



