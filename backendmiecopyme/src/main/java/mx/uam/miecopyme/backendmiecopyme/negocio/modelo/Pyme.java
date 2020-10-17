package mx.uam.miecopyme.backendmiecopyme.negocio.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

public class Pyme {
	@ApiModelProperty(notes = "id del la Pyme", required = true)
	@Id
	@GeneratedValue
	private Integer idPyme;
	
	
	@ApiModelProperty(notes = "nombre de la pyme", required = true)
	private String nombre;
	
	@NotNull
	@ApiModelProperty(notes = "Consumo del servicio", required = true)
	private Double huella;
	
	@NotNull
	@ApiModelProperty(notes = "Costo del servicio", required = true)
	private Double costo;

	@Builder.Default
	@OneToMany(targetEntity = Servicio.class, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	@JoinColumn(name = "idPyme") // No crea tabla intermedia
	private List<Servicio> servicios = new ArrayList <> ();

	public void addServicio(Optional<Servicio> historiaOpt) {
		servicios.add(historiaOpt.get());
	}
	
	public void removeServicio(Optional<Servicio> historiaOpt) {
		servicios.remove(historiaOpt.get());
	}
	
}
