package mx.uam.miecopyme.backendmiecopyme.servicio;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import mx.uam.miecopyme.backendmiecopyme.negocio.PymeService;
import mx.uam.miecopyme.backendmiecopyme.negocio.ServicioService;
import mx.uam.miecopyme.backendmiecopyme.negocio.modelo.Pyme;
import mx.uam.miecopyme.backendmiecopyme.negocio.modelo.Servicio;

/**
 * Controlador para el API Rest
 * 
 * @author Prograsaur Studios
 *
 */
@RestController
@RequestMapping("/v1") //Versionamiento
@Slf4j

public class PymeController {
	@Autowired
	private PymeService pymeService;
	
	@ApiOperation(value = "Crear Pyme",notes = "Permite crear una nueva Pyme") // Documentacion del api
	@PostMapping(path = "/pymes", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity <?> create(@RequestBody @Valid Pyme nuevaPyme) { //Validaciones
		log.info("Recibí llamada a create con " + nuevaPyme);
		Pyme pyme = pymeService.create(nuevaPyme);
		
		if(pyme != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(pyme);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La Pyme duplicado");
		}
	}
	
	@ApiOperation(value = "Obtener Pyme",notes = "Permite regresar todas las Pymes") // Documentacion del api
	@GetMapping(path = "/pymes", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity <?> retrieveAll() {
		
		Iterable <Pyme> result = pymeService.retrieveAll();
		return ResponseEntity.status(HttpStatus.OK).body(result);	
	}
	
	@ApiOperation(value = "Obtener una pyme especifica",notes = "Permite obtener una pyme por su id") // Documentacion del api
	@GetMapping(path = "/pymes/{idPyme}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity <?> retrieve(@PathVariable("idPyme") Integer idPyme) {
		log.info("Buscando al la Pyme con id "+ idPyme);
		Pyme pyme = pymeService.findByIdPyme(idPyme);
		if(pyme != null) {
			return ResponseEntity.status(HttpStatus.OK).body(pyme);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pyme no encontrado");
		}
	}

	@ApiOperation(value = "Borrar Pyme",notes = "Permite borrar una Pyme") // Documentacion del api
	@DeleteMapping(path = "/pymes/{idPyme}")
	public ResponseEntity <?>  delete(@PathVariable("idPyme") Integer idPyme) {

		try {
			log.info("Buscando a al Pyme con id "+ idPyme + "para borrar");
			boolean borrado = pymeService.delete(idPyme);
			if(borrado) 
				return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			 else 
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pyme no encontrado");

		} catch (IllegalArgumentException ex) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		} 
	}
	
	@ApiOperation(value = "Modifica Pyme",notes = "Permite agregar un servicio nuevo a la pyme") // Documentacion del api
	@PostMapping(path = "/pymes/{id}/servicio", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity <?> addServicioToPyme(
			@PathVariable("id_pyme") Integer pymeId,
			@RequestParam("id_servicio") Integer idServicio) {
		
		boolean result = pymeService.addServicioPyme(pymeId, idServicio);
		
		if(result) {
			return ResponseEntity.status(HttpStatus.OK).build(); 
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); 
		}
		
	
	}
	

}
