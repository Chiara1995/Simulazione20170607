package it.polito.tdp.seriea.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;
import it.polito.tdp.seriea.model.TeamIdMap;

public class SerieADAO {
	
	public List<Season> listSeasons() {
		String sql = "SELECT season, description FROM seasons" ;
		
		List<Season> result = new ArrayList<>() ;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add( new Season(res.getInt("season"), res.getString("description"))) ;
			}
			
			conn.close();
			return result ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Team> listTeams() {
		String sql = "SELECT team FROM teams" ;
		
		List<Team> result = new ArrayList<>() ;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add( new Team(res.getString("team"))) ;
			}
			
			conn.close();
			return result ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Team> getTeamsOfSeason(int season, TeamIdMap teamIdMap) {
		String sql = "SELECT DISTINCT matches.HomeTeam "+
					"FROM matches "+
					"WHERE matches.Season=? " ;
		
		List<Team> result = new ArrayList<>() ;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1	, season);
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				Team t=new Team(res.getString("HomeTeam"));
				t=teamIdMap.put(t);
				result.add( t) ;
			}
			
			conn.close();
			return result ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public int getResultMatch(int season, String home, String away, TeamIdMap teamIdMap) {
		String sql = "SELECT matches.FTR "+
					"FROM matches "+
					"WHERE matches.Season=? AND matches.HomeTeam=? AND matches.AwayTeam=? ";
		
		String result = null;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1	, season);
			st.setString(2, home);
			st.setString(3, away);
			ResultSet res = st.executeQuery() ;
			
			if(res.next()) {
				result=res.getString("FTR");
			}
			int peso=2;
			if(result.equals("H")){
				peso=1;
			}
			else if(result.equals("A")){
				peso=-1;
			}
			else if(result.equals("D")){
				peso=0;
			}
			
			
			conn.close();
			return peso ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 2 ;
		}
	}
	
	


}
