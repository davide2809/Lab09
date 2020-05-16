package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {
	
	private Graph<Country, DefaultEdge> grafo;
	private Map<Integer,Country> idMap;
	private BordersDAO dao;

	public Model() {
		idMap=new HashMap<Integer,Country>();
		dao=new BordersDAO();
		dao.loadAllCountries(idMap);
	}
	
	public void caricaGrafo(int anno) {
		this.grafo=new SimpleGraph<Country,DefaultEdge>(DefaultEdge.class);
		
//		Aggiungo i vertico con controllo
		for(Country c:idMap.values()) {
			if(dao.controlloAnno(anno,c)) {
				this.grafo.addVertex(c);
			}
		}
		for(Border b:dao.getBorder(idMap)) {
			if(this.grafo.containsVertex(b.getC1()) && this.grafo.containsVertex(b.getC2())) {
				grafo.addEdge(b.getC1(), b.getC2());
			}
		}
	}
	
	public int vertexNumber() {
		return this.grafo.vertexSet().size();
	}
	
	public int edgeNumber() {
		return this.grafo.edgeSet().size();
	}
	
	public Collection<Country> getCountry() {
		return this.grafo.vertexSet();
	}
	
	public int numeroVicini(Country country) {
		return this.grafo.degreeOf(country);
	}
	
	public List<Country> paesiRaggiungibili(Country country) {
		List<Country> raggiungibile= new ArrayList<Country>();
		
		DepthFirstIterator<Country,DefaultEdge> dfv=new DepthFirstIterator<>(grafo,country);
		while(dfv.hasNext()) {
			raggiungibile.add(dfv.next());
		}
		return raggiungibile;
	}

}
