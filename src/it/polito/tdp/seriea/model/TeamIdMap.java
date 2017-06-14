package it.polito.tdp.seriea.model;

import java.util.*;

public class TeamIdMap {
	private Map<String, Team> mappa;
	
	public TeamIdMap(){
		mappa=new HashMap<>();
	}
	
	public Team getTeam(int id){
		return mappa.get(id);
	}
	
	public Team put(Team r){
		Team old=mappa.get(r.getTeam());
		if(old==null){
			mappa.put(r.getTeam(),  r);
			return r;
		}
		else 
			return old;
	}


}
