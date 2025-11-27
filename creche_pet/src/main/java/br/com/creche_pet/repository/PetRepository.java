
package br.com.creche_pet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.creche_pet.model.PetModel;
import jakarta.transaction.Transactional;

@Repository

public interface PetRepository extends JpaRepository<PetModel, Long> {
	
	
	@Modifying
	@Transactional
	@Query(value= "INSERT INTO pet (nome,especie, raca, data_nascimento ,peso,observacoes,id_Tutor) values (:#{#pet.nome}, :#{#pet.especie}, :#{#pet.raca}, :#{#pet.dataNascimento}, :#{#pet.peso}, :#{#pet.observacoes}, :#{#pet.idTutor})",
	nativeQuery = true
	)
	void IncluirPet(@Param("pet") PetModel pet);

	
}
