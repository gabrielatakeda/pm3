package app.service.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import app.auth.Usuarios;
import app.auth.UsuariosRepository;
import app.auth.UsuariosService;

@SpringBootTest
public class UsuariosServiceTest {

   /* @InjectMocks
    private UsuariosService usuariosService;

    @Mock
    private UsuariosRepository usuariosRepository;

    private Usuarios usuario1;
    private Usuarios usuario2;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        
        usuario1 = new Usuarios(1L, "Matue Moreira Pinto", "401.635.380-77", "matuezinhodo085", "matue333", true, new ArrayList<>());
        usuario2 = new Usuarios(2L, "Raffa Moreira", "608.635.420-00", "BcRaff", "RaffaMoreira777", false, new ArrayList<>());
        
        // Simula erro ao salvar
        when(usuariosRepository.save(any(Usuarios.class))).thenThrow(new RuntimeException("Erro ao salvar usuário"));
    }

    @Test
    @DisplayName("Test - salvar usuário corretamente")
    void testSaveUsuarioCorretamente() {
        when(usuariosRepository.save(any(Usuarios.class))).thenReturn(usuario1);
        String result = usuariosService.save(usuario1);
        assertEquals("Usuário cadastrado com sucesso!", result);
    }

    @Test
    @DisplayName("Test - salvar usuário com erro")
    void testSaveUsuarioComErro() {
        when(usuariosRepository.save(any(Usuarios.class))).thenThrow(new RuntimeException("Erro ao salvar usuário"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            usuariosService.save(usuario1);
        });

        assertEquals("Erro ao salvar usuário", thrown.getMessage());
    }

    @Test
    @DisplayName("Test - atualizar usuário corretamente")
    void testUpdateUsuarioCorretamente() {
        when(usuariosRepository.findById(anyLong())).thenReturn(Optional.of(usuario1));
        when(usuariosRepository.save(any(Usuarios.class))).thenReturn(usuario1);
        String result = usuariosService.update(usuario1, 1L);
        assertEquals("Usuário atualizado com sucesso!", result);
    }

    @Test
    @DisplayName("Test - buscar usuário por ID")
    void testFindById() {
        when(usuariosRepository.findById(anyLong())).thenReturn(Optional.of(usuario1));
        Usuarios result = usuariosService.findById(1L);
        assertEquals(usuario1, result);
    }

    @Test
    @DisplayName("Test - buscar todos os usuários")
    void testFindAll() {
        when(usuariosRepository.findAll()).thenReturn(Arrays.asList(usuario1, usuario2));
        List<Usuarios> result = usuariosService.findAll();
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Test - deletar usuário corretamente")
    void testDeleteUsuarioCorretamente() {
        // Simula que o repositório encontra o usuário com o ID fornecido
        when(usuariosRepository.findById(anyLong())).thenReturn(Optional.of(usuario1));
        
        // Chama o método delete do serviço
        String result = usuariosService.delete(1L);
        
        // Verifica se o resultado é o esperado
        assertEquals("Usuário desativado com sucesso!", result);
        
        
    }
    
    @Test
    @DisplayName("Test - buscar usuário por nome")
    void testFindByNome() {
        // Simula o comportamento esperado do repositório
        when(usuariosRepository.findByNomeContains(anyString())).thenReturn(Arrays.asList(usuario1));

        // Chama o método da service para buscar o usuário
        List<Usuarios> result = usuariosService.findByNome("Matue Moreira Pinto");

        // Verifica se o resultado contém exatamente 1 usuário
        assertEquals(1, result.size());

        // Verifica se o usuário retornado é o esperado
        assertEquals(usuario1, result.get(0));
    }
    
    @Test
    @DisplayName("Test - erro ao buscar usuário por nome inexistente")
    void testFindByNomeInexistente() {
        // Simula um retorno vazio quando o nome não é encontrado
        when(usuariosRepository.findByNomeContains(anyString())).thenReturn(Collections.emptyList());

        // Chama o método da service para buscar um nome inexistente
        List<Usuarios> result = usuariosService.findByNome("Nome Inexistente");

        // Verifica se o resultado está vazio
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Test - erro ao buscar usuário por CPF inexistente")
    void testFindByCpfInexistente() {
        // Simula que o repositório retorna null para um CPF não cadastrado
        when(usuariosRepository.findByCpf(anyString())).thenReturn(null);

        // Verifica se o resultado é null ao buscar por um CPF inexistente
        Usuarios result = usuariosService.findByCpf("999.999.999-99");

        // Verifica se o resultado é null
        assertEquals(null, result);
    }

    @Test
    @DisplayName("Test - erro ao buscar usuário por ID inexistente")
    void testFindByIdInexistente() {
        // Simula que o repositório retorna Optional vazio ao buscar um ID inexistente
        when(usuariosRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Verifica se uma exceção é lançada ao buscar por um ID inexistente
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            usuariosService.findById(999L);
        });

        // Verifica a mensagem da exceção
        assertEquals("Usuário não encontrado", thrown.getMessage());
    }

    @Test
    @DisplayName("Test - erro ao atualizar usuário inexistente")
    void testUpdateUsuarioInexistente() {
        // Simula que o repositório retorna Optional vazio ao buscar um ID inexistente
        when(usuariosRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Verifica se uma exceção é lançada ao tentar atualizar um usuário que não existe
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            usuariosService.update(usuario1, 999L);
        });

        // Verifica a mensagem da exceção
        assertEquals("Erro ao salvar usuário", thrown.getMessage());
    }

    @DisplayName("Test - erro ao desativar usuário")
    void testDeleteUsuarioComErro() {
        // Simula que o repositório retorna um erro ao desativar o usuário
        when(usuariosRepository.findById(anyLong())).thenReturn(Optional.of(usuario1));
        // Simula a exceção lançada pelo repositório ao tentar desativar o usuário
        when(usuariosRepository.desativarUsuarios(anyLong())).thenThrow(new RuntimeException("Erro ao desativar usuário"));

        // Verifica se uma exceção é lançada ao tentar desativar um usuário
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            usuariosService.delete(1L);
        });

        // Verifica a mensagem da exceção
        assertEquals("Erro ao desativar usuário: Erro ao desativar usuário", thrown.getMessage());
    }
    
    @Test
    @DisplayName("Test - verificar se desativarUsuarios é chamado")
    void testDeleteUsuarioVerificaMetodoChamado() {
        when(usuariosRepository.findById(anyLong())).thenReturn(Optional.of(usuario1));
        
        usuariosService.delete(1L);
        
        verify(usuariosRepository).desativarUsuarios(1L);
    }
    
    @Test
    @DisplayName("Test - erro ao deletar usuário inexistente")
    void testDeleteUsuarioInexistente() {
        // Simula que o repositório retorna Optional vazio ao buscar um ID inexistente
        when(usuariosRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Verifica se uma exceção é lançada ao tentar deletar um usuário que não existe
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            usuariosService.delete(999L);
        });

        // Verifica a mensagem da exceção
        assertEquals("Usuário não encontrado", thrown.getMessage());
    }*/
}
