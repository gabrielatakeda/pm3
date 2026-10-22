package app.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import app.entity.Equipamentos;
import jakarta.transaction.Transactional;

public interface EquipamentosRepository extends JpaRepository<Equipamentos, Long> {

	public Equipamentos findByPatrimonio(String patrimonio);
	
	public List<Equipamentos> findByMarcaContains(String marca);
	
	public List<Equipamentos> findByModeloContains(String modelo);
	
	public List<Equipamentos> findBySituacao(String situacao);
	
	public List<Equipamentos> findByAtivoTrue();
	
	public List<Equipamentos> findByAtivoFalse();
	
	@Query("FROM Equipamentos e WHERE " +
		       "( :modelo = '' or e.modelo LIKE CONCAT('%', :modelo, '%') ) AND " +
		       "( :marca = '' or e.marca LIKE CONCAT('%', :marca, '%') ) AND " +
		       "( :situacao = '' or e.situacao = :situacao ) AND " +
		       "( :patrimonio = '' or e.patrimonio = :patrimonio )")
		public List<Equipamentos> findByFilter(
		       @Param("situacao") String situacao,
		       @Param("patrimonio") String patrimonio,
		       @Param("modelo") String modelo,
		       @Param("marca") String marca);
	
	
	
	@Query("FROM Equipamentos e WHERE e.dataAquisicao BETWEEN :data1 AND :data2")
	public List<Equipamentos> findByDataAquisicao(LocalDate data1, LocalDate data2);
	
	@Modifying
	@Transactional
	@Query("UPDATE Equipamentos e SET e.ativo = false, e.situacao = :situacao WHERE e.id = :id")
	public int desativarEquipamentos(long id, String situacao);
	
	@Modifying
	@Transactional
	@Query("UPDATE Equipamentos e SET e.ativo = true, e.situacao = :situacao WHERE e.id = :id")
	public int reativarEquipamentos(long id, String situacao);
	
	/*@Query("SELECT u.id, u.createdBy, u.createdDate, u.lastModifiedBy, u.lastModifiedDate FROM Equipamentos u")
	List<Object[]> findAuditoriaEquipamentos();*/
	
	/*@Query("""
		    SELECT e.id, e.createdBy, e.createdDate, e.lastModifiedBy, e.lastModifiedDate
		    FROM Equipamentos e
		    WHERE (:criadoPor IS NULL OR e.createdBy LIKE CONCAT('%', :criadoPor, '%'))
		      AND (:modificadoPor IS NULL OR e.lastModifiedBy LIKE CONCAT('%', :modificadoPor, '%'))
		      AND (:dataInicio IS NULL OR e.createdDate >= :dataInicio OR e.lastModifiedDate >= :dataInicio)
			  AND (:dataFim IS NULL OR e.createdDate <= :dataFim OR e.lastModifiedDate <= :dataFim)
		""")
		List<Object[]> findAuditoriaEquipamentosComFiltro(
		    @Param("criadoPor") String criadoPor,
		    @Param("modificadoPor") String modificadoPor,
		    @Param("dataInicio") LocalDateTime dataInicio,
		    @Param("dataFim") LocalDateTime dataFim
		);*/
	
	@Query("""
		    SELECT em.id, em.createdBy, em.createdDate, em.lastModifiedBy, em.lastModifiedDate
		    FROM Equipamentos em
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
		List<Object[]> findAuditoriaEquipamentosComFiltro(
		    @Param("criadoPor") String criadoPor,
		    @Param("modificadoPor") String modificadoPor,
		    @Param("dataInicio") LocalDateTime dataInicio,
		    @Param("dataFim") LocalDateTime dataFim
		);




}