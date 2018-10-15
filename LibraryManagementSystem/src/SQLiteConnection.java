

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;

public class SQLiteConnection {
	private static Connection con;
	
	//Connects to the database
	private void getConnection() throws ClassNotFoundException, SQLException {
		//load jdbc driver into driver manager.
		Class.forName("org.sqlite.JDBC");
		con = DriverManager.getConnection("jdbc:sqlite:LibraryDB.db");
	}
	
	public static Connection dbConnector(){
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:LibraryDB.db");
			return con;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e);
			return null;
		}

	}
	
	public void addLibrarian(int id, String name, String password) throws ClassNotFoundException, SQLException {
		
		if(con == null) {
			getConnection();
		}
		
		PreparedStatement prep = con.prepareStatement("INSERT INTO userLogin values(?,?);");
		PreparedStatement prep2 = con.prepareStatement("INSERT INTO LibrariansTable values(?,?,?);");
		prep.setInt(1, id);
		prep.setString(2, password);
		
		prep2.setInt(1, id);
		prep2.setString(2, name);
		prep2.setInt(3, id);
		
		prep.execute();
		prep2.execute();
		JOptionPane.showMessageDialog(null, "Successfully Added");
		
		prep.close();
		prep2.close();
	}
	
	public void addMember(int id, String name, String password, int num, Date dob) throws ClassNotFoundException, SQLException {
		
		if(con == null) {
			getConnection();
		}
		
		PreparedStatement prep = con.prepareStatement("INSERT INTO userLogin values(?,?);");
		PreparedStatement prep2 = con.prepareStatement("INSERT INTO MembersTable values(?,?,?,?,?,?);");
		prep.setInt(1, id);
		prep.setString(2, password);
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String birthDate = df.format(dob);
		
		prep2.setInt(1, id);
		prep2.setString(2, name);
		prep2.setInt(3, num);
		prep2.setString(4, birthDate);
		prep2.setInt(6, id);
		
		
		
		prep.execute();
		prep2.execute();
		JOptionPane.showMessageDialog(null, "Successfully Added");
		prep.close();
		prep2.close();
	}
	

	public void addBook(int id, String title, String author) throws ClassNotFoundException, SQLException {
	
		if(con == null) {
			getConnection();
		}
		
		PreparedStatement prep = con.prepareStatement("INSERT INTO BooksTable values(?,?,?,?,?,?,?);");
		prep.setInt(1, id);
		prep.setString(2, title);
		prep.setString(3, author);
		prep.setString(4, "Available");

		prep.execute();
		JOptionPane.showMessageDialog(null, "Successfully Added");
		prep.close();
	}
	
	public void removeLibrarian(int id) throws ClassNotFoundException, SQLException {
		
		if(con == null) {
			getConnection();
		}
		
		PreparedStatement prep = con.prepareStatement("DELETE FROM userLogin WHERE userID = ?");
		PreparedStatement prep2 = con.prepareStatement("DELETE FROM LibrariansTable WHERE librarianID = ?");
		prep.setInt(1, id);		
		prep2.setInt(1, id);
		//executes query and returns a number that tells how many records deleted.
		int prepRowDeleted = prep.executeUpdate();
		int prep2RowDeleted = prep2.executeUpdate();
		if(prepRowDeleted >= 1 && prep2RowDeleted >= 1) {
			JOptionPane.showMessageDialog(null, "Librarian Successfully Removed");
		}
		else {
			JOptionPane.showMessageDialog(null, "This id is not associated with a Librarian");
		}
		prep.close();
		prep2.close();
	}
	
	
	
	public void removeBook(int id) throws ClassNotFoundException, SQLException {
		
		if(con == null) {
			getConnection();
		}
		
		PreparedStatement prep = con.prepareStatement("DELETE FROM BooksTable WHERE bookID = ?");
		
		prep.setInt(1, id);
		int prepRowDeleted = prep.executeUpdate();
		if(prepRowDeleted >= 1) {
			JOptionPane.showMessageDialog(null, "Book Successfully Removed");
		}
		else {
			JOptionPane.showMessageDialog(null, "This id is not associated with a Book");
		}
		prep.close();
	}
	
	
	
	public void removeMember(int id) throws ClassNotFoundException, SQLException {
		
		if(con == null) {
			getConnection();
		}
		
		PreparedStatement prep = con.prepareStatement("DELETE FROM userLogin WHERE userID = ?;");
		PreparedStatement prep2 = con.prepareStatement("DELETE FROM MembersTable WHERE memberID = ?;");
		
		prep.setInt(1, id);		
		prep2.setInt(1, id);
		
		int prepRowDeleted = prep.executeUpdate();
		int prep2RowDeleted = prep2.executeUpdate();
		if(prepRowDeleted >= 1 && prep2RowDeleted >= 1) {
			JOptionPane.showMessageDialog(null, "Member Successfully Removed");
		}
		else {
			JOptionPane.showMessageDialog(null, "This id is not associated with a Member");
		}
		prep.close();
		prep2.close();
	}
	
	public void issueBook(int bookID, int memberID) throws ClassNotFoundException, SQLException {
		if(con == null) {
			getConnection();
		}
		//Check if the member entered exists in the library db. Exit if it doesnt
		PreparedStatement memberCheck = con.prepareStatement("SELECT memberID,fine FROM MembersTable WHERE memberID = " + memberID);
		ResultSet checkingMember = memberCheck.executeQuery();
		if(!checkingMember.next()) {
			JOptionPane.showMessageDialog(null, "Error: Member is not apart of this library");
			memberCheck.close();
			checkingMember.close();
			return;
		}
		
		int fine = checkingMember.getInt(2);
		
		if(fine > 0) {
			JOptionPane.showMessageDialog(null, "Book cannot be issued since there is an existing fine of £" + fine);
			memberCheck.close();
			checkingMember.close();
			return;
		}
		
		//Check if the book entered exists in the library db. Exit if it doesnt
		PreparedStatement bookCheck = con.prepareStatement("SELECT bookID FROM BooksTable WHERE bookID = " + bookID);
		ResultSet checkingBook= bookCheck.executeQuery();
		if(!checkingBook.next()) {
			JOptionPane.showMessageDialog(null, "Error: Book is not apart of this library");
			bookCheck.close();
			checkingBook.close();
			memberCheck.close();
			checkingMember.close();
			return;
		}
		bookCheck.close();
		checkingBook.close();		
				
		//Checking availability and memberID associated with a book
		PreparedStatement check = con.prepareStatement("SELECT availability,memberID FROM BooksTable WHERE bookID = " + bookID);
		ResultSet checkingDB = check.executeQuery();
		String availability = checkingDB.getString(1);
		int members = checkingDB.getInt(2);
		if(availability != "Unavailable" && members == 0) {
			if(con == null) {
				getConnection();
			}	
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Calendar currentDate = Calendar.getInstance();
		    Date sqlDate = new Date((currentDate.getTime()).getTime());
			String todayDate = df.format(sqlDate);

		    currentDate.add(Calendar.DAY_OF_YEAR, 7);
		    Date sqlReturn = new Date((currentDate.getTime()).getTime());
		    String returnDate = df.format(sqlReturn);

		    
			PreparedStatement prep = con.prepareStatement("UPDATE BooksTable SET availability = 'Unavailable', memberID = ?, IssueDate = ?, ReturnDate = ? WHERE bookID = ? AND availability = 'Available';");
			prep.setInt(1, memberID);
			prep.setInt(4, bookID);
			prep.setString(2, todayDate);
			prep.setString(3, returnDate);
			prep.execute();
			JOptionPane.showMessageDialog(null, "Book Successfully Issued");
			prep.close();
		}
		else if(availability == "Unavailable") {
			JOptionPane.showMessageDialog(null, "Error: Book is not available");
		}
		else if (members != 0) {
			JOptionPane.showMessageDialog(null, "Error: Book is already issued");
		}
		
		memberCheck.close();
		checkingMember.close();
		check.close();
		checkingDB.close();

	}
	
	
public void returnBook(int bookID) throws ClassNotFoundException, SQLException {
		if(con == null) {
			getConnection();
		}
		//Check if the book entered exists in the library db. Exit if it does not
		PreparedStatement bookCheck = con.prepareStatement("SELECT bookID FROM BooksTable WHERE bookID = " + bookID);
		ResultSet checkingBook = bookCheck.executeQuery();
		if(!checkingBook.next()) {
			JOptionPane.showMessageDialog(null, "Error: Book is not apart of this library");
			bookCheck.close();
			checkingBook.close();
			return;
		}
		//Checking availability and memberID associated with a book
		PreparedStatement check = con.prepareStatement("SELECT availability,memberID FROM BooksTable WHERE bookID = " + bookID);
		ResultSet checkingDB = check.executeQuery();
		String availability = checkingDB.getString(1);
		int members = checkingDB.getInt(2);	
		
		if(availability != "Available" && members != 0) {
			if(con == null) {
				getConnection();
			}
			PreparedStatement prep = con.prepareStatement("UPDATE BooksTable SET availability = 'Available', memberID = null, IssueDate = null, ReturnDate = null WHERE bookID = ?;");
			prep.setInt(1, bookID);
			prep.execute();
			JOptionPane.showMessageDialog(null, "Book Successfully Returned");
			prep.close();
		}
		else if(availability != "Unavailable") {
			JOptionPane.showMessageDialog(null, "Error: Book is already available");
		}
		
		bookCheck.close();
		check.close();
		checkingBook.close();
		checkingDB.close();
	}
	
	public void viewFine(int memberID) throws ClassNotFoundException, SQLException {
		if(con == null) {
			getConnection();
		}
		PreparedStatement memberCheck = con.prepareStatement("SELECT memberID FROM MembersTable WHERE memberID = " + memberID);
		ResultSet checkingMember = memberCheck.executeQuery();
		if(!checkingMember.next()) {
			JOptionPane.showMessageDialog(null, "Error: Member is not apart of this library");
			memberCheck.close();
			checkingMember.close();
			return;
		}
		PreparedStatement state = con.prepareStatement("SELECT fine FROM MembersTable WHERE memberID = ?;");
		state.setInt(1,  memberID);
		ResultSet res = state.executeQuery();
		int fine = res.getInt(1);
		JOptionPane.showMessageDialog(null, "You currently need to pay the library: £" + fine);
		
		state.close();
		checkingMember.close();
		memberCheck.close();
		res.close();
	}
	
	public void updateFine(int memberID, int amount) throws ClassNotFoundException, SQLException {
		if(con == null) {
			getConnection();
		}
		
		PreparedStatement memberCheck = con.prepareStatement("SELECT memberID, fine FROM MembersTable WHERE memberID = " + memberID);
		ResultSet checkingMember = memberCheck.executeQuery();
		if(!checkingMember.next()) {
			JOptionPane.showMessageDialog(null, "Error: Member is not apart of this library");
			memberCheck.close();
			checkingMember.close();
			return;
		}
		
		int fine = checkingMember.getInt(2);
		int update = fine - amount;
		if (update < 0) {
			int change = Math.abs(update);
			JOptionPane.showMessageDialog(null, "Change: £" + change);	
			update = 0;
		}
		else if(update > 0) {
			JOptionPane.showMessageDialog(null, "Remaining Fine: £" + update);
		}
		
		PreparedStatement prep = con.prepareStatement("UPDATE MembersTable SET fine = ? WHERE memberID = ?;");
		prep.setInt(1, update);
		prep.setInt(2, memberID);
		
		prep.execute();
		JOptionPane.showMessageDialog(null, "Account successfully updated");
		
		prep.close();
		memberCheck.close();
		checkingMember.close();		
	}
	
	public int daysBetween(Date d1, Date d2){
        return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
	}

	public void issueFine(int bookID) throws SQLException, ParseException, ClassNotFoundException {
		if(con == null) {
			getConnection();
		}
		Calendar currentDate = Calendar.getInstance();
	    Date sqlDate = new Date((currentDate.getTime()).getTime());
	    
		PreparedStatement dateCheck = con.prepareStatement("SELECT ReturnDate,memberID FROM BooksTable WHERE bookID = " + bookID);
		ResultSet checkingDate = dateCheck.executeQuery(); 
		
		Date bookDate;
		try{
			bookDate = checkingDate.getDate(1);
		}
		catch (Exception e) {
			bookDate = null;
		}
		int member = checkingDate.getInt(2);
		if (bookDate != null) {
			if (bookDate.after(sqlDate)) {
				int days = daysBetween(bookDate, sqlDate);
				PreparedStatement accountFine = con.prepareStatement("SELECT fine FROM MembersTable WHERE memberID = " + member);
				ResultSet f = accountFine.executeQuery(); 
				int fine = f.getInt(1);
				int newFine = fine + (days*1); 
				PreparedStatement prep = con.prepareStatement("UPDATE MembersTable SET fine = ? WHERE memberID = ?;");
				prep.setInt(1, newFine);
				prep.setInt(2, member);
				prep.execute();
				dateCheck.close();
				checkingDate.close();
				accountFine.close();
				f.close();
				prep.close();
				
				JOptionPane.showMessageDialog(null, "Account successfully updated");
				return;
			}
		}
		dateCheck.close();
		checkingDate.close();
	    
	}
	
	
	
}
