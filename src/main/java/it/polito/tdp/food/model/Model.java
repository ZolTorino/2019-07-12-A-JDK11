package it.polito.tdp.food.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	public FoodDao dao;
	private SimpleWeightedGraph<Food, DefaultWeightedEdge> grafo;
	private Map<Integer, Food> idMap;
	LinkedList<Arco> archi;
	public Model() {
		dao= new FoodDao();
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		idMap=new HashMap<Integer, Food>();
		archi=new LinkedList<>();
	}
	
	public String creaGrafo(int x) {
		String s="";
		dao.listVertices(x, idMap);
		Graphs.addAllVertices(grafo, idMap.values());
		s+="Vertici: "+grafo.vertexSet().size()+"\n";
		
		dao.listEdges(x, archi, idMap);
		for(Arco a:archi)
		{
			if(grafo.containsVertex(a.getF1()) && grafo.containsVertex(a.getF2())) {
					Graphs.addEdge(grafo, a.getF1(), a.getF2(), a.getPeso());
				
			}
		}
		s+="Archi: "+grafo.edgeSet().size()+"\n";

		return s;
	}
	
	public String max5calorie() {
		String s="";
		for(int i=0;i<5;i++)
		{
			s+=archi.get(i).f1.getDisplay_name()+ " - "+archi.get(i).f2.getDisplay_name()+ " - "+archi.get(i).getPeso()+"\n";
		}
		return s;
	}
	public Set<Food> allvertici(){
		return grafo.vertexSet();
	}
	
	Food start;
	LinkedList<Cucina> cucine=new LinkedList<>();
	HashMap<Integer, Food> preparati=new HashMap<>();
	public void init(Food in, int k) {
		start= in;
		for(int i=0;i<k;i++)
		{
			cucine.add(new Cucina());
		}
		
		
	}
	public void run() {
		
	}
	
}
