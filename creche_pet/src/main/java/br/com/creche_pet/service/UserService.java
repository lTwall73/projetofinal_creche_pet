package br.com.creche_pet.service;

import java.util.List;

import br.com.creche_pet.dto.UserDto;
import br.com.creche_pet.model.UserModel;

public interface UserService {

  void saveUser(UserDto userDto);

  UserModel findUserByEmail(String email);

  List<UserDto> findAllUsers();

boolean alterarRole(Long id);

boolean alterarAprovacao(Long id);

}
