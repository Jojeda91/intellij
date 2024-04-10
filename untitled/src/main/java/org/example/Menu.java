package org.example;

import java.net.StandardSocketOptions;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;






public class Menu {
    public static Scanner scanner = new Scanner(System.in);


    public static void showMenu() {
        System.out.println("¿Qué quieres hacer?");
        System.out.println("1. Crear un empleado por defecto");
        System.out.println("2. Crear un empleado añadiendo datos");
        System.out.println("3. Evaluar un empleado");
        System.out.println("4. Mostrar lista de empleados");
        System.out.println("5. Mostrar información de un empleado");
        System.out.println("6. Salir");
    }

    public static void showList() {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/examples", "usuario", "1234")) {
            String sql = "SELECT fullName, years, salary, category FROM Employees";
            try (PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String fullName = resultSet.getString("fullName");
                    int years = resultSet.getInt("years");
                    double salary = resultSet.getDouble("salary");
                    String category = resultSet.getString("category");
                    System.out.println("Nombre: " + fullName + ", Años: " + years + ", Salario: " + salary + ", Categoría: " + category);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al mostrar la lista de empleados: " + e.getMessage());
        }
    }


    public static void execute(){
        boolean salir = false;
        int option;
        while (salir==false){
            showMenu();
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option){
                case 1:
                    new Employee();
                    System.out.println("Has creado un nuevo empleado");
                    break;
                case 2:
                    System.out.println("Ingrese el nombre del empleado:");
                    String name = scanner.nextLine();
                    System.out.println("Ingrese los años de experiencia:");
                    int yearsInput = scanner.nextInt();
                    System.out.println("Ingrese el salario:");
                    double salaryInput = scanner.nextDouble();
                    scanner.nextLine(); // Consumir el salto de línea
                    new Employee(name, yearsInput, salaryInput);
                    System.out.println("Has creado un nuevo empleado");
                    break;
                case 3:
                    System.out.println("Introduce el nombre del empleado que quieres evaluar:");
                    String employeeName = scanner.nextLine();
                    Employee employeeToCategory = null;

                    try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/examples", "usuario", "1234")) {
                        String sql = "SELECT fullName, years, salary, category FROM Employees WHERE fullName = ?";
                        try (PreparedStatement statement = connection.prepareStatement(sql)) {
                            statement.setString(1, employeeName);
                            try (ResultSet resultSet = statement.executeQuery()) {
                                if (resultSet.next()) {
                                    String fullName = resultSet.getString("fullName");
                                    int years = resultSet.getInt("years");
                                    double salary = resultSet.getDouble("salary"); // Cambiar salary a salaryInput
                                    String category = resultSet.getString("category");
                                    employeeToCategory = new Employee(fullName, years, salary); // Usar salaryInput en lugar de salary
                                    employeeToCategory.setCategory(category);
                                }
                            }
                        }
                    } catch (SQLException e) {
                        System.out.println("Error al buscar empleado en la base de datos: " + e.getMessage());
                    }

                    // Verificar si se encontró el empleado
                    if (employeeToCategory != null) {
                        employeeToCategory.Evaluate();
                        System.out.println("Empleado evaluado correctamente.");
                    } else {
                        System.out.println("No se encontró ningún empleado con ese nombre.");
                    }
                    break;

                case 4:
                    showList();
                    break;
                case 5:
                    System.out.println("Introduce el nombre del empleado del que quieres ver la información:");
                    String nameEmployeeInfo = scanner.nextLine();

                    Employee employeeInfo = null;

                    try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/examples", "usuario", "1234")) {
                        String sql = "SELECT fullName, years, salary, category FROM Employees WHERE fullName = ?";
                        try (PreparedStatement statement = connection.prepareStatement(sql)) {
                            statement.setString(1, nameEmployeeInfo);
                            try (ResultSet resultSet = statement.executeQuery()) {
                                if (resultSet.next()) {
                                    String fullName = resultSet.getString("fullName");
                                    int years = resultSet.getInt("years");
                                    double salary = resultSet.getDouble("salary");
                                    String category = resultSet.getString("category");
                                    employeeInfo = new Employee(fullName, years, salary);
                                    employeeInfo.setCategory(category);
                                }
                            }
                        }
                    } catch (SQLException e) {
                        System.out.println("Error al buscar empleado en la base de datos: " + e.getMessage());
                    }

// Verificar si se encontró el empleado
                    if (employeeInfo != null) {
                        employeeInfo.ShowInfo(); // Mostrar información del empleado
                    } else {
                        System.out.println("No se encontró ningún empleado con ese nombre.");
                    }

                    break;
                case 6:
                    salir = true;
                    break;
            }
        System.out.println("Abandonaste el programa, tanta paz encuentres como descanso dejas");
        }
    }
}
