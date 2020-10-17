package mx.uam.miecopyme.backendmiecopyme.negocio.modelo;

import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * Clase que representa a las PyMe
 * 
 * @author Prograsaur Studios
 * 
 * NotNull.... para numeros
 * NotBlank....para string
 * NotEmpty....para listas
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity //Indica que hay que persistir en BD

public class E_Pyme {
	@ApiModelProperty(notes = "id del la Pyme", required = true)
	@Id
	@GeneratedValue
	private Integer idPyme;
	
	@NotBlank
	@ApiModelProperty(notes = "Lista de servicios", required = true)
	private ArrayList<Servicio> ListaServicio;

}
