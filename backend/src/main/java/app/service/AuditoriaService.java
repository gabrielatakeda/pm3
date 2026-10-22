package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.auth.UsuariosRepository;
import app.repository.AlunosRepository;
import app.repository.EmprestimosRepository;
import app.repository.EquipamentosRepository;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;


@Service
public class AuditoriaService {

	@Autowired private UsuariosRepository usuariosRepository;
    @Autowired private EmprestimosRepository emprestimosRepository;
    @Autowired private EquipamentosRepository equipamentosRepository;
    @Autowired private AlunosRepository alunosRepository;
    
    public List<Object[]> listarTudoAuditoria(String entidade, String criadoPor, String modificadoPor, LocalDateTime dataInicio, LocalDateTime dataFim) {
        List<Object[]> auditorias = new ArrayList<>();
        
        if(dataFim != null) {
        	dataFim = dataFim.withHour(23).withMinute(59).withSecond(59).withNano(999_000_000);
        }

        if (entidade == null || entidade.equalsIgnoreCase("Usuarios")) {
            usuariosRepository.findAuditoriaUsuariosComFiltro(criadoPor, modificadoPor, dataInicio, dataFim)
                .stream()
                .filter(this::temAlteracao) // Filtra só os que têm alteração
                .forEach(obj -> auditorias.add(prepend("Usuarios", obj)));
        }
        if (entidade == null || entidade.equalsIgnoreCase("Emprestimos")) {
            emprestimosRepository.findAuditoriaEmprestimosComFiltro(criadoPor, modificadoPor, dataInicio, dataFim)
                .stream()
                .filter(this::temAlteracao)
                .forEach(obj -> auditorias.add(prepend("Emprestimos", obj)));
        }
        if (entidade == null || entidade.equalsIgnoreCase("Equipamentos")) {
            equipamentosRepository.findAuditoriaEquipamentosComFiltro(criadoPor, modificadoPor, dataInicio, dataFim)
                .stream()
                .filter(this::temAlteracao)
                .forEach(obj -> auditorias.add(prepend("Equipamentos", obj)));
        }
        if (entidade == null || entidade.equalsIgnoreCase("Alunos")) {
            alunosRepository.findAuditoriaAlunosComFiltro(criadoPor, modificadoPor, dataInicio, dataFim)
                .stream()
                .filter(this::temAlteracao)
                .forEach(obj -> auditorias.add(prepend("Alunos", obj)));
        }

        return auditorias;
    }


    private Object[] prepend(String entidade, Object[] dados) {
        Object[] result = new Object[dados.length + 1];
        result[0] = entidade;
        System.arraycopy(dados, 0, result, 1, dados.length);
        return result;
    }

    private boolean temAlteracao(Object[] dados) {
        // Verifica se qualquer um dos campos de auditoria está preenchido
        return dados[1] != null || dados[2] != null || dados[3] != null || dados[4] != null;
    }


	
}
