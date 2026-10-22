package app.entity;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Equipamentos extends Auditable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(unique=true)
	@NotBlank(message = "Por favor, informe o número do patrimônio do equipamento")
	private String patrimonio;
	
	@NotBlank(message= "Por favor, informe a marca do equipamento")
	private String marca;
	
	@NotBlank(message= "Por favor, informe o modelo do equipamento")
	private String modelo;
	
	@NotNull(message= "Por favor, informe a data de aquisição do equipamento")
	private LocalDate dataAquisicao;
	
	private String observacao;
	
	private String situacao;
	
	private boolean ativo;
	
	@OneToMany(mappedBy = "equipamento")
	@JsonIgnoreProperties("equipamento")
	private List<Emprestimos> emprestimo;
	
	
}
