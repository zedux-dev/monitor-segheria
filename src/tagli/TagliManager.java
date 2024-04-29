package tagli;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import segheria.Segheria;

public class TagliManager {
	public TagliManager() {
		try {
			Statement stmt = Segheria.db.conn.createStatement();
			stmt.executeUpdate("CREATE TABLE tagli (ID INTEGER PRIMARY KEY AUTOINCREMENT, duration INT, time TEXT, lunghezza REAL, larghezza REAL, spessore REAL, lama_usata INT);");
			stmt.close();
        } catch (SQLException e) { System.out.println(e.getMessage()); }
	}
	
	public int[] getSessionData() {
		int tagli = 0;
		int metri = 0;
		int ore = 0;
		
		try {
			Statement stmt = Segheria.db.conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(id) AS tagli, SUM(lunghezza) AS metri, SUM(duration) AS ore FROM tagli WHERE DATETIME(time) >= '" + Segheria.sessionStart + "'");
			
			System.out.println("SELECT COUNT(id) AS tagli, SUM(lunghezza) AS metri, SUM(duration) AS ore FROM tagli WHERE DATETIME(time) > '" + Segheria.sessionStart + "'");
			
			while(rs.next()) {
				tagli = rs.getInt("tagli");
				metri = rs.getInt("metri");
				ore = rs.getInt("ore");
			}
			
			stmt.close();
			
		} catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		
		int[] data = new int[4];
		data[0] = tagli;
		data[1] = metri;
		data[2] = ore;
		
		return data;
	}
	
	public void newTaglio(int durataSecondi) {
		try {
			int lunghezza = (int)Segheria.inputSessionLunghezza.getValue();
			int larghezza = (int)Segheria.inputSessionLarghezza.getValue();
			int spessore = (int)Segheria.inputSessionSpessore.getValue();
			
			int lamaUsata = Segheria.lameManager.getSelectedLama();
			
			if(lamaUsata > -1) {
				LocalDateTime nowDate = LocalDateTime.now();
				DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				String now = nowDate.format(myFormatObj);
				
				Statement stmt = Segheria.db.conn.createStatement();
				stmt.executeUpdate("INSERT INTO tagli (duration, time, lunghezza, larghezza, spessore, lama_usata) VALUES ('" + durataSecondi + "', '" + now + "', '" + lunghezza + "', '" + larghezza + "', '" + spessore + "', '" + lamaUsata + "');");
				stmt.close();
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
;	}
}