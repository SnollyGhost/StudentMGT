import javax.swing.*;
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
    private static final String DB_URL = "jdbc:mysql://localhost/student_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    public LoginRegisterApp() {
        super("Login Page");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        // Login page components
        loginUsernameLabel = new JLabel("Username:");
        loginUsernameLabel.setBounds(20, 20, 80, 25);
        add(loginUsernameLabel);

        loginUsername = new JTextField();
        loginUsername.setBounds(110, 20, 150, 25);
        add(loginUsername);

        loginPasswordLabel = new JLabel("Password:");
        loginPasswordLabel.setBounds(20, 50, 80, 25);
        add(loginPasswordLabel);

        loginPassword = new JPasswordField();
        loginPassword.setBounds(110, 50, 150, 25);
        add(loginPassword);

        loginButton = new JButton("Login");
        loginButton.setBounds(20, 80, 90, 25);
        loginButton.addActionListener(this);
        add(loginButton);

        registerRedirectButton = new JButton("Register");
        registerRedirectButton.setBounds(130, 80, 90, 25);
        registerRedirectButton.addActionListener(this);
        add(registerRedirectButton);

        // Registration page components
        registerUsernameLabel = new JLabel("Username:");
        registerUsernameLabel.setBounds(20, 20, 80, 25);
        registerUsernameLabel.setVisible(false); // Hide initially
        add(registerUsernameLabel);

        registerUsername = new JTextField();
        registerUsername.setBounds(110, 20, 150, 25);
        registerUsername.setVisible(false); // Hide initially
        add(registerUsername);

        registerPasswordLabel = new JLabel("Password:");
        registerPasswordLabel.setBounds(20, 50, 80, 25);
        registerPasswordLabel.setVisible(false); // Hide initially
        add(registerPasswordLabel);

        registerPassword = new JPasswordField();
        registerPassword.setBounds(110, 50, 150, 25);
        registerPassword.setVisible(false); // Hide initially
        add(registerPassword);

        registerButton = new JButton("Register");
        registerButton.setBounds(20, 80, 90, 25);
        registerButton.addActionListener(this);
        registerButton.setVisible(false); // Hide initially
        add(registerButton);

        backToLoginButton = new JButton("Back to Login");
        backToLoginButton.setBounds(130, 80, 130, 25);
        backToLoginButton.addActionListener(this);
        backToLoginButton.setVisible(false); // Hide initially
        add(backToLoginButton);

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
                    // Redirect to course search page
                    new SearchPage(); // Open the new course search page
                    dispose(); // Close the current login/register page
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
                    setContentPane(getLoginPanel());
                    setTitle("LOGIN PAGE"); // Set the title to "LOGIN PAGE"
                    revalidate();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to register user!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please enter username and password!");
            }
        } else if (e.getSource() == backToLoginButton) {
            // Redirect back to login page
            setContentPane(getLoginPanel());
            setTitle("LOGIN PAGE"); // Set the title to "LOGIN PAGE"
            revalidate();
        } else if (e.getSource() == registerRedirectButton) {
            // Show registration page
            setContentPane(getRegistrationPanel());
            setTitle("Registration Page"); // Set the title to "Registration Page"
            registerUsernameLabel.setVisible(true); // Show the registration components
            registerUsername.setVisible(true);
            registerPasswordLabel.setVisible(true);
            registerPassword.setVisible(true);
            registerButton.setVisible(true);
            backToLoginButton.setVisible(true);
            revalidate();
        }
    }

    private JPanel getLoginPanel() {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(null);
        loginPanel.add(loginUsernameLabel);
        loginPanel.add(loginUsername);
        loginPanel.add(loginPasswordLabel);
        loginPanel.add(loginPassword);
        loginPanel.add(loginButton);
        loginPanel.add(registerRedirectButton);
        return loginPanel;
    }

    private JPanel getRegistrationPanel() {
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(null);
        registerPanel.add(registerUsernameLabel);
        registerPanel.add(registerUsername);
        registerPanel.add(registerPasswordLabel);
        registerPanel.add(registerPassword);
        registerPanel.add(registerButton);
        registerPanel.add(backToLoginButton);
        return registerPanel;
    }

    private boolean authenticateUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement statement = conn
                        .prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private boolean registerUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement statement = conn
                        .prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")) {
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
