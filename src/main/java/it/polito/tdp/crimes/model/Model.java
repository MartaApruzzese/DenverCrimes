package it.polito.tdp.crimes.model;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	private Graph<String, DefaultWeightedEdge> grafo;
	private EventsDao dao;
	
	
	public Model() {
		dao= new EventsDao();
	}
	
	public void creaGrafo(String categoria, int mese) {
		this.grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//Aggiungo i vertici
		Graphs.addAllVertices(this.grafo, dao.getVertici(categoria, mese));
		
		//Aggiungo gli archi
		
	}
}
