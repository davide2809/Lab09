package it.polito.tdp.borders.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Country;

public class BordersDAO {

	public void loadAllCountries(Map<Integer,Country> idMap) {

		String sql = "SELECT ccode, StateAbb, StateNme FROM country ORDER BY StateAbb";
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
//				System.out.format("%d %s %s\n", rs.getInt("ccode"), rs.getString("StateAbb"), rs.getString("StateNme"));
				if(!idMap.containsKey(rs.getInt("ccode"))) {
					Country c=new Country(rs.getString("StateAbb"),rs.getInt("ccode"),rs.getString("StateNme"));
					idMap.put(rs.getInt("ccode"), c);
				}
			}	
			
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		return;
	}
	
	public boolean controlloAnno(int anno, Country c) {
		String sql="SELECT COUNT(*) as conto " + 
				"FROM contiguity " + 
				"WHERE (state1no= ? || state2no= ? ) && YEAR<= ? && conttype=1";
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1,c.getcCode());
			st.setInt(2,c.getcCode());
			st.setInt(3,anno);
			ResultSet rs = st.executeQuery();
			int flag=0;
			while (rs.next()) { 
				if(rs.getInt("conto")>=1) {
					flag=1;
				}
			}
			conn.close();
			if(flag==1) {
				return true;
			}
			return false;
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}

		
	}

	public List<Border> getBorder(Map<Integer,Country> idMap) {
		String sql="SELECT state1no,state2no " + 
				"FROM contiguity "+
				"WHERE conttype=1";
		List<Border> bordi=new ArrayList<Border>();
		try {Connection conn = ConnectDB.getConnection();
		PreparedStatement st = conn.prepareStatement(sql);
		ResultSet rs = st.executeQuery();
		while (rs.next()) { 
			bordi.add(new Border(idMap.get(rs.getInt("state1no")), idMap.get(rs.getInt("state2no"))));
		}
		conn.close();
		}catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		return bordi;
	}
}
