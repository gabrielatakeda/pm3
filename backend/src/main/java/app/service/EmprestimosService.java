package app.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import app.entity.Alunos;
import app.entity.Emprestimos;
import app.entity.Equipamentos;
import app.repository.EmprestimosRepository;
import jakarta.transaction.Transactional;

@Service
public class EmprestimosService {
	
	@Autowired
	private EmprestimosRepository emprestimosRepository;
	

	public String save (Emprestimos emprestimos) {
		
		List<Emprestimos> lista = this.encontrarEmprestimoEmAndamentoPorAluno(emprestimos);
		Optional<Emprestimos> equip = this.encontrarEmprestimoEmAndamentoPorEquip(emprestimos.getEquipamento().getPatrimonio());
		
		if(lista != null && !lista.isEmpty()) {
			throw new RuntimeException("Aluno já possui empréstimo em andamento!");
		}else if(equip.isPresent()) {
			throw new RuntimeException("Equipamento já possui empréstimo em andamento!");
		}else if(!emprestimos.getAluno().isAtivo()) {
			throw new RuntimeException("Aluno não possui RA ativo!");
		}else if(!emprestimos.getEquipamento().isAtivo() || !emprestimos.getEquipamento().getSituacao().equals("Disponível")) {
			throw new RuntimeException("Equipamento não está apto para empréstimo!");
		}
		
		emprestimos.setDataRetirada(LocalDateTime.now());
		emprestimos.setSituacao("Em Andamento");
		Emprestimos emp = this.emprestimosRepository.save(emprestimos);
		 
		if(emp != null) {
			return "Empréstimo salvo com sucesso!";
		}else {
			throw new RuntimeException("Erro ao salvar empréstimo!");
		}
	
	}
	
	public List<Emprestimos> encontrarEmprestimoEmAndamentoPorAluno(Emprestimos emp){
		Alunos aluno = emp.getAluno();
		//aluno.setId(emp.getAluno().getId());
		List<Emprestimos> lista = this.emprestimosRepository.findByEmprestimosByAlunoAtivo(aluno);
		return lista;
	}
	
	public Optional<Emprestimos> encontrarEmprestimoEmAndamentoPorEquip(String patrimonio){
		Equipamentos equipamento = new Equipamentos();
		equipamento.setPatrimonio(patrimonio);
		//equipamento.setId(emp.getEquipamento().getId());
		Optional<Emprestimos> emprestimo = this.emprestimosRepository.findByEmprestimosByEquipamentoAtivo(equipamento.getPatrimonio());
		return emprestimo;
	}
	
	public String update (Emprestimos emprestimos, long id) {
		emprestimos.setId(id);
		Emprestimos emp = this.emprestimosRepository.save(emprestimos);
		if(emp != null) {
			return "Empréstimo atualizado com sucesso!";
		}else {
			throw new RuntimeException("Erro ao atualizar empréstimo!");
		}
	}
	
	public Emprestimos findById (long id) {
		
		Optional<Emprestimos> optional = this.emprestimosRepository.findById(id);
		if(optional.isPresent()) {
			return optional.get();
		}else
			return null;
		
	}
	
	public List<Emprestimos> findAll () {
			
			return this.emprestimosRepository.findAll();
			
	}
	
	public Page<Emprestimos> findAllPage(Pageable pageable) {
	    return this.emprestimosRepository.findAll(pageable);
	}

	/*public String encerrarEmprestimo(long id) {
		LocalDateTime data = LocalDateTime.now();
		String situacao = "Encerrado";
		this.emprestimosRepository.encerrarEmprestimos(id, data, situacao);
		return "Empréstimo encerrado com sucesso";
	}*/
	
	@Transactional
	public String encerrarEmprestimo(long id) {
	    Emprestimos emprestimo = emprestimosRepository.findById(id)
	        .orElseThrow(() -> new RuntimeException("Empréstimo não encontrado"));

	    emprestimo.setDataDevolucao(LocalDateTime.now());
	    emprestimo.setSituacao("Encerrado");

	    Emprestimos empEnc = emprestimosRepository.save(emprestimo); // <- dispara auditoria
	    
	    if(empEnc != null) {
	    	return "Empréstimo encerrado com sucesso";
	    }else {
	    	throw new RuntimeException("Erro ao encerrar empréstimo!");
	    }
	    
	}
	
	public List<Emprestimos> findByFilter(
		    LocalDateTime dataRetirada,
		    LocalDateTime dataDevolucao,
		    String situacao,
		    String ra,
		    String usuario,
		    String patrimonio) {
		    
		    return emprestimosRepository.findByFilter(dataRetirada, dataDevolucao, situacao, ra, usuario, patrimonio);
		}
	
	public List<Emprestimos> findBySituacao(String situacao){
		return this.emprestimosRepository.findBySituacao(situacao);
	}
	
	public List<Emprestimos> findByUsuario(String usuarioNome){
		return this.emprestimosRepository.findByUsuarioNomeContains(usuarioNome);
	}
	
	public List<Emprestimos> findByAluno(String ra){
		return this.emprestimosRepository.findByAlunoRa(ra);
	}
	
	public List<Emprestimos> findByEquipamento(String equipamento){
		return this.emprestimosRepository.findByEquipamentoPatrimonio(equipamento);
	}
	
	public List<Emprestimos> findByDataRetirada(LocalDateTime data1, LocalDateTime data2){
		return this.emprestimosRepository.findByDataRetirada(data1, data2);
	}
	
	public List<Emprestimos> findByDataDevolucao(LocalDateTime data1, LocalDateTime data2){
		return this.emprestimosRepository.findByDataDevolucao(data1, data2);
	}
	
}