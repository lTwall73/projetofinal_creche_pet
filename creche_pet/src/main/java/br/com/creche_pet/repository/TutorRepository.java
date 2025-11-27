package br.com.creche_pet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.creche_pet.model.TutorModel;

@Repository

public interface TutorRepository extends JpaRepository<TutorModel, Long> {

	
}
