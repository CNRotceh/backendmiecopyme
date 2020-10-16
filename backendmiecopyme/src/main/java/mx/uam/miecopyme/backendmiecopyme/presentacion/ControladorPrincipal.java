package mx.uam.miecopyme.backendmiecopyme.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
/**
 * Controlador Web
 * 
 * @author CNRotceh, joseuam
 *
 */
@Controller
@Slf4j
public class ControladorPrincipal {

	@GetMapping("/")
	public String index() {
		log.info("Se invoco index");
		return "index";
	}

	@RequestMapping("/ejemplo")
	@ResponseBody
	public String ejemplo() {
		return "Ejemplo de Spring Boot!";
	}
}