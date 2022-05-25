package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.List;

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
		for(Coppia a: dao.getArchi(categoria, mese)) {
			//Graphs.addEdgeWithVertices(this.grafo, c.getV1(), c.getV2(), c.getPeso());
			Graphs.addEdgeWithVertices(this.grafo, a.getV1(), a.getV2(),a.getPeso());
		}
		
		System.out.println("Grafo creato.");
		System.out.println("#VERTICI: "+grafo.vertexSet().size());
		System.out.println("#ARCHI: "+grafo.edgeSet().size());
	}
	
	public List<Coppia> getArchiMaggioriPesoMedio(){
		//scorro gli archi per calcolare il peso medio
		double pesoTot=0.0;
		for(DefaultWeightedEdge e: this.grafo.edgeSet()) {
			pesoTot+=this.grafo.getEdgeWeight(e);
		}
		double avg=pesoTot/this.grafo.edgeSet().size();
		
		//Scorro gl archi per prendere quelli con peso maggiore di avg
		List<Coppia> result = new ArrayList<Coppia>();
		for(DefaultWeightedEdge  e: this.grafo.edgeSet()) {
			if(this.grafo.getEdgeWeight(e)>avg) {
				result.add(new Coppia(this.grafo.getEdgeSource(e), this.grafo.getEdgeTarget(e), (int)this.grafo.getEdgeWeight(e)));
				
			}
		}
		return result;
	}
	}