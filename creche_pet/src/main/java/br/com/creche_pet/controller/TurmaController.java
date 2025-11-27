package br.com.creche_pet.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.creche_pet.model.TurmaModel;
import br.com.creche_pet.repository.TurmaRepository;






@Controller
public class TurmaController {

	
	@Autowired
     TurmaRepository turmaRepository;
	
	
	
/* exibir */
	
	
	
	@GetMapping(value="/exibir_turma")
	public String exibirTurma(ModelMap model) {
		model.addAttribute("turmas", turmaRepository.findAll());
		return "turma/exibir_turma";
	}
	
	
/*cadastro*/
	
	
	@GetMapping("/cadastrar_turma")
	 public	String cadastroturma(ModelMap model) {
		model.addAttribute("turma",new TurmaModel());
		return "turma/cadastrar_turma";
	}
		
		
		@PostMapping("/cadastrar_turma/salvar")
		public String salvarturma(@ModelAttribute TurmaModel turma,RedirectAttributes redirectAttributes) {

		    try {
		        turmaRepository.save(turma);
		        redirectAttributes.addFlashAttribute("sucesso", "Turma cadastrado com sucesso!");
		    } catch (Exception e) {
		        redirectAttributes.addFlashAttribute("erro", "Erro ao cadastrar o Turma!");
		    }
			
		
			return "redirect:/exibir_turma";
		}
	
		
/* editar*/	

		@GetMapping("/turma/editar/{id}")
		public String editarTurma(@PathVariable Long id, ModelMap model) {
			TurmaModel turma = turmaRepository.findById(id)
		        .orElseThrow(() -> new IllegalArgumentException("ID turma inválido: " + id));

		    model.addAttribute("turma", turma);
		    model.addAttribute("idTurma", id);

		    return "turma/editar_turma"; 
		}
	
		
		@PostMapping("/turma/editar/salvar")
		public String salvarTurma(@ModelAttribute TurmaModel turma, RedirectAttributes redirectAttributes) {
	        try {
	            turmaRepository.save(turma);
	            redirectAttributes.addFlashAttribute("sucesso", "Turma atualizado com sucesso!");
	        } catch (Exception e) {
	            redirectAttributes.addFlashAttribute("erro", "Erro ao atualizar o Turma!");
	        }
			
			return "redirect:/exibir_turma";
		}
	
		
/* excluir */
		
		
		@GetMapping("/turma/excluir/{id}")
		 public String excluirTurma(@PathVariable("id") Long id,
				 RedirectAttributes redirectAttributes) {
			try {
				turmaRepository.deleteById(id);
			    
				redirectAttributes.addFlashAttribute("sucesso", " turma excluído com sucesso!");
			} catch (Exception e ) {
				redirectAttributes.addFlashAttribute("erro", "Erro ao excluir a turma");
			}
			return "redirect:/exibir_turma"; 
		
	
}


}
