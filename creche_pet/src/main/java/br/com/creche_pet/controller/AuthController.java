package br.com.creche_pet.controller;

import br.com.creche_pet.model.UserModel;
import br.com.creche_pet.dto.UserDto;
import br.com.creche_pet.repository.UserRepository;
import br.com.creche_pet.service.PasswordResetService;
import br.com.creche_pet.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.validation.BindingResult;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Controller
public class AuthController {

  @Autowired
  private PasswordResetService passwordResetService;

  private final UserRepository userRepository;
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;

  public AuthController(UserRepository userRepository,
      UserService userService,
      PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
  }

  // ---------------- LOGIN ----------------
  @GetMapping("/login")
  public String loginForm(Model model) {
    model.addAttribute("user", new UserModel()); // ou UserDto
    return "visual/login"; // seu template Thymeleaf
  }

  @PostMapping("/login")
  public String login(@ModelAttribute("user") UserModel userForm,
      Model model,
      HttpSession session) {

    // Busca o usuário por email
    UserModel user = userRepository.findByEmail(userForm.getEmail()).orElse(null);

    // Verifica se usuário existe e se a senha bate
    if (user == null || !passwordEncoder.matches(userForm.getSenha(), user.getSenha())) {
      model.addAttribute("erro", "Email ou senha incorretos!");
      return "visual/login"; // mantém a página de login
    }

    // Verifica se usuário foi aprovado
    if (!user.isAprovado()) {
      model.addAttribute("erro", "Seu acesso ainda não foi aprovado pelo administrador.");
      return "visual/login";
    }

    // Salva o usuário na sessão
    session.setAttribute("user", user);

    return "redirect:/menuadm";

  }

  // ---------------- REGISTRO ----------------
  @GetMapping("/cadastro")
  public String showRegistrationForm(Model model) {
    model.addAttribute("user", new UserDto());
    return "visual/cadastro"; // seu template Thymeleaf
  }

  @PostMapping("/register/save")
  public String registration(@Valid @ModelAttribute("user") UserDto userDto,
      BindingResult result,
      Model model) {

    UserModel existing = userService.findUserByEmail(userDto.getEmail());
    if (existing != null) {
      result.rejectValue("email", null, "Já existe uma conta com esse email");
    }

    if (result.hasErrors()) {
      return "visual/cadastro";
    }

    userService.saveUser(userDto);
    return "redirect:/login?success";
  }

  // ---------------- LISTA DE USUÁRIOS ----------------
  @GetMapping("/users")
  public String listRegisteredUsers(Model model) {
    List<UserDto> users = userService.findAllUsers();
    model.addAttribute("users", users);
    return "users"; // template para listar usuários
  }

  // ---------------- LOGOUT ----------------
  @GetMapping("/logout")
  public String logout(HttpSession session) {
    session.invalidate();
    return "redirect:/login?logout";
  }

  // ---------------- ESQUECI MINHA SENHA ----------------

  // 1️⃣ Mostrar página "Esqueci a senha"
  @GetMapping("/forgot-password")
  public String showForgotPasswordPage() {
    return "forgot-password";
  }

  // 2️⃣ Enviar email com link de redefinição
  @PostMapping("/forgot-password")
  public String forgotPassword(@RequestParam String email, Model model) {

    try {
      String token = passwordResetService.createPasswordResetToken(email);
      String link = "http://localhost:8080/reset-password?token=" + token;
      model.addAttribute("link", link); // Adiciona o link no modelo
      model.addAttribute("message", "O link de redefinição foi gerado com sucesso.");
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
    }
    return "forgot-password";
  }

  // 3️⃣ Mostrar página para redefinir a senha
  @GetMapping("/reset-password")
  public String showResetForm(@RequestParam String token, Model model) {
    model.addAttribute("token", token);
    return "reset-password";
  }

  // 4️⃣ Processar redefinição da senha
  @PostMapping("/reset-password")
  public String resetPassword(
      @RequestParam String token,
      @RequestParam String password,
      @RequestParam String confirmPassword,
      Model model) {

    // validar senhas iguais
    if (!password.equals(confirmPassword)) {
      model.addAttribute("error", "As senhas não coincidem");
      model.addAttribute("token", token);
      return "reset-password";
    }

    try {
      passwordResetService.resetPassword(token, password);
      model.addAttribute("message", "Senha redefinida com sucesso!");
      return "visual/login"; // página de login
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
      model.addAttribute("token", token);
      return "reset-password";
    }
  }
}

/*
 * package br.com.creche_pet.controller;
 * 
 * import br.com.creche_pet.dto.UserDto;
 * import br.com.creche_pet.model.UserModel;
 * import br.com.creche_pet.repository.UserRepository;
 * import br.com.creche_pet.service.UserService;
 * 
 * import jakarta.servlet.http.HttpSession;
 * import jakarta.validation.Valid;
 * 
 * import org.springframework.security.crypto.password.PasswordEncoder;
 * import org.springframework.stereotype.Controller;
 * import org.springframework.ui.Model;
 * 
 * import org.springframework.validation.BindingResult;
 * import org.springframework.web.bind.annotation.GetMapping;
 * import org.springframework.web.bind.annotation.ModelAttribute;
 * import org.springframework.web.bind.annotation.PostMapping;
 * 
 * import java.util.List;
 * import java.util.Optional;
 * 
 * @Controller
 * public class AuthController {
 * 
 * private final UserRepository userRepository;
 * private final UserService userService;
 * private final PasswordEncoder passwordEncoder;
 * 
 * public AuthController(UserRepository userRepository,
 * UserService userService,
 * PasswordEncoder passwordEncoder) {
 * 
 * this.userRepository = userRepository;
 * this.userService = userService;
 * this.passwordEncoder = passwordEncoder;
 * }
 * 
 * // ===========================
 * // LOGIN
 * // ===========================
 * 
 * @GetMapping("/login")
 * public String loginForm(Model model) {
 * model.addAttribute("user", new UserModel());
 * return "visual/login";
 * }
 * 
 * @PostMapping("/login")
 * public String login(@ModelAttribute("user") UserModel userForm,
 * Model model,
 * HttpSession session) {
 * 
 * Optional<UserModel> userOpt =
 * userRepository.findByEmail(userForm.getEmail());
 * 
 * if (userOpt.isEmpty()) {
 * model.addAttribute("erro", "Email ou senha incorretos!");
 * return "visual/login";
 * }
 * 
 * UserModel user = userOpt.get();
 * 
 * // VERIFICAR SENHA CRIPTOGRAFADA
 * if (!passwordEncoder.matches(userForm.getSenha(), user.getSenha())) {
 * model.addAttribute("erro", "Email ou senha incorretos!");
 * return "visual/login";
 * }
 * 
 * // Verifica aprovação
 * if (!user.isAprovado()) {
 * model.addAttribute("erro", "Seu acesso ainda não foi aprovado.");
 * return "visual/login";
 * }
 * 
 * session.setAttribute("user", user);
 * 
 * if ("adm".equals(user.getUserType())) {
 * return "redirect:/menuadm";
 * } else {
 * return "redirect:/menuuser";
 * }
 * }
 * 
 * 
 * // ===========================
 * // CADASTRO
 * // ===========================
 * 
 * @GetMapping("/cadastro")
 * public String showRegistrationForm(Model model) {
 * model.addAttribute("user", new UserDto());
 * return "visual/cadastro";
 * }
 * 
 * @PostMapping("/register/save")
 * public String registration(@Valid @ModelAttribute("user") UserDto user,
 * BindingResult result,
 * Model model) {
 * 
 * UserModel existing = userService.findUserByEmail(user.getEmail());
 * 
 * // email já cadastrado
 * if (existing != null) {
 * result.rejectValue("email", null, "Já existe uma conta com esse email");
 * }
 * 
 * if (result.hasErrors()) {
 * return "visual/cadastro";
 * }
 * 
 * // salva novo usuário
 * userService.saveUser(user);
 * return "redirect:/login?success";
 * }
 * 
 * // ===========================
 * // LISTAR USUÁRIOS
 * // ===========================
 * 
 * @GetMapping("/users")
 * public String listRegisteredUsers(Model model) {
 * 
 * List<UserDto> users = userService.findAllUsers();
 * model.addAttribute("users", users);
 * 
 * return "users";
 * }
 * }
 * 
 * 
 * 
 * 
 * 
 * 
 * ultima atualizacao
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * /*package br.com.creche_pet.controller;
 * 
 * import java.util.List;
 * 
 * import org.springframework.stereotype.Controller;
 * import org.springframework.web.bind.annotation.GetMapping;
 * import org.springframework.ui.Model;
 * import org.springframework.validation.BindingResult;
 * import org.springframework.web.bind.annotation.ModelAttribute;
 * import org.springframework.web.bind.annotation.PostMapping;
 * 
 * import br.com.creche_pet.dto.UserDto;
 * import br.com.creche_pet.model.UserModel;
 * import br.com.creche_pet.service.UserService;
 * import jakarta.validation.Valid;
 * 
 * @Controller
 * public class AuthController {
 * 
 * private UserService userService;
 * 
 * public AuthController(UserService userService) {
 * this.userService = userService;
 * }
 * 
 * 
 * 
 * @GetMapping("/login")
 * public String loginForm() {
 * 
 * return "visual/login";
 * }
 * 
 * @PostMapping("/login")
 * public String login(@ModelAttribute("user") User userForm,
 * Model model, HttpSession session) {
 * 
 * User user = userRepository.findByEmail(userForm.getEmail());
 * 
 * if (user == null || !user.getSenha().equals(userForm.getSenha())) {
 * model.addAttribute("erro", "Email ou senha incorretos!");
 * return "login";
 * }
 * 
 * // ❌ BLOQUEIA SE NÃO FOI APROVADO PELO ADMIN
 * if (!user.isAprovado()) {
 * model.addAttribute("erro",
 * "Seu acesso ainda não foi aprovado pelo administrador.");
 * return "login";
 * }
 * 
 * session.setAttribute("user", user);
 * 
 * // Redireciona por tipo
 * if ("adm".equals(user.getUserType())) {
 * return "redirect:/menuadm";
 * } else {
 * return "redirect:/menuuser";
 * }
 * }
 * 
 * 
 * 
 * 
 * 
 * // handler method to handle user registration request
 * /* @GetMapping("register")
 * public String showRegistrationForm(Model model) {
 * UserDto user = new UserDto();
 * model.addAttribute("user", user);
 * return "register";
 * }
 * 
 * // handler method to handle register user form submit request
 * 
 * @PostMapping("/register/save")
 * public String registration(@Valid @ModelAttribute("user") UserDto user,
 * BindingResult result,
 * Model model) {
 * UserModel existing = userService.findUserByEmail(user.getEmail());
 * if (existing != null) {
 * result.rejectValue("email", null,
 * "There is already an account registered with that email");
 * }
 * if (result.hasErrors()) {
 * model.addAttribute("user", user);
 * return "register";
 * }
 * userService.saveUser(user);
 * return "redirect:/login?success";
 * }
 */
/*
 * @GetMapping("cadastro")
 * public String showRegistrationForm(Model model) {
 * UserDto user = new UserDto();
 * model.addAttribute("user", user);
 * return "visual/cadastro";
 * }
 * 
 * @PostMapping("/register/save")
 * public String registration(@Valid @ModelAttribute("user") UserDto user,
 * BindingResult result,
 * Model model) {
 * 
 * UserModel existing = userService.findUserByEmail(user.getEmail());
 * if (existing != null) {
 * result.rejectValue("email", null, "Já existe uma conta com esse email");
 * }
 * 
 * if (result.hasErrors()) {
 * return "visual/cadastro";
 * }
 * 
 * userService.saveUser(user);
 * return "redirect:/login?success";
 * }
 * 
 * 
 * 
 * 
 * 
 * @GetMapping("/users")
 * public String listRegisteredUsers(Model model) {
 * List<UserDto> users = userService.findAllUsers();
 * model.addAttribute("users", users);
 * return "users";
 * }
 * }
 */

// @Controller
// public class AuthController {
//
// private final UserService userService;
//
// public AuthController(UserService userService, PasswordEncoder
// passwordEncoder) {
// this.userService = userService;
// }
//
// @Value("${server.port:8080}")
// private int serverPort;
//
// @PostMapping("/register")
// public String register(@ModelAttribute UserModel userModel,
// HttpServletRequest request) {
// // objetivo é fazer debug
// // retirar prints depois
// System.out.println(">>> Dados recebidos: username=" + userModel.getUsername()
// + ", password="
// + userModel.getPassword());
//
// System.out.println(">>> Host: " + request.getServerName() + ":" +
// request.getServerPort());
//
// if (userModel.getPassword() == null || userModel.getUsername() == null) {
// return "redirect:/register?error";
// }
// userModel.setRole(Role.USER); // ou ADMIN, dependendo da lógica
// userService.salvarUsuario(userModel);
//
// // Tive que colocar o caminho exato, pois dava erro no redirecionamento
// // conferir se dá erro em outras máquinas
// // descomentar quando fizer isso
//
// // return "redirect://login?registered";
// return "redirect:http://localhost:8080/login?registered";
// }
// }
