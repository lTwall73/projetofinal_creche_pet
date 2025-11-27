package br.com.creche_pet.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.creche_pet.model.CargoModel;
import br.com.creche_pet.repository.CargoRepository;





@Controller
public class CargoController {

	
	@Autowired
	CargoRepository cargoRepository;
	
	
	
/* exibir */
	
	
	
	@GetMapping(value="/exibir_cargo")
	public String exibir(ModelMap model) {
		model.addAttribute("cargos", cargoRepository.findAll());
		return "cargo/exibir_cargo";
	}
	
	
/*cadastro*/
	
	
	@GetMapping("/cadastrar_cargo")
	 public	String cadastroCargo(ModelMap model) {
		model.addAttribute("cargo",new CargoModel());
		return "cargo/cadastrar_cargo";
	}
		
		
	/*	@PostMapping("/cadastrar_cargo/salvar")
		public String salvarCargos(@ModelAttribute CargoModel cargo, Model model) {
			
		cargoRepository.save(cargo); //salva ou atualiza no banco 
			
			//redireciona para a lista com uma mensagem de sucesso(opcional)
			return "redirect:/exibir_cargo";
		}*/
	
	@PostMapping("/cadastrar_cargo/salvar")
	public String salvarCargo(
	        @ModelAttribute CargoModel cargo
	        ,RedirectAttributes redirectAttributes) {

	    try {
	        cargoRepository.save(cargo);
	        redirectAttributes.addFlashAttribute("sucesso", "Cargo cadastrado com sucesso!");
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("erro", "Erro ao cadastrar o cargo!");
	    }

	    return "redirect:/exibir_cargo";
	}
	
	
		
		
	
	/* editar*/	

		@GetMapping("/cargo/editar/{id}")
		public String editarCargo(@PathVariable Long id, ModelMap model) {
		    CargoModel cargo = cargoRepository.findById(id)
		        .orElseThrow(() -> new IllegalArgumentException("ID do cargo inválido: " + id));

		    model.addAttribute("cargo", cargo);
		    model.addAttribute("idCargo", id);

		    return "cargo/editar_cargo"; // primeira página do formulário de edição
		}
	
		
		@PostMapping("/cargo/editar/salvar")
		public String salvarCargo1(@ModelAttribute CargoModel cargo, RedirectAttributes redirectAttributes) {
	        try {
	            cargoRepository.save(cargo);
	            redirectAttributes.addFlashAttribute("sucesso", "Cargo atualizado com sucesso!");
	        } catch (Exception e) {
	            redirectAttributes.addFlashAttribute("erro", "Erro ao atualizar o cargo!");
	        }
			
			return "redirect:/exibir_cargo";
		}
	
		
/* excluir */
		
		
		@GetMapping("/cargo/excluir/{id}")
		 public String excluirFunc(@PathVariable("id") Long id,
				 RedirectAttributes redirectAttributes) {
			try {
				cargoRepository.deleteById(id);
			    
				redirectAttributes.addFlashAttribute("sucesso", " Cargo excluído com sucesso!");
			} catch (Exception e ) {
				redirectAttributes.addFlashAttribute("erro", "Erro ao excluir o Cargo");
			}
			return "redirect:/exibir_cargo"; 
		
	
}
}
