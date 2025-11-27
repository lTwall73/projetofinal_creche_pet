
package br.com.creche_pet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.creche_pet.model.MatriculaModel;

@Repository

public interface MatriculaRepository extends JpaRepository<MatriculaModel, Long> {
}
