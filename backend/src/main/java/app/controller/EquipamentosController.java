package app.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.entity.Equipamentos;
import app.service.EquipamentosService;
import jakarta.validation.Valid;


@Validated
@RestController
@RequestMapping("/api/equipamentos")
@CrossOrigin("*")
public class EquipamentosController {

	@Autowired
	private EquipamentosService equipamentosService;
	

	@PostMapping("/save")
	public ResponseEntity<String> save(@Valid @RequestBody Equipamentos equipamento){
		try {
			String mensagem = this.equipamentosService.save(equipamento);
			return new ResponseEntity<>(mensagem, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Deu erro!"+e.getMessage(), HttpStatus.BAD_REQUEST );
		}
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<String> update(@Valid @RequestBody Equipamentos equipamento, @PathVariable long id){
		try {
			System.out.println("ID do Equipamento: " + id);
			String mensagem = this.equipamentosService.update(equipamento, id);
			return new ResponseEntity<>(mensagem, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Deu erro! "+e.getMessage(), HttpStatus.BAD_REQUEST );
		}
	}

	@GetMapping("/findById/{id}")
	public ResponseEntity<Equipamentos> findById(@PathVariable long id){
		try {
			Equipamentos equipamento = this.equipamentosService.findById(id);
			return new ResponseEntity<>(equipamento, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST );
		}
	}
	
	
	@GetMapping("/findAll")
	public ResponseEntity<List<Equipamentos>> findAll(){
		try {
			List<Equipamentos> lista = this.equipamentosService.findAll();
			return new ResponseEntity<>(lista, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST );
		}
	}
	
	
	@GetMapping("/findAllPage")
	public ResponseEntity<Page<Equipamentos>> findAll(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size) {
	    try {
	        Pageable pageable = PageRequest.of(page, size);
	        Page<Equipamentos> paginaEquipamentos = this.equipamentosService.findAllPage(pageable);
	        return new ResponseEntity<>(paginaEquipamentos, HttpStatus.OK);
	    } catch (Exception e) {
	        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	    }
	}
	
	@GetMapping("/findByPatrimonio")
	public ResponseEntity<Equipamentos> findByPatrimonio(@RequestParam String patrimonio){
		try {
			Equipamentos equipamento = this.equipamentosService.findByPatrimonio(patrimonio);
			return new ResponseEntity<>(equipamento, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST );
		}
	}
	
	@GetMapping("/findBySituacao")
	public ResponseEntity<List<Equipamentos>> findBySituacao(@RequestParam String situacao){
		try {
			List<Equipamentos> lista = this.equipamentosService.findBySituacao(situacao);
			return new ResponseEntity<>(lista, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST );
		}
	}
	
	@GetMapping("/findByFilter")
	public ResponseEntity<List<Equipamentos>> findByFilter(@RequestParam("situacao") String situacao, @RequestParam("patrimonio") String patrimonio,
			@RequestParam("modelo") String modelo, @RequestParam("marca") String marca){
		try {
			List<Equipamentos> lista = this.equipamentosService.findByFilter(situacao, patrimonio, modelo, marca);
			return new ResponseEntity<>(lista, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST );
		}
	}
	
	@GetMapping("/findByMarca")
	public ResponseEntity<List<Equipamentos>> findByMarca(@RequestParam String marca){
		try {
			List<Equipamentos> lista = this.equipamentosService.findByMarca(marca);
			return new ResponseEntity<>(lista, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST );
		}
	}
	
	@GetMapping("/findByModelo")
	public ResponseEntity<List<Equipamentos>> findByModelo(@RequestParam String modelo){
		try {
			List<Equipamentos> lista = this.equipamentosService.findByModelo(modelo);
			return new ResponseEntity<>(lista, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST );
		}
	}
	
	@GetMapping("/findByDataAquisicao")
	public ResponseEntity<List<Equipamentos>> findByDataAquisicao(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data1,
	        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data2){
	    try {
	        List<Equipamentos> lista = this.equipamentosService.findByDataAquisicao(data1, data2);
	        return new ResponseEntity<>(lista, HttpStatus.OK);
	    } catch (Exception e) {
	        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST );
	    }
	}
	
	@GetMapping("/findByEquipamentoAtivo")
	public ResponseEntity<List<Equipamentos>> findByEquipamentoAtivo(){
		try {
			List<Equipamentos> equip = this.equipamentosService.findEquipamentosAtivos();
			return new ResponseEntity<>(equip, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST );
		}
	}
	
	@GetMapping("/findByEquipamentoInativo")
	public ResponseEntity<List<Equipamentos>> findByEquipamentoInativo(){
		try {
			List<Equipamentos> equip = this.equipamentosService.findEquipamentosInativos();
			return new ResponseEntity<>(equip, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST );
		}
	}
	
	@PutMapping("/desativarEquipamento")
	public ResponseEntity<String> desativar(@RequestParam String patrimonio){
		try {
			String mensagem = this.equipamentosService.delete(patrimonio);
			return new ResponseEntity<>(mensagem, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST );
		}
	}
	
	@PutMapping("/reativarEquipamento")
	public ResponseEntity<String> reativar(@RequestParam String patrimonio){
		try {
			String mensagem = this.equipamentosService.reativarEquipamento(patrimonio);
			return new ResponseEntity<>(mensagem, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST );
		}
	}
	
}

