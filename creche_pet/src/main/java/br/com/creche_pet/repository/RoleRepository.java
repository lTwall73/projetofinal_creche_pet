package br.com.creche_pet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.creche_pet.model.RoleModel;

public interface RoleRepository extends JpaRepository<RoleModel, Long> {

  RoleModel findByName(String name);
}
