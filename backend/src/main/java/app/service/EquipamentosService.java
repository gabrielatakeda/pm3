package app.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import app.entity.Emprestimos;
import app.entity.Equipamentos;
import app.repository.EmprestimosRepository;
import app.repository.EquipamentosRepository;

@Service
public class EquipamentosService {

	@Autowired
	private EquipamentosRepository equipamentosRepository;

	@Autowired
	private EmprestimosRepository emprestimosRepository;

	public String save(Equipamentos equipamentos) {
		equipamentos.setAtivo(true);
		equipamentos.setSituacao("Disponível");
		Equipamentos equip = this.equipamentosRepository.save(equipamentos);
		if(equip != null) {
		   return "Equipamento salvo com sucesso!";
		}else {
			throw new RuntimeException("Erro ao salvar equipamento!");
		}
		
	}

	public String update(Equipamentos equipamentos, long id) {
		equipamentos.setId(id);
		System.out.println("ID do Equipamento recebido na service: " + equipamentos.getId());
		System.out.println("Patrimônio do Equipamento recebido na service: " + equipamentos.getPatrimonio());
		Equipamentos equip = this.equipamentosRepository.save(equipamentos);
		if(equip != null) {
			   return "Equipamento salvo com sucesso!";
			}else {
				throw new RuntimeException("Erro ao salvar equipamento!");
			}
	}

	public Equipamentos findById(long id) {

		Optional<Equipamentos> optional = this.equipamentosRepository.findById(id);
		if (optional.isPresent()) {
			return optional.get();
		} else
			return null;

	}
	
	public List<Equipamentos> findAll() {

		return this.equipamentosRepository.findAll();

	}
	
	
	public Page<Equipamentos> findAllPage(Pageable pageable) {
	    return this.equipamentosRepository.findAll(pageable);
	}

	public String delete(String patrimonio) {
		
		Equipamentos equipamento = this.equipamentosRepository.findByPatrimonio(patrimonio);
		long id = equipamento.getId();
		equipamento.setId(id);
		Emprestimos emp = new Emprestimos();
		emp.setEquipamento(equipamento);

		Emprestimos lista = this.encontrarEmprestimoEmAndamentoPorEquip(emp);

		if (lista != null) {
			throw new RuntimeException("Equipamento possui empréstimo em andamento!");
		}else {
			equipamento.setAtivo(false);
			equipamento.setSituacao("Desativado");
			/*int equipDesativado = this.equipamentosRepository.desativarEquipamentos(id, "Desativado");
		    if (equipDesativado > 0) {
		        return "Equipamento desativado com sucesso!";
		    } else {
		        throw new RuntimeException("Erro ao desativar equipamento!");
		    }*/
			
			Equipamentos equipDel = this.equipamentosRepository.save(equipamento);
			
			if(equipDel != null) {
				return "Equipamento desativado com sucesso!";
			}
			else {
		        throw new RuntimeException("Erro ao desativar equipamento!");
		    }
		}
		
		
	}

	public Emprestimos encontrarEmprestimoEmAndamentoPorEquip(Emprestimos emp){
		Equipamentos equipamento = new Equipamentos();
		equipamento.setPatrimonio(emp.getEquipamento().getPatrimonio());
		Optional<Emprestimos> lista = this.emprestimosRepository.findByEmprestimosByEquipamentoAtivo(equipamento.getPatrimonio());
		if(lista.isPresent())		
			return lista.get();
		else
			return null;
	}
	
	
	
	public String reativarEquipamento(String patrimonio) {
	    Equipamentos equipamento = this.equipamentosRepository.findByPatrimonio(patrimonio);
	    long id = equipamento.getId();
	    
	    equipamento.setAtivo(true);
	    equipamento.setSituacao("Disponível");
	    /*int equipamentoReativado = this.equipamentosRepository.reativarEquipamentos(id, "Disponível");
	    if (equipamentoReativado > 0) {
	        return "Equipamento reativado com sucesso!";
	    } else {
	        throw new RuntimeException("Erro ao reativar equipamento!");
	    }*/
	    
	    Equipamentos equipDel = this.equipamentosRepository.save(equipamento);
		
		if(equipDel != null) {
			return "Equipamento reativado com sucesso!";
		}
		else {
	        throw new RuntimeException("Erro ao reativar equipamento!");
	    }
	}
	
	public List<Equipamentos> findByFilter(String situacao, String patrimonio, String modelo, String marca) {
	    return this.equipamentosRepository.findByFilter(situacao, patrimonio, modelo, marca);
	}
	

	public Equipamentos findByPatrimonio(String patrimonio) {
		return this.equipamentosRepository.findByPatrimonio(patrimonio);
	}

	public List<Equipamentos> findByMarca(String marca) {
		return this.equipamentosRepository.findByMarcaContains(marca);
	}
	/*public List<Equipamentos> equipamentosFiltrados(String modelo, String marca, String patrimonio, String situacao) {
		return this.equipamentosRepository.equipamentosFiltrados(modelo, marca, patrimonio, situacao);
	}*/

	public List<Equipamentos> findByModelo(String modelo) {
		return this.equipamentosRepository.findByModeloContains(modelo);
	}

	public List<Equipamentos> findByDataAquisicao(LocalDate data1, LocalDate data2) {
		return this.equipamentosRepository.findByDataAquisicao(data1, data2);
	}

	public List<Equipamentos> findBySituacao(String situacao) {
	    return this.equipamentosRepository.findBySituacao(situacao);
	}
	
	public List<Equipamentos> findEquipamentosAtivos(){
		return this.equipamentosRepository.findByAtivoTrue();
	}
	
	public List<Equipamentos> findEquipamentosInativos(){
		return this.equipamentosRepository.findByAtivoFalse();
	}

}