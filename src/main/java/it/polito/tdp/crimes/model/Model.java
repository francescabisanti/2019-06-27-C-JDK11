package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	EventsDao dao;
	SimpleWeightedGraph<String, DefaultWeightedEdge>grafo;
	Double mediano=0.0;
	List <String> migliore;
	
	public Double getMediano() {
		return mediano;
	}

	public Model() {
		dao= new EventsDao();
		
	}
	
	public void creaGrafo(String categoria, Integer giorno) {
		grafo= new SimpleWeightedGraph< String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, dao.getVertici(categoria, giorno));
		for(Adiacenza a: dao.getAdiacenza(categoria, giorno)) {
			if(grafo.containsVertex(a.getTipo1())&& grafo.containsVertex(a.getTipo2())) {
				Graphs.addEdge(grafo, a.getTipo1(), a.getTipo2(), a.getPeso());
			}
		}
	
	}
	
	public List <DefaultWeightedEdge> puntoD(){
		List<DefaultWeightedEdge> result= new ArrayList<>();
		Double pesoMin=Double.MAX_VALUE;
		Double pesoMax=0.0;
		for(DefaultWeightedEdge e: grafo.edgeSet()) {
			if(grafo.getEdgeWeight(e)<pesoMin)
				pesoMin=grafo.getEdgeWeight(e);
			if(grafo.getEdgeWeight(e)>pesoMax)
				pesoMax= grafo.getEdgeWeight(e);
		}
		 mediano= (pesoMin+pesoMax)/2;
		for(DefaultWeightedEdge e: grafo.edgeSet()) {
			if(grafo.getEdgeWeight(e)<mediano)
				result.add(e);
			
		}
		return result;
	}
	
	public int getNVertici() {
		return grafo.vertexSet().size();
	}
	public int getNArchi() {
		return grafo.edgeSet().size();
	}
	
	public List<String> trovaPercorso(DefaultWeightedEdge e){
		migliore= new ArrayList<>();
		List<String> parziale= new ArrayList<>();
		String primo= grafo.getEdgeSource(e);
		String arrivo= grafo.getEdgeTarget(e);
		parziale.add(primo);
		cerca(parziale, arrivo);
		return migliore;
		
	}
	
	
	private void cerca(List<String> parziale, String arrivo) {
		String ultimo= parziale.get(parziale.size()-1);
		if(ultimo.equals(arrivo)) {
			if(calcolaPeso(parziale)>calcolaPeso(migliore)) {
				migliore= new ArrayList<>(parziale);
			}
			return;
		}
		for(String s: Graphs.neighborListOf(grafo, ultimo)) {
			if(!parziale.contains(s)) {
				parziale.add(s);
				cerca(parziale,arrivo);
				parziale.remove(s);
			}
		}
		
	}

	private double calcolaPeso(List<String> parziale) {
		Double peso=0.0;
		for(int i=1; i<parziale.size(); i++) {
			String a = parziale.get(i-1);
			String b=parziale.get(i);
			peso=peso+grafo.getEdgeWeight(grafo.getEdge(a, b));
		}
		return peso;
	}

	public List <String> getCategorie(){
		return dao.getCategorie();
	}
	public List <Integer> getAnni(){
		return dao.getAnni();
	}

	public EventsDao getDao() {
		return dao;
	}

	public SimpleWeightedGraph<String, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}
	
}
