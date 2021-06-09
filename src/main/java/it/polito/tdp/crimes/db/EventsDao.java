package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Event;


public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List <String> getCategorie(){
		List <String> categorie= new ArrayList <>();
		String sql="SELECT DISTINCT  e.offense_category_id AS c "
				+ "FROM EVENTS e "
				+ "ORDER BY c ASC";
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				categorie.add(res.getString("c"));
			}
			
			conn.close();
			return categorie ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	
	public List <Integer> getGiorno(){
		List <Integer> giorni= new ArrayList <>();
		String sql="SELECT DISTINCT  DAY(e.reported_date) AS c "
				+ "FROM EVENTS e "
				+ "ORDER BY c ASC";
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				giorni.add(res.getInt("c"));
			}
			
			conn.close();
			return giorni ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	public List <String> getVertici(String categoria, int anno){
		List <String> vertici= new ArrayList <>();
		String sql="SELECT DISTINCT  e.offense_type_id AS c "
				+ "FROM EVENTS e "
				+ "WHERE e.offense_category_id=?  AND DAY(e.reported_date)=? "
				+ "ORDER BY c ASC";
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setString(1, categoria);
			st.setInt(2, anno);
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				vertici.add(res.getString("c"));
			}
			
			conn.close();
			return vertici ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List <Adiacenza> getAdiacenza(String categoria, int anno){
		List <Adiacenza> adiacenze= new ArrayList <>();
		String sql="SELECT e1.offense_type_id as t1, e2.offense_type_id as t2, COUNT(DISTINCT e1.precinct_id) AS peso "
				+ "FROM EVENTS e1, EVENTS e2\r\n"
				+ "WHERE e1.offense_type_id> e2.offense_type_id AND e1.precinct_id=e2.precinct_id  "
				+ "AND DAY(e1.reported_date)=? AND DAY(e1.reported_date)=DAY(e2.reported_date) "
				+ "AND e1.offense_category_id= ? AND e1.offense_category_id=e2.offense_category_id "
				+ "GROUP BY  e1.offense_type_id, e2.offense_type_id \r\n"
				+ "HAVING peso >0";
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setString(2, categoria);
			st.setInt(1, anno);
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				Adiacenza a= new Adiacenza(res.getString("t1"), res.getString("t2"), res.getDouble("peso"));
				adiacenze.add(a);
			}
			
			conn.close();
			return adiacenze ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

}
