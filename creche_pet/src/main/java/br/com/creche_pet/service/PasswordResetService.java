package br.com.creche_pet.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.creche_pet.model.UserModel;
import br.com.creche_pet.repository.UserRepository;

@Service
public class PasswordResetService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  // 1️⃣ GERAR TOKEN (sem enviar e-mail)
  public String createPasswordResetToken(String email) {
    UserModel user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    String token = UUID.randomUUID().toString();
    user.setResetToken(token);
    user.setResetTokenExpiry(LocalDateTime.now().plusHours(1)); // Expira em 1 hora
    userRepository.save(user);

    return token; // Retorna o token para o controller
  }

  // 2️⃣ VALIDAR TOKEN
  public boolean validateResetToken(String token) {
    // ❌ Antes: UserModel user = userRepository.findByResetToken(token);
    // ✅ Agora: Obter o UserModel do Optional
    UserModel user = userRepository.findByResetToken(token)
        .orElse(null);
    if (user == null) {
      return false;
    }
    return !user.getResetTokenExpiry().isBefore(LocalDateTime.now());
  }

  // 3️⃣ REDEFINIR SENHA (via token)
  public void resetPassword(String token, String newPassword) {
    // ❌ Antes: UserModel user = userRepository.findByResetToken(token);
    // ✅ Agora: Obter o UserModel do Optional
    UserModel user = userRepository.findByResetToken(token)
        .orElseThrow(() -> new RuntimeException("Token inválido"));

    if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
      throw new RuntimeException("Token expirado");
    }

    user.setSenha(passwordEncoder.encode(newPassword));
    user.setResetToken(null);
    user.setResetTokenExpiry(null);
    userRepository.save(user);
  }

  // 4️⃣ REDEFINIR SENHA POR ID (para o ADMIN)
  public void resetUserPassword(Long userId, String newPassword) {
    // ❌ Antes: UserModel user = userRepository.findById(userId);
    // ✅ Agora: Obter o UserModel do Optional
    UserModel user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    user.setSenha(passwordEncoder.encode(newPassword));
    userRepository.save(user);
  }
}
