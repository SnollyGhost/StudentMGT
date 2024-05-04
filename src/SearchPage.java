import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SearchPage extends JFrame implements ActionListener {
    // Components
    private JTextField courseCodeField;
    private JButton searchButton;
    private JPanel resultPanel;
    private JLabel resultLabel;

    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost/student_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    public SearchPage() {
        super("Search Page");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel();
        courseCodeField = new JTextField(10);
        searchPanel.add(new JLabel("Course Code:"));
        searchPanel.add(courseCodeField);
        searchButton = new JButton("Search");
        searchButton.addActionListener(this);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        resultPanel = new JPanel();
        resultLabel = new JLabel();
        resultPanel.add(resultLabel);
        add(resultPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            String courseCode = courseCodeField.getText().trim();
            if (!courseCode.isEmpty()) {
                // Search logic
                String studentInfo = searchStudentByCourse(courseCode);
                if (studentInfo != null) {
                    resultLabel.setText(studentInfo);
                } else {
                    resultLabel.setText("No student found for the given course code.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a course code.");
            }
        }
    }

    // Database querying logic to retrieve student information based on the course code
    private String searchStudentByCourse(String courseCode) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM student_results WHERE course_code = ?")) {
            statement.setString(1, courseCode);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // Student found, construct and return student information
                int studentId = resultSet.getInt("student_id");
                String grade = resultSet.getString("grade");
                return "Student ID: " + studentId + ", Grade: " + grade;
            } else {
                // No student found for the given course code
                return null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SearchPage::new);
    }
}
