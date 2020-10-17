package mx.uam.miecopyme.backendmiecopyme.servicio;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import mx.uam.miecopyme.backendmiecopyme.negocio.ServicioService;
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
public class ServicioController {
	
	@Autowired
	private ServicioService servicioService;
	
	@CrossOrigin(origins = "http://localhost:8100")
	@ApiOperation(value = "Crear servicios",notes = "Permite crear un nuevo servicio") // Documentacion del api
	@PostMapping(path = "/servicios", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity <?> create(@RequestBody @Valid Servicio[] nuevoServicio) { //Validaciones
		log.info("Recibí llamada a create con " + nuevoServicio);
		Servicio[] servicio = servicioService.create(nuevoServicio);
		
		if(servicio != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(servicio);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Servicio duplicado");
		}
	}
	
	@CrossOrigin(origins = "http://localhost:8100")
	@ApiOperation(value = "Obtener Servicio",notes = "Permite regresar todos los Servicioes") // Documentacion del api
	@GetMapping(path = "/servicios", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity <?> retrieveAll() {
		
		Iterable <Servicio> result = servicioService.retrieveAll();
		return ResponseEntity.status(HttpStatus.OK).body(result);	
	}
	
	@CrossOrigin(origins = "http://localhost:8100")
	@ApiOperation(value = "Obtener Servicioes",notes = "Permite obtener un Servicio por su id") // Documentacion del api
	@GetMapping(path = "/servicios/{idServicio}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity <?> retrieve(@PathVariable("idServicio") Integer idServicio) {
		log.info("Buscando al Servicio con id "+ idServicio);
		Servicio Servicio = servicioService.findByIdServicio(idServicio);
		if(Servicio != null) {
			return ResponseEntity.status(HttpStatus.OK).body(Servicio);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Servicio no encontrado");
		}
	}

	@ApiOperation(value = "Actualizar Servicio",notes = "Permite actualizar un Servicio existente con su id") // Documentacion del api
	@PutMapping(path = "/servicios/{idServicio}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity <?> update(@PathVariable("idServicio") @Valid Integer idServicio, @RequestBody Servicio actualizaServicio) {
		log.info("Buscando al Servicio con id "+ idServicio + " para actualizar");
		actualizaServicio.setIdServicio(idServicio);
		Boolean actualizado = servicioService.update(actualizaServicio);
		if(actualizado) {
			return ResponseEntity.status(HttpStatus.OK).body(actualizaServicio);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Servicio no encontrado");
		}	
	}
	
	
	@ApiOperation(value = "Borrar Servicio",notes = "Permite borrar un Servicio") // Documentacion del api
	@DeleteMapping(path = "/servicios/{idServicio}")
	public ResponseEntity <?>  delete(@PathVariable("idServicio") Integer idServicio) {
		
		try {
			log.info("Buscando al Servicio con id "+ idServicio + "para borrar");
			boolean borrado = servicioService.delete(idServicio);
			if(borrado) 
				return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			 else 
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Servicio no encontrado");
			
		} catch (IllegalArgumentException ex) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		} 
	}

}
