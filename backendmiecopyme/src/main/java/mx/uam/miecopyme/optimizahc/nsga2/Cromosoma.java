package mx.uam.miecopyme.optimizahc.nsga2;

import java.util.ArrayList;

public class Cromosoma {
	
	private ArrayList<Integer> tipos = new ArrayList<>();
	
	private ArrayList<Double> costos = new ArrayList<>();
	
	private ArrayList<Double> consumos = new ArrayList<>();

	public void setTipos(ArrayList<Integer> tip) {
		this.tipos = tip;
	}
	
	public void setCostos(ArrayList<Double> cost) {
		this.costos = cost;
	}
	
	public void setConsumos(ArrayList<Double> cons) {
		this.consumos = cons;
	}
	
	public ArrayList<Integer> getTipos() {
		return tipos;
	}
	
	public ArrayList<Double> getCostos() {
		return costos;
	}
	
	public ArrayList<Double> getConsumos() {
		return consumos;
	}
}
