package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	EventsDao dao;
	SimpleWeightedGraph <String, DefaultWeightedEdge> grafo;
	List<String> migliore;
	
	public Model() {
		dao= new EventsDao();
	}
	
	public List <String> getCategorie(){
		return dao.getCategorie();
	}
	
	public List <Integer> getGiorno(){
		return dao.getGiorno();
	}
	
	public void creaGrafo(String categoria, int giorno) {
		grafo= new SimpleWeightedGraph <String, DefaultWeightedEdge> (DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, dao.getVertici(categoria, giorno));
		for(Adiacenza a: dao.getAdiacenza(categoria, giorno)) {
			Graphs.addEdgeWithVertices(this.grafo, a.getT1(), a.getT2(), a.getPeso());
		}
	
	}
	
	public double pesoMedio() {
		double pesoMedio=0.0;
		double pesoMax=Integer.MIN_VALUE;
		double pesoMin=Integer.MAX_VALUE;
		for(DefaultWeightedEdge e: this.grafo.edgeSet()) {
			if(grafo.getEdgeWeight(e)>pesoMax)
				pesoMax=grafo.getEdgeWeight(e);
			if(grafo.getEdgeWeight(e)<pesoMin)
				pesoMin=grafo.getEdgeWeight(e);
		}
		pesoMedio=(pesoMax+pesoMin)/2;
		return pesoMedio;
	}
	
	public List <Adiacenza> puntoD(String categoria, int anno){
		List <Adiacenza> result= new ArrayList<>();
		for (Adiacenza e: this.dao.getAdiacenza(categoria, anno)) {
			if(e.getPeso()<this.pesoMedio())
				result.add(e);
		}
		return result;
	}
	
	
	public List<String>trovaPercorso(String t1, String t2){
		this.migliore=new ArrayList<>();
		List <String> parziale= new ArrayList <>();
		parziale.add(t1);
		cerca(parziale,t2);
		return migliore;
		
	}
	
	private void cerca(List<String> parziale, String t2) {
		//caso terminale
			if(parziale.get(parziale.size()-1).equals(t2)) {
				if(parziale.size()>migliore.size()) {
					migliore= new ArrayList<>(parziale);
					
				}
				return;
			}
			List <String> vicini= Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1));
			for(String v: vicini) {
				if(!parziale.contains(v)) {
					parziale.add(v);
					cerca(parziale, t2);
					parziale.remove(parziale.get(parziale.size()-1));
					
				}
			}
		
	}

	public int getNvertici() {
		return grafo.vertexSet().size();
	}
	
	public int getNArchi() {
		return grafo.edgeSet().size();
	}
}
