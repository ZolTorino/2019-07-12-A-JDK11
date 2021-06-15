package it.polito.tdp.food.model;


public class Cucina implements Comparable<Cucina> {
	Food f;
	Double t;
	@Override
	public int compareTo(Cucina other) {
		return this.t.compareTo(other.t);
	}
	public Cucina(Food f, Double t) {
		super();
		this.f = f;
		this.t = t;
	}
	
}
