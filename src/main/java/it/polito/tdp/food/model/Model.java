package it.polito.tdp.food.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
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
	Double tempotot;
	private PriorityQueue<Cucina> queue;
	public void init(Food in, int k) {
		start= in;
		if(prossimi(in)!=null) {
			LinkedList<Arco> lista= new LinkedList<>(prossimi(in));
			this.queue=new PriorityQueue<Cucina>();
			for(int i=0;i<k&&i<lista.size();i++)
			{
				queue.add(new Cucina(lista.get(i).f2,lista.get(i).peso));
				System.out.println("Adiacenti al primo: "+ lista.get(i).f2.getDisplay_name()+"\n");
			}
		}
		System.out.println("Dimensione coda: "+queue.size());
	}
	public String run(int k) {
		while(!this.queue.isEmpty())
		{
			Cucina c=this.queue.poll();
			preparati.put(c.f.getFood_code(),c.f);
			LinkedList<Arco> next= new LinkedList<>(prossimi(c.f));
			for(Arco a:next)
			{
				if(!preparati.containsValue(a.f2))
						{
							double start=c.t;
							c.f=a.f2;
							c.t=a.peso+start;
							tempotot=c.t;
							queue.add(c);
							break;
						}
				
			}
			
		}
		return "Praparati "+ preparati.size()+" cibi in "+tempotot+ " min";
	}
	
	public List<Arco> prossimi(Food p){
		LinkedList<Arco> lista= new LinkedList<>();
		for(Food f:Graphs.neighborListOf(grafo, p))
		{
			lista.add(new Arco(p,f,grafo.getEdgeWeight(grafo.getEdge(p, f))));
		}
		Collections.sort(lista, (a1,a2)->a1.peso.compareTo(a2.peso));
		
		return lista;
		
	}
	
}
