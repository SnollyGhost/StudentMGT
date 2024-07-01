// import javax.swing.*;
// import java.awt.*;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
// import java.sql.*;

// public class SearchPage extends JFrame implements ActionListener {
//     // Components
//     private JTextField courseCodeField;
//     private JButton searchButton;
//     private JButton backButton; // Add back button
//     private JPanel resultPanel;
//     private JLabel resultLabel;

//     // Database connection parameters
//     private static final String DB_URL = "jdbc:mysql://localhost:3306/student_management?useSSL=false";
//     private static final String DB_USER = "root";
//     private static final String DB_PASSWORD = "1697";

//     // Logged-in student ID
//     private int studentId;

//     public SearchPage(int studentId) {
//         super("Search Page");
//         this.studentId = studentId;
//         setSize(400, 300);
//         setDefaultCloseOperation(EXIT_ON_CLOSE);
//         setLayout(new GridBagLayout());
//         setLocationRelativeTo(null); // Center the window on the screen

//         GridBagConstraints gbc = new GridBagConstraints();
//         gbc.insets = new Insets(10, 10, 10, 10);

//         // Search panel components
//         gbc.gridx = 0;
//         gbc.gridy = 0;
//         gbc.anchor = GridBagConstraints.EAST;
//         add(new JLabel("Course Code:"), gbc);

//         courseCodeField = new JTextField(10);
//         gbc.gridx = 1;
//         gbc.anchor = GridBagConstraints.WEST;
//         add(courseCodeField, gbc);

//         searchButton = new JButton("Search");
//         searchButton.addActionListener(this);
//         gbc.gridx = 2;
//         gbc.anchor = GridBagConstraints.CENTER;
//         add(searchButton, gbc);

//         // Result panel components
//         resultPanel = new JPanel();
//         resultLabel = new JLabel();
//         resultPanel.add(resultLabel);
//         gbc.gridx = 0;
//         gbc.gridy = 1;
//         gbc.gridwidth = 3;
//         gbc.anchor = GridBagConstraints.CENTER;
//         add(resultPanel, gbc);

//         // Back button setup
//         backButton = new JButton("Logout");
//         backButton.addActionListener(this);
//         JPanel buttonPanel = new JPanel(new GridBagLayout());
//         GridBagConstraints buttonGbc = new GridBagConstraints();
//         buttonGbc.gridx = 0;
//         buttonGbc.gridy = 0;
//         buttonGbc.anchor = GridBagConstraints.CENTER;
//         buttonPanel.add(backButton, buttonGbc);

//         gbc.gridy = 2;
//         gbc.gridwidth = 3;
//         add(buttonPanel, gbc);

//         setVisible(true);
//     }

//     @Override
//     public void actionPerformed(ActionEvent e) {
//         if (e.getSource() == searchButton) {
//             String courseCode = courseCodeField.getText().trim();
//             if (!courseCode.isEmpty()) {
//                 String studentInfo = searchStudentByCourse(courseCode);
//                 if (studentInfo != null) {
//                     resultLabel.setText(studentInfo);
//                 } else {
//                     resultLabel.setText("No grade found for the given course code.");
//                 }
//             } else {
//                 JOptionPane.showMessageDialog(this, "Please enter a course code.");
//             }
//         } else if (e.getSource() == backButton) {
//             // Close current search page and open login/register page
//             dispose(); // Close the current window
//             new LoginRegisterApp(); // Open login/register page again
//         }
//     }

//     // Database querying logic to retrieve student information based on the course
//     // code
//     private String searchStudentByCourse(String courseCode) {
//         try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
//                 PreparedStatement statement = conn.prepareStatement(
//                         "SELECT * FROM student_results WHERE course_code = ? AND student_id = ?")) {
//             statement.setString(1, courseCode);
//             statement.setInt(2, studentId);
//             ResultSet resultSet = statement.executeQuery();
//             if (resultSet.next()) {
//                 String grade = resultSet.getString("grade");
//                 return "Course Code: " + courseCode + ", Grade: " + grade;
//             } else {
//                 return null;
//             }
//         } catch (SQLException ex) {
//             ex.printStackTrace();
//             return null;
//         }
//     }

//     public static void main(String[] args) {
//         SwingUtilities.invokeLater(() -> new LoginRegisterApp()); // Start with the login/register app
//     }
// }


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SearchPage extends JFrame implements ActionListener {
    // Components
    private JTextField courseCodeField;
    private JButton searchButton;
    private JButton backButton; // Add back button
    private JPanel resultPanel;
    private JLabel resultLabel;

    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/student_management?useSSL=false";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1697";

    // Logged-in student ID
    private int studentId;

    public SearchPage(int studentId) {
        super("Search Page");
        this.studentId = studentId;
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window on the screen

        // Create background panel with custom image
        BackgroundPanel backgroundPanel = new BackgroundPanel("C:\\Users\\R4D3ON\\Documents\\BOOK\\4th Year II\\AOOP\\StudentMGT\\asset\\academic.jpg");
        backgroundPanel.setLayout(new GridBagLayout());
        setContentPane(backgroundPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Search panel components
        JLabel courseCodeLabel = new JLabel("Course Code:");
        courseCodeLabel.setForeground(Color.WHITE); // Set text color for contrast
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        backgroundPanel.add(courseCodeLabel, gbc);

        courseCodeField = new JTextField(10);
        courseCodeField.setOpaque(false); // Make the field background semi-transparent
        courseCodeField.setForeground(Color.WHITE); // Set text color for contrast
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        backgroundPanel.add(courseCodeField, gbc);

        searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        searchButton.setOpaque(false);
        searchButton.setForeground(Color.WHITE); // Set text color for contrast
        searchButton.setContentAreaFilled(false); // Make button background semi-transparent
        gbc.gridx = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        backgroundPanel.add(searchButton, gbc);

        // Result panel components
        resultPanel = new JPanel();
        resultPanel.setOpaque(false); // Make the panel transparent
        resultLabel = new JLabel();
        resultLabel.setForeground(Color.WHITE); // Set text color for contrast
        resultPanel.add(resultLabel);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        backgroundPanel.add(resultPanel, gbc);

        // Back button setup
        backButton = new JButton("Logout");
        backButton.addActionListener(this);
        backButton.setOpaque(false);
        backButton.setForeground(Color.WHITE); // Set text color for contrast
        backButton.setContentAreaFilled(false); // Make button background semi-transparent
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false); // Make the panel transparent
        GridBagConstraints buttonGbc = new GridBagConstraints();
        buttonGbc.gridx = 0;
        buttonGbc.gridy = 0;
        buttonGbc.anchor = GridBagConstraints.CENTER;
        buttonPanel.add(backButton, buttonGbc);

        gbc.gridy = 2;
        gbc.gridwidth = 3;
        backgroundPanel.add(buttonPanel, gbc);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            String courseCode = courseCodeField.getText().trim();
            if (!courseCode.isEmpty()) {
                String studentInfo = searchStudentByCourse(courseCode);
                if (studentInfo != null) {
                    resultLabel.setText(studentInfo);
                } else {
                    resultLabel.setText("No grade found for the given course code.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a course code.");
            }
        } else if (e.getSource() == backButton) {
            // Close current search page and open login/register page
            dispose(); // Close the current window
            new LoginRegisterApp(); // Open login/register page again
        }
    }

    // Database querying logic to retrieve student information based on the course
    // code
    private String searchStudentByCourse(String courseCode) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement statement = conn.prepareStatement(
                        "SELECT * FROM student_results WHERE course_code = ? AND student_id = ?")) {
            statement.setString(1, courseCode);
            statement.setInt(2, studentId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String grade = resultSet.getString("grade");
                return "Course Code: " + courseCode + ", Grade: " + grade;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginRegisterApp()); // Start with the login/register app
    }
}

// Custom JPanel to draw the background image
class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String imagePath) {
        try {
            backgroundImage = new ImageIcon(imagePath).getImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

