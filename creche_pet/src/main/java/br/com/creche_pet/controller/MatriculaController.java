package br.com.creche_pet.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.creche_pet.model.MatriculaModel;

import br.com.creche_pet.repository.MatriculaRepository;
import br.com.creche_pet.repository.PetRepository;
import br.com.creche_pet.repository.TurmaRepository;
import br.com.creche_pet.service.MatriculaService;
import ch.qos.logback.core.model.Model;





@Controller
public class MatriculaController {

	
	@Autowired
	MatriculaRepository matriculaRepository;
	
	@Autowired
	PetRepository petRepository;

	@Autowired
	TurmaRepository turmaRepository;

	@Autowired
	 MatriculaService matriculaService;
	
	
/* exibir */
	
	
	
	@GetMapping(value="/exibir_matricula")
	public String exibirMatricula(ModelMap model) {
		model.addAttribute("matriculas", matriculaRepository.findAll());
		model.addAttribute("pets", petRepository.findAll());  
		return "matricula/exibir_matricula";
	}
	
	
/*cadastro*/
	
	@GetMapping("/cadastrar_matricula")
	public String cadastroMatricula(ModelMap model) {
	    model.addAttribute("matricula", new MatriculaModel());
	    model.addAttribute("pets", petRepository.findAll());     // usa o objeto injetado
	    model.addAttribute("turmas", turmaRepository.findAll()); // usa o objeto injetado
	    return "matricula/cadastrar_matricula";
	}

		
		
		@PostMapping("/cadastrar_matricula/salvar")
		public String salvarMatricula(@ModelAttribute MatriculaModel matricula, Model model, RedirectAttributes redirectAttributes) {

		    try {
		        if (matriculaService.verificarVagas(matricula)) {
		            matriculaRepository.save(matricula);
		            redirectAttributes.addFlashAttribute("sucesso", "Matrícula realizada com sucesso!");
		        } else {
		            redirectAttributes.addFlashAttribute("aviso", "Turma cheia! Não é possível matricular neste momento.");
		        }
		    } catch (Exception e) {
		        redirectAttributes.addFlashAttribute("erro", "Erro ao processar matrícula!");
		    }
			
		/*	if(turmaRepository.findById(matricula.getIdTurma()).get().getCapacidade() >  
			turmaRepository.totalMatriculados(matricula.getIdTurma())
			) 
			{
				System.out.println("tem vaga");
				
			}
			else
			{
				System.out.println("não tem vaga");
			}
			matriculaRepository.save(matricula); //salva ou atualiza no banco */
			
			
			
			//redireciona para a lista com uma mensagem de sucesso(opcional)
			return "redirect:/exibir_matricula";
		}
	
	
/* editar*/	

		@GetMapping("/matricula/editar/{id}")
		public String editarMatricula(@PathVariable Long id, ModelMap model) {
			MatriculaModel matricula = matriculaRepository.findById(id)
		        .orElseThrow(() -> new IllegalArgumentException("ID turma inválido: " + id));

			
		    model.addAttribute("matricula", matricula);
		    model.addAttribute("pets", petRepository.findAll());
		    model.addAttribute("turmas", turmaRepository.findAll());
		    model.addAttribute("idMatricula", id);

		    return "matricula/editar_matricula"; 
		}
	
		
		@PostMapping("/matricula/editar/salvar")
		public String salvarMatricula(@ModelAttribute MatriculaModel matricula, RedirectAttributes redirectAttributes) {
	        try {
	        	matriculaRepository.save(matricula);
	            redirectAttributes.addFlashAttribute("sucesso", "Matrícula atualizado com sucesso!");
	        } catch (Exception e) {
	            redirectAttributes.addFlashAttribute("erro", "Erro ao atualizar o Matrícula!");
	        }
			
			return "redirect:/exibir_matricula";
		}
		
		
		
		
/* excluir */
		
		
		@GetMapping("/matricula/excluir/{id}")
		 public String excluirMatricula(@PathVariable("id") Long id,
				 RedirectAttributes redirectAttributes) {
			try {
				matriculaRepository.deleteById(id);
			    
				redirectAttributes.addFlashAttribute("sucesso", "Matrícula excluído com sucesso!");
			} catch (Exception e ) {
				redirectAttributes.addFlashAttribute("erro", "Erro ao excluir a Matrícula");
			}
			return "redirect:/exibir_matricula"; 
		
	
}
		

		
	

}
