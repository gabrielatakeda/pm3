package app.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import app.entity.Alunos;
import app.entity.Emprestimos;
import jakarta.transaction.Transactional;

public interface EmprestimosRepository extends JpaRepository<Emprestimos, Long>{
	
	//public List<Emprestimos> findByAlunos_Ra(String ra);
	
	public List<Emprestimos> findBySituacao(String situacao);
	
	public List<Emprestimos> findByAlunoRa(String ra);
	
	public List<Emprestimos> findByEquipamentoPatrimonio(String patrimonio);
	
	public List<Emprestimos> findByUsuarioNomeContains(String usuarioNome);
	

	@Query("FROM Emprestimos e WHERE e.dataRetirada BETWEEN :data1 AND :data2")
	public List<Emprestimos> findByDataRetirada(LocalDateTime data1, LocalDateTime data2);
	
	@Query("FROM Emprestimos e WHERE e.dataDevolucao BETWEEN :data1 AND :data2")
	public List<Emprestimos> findByDataDevolucao(LocalDateTime data1, LocalDateTime data2);
	
	@Query("FROM Emprestimos e WHERE e.aluno = :aluno AND e.situacao = 'Em Andamento'")
	public List<Emprestimos> findByEmprestimosByAlunoAtivo(Alunos aluno);
	
	/*@Query("FROM Emprestimos e WHERE e.equipamento = :equipamento AND e.situacao = 'Em Andamento'")
	public List<Emprestimos> findByEmprestimosByEquipamentoAtivo(Equipamentos equipamento);*/
	
	@Query("FROM Emprestimos e WHERE e.equipamento.patrimonio = :patrimonio AND e.situacao = 'Em Andamento'")
	public Optional<Emprestimos> findByEmprestimosByEquipamentoAtivo(String patrimonio);
	
	@Query("SELECT e FROM Emprestimos e WHERE " +
			   "(:dataRetirada IS NULL OR e.dataRetirada >= :dataRetirada) AND " +
	           "(:dataDevolucao IS NULL OR e.dataDevolucao <= :dataDevolucao) AND " +
	           "(:situacao = '' OR e.situacao = :situacao) AND " +
	           "(:ra = '' OR e.aluno.ra = :ra) AND " +
	           "(:usuario = '' OR e.usuario.nome = :usuario) AND " +
	           "(:patrimonio = '' OR e.equipamento.patrimonio = :patrimonio)")
	    List<Emprestimos> findByFilter(
	        @Param("dataRetirada") LocalDateTime dataRetirada,
	        @Param("dataDevolucao") LocalDateTime dataDevolucao,
	        @Param("situacao") String situacao,
	        @Param("ra") String ra,
	        @Param("usuario") String usuario,
	        @Param("patrimonio") String patrimonio);
	
	@Modifying
	@Transactional
	@Query("UPDATE Emprestimos e SET e.dataDevolucao = :date, e.situacao = :situacao WHERE e.id = :id")
	public void encerrarEmprestimos(long id, LocalDateTime date, String situacao);
	
	/*@Query("SELECT u.id, u.createdBy, u.createdDate, u.lastModifiedBy, u.lastModifiedDate FROM Emprestimos u")
	List<Object[]> findAuditoriaEmprestimos();*/

	/*@Query("""
		    SELECT em.id, em.createdBy, em.createdDate, em.lastModifiedBy, em.lastModifiedDate
		    FROM Emprestimos em
		    WHERE (:criadoPor IS NULL OR em.createdBy LIKE CONCAT('%', :criadoPor, '%'))
		      AND (:modificadoPor IS NULL OR em.lastModifiedBy LIKE CONCAT('%', :modificadoPor, '%'))
		      AND (:dataInicio IS NULL OR em.createdDate >= :dataInicio OR em.lastModifiedDate >= :dataInicio)
			  AND (:dataFim IS NULL OR em.createdDate <= :dataFim OR em.lastModifiedDate <= :dataFim)
		""")
		List<Object[]> findAuditoriaEmprestimosComFiltro(
		    @Param("criadoPor") String criadoPor,
		    @Param("modificadoPor") String modificadoPor,
		    @Param("dataInicio") LocalDateTime dataInicio,
		    @Param("dataFim") LocalDateTime dataFim
		);*/
	
	@Query("""
		    SELECT em.id, em.createdBy, em.createdDate, em.lastModifiedBy, em.lastModifiedDate
		    FROM Emprestimos em
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
		List<Object[]> findAuditoriaEmprestimosComFiltro(
		    @Param("criadoPor") String criadoPor,
		    @Param("modificadoPor") String modificadoPor,
		    @Param("dataInicio") LocalDateTime dataInicio,
		    @Param("dataFim") LocalDateTime dataFim
		);




}
