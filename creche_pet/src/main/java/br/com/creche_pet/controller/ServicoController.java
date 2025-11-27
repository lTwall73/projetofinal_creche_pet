package br.com.creche_pet.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.creche_pet.model.ServicoModel;
import br.com.creche_pet.repository.ServicoRepository;





@Controller
public class ServicoController {

	
	@Autowired
	ServicoRepository servicoRepository;
	
	
	
/* exibir */
	
	
	
	@GetMapping(value="/exibir_servico")
	public String exibirServico(ModelMap model) {
		model.addAttribute("servicos", servicoRepository.findAll());
		return "servico/exibir_servico";
	}
	
	
	
/*cadastro*/
	
	
	@GetMapping("/cadastrar_servico")
	 public	String cadastroServico(ModelMap model) {
		model.addAttribute("servico",new ServicoModel());
		return "servico/cadastrar_servico";
	}
		
		
		@PostMapping("/cadastrar_servico/salvar")
		public String salvarServicoo(@ModelAttribute ServicoModel servico, RedirectAttributes redirectAttributes) {

		    try {
		        servicoRepository.save(servico);
		        redirectAttributes.addFlashAttribute("sucesso", " Serviço cadastrado com sucesso!");
		    } catch (Exception e) {
		        redirectAttributes.addFlashAttribute("erro", "Erro ao cadastrar o Serviço!");
		    }

			return "redirect:/exibir_servico";
		}
	
	
		
		
	
	/* editar*/	

		@GetMapping("/servico/editar/{id}")
		public String editarServico(@PathVariable Long id, ModelMap model) {
		   ServicoModel servico = servicoRepository.findById(id)
		        .orElseThrow(() -> new IllegalArgumentException("ID do servico inválido: " + id));

		    model.addAttribute("servico", servico);
		    model.addAttribute("idServico", id);

		    return "servico/editar_servico"; // primeira página do formulário de edição
		}
	
		
		@PostMapping("/servico/editar/salvar")
		public String salvarServico(@ModelAttribute ServicoModel servico, RedirectAttributes redirectAttributes) {
	        try {
	        	servicoRepository.save(servico);
	            redirectAttributes.addFlashAttribute("sucesso", "Serviço atualizado com sucesso!");
	        } catch (Exception e) {
	            redirectAttributes.addFlashAttribute("erro", "Erro ao atualizar o Serviço!");
	        }
			
			
			return "redirect:/exibir_servico";
		}
	
		
/* excluir */
		
		
		@GetMapping("/servico/excluir/{id}")
		 public String excluirFunc(@PathVariable("id") Long id,
				 RedirectAttributes redirectAttributes) {
			try {
				servicoRepository.deleteById(id);
			    
				redirectAttributes.addFlashAttribute("sucesso", " Serviço excluído com sucesso!");
			} catch (Exception e ) {
				redirectAttributes.addFlashAttribute("erro", "Erro ao excluir o Serviço");
			}
			return "redirect:/exibir_servico"; 
		
	
}
}
