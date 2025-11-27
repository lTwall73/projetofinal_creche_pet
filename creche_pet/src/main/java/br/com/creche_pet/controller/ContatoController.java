package br.com.creche_pet.controller;





import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import br.com.creche_pet.model.ContatoModel;
import br.com.creche_pet.repository.ContatoRepository;





@Controller
public class ContatoController {

	
	@Autowired
	ContatoRepository contatoRepository;
	
	
	
/* exibir */
	
	
	
	@GetMapping(value="/exibir_contato")
	public String exibirContato(ModelMap model) {
		model.addAttribute("contato", contatoRepository.findAllByOrderByIdContatoDesc());
		return "contato/exibir_contato";
	}
	
	
	
/*enviar*/
	
	
	
	@GetMapping("/contato")
	 public	String cadastroContato(ModelMap model) {
		model.addAttribute("contato",new ContatoModel());
		return "visual/contato";
	}
	
	@PostMapping("/contato")
	public String salvarContato(
	        @ModelAttribute ContatoModel contato
	        ,RedirectAttributes redirectAttributes) {

	    try {
	        contatoRepository.save(contato);
	        redirectAttributes.addFlashAttribute("sucesso", "Mensagem enviada com sucesso!");
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("erro", "Erro ao enviar Mensagem!");
	    }

	    return "redirect:/contato";
	}
	
	/* editar*/	

	@GetMapping("/contato/editar/{id}")
	public String editarContato(@PathVariable Long id, ModelMap model) {
	    ContatoModel contato = contatoRepository.findById(id)
	        .orElseThrow(() -> new IllegalArgumentException("ID do cargo inválido: " + id));

	    model.addAttribute("contato", contato);
	    model.addAttribute("idContato", id);

	    return "contato/editar_contato"; // primeira página do formulário de edição
	}

	
	@PostMapping("/contato/editar/salvar")
	public String salvarContato2(@ModelAttribute ContatoModel contato, RedirectAttributes redirectAttributes) {
        try {
        	contatoRepository.save(contato);
            redirectAttributes.addFlashAttribute("sucesso", "Contato atualizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao atualizar o contato!");
        }
		
		return "redirect:/exibir_contato";
	}
	

	
	
/* excluir */
	
	
	@GetMapping("/contato/excluir/{id}")
	 public String excluirContato(@PathVariable("id") Long id,
			 RedirectAttributes redirectAttributes) {
		try {
			contatoRepository.deleteById(id);
		    
			redirectAttributes.addFlashAttribute("sucesso", " Contato excluído com sucesso!");
		} catch (Exception e ) {
			redirectAttributes.addFlashAttribute("erro", "Erro ao excluir o Contato");
		}
		return "redirect:/exibir_contato"; 
	

}
	
	
	
	

	/*        exibir   tudo da pagina visual  + menu adm e menu funcionario  
	 * 
	@GetMapping(value="/cadastro")
	  public String cadastro() { 
	        return "visual/cadastro"; 
	}
	
	 */
	

	

	@GetMapping(value="/galeria")
	  public String galeria() { 
	        return "visual/galeria"; 
	}
	

	@GetMapping(value="/planos")
	  public String planos() { 
	        return "visual/planos"; 
	}
	

	@GetMapping(value="/servicos")
	  public String servicos() { 
	        return "visual/servicos"; 
	}
	

	@GetMapping(value="/sobre-nos")
	  public String sobre() { 
	        return "visual/sobre-nos"; 
	}
	

	@GetMapping(value="/")
	  public String index() { 
	        return "visual/index"; 
	}
	

	
	@GetMapping(value="/menuadm")
	  public String menuadm() { 
	        return "menuadm"; 
	}
	

	@GetMapping(value="/menuuser")
	  public String menuuser() { 
	        return "menuuser"; 
	}
	
	
	
	
	
	
	

}
