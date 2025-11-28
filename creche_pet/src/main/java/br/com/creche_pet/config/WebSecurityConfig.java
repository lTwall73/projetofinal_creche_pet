package br.com.creche_pet.config;

import br.com.creche_pet.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, proxyTargetClass = true)
public class WebSecurityConfig {

  @Autowired
  private CustomUserDetailsService customUserDetailsService;

  @Autowired
  private DataSource dataSource;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(customUserDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http
        .authorizeHttpRequests(authz -> authz

            // ARQUIVOS ESTÁTICOS
            .requestMatchers("/resources/**", "/webjars/**", "/assets/**",
                "/css/**", "/js/**", "/img/**", "/images/**")
            .permitAll()

            // PÚBLICO: Páginas dentro de /visual/
            .requestMatchers("/contato", "/galeria", "/planos", "/servicos", "/sobre-nos", "/cadastro").permitAll()

            // PÚBLICO: login, cadastro e reset
            .requestMatchers("/", "/login", "/login/**",
                "/cadastro/**", "/forgot-password", "/reset-password", "/cadastro")
            .permitAll()

            // ACESSO PARA USER e ADMIN
            .requestMatchers(
                "/exibir_pet",
                "/exibir_matricula",
                "/exibir_turma",
                "/exibir_contato",
                "/exibir_servico",
                "/menuuser",
                "/menuuser/**")
            .hasAnyRole("USER", "ADMIN")

            // ACESSO EXCLUSIVO DO ADMIN
            .requestMatchers(
                "/exibir_funcionario",
                "/exibir_cargo",
                "/exibir_usuario",
                "/menuadm",
                "/menuadm/**",
                "/admin/**",
                "/usuario/**")
            .hasRole("ADMIN")

            // TODO O RESTO EXIGE AUTENTICAÇÃO
            .anyRequest().authenticated())

        .formLogin(form -> form
            .loginPage("/login")
            .usernameParameter("email") // <--- campo do email
            .passwordParameter("senha") // <--- campo da senha
            /* .defaultSuccessUrl("/home", true) */
            .successHandler(customAuthenticationSuccessHandler()) // nosso handler .failureUrl("/login?error=true")
            .permitAll())

        .csrf(csrf -> csrf
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))

        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login?logout")
            .deleteCookies("my-remember-me-cookie")
            .permitAll())

        .rememberMe(remember -> remember
            .userDetailsService(customUserDetailsService)
            .key("my-secure-key")
            .tokenRepository(persistentTokenRepository())
            .tokenValiditySeconds(24 * 60 * 60));

    return http.build();
  }

  @Bean
  public PersistentTokenRepository persistentTokenRepository() {
    JdbcTokenRepositoryImpl tokenRepositoryImpl = new JdbcTokenRepositoryImpl();
    tokenRepositoryImpl.setDataSource(dataSource);
    return tokenRepositoryImpl;
  }

  @Bean
  public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
    return (request, response, authentication) -> {

      boolean isAdmin = authentication.getAuthorities().stream()
          .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

      boolean isUser = authentication.getAuthorities().stream()
          .anyMatch(a -> a.getAuthority().equals("ROLE_USER"));

      if (isAdmin) {
        response.sendRedirect("/menuadm");
      } else if (isUser) {
        response.sendRedirect("/menuuser");
      } else {
        response.sendRedirect("/login?error=true");
      }
    };
  }

}
