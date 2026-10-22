package app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import app.entity.Alunos;
import app.entity.Emprestimos;
import app.entity.Equipamentos;
import app.service.AlunosService;
import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/alunos")
@CrossOrigin("*")
public class AlunosController {

	@Autowired
	private AlunosService alunosService;
	

	@PostMapping("/save")
	public ResponseEntity<String> save(@Valid@RequestBody Alunos aluno){
		try {
			String mensagem = this.alunosService.save(aluno);
			return new ResponseEntity<>(mensagem, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Deu erro!"+e.getMessage(), HttpStatus.BAD_REQUEST );
		}
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<String> update(@Valid@RequestBody Alunos aluno, @PathVariable long id){
		try {
			String mensagem = this.alunosService.update(aluno, id);
			return new ResponseEntity<>(mensagem, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Deu erro!"+e.getMessage(), HttpStatus.BAD_REQUEST );
		}
	}
	
	@GetMapping("/findAllPage")
	public ResponseEntity<Page<Alunos>> findAll(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size) {
	    try {
	        Pageable pageable = PageRequest.of(page, size);
	        Page<Alunos> paginaEmprestimo = this.alunosService.findAllPage(pageable);
	        return new ResponseEntity<>(paginaEmprestimo, HttpStatus.OK);
	    } catch (Exception e) {
	        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	    }
	}

	@GetMapping("/findById/{id}")
	public ResponseEntity<Alunos> findById(@PathVariable long id){
		try {
			Alunos aluno = this.alunosService.findById(id);
			return new ResponseEntity<>(aluno, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST );
		}
	}
	
	@GetMapping("/findAll")
	public ResponseEntity<List<Alunos>> findAll(){
		try {
			List<Alunos> lista = this.alunosService.findAll();
			return new ResponseEntity<>(lista, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST );
		}
	}
	
	@GetMapping("/findByRa")
	public ResponseEntity<Alunos> findByRa(@RequestParam String ra){
		try {
			Alunos aluno = this.alunosService.findByRa(ra);
			return new ResponseEntity<>(aluno, HttpStatus.OK);
		} catch (Exception e) {
			
			System.out.println(e.getMessage());
			
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST );
		}
	}
	
	@GetMapping("/findByFilter")
	public ResponseEntity<List<Alunos>> findByFilter(@RequestParam("ra") String ra, @RequestParam("nome") String nome,
			@RequestParam("curso") String curso){
		try {
			List<Alunos> lista = this.alunosService.findByFilter(ra, nome, curso);
			return new ResponseEntity<>(lista, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST );
		}
	}
	
	@GetMapping("/findByNome")
	public ResponseEntity<List<Alunos>> findByNome(@RequestParam String nome){
		try {
			List<Alunos> aluno = this.alunosService.findByNome(nome);
			return new ResponseEntity<>(aluno, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST );
		}
	}
	
	@GetMapping("/findByCpf")
	public ResponseEntity<Alunos> findByCpf(@RequestParam String cpf){
		try {
			Alunos aluno = this.alunosService.findByCpf(cpf);
			return new ResponseEntity<>(aluno, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST );
		}
	}
	
	@PutMapping("/desativarAluno")
	public ResponseEntity<String> delete(@RequestParam String ra){
		try {
			System.out.println("ra="+ra);
			String mensagem = this.alunosService.delete(ra);
			return new ResponseEntity<>(mensagem, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST );
		}
	}
	
	@PutMapping("/reativarAluno")
	public ResponseEntity<String> reativar(@RequestParam String ra){
		try {
			String mensagem = this.alunosService.reativar(ra);
			return new ResponseEntity<>(mensagem, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST );
		}
	}
	
}

