package segheria.lame;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import segheria.Segheria;

public class LameManager {
	public LameManager() {
		try {
			Statement stmt = Segheria.db.conn.createStatement();
			stmt.executeUpdate("CREATE TABLE lame (ID INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, maxOre INT, maxMetri INT, totOre INT, totMetri INT, lastCambio TEXT, lastAffilatura TEXT)");
			stmt.close();            
        } catch (SQLException e) { System.out.println(e.getMessage()); }
	}
	
	public void newLama(String name, int maxMetri, int maxOre) {
		try {
			Statement stmt = Segheria.db.conn.createStatement();
			LocalDateTime nowDate = LocalDateTime.now();
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String now = nowDate.format(myFormatObj);
			
			stmt.executeUpdate("INSERT INTO lame (name, maxOre, maxMetri, totOre, totMetri, lastCambio, lastAffilatura) VALUES ('" + name + "', " + String.valueOf(maxMetri) + ", " + String.valueOf(maxOre) + ", 0, 0, '" + now + "', '" + now + "');");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void cambiaLama(int id) {
		try {
			LocalDateTime nowDate = LocalDateTime.now();
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String now = nowDate.format(myFormatObj);
			
			Statement stmt = Segheria.db.conn.createStatement();
			stmt.executeUpdate("UPDATE lame SET lastCambio='" + now + "' WHERE id=" + id + ";");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void affilaLama(int id) {
		try {
			LocalDateTime nowDate = LocalDateTime.now();
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String now = nowDate.format(myFormatObj);
			
			Statement stmt = Segheria.db.conn.createStatement();
			stmt.executeUpdate("UPDATE lame SET lastAffilatura='" + now + "' WHERE id=" + id + ";");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void editLama(int id, String name, int maxMetri, int maxOre) {
		try {
			Statement stmt = Segheria.db.conn.createStatement();			
			stmt.executeUpdate("UPDATE lame SET name='" + name + "', maxOre=" + maxOre + ", maxMetri=" + maxMetri + " WHERE id=" + id + ";");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteLama(int id) {
		try {
			Statement stmt = Segheria.db.conn.createStatement();
			stmt.executeUpdate("DELETE FROM lame WHERE id=" + id + ";");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int getSelectedLama() {
		try {
			Statement stmt = Segheria.db.conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT value FROM options WHERE option = 'selected-lama';");
			while(rs.next()) {
				String val = rs.getString("value");
				if(val.equals("")) return -1;
				return Integer.valueOf(val);
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public ArrayList<Lama> getLame() {
		ArrayList<Lama> lame = new ArrayList<Lama>();
		
		try {			
			Statement stmt = Segheria.db.conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM lame;");
			
			while(rs.next()) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				
				Lama l = new Lama(rs.getString("name"));
				l.id = rs.getInt("id");
				l.maxOre = rs.getInt("maxOre");
				l.maxMetri = rs.getInt("maxMetri");
				l.oreUso = rs.getInt("totOre");
				l.metriUso = rs.getInt("totMetri");
				l.ultimaAffilatura = LocalDateTime.parse(rs.getString("lastAffilatura"), formatter);
				l.ultimoCambio = LocalDateTime.parse(rs.getString("lastCambio"), formatter);
				
				lame.add(l);
			}
			
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return lame;
	}
	
	public boolean[] checkLimits(int id) {
		boolean limitMetriReached = false;
		boolean limitOreReached = false;

		try {
			Lama lama = getLama(id);
			
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				
			Statement stmt = Segheria.db.conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT SUM(duration) AS ore FROM tagli WHERE lama_usata = " + id + " AND DATETIME(time) >= '" + lama.ultimoCambio.format(myFormatObj) + "';");
			while(rs.next()) {
				double ore = rs.getDouble("ore");

				if(((ore / 60) / 60) >= lama.maxOre) {
					limitOreReached = true;
				}
			}
			stmt.close();
			

			stmt = Segheria.db.conn.createStatement();
			rs = stmt.executeQuery("SELECT SUM(lunghezza) AS metri FROM tagli WHERE lama_usata = " + id + " AND DATETIME(time) >= '" + lama.ultimaAffilatura.format(myFormatObj) + "';");
			while(rs.next()) {
				double metri = rs.getDouble("metri");
				System.out.println(rs.getDouble("metri"));
				if((metri / 100) >= lama.maxMetri) {
					limitMetriReached = true;
				}
			}
			stmt.close();
    		
    	} catch(Exception ex) {
    		System.out.println(ex.getMessage());
    	}
		
		boolean[] data = new boolean[2];
		data[0] = limitMetriReached;
		data[1] = limitOreReached;
		return data;
	}
	
	public Lama getLama(int id) {
		Lama l = new Lama("");
		
		try {
			double totMetri = 0;
			double totOre = 0;
			
			Statement stmt = Segheria.db.conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT SUM(lunghezza) AS totMetri, SUM(duration) AS totOre FROM tagli WHERE lama_usata = '" + id + "';");
			while(rs.next()) {
				totMetri = rs.getDouble("totMetri");
				totOre = rs.getDouble("totOre");
			}
			stmt.close();
			
			
			stmt = Segheria.db.conn.createStatement();
			rs = stmt.executeQuery("SELECT * FROM lame WHERE id = " + id + ";");
			
			while(rs.next()) {
				double metri = (double)totMetri / 100;
				double ore = ((double)totOre / 60) / 60;
				
				l.name = rs.getString("name");
				l.id = rs.getInt("id");
				l.maxOre = rs.getInt("maxOre");
				l.maxMetri = rs.getInt("maxMetri");
				l.oreUso = ore;
				l.metriUso = metri;
				
				DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime ultimaAffilatura = LocalDateTime.from(f.parse(rs.getString("lastAffilatura")));
				LocalDateTime ultimoCambio = LocalDateTime.from(f.parse(rs.getString("lastCambio")));
				
				l.ultimaAffilatura = ultimaAffilatura;
				l.ultimoCambio = ultimoCambio;
			}
			
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return l;
	}
}
