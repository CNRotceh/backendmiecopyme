package mx.uam.miecopyme.backendmiecopyme.servicio;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import mx.uam.miecopyme.backendmiecopyme.negocio.AlgoritmoService;
import mx.uam.miecopyme.backendmiecopyme.negocio.PymeService;
import mx.uam.miecopyme.backendmiecopyme.negocio.modelo.Pyme;

/**
 * Controlador para el API Rest
 * 
 * @author Prograsaur Studios
 *
 */
@RestController
@RequestMapping("/v1") //Versionamiento
@Slf4j
public class AlgoritmoController {

	@Autowired
	private AlgoritmoService algoritmoService;
	
	@CrossOrigin(origins = "https://smie.000webhostapp.com")
	@ApiOperation(value = "Ejecutar algoritmo",notes = "Permite crear alternativas de acuerdo al algoritmo genetic") // Documentacion del api
	@PostMapping(path = "/algoritmo", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity <?> ejecutar(@RequestBody @Valid Pyme pymeActual) { //Validaciones
		
		log.info("Pyme recibida",pymeActual);
		Iterable<Pyme> resul = algoritmoService.run(pymeActual);
		
		if(resul != null) {
			return ResponseEntity.status(HttpStatus.OK).body(resul);
		} else {
		
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error en el algoritmo");
		}

	}

}
