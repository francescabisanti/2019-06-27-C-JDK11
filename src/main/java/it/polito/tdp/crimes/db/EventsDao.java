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
		String sql="SELECT DISTINCT e.offense_category_id AS id "
				+ "FROM events e "
				+ "ORDER BY e.offense_category_id ASC";
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<String> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				list.add(res.getString("id"));
			
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List <Integer> getAnni(){
		String sql="SELECT DISTINCT DAY(e.reported_date) AS id "
				+ "FROM events e "
				+ "ORDER BY DAY(e.reported_date) ASC";
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Integer> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				list.add(res.getInt("id"));
			
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List <String> getVertici(String categoria, Integer giorno){
		String sql="SELECT DISTINCT e.offense_type_id AS id "
				+ "FROM events e "
				+ "WHERE DAY(e.reported_date)=? AND e.offense_category_id=? ";
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, giorno);
			st.setString(2, categoria);
			List<String> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				String tipo= res.getString("id");
				if(tipo!=null) {
					list.add(tipo);
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
	
	public List <Adiacenza> getAdiacenza(String categoria, Integer giorno){
		String sql="SELECT DISTINCT e1.offense_type_id AS id1, e2.offense_type_id AS id2, COUNT(DISTINCT(e1.precinct_id)) AS peso "
				+ "FROM events e1, events e2 "
				+ "WHERE e1.precinct_id=e2.precinct_id AND e1.offense_type_id> e2.offense_type_id AND "
				+ "DAY(e1.reported_date)=? AND e1.offense_category_id=?  AND DAY(e2.reported_date)=? AND e2.offense_category_id=? "
				+ "GROUP BY e1.offense_type_id, e2.offense_type_id";
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, giorno);
			st.setString(2, categoria);
			st.setInt(3, giorno);
			st.setString(4, categoria);
			List<Adiacenza> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				String tipo1= res.getString("id1");
				String tipo2= res.getString("id2");
				Double peso= res.getDouble("peso");
				if(tipo1!=null && tipo2!=null) {
					Adiacenza a= new Adiacenza(tipo1, tipo2, peso);
					list.add(a);
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
}
