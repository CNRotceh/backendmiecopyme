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
		
		for(int i = 0; i < 7; i++) {
			padre.add(new Servicio());
			padre.get(i).setIdServicio(i+1);
			padre.get(i).setTipo(i+1);
			padre.get(i).setConsumo(rand.nextDouble()*1000);
			padre.get(i).setCosto(rand.nextDouble()*100);
		}
		double[][] fitness = new double[4][2];
		List<ArrayList<Servicio>> soluciones = algor.nsga2(padre, fitness);
		int indice = 0;
		for(ArrayList<Servicio> servs: soluciones) {
			String cadena = "Solución: [";
			for(Servicio srv: servs) {
				cadena+= " ("+srv.getTipo()+","+String.format("%.2f",srv.getCosto())+","+String.format("%.2f",srv.getConsumo())+") ";
			}
			cadena += "] Costo" + fitness[indice][0] + " Huella=" + fitness[indice][1] + "\n";
			System.out.println(cadena);
			indice += 1;
		}
		//System.out.println("fitness:" + fitness[0][0]);

	}

}
