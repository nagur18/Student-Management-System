package com.sms.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.sms.model.Student;
import com.sms.util.DBConnection;

public class DatabaseHandler {

    // INSERT
    public boolean insertStudent(Student s) {
        String sql = "INSERT INTO students (roll_no, name, course, marks) VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, s.getRollNo());
            ps.setString(2, s.getName());
            ps.setString(3, s.getCourse());
            ps.setInt(4, s.getMarks());

            ps.executeUpdate();
            return true;

        } catch (SQLIntegrityConstraintViolationException e) {
            // Duplicate roll number
            System.out.println("Roll Number Already Exists");
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // UPDATE
    public boolean updateStudent(Student s) {
        String sql = "UPDATE students SET name=?, course=?, marks=? WHERE id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, s.getName());
            ps.setString(2, s.getCourse());
            ps.setInt(3, s.getMarks());
            ps.setInt(4, s.getId());

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    public boolean deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // SELECT ALL
    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Student s = new Student(
                        rs.getInt("id"),
                        rs.getString("roll_no"),
                        rs.getString("name"),
                        rs.getString("course"),
                        rs.getInt("marks"),
                        rs.getTimestamp("created_at")
                );
                list.add(s);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // DUPLICATE CHECK
    public boolean checkDuplicateRollNo(String rollNo) {
        String sql = "SELECT id FROM students WHERE roll_no=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, rollNo);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // true if exists

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
