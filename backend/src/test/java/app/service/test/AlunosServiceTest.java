package app.service.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import app.controller.AlunosController;
import app.entity.Alunos;
import app.entity.Emprestimos;
import app.repository.AlunosRepository;
import app.repository.EmprestimosRepository;
import app.service.AlunosService;

@SpringBootTest
public class AlunosServiceTest {

   /* @Autowired
    AlunosController alunosController;

    @InjectMocks
    private AlunosService alunosService;

    @Mock
    private AlunosRepository alunosRepository;

    @Mock
    private EmprestimosRepository emprestimosRepository;

    private Alunos aluno1;
    private Alunos aluno2;
    private Emprestimos emprestimo1;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        Date dataNascimento1 = new GregorianCalendar(2000, Calendar.JANUARY, 1).getTime();
        Date dataNascimento2 = new GregorianCalendar(1999, Calendar.FEBRUARY, 2).getTime();

        aluno1 = new Alunos(1L, "Kanye West da Silva", dataNascimento1, "232.209.190-14", 
                "Kanye.silva@gmail.com", "(11) 91234-5678", null, null, 
                "505567", "Psicologia", true, Collections.emptyList());

        aluno2 = new Alunos(2L, "Rebeca Andrade do Santos", dataNascimento2, "285.748.800-94", 
                "Rebeca.santos@gmail.com", "(11) 91234-5678", null, null, 
                "056070", "Educação Física", true, Collections.emptyList());
        emprestimo1 = new Emprestimos();
        emprestimo1.setAluno(aluno1);

        // Configuração básica para simular exceção ao salvar aluno
        when(alunosRepository.save(any(Alunos.class))).thenThrow(new RuntimeException("Erro ao salvar aluno"));
    }

    @Test
    @DisplayName("Test - salvar aluno corretamente")
    void testSaveAlunoCorretamente() {
        when(alunosRepository.save(any(Alunos.class))).thenReturn(aluno1);
        String result = alunosService.save(aluno1);
        assertEquals("Aluno salvo com sucesso!", result);
    }

    @Test
    @DisplayName("Test - salvar aluno com erro")
    void testSaveAlunoComErro() {
        when(alunosRepository.save(any(Alunos.class))).thenThrow(new RuntimeException("Erro ao salvar aluno"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            alunosService.save(aluno1);
        });

        assertEquals("Erro ao salvar aluno", thrown.getMessage());
    }
    @Test
    @DisplayName("Test - atualizar aluno corretamente")
    void testUpdateAlunoCorretamente() {
        when(alunosRepository.findById(anyLong())).thenReturn(Optional.of(aluno1));
        when(alunosRepository.save(any(Alunos.class))).thenReturn(aluno1);
        String result = alunosService.update(aluno1, 1L);
        assertEquals("Aluno atualizado com sucesso!", result);
    }

    @Test
    @DisplayName("Test - atualizar aluno com erro")
    void testUpdateAlunoComErro() {
        when(alunosRepository.findById(anyLong())).thenReturn(Optional.of(aluno1));
        when(alunosRepository.save(any(Alunos.class))).thenThrow(new RuntimeException("Erro ao atualizar aluno"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            alunosService.update(aluno1, 1L);
        });

        assertEquals("Erro ao atualizar aluno", thrown.getMessage());
    }

    @Test
    @DisplayName("Test - buscar aluno por ID")
    void testFindById() {
        when(alunosRepository.findById(anyLong())).thenReturn(Optional.of(aluno1));
        Alunos result = alunosService.findById(1L);
        assertEquals(aluno1, result);
    }

    @Test
    @DisplayName("Test - buscar aluno por ID não encontrado")
    void testFindByIdNaoEncontrado() {
        when(alunosRepository.findById(anyLong())).thenReturn(Optional.empty());
        Alunos result = alunosService.findById(1L);
        assertEquals(null, result);
    }

    @Test
    @DisplayName("Test - buscar todos os alunos")
    void testFindAll() {
        when(alunosRepository.findAll()).thenReturn(Arrays.asList(aluno1, aluno2));
        List<Alunos> result = alunosService.findAll();
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Test - deletar aluno corretamente")
    void testDeleteAlunoCorretamente() {
        when(alunosRepository.findByRa(anyString())).thenReturn(aluno1);
        when(emprestimosRepository.findByEmprestimosByAlunoAtivo(any(Alunos.class))).thenReturn(Collections.emptyList());
        when(alunosRepository.desativarAlunos(anyLong())).thenReturn(1);

        String result = alunosService.delete("505567");
        assertEquals("Aluno desativado com sucesso!", result);
    }
    
    @Test
    @DisplayName("Test - erro ao desativar aluno")
    void testErroDesativarAluno() {
        when(alunosRepository.findByRa(anyString())).thenReturn(aluno1);
        when(emprestimosRepository.findByEmprestimosByAlunoAtivo(any(Alunos.class))).thenReturn(Collections.emptyList());
        when(alunosRepository.desativarAlunos(anyLong())).thenReturn(0); // Simular erro
        
        Exception exception = assertThrows(RuntimeException.class, () -> {
            alunosService.delete("505567");
        });

        assertEquals("Erro ao desativar aluno!", exception.getMessage());
    }

    @Test
    @DisplayName("Test - deletar aluno com empréstimo em andamento")
    void testDeleteAlunoComEmprestimoEmAndamento() {
        when(alunosRepository.findByRa(anyString())).thenReturn(aluno1);
        when(emprestimosRepository.findByEmprestimosByAlunoAtivo(any(Alunos.class))).thenReturn(Arrays.asList(emprestimo1));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            alunosService.delete("505567");
        });

        assertEquals("Aluno possui empréstimo em andamento.", thrown.getMessage());
    }

    @Test
    @DisplayName("Test - reativar aluno corretamente")
    void testReativarAlunoCorretamente() {
        when(alunosRepository.findByRa(anyString())).thenReturn(aluno1);
        when(alunosRepository.reativarAlunos(anyLong())).thenReturn(1);

        String result = alunosService.reativarAluno("505567");
        assertEquals("Aluno reativado com sucesso!", result);
    }

    @Test
    @DisplayName("Test - reativar aluno com erro")
    void testReativarAlunoComErro() {
        when(alunosRepository.findByRa(anyString())).thenReturn(aluno1);
        when(alunosRepository.reativarAlunos(anyLong())).thenReturn(0);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            alunosService.reativarAluno("505567");
        });

        assertEquals("Erro ao reativar aluno", exception.getMessage());
    }

    @Test
    @DisplayName("Test - buscar aluno por RA")
    void testFindByRa() {
        when(alunosRepository.findByRa(anyString())).thenReturn(aluno1);
        Alunos result = alunosService.findByRa("505567");
        assertEquals(aluno1, result);
    }

    @Test
    @DisplayName("Test - buscar aluno por CPF")
    void testFindByCpf() {
        when(alunosRepository.findByCpf(anyString())).thenReturn(aluno1);
        Alunos result = alunosService.findByCpf("232.209.190-14");
        assertEquals(aluno1, result);
    }

    @Test
    @DisplayName("Test - buscar alunos ativos")
    void testFindAlunosAtivos() {
        when(alunosRepository.findByAtivoTrue()).thenReturn(Arrays.asList(aluno1));
        List<Alunos> result = alunosService.findAlunosAtivos();
        assertEquals(1, result.size());
        assertEquals(aluno1, result.get(0));
    }

    @Test
    @DisplayName("Test - buscar alunos inativos")
    void testFindAlunosInativos() {
        when(alunosRepository.findByAtivoFalse()).thenReturn(Collections.singletonList(aluno2));
        List<Alunos> result = alunosService.findAlunosInativos();
        assertEquals(1, result.size());
        assertEquals(aluno2, result.get(0));
    }
    
    @Test
    @DisplayName("Test - buscar aluno por nome")
    void testFindByNome() {
        when(alunosRepository.findByNomeContains(anyString())).thenReturn(Arrays.asList(aluno1));
        List<Alunos> result = alunosService.findByNome("Kanye");
        assertEquals(1, result.size());
        assertEquals(aluno1, result.get(0));
    }
    
    @Test
    void calcularIdade_DeveRetornarIdadeCorreta_QuandoAlunoExiste() {
        // Dados de um aluno de 20 anos
        long alunoId = 1L;
        Alunos aluno = new Alunos();
        aluno.setId(alunoId);
        aluno.setNome("João");
        aluno.setDataNascimento(Date.from(LocalDate.of(2003, 9, 23) // Data de nascimento
                .atStartOfDay(ZoneId.systemDefault()).toInstant()));

        // Mock do método findById do repositório
        when(alunosRepository.findById(alunoId)).thenReturn(Optional.of(aluno));

        // Executa o método a ser testado
        String resultado = alunosService.calcularIdade(alunoId);

        // Verifica o resultado esperado
        assertEquals("A idade do aluno João é: 21 anos.", resultado);
    }

    @Test
    void calcularIdade_DeveLancarExcecao_QuandoAlunoNaoEncontrado() {
        long alunoId = 1L;

        // Mock do método findById para retornar vazio (aluno não encontrado)
        when(alunosRepository.findById(alunoId)).thenReturn(Optional.empty());

        // Executa o método e verifica a exceção
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            alunosService.calcularIdade(alunoId);
        });

        // Verifica a mensagem de erro
        assertEquals("Aluno não encontrado ou data de nascimento não informada.", exception.getMessage());
    }

    @Test
    void calcularIdade_DeveLancarExcecao_QuandoDataNascimentoNula() {
        long alunoId = 1L;
        Alunos aluno = new Alunos();
        aluno.setId(alunoId);
        aluno.setNome("Maria");

        // Mock do método findById para retornar um aluno com data de nascimento nula
        when(alunosRepository.findById(alunoId)).thenReturn(Optional.of(aluno));

        // Executa o método e verifica a exceção
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            alunosService.calcularIdade(alunoId);
        });

        // Verifica a mensagem de erro
        assertEquals("Aluno não encontrado ou data de nascimento não informada.", exception.getMessage());
    }*/
}
