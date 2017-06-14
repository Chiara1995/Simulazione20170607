package it.polito.tdp.seriea.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

import it.polito.tdp.seriea.db.SerieADAO;

public class Model {

	private SerieADAO sdao;
	private List<Season> seasons;
	private List<Team> teams;
	private TeamIdMap teamIdMap;
	private DirectedWeightedMultigraph<Team,DefaultWeightedEdge> grafo;
	
	public Model(){
		sdao=new SerieADAO();
		teamIdMap=new TeamIdMap();
	}
	
	public List<Season> getSeasons(){
		if(seasons==null){
			seasons=sdao.listSeasons();
		}
		return seasons;
	}
	
	public List<Team> getTeams(int season){
		if(teams==null){
			teams=sdao.getTeamsOfSeason(season, teamIdMap);
		}
		return teams;
	}
	
	public DirectedWeightedMultigraph<Team,DefaultWeightedEdge> getGrafo(int season){
		if(grafo==null){
			this.creaGrafo(season);
		}
		return grafo;
	}
	
	public void creaGrafo(int season){
		grafo=new DirectedWeightedMultigraph<Team,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		teams=null;
		//aggiungo vertici
		Graphs.addAllVertices(grafo, this.getTeams(season));
		//aggiungo archi
		for(Team t : grafo.vertexSet()){			
			for(Team t2 : grafo.vertexSet()){
				if(!t.equals(t2)){
					int peso=sdao.getResultMatch(season, t.getTeam(), t2.getTeam(), teamIdMap);
					if(!t.equals(t2)){
						DefaultWeightedEdge e=grafo.addEdge(t, t2);
						grafo.setEdgeWeight(e, peso);
					}
				}
			}
		}
	}
	
	public List<Punteggio> getClassifica(int season){
		List<Punteggio> punteggi=new ArrayList<Punteggio>();
		for(Team t : this.getGrafo(season).vertexSet()){
			int p=0;
			//archi uscenti
			for(DefaultWeightedEdge e : this.getGrafo(season).outgoingEdgesOf(t)){
				if(this.getGrafo(season).getEdgeWeight(e)==0){
					p++;
				}
				else if(this.getGrafo(season).getEdgeWeight(e)==1){
					p+=3;
				}
			}
			//archi entranti
			for(DefaultWeightedEdge e : this.getGrafo(season).incomingEdgesOf(t)){
				if(this.getGrafo(season).getEdgeWeight(e)==0){
					p++;
				}
				else if(this.getGrafo(season).getEdgeWeight(e)==-1){
					p+=3;
				}
			}
			Punteggio punteggio=new Punteggio(t, p);
			punteggi.add(punteggio);
		}		
		Collections.sort(punteggi, new Comparator<Punteggio>(){
			public int compare(Punteggio l1, Punteggio l2){
				return l2.getPunteggio()-l1.getPunteggio();
			}
		});
		return punteggi;	
	}
	
	//Esercizio 2
	
	private List<DefaultWeightedEdge> cammino;
	private List<Team> camminoTeam;
	
	public List<Team> risolvi(int season){
		List<DefaultWeightedEdge> parziale=new ArrayList<>();
		cammino=new ArrayList<>();
		camminoTeam=new ArrayList<>();
		//Elimino archi con peso pari a 0 o -1 da una copia del grafo
		DirectedWeightedMultigraph<Team,DefaultWeightedEdge> graph=this.getGrafo(season);
		List<DefaultWeightedEdge> list=new ArrayList<>();
		for(DefaultWeightedEdge t : graph.edgeSet()){			
			if(graph.getEdgeWeight(t)==0 || graph.getEdgeWeight(t)==-1){
				list.add(t);
			}
		}
		for(DefaultWeightedEdge d : list){
			graph.removeEdge(d);
		}
		scegli(parziale, 0, season, graph);
		for(DefaultWeightedEdge de : cammino){
			if(camminoTeam.size()==0){
				camminoTeam.add(graph.getEdgeSource(de));
			}
			camminoTeam.add(graph.getEdgeTarget(de));
		}
		return camminoTeam;

	}
	
	private void scegli(List<DefaultWeightedEdge> parziale, int livello, int season, DirectedWeightedMultigraph<Team,DefaultWeightedEdge> graph){
		//Condizione di ottimalità
		System.out.println("Entro in un scegli livello "+livello+"\n");
		if(parziale.size()>cammino.size()){
//			System.out.println("controllo ottimalità "+parziale+"\n");
			cammino.clear();
			cammino.addAll(parziale);
		}
		//Generazione di una nuova soluzione
		for(DefaultWeightedEdge de : graph.edgeSet()){
			//Se parziale è vuoto aggiungo un arco
			if(parziale.size()==0){
				System.out.println("Caso parziale vuoto\n");
				parziale.add(de);
				scegli(parziale, livello+1, season, graph);
				parziale.remove(de);
			}
			else{
				for(DefaultWeightedEdge d : graph.outgoingEdgesOf(graph.getEdgeTarget((parziale.get(parziale.size()-1))))){
					//archi uscenti dal nodo finale dell'ultimo arco
					if(!parziale.contains(d)){
						parziale.add(d);
						scegli(parziale, livello+1, season, graph);
						parziale.remove(d);
					}	
				}
			}
		}

	}
	
	
	
	
	
}
