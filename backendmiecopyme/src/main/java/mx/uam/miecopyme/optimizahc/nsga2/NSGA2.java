package mx.uam.miecopyme.optimizahc.nsga2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mx.uam.miecopyme.backendmiecopyme.negocio.modelo.Servicio;
import mx.uam.miecopyme.optimizahc.calculos.FuncionesObjetivo;
import mx.uam.miecopyme.optimizahc.db.Database;

public class NSGA2 {
	
	public static int TAMANIO_POBLACION = 50;
	public static int NUMERO_GENERACIONES = 100;
	public static double PROBABILIDAD_CRUZA = 0.6;
	public static double PROBABILIDAD_MUTACION = 0.05;
	public static double INFINITO = 999999999;
	public ArrayList<Integer> tiposGlobales = new ArrayList<Integer>();
	public static Random rand = new Random();
	
	public List<ArrayList<Servicio>> nsga2(List<Servicio> servicios, double[][] fit) {
		Cromosoma primero = new Cromosoma();
		int tamanioCromosoma = servicios.size();
		String cadenaInicia = "Cromosoma inicial: [";
		List<Double> costos = new ArrayList<Double>();
		List<Double> consumos = new ArrayList<Double>();
		//double[] fitInicial = new double[2];
		for(Servicio serv:servicios) {
			costos.add(serv.getCosto());
			consumos.add(serv.getConsumo());
			tiposGlobales.add(serv.getTipo());
			cadenaInicia += " ("+serv.getTipo()+","+String.format("%.2f",serv.getCosto())+"," +String.format("%.2f",serv.getConsumo())+") ";
		}
		fit[0][0] = FuncionesObjetivo.costo(costos);
		fit[0][1] = FuncionesObjetivo.huella(consumos,tiposGlobales,tamanioCromosoma);
		cadenaInicia += "]\nCosto: $"+ fit[0][0] + ", Huella: " + fit[0][1] + "tCO2\n";
		
		System.out.println(cadenaInicia);
		
		
		//Agregar valores del cromosoma inicial a la base de datos
		double[][] fitness = new double[TAMANIO_POBLACION*2][2];
		Cromosoma[] poblacion = new Cromosoma[TAMANIO_POBLACION*2];
		int indiceAleatorio;
		int numeroPruebas = Database.consumosPrueba.length;
		for(int i = 0; i < TAMANIO_POBLACION; i++) {
			poblacion[i] = new Cromosoma();
			for(int j = 0; j < tamanioCromosoma; j++) {
				poblacion[i].getTipos().add(servicios.get(j).getTipo());
				indiceAleatorio = Math.abs(rand.nextInt()%(numeroPruebas+1));
				if(indiceAleatorio == numeroPruebas) {
					poblacion[i].getCostos().add(servicios.get(j).getCosto());
					poblacion[i].getConsumos().add(servicios.get(j).getConsumo());
				} else {
					poblacion[i].getCostos().add(Database.costosPrueba[indiceAleatorio]);
					poblacion[i].getConsumos().add(Database.consumosPrueba[indiceAleatorio]);
				}
			}
			fitness[i][0] = FuncionesObjetivo.costo(poblacion[i].getCostos());
			fitness[i][1] = FuncionesObjetivo.huella(poblacion[i].getConsumos(),tiposGlobales,tamanioCromosoma);
		}
		
		for(int gens = 0; gens < NUMERO_GENERACIONES; gens++) {
			//System.out.println("Generación: " + (gens+1));
			Cromosoma[] poblacionAux = seleccion(poblacion,fitness,tamanioCromosoma);
			poblacion = cruza(poblacionAux,poblacion,tamanioCromosoma);
			//System.out.println("Cruza Hecha");
			poblacion = mutacion(poblacion,fitness,tamanioCromosoma,servicios);
			for(int j = 0; j < TAMANIO_POBLACION*2; j++) {
				fitness[j][0] = FuncionesObjetivo.costo(poblacion[j].getCostos());
				fitness[j][1] = FuncionesObjetivo.huella(poblacion[j].getConsumos(),tiposGlobales,tamanioCromosoma);
			}
			//System.out.println("Mutación Hecha");
			poblacion = ordena(poblacion,fitness,tamanioCromosoma);
			//System.out.println("Nueva población: " + poblacion[0].getConsumos() + ", " + poblacion[0].getCostos());
			
		}
		for(int j = 0; j < TAMANIO_POBLACION; j++) {
			fitness[j][0] = FuncionesObjetivo.costo(poblacion[j].getCostos());
			fitness[j][1] = FuncionesObjetivo.huella(poblacion[j].getConsumos(),tiposGlobales,tamanioCromosoma);
		}
		List<ArrayList<Servicio>> alternativas = new ArrayList<ArrayList<Servicio>>();
		alternativas.add(new ArrayList<Servicio>());
		alternativas.get(0).addAll(servicios);
		for(int i = 0; i < 3; i++) {
			alternativas.add(new ArrayList<Servicio>());
			String cadena = "Solucion " + (i+1) + ": [";
			for(int j = 0; j < tamanioCromosoma; j++) {
				alternativas.get(i).add(new Servicio());
				alternativas.get(i).get(j).setTipo(servicios.get(j).getTipo());
				alternativas.get(i).get(j).setCosto(poblacion[i].getCostos().get(j));
				alternativas.get(i).get(j).setConsumo(poblacion[i].getConsumos().get(j));
				alternativas.get(i).get(j).setIdServicio(servicios.get(j).getIdServicio());
				cadena += " ("+alternativas.get(i).get(j).getTipo()+","+String.format("%.2f",alternativas.get(i).get(j).getCosto())+"," +String.format("%.2f",alternativas.get(i).get(j).getConsumo())+") ";
			}
			fit[i+1][0] = fitness[i][0];
			fit[i+1][1] = fitness[i][1];
			cadena += "]\nCosto: $"+ fitness[i][0] + ", Huella: " + fitness[i][1] + "tCO2\n";
			System.out.println(cadena);
		}
	
		return alternativas;
	}
	
	public Cromosoma[] seleccion(Cromosoma[] poblacion, double[][] fitness, int tamanioCromosoma) {
		int[] indices = new int[TAMANIO_POBLACION];
		for(int i = 0; i < TAMANIO_POBLACION; i++) {
			indices[i] = i;
		}
		Cromosoma[] poblacionAux = new Cromosoma[TAMANIO_POBLACION];
		int candidato1;
		int indice1;
		int candidato2;
		int indice2;
		int fitnessDominante;
		int aux;
		int barajas;
		//Barajeo
		barajas = (rand.nextInt()%980)+20;
		for(int j = 0; j < barajas; j++) {
			indice1 = Math.abs(rand.nextInt()%TAMANIO_POBLACION);
			indice2 = Math.abs(rand.nextInt()%TAMANIO_POBLACION);
			while(indice2 == indice1) {
				indice2 = Math.abs(rand.nextInt()%TAMANIO_POBLACION);
			}
			aux=indices[indice1];
			indices[indice1] = indices[indice2];
			indices[indice2] = aux;
		}
		//Torneo
		for(int j = 0; j < TAMANIO_POBLACION; j++) {
			candidato1=j;
			candidato2=indices[j];
			poblacionAux[j] = new Cromosoma();
			if((fitness[candidato1][0]<=fitness[candidato2][0] && fitness[candidato1][1]<=fitness[candidato2][1]) && (fitness[candidato1][0]<fitness[candidato2][0] || fitness[candidato1][1]<fitness[candidato2][1])) {
				poblacionAux[j].getConsumos().addAll(poblacion[candidato1].getConsumos());
				poblacionAux[j].getCostos().addAll(poblacion[candidato1].getCostos());
			} else if((fitness[candidato2][0]<=fitness[candidato1][0] && fitness[candidato2][1]<=fitness[candidato1][1]) && (fitness[candidato2][0]<fitness[candidato1][0] || fitness[candidato2][1]<fitness[candidato1][1])) {
				poblacionAux[j].getConsumos().addAll(poblacion[candidato2].getConsumos());
				poblacionAux[j].getCostos().addAll(poblacion[candidato2].getCostos());
			} else {
				fitnessDominante = Math.abs(rand.nextInt()%2);
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
			poblacion[i+TAMANIO_POBLACION] = new Cromosoma();
			poblacion[i+1+TAMANIO_POBLACION] = new Cromosoma();
			poblacion[i+TAMANIO_POBLACION].getTipos().addAll(tiposGlobales);
			poblacion[i+1+TAMANIO_POBLACION].getTipos().addAll(tiposGlobales);

			for(int j = 0; j < tamanioCromosoma; j++) {
				cruza = rand.nextDouble();
				if(cruza < PROBABILIDAD_CRUZA) {
					poblacion[i+TAMANIO_POBLACION].getCostos().add(poblacionAux[i+1].getCostos().get(j));
					poblacion[i+TAMANIO_POBLACION].getConsumos().add(poblacionAux[i+1].getConsumos().get(j));
					poblacion[i+1+TAMANIO_POBLACION].getCostos().add(poblacionAux[i].getCostos().get(j));
					poblacion[i+1+TAMANIO_POBLACION].getConsumos().add(poblacionAux[i].getConsumos().get(j));
				} else {
					poblacion[i+TAMANIO_POBLACION].getCostos().add(poblacionAux[i].getCostos().get(j));
					poblacion[i+TAMANIO_POBLACION].getConsumos().add(poblacionAux[i].getConsumos().get(j));
					poblacion[i+1+TAMANIO_POBLACION].getCostos().add(poblacionAux[i+1].getCostos().get(j));
					poblacion[i+1+TAMANIO_POBLACION].getConsumos().add(poblacionAux[i+1].getConsumos().get(j));
				}
			}
			i+=1;
		}
		return poblacion;
	}
	
	public Cromosoma[] mutacion(Cromosoma[] poblacion, double[][] fitness, int tamanioCromosoma, List<Servicio> servicios) {
		double muta;
		int indiceAleatorio;
		int numeroPruebas = Database.consumosPrueba.length;
		for(int i = TAMANIO_POBLACION; i < TAMANIO_POBLACION*2; i++) {
			for(int j = 0; j < tamanioCromosoma; j++) {
				muta = rand.nextDouble();
				if(muta < PROBABILIDAD_MUTACION) {
					indiceAleatorio = Math.abs(rand.nextInt()%(numeroPruebas+1));
					if(indiceAleatorio == numeroPruebas) {
						poblacion[i].getCostos().set(j,servicios.get(j).getCosto());
						poblacion[i].getConsumos().set(j,servicios.get(j).getConsumo());
					} else {
						poblacion[i].getCostos().set(j,Database.costosPrueba[indiceAleatorio]);
						poblacion[i].getConsumos().set(j,Database.consumosPrueba[indiceAleatorio]);
					}
				}
			}
			fitness[i][0] = FuncionesObjetivo.costo(poblacion[i].getCostos());
			fitness[i][1] = FuncionesObjetivo.huella(poblacion[i].getConsumos(),tiposGlobales,tamanioCromosoma);
		}
		return poblacion;
	}
	
	public Cromosoma[] ordena(Cromosoma[] poblacion, double[][] fitness, int tamanioCromosoma) {
		ArrayList<Integer> dominantes = new ArrayList<Integer>();
		ArrayList<ArrayList<Integer>> frentes = new ArrayList<ArrayList<Integer>>();
		int tamanioFrente;
		int indiceDomina;
		boolean salto;
		int tamanioTotal = 0;
		for(int i = 0; i < TAMANIO_POBLACION*2; i++) {
			ArrayList<Integer> frenteAux = new ArrayList<Integer>();
			salto =false;
			indiceDomina=i;
			//Revisa si el índice actual ya está en algún frente
			for(int j = 0; j < frentes.size(); j++) {
				//System.out.println("Tamanio del frente actual:" + frentes.get(j).size() + ": " + frentes.get(j));
				//for(int k = 0; k < frentes.get(j).size(); j++) {
				for(int ind: frentes.get(j)) {
					//System.out.println("Actual: " + ind);
					if(i == ind) {
						salto = true;
						break;
					}
				}
				if(salto) {
					break;
				}
			}
			//Si no está, empieza a compararse con los demás
			if(!salto) {
				for(int j = i+1; j < TAMANIO_POBLACION*2; j++) {
					//Revisa que el índice con el que está comparando no esté ya en la lista de frentes
					salto = false;
					for(int k = 0; k < frentes.size(); k++) {
						for(int l = 0; l < frentes.get(k).size(); l++) {
							if(j == frentes.get(k).get(l)) {
								salto = true;
								break;
							}
						}
						if(salto) {
							break;
						}
					}
					//Si no está, revisa quién domina a quien
					if(!salto) {
						if((fitness[i][0]<=fitness[j][0] && fitness[i][1]<=fitness[j][1]) && (fitness[i][0]<fitness[j][0] || fitness[i][1]<fitness[j][1])) {
							indiceDomina=i;
						} else if((fitness[j][0]<=fitness[i][0] && fitness[j][1]<=fitness[i][1]) && (fitness[j][0]<fitness[i][0] || fitness[j][1]<fitness[i][1])) {
							indiceDomina=j;
						} else {
							//Si no existe un dominio, se conserva el actual y se agrega al frente el otro
							indiceDomina=i;
							frenteAux.add(0,j);
						}
					}
				}
				//Revisa si entre los elementos del frente hay algún dominado y se retira
				frenteAux.add(indiceDomina);
				tamanioFrente = frenteAux.size();
				if(tamanioFrente > 1) {
					for(int j = 0; j < tamanioFrente; j++) {
						for(int k = j+1; k < tamanioFrente; k++) {
							if((fitness[frenteAux.get(j)][0]<=fitness[frenteAux.get(k)][0] && fitness[frenteAux.get(j)][1]<=fitness[frenteAux.get(k)][1]) && (fitness[frenteAux.get(j)][0]<fitness[frenteAux.get(k)][0] || fitness[frenteAux.get(j)][1]<fitness[frenteAux.get(k)][1])) {
								frenteAux.remove(k);
								k-=1;
								tamanioFrente-=1;
							} else if((fitness[frenteAux.get(j)][0]<=fitness[i][0] && fitness[j][1]<=fitness[i][1]) && (fitness[j][0]<fitness[i][0] || fitness[j][1]<fitness[i][1])) {
								frenteAux.remove(j);
								j-=1;
								tamanioFrente-=1;
								break;
							}
						}
					}
				}
				//Se agrega frenteAux
				frentes.add(new ArrayList<Integer>());
				frentes.get(frentes.size()-1).addAll(frenteAux);
				tamanioTotal+=frentes.get(frentes.size()-1).size();
				if(tamanioTotal >= TAMANIO_POBLACION) {
					break;
				}
			}
		}
		Cromosoma[] poblacionAux = new Cromosoma[TAMANIO_POBLACION*2];
		//Se agregan a la población
		if(tamanioTotal > TAMANIO_POBLACION) {
			//System.out.println("Tamaño sobrepasado: " + tamanioTotal + ", calcular distancias");
			poblacionAux = distancias(poblacion, fitness, frentes);
			//System.out.println("Distancias hechas");
		} else {
			int indice=0;
			for(int i = 0; i < frentes.size(); i++) {
				for(int j = 0; j < frentes.get(i).size(); j++) {
					poblacionAux[indice] = new Cromosoma();
					poblacionAux[indice].getTipos().addAll(tiposGlobales);
					poblacionAux[indice].getCostos().addAll(poblacion[frentes.get(i).get(j)].getCostos());
					poblacionAux[indice].getConsumos().addAll(poblacion[frentes.get(i).get(j)].getConsumos());
					indice+=1;
				}
			}
		}
		return poblacionAux;
	}


	public Cromosoma[] distancias(Cromosoma[] poblacion, double[][] fitness, ArrayList<ArrayList<Integer>> frentes) {
		double minimoCosto = fitness[0][0];
		double maximoCosto = fitness[0][0];
		double minimoConsumo = fitness[0][1];
		double maximoConsumo = fitness[0][1];
		double distMinMaxCosto;
		double distMinMaxConsumo;
		for(int i = 0; i < fitness.length; i++) {
			if(fitness[i][0] < minimoCosto) {
				minimoCosto = fitness[i][0];
			}
			if(fitness[i][0] > maximoCosto) {
				maximoCosto = fitness[i][0];
			}
			if(fitness[i][1] < minimoConsumo) {
				minimoConsumo = fitness[i][0];
			}
			if(fitness[i][1] > maximoConsumo) {
				maximoConsumo = fitness[i][0];
			}
		}	
		distMinMaxCosto = maximoCosto-minimoCosto;
		distMinMaxConsumo = maximoConsumo-minimoConsumo;
		int tamanioFrente = frentes.get(frentes.size()-1).size();
		double[][] ordenaCosto = new double[tamanioFrente][2];
		double[][] ordenaConsumo = new double[tamanioFrente][2];
		double[][] distancias = new double[tamanioFrente][2];
		for(int i = 0; i < tamanioFrente; i++) {
			//Indice del fitness
			ordenaCosto[i][0] = frentes.get(frentes.size()-1).get(i);
			//Valor del fitness
			ordenaCosto[i][1] = fitness[frentes.get(frentes.size()-1).get(i)][0];
			ordenaCosto[i][0] = frentes.get(frentes.size()-1).get(i);
			ordenaConsumo[i][1] = fitness[frentes.get(frentes.size()-1).get(i)][1];
			distancias[i][0] = frentes.get(frentes.size()-1).get(i);
			distancias[i][1] = 0;
		}
		
		double fitAux;
		double indiceAux;
		int indice;
		//Se ordenan ambas listas
		for(int i = 1; i < tamanioFrente; i++) {
			indiceAux = ordenaCosto[i][0];
			fitAux = ordenaCosto[i][1];
			indice = i;
			while(indice > 0 && ordenaCosto[indice-1][1] > fitAux) {
				ordenaCosto[indice][0]=ordenaCosto[indice-1][0];
				ordenaCosto[indice][1]=ordenaCosto[indice-1][1];
				indice-=1;
			}
			ordenaCosto[indice][0]=indiceAux;
			ordenaCosto[indice][1]=fitAux;
		}
		for(int i = 1; i < tamanioFrente; i++) {
			indiceAux = ordenaConsumo[i][0];
			fitAux = ordenaConsumo[i][1];
			indice = i;
			while(indice > 0 && ordenaConsumo[indice-1][1] > fitAux) {
				ordenaConsumo[indice][0]=ordenaConsumo[indice-1][0];
				ordenaConsumo[indice][1]=ordenaConsumo[indice-1][1];
				indice-=1;
			}
			ordenaConsumo[indice][0]=indiceAux;
			ordenaConsumo[indice][1]=fitAux;
		}
		
		double minimoCostoFrente = ordenaCosto[0][1];
		double minimoConsumoFrente = ordenaConsumo[0][1];
		
		for(int i = 0; i < tamanioFrente; i++) {
			//Busca en las listas ordenadas el valor del fitness que necesita
			for(int j = 0; j < tamanioFrente; j++) {
				if(distancias[i][0] == ordenaCosto[j][0]) {
					if(j > 0 && ordenaCosto[j][1] != minimoCostoFrente) {
						distancias[i][1] += ((ordenaCosto[j][1]-ordenaCosto[j-1][1])/distMinMaxCosto);
					} else {
						distancias[i][1] = INFINITO;
					}
				}
			}
			for(int j = 0; j < tamanioFrente; j++) {
				if(distancias[i][0] == ordenaConsumo[j][0]) {
					if(j > 0 && ordenaConsumo[j][1] != minimoConsumoFrente) {
						distancias[i][1] += ((ordenaConsumo[j][1]-ordenaConsumo[j-1][1])/distMinMaxConsumo);
					} else {
						distancias[i][1] = INFINITO;
					}
				}
			}
		}
		double distAux;
		//Se ordenan las distancias
		for(int i = 1; i < tamanioFrente; i++) {
			indiceAux = distancias[i][0];
			distAux = distancias[i][1];
			indice = i;
			while(indice > 0 && distancias[indice-1][1] > distAux) {
				distancias[indice][0]=distancias[indice-1][0];
				distancias[indice][1]=distancias[indice-1][1];
				indice-=1;
			}
			distancias[indice][0]=indiceAux;
			distancias[indice][1]=distAux;
		}
		
		Cromosoma[] poblacionAux = new Cromosoma[TAMANIO_POBLACION*2];
		indice=0;
		int indice2 = tamanioFrente;
		for(int i = 0; i < frentes.size(); i++) {
			poblacionAux[indice] = new Cromosoma();
			if(i < frentes.size()-1) {
				for(int j = 0; j < frentes.get(i).size(); j++) {
					poblacionAux[indice] = new Cromosoma();
					poblacionAux[indice].getTipos().addAll(tiposGlobales);
					poblacionAux[indice].getCostos().addAll(poblacion[frentes.get(i).get(j)].getCostos());
					poblacionAux[indice].getConsumos().addAll(poblacion[frentes.get(i).get(j)].getConsumos());
					fitness[indice][0] = fitness[frentes.get(i).get(j)][0];
					fitness[indice][1] = fitness[frentes.get(i).get(j)][1];
					indice+=1;
				}
			} else {
				//Para el último frente, se toman los de mayor distancia hasta llenar la mitad de la población
				for(int j = (tamanioFrente-1); j >= 0; j--) {
					if(indice == TAMANIO_POBLACION) {
						break;
					} else {
						poblacionAux[indice] = new Cromosoma();
						poblacionAux[indice].getTipos().addAll(tiposGlobales);
						poblacionAux[indice].getCostos().addAll(poblacion[(int)distancias[j][0]].getCostos());
						poblacionAux[indice].getConsumos().addAll(poblacion[(int)distancias[j][0]].getConsumos());
						fitness[indice][0] = fitness[(int)distancias[j][0]][0];
						fitness[indice][1] = fitness[(int)distancias[j][0]][1];
						indice+=1;
					}
				}
			}
		}
		
		return poblacionAux;
	}
	
}
