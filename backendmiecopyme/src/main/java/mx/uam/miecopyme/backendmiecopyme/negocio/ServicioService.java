package mx.uam.miecopyme.backendmiecopyme.negocio;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import mx.uam.miecopyme.backendmiecopyme.datos.ServicioRepository;
import mx.uam.miecopyme.backendmiecopyme.negocio.modelo.Servicio;

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
	public Servicio[] create(Servicio[] servicioNuevo) {
		//Traza para la prueba
		log.info("Voy a guardar al servicio " + servicioNuevo);
		for(Servicio aux:servicioNuevo)
			servicioRepository.save(aux);
			
		return servicioNuevo;
	}
	
	/**
	 * 
	 * @param 
	 * @return
	 */
	public Iterable <Servicio> retrieveAll () {
		log.info("Regresando todos los servicios:");
		return servicioRepository.findAll();
	}

	/**
	 * 
	 * @param
	 * @return
	 */
	public Servicio findByIdServicio(Integer idServicio) {
		Optional<Servicio> servicioOpt = servicioRepository.findById(idServicio);	
		if(servicioOpt.isPresent()) {
			log.info("Se ha encontrado el servicio" + servicioOpt);
			return servicioOpt.get();
		} else {
			log.info("No es posible regresar el servicio indicado");
			return null;
		}
	}

	/**
	 * 
	 * @param
	 * @return
	 */
	public boolean update(Servicio actualizaServicio) {
		// Primero veo que si esté en la BD
		Optional<Servicio> servicioOpt = servicioRepository.findById(actualizaServicio.getIdServicio());	
		if(servicioOpt.isPresent()) {
			Servicio servicio = servicioOpt.get(); // Este es el que está en la bd
			log.info("El servicio actual es: " + servicio);
			servicio.setTipo(actualizaServicio.getTipo());
			servicio.setConsumo(actualizaServicio.getConsumo());
			servicio.setCosto(actualizaServicio.getCosto());
			log.info("El servicio que se quiere modificar es: " + actualizaServicio);
			servicioRepository.save(servicio); // Persisto los cambios		
			return true;
		} else {
			log.info("No existe el servicio que se busca modificar");
			return false;
		}

	}
	
	/**
	 * 
	 * @param
	 * @return
	 */
	public boolean delete(Integer idBorraServicio) {
	
		Optional <Servicio> servicioOpt = servicioRepository.findById(idBorraServicio);	
		if(servicioOpt.isPresent()) {
			Servicio servicio = servicioOpt.get();
			servicioRepository.delete(servicio);
			//if(!servicioOpt.isPresent())
			log.info("Servicio borrado");
			return true;
		} else {
			log.info("No es posible borrar un servicio inexistente");
			return false;
		}
	}

}
