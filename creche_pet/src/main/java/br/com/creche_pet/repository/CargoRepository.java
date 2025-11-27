
package br.com.creche_pet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.creche_pet.model.CargoModel;

@Repository

public interface CargoRepository extends JpaRepository<CargoModel, Long> {
}
