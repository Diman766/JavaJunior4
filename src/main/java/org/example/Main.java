package org.example;

import org.example.models.Course;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {


        String url = "jdbc:mysql://schoolDB:3306/";
//        String url = "jdbc:mysql://localhost:3306/";
        String user = "root";
        String password = "admin";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            // Создание базы данных
            createDatabase(connection);
            System.out.println("Database created successfully");

            // Использование базы данных
            useDatabase(connection);
            System.out.println("Use database successfully");

            // Создание таблицы
            createTable(connection);
            System.out.println("Create table successfully");

        } catch (SQLException e) {
            e.printStackTrace();
        }




        try (SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Course.class)
                .buildSessionFactory()) {


            // Создание объекта
            Course course = new Course("JavaJunior", 5);

            // Добавление объекта
            addCourse(sessionFactory, course);

            // Чтение объекта из базы данных

            readCourse(sessionFactory, 1);

            // Обновление объекта

            changeCourse(sessionFactory, 1, "JJ", 10);

            // Удаление объекта

            deleteCourse(sessionFactory, 11);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createDatabase(Connection connection) throws SQLException {
        String createDatabaseSQL = "CREATE DATABASE IF NOT EXISTS schoolDB;";
        try (PreparedStatement statement = connection.prepareStatement(createDatabaseSQL)) {
            statement.execute();
        }
    }

    private static void useDatabase(Connection connection) throws SQLException {
        String useDatabaseSQL = "USE schoolDB;";
        try (PreparedStatement statement = connection.prepareStatement(useDatabaseSQL)) {
            statement.execute();
        }
    }

    private static void createTable(Connection connection) throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS Courses (id INT AUTO_INCREMENT PRIMARY KEY, title VARCHAR(255), duration INT);";
        try (PreparedStatement statement = connection.prepareStatement(createTableSQL)) {
            statement.execute();
        }
    }

    private static void addCourse(SessionFactory sessionFactory, Course course) {

        // Создание сессии
        Session session = sessionFactory.getCurrentSession();

        // Начало транзакции
        session.beginTransaction();

        // Добавление объекта
        session.save(course);
        System.out.println("Object Course save successfully");
        session.getTransaction().commit();
    }

    private static void readCourse(SessionFactory sessionFactory, int id) {

        // Создание сессии
        Session session = sessionFactory.getCurrentSession();

        // Начало транзакции
        session.beginTransaction();

        // Чтение объекта
        Course course = session.get(Course.class, id);
        System.out.println("Object Course read successfully");
        System.out.println("Course object: " + course);
        session.getTransaction().commit();
    }

    private static void changeCourse(SessionFactory sessionFactory, int id, String title, int duration) {

        // Создание сессии
        Session session = sessionFactory.getCurrentSession();

        // Начало транзакции
        session.beginTransaction();

        // Чтение объекта
        Course retrievedCourse = session.get(Course.class, id);

        // Изменение объекта
        if (retrievedCourse != null) {
            retrievedCourse.setTitle(title);
            retrievedCourse.setDuration(duration);
            session.update(retrievedCourse);
            System.out.println("Object Course update successfully");
        } else {
            System.out.println("The object does not exist");
        }

        session.getTransaction().commit();
    }

    private static void deleteCourse(SessionFactory sessionFactory, int id) {

        // Создание сессии
        Session session = sessionFactory.getCurrentSession();

        // Начало транзакции
        session.beginTransaction();

        // Удаление объекта

        Course deleteCourse = session.get(Course.class, id);

        if (deleteCourse != null) {
            session.delete(deleteCourse);
            System.out.println("Object Course delete successfully");
        } else {
            System.out.println("The object does not exist");
        }
        session.getTransaction().commit();
    }
}