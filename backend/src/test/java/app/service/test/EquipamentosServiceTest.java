package app.service.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import app.entity.Emprestimos;
import app.entity.Equipamentos;
import app.repository.EmprestimosRepository;
import app.repository.EquipamentosRepository;
import app.service.EquipamentosService;

public class EquipamentosServiceTest {

	/*@InjectMocks
	private EquipamentosService equipamentosService;

	@Mock
	private EquipamentosRepository equipamentosRepository;

	@Mock
	private EmprestimosRepository emprestimosRepository;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("Save correto")
	void Save() {
		Equipamentos equipamento = new Equipamentos();
		equipamento.setPatrimonio("123456");
		equipamento.setMarca("Dell");
		equipamento.setModelo("Inspiron");
		equipamento.setDataAquisicao(LocalDate.now());

		when(equipamentosRepository.save(any(Equipamentos.class))).thenReturn(equipamento);

		String result = equipamentosService.save(equipamento);
		assertEquals("Equipamento salvo com sucesso!", result);
		verify(equipamentosRepository, times(1)).save(equipamento);
	}

	/*@Test
	@DisplayName("Save com erro de validation")
	void SaveNoValid() {
		Equipamentos equipamento = new Equipamentos(1, null, "", "", null, "", null, false, null);
		/*equipamento.setPatrimonio(null);
		equipamento.setMarca("Dell");
		equipamento.setModelo("Inspiron");
		equipamento.setDataAquisicao(LocalDate.now());

		when(equipamentosRepository.save(equipamento)).thenReturn(equipamento);

		assertThrows(Exception.class, () -> {
			String result = equipamentosService.save(equipamento);
		});

	}

	@Test
	void Update() {
		Equipamentos equipamento = new Equipamentos();
		equipamento.setPatrimonio("123456");

		when(equipamentosRepository.save(any(Equipamentos.class))).thenReturn(equipamento);

		String result = equipamentosService.update(equipamento, 1L);
		assertEquals("Equipamento salvo com sucesso!", result);
		assertEquals(1L, equipamento.getId());
		verify(equipamentosRepository, times(1)).save(equipamento);
	}

	@Test
	void FindByIdFound() {
		Equipamentos equipamento = new Equipamentos();
		equipamento.setId(1L);

		when(equipamentosRepository.findById(1L)).thenReturn(Optional.of(equipamento));

		Equipamentos found = equipamentosService.findById(1L);
		assertNotNull(found);
		assertEquals(1L, found.getId());
		verify(equipamentosRepository, times(1)).findById(1L);
	}

	@Test
	void FindByIdNotFound() {
		when(equipamentosRepository.findById(1L)).thenReturn(Optional.empty());

		Equipamentos found = equipamentosService.findById(1L);
		assertNull(found);
		verify(equipamentosRepository, times(1)).findById(1L);
	}

	@Test
	void FindAll() {
		List<Equipamentos> equipamentosList = new ArrayList<>();
		equipamentosList.add(new Equipamentos());

		when(equipamentosRepository.findAll()).thenReturn(equipamentosList);

		List<Equipamentos> result = equipamentosService.findAll();
		assertNotNull(result);
		assertEquals(1, result.size());
		verify(equipamentosRepository, times(1)).findAll();
	}

	@Test
	void FindByPatrimonio() {
		Equipamentos equipamento = new Equipamentos();
		equipamento.setPatrimonio("123456");

		when(equipamentosRepository.findByPatrimonio("123456")).thenReturn(equipamento);

		Equipamentos found = equipamentosService.findByPatrimonio("123456");
		assertNotNull(found);
		assertEquals("123456", found.getPatrimonio());
		verify(equipamentosRepository, times(1)).findByPatrimonio("123456");
	}

	@Test
	void FindByMarca() {
		List<Equipamentos> equipamentosList = new ArrayList<>();
		equipamentosList.add(new Equipamentos());

		when(equipamentosRepository.findByMarcaContains("Dell")).thenReturn(equipamentosList);

		List<Equipamentos> result = equipamentosService.findByMarca("Dell");
		assertNotNull(result);
		assertEquals(1, result.size());
		verify(equipamentosRepository, times(1)).findByMarcaContains("Dell");
	}

	@Test
	void FindByModelo() {
		List<Equipamentos> equipamentosList = new ArrayList<>();
		equipamentosList.add(new Equipamentos());

		when(equipamentosRepository.findByModeloContains("Inspiron")).thenReturn(equipamentosList);

		List<Equipamentos> result = equipamentosService.findByModelo("Inspiron");
		assertNotNull(result);
		assertEquals(1, result.size());
		verify(equipamentosRepository, times(1)).findByModeloContains("Inspiron");
	}

	@Test
	public void testFindBySituacao() {
		List<Equipamentos> equipamentosList = new ArrayList<>();
		equipamentosList.add(new Equipamentos());

		when(equipamentosRepository.findBySituacao("Disponível")).thenReturn(equipamentosList);

		List<Equipamentos> result = equipamentosService.findBySituacao("Disponível");
		assertNotNull(result);
		assertEquals(1, result.size());
		verify(equipamentosRepository, times(1)).findBySituacao("Disponível");
	}

	@Test
	void FindEquipamentosAtivos() {
		List<Equipamentos> equipamentosList = new ArrayList<>();
		equipamentosList.add(new Equipamentos());

		when(equipamentosRepository.findByAtivoTrue()).thenReturn(equipamentosList);

		List<Equipamentos> result = equipamentosService.findEquipamentosAtivos();
		assertNotNull(result);
		assertEquals(1, result.size());
		verify(equipamentosRepository, times(1)).findByAtivoTrue();
	}

	@Test
	void FindEquipamentosInativos() {
		List<Equipamentos> equipamentosList = new ArrayList<>();
		equipamentosList.add(new Equipamentos());

		when(equipamentosRepository.findByAtivoFalse()).thenReturn(equipamentosList);

		List<Equipamentos> result = equipamentosService.findEquipamentosInativos();
		assertNotNull(result);
		assertEquals(1, result.size());
		verify(equipamentosRepository, times(1)).findByAtivoFalse();
	}

	@Test
	public void testDelete_EquipamentoComEmprestimoEmAndamento() {
		// Given
		String patrimonio = "12345";
		Equipamentos equipamento = new Equipamentos();
		equipamento.setId(1L);
		when(equipamentosRepository.findByPatrimonio(patrimonio)).thenReturn(equipamento);

		List<Emprestimos> emprestimosList = new ArrayList<>();
		emprestimosList.add(new Emprestimos());
		//when(emprestimosRepository.findByEmprestimosByEquipamentoAtivo(any(Equipamentos.class)))
				//.thenReturn(emprestimosList);

		// When & Then
		assertThrows(RuntimeException.class, () -> {
			equipamentosService.delete(patrimonio);
		});

		//verify(equipamentosRepository, never()).desativarEquipamentos(anyLong());
	}

	@Test
	public void testDelete_SemEmprestimoEmAndamento() {
		// Given
		String patrimonio = "12345";
		Equipamentos equipamento = new Equipamentos();
		equipamento.setId(1L);
		when(equipamentosRepository.findByPatrimonio(patrimonio)).thenReturn(equipamento);

		List<Emprestimos> emprestimosList = new ArrayList<>();
		//when(emprestimosRepository.findByEmprestimosByEquipamentoAtivo(any(Equipamentos.class)))
				//.thenReturn(emprestimosList);

		//when(equipamentosRepository.desativarEquipamentos(1L)).thenReturn(1);

		// When
		String result = equipamentosService.delete(patrimonio);

		// Then
		assertEquals("Equipamento desativado com sucesso!", result);
		//verify(equipamentosRepository).desativarEquipamentos(1L);
	}

	@Test
	public void testReativarEquipamento_Sucesso() {
		// Given
		String patrimonio = "12345";
		Equipamentos equipamento = new Equipamentos();
		equipamento.setId(1L);
		when(equipamentosRepository.findByPatrimonio(patrimonio)).thenReturn(equipamento);
		//when(equipamentosRepository.reativarEquipamentos(1L)).thenReturn(1);

		// When
		String result = equipamentosService.reativarEquipamento(patrimonio);

		// Then
		assertEquals("Equipamento reativado com sucesso!", result);
		//verify(equipamentosRepository).reativarEquipamentos(1L);
	}

	@Test
	public void testReativarEquipamento_Erro() {
		// Given
		String patrimonio = "12345";
		Equipamentos equipamento = new Equipamentos();
		equipamento.setId(1L);
		when(equipamentosRepository.findByPatrimonio(patrimonio)).thenReturn(equipamento);
		//when(equipamentosRepository.reativarEquipamentos(1L)).thenReturn(0);

		// When & Then
		assertThrows(RuntimeException.class, () -> {
			equipamentosService.reativarEquipamento(patrimonio);
		});
	}

	@Test
	public void testEncontrarEmprestimoEmAndamentoPorEquip_ComEmprestimo() {
		// Given
		Equipamentos equipamento = new Equipamentos();
		equipamento.setId(1L);
		Emprestimos emprestimo = new Emprestimos();
		emprestimo.setEquipamento(equipamento);

		List<Emprestimos> emprestimosList = new ArrayList<>();
		emprestimosList.add(emprestimo);

		//when(emprestimosRepository.findByEmprestimosByEquipamentoAtivo(any(Equipamentos.class)))
				//.thenReturn(emprestimosList);

		// When
		/*List<Emprestimos> result = equipamentosService.encontrarEmprestimoEmAndamentoPorEquip(emprestimo);

		// Then
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(equipamento.getId(), result.get(0).getEquipamento().getId());
	}

	@Test
	public void testEncontrarEmprestimoEmAndamentoPorEquip_SemEmprestimo() {
		// Given
		Equipamentos equipamento = new Equipamentos();
		equipamento.setId(1L);
		Emprestimos emprestimo = new Emprestimos();
		emprestimo.setEquipamento(equipamento);

		List<Emprestimos> emprestimosList = new ArrayList<>();

		/*when(emprestimosRepository.findByEmprestimosByEquipamentoAtivo(any(Equipamentos.class)))
				.thenReturn(emprestimosList);

		// When
		List<Emprestimos> result = equipamentosService.encontrarEmprestimoEmAndamentoPorEquip(emprestimo);

		// Then
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	@Test
	public void testFindByDataAquisicao() {
		// Given
		LocalDate data1 = LocalDate.of(2020, 1, 1);
		LocalDate data2 = LocalDate.of(2021, 1, 1);

		List<Equipamentos> equipamentosList = new ArrayList<>();
		equipamentosList.add(new Equipamentos());

		when(equipamentosRepository.findByDataAquisicao(data1, data2)).thenReturn(equipamentosList);

		// When
		List<Equipamentos> result = equipamentosService.findByDataAquisicao(data1, data2);

		// Then
		assertNotNull(result);
		assertEquals(1, result.size());
	}*/

}
