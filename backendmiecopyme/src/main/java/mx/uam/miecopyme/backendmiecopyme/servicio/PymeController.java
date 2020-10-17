package mx.uam.miecopyme.backendmiecopyme.servicio;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
	

}
