package it.polito.tdp.porto.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.porto.db.PortoDAO;

public class Model {
	
	private Graph<Author,DefaultEdge> grafo;
	private Map<Integer, Author> mapA ;
	
	public  Model() {
		grafo= new SimpleGraph<>(DefaultEdge.class);
		mapA = new HashMap<>();
		creaGrafo();
	
	}
	
	public void creaGrafo() {
		
		PortoDAO dao = new PortoDAO();
		
		//aggiungo i veritici
		mapA = dao.getAllAutore();
		Graphs.addAllVertices(grafo, dao.getAllAutore().values());
		
		//aggiungo gli archi
		for(CoAuthor ca: dao.getCoAuthor()) {
			grafo.addEdge(mapA.get(ca.getIdA1()), mapA.get(ca.getIdA2()));
		}
		
	}

	public Graph<Author, DefaultEdge> getGrafo() {
		return grafo;
	}

	public List<Author> getAllAuthor() {
		PortoDAO dao = new PortoDAO();
		List<Author> autori = new ArrayList<Author>();
		mapA = dao.getAllAutore();
		for(Author a : mapA.values())
			autori.add(a);
		Collections.sort(autori);
		return autori;
	}
	
	public List<Author> getCoAutori(Author autore){
		
		List<Author> coAutori = new ArrayList<>();
		coAutori = Graphs.neighborListOf(grafo, autore);
		
		return coAutori;
	}

	public List<Author> getNoCoAutori(Author autore) {
		
		List<Author> noCoAutori = new ArrayList<>();
		
		List<Author> coAutori = new ArrayList<>();
		coAutori = Graphs.neighborListOf(grafo, autore);
		
		Set<Author> tuttiAutori = new HashSet<>();
		tuttiAutori = grafo.vertexSet();
		
		for(Author tmp: tuttiAutori) {
			if(!coAutori.contains(tmp))
				noCoAutori.add(tmp);
		}
		Collections.sort(noCoAutori);
		return noCoAutori;
	}
	
	public List<Paper> sequenzaArticoli(Author aPrimo, Author aSecondo){
		List<Paper> paper = new ArrayList<>();
		PortoDAO dao = new PortoDAO();
		
		DijkstraShortestPath<Author, DefaultEdge> dijkstra = new DijkstraShortestPath<Author, DefaultEdge>(this.grafo);
		GraphPath<Author, DefaultEdge> path = dijkstra.getPath(aPrimo, aSecondo);
		List<DefaultEdge> edges = path.getEdgeList();
		
		// ciascun edge corrisponderà ad un paper!
		
				for(DefaultEdge e: edges) {
					// autori che corrispondono all'edge
					Author as = grafo.getEdgeSource(e) ;
					Author at = grafo.getEdgeTarget(e) ;
					// trovo l'articolo
					Paper p = dao.getArticoloComune(as, at);
					if(p == null)
						throw new InternalError("Paper not found...") ;
					paper.add(p) ;
				}
				
		return paper ;

	}

}
