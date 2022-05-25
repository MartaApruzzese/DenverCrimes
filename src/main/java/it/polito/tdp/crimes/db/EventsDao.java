package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.crimes.model.Coppia;
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
	
	public List<String> getVertici(String categoria, int mese){
		String sql="SELECT Distinct offense_type_id AS result "
				+ "FROM EVENTS "
				+ "WHERE MONTH(reported_date)= ? AND offense_category_id= ? ";
		
		List<String> result= new ArrayList<String>();
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, mese);
			st.setString(2, categoria);
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add(res.getString("result"));
			}
			
			
			conn.close();
			return result ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public List<Coppia> getArchi(String categoria, int mese){
		String sql="SELECT e1.offense_type_id AS tipo1, e2.offense_type_id AS tipo2, COUNT( distinct e1.neighborhood_id) AS peso "
				+ "FROM EVENTS e1, EVENTS e2 "
				+ "WHERE e1.offense_type_id>e2.offense_type_id "
				+ "AND e1.offense_category_id= ? AND e2.offense_category_id= e1.offense_category_id "
				+ "AND MONTH(e1.reported_date) = ? AND MONTH(e2.reported_date)= MONTH(e1.reported_date) "
				+ "AND e1.neighborhood_id= e2.neighborhood_id "
				+ "GROUP BY e1.offense_type_id, e2.offense_type_id ";
		List<Coppia> result= new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(2, mese);
			st.setString(1, categoria);
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add(new Coppia(res.getString("tipo1"), res.getString("tipo2"), res.getInt("peso")));
			}
			
			
			conn.close();
			return result ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
}
