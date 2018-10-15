import java.awt.EventQueue;
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

public class Admins extends JFrame {

	private JPanel contentPane;
	Connection connection = null;
	private JTable table;

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
	
	public void refreshBooksTable() {
		try {
			PreparedStatement state = connection.prepareStatement("SELECT * FROM BooksTable;");
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
	public Admins() {
		this.setTitle("Admin");
		connection = SQLiteConnection.dbConnector();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1213, 806);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		JButton btnAddLibrarian = new JButton("Add/Update Librarian");
		btnAddLibrarian.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AddLibrarian lib = new AddLibrarian();
				lib.setVisible(true);
				lib.refreshLibrariansTable();		
			}
		});
		contentPane.setLayout(null);
		btnAddLibrarian.setBounds(60, 153, 178, 25);
		contentPane.add(btnAddLibrarian);
		
		JButton btnRemoveLibrarian = new JButton("Remove Librarian");
		btnRemoveLibrarian.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshLibrariansTable();
				try {
					int id = Integer.parseInt(JOptionPane.showInputDialog("Enter librarian's id :"));
					SQLiteConnection db = new SQLiteConnection();
					db.removeLibrarian(id);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NumberFormatException e2) {
					JOptionPane.showMessageDialog(null, "Error: Incorrect format for id");
				}
				refreshLibrariansTable();
				
			}
		});
		btnRemoveLibrarian.setBounds(268, 153, 144, 25);
		contentPane.add(btnRemoveLibrarian);
		
		JButton btnAddBook = new JButton("Add Book");
		btnAddBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshBooksTable();
				try {
					int id = Integer.parseInt(JOptionPane.showInputDialog("Enter book's id :"));
					String title = JOptionPane.showInputDialog("Enter books's title :");
					String author = JOptionPane.showInputDialog("Enter book's author :");
					SQLiteConnection db = new SQLiteConnection();
					db.addBook(id, title, author);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					
					JOptionPane.showMessageDialog(null, "Error: Book with this id already exists");
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NumberFormatException e1) {
					JOptionPane.showMessageDialog(null, "Error: Incorrect format for id");
					
				}
				refreshBooksTable();
			}
		});
		btnAddBook.setBounds(60, 191, 178, 25);
		contentPane.add(btnAddBook);
		
		JButton btnRemoveBook = new JButton("Remove Book");
		btnRemoveBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshBooksTable();		
				try {
					int id = Integer.parseInt(JOptionPane.showInputDialog("Enter book's id :"));
					SQLiteConnection db = new SQLiteConnection();
					db.removeBook(id);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NumberFormatException e2) {
					JOptionPane.showMessageDialog(null, "Error: Incorrect format for id");
				}
				refreshBooksTable();
			}
		});
		btnRemoveBook.setBounds(268, 191, 144, 25);
		contentPane.add(btnRemoveBook);
		
		JLabel lblLibraryManagementSystem = new JLabel("Library Management System");
		lblLibraryManagementSystem.setFont(new Font("Times New Roman", Font.BOLD, 30));
		lblLibraryManagementSystem.setBounds(226, 42, 376, 54);
		contentPane.add(lblLibraryManagementSystem);
		
		JButton btnAddMember = new JButton("Add/Update Member");
		btnAddMember.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AddMember mem = new AddMember();
				mem.setVisible(true);
				mem.refreshMembersTable();
			}
		});
		btnAddMember.setBounds(60, 229, 178, 25);
		contentPane.add(btnAddMember);
		
		JButton btnRemoveMember = new JButton("Remove Member");
		btnRemoveMember.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				refreshMembersTable();
				try {
					int id = Integer.parseInt(JOptionPane.showInputDialog("Enter member's id :"));
					SQLiteConnection db = new SQLiteConnection();
					db.removeMember(id);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NumberFormatException e2) {
					JOptionPane.showMessageDialog(null, "Error: Incorrect format for id");
				}
				refreshMembersTable();
			}
		});
		btnRemoveMember.setBounds(268, 229, 144, 25);
		contentPane.add(btnRemoveMember);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(434, 153, 749, 593);
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
		btnViewAvailableBooks.setBounds(60, 373, 352, 25);
		contentPane.add(btnViewAvailableBooks);
		
		
		
		JButton btnViewFine = new JButton("View All Member's With Fines");
		btnViewFine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					PreparedStatement state = connection.prepareStatement("SELECT * FROM MembersTable WHERE fine IS NOT NULL;");
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
		btnViewFine.setBounds(60, 335, 352, 25);
		contentPane.add(btnViewFine);
		
		JButton btnViewAllReserved = new JButton("View All Reserved Books");
		btnViewAllReserved.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					PreparedStatement state = connection.prepareStatement("SELECT bookID, title, author, availability FROM BooksTable WHERE availability = 'Reserved';");
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
		btnViewAllReserved.setBounds(60, 411, 352, 25);
		contentPane.add(btnViewAllReserved);
		
		JButton btnViewAllBooks = new JButton("View All Books");
		btnViewAllBooks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshBooksTable();		
			}
		});
		btnViewAllBooks.setBounds(60, 449, 352, 25);
		contentPane.add(btnViewAllBooks);
		
		JButton btnViewAllMembers = new JButton("View All Members");
		btnViewAllMembers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshMembersTable();
			}
		});
		btnViewAllMembers.setBounds(60, 487, 352, 25);
		contentPane.add(btnViewAllMembers);
		
		JButton btnViewAllLibrarians = new JButton("View All Librarians");
		btnViewAllLibrarians.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshLibrariansTable();
			}
		});
		btnViewAllLibrarians.setBounds(60, 297, 352, 25);
		contentPane.add(btnViewAllLibrarians);
		
		JLabel icon = new JLabel("");
		Image image = new ImageIcon(this.getClass().getResource("/Books-icon.png")).getImage();
		icon.setIcon(new ImageIcon(image));
		icon.setBounds(614, 7, 135, 123);
		contentPane.add(icon);
		
		JButton btnViewBooksBorrowed = new JButton("View Books Borrowed By A Member");
		btnViewBooksBorrowed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					int memberID = Integer.parseInt(JOptionPane.showInputDialog("Enter member's id :"));
					PreparedStatement memberCheck = connection.prepareStatement("SELECT * FROM  BooksTable WHERE memberID = " + memberID);
					ResultSet checkingMember = memberCheck.executeQuery();
					table.setModel(DbUtils.resultSetToTableModel(checkingMember));
					memberCheck.close();
					checkingMember.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
		});
		btnViewBooksBorrowed.setBounds(60, 525, 352, 25);
		contentPane.add(btnViewBooksBorrowed);
		
		refreshBooksTable();
	}
}
