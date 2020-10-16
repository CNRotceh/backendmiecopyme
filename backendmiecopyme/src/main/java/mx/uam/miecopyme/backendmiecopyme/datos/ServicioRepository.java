package mx.uam.miecopyme.backendmiecopyme.datos;

import org.springframework.data.repository.CrudRepository;

import mx.uam.miecopyme.backendmiecopyme.negocio.modelo.Servicio;


/**
 * Guardado y almacenamiento de servicios
 * 
 * @author Prograsaur Studios
 *
 */
public interface ServicioRepository extends CrudRepository <Servicio, Integer>{

}
