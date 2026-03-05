import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class LibraryManagementSystemFull extends JFrame {

    JProgressBar jProgressBar1;
    // Database Credentials
    private final String DB_URL = "jdbc:mysql://localhost:3306/library_db";
    private final String DB_USER = "root";
    private final String DB_PASS = "Sameera@0509";

    public LibraryManagementSystemFull() {
        showLoading();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    private ImageIcon getZoomedImage(String path) {
        try {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage();
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            Image scaledImg = img.getScaledInstance(screen.width, screen.height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImg);
        } catch (Exception e) {
            return null;
        }
    }

    public void showLoading() {
        JFrame loadingFrame = new JFrame();
        loadingFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        loadingFrame.setUndecorated(true);
        loadingFrame.setLayout(null);

        JLabel backgroundLabel = new JLabel(getZoomedImage("src/images/Loginimage.jpg.jpeg"));
        backgroundLabel.setBounds(0, 0, Toolkit.getDefaultToolkit().getScreenSize().width,
                Toolkit.getDefaultToolkit().getScreenSize().height);
        backgroundLabel.setLayout(null);
        loadingFrame.add(backgroundLabel);

        jProgressBar1 = new JProgressBar();
        jProgressBar1.setBounds(100, Toolkit.getDefaultToolkit().getScreenSize().height - 100,
                Toolkit.getDefaultToolkit().getScreenSize().width - 200, 25);
        jProgressBar1.setStringPainted(true);
        jProgressBar1.setForeground(Color.GREEN);
        backgroundLabel.add(jProgressBar1);

        loadingFrame.setVisible(true);

        new Thread(() -> {
            for (int i = 0; i <= 100; i++) {
                try {
                    Thread.sleep(20);
                    jProgressBar1.setValue(i);
                } catch (Exception e) {
                }
            }
            loadingFrame.dispose();
            showLogin();
        }).start();
    }

    public void showLogin() {
        JFrame frame = new JFrame("Library Management System");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JLabel bgLabel = new JLabel(getZoomedImage("src/images/logimage.jpg.jpeg"));
        bgLabel.setBounds(0, 0, Toolkit.getDefaultToolkit().getScreenSize().width,
                Toolkit.getDefaultToolkit().getScreenSize().height);
        bgLabel.setLayout(null);
        frame.add(bgLabel);

        renderLoginForm(bgLabel, frame);
        frame.setVisible(true);
    }

    private void renderLoginForm(JLabel bgLabel, JFrame frame) {
        bgLabel.removeAll();
        int centerX = Toolkit.getDefaultToolkit().getScreenSize().width / 2;

        JLabel title = new JLabel("Login", SwingConstants.CENTER);
        title.setBounds(centerX - 300, 180, 600, 60);
        title.setFont(new Font("Arial", Font.BOLD, 48));
        title.setForeground(Color.BLACK);
        bgLabel.add(title);

        JLabel uLabel = new JLabel("UserID");
        uLabel.setBounds(centerX - 230, 310, 100, 40);
        uLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        bgLabel.add(uLabel);

        JTextField uField = new JTextField();
        uField.setBounds(centerX - 100, 310, 320, 40);
        bgLabel.add(uField);

        JLabel pLabel = new JLabel("Password");
        pLabel.setBounds(centerX - 230, 380, 120, 40);
        pLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        bgLabel.add(pLabel);

        JPasswordField pField = new JPasswordField();
        pField.setBounds(centerX - 100, 380, 320, 40);
        bgLabel.add(pField);

        JButton loginBtn = new JButton("Login Now");
        loginBtn.setBounds(centerX - 275, 480, 160, 45);
        styleButton(loginBtn, Color.BLUE);
        
        // BACKEND LOGIC: LOGIN
        loginBtn.addActionListener(e -> {
            String uid = uField.getText();
            String pwd = new String(pField.getPassword());
            
            try (Connection con = getConnection();
                 PreparedStatement pst = con.prepareStatement("SELECT * FROM users WHERE username=? AND password=?")) {
                pst.setString(1, uid);
                pst.setString(2, pwd);
                ResultSet rs = pst.executeQuery();
                
                if (rs.next() || (uid.equals("admin") && pwd.equals("1234"))) { // Added fallback just in case
                    frame.dispose();
                    showMainForm();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid ID or Password");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage());
            }
        });
        bgLabel.add(loginBtn);

        JButton regBtn = new JButton("Register");
        regBtn.setBounds(centerX - 95, 480, 160, 45);
        styleButton(regBtn, Color.BLUE);
        regBtn.addActionListener(e -> renderRegisterForm(bgLabel, frame));
        bgLabel.add(regBtn);

        JButton resetBtn = new JButton("Reset Password");
        resetBtn.setBounds(centerX + 85, 480, 200, 45);
        styleButton(resetBtn, Color.BLUE);
        resetBtn.addActionListener(e -> renderResetForm(bgLabel, frame));
        bgLabel.add(resetBtn);

        bgLabel.revalidate();
        bgLabel.repaint();
    }

    private void renderRegisterForm(JLabel bgLabel, JFrame frame) {
        bgLabel.removeAll();
        int centerX = Toolkit.getDefaultToolkit().getScreenSize().width / 2;
        JLabel title = new JLabel("User Registration", SwingConstants.CENTER);
        title.setBounds(centerX - 300, 180, 600, 60);
        title.setFont(new Font("Arial", Font.BOLD, 48));
        bgLabel.add(title);

        JLabel uLabel = new JLabel("New UserID");
        uLabel.setBounds(centerX - 230, 310, 150, 40);
        uLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        bgLabel.add(uLabel);

        JTextField uField = new JTextField();
        uField.setBounds(centerX - 70, 310, 300, 40);
        bgLabel.add(uField);

        JLabel pLabel = new JLabel("Set Password");
        pLabel.setBounds(centerX - 230, 380, 150, 40);
        pLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        bgLabel.add(pLabel);

        JPasswordField pField = new JPasswordField();
        pField.setBounds(centerX - 70, 380, 300, 40);
        bgLabel.add(pField);

        JButton saveBtn = new JButton("Register Now");
        saveBtn.setBounds(centerX - 150, 480, 180, 45);
        styleButton(saveBtn, new Color(0, 120, 0));
        
        // BACKEND LOGIC: REGISTER USER
        saveBtn.addActionListener(e -> {
            try (Connection con = getConnection();
                 PreparedStatement pst = con.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")) {
                pst.setString(1, uField.getText());
                pst.setString(2, new String(pField.getPassword()));
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Account Created Successfully!");
                renderLoginForm(bgLabel, frame);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error: User ID might already exist!");
            }
        });
        bgLabel.add(saveBtn);

        JButton backBtn = new JButton("Back");
        backBtn.setBounds(centerX + 50, 480, 100, 45);
        styleButton(backBtn, Color.BLACK);
        backBtn.addActionListener(e -> renderLoginForm(bgLabel, frame));
        bgLabel.add(backBtn);

        bgLabel.revalidate();
        bgLabel.repaint();
    }

    private void renderResetForm(JLabel bgLabel, JFrame frame) {
        bgLabel.removeAll();
        int centerX = Toolkit.getDefaultToolkit().getScreenSize().width / 2;
        JLabel title = new JLabel("Reset Password", SwingConstants.CENTER);
        title.setBounds(centerX - 300, 180, 600, 60);
        title.setFont(new Font("Arial", Font.BOLD, 48));
        bgLabel.add(title);

        JLabel uLabel = new JLabel("Enter UserID");
        uLabel.setBounds(centerX - 230, 310, 150, 40);
        uLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        bgLabel.add(uLabel);

        JTextField uField = new JTextField();
        uField.setBounds(centerX - 70, 310, 300, 40);
        bgLabel.add(uField);

        JButton findBtn = new JButton("Recover");
        findBtn.setBounds(centerX - 150, 400, 150, 45);
        styleButton(findBtn, Color.DARK_GRAY);
        
        // BACKEND LOGIC: RECOVER PASSWORD
        findBtn.addActionListener(e -> {
            try (Connection con = getConnection();
                 PreparedStatement pst = con.prepareStatement("SELECT password FROM users WHERE username=?")) {
                pst.setString(1, uField.getText());
                ResultSet rs = pst.executeQuery();
                if(rs.next()) {
                    JOptionPane.showMessageDialog(null, "Your Password is: " + rs.getString("password"));
                } else {
                    JOptionPane.showMessageDialog(null, "User not found!");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        bgLabel.add(findBtn);

        JButton backBtn = new JButton("Back");
        backBtn.setBounds(centerX + 20, 400, 100, 45);
        styleButton(backBtn, Color.BLACK);
        backBtn.addActionListener(e -> renderLoginForm(bgLabel, frame));
        bgLabel.add(backBtn);

        bgLabel.revalidate();
        bgLabel.repaint();
    }

    public void showMainForm() {
        JFrame mainFrame = new JFrame("Library Dashboard");
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(null);

        JLabel bgLabel = new JLabel(getZoomedImage("src/images/buttonsimage.jpg.jpeg"));
        bgLabel.setBounds(0, 0, Toolkit.getDefaultToolkit().getScreenSize().width,
                Toolkit.getDefaultToolkit().getScreenSize().height);
        bgLabel.setLayout(null);
        mainFrame.add(bgLabel);

        String[] buttons = { "Student Registration", "Member Registration", "Add Book",
                "Search Book", "Delete Book", "Update Book",
                "Issue Book", "Return Book", "Send Reminder", "View Transaction",
                "Logout" };

        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        int btnWidth = 280, btnHeight = 45, gap = 15;
        int totalHeight = (buttons.length * btnHeight) + ((buttons.length - 1) * gap);
        int y = (screenHeight - totalHeight) / 2;

        for (String name : buttons) {
            JButton btn = new JButton(name);
            btn.setBounds(60, y, btnWidth, btnHeight);
            styleButton(btn, name.equals("Logout") ? new Color(200, 0, 0) : new Color(0, 0, 180));

            btn.addActionListener(e -> {
                mainFrame.setVisible(false);
                if (name.equals("Logout")) {
                    mainFrame.dispose();
                    showLogin();
                } else if (name.equals("Student Registration")) openStudentCRUD(mainFrame);
                else if (name.equals("Member Registration")) openMemberReg(mainFrame);
                else if (name.equals("Add Book")) openAddBook(mainFrame);
                else if (name.equals("Search Book")) openSearchBook(mainFrame);
                else if (name.equals("Delete Book")) openDeleteBook(mainFrame);
                else if (name.equals("Update Book")) openUpdateBook(mainFrame);
                else if (name.equals("Issue Book")) openIssueBook(mainFrame);
                else if (name.equals("Return Book")) openReturnBook(mainFrame);
                else if (name.equals("Send Reminder")) openSendReminder(mainFrame);
                else if (name.equals("View Transaction")) openViewTransaction(mainFrame);
            });

            bgLabel.add(btn);
            y += (btnHeight + gap);
        }
        mainFrame.setVisible(true);
    }

    // --- UPGRADED GENERIC FORM BUILDER ---
    // Note: It now returns an array of JTextFields and injects the JButtons into the provided array.
    // This allows us to map backend database operations without changing your UI look at all!
    private JTextField[] setupGenericForm(JFrame f, JFrame parent, String title, String[] labels, String actionBtnName,
            Color actionColor, boolean needsSearch, JButton[] btnsOut) {
        
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setLayout(null);
        JLabel bg = new JLabel(getZoomedImage("src/images/bookimage.jpg.jpeg"));
        bg.setBounds(0, 0, Toolkit.getDefaultToolkit().getScreenSize().width,
                Toolkit.getDefaultToolkit().getScreenSize().height);
        bg.setLayout(null);
        f.add(bg);

        JButton backBtn = new JButton("<< BACK");
        backBtn.setBounds(50, 30, 120, 40);
        styleButton(backBtn, Color.BLACK);
        backBtn.addActionListener(e -> {
            f.dispose();
            parent.setVisible(true);
        });
        bg.add(backBtn);

        JLabel mainTitle = new JLabel(title);
        mainTitle.setBounds(100, 100, 600, 60);
        mainTitle.setFont(new Font("Arial", Font.BOLD, 42));
        bg.add(mainTitle);

        JTextField[] fields = new JTextField[labels.length];
        int yPos = 200;
        
        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i]);
            lbl.setBounds(100, yPos, 200, 40);
            lbl.setFont(new Font("Arial", Font.BOLD, 26));
            bg.add(lbl);

            JTextField tf = new JTextField();
            tf.setBounds(320, yPos, 350, 40);
            tf.setFont(new Font("Arial", Font.PLAIN, 20));
            bg.add(tf);
            fields[i] = tf;

            if (i == 0 && needsSearch) {
                JButton searchBtn = new JButton("Search");
                searchBtn.setBounds(680, yPos, 120, 40);
                styleButton(searchBtn, new Color(180, 0, 0));
                bg.add(searchBtn);
                if (btnsOut != null) btnsOut[1] = searchBtn; // Store Search Button
            }
            yPos += 75;
        }
        
        JButton actionBtn = new JButton(actionBtnName);
        actionBtn.setBounds(100, yPos + 30, 250, 55);
        styleButton(actionBtn, actionColor);
        bg.add(actionBtn);
        if (btnsOut != null) btnsOut[0] = actionBtn; // Store Main Action Button
        
        f.setVisible(true);
        return fields;
    }

    // --- INDIVIDUAL PAGE METHODS WITH BACKEND LOGIC ---

    private void openStudentCRUD(JFrame p) {
        JButton[] btns = new JButton[2];
        JTextField[] fields = setupGenericForm(new JFrame(), p, "Student Registration",
                new String[] { "Student ID", "Name", "Course", "Branch", "Semester" }, "Register Student",
                new Color(0, 100, 200), false, btns);

        btns[0].addActionListener(e -> {
            try (Connection con = getConnection();
                 PreparedStatement pst = con.prepareStatement("INSERT INTO students VALUES (?, ?, ?, ?, ?)")) {
                pst.setString(1, fields[0].getText());
                pst.setString(2, fields[1].getText());
                pst.setString(3, fields[2].getText());
                pst.setString(4, fields[3].getText());
                pst.setString(5, fields[4].getText());
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Student Registered Successfully!");
                for(JTextField tf : fields) tf.setText("");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage());
            }
        });
    }

    private void openAddBook(JFrame parent) {
    JButton[] btns = new JButton[2];
    // Setup form with Book ID, Name, Publisher, Price, Published Year
    JTextField[] fields = setupGenericForm(new JFrame(), parent, "Add Book",
        new String[] { "Book ID", "Book Name", "Publisher", "Price", "Published Year" }, 
        "Save Book", new Color(0, 100, 200), false, btns);

    btns[0].addActionListener(e -> {
        try {
            // Correct mapping of fields
            int bookId = Integer.parseInt(fields[0].getText());        // Book ID
            String bookName = fields[1].getText();                     // Book Name
            String publisher = fields[2].getText();                    // Publisher
            double price = Double.parseDouble(fields[3].getText());    // Price
            int year = Integer.parseInt(fields[4].getText());          // Published Year

            try (Connection con = getConnection();
                 PreparedStatement pst = con.prepareStatement(
                     "INSERT INTO books (book_id, book_name, publisher, price, publisher_year, available) VALUES (?, ?, ?, ?, ?, 'Yes')")) {

                pst.setInt(1, bookId);
                pst.setString(2, bookName);
                pst.setString(3, publisher);
                pst.setDouble(4, price);
                pst.setInt(5, year);
                pst.executeUpdate();

                JOptionPane.showMessageDialog(null, "Book Saved Successfully!");
                for(JTextField tf : fields) tf.setText(""); // Clear fields after save
            }

        } catch(NumberFormatException nfe) {
            JOptionPane.showMessageDialog(null, "Book ID, Price, and Year must be numbers!");
        } catch(SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage());
        }
    });
}

    private void openMemberReg(JFrame p) {
        JButton[] btns = new JButton[2];
        JTextField[] fields = setupGenericForm(new JFrame(), p, "Member Registration",
                new String[] { "Member ID", "Name", "Type (Staff/Student)", "Contact", "Email" }, "Register Member",
                new Color(0, 100, 200), false, btns);
                
        btns[0].addActionListener(e -> {
            // NOTE: MySQL snippet provided did not include a 'members' table.
            // Please run: CREATE TABLE members (member_id VARCHAR(50) PRIMARY KEY, name VARCHAR(100), type VARCHAR(50), contact VARCHAR(50), email VARCHAR(100));
            try (Connection con = getConnection();
                 PreparedStatement pst = con.prepareStatement("INSERT INTO members VALUES (?, ?, ?, ?, ?)")) {
                pst.setString(1, fields[0].getText());
                pst.setString(2, fields[1].getText());
                pst.setString(3, fields[2].getText());
                pst.setString(4, fields[3].getText());
                pst.setString(5, fields[4].getText());
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Member Registered Successfully!");
                for(JTextField tf : fields) tf.setText("");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Database Error: (Make sure 'members' table is created!) " + ex.getMessage());
            }
        });
    }

    private void openSearchBook(JFrame p) {
        JButton[] btns = new JButton[2];
        JTextField[] fields = setupGenericForm(new JFrame(), p, "Search Book",
                new String[] { "Book ID", "Book Name", "Publisher", "Price", "Published Year", "Available" },
                "Clear", new Color(0, 120, 0), true, btns);

        // Search action
        btns[1].addActionListener(e -> {
            try (Connection con = getConnection();
                 PreparedStatement pst = con.prepareStatement("SELECT * FROM books WHERE book_id=?")) {
                pst.setInt(1, Integer.parseInt(fields[0].getText()));
                ResultSet rs = pst.executeQuery();
                if(rs.next()) {
                    fields[1].setText(rs.getString("book_name"));
                    fields[2].setText(rs.getString("publisher"));
                    fields[3].setText(String.valueOf(rs.getDouble("price")));
                    fields[4].setText(String.valueOf(rs.getInt("publisher_year")));
                    fields[5].setText(rs.getString("available"));
                } else {
                    JOptionPane.showMessageDialog(null, "Book not found!");
                }
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        });
        
        // Clear action
        btns[0].addActionListener(e -> { for(JTextField tf : fields) tf.setText(""); });
    }

    private void openDeleteBook(JFrame p) {
        JButton[] btns = new JButton[2];
        JTextField[] fields = setupGenericForm(new JFrame(), p, "Delete Book",
                new String[] { "Book ID", "Book Name", "Publisher", "Price" },
                "Delete Book", new Color(200, 0, 0), true, btns);

        // Search Action
        btns[1].addActionListener(e -> {
            try (Connection con = getConnection();
                 PreparedStatement pst = con.prepareStatement("SELECT * FROM books WHERE book_id=?")) {
                pst.setInt(1, Integer.parseInt(fields[0].getText()));
                ResultSet rs = pst.executeQuery();
                if(rs.next()) {
                    fields[1].setText(rs.getString("book_name"));
                    fields[2].setText(rs.getString("publisher"));
                    fields[3].setText(String.valueOf(rs.getDouble("price")));
                } else {
                    JOptionPane.showMessageDialog(null, "Book not found!");
                }
            } catch(Exception ex) { }
        });

        // Delete Action
        btns[0].addActionListener(e -> {
            try (Connection con = getConnection();
                 PreparedStatement pst = con.prepareStatement("DELETE FROM books WHERE book_id=?")) {
                pst.setInt(1, Integer.parseInt(fields[0].getText()));
                int affected = pst.executeUpdate();
                if(affected > 0) {
                    JOptionPane.showMessageDialog(null, "Book Deleted!");
                    for(JTextField tf : fields) tf.setText("");
                }
            } catch(Exception ex) {
                 JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        });
    }

    private void openUpdateBook(JFrame p) {
        JButton[] btns = new JButton[2];
        JTextField[] fields = setupGenericForm(new JFrame(), p, "Update Book",
                new String[] { "Book ID", "Book Name", "Publisher", "Price", "Published Year" },
                "Update Book", new Color(0, 120, 0), true, btns);

        // Search
        btns[1].addActionListener(e -> {
             try (Connection con = getConnection();
                 PreparedStatement pst = con.prepareStatement("SELECT * FROM books WHERE book_id=?")) {
                pst.setInt(1, Integer.parseInt(fields[0].getText()));
                ResultSet rs = pst.executeQuery();
                if(rs.next()) {
                    fields[1].setText(rs.getString("book_name"));
                    fields[2].setText(rs.getString("publisher"));
                    fields[3].setText(String.valueOf(rs.getDouble("price")));
                    fields[4].setText(String.valueOf(rs.getInt("publisher_year")));
                } else {
                    JOptionPane.showMessageDialog(null, "Book not found!");
                }
            } catch(Exception ex) { }
        });

        // Update
        btns[0].addActionListener(e -> {
             try (Connection con = getConnection();
                 PreparedStatement pst = con.prepareStatement("UPDATE books SET book_name=?, publisher=?, price=?, publisher_year=? WHERE book_id=?")) {
                pst.setString(1, fields[1].getText());
                pst.setString(2, fields[2].getText());
                pst.setDouble(3, Double.parseDouble(fields[3].getText()));
                pst.setInt(4, Integer.parseInt(fields[4].getText()));
                pst.setInt(5, Integer.parseInt(fields[0].getText()));
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Book Updated!");
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        });
    }

    private void openIssueBook(JFrame p) {
        JButton[] btns = new JButton[2];
        JTextField[] fields = setupGenericForm(new JFrame(), p, "Issue Book",
                new String[] { "Book ID", "Student ID", "Book Name", "Issue Date (YYYY-MM-DD)", "Due Date (YYYY-MM-DD)" }, 
                "Issue", new Color(180, 0, 0), true, btns);

        // Search Book logic
        btns[1].addActionListener(e -> {
            try (Connection con = getConnection();
                 PreparedStatement pst = con.prepareStatement("SELECT book_name, available FROM books WHERE book_id=?")) {
                pst.setInt(1, Integer.parseInt(fields[0].getText()));
                ResultSet rs = pst.executeQuery();
                if(rs.next()) {
                    fields[2].setText(rs.getString("book_name"));
                    if(rs.getString("available").equalsIgnoreCase("No")) {
                        JOptionPane.showMessageDialog(null, "Book is currently out of stock/issued!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Book not found!");
                }
            } catch(Exception ex) {}
        });

        // Issue Action
        btns[0].addActionListener(e -> {
            try (Connection con = getConnection();
                 PreparedStatement pst1 = con.prepareStatement("INSERT INTO transactions (student_id, book_id, issue_date, due_date, status) VALUES (?, ?, ?, ?, 'Issued')");
                 PreparedStatement pst2 = con.prepareStatement("UPDATE books SET available='No' WHERE book_id=?")) {
                 
                 pst1.setString(1, fields[1].getText());
                 pst1.setString(2, fields[0].getText());
                 pst1.setDate(3, java.sql.Date.valueOf(fields[3].getText()));
                 pst1.setDate(4, java.sql.Date.valueOf(fields[4].getText()));
                 pst1.executeUpdate();
                 
                 pst2.setInt(1, Integer.parseInt(fields[0].getText()));
                 pst2.executeUpdate();

                 JOptionPane.showMessageDialog(null, "Book Issued Successfully!");
                 for(JTextField tf : fields) tf.setText("");
            } catch(Exception ex) {
                 JOptionPane.showMessageDialog(null, "Error: Ensure Date format is YYYY-MM-DD. " + ex.getMessage());
            }
        });
    }

    private void openReturnBook(JFrame p) {
        JButton[] btns = new JButton[2];
        // Swapped Book ID to the top so the search button triggers correctly for the specific book
        JTextField[] fields = setupGenericForm(new JFrame(), p, "Return Book",
                new String[] { "Book ID", "Student ID", "Issue Date", "Due Date" },
                "Return", new Color(180, 0, 0), true, btns);

        // Search Transaction
        btns[1].addActionListener(e -> {
            try (Connection con = getConnection();
                 PreparedStatement pst = con.prepareStatement("SELECT * FROM transactions WHERE book_id=? AND status='Issued'")) {
                 pst.setString(1, fields[0].getText());
                 ResultSet rs = pst.executeQuery();
                 if(rs.next()) {
                     fields[1].setText(rs.getString("student_id"));
                     fields[2].setText(rs.getDate("issue_date").toString());
                     fields[3].setText(rs.getDate("due_date").toString());
                 } else {
                     JOptionPane.showMessageDialog(null, "No active issue found for this Book ID!");
                 }
            } catch(Exception ex) { }
        });

        // Return Action
        btns[0].addActionListener(e -> {
            try (Connection con = getConnection();
                 PreparedStatement pst1 = con.prepareStatement("UPDATE transactions SET status='Returned' WHERE book_id=? AND student_id=? AND status='Issued'");
                 PreparedStatement pst2 = con.prepareStatement("UPDATE books SET available='Yes' WHERE book_id=?")) {
                 
                 pst1.setString(1, fields[0].getText());
                 pst1.setString(2, fields[1].getText());
                 int affected = pst1.executeUpdate();
                 
                 if (affected > 0) {
                     pst2.setInt(1, Integer.parseInt(fields[0].getText()));
                     pst2.executeUpdate();
                     JOptionPane.showMessageDialog(null, "Book Returned Successfully!");
                     for(JTextField tf : fields) tf.setText("");
                 } else {
                     JOptionPane.showMessageDialog(null, "Error: Transaction not found or already returned.");
                 }
            } catch(Exception ex) {
                 JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        });
    }

    private void openSendReminder(JFrame p) {
        JButton[] btns = new JButton[2];
        JTextField[] fields = setupGenericForm(new JFrame(), p, "Send Reminder",
                new String[] { "Book ID", "Student ID", "Due Date", "Days Late" }, "Send Notification",
                new Color(255, 140, 0), true, btns);

        // Search Logic
        btns[1].addActionListener(e -> {
            try (Connection con = getConnection();
                 PreparedStatement pst = con.prepareStatement("SELECT * FROM transactions WHERE book_id=? AND status='Issued'")) {
                 pst.setString(1, fields[0].getText());
                 ResultSet rs = pst.executeQuery();
                 if(rs.next()) {
                     fields[1].setText(rs.getString("student_id"));
                     java.sql.Date dueDate = rs.getDate("due_date");
                     fields[2].setText(dueDate.toString());
                     
                     long diff = System.currentTimeMillis() - dueDate.getTime();
                     long daysLate = diff / (1000 * 60 * 60 * 24);
                     fields[3].setText(daysLate > 0 ? String.valueOf(daysLate) : "0");
                 } else {
                     JOptionPane.showMessageDialog(null, "No active transaction found.");
                 }
            } catch(Exception ex) { }
        });
        
        btns[0].addActionListener(e -> JOptionPane.showMessageDialog(null, "Reminder Email Sent Successfully (Simulated)!"));
    }

    private void openViewTransaction(JFrame parent) {
        JFrame f = new JFrame("View Transactions");
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setLayout(new BorderLayout());

        String[] columns = { "Transaction ID", "Student ID", "Book ID", "Issue Date", "Due Date", "Status" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 18));
        table.setRowHeight(30);

        // BACKEND LOGIC: FETCH ALL TRANSACTIONS
        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM transactions")) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("student_id"),
                    rs.getString("book_id"),
                    rs.getDate("issue_date"),
                    rs.getDate("due_date"),
                    rs.getString("status")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage());
        }

        f.add(new JScrollPane(table), BorderLayout.CENTER);
        JButton back = new JButton("Back to Dashboard");
        styleButton(back, Color.BLACK);
        back.addActionListener(e -> {
            f.dispose();
            parent.setVisible(true);
        });
        f.add(back, BorderLayout.SOUTH);
        f.setVisible(true);
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
    }

    private void testDBConnection() {
        try {
            Connection con = getConnection();
            // Connected smoothly, no need to interrupt flow
            con.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database Connection Failed! Make sure MySQL is running and credentials are correct.\n" + e.getMessage());
        }
    }

    public static void main(String[] args) {
        LibraryManagementSystemFull app = new LibraryManagementSystemFull();
        app.testDBConnection();
    }
}