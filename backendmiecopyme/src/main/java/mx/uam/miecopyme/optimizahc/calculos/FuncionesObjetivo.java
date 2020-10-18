package mx.uam.miecopyme.optimizahc.calculos;

import java.util.ArrayList;
import java.util.List;

import mx.uam.miecopyme.backendmiecopyme.negocio.modelo.Servicio;

public class FuncionesObjetivo {

	public static double costo(List<Double> costos) {
		double costoTotal = 0.0;
		for(double cos: costos) {
			costoTotal += cos;
		}
		return costoTotal;
	}
	
	public static double huella(List<Double> consumos, ArrayList<Integer> tipos, int tamanioCromosoma) {
		double huella = 0.0;
		for(int i = 0; i < tamanioCromosoma; i++) {
			switch(tipos.get(i)) {
				//Electricidad
				case 1: {
					huella += consumos.get(i)*FactoresEmision.ELECTRICIDAD;
					break;
				}
				//Gas
				case 2: {
					//Tipo de gas
					huella += consumos.get(i)*FactoresEmision.GLP;
					break;
				}
				//Refrigeración, enfriamiento
				case 3: case 4: {
					huella += consumos.get(i)*FactoresEmision.TRF_KWHATS*FactoresEmision.ELECTRICIDAD;
					break;
				}
				/*case 4: {
					huella += serv.getConsumo()*FactoresEmision.TRF_KWHATS*FactoresEmision.ELECTRICIDAD;
					break;
				}*/
				//Aire acondicionado, calefacción
				case 5: case 6:{
					huella += consumos.get(i)*FactoresEmision.BTU_KWHATS*FactoresEmision.ELECTRICIDAD;
					break;
				}
				/*case 6: {
					huella += serv.getConsumo()*FactoresEmision.BTU_KWHATS*FactoresEmision.ELECTRICIDAD;
					break;
				}*/
				//Vehículo
				case 7:{
					huella += consumos.get(i)*FactoresEmision.GASOLINA_E5;
					break;
				}
				default: {
					break;
				}
			}
		}
		return huella;
	}
	
}
