package br.com.creche_pet.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.creche_pet.dto.UserDto;
import br.com.creche_pet.model.RoleModel;
import br.com.creche_pet.model.UserModel;
import br.com.creche_pet.repository.RoleRepository;
import br.com.creche_pet.repository.UserRepository;
import br.com.creche_pet.service.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(UserDto dto) {
        UserModel user = new UserModel();
        user.setNome(dto.getNome());
        user.setEmail(dto.getEmail());
        user.setSenha(passwordEncoder.encode(dto.getSenha()));
        user.setAprovado(false);

        // Busca role ROLE_USER
        RoleModel roleUser = roleRepository.findByName("ROLE_USER");
        if (roleUser == null) {
            roleUser = new RoleModel();
            roleUser.setName("ROLE_USER");
            roleRepository.save(roleUser);
        }

        // usa ArrayList mutável
        user.setRoles(new ArrayList<>(Collections.singletonList(roleUser)));

        userRepository.save(user);
    }

    @Override
    public UserModel findUserByEmail(String email) {
        Optional<UserModel> userOpt = userRepository.findByEmail(email);
        return userOpt.orElse(null);
    }

    @Override
    public List<UserDto> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToUserDto)
                .collect(Collectors.toList());
    }

    private UserDto mapToUserDto(UserModel user) {
        UserDto dto = new UserDto();
        dto.setIdUser(user.getIdUser());
        dto.setNome(user.getNome());
        dto.setEmail(user.getEmail());
        dto.setSenha("");
        return dto;
    }

    @Override
    public boolean alterarAprovacao(Long id) {
        Optional<UserModel> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            UserModel user = userOpt.get();
            user.setAprovado(!user.isAprovado());
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean alterarRole(Long id) {
        Optional<UserModel> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            UserModel user = userOpt.get();

            boolean isAdmin = user.getRoles().stream()
                    .anyMatch(r -> "ROLE_ADMIN".equalsIgnoreCase(r.getName()));

            RoleModel role;
            if (isAdmin) {
                role = roleRepository.findByName("ROLE_USER");
                if (role == null) {
                    role = new RoleModel();
                    role.setName("ROLE_USER");
                    roleRepository.save(role);
                }
            } else {
                role = roleRepository.findByName("ROLE_ADMIN");
                if (role == null) {
                    role = new RoleModel();
                    role.setName("ROLE_ADMIN");
                    roleRepository.save(role);
                }
            }

            // garante lista mutável para JPA
            user.setRoles(new ArrayList<>(Collections.singletonList(role)));
            userRepository.save(user);
            return true;
        }
        return false;
    }
}










/*package br.com.creche_pet.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.creche_pet.dto.UserDto;
import br.com.creche_pet.model.RoleModel;
import br.com.creche_pet.model.UserModel;
import br.com.creche_pet.repository.RoleRepository;
import br.com.creche_pet.repository.UserRepository;
import br.com.creche_pet.service.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

  private UserRepository userRepository;
  private RoleRepository roleRepository;
  private PasswordEncoder passwordEncoder;

  public UserServiceImpl(UserRepository userRepository,
      RoleRepository roleRepository,
      PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
  }
  
  
  
  public void saveUser(UserDto dto) {
	    UserModel user = new UserModel();

	    user.setNome(dto.getNome());
	    user.setEmail(dto.getEmail());

	    // Criptografa senha
	    user.setSenha(passwordEncoder.encode(dto.getSenha()));

	    // Define role padrão
	    RoleModel role = roleRepository.findByName("ROLE_USER");
	    user.setRoles(List.of(role));

	    userRepository.save(user);
	}

  
  


  // Atualização para lidar com Optional
  @Override
  public UserModel findUserByEmail(String email) {
    Optional<UserModel> userOpt = userRepository.findByEmail(email);
    // Retorna o UserModel se encontrado, ou null se não encontrado
    // Isso mantém a assinatura do método original (retornando UserModel)
    return userOpt.orElse(null);
  }

  public List<UserDto> findAllUsers() {
    List<UserModel> users = userRepository.findAll();
    return users.stream()
        .map((user) -> mapToUserDto(user))
        .collect(Collectors.toList());
  }

  private UserDto mapToUserDto(UserModel user) {
    UserDto userDto = new UserDto();
    String[] str = user.getNome().split(" ");
    userDto.setFirstName(str[0]);
    userDto.setLastName(str[1]);
    userDto.setEmail(user.getEmail());
    return userDto;
  }

  private RoleModel checkRoleExist() {
    RoleModel role = new RoleModel();
    role.setName("ROLE_ADMIN");
    return roleRepository.save(role);
  }
}
*/