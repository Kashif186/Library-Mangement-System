import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

public class AddLibrarian extends JDialog {

	private final JPanel contentPanel = new JPanel();	
	Connection connection = null;
	private JTable table;
	private JTextField textFieldID;
	private JTextField textFieldName;
	private JPasswordField passwordField;
	public void refreshLibrariansTable() {
		try {
			PreparedStatement state = connection.prepareStatement("SELECT * FROM LibrariansTable");
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
	public AddLibrarian() {
		this.setTitle("Add Librarian");
		connection = SQLiteConnection.dbConnector();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1263, 826);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPanel);
		contentPanel.setLayout(null);
		
		JLabel lblLibraryManagementSystem = new JLabel("Library Management System");
		lblLibraryManagementSystem.setFont(new Font("Times New Roman", Font.BOLD, 30));
		lblLibraryManagementSystem.setBounds(226, 42, 376, 54);
		contentPanel.add(lblLibraryManagementSystem);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(434, 153, 799, 613);
		contentPanel.add(scrollPane);
		
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					int row = table.getSelectedRow();
					String id = (table.getModel().getValueAt(row, 0)).toString();
					
					
					String query = "SELECT * FROM LibrariansTable where librarianID = ?";
					PreparedStatement prep = connection.prepareStatement(query);
					prep.setString(1, id);
					
					ResultSet rs = prep.executeQuery();
					
					while(rs.next()) {
						textFieldID.setText(rs.getString("librarianID"));
						textFieldName.setText(rs.getString("name"));		
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
		
		JLabel lblAddingLibrarian = new JLabel("Adding Librarian - Complete All Fields:");
		lblAddingLibrarian.setFont(new Font("Times New Roman", Font.PLAIN, 24));
		lblAddingLibrarian.setBounds(12, 153, 387, 42);
		contentPanel.add(lblAddingLibrarian);
		
		JLabel lblLibrarianId = new JLabel("Librarian ID :");
		lblLibrarianId.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblLibrarianId.setBounds(12, 226, 135, 24);
		contentPanel.add(lblLibrarianId);
		
		textFieldID = new JTextField();
		textFieldID.setBounds(159, 226, 240, 24);
		contentPanel.add(textFieldID);
		textFieldID.setColumns(10);
		
		JLabel lblName = new JLabel("Name :");
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblName.setBounds(66, 260, 81, 24);
		contentPanel.add(lblName);
		
		textFieldName = new JTextField();
		textFieldName.setBounds(159, 263, 240, 24);
		contentPanel.add(textFieldName);
		textFieldName.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password :");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPassword.setBounds(34, 297, 113, 24);
		contentPanel.add(lblPassword);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(159, 301, 240, 22);
		contentPanel.add(passwordField);
		
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {				 
				try {
					SQLiteConnection db = new SQLiteConnection();
					int id = Integer.parseInt(textFieldID.getText());
					String name = textFieldName.getText();
					String password = passwordField.getText();
					db.addLibrarian(id, name, password);
					refreshLibrariansTable();			
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Error: A user with the entered id already exists.");
					return;
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error: ID must be integers");
					return;
				}	
			}
		});
		btnAdd.setBounds(50, 383, 97, 25);
		contentPanel.add(btnAdd);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {	
				try {					
					int id = Integer.parseInt(textFieldID.getText());
					String name = textFieldName.getText();
					String password = passwordField.getText();
					PreparedStatement prep = connection.prepareStatement("SELECT librarianID FROM LibrariansTable WHERE librarianID = ?");
					prep.setInt(1, id);
					ResultSet rs = prep.executeQuery();
					if(!rs.next()) {
						JOptionPane.showMessageDialog(null, "Error: This is not an ID for a Librarian");
						prep.close();
						rs.close();
						return;
					}
					
					PreparedStatement query = connection.prepareStatement("UPDATE LibrariansTable SET name = ? WHERE librarianID = ?");
					
					query.setString(1, name);
					query.setInt(2, id);
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
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "Error: ID must be integers");
				}
				refreshLibrariansTable();
				
			}
		});
		btnUpdate.setBounds(159, 383, 97, 25);
		contentPanel.add(btnUpdate);
		
		refreshLibrariansTable();
	}

}
