import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.proteanit.sql.DbUtils;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AddMember extends JDialog {

	
	private final JPanel contentPanel = new JPanel();
	
	Connection connection = null;
	private JTable table;
	private JTextField textFieldID;
	private JTextField textFieldName;
	private JPasswordField passwordField;
	private int a;
	private JTextField numField;
	private JTextField textField_1;
	
	public void refreshMembersTable() {
		try {
			PreparedStatement state = connection.prepareStatement("SELECT * FROM MembersTable;");
			ResultSet res = state.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(res));
			state.close();
			res.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AddMember() {
		this.setTitle("Add Member");
		connection = SQLiteConnection.dbConnector();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1251, 831);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPanel);
		contentPanel.setLayout(null);
		
		JLabel lblLibraryManagementSystem = new JLabel("Library Management System");
		lblLibraryManagementSystem.setFont(new Font("Times New Roman", Font.BOLD, 30));
		lblLibraryManagementSystem.setBounds(226, 42, 376, 54);
		contentPanel.add(lblLibraryManagementSystem);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(434, 153, 787, 618);
		contentPanel.add(scrollPane);
		
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					int row = table.getSelectedRow();
					String id = (table.getModel().getValueAt(row, 0)).toString();
					
					
					String query = "SELECT * FROM MembersTable where memberID = ?";
					PreparedStatement prep = connection.prepareStatement(query);
					prep.setString(1, id);
					
					ResultSet rs = prep.executeQuery();
					
					while(rs.next()) {
						textFieldID.setText(rs.getString("memberID"));
						textFieldName.setText(rs.getString("name"));
						numField.setText(rs.getString("phoneNumber"));
						textField_1.setText(rs.getString("dateOfBirth"));			
					}
					prep.close();
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		scrollPane.setViewportView(table);
		
		JLabel icon = new JLabel("");
		Image image = new ImageIcon(this.getClass().getResource("/Books-icon.png")).getImage();
		icon.setIcon(new ImageIcon(image));
		icon.setBounds(614, 7, 135, 123);
		contentPanel.add(icon);
		
		JLabel lblAddingLibrarian = new JLabel("Adding Member - Complete All Fields:");
		lblAddingLibrarian.setFont(new Font("Times New Roman", Font.PLAIN, 24));
		lblAddingLibrarian.setBounds(12, 153, 387, 42);
		contentPanel.add(lblAddingLibrarian);
		
		JLabel lblLibrarianId = new JLabel("Member ID :");
		lblLibrarianId.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblLibrarianId.setBounds(44, 225, 103, 24);
		contentPanel.add(lblLibrarianId);
		
		textFieldID = new JTextField();
		textFieldID.setBounds(159, 226, 240, 24);
		contentPanel.add(textFieldID);
		textFieldID.setColumns(10);
		
		JLabel lblName = new JLabel("Name :");
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblName.setBounds(89, 262, 58, 24);
		contentPanel.add(lblName);
		
		textFieldName = new JTextField();
		textFieldName.setBounds(159, 263, 240, 24);
		contentPanel.add(textFieldName);
		textFieldName.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password :");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblPassword.setBounds(66, 299, 81, 24);
		contentPanel.add(lblPassword);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(159, 301, 240, 22);
		contentPanel.add(passwordField);
		
		JLabel lblPhoneNumber = new JLabel("Phone Number :");
		lblPhoneNumber.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblPhoneNumber.setBounds(30, 334, 117, 24);
		contentPanel.add(lblPhoneNumber);
		
		numField = new JTextField();
		numField.setBounds(159, 336, 240, 22);
		contentPanel.add(numField);
		numField.setColumns(10);
		
		JLabel lblDateOfBirth = new JLabel("Date of Birth (YYYY-MM-DD) :");
		lblDateOfBirth.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblDateOfBirth.setBounds(12, 371, 228, 24);
		contentPanel.add(lblDateOfBirth);
		
		textField_1 = new JTextField();
		textField_1.setBounds(241, 371, 158, 22);
		contentPanel.add(textField_1);
		textField_1.setColumns(10);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {				 
				try {
					SQLiteConnection db = new SQLiteConnection();
					int id = Integer.parseInt(textFieldID.getText());
					String name = textFieldName.getText();
					String password = passwordField.getText();
					int num = Integer.parseInt(numField.getText());		
					String text = textField_1.getText();
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					try {
						Date dob = df.parse(text);
						db.addMember(id, name, password, num, dob);
						refreshMembersTable();
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null, "Error: Incorrect format for date. Try Again.");
						return;
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error: ID already taken or the format is incorrect - ID must be numbers");
					e.printStackTrace();
					return;
				}
			}
		});
		btnAdd.setBounds(89, 467, 97, 25);
		contentPanel.add(btnAdd);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {					
					int id = Integer.parseInt(textFieldID.getText());
					String name = textFieldName.getText();
					String password = passwordField.getText();
					int num = Integer.parseInt(numField.getText());		
					String text = textField_1.getText();
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					Date dob = df.parse(text);
					String birthDate = df.format(dob);
					
					PreparedStatement prep = connection.prepareStatement("SELECT memberID FROM MembersTable WHERE memberID = ?");
					prep.setInt(1, id);
					ResultSet rs = prep.executeQuery();
					if(!rs.next()) {
						JOptionPane.showMessageDialog(null, "Error: This is not an ID for a Librarian");
						prep.close();
						rs.close();
						return;
					}
					
					PreparedStatement query = connection.prepareStatement("UPDATE MembersTable SET name = ?, phoneNumber = ?, dateOfBirth = ?  WHERE memberID = ?");
					
					query.setString(1, name);
					query.setInt(2, num);
					query.setString(3, birthDate);
					query.setInt(4, id);
					query.execute();
					query.close();
					
					if(password.equals(null)||password.equals("")) {	
					}
					else {
						PreparedStatement query2 = connection.prepareStatement("UPDATE UserLogin SET password = ? WHERE userID = ?");
						query2.setString(1, password);
						query2.setInt(2, id);
						query2.execute();
						query2.close();
					}
					
					JOptionPane.showMessageDialog(null, "Update Successfull");
					prep.close();
					rs.close();
					
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(null, "Error: ID and phone number must be integers");
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "Error: Incorrect format for date");
					e1.printStackTrace();
				}
				refreshMembersTable();
			}
		});
		btnUpdate.setBounds(198, 467, 97, 25);
		contentPanel.add(btnUpdate);
		
		refreshMembersTable();
	}

}
