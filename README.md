# The student Management Project


## This platform allows Instructors to post Grades and allows students to retrieve their grades based on their respective ID numbers

- It implements the following techniques. 
### 1. **Java Swing for GUI:**
   - **JFrame:** Used to create the main window.
   - **JLabel, JTextField, JPasswordField, JButton:** Swing components used to create labels, text fields, password fields, and buttons.
   - **GridBagLayout and GridBagConstraints:** Used for flexible and complex layouts of components within the frame.
### 2. **Event Handling:**
   - **ActionListener Interface:** Implemented by the `LoginRegisterApp` and `TeacherWindow` classes to handle button click events.
   - **actionPerformed Method:** Contains logic to handle events when buttons are clicked (e.g., login, register, submit grade).
### 3. **Database Interaction with JDBC:**
   - **Connection, PreparedStatement, ResultSet:** Used to connect to a MySQL database, execute SQL queries, and process the results.
   - **Database URL, User, Password:** Parameters for establishing a connection to the database.
### 4. **User Authentication and Registration:**
   - **Login Logic:** Validates the username and password against the database records.
   - **Registration Logic:** Inserts new user details into the database.
   - **Role-Based Redirection:** Redirects users based on their role (teacher or student) after successful login.

### 5. **Visibility Management:**
   - **setVisible Method:** Used to show or hide components dynamically based on the current view (login or registration).
   - **showLoginPage and showRegistrationPage Methods:** Toggle the visibility of components to switch between login and registration views.

### 6. **Encapsulation and Modularity:**
   - **Separate Methods for Database Operations:** Methods like `authenticateUser` and `registerUser` encapsulate the database interaction logic, making the code modular and easier to maintain.
   - **Separate Window for Teachers:** The `TeacherWindow` class encapsulates the logic and UI for the teacher's functionalities.

### 7. **Error Handling:**
   - **Exception Handling with try-catch:** Handles SQL exceptions and prints stack traces for debugging.

### 8. **Message Dialogs:**
   - **JOptionPane:** Used to display messages to the user, such as success or error messages during login, registration, and grade submission.

### Breakdown:

#### Login Logic:
```java
if (e.getSource() == loginButton) {
    // Perform login logic
    String username = loginUsername.getText().trim();
    String password = new String(loginPassword.getPassword()).trim();
    if (!username.isEmpty() && !password.isEmpty()) {
        if (authenticateUser(username, password)) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            // Redirect based on user role
            if (loggedInUserRole.equals("teacher")) {
                new TeacherWindow(loggedInUserId, this);
                setVisible(false);
            } else {
                new SearchPage(loggedInUserId);
                dispose();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password!");
        }
    } else {
        JOptionPane.showMessageDialog(this, "Please enter username and password!");
    }
}
```

#### Database Authentication:
```java
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
```

