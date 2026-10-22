package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.service.AuditoriaService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
@CrossOrigin("*")
public class AuditoriaController {
	
	 @Autowired
	    private AuditoriaService auditoriaService;

	    /*@GetMapping("/audit")
	    public List<Object[]> getAuditoria() {
	        return auditoriaService.listarTudoAuditoria();
	    }*/
	 
	 @GetMapping("/audit")
	 public ResponseEntity<List<Object[]>> listarAuditoriaComFiltro(
	     @RequestParam(required = false) String entidade,
	     @RequestParam(required = false) String criadoPor,
	     @RequestParam(required = false) String modificadoPor,
	     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
	     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim

	 ) {
	     List<Object[]> resultado = auditoriaService.listarTudoAuditoria(entidade, criadoPor, modificadoPor, dataInicio, dataFim);
	     return ResponseEntity.ok(resultado);
	 }
	    
	    
}
