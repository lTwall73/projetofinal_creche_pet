package br.com.creche_pet.repository;

import br.com.creche_pet.model.UserModel;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, Long> {
  Optional<UserModel> findByEmail(String email);
  Optional<UserModel> findByResetToken(String resetToken);

}
