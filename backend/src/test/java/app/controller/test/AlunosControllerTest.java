package app.controller.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import app.controller.AlunosController;
import app.entity.Alunos;
import app.service.AlunosService;

@SpringBootTest
public class AlunosControllerTest {

	/*@Autowired
	AlunosController alunosController;

	@MockBean
	AlunosService alunosService;

	private Alunos aluno1;
	private Alunos aluno2;

	@BeforeEach
	void setup() {
		// Definir uma data de nascimento fictícia
		Date dataNascimento1 = new GregorianCalendar(2000, GregorianCalendar.JANUARY, 1).getTime();
		Date dataNascimento2 = new GregorianCalendar(1999, GregorianCalendar.FEBRUARY, 2).getTime();

		// Inicializar os objetos Alunos com o construtor padrão e setters
		aluno1 = new Alunos();
		aluno1.setId(1);
		aluno1.setNome("Kanye West da Silva");
		aluno1.setDataNascimento(dataNascimento1);
		aluno1.setCpf("232.209.190-14");
		aluno1.setEmail("Kanye.silva@gmail.com");
		aluno1.setCelular("(11) 91234-5678");
		aluno1.setRa("505567");
		aluno1.setCurso("Psicologia");
		aluno1.setAtivo(true);
		aluno1.setEmprestimos(Collections.emptyList()); // Inicializa a lista de emprestimos como vazia

		aluno2 = new Alunos();
		aluno2.setId(2);
		aluno2.setNome("Rebeca Andrade do Santos");
		aluno2.setDataNascimento(dataNascimento2);
		aluno2.setCpf("285.748.800-94");
		aluno2.setEmail("Rebeca.santos@gmail.com");
		aluno2.setCelular("(11) 97654-3210");
		aluno2.setRa("056070");
		aluno2.setCurso("Educação Física");
		aluno2.setAtivo(true);
		aluno2.setEmprestimos(Collections.emptyList()); // Inicializa a lista de emprestimos como vazia

		// Configurar o comportamento dos mocks
		when(alunosService.save(any(Alunos.class))).thenReturn("Aluno salvo com sucesso!");
		when(alunosService.update(any(Alunos.class), anyLong())).thenReturn("Aluno atualizado com sucesso!");
		when(alunosService.findAll()).thenReturn(Arrays.asList(aluno1, aluno2));
		when(alunosService.findById(anyLong())).thenReturn(aluno1);
		when(alunosService.findByRa(anyString())).thenReturn(aluno1);
		when(alunosService.findByNome(anyString())).thenReturn(Arrays.asList(aluno1, aluno2));
		when(alunosService.findByCpf(anyString())).thenReturn(aluno1);
		when(alunosService.findAlunosAtivos()).thenReturn(Arrays.asList(aluno1, aluno2));
		when(alunosService.findAlunosInativos()).thenReturn(Collections.emptyList());
		when(alunosService.delete(anyString())).thenReturn("Aluno desativado com sucesso!");
		when(alunosService.reativarAluno(anyString())).thenReturn("Aluno reativado com sucesso!");
	}

	@Test
	@DisplayName("Test - salvar aluno corretamente")
	void testSaveAlunoCorretamente() {
		ResponseEntity<String> response = alunosController.save(aluno1);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Aluno salvo com sucesso!", response.getBody());
	}

	@Test
	@DisplayName("Test - salvar aluno com bad request")
	void testSaveAlunoComBadRequest() {
		when(alunosService.save(any(Alunos.class))).thenThrow(new RuntimeException("Erro ao salvar aluno"));
		ResponseEntity<String> response = alunosController.save(aluno1);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("Deu erro!Erro ao salvar aluno", response.getBody());
	}

	@Test
	@DisplayName("Test - salvar aluno erro de validação")
	void testSaveAlunoComErroDeValidacao() {
		Alunos aluno = new Alunos(1, "", null, "", "", "", "", "", "", "", false, null);
		assertThrows(Exception.class, () -> {
			ResponseEntity<String> response = alunosController.save(aluno);
		});

	}

	@Test
	@DisplayName("Test - atualizar aluno corretamente")
	void testUpdateAlunoCorretamente() {
		ResponseEntity<String> response = alunosController.update(aluno1, 1L);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Aluno atualizado com sucesso!", response.getBody());
	}
	
	@Test
	@DisplayName("Test - atualizar aluno erro de validação")
	void testAtualizarAlunoComErroDeValidacao() {
		Alunos aluno = new Alunos(1, "", null, "", "", "", "", "", "", "", false, null);
		assertThrows(Exception.class, () -> {
			ResponseEntity<String> response = alunosController.update(aluno,1);
		});

	}

	@Test
	@DisplayName("Test - atualizar aluno com erro")
	void testUpdateAlunoComErro() {
		when(alunosService.update(any(Alunos.class), anyLong()))
				.thenThrow(new RuntimeException("Erro ao atualizar aluno"));
		ResponseEntity<String> response = alunosController.update(aluno1, 1L);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("Deu erro!Erro ao atualizar aluno", response.getBody());
	}

	@Test
	@DisplayName("Test - desativar aluno corretamente")
	void testDesativarAlunoCorretamente() {
		// Configura o mock para retornar uma mensagem de sucesso ao desativar um aluno
		Mockito.when(alunosService.delete("123456")).thenReturn("Aluno desativado com sucesso!");

		// Chama o método delete do controlador
		ResponseEntity<String> response = alunosController.delete("123456");

		// Verifica se a resposta tem o status OK e a mensagem correta
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Aluno desativado com sucesso!", response.getBody());
	}

	@Test
	@DisplayName("Test - desativar aluno com erro")
	void testDesativarAlunoComErro() {
		// Configura o mock para lançar uma exceção ao desativar um aluno
		Mockito.when(alunosService.delete(Mockito.anyString()))
				.thenThrow(new RuntimeException("Erro ao desativar aluno"));

		// Chama o método delete do controlador
		ResponseEntity<String> response = alunosController.delete("123456");

		// Verifica se a resposta tem o status BAD_REQUEST e a mensagem de erro
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("Erro ao desativar aluno", response.getBody());
	}

	@Test
	@DisplayName("Test - buscar aluno por ID corretamente")
	void testFindByIdCorretamente() {
		ResponseEntity<Alunos> response = alunosController.findById(1L);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(aluno1, response.getBody());
	}

	@Test
	@DisplayName("Test - buscar aluno por ID com erro")
	void testFindByIdComErro() {
		when(alunosService.findById(anyLong())).thenThrow(new RuntimeException("Erro ao buscar aluno por ID"));
		ResponseEntity<Alunos> response = alunosController.findById(1L);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(null, response.getBody());
	}

	@Test
	@DisplayName("Test - buscar aluno por RA corretamente")
	void testFindByRaCorretamente() {
		ResponseEntity<Alunos> response = alunosController.findByRa("505567");
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(aluno1, response.getBody());
	}

	@Test
	@DisplayName("Test - buscar aluno por RA com erro")
	void testFindByRaComErro() {
		when(alunosService.findByRa(anyString())).thenThrow(new RuntimeException("Erro ao buscar aluno por RA"));
		ResponseEntity<Alunos> response = alunosController.findByRa("505567");
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(null, response.getBody());
	}

	@Test
	@DisplayName("Test - buscar aluno por nome corretamente")
	void testFindByNomeCorretamente() {
		ResponseEntity<List<Alunos>> response = alunosController.findByNome("Kanye");
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(Arrays.asList(aluno1, aluno2), response.getBody());
	}

	@Test
	@DisplayName("Test - buscar aluno por nome com erro")
	void testFindByNomeComErro() {
		when(alunosService.findByNome(anyString())).thenThrow(new RuntimeException("Erro ao buscar aluno por nome"));
		ResponseEntity<List<Alunos>> response = alunosController.findByNome("Kanye");
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(null, response.getBody());
	}

	@Test
	@DisplayName("Test - buscar aluno por CPF corretamente")
	void testFindByCpfCorretamente() {
		ResponseEntity<Alunos> response = alunosController.findByCpf("232.209.190-14");
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(aluno1, response.getBody());
	}

	@Test
	@DisplayName("Test - buscar aluno por CPF com erro")
	void testFindByCpfComErro() {
		when(alunosService.findByCpf(anyString())).thenThrow(new RuntimeException("Erro ao buscar aluno por CPF"));
		ResponseEntity<Alunos> response = alunosController.findByCpf("232.209.190-14");
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(null, response.getBody());
	}

	@Test
	@DisplayName("Test - buscar alunos ativos corretamente")
	void testFindByAlunoAtivoCorretamente() {
		ResponseEntity<List<Alunos>> response = alunosController.findByAlunoAtivo();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(Arrays.asList(aluno1, aluno2), response.getBody());
	}

	@Test
	@DisplayName("Test - buscar alunos ativos com erro")
	void testFindByAlunoAtivoComErro() {
		when(alunosService.findAlunosAtivos()).thenThrow(new RuntimeException("Erro ao buscar alunos ativos"));
		ResponseEntity<List<Alunos>> response = alunosController.findByAlunoAtivo();
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(null, response.getBody());
	}

	@Test
	@DisplayName("Test - buscar alunos inativos corretamente")
	void testFindByAlunoInativoCorretamente() {
		ResponseEntity<List<Alunos>> response = alunosController.findByAlunoInativo();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(Collections.emptyList(), response.getBody());
	}

	@Test
	@DisplayName("Test - buscar alunos inativos com erro")
	void testFindByAlunoInativoComErro() {
		when(alunosService.findAlunosInativos()).thenThrow(new RuntimeException("Erro ao buscar alunos inativos"));
		ResponseEntity<List<Alunos>> response = alunosController.findByAlunoInativo();
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(null, response.getBody());
	}

	@Test
	@DisplayName("Test - reativar aluno corretamente")
	void testReativarAlunoCorretamente() {
		ResponseEntity<String> response = alunosController.reativar("505567");
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Aluno reativado com sucesso!", response.getBody());
	}

	@Test
	@DisplayName("Test - reativar aluno com erro")
	void testReativarAlunoComErro() {
		when(alunosService.reativarAluno(anyString())).thenThrow(new RuntimeException("Erro ao reativar aluno"));
		ResponseEntity<String> response = alunosController.reativar("505567");
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("Deu erro!Erro ao reativar aluno", response.getBody());
	}*/
}
