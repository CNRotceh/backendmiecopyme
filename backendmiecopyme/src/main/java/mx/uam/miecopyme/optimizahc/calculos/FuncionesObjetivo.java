package mx.uam.miecopyme.optimizahc.calculos;

import java.util.List;

import mx.uam.miecopyme.backendmiecopyme.negocio.modelo.Servicio;

public class FuncionesObjetivo {

	public double costo(List<Servicio> cromosoma) {
		double costoTotal = 0.0;
		for(Servicio serv: cromosoma) {
			costoTotal += serv.getCosto();
		}
		return costoTotal;
	}
	
	public double huella(List<Servicio> cromosoma) {
		double huella = 0.0;
		for(Servicio serv: cromosoma) {
			switch(serv.getTipo()) {
				//Electricidad
				case 1: {
					huella += serv.getConsumo()*FactoresEmision.ELECTRICIDAD;
					break;
				}
				//Gas
				case 2: {
					//Tipo de gas
					huella += serv.getConsumo()*FactoresEmision.GLP;
					break;
				}
				//Refrigeración, enfriamiento
				case 3: case 4: {
					huella += serv.getConsumo()*FactoresEmision.TRF_KWHATS*FactoresEmision.ELECTRICIDAD;
					break;
				}
				/*case 4: {
					huella += serv.getConsumo()*FactoresEmision.TRF_KWHATS*FactoresEmision.ELECTRICIDAD;
					break;
				}*/
				//Aire acondicionado, calefacción
				case 5: case 6:{
					huella += serv.getConsumo()*FactoresEmision.BTU_KWHATS*FactoresEmision.ELECTRICIDAD;
					break;
				}
				/*case 6: {
					huella += serv.getConsumo()*FactoresEmision.BTU_KWHATS*FactoresEmision.ELECTRICIDAD;
					break;
				}*/
				default: {
					break;
				}
			}
		}
		return huella;
	}
	
}
