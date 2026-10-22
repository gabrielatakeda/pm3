package app.auth;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;

public interface UsuariosRepository extends JpaRepository<Usuarios, Long>{

	public List<Usuarios> findByNomeContains(String nome);
	
	public Usuarios findByCpf(String cpf);
	
	public Optional<Usuarios> findByLogin(String login);
	
	@Modifying
	@Transactional
	@Query("UPDATE Usuarios u SET u.ativo = false WHERE u.id = :id")
	public void desativarUsuarios(long id);
	
	@Modifying
	@Transactional
	@Query("UPDATE Usuarios u SET u.ativo = true WHERE u.id = :id")
	public void reativarUsuario(long id);
	
	/*@Query("SELECT u.id, u.createdBy, u.createdDate, u.lastModifiedBy, u.lastModifiedDate FROM Usuarios u")
	List<Object[]> findAuditoriaUsuarios();*/
	
	/*@Query("""
		    SELECT u.id, u.createdBy, u.createdDate, u.lastModifiedBy, u.lastModifiedDate
		    FROM Usuarios u
		    WHERE (:criadoPor IS NULL OR u.createdBy LIKE CONCAT('%', :criadoPor, '%'))
		      AND (:modificadoPor IS NULL OR u.lastModifiedBy LIKE CONCAT('%', :modificadoPor, '%'))
		      AND (:dataInicio IS NULL OR u.createdDate >= :dataInicio OR u.lastModifiedDate >= :dataInicio)
			  AND (:dataFim IS NULL OR u.createdDate <= :dataFim OR u.lastModifiedDate <= :dataFim)
		""")
		List<Object[]> findAuditoriaUsuariosComFiltro(
		    @Param("criadoPor") String criadoPor,
		    @Param("modificadoPor") String modificadoPor,
		    @Param("dataInicio") LocalDateTime dataInicio,
		    @Param("dataFim") LocalDateTime dataFim
		);*/
	
	@Query("""
		    SELECT em.id, em.createdBy, em.createdDate, em.lastModifiedBy, em.lastModifiedDate
		    FROM Usuarios em
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
		List<Object[]> findAuditoriaUsuariosComFiltro(
		    @Param("criadoPor") String criadoPor,
		    @Param("modificadoPor") String modificadoPor,
		    @Param("dataInicio") LocalDateTime dataInicio,
		    @Param("dataFim") LocalDateTime dataFim
		);



}
