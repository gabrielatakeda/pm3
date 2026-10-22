package app.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import app.entity.Alunos;
import app.entity.Equipamentos;
import jakarta.transaction.Transactional;

public interface AlunosRepository extends JpaRepository<Alunos, Long>{

	public Alunos findByRa(String ra);
	
	public List<Alunos> findByNomeContains(String nome);
	
	public Alunos findByCpf(String cpf);
	
	public List<Alunos> findByAtivoTrue();
	
	public List<Alunos> findByAtivoFalse();
	
	@Modifying
	@Transactional
	@Query("UPDATE Alunos a SET a.ativo = false WHERE a.id = :id")
	public int desativarAlunos(long id);
	
	@Modifying
	@Transactional
	@Query("UPDATE Alunos a SET a.ativo = true WHERE a.id = :id")
	public int reativarAlunos(long id);
	
	@Query("FROM Alunos a WHERE " +
		       "( :ra = '' or a.ra LIKE CONCAT('%', :ra, '%') ) AND " +
		       "( :curso = '' or a.curso LIKE CONCAT('%', :curso, '%') ) AND " +
		       "( :nome = '' or a.nome LIKE CONCAT('%', :nome, '%') )")
		public List<Alunos> findByFilter(
		       @Param("ra") String ra,
		       @Param("nome") String nome,
		       @Param("curso") String curso);
	
	/*@Query("SELECT u.id, u.createdBy, u.createdDate, u.lastModifiedBy, u.lastModifiedDate FROM Alunos u")
	List<Object[]> findAuditoriaAlunos();*/
	
	/*@Query("""
		    SELECT a.id, a.createdBy, a.createdDate, a.lastModifiedBy, a.lastModifiedDate
		    FROM Alunos a
		    WHERE (:criadoPor IS NULL OR a.createdBy LIKE CONCAT('%', :criadoPor, '%'))
		      AND (:modificadoPor IS NULL OR a.lastModifiedBy LIKE CONCAT('%', :modificadoPor, '%'))
		      AND (:dataInicio IS NULL OR a.createdDate >= :dataInicio OR a.lastModifiedDate >= :dataInicio)
			  AND (:dataFim IS NULL OR a.createdDate <= :dataFim OR a.lastModifiedDate <= :dataFim)
		""")
		List<Object[]> findAuditoriaAlunosComFiltro(
		    @Param("criadoPor") String criadoPor,
		    @Param("modificadoPor") String modificadoPor,
		    @Param("dataInicio") LocalDateTime dataInicio,
		    @Param("dataFim") LocalDateTime dataFim
		);*/
	
	@Query("""
		    SELECT em.id, em.createdBy, em.createdDate, em.lastModifiedBy, em.lastModifiedDate
		    FROM Alunos em
		    WHERE (:criadoPor IS NULL OR em.createdBy LIKE CONCAT('%', :criadoPor, '%'))
		      AND (:modificadoPor IS NULL OR em.lastModifiedBy LIKE CONCAT('%', :modificadoPor, '%'))
		      AND (
		        (:dataInicio IS NULL AND :dataFim IS NULL)
		        OR (:dataInicio IS NOT NULL AND :dataFim IS NULL AND (em.createdDate >= :dataInicio OR em.lastModifiedDate >= :dataInicio))
		        OR (:dataInicio IS NULL AND :dataFim IS NOT NULL AND (em.createdDate <= :dataFim OR em.lastModifiedDate <= :dataFim))
		        OR (:dataInicio IS NOT NULL AND :dataFim IS NOT NULL AND 
		            ((em.createdDate BETWEEN :dataInicio AND :dataFim) OR 
		             (em.lastModifiedDate BETWEEN :dataInicio AND :dataFim)))
		      )
		""")
		List<Object[]> findAuditoriaAlunosComFiltro(
		    @Param("criadoPor") String criadoPor,
		    @Param("modificadoPor") String modificadoPor,
		    @Param("dataInicio") LocalDateTime dataInicio,
		    @Param("dataFim") LocalDateTime dataFim
		);



}
