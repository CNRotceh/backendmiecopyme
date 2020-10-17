package mx.uam.miecopyme.optimizahc.nsga2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mx.uam.miecopyme.backendmiecopyme.negocio.modelo.Servicio;

public class Ejemplo {

	public static void main(String[] args) {
		ArrayList<Servicio> padre = new ArrayList<Servicio>();
		Random rand = new Random();
		NSGA2 algor = new NSGA2();
		
		for(int i = 0; i < 6; i++) {
			padre.add(new Servicio());
			padre.get(i).setIdServicio(i+1);
			padre.get(i).setTipo(i+1);
			padre.get(i).setConsumo(rand.nextDouble()*1000);
			padre.get(i).setCosto(rand.nextDouble()*100);
		}
		double[][] fitness = new double[4][2];
		List<ArrayList<Servicio>> soluciones = algor.nsga2(padre, fitness);
		//System.out.println("fitness:" + fitness[0][0]);

	}

}
