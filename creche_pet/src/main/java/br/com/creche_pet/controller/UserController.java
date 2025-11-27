package br.com.creche_pet.controller;

import br.com.creche_pet.model.RoleModel;
import br.com.creche_pet.model.UserModel;
import br.com.creche_pet.repository.RoleRepository;
import br.com.creche_pet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    /* ==============================
       EXIBIR USUÁRIOS
       ============================== */
    @GetMapping("/exibir_usuario")
    public String exibirUsuarios(ModelMap model) {
        List<UserModel> usuarios = userRepository.findAll();
        List<RoleModel> roles = roleRepository.findAll(); // todas as roles disponíveis
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("roles", roles);
        return "usuario/exibir_usuario"; // página Thymeleaf
    }

    /* ==============================
       ALTERAR APROVAÇÃO
       ============================== */
    @PostMapping("/usuario/alterar-aprovacao/{id}")
    public String alterarAprovacao(@PathVariable Long id,
                                   @RequestParam boolean aprovado,
                                   RedirectAttributes redirectAttributes) {
        try {
            UserModel u = userRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
            u.setAprovado(aprovado);
            userRepository.save(u);
            redirectAttributes.addFlashAttribute("sucesso", "Status de aprovação atualizado!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao atualizar aprovação!");
        }
        return "redirect:/exibir_usuario";
    }

    /* ==============================
       ALTERAR ROLE
       ============================== */
    @PostMapping("/usuario/alterar-role/{id}")
    public String alterarRole(@PathVariable Long id,
                              @RequestParam("roleId") Long roleId,
                              RedirectAttributes redirectAttributes) {
        try {
            UserModel u = userRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
            RoleModel role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new IllegalArgumentException("Role inválida: " + roleId));

            // Inicializa lista se for null
            if (u.getRoles() == null) {
                u.setRoles(new ArrayList<>());
            }

            // Remove todas as roles antigas e adiciona a nova
            u.getRoles().clear();
            u.getRoles().add(role);

            userRepository.save(u);
            redirectAttributes.addFlashAttribute("sucesso", "Status atualizada com sucesso!");
        } catch (Exception e) {
            e.printStackTrace(); // importante para ver o erro real no console
            redirectAttributes.addFlashAttribute("erro", "Erro ao atualizar Status!");
        }
        return "redirect:/exibir_usuario";
    }



    

    /* ==============================
       EXCLUIR USUÁRIO
       ============================== */
    @GetMapping("/usuario/excluir/{id}")
    public String excluirUsuario(@PathVariable("id") Long id,
                                 RedirectAttributes redirectAttributes) {
        try {
            UserModel user = userRepository.findById(id)
                               .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
            
            // Limpar roles
            if (user.getRoles() != null) {
                user.getRoles().clear();
                userRepository.save(user); // salva as alterações antes de excluir
            }

            userRepository.delete(user);
            redirectAttributes.addFlashAttribute("sucesso", "Usuário excluído com sucesso!");
        } catch (Exception e) {
            e.printStackTrace(); // para ver o erro no console
            redirectAttributes.addFlashAttribute("erro", "Erro ao excluir o usuário!");
        }
        return "redirect:/exibir_usuario";
    }

}

