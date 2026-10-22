package app.entity;

import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Alunos extends Auditable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotBlank(message="Por favor, informe o nome do aluno")
	private String nome;
	
	public Alunos(@NotBlank(message = "Por favor, informe o nome do aluno") String nome,
			@NotBlank(message = "Por favor, informe o RA do aluno") String ra,
			@NotBlank(message = "Por favor, informe o curso do aluno") String curso, boolean ativo, String cpf) {
		this.nome = nome;
		this.ra = ra;
		this.curso = curso;
		this.ativo = ativo;
		this.cpf = cpf;
	}
	
	@NotBlank(message="Por favor, informe o RA do aluno")
	private String ra;
	
	@NotBlank(message="Por favor, informe o curso do aluno")
	private String curso;
	
	private boolean ativo;
	
	@OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL)
	@JsonIgnoreProperties("aluno")
	private List<Emprestimos> emprestimos;        
	
	@Column(name = "data_nascimento")
	private Date dataNascimento;
	
	// @Column(unique=true)
	// @NotBlank(message="Por favor, informe o CPF do aluno")
	// @CPF(message="CPF inválido")
	private String cpf;
	
	// @NotBlank(message="Por favor, informe o email do aluno")
	// @Email(message="E-mail inválido")
	private String email;
	
	// @NotBlank(message="Por favor, informe o telefone do aluno")
	// @Pattern(regexp = "\\(\\d{2}\\) ?\\d{5}-\\d{4}", message = "O telefone deve corresponder ao formato (xx) xxxxx-xxxx")
	private String celular;
	
	
	private String usuario;
	private String senha;
	
	

	
	
}
