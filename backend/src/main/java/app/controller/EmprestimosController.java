package app.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import app.entity.Emprestimos;
import app.entity.Equipamentos;
import app.service.EmprestimosService;
import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/emprestimos")
@CrossOrigin("*")
public class EmprestimosController {

	@Autowired
	private EmprestimosService emprestimosService;
	

	@PostMapping("/save")
	public ResponseEntity<String> save(@Valid @RequestBody Emprestimos emprestimo){
		try {
			String mensagem = this.emprestimosService.save(emprestimo);
			return new ResponseEntity<>(mensagem, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST );
		}
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<String> update(@Valid @RequestBody Emprestimos emprestimo, @PathVariable long id){
		try {
			String mensagem = this.emprestimosService.update(emprestimo, id);
			return new ResponseEntity<>(mensagem, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST );
		}
	}

	@GetMapping("/findById/{id}")
	public ResponseEntity<Emprestimos> findById(@PathVariable long id){
		try {
			Emprestimos emprestimo = this.emprestimosService.findById(id);
			return new ResponseEntity<>(emprestimo, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST );
		}
	}
	
	@GetMapping("/findAll")
	public ResponseEntity<List<Emprestimos>> findAll(){
		 System.out.println("Recebi uma requisição para findAll()!");
		try {
			List<Emprestimos> lista = this.emprestimosService.findAll();
			return new ResponseEntity<>(lista, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST );
		}
	}
	
	@GetMapping("/findAllPage")
	public ResponseEntity<Page<Emprestimos>> findAll(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size) {
	    try {
	        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dataRetirada"));
	        Page<Emprestimos> paginaEmprestimo = this.emprestimosService.findAllPage(pageable);
	        return new ResponseEntity<>(paginaEmprestimo, HttpStatus.OK);
	    } catch (Exception e) {
	        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	    }
	}

	
	@GetMapping("/findByFilter")
	public ResponseEntity<List<Emprestimos>> findByFilter(
			@RequestParam(value="dataRetirada",required = false) LocalDateTime dataRetirada,
			@RequestParam(value="dataDevolucao",required = false) LocalDateTime dataDevolucao,
			@RequestParam("situacao") String situacao, 
			@RequestParam("ra") String ra, @RequestParam("usuario") String usuario, 
			@RequestParam("patrimonio") String patrimonio){
		try {
			
			System.out.println(dataRetirada);
			System.out.println(dataDevolucao);
			if(dataDevolucao != null) {
				dataDevolucao = dataDevolucao.withHour(23);
				dataDevolucao = dataDevolucao.withMinute(59);
				dataDevolucao = dataDevolucao.withSecond(59);
			}
			
			List<Emprestimos> lista = this.emprestimosService.findByFilter(dataRetirada, dataDevolucao, situacao, ra,
		    		usuario, patrimonio);
			return new ResponseEntity<>(lista, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST );
		}
	}

	
	@GetMapping("/findBySituacao")
	public ResponseEntity<List<Emprestimos>> findBySituacao(@RequestParam String situacao){
		try {
			List<Emprestimos> lista = this.emprestimosService.findBySituacao(situacao);
			return new ResponseEntity<>(lista, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/findByUsuario")
	public ResponseEntity<List<Emprestimos>> findByUsuario(@RequestParam String usuarioNome){
		try {
			List<Emprestimos> lista = this.emprestimosService.findByUsuario(usuarioNome);
			return new ResponseEntity<>(lista, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/findByAlunoRa")
	public ResponseEntity<List<Emprestimos>> findByAluno(@RequestParam String ra){
		try {
			List<Emprestimos> lista = this.emprestimosService.findByAluno(ra);
			return new ResponseEntity<>(lista, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/findByEquipamentoPatrimonio")
	public ResponseEntity<List<Emprestimos>> findByEquipamento(@RequestParam String patrimonio){
		try {
			List<Emprestimos> lista = this.emprestimosService.findByEquipamento(patrimonio);
			return new ResponseEntity<>(lista, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/findByEquipamentoPorEmprestimoAtivo")
	public ResponseEntity<Emprestimos> findByEquipamentoPorEmprestimoAtivo(@RequestParam String patrimonio){
		try {
			Optional<Emprestimos> lista = this.emprestimosService.encontrarEmprestimoEmAndamentoPorEquip(patrimonio);
			if(lista.isPresent())
				return new ResponseEntity<>(lista.get(), HttpStatus.OK);
			else
				return new ResponseEntity<>(null, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/findByDataRetirada")
	public ResponseEntity<List<Emprestimos>> findByDataRetirada(
	        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime data1,
	        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime data2) {
	    try {
			List<Emprestimos> lista = this.emprestimosService.findByDataRetirada(data1, data2);
			return new ResponseEntity<>(lista, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/findByDataDevolucao")
	public ResponseEntity<List<Emprestimos>> findByDataDevolucao(
	        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime data1,
	        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime data2) {
	    try {
			List<Emprestimos> lista = this.emprestimosService.findByDataDevolucao(data1, data2);
			return new ResponseEntity<>(lista, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/encerrar/{id}")
	public ResponseEntity<String> encerrar(@PathVariable long id){
		try {
			String mensagem = this.emprestimosService.encerrarEmprestimo(id);
			return new ResponseEntity<>(mensagem, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST );
		}
	}
	
}

