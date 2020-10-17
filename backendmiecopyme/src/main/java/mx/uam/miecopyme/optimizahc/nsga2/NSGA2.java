package mx.uam.miecopyme.optimizahc.nsga2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mx.uam.miecopyme.backendmiecopyme.negocio.modelo.Servicio;
import mx.uam.miecopyme.optimizahc.calculos.FuncionesObjetivo;
import mx.uam.miecopyme.optimizahc.db.Database;

public class NSGA2 {
	
	public static int TAMANIO_POBLACION = 100;
	public static int NUMERO_GENERACIONES = 2000;
	public static double PROBABILIDAD_CRUZA = 0.6;
	public static double PROBABILIDAD_MUTACION = 0.05;
	public static Random rand = new Random();
	
	public List<ArrayList<Servicio>> nsga2(List<Servicio> servicios) throws CloneNotSupportedException {
		int tamanioCromosoma = servicios.size();
		/*List<Servicio> serviciosAux = new ArrayList<>();
		for(Servicio serv:servicios) {
			serviciosAux.add(serv.clone());
		}*/
		//Agregar valores del cromosoma inicial a la base de datos
		double[][] fitness = new double[TAMANIO_POBLACION*2][2];
		Cromosoma[] poblacion = new Cromosoma[TAMANIO_POBLACION*2];
		int indiceAleatorio;
		int numeroPruebas = Database.consumosPrueba.length;
		for(int i = 0; i < TAMANIO_POBLACION; i++) {
			for(int j = 0; j < tamanioCromosoma; j++) {
				poblacion[i].getTipos().add(servicios.get(j).getTipo());
				indiceAleatorio = rand.nextInt()%(numeroPruebas+1);
				if(indiceAleatorio == numeroPruebas) {
					poblacion[i].getCostos().add(servicios.get(j).getCosto());
					poblacion[i].getConsumos().add(servicios.get(j).getConsumo());
				} else {
					poblacion[i].getCostos().add(Database.costosPrueba[indiceAleatorio]);
					poblacion[i].getConsumos().add(Database.costosPrueba[indiceAleatorio]);
				}
			}
			fitness[i][0] = FuncionesObjetivo.costo(poblacion[i].getCostos());
			fitness[i][1] = FuncionesObjetivo.huella(poblacion[i].getConsumos(),poblacion[i].getTipos(),tamanioCromosoma);
		}
		
		
		for(int i = 0; i < NUMERO_GENERACIONES; i++) {
			Cromosoma[] poblacionAux = seleccion(poblacion,fitness,tamanioCromosoma);
			poblacion = cruza(poblacionAux,poblacion,tamanioCromosoma);
			poblacion = mutacion(poblacion,tamanioCromosoma,servicios);
			poblacion = ordena(poblacion,fitness,tamanioCromosoma);
			//poblacion = distancias(poblacion,fitness,tamanioCromosoma);
		}
	
		return null;
	}
	
	public Cromosoma[] seleccion(Cromosoma[] poblacion, double[][] fitness, int tamanioCromosoma) {
		Cromosoma[] poblacionAux = new Cromosoma[TAMANIO_POBLACION];
		int candidato1;
		int candidato2;
		int fitnessDominante;
		for(int j = 0; j < TAMANIO_POBLACION; j++) {
			poblacionAux[j].getTipos().addAll(poblacion[j].getTipos());
			candidato1 = rand.nextInt()%TAMANIO_POBLACION;
			candidato2 = rand.nextInt()%TAMANIO_POBLACION;
			while(candidato2 == candidato1) {
				candidato2 = rand.nextInt()%TAMANIO_POBLACION;
			}
			if((fitness[candidato1][0]<=fitness[candidato2][0] && fitness[candidato1][1]<=fitness[candidato2][1]) && (fitness[candidato1][0]<fitness[candidato2][0] || fitness[candidato1][1]<fitness[candidato2][1])) {
				poblacionAux[j].getConsumos().addAll(poblacion[candidato1].getConsumos());
				poblacionAux[j].getCostos().addAll(poblacion[candidato1].getCostos());
			} else if((fitness[candidato2][0]<=fitness[candidato1][0] && fitness[candidato2][1]<=fitness[candidato1][1]) && (fitness[candidato2][0]<fitness[candidato1][0] || fitness[candidato2][1]<fitness[candidato1][1])) {
				poblacionAux[j].getConsumos().addAll(poblacion[candidato2].getConsumos());
				poblacionAux[j].getCostos().addAll(poblacion[candidato2].getCostos());
			} else {
				fitnessDominante = rand.nextInt()%2;
				if(fitness[candidato1][fitnessDominante]<fitness[candidato2][fitnessDominante]) {
					poblacionAux[j].getConsumos().addAll(poblacion[candidato1].getConsumos());
					poblacionAux[j].getCostos().addAll(poblacion[candidato1].getCostos());
				} else if(fitness[candidato2][fitnessDominante]<fitness[candidato1][fitnessDominante]) {
					poblacionAux[j].getConsumos().addAll(poblacion[candidato2].getConsumos());
					poblacionAux[j].getCostos().addAll(poblacion[candidato2].getCostos());
				} else {
					poblacionAux[j].getConsumos().addAll(poblacion[candidato1].getConsumos());
					poblacionAux[j].getCostos().addAll(poblacion[candidato1].getCostos());
				}
			}
			
		}
		return poblacionAux;
	}
	
	public Cromosoma[] cruza(Cromosoma[] poblacionAux, Cromosoma[] poblacion, int tamanioCromosoma) {
		double cruza;
		for(int i = 0; i < TAMANIO_POBLACION; i++) {
			
			poblacion[i+TAMANIO_POBLACION].getTipos().removeAll(poblacion[i+TAMANIO_POBLACION].getTipos());
			poblacion[i+TAMANIO_POBLACION].getCostos().removeAll(poblacion[i+TAMANIO_POBLACION].getCostos());
			poblacion[i+TAMANIO_POBLACION].getConsumos().removeAll(poblacion[i+TAMANIO_POBLACION].getConsumos());
			poblacion[i+1+TAMANIO_POBLACION].getTipos().removeAll(poblacion[i+1+TAMANIO_POBLACION].getTipos());
			poblacion[i+1+TAMANIO_POBLACION].getCostos().removeAll(poblacion[i+1+TAMANIO_POBLACION].getCostos());
			poblacion[i+1+TAMANIO_POBLACION].getConsumos().removeAll(poblacion[i+1+TAMANIO_POBLACION].getConsumos());
			
			poblacion[i+TAMANIO_POBLACION].getTipos().addAll(poblacionAux[i].getTipos());
			poblacion[i+1+TAMANIO_POBLACION].getTipos().addAll(poblacionAux[i].getTipos());

			for(int j = 0; j < tamanioCromosoma; j++) {
				cruza = rand.nextDouble();
				if(cruza < PROBABILIDAD_CRUZA) {
					poblacion[i+TAMANIO_POBLACION].getCostos().add(poblacionAux[i].getCostos().get(j));
					poblacion[i+TAMANIO_POBLACION].getConsumos().add(poblacionAux[i].getConsumos().get(j));
					poblacion[i+1+TAMANIO_POBLACION].getCostos().add(poblacionAux[i+1].getCostos().get(j));
					poblacion[i+1+TAMANIO_POBLACION].getConsumos().add(poblacionAux[i+1].getConsumos().get(j));
				} else {
					poblacion[i+TAMANIO_POBLACION].getCostos().add(poblacionAux[i+1].getCostos().get(j));
					poblacion[i+TAMANIO_POBLACION].getConsumos().add(poblacionAux[i+1].getConsumos().get(j));
					poblacion[i+1+TAMANIO_POBLACION].getCostos().add(poblacionAux[i].getCostos().get(j));
					poblacion[i+1+TAMANIO_POBLACION].getConsumos().add(poblacionAux[i].getConsumos().get(j));
				}
			}
			i+=1;
		}
		return poblacion;
	}
	
	public Cromosoma[] mutacion(Cromosoma[] poblacion, int tamanioCromosoma, List<Servicio> servicios) {
		double muta;
		int indiceAleatorio;
		int numeroPruebas = Database.consumosPrueba.length;
		for(int i = 0; i < TAMANIO_POBLACION*2; i++) {
			for(int j = 0; j < tamanioCromosoma; j++) {
				muta = rand.nextDouble();
				if(muta < PROBABILIDAD_MUTACION) {
					poblacion[i].getCostos().remove(j);
					poblacion[i].getConsumos().remove(j);
					indiceAleatorio = rand.nextInt()%(numeroPruebas+1);
					if(indiceAleatorio == numeroPruebas) {
						poblacion[i].getCostos().set(j,servicios.get(j).getCosto());
						poblacion[i].getConsumos().set(j,servicios.get(j).getConsumo());
					} else {
						poblacion[i].getCostos().add(j,Database.costosPrueba[indiceAleatorio]);
						poblacion[i].getConsumos().add(j,Database.costosPrueba[indiceAleatorio]);
					}
				}
			}
		}
		return poblacion;
	}
	
	public Cromosoma[] ordena(Cromosoma[] poblacion, double[][] fitness, int tamanioCromosoma) {
		ArrayList<Cromosoma> poblacionAux = new ArrayList<>();
	
		return poblacion;
	}

}
