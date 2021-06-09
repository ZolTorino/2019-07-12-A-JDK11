package it.polito.tdp.food.model;


public class Cucina implements Comparable<Cucina> {
	Food f;
	int t;
	@Override
	public int compareTo(Cucina other) {
		return this.t-other.t;
	}
}
