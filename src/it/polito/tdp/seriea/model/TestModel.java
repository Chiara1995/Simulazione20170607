package it.polito.tdp.seriea.model;

public class TestModel {
	
	public static void main(String[] args) {
		Model m=new Model();
		System.out.println(m.getSeasons());
		System.out.println(m.getTeams(2003));
		System.out.println(m.getGrafo(2003).vertexSet().size());
		System.out.println(m.getGrafo(2003).edgeSet().size());
		System.out.println(m.getClassifica(2003));
		System.out.println(m.risolvi(2003));
	}

}
