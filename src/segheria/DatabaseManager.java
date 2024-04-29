package segheria;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    public Connection conn = null;

	public DatabaseManager() {
		try {
            conn = DriverManager.getConnection("jdbc:sqlite:segheria.db");
            
            System.out.println("Connection to SQLite has been established.");
            
        } catch (SQLException e) {  
            System.out.println(e.getMessage());
        }
		
		initTables();
	}
	
	public void initTables() {
		/*
		try {
			Statement stmt = conn.createStatement();
			stmt = conn.createStatement();
			stmt.executeUpdate("CREATE TABLE tagli (ID INT PRIMARY key NOT NULL, duration INT NOT NULL, time TEXT NOT NULL, lunghezza REAL NOT NULL, larghezza REAL NOT NULL, spessore REAL NOT NULL, lama_usata INT NOT NULL)");
			stmt.close();
		} catch (SQLException e) { System.out.println(e.getMessage()); }
		*/
		
		try {
			Statement stmt = conn.createStatement();
			stmt = conn.createStatement();
			stmt.executeUpdate("CREATE TABLE options (ID INTEGER PRIMARY KEY AUTOINCREMENT, option TEXT, value TEXT)");
			stmt.close();
			
			stmt = conn.createStatement();			
			stmt.executeUpdate("INSERT INTO options (option, value) VALUES ('selected-lama', '');");
			stmt.close();
			
		} catch (SQLException e) { System.out.println(e.getMessage()); }
	}
	
	public void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
