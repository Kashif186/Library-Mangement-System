import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import net.proteanit.sql.DbUtils;

public class Members extends JFrame {

	private JPanel contentPane;
	Connection connection = null;
	JTable table;
	private int id;
	private String name;
	
	public void setID(int num) {
		id = num;	
	}
	
	public void setName(int num) {
		try {
			PreparedStatement prep = connection.prepareStatement("SELECT name FROM MembersTable WHERE memberID = ?");
			prep.setInt(1, num);
			ResultSet rs = prep.executeQuery();
			name = rs.getString(1);
			
			prep.close();
			rs.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void refreshBooksTable() {
		try {
			PreparedStatement state = connection.prepareStatement("SELECT bookID,title,author,availability,ReturnDate FROM BooksTable;");
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
	 * Create the frame.
	 */
	public Members() {
		this.setTitle("Members");
		connection = SQLiteConnection.dbConnector();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1282, 856);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblLibraryManagementSystem = new JLabel("Library Management System");
		lblLibraryManagementSystem.setFont(new Font("Times New Roman", Font.BOLD, 30));
		lblLibraryManagementSystem.setBounds(229, 32, 373, 79);
		contentPane.add(lblLibraryManagementSystem);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(404, 158, 848, 638);
		contentPane.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		
		JButton btnViewAvailableBooks = new JButton("View Available Books");
		btnViewAvailableBooks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					PreparedStatement state = connection.prepareStatement("SELECT bookID, title, author, availability FROM BooksTable WHERE availability = 'Available';");
					ResultSet res = state.executeQuery();
					table.setModel(DbUtils.resultSetToTableModel(res));
					state.close();
					res.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnViewAvailableBooks.setBounds(49, 220, 331, 25);
		contentPane.add(btnViewAvailableBooks);
		
		
		
		JButton btnViewFine = new JButton("View Your Fine");
		btnViewFine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SQLiteConnection db = new SQLiteConnection();
				try {
					db.viewFine(id);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnViewFine.setBounds(49, 334, 331, 25);
		contentPane.add(btnViewFine);
		
		
		JButton btnViewAllBooks = new JButton("View All Books");
		btnViewAllBooks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PreparedStatement state;
				try {
					state = connection.prepareStatement("SELECT bookID, title, author, availability, ReturnDate FROM BooksTable;");
					ResultSet res = state.executeQuery();
					table.setModel(DbUtils.resultSetToTableModel(res));
					state.close();
					res.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
					
			}
		});
		btnViewAllBooks.setBounds(49, 258, 331, 25);
		contentPane.add(btnViewAllBooks);
		
		JLabel icon = new JLabel("");
		Image image = new ImageIcon(this.getClass().getResource("/Books-icon.png")).getImage();
		icon.setIcon(new ImageIcon(image));
		icon.setBounds(614, 7, 135, 123);
		contentPane.add(icon);
		
		JLabel lblSearchBookVia = new JLabel("Search Book via:");
		lblSearchBookVia.setBounds(49, 158, 106, 20);
		contentPane.add(lblSearchBookVia);
		
		JButton btnId = new JButton("ID");
		btnId.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				refreshBooksTable();
				try {
					int bookID = Integer.parseInt(JOptionPane.showInputDialog("Enter book's id :"));
					PreparedStatement bookCheck = connection.prepareStatement("SELECT bookID,title,author,availability, ReturnDate FROM  BooksTable WHERE bookID = " + bookID);
					ResultSet checkingBook = bookCheck.executeQuery();
					table.setModel(DbUtils.resultSetToTableModel(checkingBook));
					bookCheck.close();
					checkingBook.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(null, "Error: Incorrect format for ID. Must be integers. Try again.");
				}
				
			}
		});
		btnId.setBounds(166, 158, 58, 25);
		contentPane.add(btnId);
		
		JButton btnTitle = new JButton("Title");
		btnTitle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshBooksTable();
				String title = JOptionPane.showInputDialog("Enter book's title :");
				try {
					PreparedStatement bookCheck = connection.prepareStatement("SELECT bookID,title,author,availability, ReturnDate FROM  BooksTable WHERE title = ?" );
					bookCheck.setString(1, title);
					ResultSet checkingBook = bookCheck.executeQuery();
					table.setModel(DbUtils.resultSetToTableModel(checkingBook));
					bookCheck.close();
					checkingBook.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}	
				
			}
		});
		btnTitle.setBounds(230, 158, 58, 25);
		contentPane.add(btnTitle);
		
		JButton btnAuthor = new JButton("Author");
		btnAuthor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshBooksTable();
				String author = JOptionPane.showInputDialog("Enter author's name :");
				try {
					PreparedStatement bookCheck = connection.prepareStatement("SELECT bookID,title,author,availability, ReturnDate FROM  BooksTable WHERE author = ?");
					bookCheck.setString(1, author);
					ResultSet checkingBook = bookCheck.executeQuery();
					table.setModel(DbUtils.resultSetToTableModel(checkingBook));
					bookCheck.close();
					checkingBook.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnAuthor.setBounds(300, 158, 80, 25);
		contentPane.add(btnAuthor);
		
		JButton btnViewYourBorrowed = new JButton("View Your Borrowed Books");
		btnViewYourBorrowed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					PreparedStatement state = connection.prepareStatement("SELECT * FROM BooksTable WHERE memberID = ?");
					state.setInt(1, id);
					ResultSet res = state.executeQuery();
					table.setModel(DbUtils.resultSetToTableModel(res));
					state.close();
					res.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		btnViewYourBorrowed.setBounds(49, 296, 331, 25);
		contentPane.add(btnViewYourBorrowed);
		
		refreshBooksTable();
	}
}
