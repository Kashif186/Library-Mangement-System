import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Image;

public class Login {

	private JFrame frame;
	Connection connection = null;
	private JTextField textField;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login window = new Login();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Login() {
		initialize();
		connection = SQLiteConnection.dbConnector();
//		try {
//
//			//add fines to users who have not returned books
//			PreparedStatement prep = connection.prepareStatement("SELECT * FROM BooksTable");
//			ResultSet books = prep.executeQuery();
//			while (books.next()) {
//				int bookID = books.getInt(1);
//				SQLiteConnection database = new SQLiteConnection();
//				database.issueFine(bookID);
//			}		
//			prep.close();
//			books.close();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 931, 696);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(402, 203, 308, 30);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPassword.setBounds(292, 311, 100, 30);
		frame.getContentPane().add(lblPassword);
		
		JLabel lblUserId = new JLabel("User ID:");
		lblUserId.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblUserId.setBounds(292, 200, 81, 30);
		frame.getContentPane().add(lblUserId);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(402, 314, 308, 30);
		frame.getContentPane().add(passwordField);
		
		JButton btnLogin = new JButton("Login");
		Image img = new ImageIcon(this.getClass().getResource("/Login-icon.png")).getImage();
		btnLogin.setIcon(new ImageIcon(img));
		btnLogin.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					String query = "SELECT * FROM UserLogin WHERE userID = ? AND password = ?";
					PreparedStatement prep = connection.prepareStatement(query);
					prep.setString(1, textField.getText());
					prep.setString(2, passwordField.getText());
					int id = Integer.parseInt(textField.getText());
					
					ResultSet rs = prep.executeQuery();
					int count = 0;
					//rs gives value one by one so use while loop
					while(rs.next()) {
						count++;
						
					}
					if(count == 1) {
						JOptionPane.showMessageDialog(null, "User ID and password is correct");
						frame.dispose();
						
						PreparedStatement memberCheck = connection.prepareStatement("SELECT memberID FROM MembersTable WHERE memberID = ?");
						memberCheck.setInt(1, id);
						ResultSet checkingMember = memberCheck.executeQuery();
						if(!checkingMember.next()) {
							memberCheck.close();
							checkingMember.close();
						}
						else{
							Members mem = new Members();
							mem.setID(id);
							mem.setVisible(true);
							rs.close();
							prep.close();
							memberCheck.close();
							checkingMember.close();
							return;
						}
						
						PreparedStatement librarianCheck = connection.prepareStatement("SELECT librarianID FROM LibrariansTable WHERE librarianID = ?");
						librarianCheck.setInt(1, id);
						ResultSet checkingLibrarian = librarianCheck.executeQuery();
						
						if(!checkingLibrarian.next()) {
							librarianCheck.close();
							checkingLibrarian.close();
						}
						else {
							Librarians lib = new Librarians();
							lib.setVisible(true);
							rs.close();
							prep.close();
							librarianCheck.close();
							checkingLibrarian.close();
							return;
						}
						
						PreparedStatement adminCheck = connection.prepareStatement("SELECT adminID FROM AdminsTable WHERE adminID = ?");
						adminCheck.setInt(1, id);
						ResultSet checkingAdmin = adminCheck.executeQuery();
						
						if(!checkingAdmin.next()) {
							adminCheck.close();
							checkingAdmin.close();
						}
						else {
							Admins a = new Admins();
							a.setVisible(true);						
							rs.close();
							prep.close();
							adminCheck.close();
							checkingAdmin.close();
							return;
						}
					}
					else {
						JOptionPane.showMessageDialog(null, "User ID or password is invalid");
					}
					rs.close();
					prep.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}			
			}
		});
		btnLogin.setBounds(329, 419, 139, 56);
		frame.getContentPane().add(btnLogin);
		
		JLabel lblLibraryManagementSystem = new JLabel("Library Management System");
		lblLibraryManagementSystem.setFont(new Font("Times New Roman", Font.BOLD, 40));
		lblLibraryManagementSystem.setBounds(216, 64, 508, 62);
		frame.getContentPane().add(lblLibraryManagementSystem);
		
		JButton btnExit = new JButton("Exit");
		Image image2 = new ImageIcon(this.getClass().getResource("/exit-icon.png")).getImage();
		btnExit.setIcon(new ImageIcon(image2));
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		btnExit.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		btnExit.setBounds(528, 419, 139, 56);
		frame.getContentPane().add(btnExit);
		
		JLabel label = new JLabel("");
		Image image = new ImageIcon(this.getClass().getResource("/Library-icon.png")).getImage();
		label.setIcon(new ImageIcon(image));
		label.setBounds(77, 190, 189, 222);
		frame.getContentPane().add(label);
	}
}
