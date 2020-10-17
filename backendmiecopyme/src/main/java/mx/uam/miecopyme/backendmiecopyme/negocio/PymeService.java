package mx.uam.miecopyme.backendmiecopyme.negocio;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import mx.uam.miecopyme.backendmiecopyme.datos.PymeRepository;
import mx.uam.miecopyme.backendmiecopyme.datos.ServicioRepository;
import mx.uam.miecopyme.backendmiecopyme.negocio.modelo.Pyme;
import mx.uam.miecopyme.backendmiecopyme.negocio.modelo.Servicio;


/**
 * 
 * Clase que contiene la lógica de negocio del manejo de las Pymes
 * 
 * @author Prograsaur Studios
 *
 */
@Service
@Slf4j
public class PymeService {
	@Autowired
	private PymeRepository pymeRepository;
	
	@Autowired
	private ServicioRepository servicioRepository;
	
	/**
	 * 
	 * @param nuevoPyme
	 * @return el pyme que se acaba de crear si la creacion es exitosa, null de lo contrario
	 */
	public Pyme create(Pyme nuevoPyme) {
		//Traza para la prueba
		log.info("Voy a guardar al pyme " + nuevoPyme);
		return pymeRepository.save(nuevoPyme);
	}
	
	/**
	 * 
	 * @return
	 */
	public Iterable <Pyme> retrieveAll () {
		log.info("Regresando todos los pymes:");
		return pymeRepository.findAll();
	}

	/**
	 * 
	 * @param
	 * @return
	 */
	public Pyme findByIdPyme(Integer idPyme) {
		// Regla de negocio: No se puede crear más de un pyme con el mismo idPyme
		Optional <Pyme> pymeOpt = pymeRepository.findById(idPyme);	
		if(pymeOpt.isPresent()) {
			log.info("Se ha encontrado el pyme" + pymeOpt);
			return pymeOpt.get();
		} else {
			log.info("No es posible regresar el pyme indicado");
			return null;
		}
	}

	/**
	 * 
	 * @param
	 * @return
	 */
	public boolean update(Pyme actualizaPyme) {
		// Primero veo que si esté en la BD
		Optional <Pyme> pymeOpt = pymeRepository.findById(actualizaPyme.getIdPyme());	
		if(pymeOpt.isPresent()) {
			Pyme pyme = pymeOpt.get(); // Este es el que está en la bd
			log.info("El pyme actual es: " + pyme);
			log.info("Actualizar cosas");
			//log.info("Actualizando pyme");
			log.info("El pyme que se quiere modificar es: " + actualizaPyme);
			pymeRepository.save(pyme); // Persisto los cambios		
			return true;
		} else {
			log.info("No existe el pyme que se busca modificar");
			return false;
		}

	}
	
	/**
	 * 
	 * @param
	 * @return
	 */
	public boolean delete(Integer idBorraPyme) {
	
		Optional <Pyme> pymeOpt = pymeRepository.findById(idBorraPyme);	
		if(pymeOpt.isPresent()) {
			Pyme pyme = pymeOpt.get();
			pymeRepository.delete(pyme);
			log.info("Pyme borrado");
			return true;
		} else {
			log.info("No es posible borrar un pyme inexistente");
			return false;
		}
	}
	
	public boolean addServicioToPyme(Integer PymeId, Integer[] idServicios) {
		log.info("Agregando Servicios "+idServicios+" al Pyme "+PymeId);

		// 1.- Recuperamos el Pyme
		Optional <Pyme> pymeOpt = pymeRepository.findById(PymeId);
		if(!pymeOpt.isPresent()) {
			log.info("No se encontro el pyme");
			return false;
		}
		
		
		Pyme pyme = pymeOpt.get();
		
		for(Integer histid: idServicios) {
			//2Recuperamos el id servicio
			Optional<Servicio> servicioOpt = servicioRepository.findById(histid);
			
			if(!servicioOpt.isPresent()) {
				log.info("No se encontro la servicio ");
				return false;
			}
			
			
			pyme.addServicio(servicioOpt);
		}
		
		// .- Persistir el cambio
		pymeRepository.save(pyme);
		
		return true;
	}

	public boolean removeServicioFromPyme(Integer PymeId, Integer[] idServicios) {
		log.info("Removiendo Servicios " + idServicios + " del Pyme "+PymeId);

		// 1.- Recuperamos el Pyme
		Optional <Pyme> pymeOpt = pymeRepository.findById(PymeId);
		if(!pymeOpt.isPresent()) {
			log.info("No se encontro el pyme");
			return false;
		}
		
		
		Pyme pyme = pymeOpt.get();
		for(Integer histid: idServicios) {
			//2Recuperamos el id servicio
			Optional<Servicio> servicioOpt = servicioRepository.findById(histid);
			
			if(!servicioOpt.isPresent()) {
				log.info("No se encontro la servicio o el pyme");
				return false;
			}

			pyme.removeServicio(servicioOpt);	
		}
		
		
		// 5.- Persistir el cambio
		pymeRepository.save(pyme);
		
		return true;
	}

}


