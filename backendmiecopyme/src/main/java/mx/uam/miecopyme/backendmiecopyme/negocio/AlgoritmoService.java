package mx.uam.miecopyme.backendmiecopyme.negocio;

import java.util.ArrayList;
import java.util.List;

import mx.uam.miecopyme.backendmiecopyme.negocio.modelo.Pyme;
import mx.uam.miecopyme.backendmiecopyme.negocio.modelo.Servicio;
import mx.uam.miecopyme.optimizahc.nsga2.NSGA2;

public class AlgoritmoService {
	
	public Iterable<Pyme> run(Pyme pymeActual){
		NSGA2 algor = new NSGA2();
		List<ArrayList<Servicio>> soluciones = algor.nsga2(pymeActual.getServicios());
		List<Pyme> pymesAlternas = new ArrayList<Pyme>();
		for(int i = 0; i < soluciones.size(); i++) {
			pymesAlternas.add(new Pyme());
			pymesAlternas.get(i).setIdPyme(pymeActual.getIdPyme());
			pymesAlternas.get(i).setNombre(pymeActual.getNombre());
			pymesAlternas.get(i).getServicios().addAll(soluciones.get(i));
		}
		return pymesAlternas;
	}
	
}
