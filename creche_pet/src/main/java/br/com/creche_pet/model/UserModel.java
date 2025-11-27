package br.com.creche_pet.model;

import java.time.LocalDateTime;
import java.util.*;



// import br.com.creche_pet.enums.Role;
import jakarta.persistence.*;

@Entity
@Table(name = "usuario")
public class UserModel {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_user")
  private Long idUser;

  @Column(name = "nome")
  private String nome;

  @Column(name = "email")
  private String email;

  @Column(name = "senha")
  private String senha;
  

  
  @Column(name = "aprovado")
  private boolean aprovado;


 

@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinTable(name = "users_roles", joinColumns = {
      @JoinColumn(name = "id_user", referencedColumnName = "id_user") }, inverseJoinColumns = {
          @JoinColumn(name = "id_role", referencedColumnName = "id_role") })
  private List<RoleModel> roles = new ArrayList<>();

  // --- Campos para recuperação de senha ---
  @Column(name = "reset_token")
  private String resetToken; // Armazena o token único

  @Column(name = "reset_token_expiry")
  private LocalDateTime resetTokenExpiry; // Armazena a data de expiração do token

  // --- Fim dos campos para recuperação de senha ---

  public Long getIdUser() {
    return idUser;
  }

  public void setIdUser(Long idUser) {
    this.idUser = idUser;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getSenha() {
    return senha;
  }

  public void setSenha(String senha) {
    this.senha = senha;
  }

  public List<RoleModel> getRoles() {
    return roles;
  }

  public void setRoles(List<RoleModel> roles) {
    this.roles = roles;
  }

  public String getResetToken() {
    return resetToken;
  }

  public void setResetToken(String resetToken) {
    this.resetToken = resetToken;
  }

  public LocalDateTime getResetTokenExpiry() {
    return resetTokenExpiry;
  }

  public void setResetTokenExpiry(LocalDateTime resetTokenExpiry) {
    this.resetTokenExpiry = resetTokenExpiry;
  }



public boolean isAprovado() {
	return aprovado;
}

public void setAprovado(boolean aprovado) {
	this.aprovado = aprovado;
}
  


}
