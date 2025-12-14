package com.sms.ui;

import java.awt.Font;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.sms.dao.DatabaseHandler;
import com.sms.model.Student;

public class StudentUI extends JFrame {

    // ===== State =====
    private int selectedStudentId = -1;

    // ===== UI Components =====
    private JTextField txtRoll;
    private JTextField txtName;
    private JTextField txtCourse;
    private JTextField txtMarks;

    private JTable table;
    private DefaultTableModel tableModel;

    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;

    private DatabaseHandler db;

    // ===== Constructor =====
    public StudentUI() {

        db = new DatabaseHandler();

        setTitle("Student Management System");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // ===== Title =====
        JLabel titleLabel = new JLabel("STUDENT MANAGEMENT SYSTEM");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBounds(250, 10, 400, 30);
        add(titleLabel);

        // ===== Form Panel =====
        JPanel formPanel = new JPanel();
        formPanel.setLayout(null);
        formPanel.setBounds(20, 60, 300, 300);
        add(formPanel);

        // Roll No
        formPanel.add(label("Roll No:", 10, 20));
        txtRoll = field(formPanel, 100, 20);

        // Name
        formPanel.add(label("Name:", 10, 60));
        txtName = field(formPanel, 100, 60);

        // Course
        formPanel.add(label("Course:", 10, 100));
        txtCourse = field(formPanel, 100, 100);

        // Marks
        formPanel.add(label("Marks:", 10, 140));
        txtMarks = field(formPanel, 100, 140);

        // ===== Table =====
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Roll No", "Name", "Course", "Marks", "Created At"}, 0
        );

        table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(340, 60, 520, 300);
        add(scrollPane);

        // ===== Buttons =====
        btnAdd = button("Add", 150, 380);
        btnUpdate = button("Update", 300, 380);
        btnDelete = button("Delete", 450, 380);
        btnClear = button("Clear", 600, 380);

        add(btnAdd);
        add(btnUpdate);
        add(btnDelete);
        add(btnClear);

        // ===== Button Actions =====
        btnAdd.addActionListener(e -> addStudent());
        btnUpdate.addActionListener(e -> updateStudent());
        btnDelete.addActionListener(e -> deleteStudent());
        btnClear.addActionListener(e -> clearForm());

        // ===== Table Selection =====
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedStudent();
            }
        });

        loadStudentsToTable();
        setVisible(true);
    }

    // ===== ADD STUDENT =====
    private void addStudent() {

        if (!validateInputs()) return;

        Student s = new Student(
                txtRoll.getText().trim(),
                txtName.getText().trim(),
                txtCourse.getText().trim(),
                Integer.parseInt(txtMarks.getText().trim())
        );

        if (db.checkDuplicateRollNo(s.getRollNo())) {
            show("Roll Number Already Exists!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (db.insertStudent(s)) {
            show("Record Added Successfully", JOptionPane.INFORMATION_MESSAGE);
            loadStudentsToTable();
            clearForm();
        } else {
            show("Database Error!", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ===== UPDATE STUDENT =====
    private void updateStudent() {

        if (selectedStudentId == -1) {
            show("Please select a student to update", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validateInputs()) return;

        Student s = new Student(
                selectedStudentId,
                txtRoll.getText().trim(),
                txtName.getText().trim(),
                txtCourse.getText().trim(),
                Integer.parseInt(txtMarks.getText().trim()),
                null
        );

        if (db.updateStudent(s)) {
            show("Record Updated Successfully", JOptionPane.INFORMATION_MESSAGE);
            loadStudentsToTable();
            clearForm();
            selectedStudentId = -1;
        } else {
            show("Update Failed!", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ===== DELETE STUDENT =====
    private void deleteStudent() {

        if (selectedStudentId == -1) {
            show("Please select a student to delete", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this record?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (db.deleteStudent(selectedStudentId)) {
                show("Record Deleted Successfully", JOptionPane.INFORMATION_MESSAGE);
                loadStudentsToTable();
                clearForm();
                selectedStudentId = -1;
            } else {
                show("Delete Failed!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ===== LOAD SELECTED ROW =====
    private void loadSelectedStudent() {

        int row = table.getSelectedRow();
        if (row == -1) return;

        int modelRow = table.convertRowIndexToModel(row);

        selectedStudentId = (int) tableModel.getValueAt(modelRow, 0);
        txtRoll.setText(tableModel.getValueAt(modelRow, 1).toString());
        txtName.setText(tableModel.getValueAt(modelRow, 2).toString());
        txtCourse.setText(tableModel.getValueAt(modelRow, 3).toString());
        txtMarks.setText(tableModel.getValueAt(modelRow, 4).toString());
    }

    // ===== LOAD TABLE =====
    private void loadStudentsToTable() {

        tableModel.setRowCount(0);
        List<Student> students = db.getAllStudents();

        for (Student s : students) {
            tableModel.addRow(new Object[]{
                    s.getId(),
                    s.getRollNo(),
                    s.getName(),
                    s.getCourse(),
                    s.getMarks(),
                    s.getCreatedAt()
            });
        }
    }

    // ===== VALIDATION =====
    private boolean validateInputs() {

        if (txtRoll.getText().trim().isEmpty() ||
            txtName.getText().trim().isEmpty() ||
            txtCourse.getText().trim().isEmpty() ||
            txtMarks.getText().trim().isEmpty()) {

            show("All fields are required!", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            int marks = Integer.parseInt(txtMarks.getText().trim());
            if (marks < 0 || marks > 100) {
                show("Marks must be between 0 and 100", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            show("Marks must be numeric", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    // ===== CLEAR FORM =====
    private void clearForm() {
        txtRoll.setText("");
        txtName.setText("");
        txtCourse.setText("");
        txtMarks.setText("");
        table.clearSelection();
        selectedStudentId = -1;
    }

    // ===== UI HELPERS =====
    private JLabel label(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 80, 25);
        return lbl;
    }

    private JTextField field(JPanel panel, int x, int y) {
        JTextField tf = new JTextField();
        tf.setBounds(x, y, 180, 25);
        panel.add(tf);
        return tf;
    }

    private JButton button(String text, int x, int y) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, 100, 30);
        return btn;
    }

    private void show(String msg, int type) {
        JOptionPane.showMessageDialog(this, msg, "Message", type);
    }

    public static void main(String[] args) {
        new StudentUI();
    }
}
