package app.controller.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import app.auth.Usuarios;
import app.auth.UsuariosController;
import app.auth.UsuariosService;

@SpringBootTest
public class UsuariosControllerTest {

   /* @InjectMocks
    private UsuariosController usuariosController;
    
    @Autowired
    private UsuariosController usuariosControl;

    @Mock
    private UsuariosService usuariosService;

    private Usuarios usuario1;
    private Usuarios usuario2;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        usuario1 = new Usuarios(1L, "Matue Moreira Pinto", "401.635.380-77", "matuezinhodo085", "matue333", true, new ArrayList<>());
        usuario2 = new Usuarios(2L, "Raffa Moreira", "608.635.420-00", "BcRaff", "RaffaMoreira777", false, new ArrayList<>());
    
    }

    @Test
    @DisplayName("Test - salvar usuario corretamente")
    public void testSaveSuccess() {
        when(usuariosService.save(any(Usuarios.class))).thenReturn("Usuário salvo com sucesso!");

        ResponseEntity<String> response = usuariosController.save(usuario1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuário salvo com sucesso!", response.getBody());
        verify(usuariosService, times(1)).save(any(Usuarios.class));
    }
    
    @Test
    @DisplayName("Test - salvar usuario com erro de validação")
    public void testSaveNotValid() {
        //when(usuariosService.save(any(Usuarios.class))).thenReturn("Usuário salvo com sucesso!");
    	Usuarios usuario = new Usuarios(2, "", "", "", "", false, null);
        
    	assertThrows(Exception.class, () -> {
    		ResponseEntity<String> response = usuariosControl.save(usuario);
    	});
        
    }

    @Test
    @DisplayName("Test - salvar usuario que da errado")
    public void testSaveFailure() {
        // Simulando falha ao salvar usuário
        when(usuariosService.save(any(Usuarios.class))).thenThrow(new RuntimeException("Erro ao salvar usuário"));

        // Chama o método save do controlador e verifica a resposta
        ResponseEntity<String> response = usuariosController.save(usuario1);

        // Verifica o status da resposta e o corpo
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // Verifica o corpo da resposta, ajustando a mensagem conforme a implementação real do controlador
        assertEquals("Deu erro!Erro ao salvar usuário", response.getBody());

        verify(usuariosService, times(1)).save(any(Usuarios.class));
    }


    @Test
    @DisplayName("Test - atualizar usuario corretamente")
    public void testUpdateSuccess() {
        when(usuariosService.update(any(Usuarios.class), eq(1L))).thenReturn("Usuário atualizado com sucesso!");

        ResponseEntity<String> response = usuariosController.update(usuario1, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuário atualizado com sucesso!", response.getBody());
        verify(usuariosService, times(1)).update(any(Usuarios.class), eq(1L));
    }
    
    @Test
    @DisplayName("Test - atualizar usuario com erro de validação")
    public void testUpdateNotValid() {
        //when(usuariosService.save(any(Usuarios.class))).thenReturn("Usuário salvo com sucesso!");
    	Usuarios usuario = new Usuarios(2, "", "", "", "", false, null);
        
    	assertThrows(Exception.class, () -> {
    		ResponseEntity<String> response = usuariosControl.update(usuario,2);
    	});
        
    }

    @Test
    @DisplayName("Test - atualizar usuario com erro")
    public void testUpdateFailure() {
        // Simulando falha ao atualizar usuário
        when(usuariosService.update(any(Usuarios.class), eq(1L))).thenThrow(new RuntimeException("Erro ao atualizar usuário"));

        // Chama o método update do controlador e verifica a resposta
        ResponseEntity<String> response = usuariosController.update(usuario1, 1L);

        // Verifica o status da resposta e o corpo
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Deu erro!Erro ao atualizar usuário", response.getBody());

        verify(usuariosService, times(1)).update(any(Usuarios.class), eq(1L));
    }

    @Test
    @DisplayName("Test - FindById de usuario corretamente")
    public void testFindByIdSuccess() {
        when(usuariosService.findById(1L)).thenReturn(usuario1);

        ResponseEntity<Usuarios> response = usuariosController.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuario1, response.getBody());
        verify(usuariosService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Test - FindById de usuario com erro")
    public void testFindByIdFailure() {
        // Simulando falha ao buscar usuário por ID
        when(usuariosService.findById(1L)).thenThrow(new RuntimeException("Erro ao buscar usuário"));

        // Verifica a resposta do método findById quando ocorre uma falha
        ResponseEntity<Usuarios> response = usuariosController.findById(1L);

        // Verifica o status da resposta e o corpo
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(usuariosService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Test - FindAll de usuario corretamente")
    public void testFindAllSuccess() {
        List<Usuarios> usuarios = new ArrayList<>();
        usuarios.add(usuario1);
        usuarios.add(usuario2);

        when(usuariosService.findAll()).thenReturn(usuarios);

        ResponseEntity<List<Usuarios>> response = usuariosController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuarios, response.getBody());
        verify(usuariosService, times(1)).findAll();
    }

    @Test
    @DisplayName("Test - FindAll de usuario com erro")
    public void testFindAllFailure() {
        // Simulando falha ao buscar todos os usuários
        when(usuariosService.findAll()).thenThrow(new RuntimeException("Erro ao buscar usuários"));

        // Verifica a resposta do método findAll quando ocorre uma falha
        ResponseEntity<List<Usuarios>> response = usuariosController.findAll();

        // Verifica o status da resposta e o corpo
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(usuariosService, times(1)).findAll();
    }

    @Test
    @DisplayName("Test - FindByNome de usuario corretamente")
    public void testFindByNomeSuccess() {
        List<Usuarios> usuarios = new ArrayList<>();
        usuarios.add(usuario1);

        when(usuariosService.findByNome("Matue Moreira Pinto")).thenReturn(usuarios);

        ResponseEntity<List<Usuarios>> response = usuariosController.findByNome("Matue Moreira Pinto");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuarios, response.getBody());
        verify(usuariosService, times(1)).findByNome("Matue Moreira Pinto");
    }

    @Test
    @DisplayName("Test - FindByNome de usuario com erro")
    public void testFindByNomeFailure() {
        // Simulando falha ao buscar usuário por nome
        when(usuariosService.findByNome("Matue Moreira Pinto")).thenThrow(new RuntimeException("Erro ao buscar usuário pelo nome"));

        // Verifica a resposta do método findByNome quando ocorre uma falha
        ResponseEntity<List<Usuarios>> response = usuariosController.findByNome("Matue Moreira Pinto");

        // Verifica o status da resposta e o corpo
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(null, response.getBody());
        verify(usuariosService, times(1)).findByNome("Matue Moreira Pinto");
    }


    @Test
    @DisplayName("Test - FindByCpf de usuario corretamente")
    public void testFindByCpfSuccess() {
        when(usuariosService.findByCpf("401.635.380-77")).thenReturn(usuario1);

        ResponseEntity<Usuarios> response = usuariosController.findByCpf("401.635.380-77");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuario1, response.getBody());
        
    }

    @Test
    @DisplayName("Test - FindByCpf with error")
    public void testFindByCpfFailure() {
    	 // Simulando falha ao buscar usuário por nome
        when(usuariosService.findByCpf("401.635.380-77")).thenThrow(new RuntimeException("Erro ao buscar usuário pelo cpf"));

        // Verifica a resposta do método findByNome quando ocorre uma falha
        ResponseEntity<Usuarios> response = usuariosController.findByCpf("401.635.380-77");

        // Verifica o status da resposta e o corpo
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
         
      
    }

       
 

    @Test
    @DisplayName("Test - Delete usuario corretamente")
    public void testDeleteSuccess() {
        when(usuariosService.delete(1L)).thenReturn("Usuário deletado com sucesso!");

        ResponseEntity<String> response = usuariosController.delete(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuário deletado com sucesso!", response.getBody());
        
    }

    @Test
    @DisplayName("Test - Delete usuario com erro")
    public void testDeleteFailure() {
    	 // Simulando o comportamento do serviço para lançar uma exceção quando um ID é fornecido
        Mockito.when(usuariosService.delete(99L)).thenThrow(new RuntimeException("Usuario não encontrado"));

        // Chama o método delete do controlador com o ID que está simulando a falha
        ResponseEntity<String> response = usuariosController.delete(99L);

        // Verifica se o status HTTP é BAD_REQUEST (400)
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

      

        
    }*/
}
