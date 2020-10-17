package mx.uam.miecopyme.backendmiecopyme.negocio;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import mx.uam.miecopyme.backendmiecopyme.datos.PymeRepository;

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
	private ServicioService servicioService;
	
	/**
	 * 
	 * @param PymeNuevo
	 * @return la pyme que se acaba de crear si la creacion es exitosa, null de lo contrario
	 * 
	 */
	public Pyme create(Pyme pymeNuevo) {
		//Traza para la prueba
		log.info("Voy a guardar al servicio " + pymeNuevo);
		return pymeRepository.save(pymeNuevo);
	}
	
	/**
	 * 
	 * @param 
	 * @return
	 */
	public Iterable <Pyme> retrieveAll () {
		log.info("Regresando todos las Pymes:");
		return pymeRepository.findAll();
	}

	/**
	 * 
	 * @param
	 * @return
	 */
	public Pyme findByIdPyme( Integer idPyme) {
		Optional<Pyme> pymeOpt = pymeRepository.findById(idPyme);	
		if(pymeOpt.isPresent()) {
			log.info("Se ha encontrado la Pyme" + pymeOpt);
			return pymeOpt.get();
		} else {
			log.info("No es posible regresar la pyme indicado");
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
		Optional<Pyme> pymeOpt = pymeRepository.findById(actualizaPyme.getIdPyme());	
		if(pymeOpt.isPresent()) {
			Pyme pyme = pymeOpt.get(); // Este es el que está en la bd
			log.info("LA pyme es: " + pyme);
			pyme.setServicios(actualizaPyme.getServicios());
			log.info("La pyme que se quiere modificar es: " + actualizaPyme);
			pymeRepository.save(pyme); // Persisto los cambios		
			return true;
		} else {
			log.info("No existe la pyme que se busca modificar");
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
			//if(!servicioOpt.isPresent())
			log.info("pyme borrado");
			return true;
		} else {
			log.info("No es posible borrar una pyme  inexistente");
			return false;
		}
	}


public boolean addServicioPyme(Integer pymeId, Integer idServicio) {
		
		log.info("Agregando el servicio  la pyme "+idServicio+" al grupo "+pymeId);
		
		// 1.- Recuperamos primero al servicio
		Servicio serv = servicioService.findByIdServicio(pymeId);
		
		// 2.- Recuperamos a la pyme
		Optional <Pyme> pymeOpt = pymeRepository.findById(pymeId);
		
		// 3.- Verificamos que el servicio y la pyme existan
		if(!pymeOpt.isPresent() || serv == null) {
			
			log.info("No se encontro la pyme o el servicio");
			
			return false;
		}
		
		// 4.- Agrego el alumno al grupo
		Pyme pyme = pymeOpt.get();
		pyme.addServicio(serv);
		
		// 5.- Persistir el cambio
		pymeRepository.save(pyme);
		
		return true;
	}

}
