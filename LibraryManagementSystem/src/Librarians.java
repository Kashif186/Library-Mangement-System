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

public class Librarians extends JFrame {

	private JPanel contentPane;
	private static Librarians frame = new Librarians();
		
	Connection connection = null;
	JTable table;
	
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
	public Librarians() {
		this.setTitle("Librarian");
		connection = SQLiteConnection.dbConnector();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1286, 882);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
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
		btnAddBook.setBounds(12, 191, 181, 25);
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
		btnRemoveBook.setBounds(226, 191, 144, 25);
		contentPane.add(btnRemoveBook);
		
		JLabel lblLibraryManagementSystem = new JLabel("Library Management System");
		lblLibraryManagementSystem.setFont(new Font("Times New Roman", Font.BOLD, 30));
		lblLibraryManagementSystem.setBounds(226, 42, 376, 54);
		contentPane.add(lblLibraryManagementSystem);
		
		JButton btnAddMember = new JButton("Add/Update Member");
		btnAddMember.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
				AddMember mem = new AddMember();
				mem.setVisible(true);
				mem.refreshMembersTable();
			}
		});
		btnAddMember.setBounds(12, 229, 181, 25);
		contentPane.add(btnAddMember);
		
		JButton btnRemoveMember = new JButton("Remove Member");
		btnRemoveMember.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				refreshMembersTable();
				try {
					int id = Integer.parseInt(JOptionPane.showInputDialog("Enter book's id :"));
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
		btnRemoveMember.setBounds(226, 229, 144, 25);
		contentPane.add(btnRemoveMember);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(382, 153, 874, 669);
		contentPane.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		
		JButton btnViewAvailableBooks = new JButton("View All Available Books");
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
		btnViewAvailableBooks.setBounds(12, 408, 358, 25);
		contentPane.add(btnViewAvailableBooks);
		
		
		
		JButton btnViewFine = new JButton("View All Member's With Fines");
		btnViewFine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					PreparedStatement state = connection.prepareStatement("SELECT * FROM MembersTable WHERE fine IS NOT NULL AND fine != 0;");
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
		btnViewFine.setBounds(12, 370, 358, 25);
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
		btnViewAllReserved.setBounds(12, 446, 358, 25);
		contentPane.add(btnViewAllReserved);
		
		JButton btnViewAllBooks = new JButton("View All Books");
		btnViewAllBooks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshBooksTable();		
			}
		});
		btnViewAllBooks.setBounds(12, 484, 358, 25);
		contentPane.add(btnViewAllBooks);
		
		JButton btnViewAllMembers = new JButton("View All Members");
		btnViewAllMembers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshMembersTable();
			}
		});
		btnViewAllMembers.setBounds(12, 522, 358, 25);
		contentPane.add(btnViewAllMembers);
		
		JButton btnViewAllLibrarians = new JButton("View All Librarians");
		btnViewAllLibrarians.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshLibrariansTable();
			}
		});
		btnViewAllLibrarians.setBounds(12, 332, 358, 25);
		contentPane.add(btnViewAllLibrarians);
		
		JLabel icon = new JLabel("");
		Image image = new ImageIcon(this.getClass().getResource("/Books-icon.png")).getImage();
		icon.setIcon(new ImageIcon(image));
		icon.setBounds(614, 7, 135, 123);
		contentPane.add(icon);
		
		JButton btnReceivePaymentFor = new JButton("Receive Payment For Fines");
		btnReceivePaymentFor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshMembersTable();
				int id = Integer.parseInt(JOptionPane.showInputDialog("Enter member's id :"));
				int amount = Integer.parseInt(JOptionPane.showInputDialog("Enter Payment Amount :"));
				SQLiteConnection db = new SQLiteConnection();
				try {
					db.updateFine(id, amount);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				refreshMembersTable();
				
			}
		});
		btnReceivePaymentFor.setBounds(12, 560, 358, 25);
		contentPane.add(btnReceivePaymentFor);
		
		JButton btnIssueBook = new JButton("Issue Book");
		btnIssueBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshBooksTable();
				
				try {
					int bookID = Integer.parseInt(JOptionPane.showInputDialog("Enter book's id :"));
					int memberID = Integer.parseInt(JOptionPane.showInputDialog("Enter member's id :"));
					SQLiteConnection db = new SQLiteConnection();
					db.issueBook(bookID, memberID);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (NumberFormatException e2) {
					JOptionPane.showMessageDialog(null, "Error: Incorrect format for id");
				}
				refreshBooksTable();			
			}
		});
		btnIssueBook.setBounds(12, 267, 182, 25);
		contentPane.add(btnIssueBook);
		
		JButton btnReturnBook = new JButton("Return Book");
		btnReturnBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshBooksTable();
				try {
					int bookID = Integer.parseInt(JOptionPane.showInputDialog("Enter book's id :"));
					SQLiteConnection db = new SQLiteConnection();
					db.returnBook(bookID);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (NumberFormatException e2) {
					JOptionPane.showMessageDialog(null, "Error: Incorrect format for id");
				}
				refreshBooksTable();	
			}
		});
		btnReturnBook.setBounds(226, 267, 144, 25);
		contentPane.add(btnReturnBook);
		
		JLabel lblSearchBookVia = new JLabel("Search Book via:");
		lblSearchBookVia.setBounds(12, 162, 106, 16);
		contentPane.add(lblSearchBookVia);
		
		JButton btnId = new JButton("ID");
		btnId.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				refreshBooksTable();
				try {
					int bookID = Integer.parseInt(JOptionPane.showInputDialog("Enter book's id :"));
					PreparedStatement bookCheck = connection.prepareStatement("SELECT * FROM  BooksTable WHERE bookID = " + bookID);
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
		btnId.setBounds(117, 158, 58, 25);
		contentPane.add(btnId);
		
		JButton btnTitle = new JButton("Title");
		btnTitle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshBooksTable();
				String title = JOptionPane.showInputDialog("Enter book's title :");
				try {
					PreparedStatement bookCheck = connection.prepareStatement("SELECT * FROM  BooksTable WHERE title = ?" );
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
		btnTitle.setBounds(187, 158, 58, 25);
		contentPane.add(btnTitle);
		
		JButton btnAuthor = new JButton("Author");
		btnAuthor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshBooksTable();
				String author = JOptionPane.showInputDialog("Enter author's name :");
				try {
					PreparedStatement bookCheck = connection.prepareStatement("SELECT * FROM BooksTable WHERE author = ?");
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
		btnAuthor.setBounds(257, 158, 83, 25);
		contentPane.add(btnAuthor);
		
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
		btnViewBooksBorrowed.setBounds(12, 598, 358, 25);
		contentPane.add(btnViewBooksBorrowed);
		
		refreshBooksTable();
	}
}
