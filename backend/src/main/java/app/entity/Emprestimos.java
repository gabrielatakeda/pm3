package app.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import app.auth.Usuarios;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Emprestimos extends Auditable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private LocalDateTime dataRetirada;
	private LocalDateTime dataDevolucao;
	
	private String situacao;
	
	private String observacao;
	
	@ManyToOne
	@JsonIgnoreProperties("emprestimos")
	private Alunos aluno;
	
	@ManyToOne
	@JsonIgnoreProperties("emprestimo")
	private Equipamentos equipamento;
	
	@ManyToOne
	@JsonIgnoreProperties("emprestimos")
	private Usuarios usuario;

	
}
