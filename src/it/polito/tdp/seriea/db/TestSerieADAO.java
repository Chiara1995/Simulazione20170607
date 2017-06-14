package it.polito.tdp.seriea.db;

import java.util.List;

import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;
import it.polito.tdp.seriea.model.TeamIdMap;

public class TestSerieADAO {

	public static void main(String[] args) {
		SerieADAO dao = new SerieADAO() ;
		
		List<Season> seasons = dao.listSeasons() ;
		System.out.println(seasons);
		
		List<Team> teams = dao.listTeams() ;
		System.out.println(teams);
		TeamIdMap teamIdMap=new TeamIdMap();
		System.out.println(dao.getTeamsOfSeason(2003, teamIdMap).size());
		
		System.out.println(dao.getResultMatch(2003, "Roma", "Bologna", teamIdMap));
		System.out.println(dao.getResultMatch(2003, "Inter", "Roma", teamIdMap));
		System.out.println(dao.getResultMatch(2003, "Inter", "Milan", teamIdMap));


	}

}
