package app.service.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import app.auth.Usuarios;
import app.entity.Alunos;
import app.entity.Emprestimos;
import app.entity.Equipamentos;
import app.repository.EmprestimosRepository;
import app.service.EmprestimosService;

@SpringBootTest
public class EmprestimoServiceTest {

	@Autowired
	EmprestimosService emprestimoService;

	@MockBean
	EmprestimosRepository emprestimoRepository;

	Emprestimos emprestimo;
	Emprestimos emprestimoAtualizado;
	Emprestimos emprestimoErrado;

	@BeforeEach
	void setup() {
		// Formato de data para java.util.Date
		SimpleDateFormat formato1 = new SimpleDateFormat("dd/MM/yyyy");
		// Formato de data para LocalDate
		DateTimeFormatter formato2 = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		try {
			// Convertendo a String para java.util.Date
			Date dataNascimento = formato1.parse("20/03/2003");

			// Convertendo a String para LocalDate corretamente
			LocalDate dtAquisicao = LocalDate.parse("06/09/2024", formato2);

			// Pegando a data e hora atuais para LocalDateTime
			LocalDateTime dataRetirada = LocalDateTime.now();
			LocalDateTime dataDevolucao = dataRetirada.plusHours(2);

			// Criando a instância de Alunos
			Alunos aluno = new Alunos("Beatriz", "741256", "Engenharia", true, "115.822.819-80");

			// Criando a instância de Equipamentos
			Equipamentos equipamento = new Equipamentos(1, "123456", "Dell", "AX05", dtAquisicao, "OK", "Disponível",
					true, null);

			// Criando a instância de Usuarios
			Usuarios usuario = new Usuarios("Gabriela", "Colaborador", "282.249.270-08", "gabi", "$2a$12$A0t.YpdGg/3lbIQMWv1xi.9PZIfNYia0XgSymx/Au45k8U16kailm", false);

			// Criando as instâncias de Emprestimos
			emprestimo = new Emprestimos(1, dataRetirada, null, "Em Andamento", "", aluno, equipamento, usuario);
			emprestimoAtualizado = new Emprestimos(1, dataRetirada, dataDevolucao, "Encerrado", "", aluno, equipamento,
				usuario);
			emprestimoErrado = new Emprestimos(1, dataRetirada, dataDevolucao, "Encerrado", "", null, equipamento,
					null);

			// Mockando os métodos do repository
			Mockito.when(emprestimoRepository.save(emprestimo)).thenReturn(emprestimo);
			Mockito.when(emprestimoRepository.findAll()).thenReturn(List.of(emprestimo));
			Mockito.when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimo));

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Save empréstimo corretamente")
	void salvarEmpCorreto() {
		Mockito.when(emprestimoRepository.findByEmprestimosByAlunoAtivo(Mockito.any(Alunos.class)))
				.thenReturn(Collections.emptyList());

		//Mockito.when(emprestimoRepository.findByEmprestimosByEquipamentoAtivo(Mockito.any(Equipamentos.class)))
				//.thenReturn(Collections.emptyList());

		Mockito.when(emprestimoRepository.save(emprestimo)).thenReturn(emprestimo);

		String response = this.emprestimoService.save(emprestimo);
		assertEquals("Empréstimo salvo com sucesso!", response);
	}

	@Test
	@DisplayName("Save empréstimo validation")
	void salvarEmpValidation() {
		Mockito.when(emprestimoRepository.findByEmprestimosByAlunoAtivo(Mockito.any(Alunos.class)))
				.thenReturn(Collections.emptyList());

		//Mockito.when(emprestimoRepository.findByEmprestimosByEquipamentoAtivo(Mockito.any(Equipamentos.class)))
				//.thenReturn(Collections.emptyList());

		Mockito.when(emprestimoRepository.save(emprestimoErrado)).thenReturn(emprestimoErrado);

		assertThrows(Exception.class, () -> {
			String response = this.emprestimoService.save(emprestimoErrado);
		});
	}

	@Test
	@DisplayName("Save empréstimo incorreto")
	void salvarEmpIncorreto() {
		Mockito.when(emprestimoRepository.findByEmprestimosByAlunoAtivo(Mockito.any(Alunos.class)))
				.thenReturn(Collections.emptyList());

		//Mockito.when(emprestimoRepository.findByEmprestimosByEquipamentoAtivo(Mockito.any(Equipamentos.class)))
				//.thenReturn(Collections.emptyList());

		Mockito.when(emprestimoRepository.save(emprestimo)).thenReturn(null);

		assertThrows(Exception.class, () -> {
			String response = this.emprestimoService.save(emprestimo);
		});
	}

	@Test
	@DisplayName("Aluno já possui empréstimo em andamento")
	void salvarEmprestimoAlunoComEmprestimoAtivo() {
		List<Emprestimos> emprestimosAtivos = List.of(emprestimo);

		Mockito.when(emprestimoRepository.findByEmprestimosByAlunoAtivo(Mockito.any(Alunos.class)))
				.thenReturn(emprestimosAtivos);

		assertThrows(RuntimeException.class, () -> {
			emprestimoService.save(emprestimo);
		});
	}


	@Test
	@DisplayName("Update com Sucesso")
	void updateComSucesso() {
		// Mocking the repository save call to return the updated object
		Mockito.when(emprestimoRepository.save(Mockito.any(Emprestimos.class))).thenReturn(emprestimo);

		// Call the update method
		String result = emprestimoService.update(emprestimo, 1);

		// Assertions
		assertEquals("Empréstimo atualizado com sucesso!", result);
	}

	@Test
	@DisplayName("Update com Erro")
	void updateComErro() {
		// Simulate a repository failure by returning null
		Mockito.when(emprestimoRepository.save(Mockito.any(Emprestimos.class))).thenReturn(null);

		// Call the update method and assert that an exception is thrown
		RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
			emprestimoService.update(emprestimo, 1);
		});

		assertEquals("Erro ao atualizar empréstimo!", thrown.getMessage());

	}

	@Test
	@DisplayName("FindById - Correto")
	void findById() {
		Mockito.when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimo));

		Emprestimos emp = emprestimoService.findById(1);

		assertEquals(emprestimo, emp);
	}

	@Test
	@DisplayName("FindById - Incorreto")
	void findByIdIncorreto() {
		Mockito.when(emprestimoRepository.findById(99L)).thenReturn(Optional.empty());
		Emprestimos emp = emprestimoService.findById(99);
		assertEquals(null, emp);
	}

	@Test
	@DisplayName("FindBy - Situacao Correta")
	void findBySituacaoCorreta() {
		Mockito.when(emprestimoRepository.findBySituacao("Em Andamento")).thenReturn(List.of(emprestimo));
		List<Emprestimos> emps = emprestimoService.findBySituacao("Em Andamento");

		assertEquals(1, emps.size());
	}

	@Test
	@DisplayName("FindAll")
	void findAll() {
		Mockito.when(emprestimoRepository.findAll()).thenReturn(List.of(emprestimo));
		List<Emprestimos> emps = emprestimoService.findAll();
		assertEquals(1, emps.size());
	}

	@Test
	@DisplayName("FindBy - RA aluno")
	void findRaAluno() {
		Mockito.when(emprestimoRepository.findByAlunoRa("505233")).thenReturn(List.of(emprestimo));
		List<Emprestimos> emps = emprestimoService.findByAluno("505233");
		assertEquals(1, emps.size());
	}

	@Test
	@DisplayName("FindBy - Patrimonio")
	void findPatrimonio() {
		Mockito.when(emprestimoRepository.findByEquipamentoPatrimonio("123456")).thenReturn(List.of(emprestimo));
		List<Emprestimos> emps = emprestimoService.findByEquipamento("123456");
		assertEquals(1, emps.size());
	}

	@Test
	@DisplayName("FindBy - Data Retirada")
	void findDataRetirada() {
		LocalDateTime data1 = LocalDateTime.now();
		LocalDateTime data2 = data1.plusHours(2);
		Mockito.when(emprestimoRepository.findByDataRetirada(data1, data2)).thenReturn(List.of(emprestimo));
		List<Emprestimos> emps = emprestimoService.findByDataRetirada(data1, data2);
		assertEquals(1, emps.size());
	}

	@Test
	@DisplayName("FindBy - Data Devolucao")
	void findDataDevolucao() {
		LocalDateTime data1 = LocalDateTime.now();
		LocalDateTime data2 = data1.plusHours(2);
		Mockito.when(emprestimoRepository.findByDataDevolucao(data1, data2)).thenReturn(List.of(emprestimo));
		List<Emprestimos> emps = emprestimoService.findByDataDevolucao(data1, data2);
		assertEquals(1, emps.size());
	}

	@Test
	@DisplayName("Encerrar empréstimo")
	void encerrarEmprestimo() {
		String retorno = emprestimoService.encerrarEmprestimo(1);
		assertEquals("Empréstimo encerrado com sucesso", retorno);
	}


}
