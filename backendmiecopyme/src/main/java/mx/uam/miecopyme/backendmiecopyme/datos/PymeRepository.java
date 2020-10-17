package mx.uam.miecopyme.backendmiecopyme.datos;

import org.springframework.data.repository.CrudRepository;

import mx.uam.miecopyme.backendmiecopyme.negocio.modelo.Pyme;

/**
 * Guardado y almacenamiento de pyme
 * 
 * @author Prograsaur Studios
 *
 */

public interface PymeRepository extends CrudRepository <Pyme, Integer>{

}
