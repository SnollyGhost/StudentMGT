import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginRegisterApp extends JFrame implements ActionListener {

    // Components for Login page
    private JTextField loginUsername;
    private JPasswordField loginPassword;
    private JButton loginButton;
    private JButton registerRedirectButton;

    // Components for Registration page
    private JTextField registerUsername;
    private JPasswordField registerPassword;
    private JButton registerButton;
    private JButton backToLoginButton;

    // Labels for input fields
    private JLabel loginUsernameLabel;
    private JLabel loginPasswordLabel;
    private JLabel registerUsernameLabel;
    private JLabel registerPasswordLabel;

    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/student_management?useSSL=false";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1697";

    // Placeholder for logged-in user ID
    private int loggedInUserId;
    private String loggedInUserRole;

    public LoginRegisterApp() {
        super("Login Page");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        setLocationRelativeTo(null); // Center the window on the screen

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Login page components
        loginUsernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(loginUsernameLabel, gbc);

        loginUsername = new JTextField(15);
        gbc.gridx = 1;
        add(loginUsername, gbc);

        loginPasswordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(loginPasswordLabel, gbc);

        loginPassword = new JPasswordField(15);
        gbc.gridx = 1;
        add(loginPassword, gbc);

        loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginButton.addActionListener(this);
        add(loginButton, gbc);

        registerRedirectButton = new JButton("Register");
        gbc.gridy = 3;
        registerRedirectButton.addActionListener(this);
        add(registerRedirectButton, gbc);

        // Registration page components
        registerUsernameLabel = new JLabel("Username:");
        registerUsernameLabel.setVisible(false); // Hide initially
        gbc.gridy = 0;
        add(registerUsernameLabel, gbc);

        registerUsername = new JTextField(15);
        registerUsername.setVisible(false); // Hide initially
        gbc.gridx = 1;
        add(registerUsername, gbc);

        registerPasswordLabel = new JLabel("Password:");
        registerPasswordLabel.setVisible(false); // Hide initially
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(registerPasswordLabel, gbc);

        registerPassword = new JPasswordField(15);
        registerPassword.setVisible(false); // Hide initially
        gbc.gridx = 1;
        add(registerPassword, gbc);

        registerButton = new JButton("Register");
        registerButton.setVisible(false); // Hide initially
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        registerButton.addActionListener(this);
        add(registerButton, gbc);

        backToLoginButton = new JButton("Back to Login");
        backToLoginButton.setVisible(false); // Hide initially
        gbc.gridy = 3;
        backToLoginButton.addActionListener(this);
        add(backToLoginButton, gbc);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            // Perform login logic
            String username = loginUsername.getText().trim();
            String password = new String(loginPassword.getPassword()).trim();
            if (!username.isEmpty() && !password.isEmpty()) {
                if (authenticateUser(username, password)) {
                    JOptionPane.showMessageDialog(this, "Login successful!");
                    // Show teacher window if the logged-in user is a teacher
                    if (loggedInUserRole.equals("teacher")) {
                        new TeacherWindow(loggedInUserId, this); // Open the teacher window
                        setVisible(false); // Hide the login/register page
                    } else {
                        new SearchPage(loggedInUserId); // Open the new course search page for students, passing the
                                                        // student ID
                        dispose(); // Close the current login/register page
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please enter username and password!");
            }
        } else if (e.getSource() == registerButton) {
            // Perform registration logic
            String username = registerUsername.getText().trim();
            String password = new String(registerPassword.getPassword()).trim();
            if (!username.isEmpty() && !password.isEmpty()) {
                if (registerUser(username, password)) {
                    JOptionPane.showMessageDialog(this, "Registration successful!");
                    // Redirect back to login page
                    showLoginPage();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to register user!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please enter username and password!");
            }
        } else if (e.getSource() == backToLoginButton) {
            // Redirect back to login page
            showLoginPage();
        } else if (e.getSource() == registerRedirectButton) {
            // Show registration page
            showRegistrationPage();
        }
    }

    public void showLoginPage() {
        loginUsernameLabel.setVisible(true);
        loginUsername.setVisible(true);
        loginPasswordLabel.setVisible(true);
        loginPassword.setVisible(true);
        loginButton.setVisible(true);
        registerRedirectButton.setVisible(true);

        registerUsernameLabel.setVisible(false);
        registerUsername.setVisible(false);
        registerPasswordLabel.setVisible(false);
        registerPassword.setVisible(false);
        registerButton.setVisible(false);
        backToLoginButton.setVisible(false);

        setTitle("Login Page");
    }

    private void showRegistrationPage() {
        loginUsernameLabel.setVisible(false);
        loginUsername.setVisible(false);
        loginPasswordLabel.setVisible(false);
        loginPassword.setVisible(false);
        loginButton.setVisible(false);
        registerRedirectButton.setVisible(false);

        registerUsernameLabel.setVisible(true);
        registerUsername.setVisible(true);
        registerPasswordLabel.setVisible(true);
        registerPassword.setVisible(true);
        registerButton.setVisible(true);
        backToLoginButton.setVisible(true);

        setTitle("Registration Page");
    }

    private boolean authenticateUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement statement = conn
                        .prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                loggedInUserId = resultSet.getInt("id");
                loggedInUserRole = resultSet.getString("role");
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private boolean registerUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement statement = conn
                        .prepareStatement("INSERT INTO users (username, password, role) VALUES (?, ?, 'student')")) {
            statement.setString(1, username);
            statement.setString(2, password);
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginRegisterApp::new);
    }
}

class TeacherWindow extends JFrame implements ActionListener {
    private JTextField gradeStudentId;
    private JTextField gradeCourseCode;
    private JTextField gradeValue;
    private JButton gradeSubmitButton;
    private JButton backToLoginButton;

    private JLabel gradeStudentIdLabel;
    private JLabel gradeCourseCodeLabel;
    private JLabel gradeValueLabel;

    private int teacherId;
    private LoginRegisterApp loginRegisterApp;

    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/student_management?useSSL=false";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1697";

    public TeacherWindow(int teacherId, LoginRegisterApp loginRegisterApp) {
        super("Teacher Window");
        this.teacherId = teacherId;
        this.loginRegisterApp = loginRegisterApp;

        setSize(400, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        setLocationRelativeTo(null); // Center the window on the screen

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gradeStudentIdLabel = new JLabel("Student ID:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(gradeStudentIdLabel, gbc);

        gradeStudentId = new JTextField(15);
        gbc.gridx = 1;
        add(gradeStudentId, gbc);

        gradeCourseCodeLabel = new JLabel("Course Code:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(gradeCourseCodeLabel, gbc);

        gradeCourseCode = new JTextField(15);
        gbc.gridx = 1;
        add(gradeCourseCode, gbc);

        gradeValueLabel = new JLabel("Grade:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(gradeValueLabel, gbc);

        gradeValue = new JTextField(15);
        gbc.gridx = 1;
        add(gradeValue, gbc);

        gradeSubmitButton = new JButton("Submit Grade");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gradeSubmitButton.addActionListener(this);
        add(gradeSubmitButton, gbc);

        backToLoginButton = new JButton("Logout");
        gbc.gridy = 4;
        backToLoginButton.addActionListener(this);
        add(backToLoginButton, gbc);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == gradeSubmitButton) {
            int studentId = Integer.parseInt(gradeStudentId.getText().trim());
            String courseCode = gradeCourseCode.getText().trim();
            String grade = gradeValue.getText().trim();

            if (addGrade(studentId, courseCode, grade, teacherId)) {
                JOptionPane.showMessageDialog(this, "Grade added successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add grade!");
            }
        } else if (e.getSource() == backToLoginButton) {
            dispose(); // Close the teacher window
            loginRegisterApp.setVisible(true); // Show the login/register page
            loginRegisterApp.showLoginPage(); // Show the login page components
        }
    }

    private boolean addGrade(int studentId, String courseCode, String grade, int teacherId) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement statement = conn.prepareStatement(
                        "INSERT INTO student_results (student_id, course_code, grade, teacher_id) VALUES (?, ?, ?, ?)")) {
            statement.setInt(1, studentId);
            statement.setString(2, courseCode);
            statement.setString(3, grade);
            statement.setInt(4, teacherId);
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}




