package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
public class Employee {
    public String fullName;
    public int years;
    public double salary;
    public String category;
    public static int counterEmployees = 0;



    Employee(){
        this.counterEmployees++;
        this.fullName = "Empleado Fantasma "+counterEmployees;
        this.category = "Pendiente de evaluación";
        saveToDatabase();
    }

    public Employee(String fullName, int years, double salary) {
        this.fullName = fullName;
        this.years = years;
        this.salary = salary;
        this.category = "Pendiente de evaluación";
        this.counterEmployees++;
        saveToDatabase();
    }
    public void saveToDatabase() {
        String url = "jdbc:postgresql://localhost:5432/examples";
        String user = "usuario";
        String password = "1234";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "INSERT INTO Employees (fullName, years, salary, category) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, fullName);
                statement.setInt(2, years);
                statement.setDouble(3, salary);
                statement.setString(4, category);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error al guardar empleado en la base de datos: " + e.getMessage());
        }
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getYears() {
        return years;
    }

    public void setYears(int years) {
        this.years = years;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void Evaluate(){
        if (years<=3){
            setCategory("Principiante");
        } else if (years > 3 && years < 18) {
            setCategory("Intermedio");
        } else {
            setCategory("Senior");
        }
    }

    public void ShowInfo(){
        System.out.println("El empleado se llama "+fullName);
        System.out.println("Lleva "+years+" años en la empresa");
        System.out.println("Su salario es de "+salary);
        System.out.println("La categoría del empleado es "+"'"+category+"'");
    }
}
