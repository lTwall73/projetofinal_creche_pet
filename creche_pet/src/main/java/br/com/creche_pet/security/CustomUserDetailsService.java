package br.com.creche_pet.security;

import br.com.creche_pet.model.UserModel;
import br.com.creche_pet.model.RoleModel;
import br.com.creche_pet.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserModel> userOpt = userRepository.findByEmail(email);

        // Extrai o UserModel ou lança exceção se não encontrado
        UserModel user = userOpt.orElseThrow(
            () -> new UsernameNotFoundException("Usuário não encontrado com email: " + email)
        );

        // ❌ Bloqueia login se usuário não estiver aprovado
        if (!user.isAprovado()) {
            throw new UsernameNotFoundException("Usuário não aprovado pelo administrador");
        }

        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getSenha(),
            mapRolesToAuthorities(user.getRoles())
        );
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<RoleModel> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
