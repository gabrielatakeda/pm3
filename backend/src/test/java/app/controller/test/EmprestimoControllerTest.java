package app.controller.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import org.springframework.http.ResponseEntity;

import app.auth.Usuarios;
import app.controller.EmprestimosController;
import app.entity.Alunos;
import app.entity.Emprestimos;
import app.entity.Equipamentos;
import app.repository.EmprestimosRepository;
import app.service.EmprestimosService;

@SpringBootTest
public class EmprestimoControllerTest {

	@Autowired
	EmprestimosController emprestimoController;

	@MockBean
	EmprestimosService emprestimoService;

	@MockBean
	EmprestimosRepository emprestimoRepository;

	Emprestimos emprestimo;
	Emprestimos emprestimoAtualizado;
	Emprestimos emprestimoErrado;

	@BeforeEach
	void setup() {
		SimpleDateFormat formato1 = new SimpleDateFormat("dd/MM/yyyy");
		DateTimeFormatter formato2 = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		try {
			Date dataNascimento = formato1.parse("20/03/2003");
			LocalDate dtAquisicao = LocalDate.parse("06/09/2024", formato2);
			LocalDateTime dataRetirada = LocalDateTime.now();
			LocalDateTime dataDevolucao = dataRetirada.plusHours(2);

			Alunos aluno = new Alunos("Beatriz", "741256", "Engenharia", true, "115.822.819-80");

			Equipamentos equipamento = new Equipamentos(1, "123456", "Dell", "AX05", dtAquisicao, "OK", "Disponível",
					true, null);

			Usuarios usuario = new Usuarios("Gabriela", "Colaborador", "282.249.270-08", "gabi", "$2a$12$A0t.YpdGg/3lbIQMWv1xi.9PZIfNYia0XgSymx/Au45k8U16kailm", false);

			emprestimo = new Emprestimos(1, dataRetirada, null, "Em Andamento", "", aluno, equipamento, usuario);
			emprestimoAtualizado = new Emprestimos(1, dataRetirada, dataDevolucao, "Encerrado", "", aluno, equipamento,
					usuario);
			emprestimoErrado = new Emprestimos(1, dataRetirada, dataDevolucao, "Encerrado", "", null, null,
					null);

			Mockito.when(emprestimoRepository.save(emprestimo)).thenReturn(emprestimo);
			Mockito.when(emprestimoRepository.findAll()).thenReturn(List.of(emprestimo));

		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@DisplayName("Salvar empréstimo de forma correta")
	void salvarEmprestimoCorreto() {
		Mockito.when(emprestimoRepository.findByEmprestimosByAlunoAtivo(Mockito.any(Alunos.class)))
				.thenReturn(Collections.emptyList());

		//Mockito.when(emprestimoRepository.findByEmprestimosByEquipamentoAtivo(Mockito.any(Equipamentos.class)))
				//.thenReturn(Collections.emptyList());
		ResponseEntity<String> response = emprestimoController.save(emprestimo);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	@DisplayName("Salvar empréstimo caindo no catch")
	void salvarEmprestimoCaindoNoCatch() {
		// Mock repository methods
		Mockito.when(emprestimoRepository.findByEmprestimosByAlunoAtivo(Mockito.any(Alunos.class)))
				.thenReturn(Collections.emptyList());

		//Mockito.when(emprestimoRepository.findByEmprestimosByEquipamentoAtivo(Mockito.any(Equipamentos.class)))
				//.thenReturn(Collections.emptyList());
		
		// Simulate exception being thrown from the service layer
		Mockito.when(emprestimoService.save(Mockito.any(Emprestimos.class)))
				.thenThrow(new RuntimeException("Erro ao salvar empréstimo"));

		// Call the controller method
		ResponseEntity<String> response = emprestimoController.save(emprestimo);
		
		// Assertions
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("Erro ao salvar empréstimo", response.getBody());
	}


	@Test
	@DisplayName("Update - Correto")
	void update() {
		Mockito.when(emprestimoRepository.save(emprestimo)).thenReturn(emprestimoAtualizado);
		Mockito.when(emprestimoService.update(emprestimo, 1)).thenReturn("Empréstimo atualizado com sucesso!");

		ResponseEntity<String> response = this.emprestimoController.update(emprestimo, 1);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Empréstimo atualizado com sucesso!", response.getBody());
	}
	
	@Test
	@DisplayName("Update - Caindo no catch")
	void updateCaindoNoCatch() {
	    Mockito.when(emprestimoService.update(Mockito.any(Emprestimos.class), Mockito.eq(1L)))
	           .thenThrow(new RuntimeException("Erro ao atualizar empréstimo"));

	    ResponseEntity<String> response = this.emprestimoController.update(emprestimo, 1);

	    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	    assertEquals("Erro ao atualizar empréstimo", response.getBody());
	}

	@Test
	@DisplayName("FindById - ID Válido")
	void findByIdValido() {
		Mockito.when(emprestimoRepository.findById(1L)).thenReturn(Optional.of(emprestimo));
		ResponseEntity<Emprestimos> response = emprestimoController.findById(1);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	@DisplayName("FindById - Caindo no catch")
	void findByIdCaindoNoCatch() {
	    Mockito.when(emprestimoService.findById(Mockito.eq(1L)))
	           .thenThrow(new RuntimeException("Erro ao encontrar empréstimo"));

	    ResponseEntity<Emprestimos> response = this.emprestimoController.findById(1);

	    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	@DisplayName("FindAll")
	void findAll() {
		Mockito.when(emprestimoRepository.findAll()).thenReturn(List.of(emprestimo));
		ResponseEntity<List<Emprestimos>> lista = emprestimoController.findAll();
		assertEquals(HttpStatus.OK, lista.getStatusCode());
	}
	
	@Test
	@DisplayName("FindAll - Caindo no catch")
	void findAllCaindoNoCatch() {
	    Mockito.when(emprestimoService.findAll())
	           .thenThrow(new RuntimeException("Erro ao listar empréstimos"));

	    ResponseEntity<List<Emprestimos>> response = this.emprestimoController.findAll();

	    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	@DisplayName("FindBy - Situação Correta")
	void situacaoCorreta() {
		Mockito.when(emprestimoRepository.findBySituacao("Em Andamento")).thenReturn(List.of(emprestimo));
		ResponseEntity<List<Emprestimos>> lista = emprestimoController.findBySituacao("Em Andamento");
		assertEquals(HttpStatus.OK, lista.getStatusCode());
	}
	
	@Test
	@DisplayName("FindBySituacao - Caindo no catch")
	void findBySituacaoCaindoNoCatch() {
	    Mockito.when(emprestimoService.findBySituacao(Mockito.anyString()))
	           .thenThrow(new RuntimeException("Erro ao encontrar situação"));

	    ResponseEntity<List<Emprestimos>> response = this.emprestimoController.findBySituacao("Em Andamento");

	    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	@DisplayName("FindBy - RA Aluno Correto")
	void raCorreto() {
		Mockito.when(emprestimoRepository.findByAlunoRa("505233")).thenReturn(List.of(emprestimo));
		ResponseEntity<List<Emprestimos>> lista = emprestimoController.findByAluno("505233");
		assertEquals(HttpStatus.OK, lista.getStatusCode());
	}

	@Test
	@DisplayName("FindByAluno - Caindo no catch")
	void findByAlunoCaindoNoCatch() {
	    Mockito.when(emprestimoService.findByAluno(Mockito.anyString()))
	           .thenThrow(new RuntimeException("Erro ao encontrar aluno"));

	    ResponseEntity<List<Emprestimos>> response = this.emprestimoController.findByAluno("505233");

	    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}


	@Test
	@DisplayName("FindBy - Patrimonio Equipamento Correto")
	void patrimonioCorreto() {
		Mockito.when(emprestimoRepository.findByEquipamentoPatrimonio("123456")).thenReturn(List.of(emprestimo));
		ResponseEntity<List<Emprestimos>> lista = emprestimoController.findByEquipamento("123456");
		assertEquals(HttpStatus.OK, lista.getStatusCode());
	}

	@Test
	@DisplayName("FindByEquipamento - Caindo no catch")
	void findByEquipamentoCaindoNoCatch() {
	    Mockito.when(emprestimoService.findByEquipamento(Mockito.anyString()))
	           .thenThrow(new RuntimeException("Erro ao encontrar equipamento"));

	    ResponseEntity<List<Emprestimos>> response = this.emprestimoController.findByEquipamento("123456");

	    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	@DisplayName("FindBy - Data Retirada Válida")
	void dataRetValida() {
		LocalDateTime dataRetirada = LocalDateTime.now();
		LocalDateTime dataDevolucao = dataRetirada.plusHours(2);

		Mockito.when(emprestimoRepository.findByDataRetirada(dataRetirada, dataDevolucao))
				.thenReturn(List.of(emprestimo));
		ResponseEntity<List<Emprestimos>> lista = emprestimoController.findByDataRetirada(dataRetirada, dataDevolucao);
		assertEquals(HttpStatus.OK, lista.getStatusCode());
	}

	@Test
	@DisplayName("FindByDataRetirada - Caindo no catch")
	void findByDataRetiradaCaindoNoCatch() {
	    LocalDateTime dataRetirada = LocalDateTime.now();
	    LocalDateTime dataDevolucao = dataRetirada.plusHours(2);

	    Mockito.when(emprestimoService.findByDataRetirada(dataRetirada, dataDevolucao))
	           .thenThrow(new RuntimeException("Erro ao encontrar data de retirada"));

	    ResponseEntity<List<Emprestimos>> response = this.emprestimoController.findByDataRetirada(dataRetirada, dataDevolucao);

	    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

	}


	@Test
	@DisplayName("FindBy - Data Devolucao Válida")
	void dataDevValida() {
		LocalDateTime dataRetirada = LocalDateTime.now();
		LocalDateTime dataDevolucao = dataRetirada.plusHours(2);

		Mockito.when(emprestimoRepository.findByDataDevolucao(dataRetirada, dataDevolucao))
				.thenReturn(List.of(emprestimo));
		ResponseEntity<List<Emprestimos>> lista = emprestimoController.findByDataDevolucao(dataRetirada, dataDevolucao);
		assertEquals(HttpStatus.OK, lista.getStatusCode());
	}

	@Test
	@DisplayName("FindByDataDevolucao - Caindo no catch")
	void findByDataDevolucaoCaindoNoCatch() {
	    LocalDateTime dataRetirada = LocalDateTime.now();
	    LocalDateTime dataDevolucao = dataRetirada.plusHours(2);

	    Mockito.when(emprestimoService.findByDataDevolucao(dataRetirada, dataDevolucao))
	           .thenThrow(new RuntimeException("Erro ao encontrar data de devolução"));

	    ResponseEntity<List<Emprestimos>> response = this.emprestimoController.findByDataDevolucao(dataRetirada, dataDevolucao);

	    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	@DisplayName("Encerrar Empréstimo Válido")
	void encerrar() {
		ResponseEntity<String> lista = emprestimoController.encerrar(1);
		assertEquals(HttpStatus.OK, lista.getStatusCode());
	}
	
	@Test
	@DisplayName("Encerrar - Caindo no catch")
	void encerrarCaindoNoCatch() {
	    Mockito.when(emprestimoService.encerrarEmprestimo(Mockito.eq(1L)))
	           .thenThrow(new RuntimeException("Erro ao encerrar empréstimo"));

	    ResponseEntity<String> response = this.emprestimoController.encerrar(1);

	    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	    assertEquals(null, response.getBody());
	}

}