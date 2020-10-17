package mx.uam.miecopyme.backendmiecopyme.negocio.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * Clase que representa a los servicios con los que cuenta la PyMe
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
public class Servicio {
	
	@ApiModelProperty(notes = "id del servicio", required = true)
	@Id
	@GeneratedValue
	private Integer idServicio;
	
	@NotNull
	@ApiModelProperty(notes = "Tipo de servicio", required = true)
	private Integer tipo;
	
	@NotNull
	@ApiModelProperty(notes = "Consumo del servicio", required = true)
	private Double consumo;
	
	@NotNull
	@ApiModelProperty(notes = "Costo del servicio", required = true)
	private Double costo;
	
	public Servicio clone() throws CloneNotSupportedException {
        return (Servicio) super.clone();
	}

}
