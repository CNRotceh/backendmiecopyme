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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	
	
	@CrossOrigin(origins = "http://localhost:8100")
	@ApiOperation(value = "Crear pyme",notes = "Permite crear un nuevo pyme, su id debe ser única") // Documentacion del api
	@PostMapping(path = "/pymes", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity <?> create(@RequestBody @Valid Pyme nuevoPyme) { //Validaciones
		log.info("Recibí llamada a create con " + nuevoPyme);
		Pyme pyme = pymeService.create(nuevoPyme);
		
		if(pyme != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(pyme);
		} else {
			//log.info("Duplicado");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Pyme duplicado");
		}
	}
		
	@CrossOrigin(origins = "http://localhost:8100")
	@ApiOperation(value = "Obtener pyme",notes = "Permite regresar todos los pymes") // Documentacion del api
	@GetMapping(path = "/pymes", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity <?> retrieveAll() {
		
		Iterable <Pyme> result = pymeService.retrieveAll();
		return ResponseEntity.status(HttpStatus.OK).body(result);	
	}

	@CrossOrigin(origins = "http://localhost:8100")
	@ApiOperation(value = "Obtener pyme",notes = "Permite obtener un pyme por su id") // Documentacion del api
	@GetMapping(path = "/pymes/{idPyme}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity <?> retrieve(@PathVariable("idPyme") Integer idPyme) {
		log.info("Buscando al pyme con id "+ idPyme);
		Pyme pyme = pymeService.findByIdPyme(idPyme);
		if(pyme != null) {
			return ResponseEntity.status(HttpStatus.OK).body(pyme);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pyme no encontrado");
		}
	}

	@ApiOperation(value = "Actualizar pyme",notes = "Permite actualizar un pyme existente con su id") // Documentacion del api
	@PutMapping(path = "/pymes/{idPyme}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity <?> update(@PathVariable("idPyme") @Valid Integer idPyme, @RequestBody Pyme actualizaPyme) {
		log.info("Buscando al pyme con id "+ idPyme + " para actualizar");
		actualizaPyme.setIdPyme(idPyme);
		Boolean actualizado = pymeService.update(actualizaPyme);
		if(actualizado) {
			return ResponseEntity.status(HttpStatus.OK).body(actualizaPyme);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pyme no encontrado");
		}	
	}
	
	
	@ApiOperation(value = "Borrar pyme",notes = "Permite borrar un pyme") // Documentacion del api
	@DeleteMapping(path = "/pymes/{idPyme}")
	public ResponseEntity <?>  delete(@PathVariable("idPyme") Integer idPyme) {
		log.info("Buscando al pyme con id "+ idPyme + "para borrar");
		boolean borrado = pymeService.delete(idPyme);
		if(borrado != false) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El pyme no existe");
		}	
	}

	
	/**
	 * 
	 * POST /pyme/{idPyme}/servicio?idServicio=1
	 * 
	 * @return
	 */
	
	@CrossOrigin(origins = "http://localhost:8100")
	@PostMapping(path = "/pymes/{idPyme}/servicios", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity <?> addServicioToPyme(@PathVariable("idPyme") Integer idPyme,@RequestParam("idServicio") Integer[] idServicio) {
		
		boolean result = pymeService.addServicioToPyme(idPyme, idServicio);
		if(result) {
			return ResponseEntity.status(HttpStatus.OK).build(); 
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); 
		}
	}
	
	/**
	 * 
	 * DELETE /pyme/{idPyme}/servicio?idServicio=1
	 * 
	 * @return
	 */
	@DeleteMapping(path = "/pymes/{idPyme}/servicios")
	public ResponseEntity <?> removeServicioFromPyme(@PathVariable("idPyme") Integer idPyme,@RequestParam("idServicio") Integer[] idServicio) {
		
		boolean result = pymeService.removeServicioFromPyme(idPyme, idServicio);
		if(result) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); 
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); 
		}
	}
	

}
