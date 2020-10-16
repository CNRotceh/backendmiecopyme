package mx.uam.miecopyme.backendmiecopyme.negocio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import mx.uam.miecopyme.backendmiecopyme.negocio.modelo.Servicio;
import mx.uam.ss.backendservicio.datos.ColaboradorRepository;
import mx.uam.ss.backendservicio.negocio.modelo.Colaborador;

/**
 * 
 * Clase que contiene la lógica de negocio del manejo de servicios
 * 
 * @author Prograsaur Studios
 *
 */
@Service
@Slf4j
public class ServicioService {
	
	@Autowired
	private ServicioRepository servicioRepository;
	
	/**
	 * 
	 * @param servicioNuevo
	 * @return el servicio que se acaba de crear si la creacion es exitosa, null de lo contrario
	 * 
	 */
	public Servicio create(Servicio servicioNuevo) {
		//Traza para la prueba
		log.info("Voy a guardar al colaborador " + servicioNuevo);
		return servicioRepository.save(servicioNuevo);
	}

}
