
package br.com.creche_pet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import br.com.creche_pet.model.TurmaModel;

@Repository

public interface TurmaRepository extends JpaRepository<TurmaModel, Long> {
	
	@Query(value= "SELECT count(*) FROM creche_pet.matricula where id_turma=?1;",nativeQuery = true)
	int totalMatriculados(@Param("id_turma") Long idTurma);
}
