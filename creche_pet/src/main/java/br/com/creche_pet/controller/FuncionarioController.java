package br.com.creche_pet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import br.com.creche_pet.model.CargoModel;
import br.com.creche_pet.model.EnderecoModel;
import br.com.creche_pet.model.FuncionarioModel;
import br.com.creche_pet.repository.CargoRepository;
import br.com.creche_pet.repository.EnderecoRepository;
import br.com.creche_pet.repository.FuncionarioRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



@Controller
@SessionAttributes({"funcionario", "cargo", "endereco"})
public class FuncionarioController {

	@Autowired
	FuncionarioRepository funcionarioRepository;
	
	@Autowired
	CargoRepository cargoRepository;
	
	@Autowired
    EnderecoRepository enderecoRepository;
	
	
	
/* exibir  */
	
	@GetMapping(value="/exibir_funcionario")
	public String funcionario(ModelMap model) {
		
		model.addAttribute("funcionarios", funcionarioRepository.findAll());
		
		return "funcionario/exibir_funcionario";
	}
	
	
/* cadastro 1 */
	
	@GetMapping(value="/cadastrar_funcionario1")
    public String cadastrar(ModelMap model) { 	
		   model.addAttribute("funcionario", new FuncionarioModel());
		
        return "funcionario/cadastrar_funcionario1"; 
    	    	
	}	
	
	
	@PostMapping("/cadastrar_funcionario1")
	public String processarFuncionario(@ModelAttribute("funcionario") FuncionarioModel funcionario) {
	 	System.out.println("cadastrando dados do funcionario ");
	    return "redirect:/cadastrar_funcionario2";
	}
	
	
  /* cadastro 2 */
	
	@GetMapping(value="/cadastrar_funcionario2")
    public String cadastrar2(ModelMap model) { 	
		   model.addAttribute("cargo", new CargoModel());
		    model.addAttribute("cargos", cargoRepository.findAll()); // ← ESSENCIAL!
		
        return "funcionario/cadastrar_funcionario2"; 
    	    	
	}	
	@PostMapping("/cadastrar_funcionario2")
	public String processarCargo(
	        @ModelAttribute("cargo") CargoModel cargo, 
	        @ModelAttribute("funcionario") FuncionarioModel funcionario,
	        ModelMap model) {
	    
	    // Busca o cargo existente pelo ID selecionado no select
	    CargoModel cargoSelecionado = cargoRepository.findById(cargo.getIdCargo())
	            .orElse(null);
	    
	    // Associa o cargo ao funcionário em memória
	    funcionario.setCargo(cargoSelecionado);
	    
	    // Atualiza os objetos na sessão
	    model.addAttribute("cargo", cargoSelecionado);
	    model.addAttribute("funcionario", funcionario);
	    
	    System.out.println("Cargo associado ao funcionário: " + cargoSelecionado.getFuncao());

	    // Vai para o próximo passo (endereço)
	    return "redirect:/cadastrar_funcionario3";
	}

	
	
	
	
/* cadastro 3 */
	
	
	 
	@GetMapping(value="/cadastrar_funcionario3")
    public String cadastrar3(ModelMap model) { 	
		 model.addAttribute("endereco", new EnderecoModel());
		
        return "funcionario/cadastrar_funcionario3"; 
    	    	
	}	
	
	 @PostMapping("/cadastrar_funcionario3")
	    public String processarfuncio3(@ModelAttribute("endereco") EnderecoModel endereco) {
	    	System.out.println("cadastrando endereco funcionario");
		 return "redirect:/confirmacao_funcionario";
	 }
	
	 
	 
	 
	
/* confirmar*/
	 
	 @GetMapping("/confirmacao_funcionario")
	 public String confirmarFuncionario(
	         @ModelAttribute("funcionario") FuncionarioModel funcionario,
	         @ModelAttribute("cargo") CargoModel cargo,
	         @ModelAttribute("endereco") EnderecoModel endereco,
	         ModelMap model) {

	     // Reexpõe os atributos salvos na sessão para o template Thymeleaf
	     model.addAttribute("funcionario", funcionario);
	     model.addAttribute("cargo", cargo);
	     model.addAttribute("endereco", endereco);

	     return "funcionario/confirmacao_funcionario";
	 }

	 
	
	 
	 @PostMapping("/confirmacao_funcionario/salvar")
	 public String salvarFuncionarioFinal(
	         @ModelAttribute("funcionario") FuncionarioModel funcionario,
	         @ModelAttribute("endereco") EnderecoModel endereco,
	         RedirectAttributes redirectAttributes) {

	     // Salvar endereço primeiro
	     enderecoRepository.save(endereco);

	     // Associar endereço (cargo já está definido)
	     funcionario.setEndereco(endereco);

	     // Salvar funcionário (sem criar novo cargo)
	     funcionarioRepository.save(funcionario);
	     
	     redirectAttributes.addFlashAttribute("sucesso", "Funcionário cadastrado com sucesso!");

	     return "redirect:/exibir_funcionario";
	 }


	

	 
		
/*editar 1 */
			
			
			@GetMapping("/funcionario/editar/{id}")
			public String editarfunc(@PathVariable Long id, ModelMap model) {
			    FuncionarioModel funcionario = funcionarioRepository.findById(id)
			        .orElseThrow(() -> new IllegalArgumentException("ID do Funcionário inválido: " + id));
			    
			    CargoModel cargo = funcionario.getCargo();
			    EnderecoModel endereco = funcionario.getEndereco();
			    
			    model.addAttribute("funcionario", funcionario);
			    model.addAttribute("cargo", cargo);
			    model.addAttribute("endereco", endereco);

			    return "funcionario/editar_funcionario1"; 
			}

			@PostMapping("/editar_funcionario1")
			public String processarEdicaoFunc(@ModelAttribute("funcionario") FuncionarioModel funcionario) {
			    System.out.println("Editando funcionario...");
			    return "redirect:/editar_funcionario2";
			}

			
			
  /*editar 2 */

		/*	@GetMapping(value="/editar_funcionario2")
		    public String editarfunc(ModelMap model) { 	
				    model.addAttribute("cargos", cargoRepository.findAll()); // ← ESSENCIAL!
				
		        return "funcionario/editar_funcionario2"; 
		    	    	
			}	
			*/
			
			@GetMapping("/editar_funcionario2")
			public String editarfunc(
			        @ModelAttribute("funcionario") FuncionarioModel funcionario,
			        ModelMap model) {

			    // Pegamos o cargo REAL do funcionário
			    CargoModel cargoAtual = funcionario.getCargo();

			    model.addAttribute("cargo", cargoAtual);  // ← ⬅ AGORA É O CERTO!
			    model.addAttribute("cargos", cargoRepository.findAll());

			    return "funcionario/editar_funcionario2";
			}

			
			@PostMapping("/editar_funcionario2")
			public String processarEdicaoFunc(
			        @ModelAttribute("cargo") CargoModel cargo, 
			        @ModelAttribute("funcionario") FuncionarioModel funcionario,
			        ModelMap model) {
			    
			    // Busca o cargo existente pelo ID selecionado no select
			    CargoModel cargoSelecionado = cargoRepository.findById(cargo.getIdCargo())
			            .orElse(null);
			    
			    // Associa o cargo ao funcionário em memória
			    funcionario.setCargo(cargoSelecionado);
			    
			    // Atualiza os objetos na sessão
			    model.addAttribute("cargo", cargoSelecionado);
			    model.addAttribute("funcionario", funcionario);
			    
			    System.out.println("Cargo associado ao funcionário: " + cargoSelecionado.getFuncao());

			    // Vai para o próximo passo (endereço)
			    return "redirect:/editar_funcionario3";
			}
			
			
			
/* editar3*/
			
			@GetMapping("/editar_funcionario3")
			public String editarFunc3(ModelMap model) {
			    // "endereco" já está na sessão
			    return "funcionario/editar_funcionario3";
			}

			@PostMapping("/editar_funcionario3")
			public String processarEdicaoFunc3(@ModelAttribute("endereco") EnderecoModel endereco) {
			    System.out.println("Editando endereço...");
			    return "redirect:/confirmacao_edicao_funcionario";
			}

	
			
/* confirmar edicao */
			
			
			
			 @GetMapping("/confirmacao_edicao_funcionario")
			 public String confirmarEdicaoFunc(
			         @ModelAttribute("funcionario") FuncionarioModel funcionario,
			         @ModelAttribute("cargo") CargoModel cargo,
			         @ModelAttribute("endereco") EnderecoModel endereco,
			         ModelMap model) {

			     // Reexpõe os atributos salvos na sessão para o template Thymeleaf
			     model.addAttribute("funcionario", funcionario);
			     model.addAttribute("cargo", cargo);
			     model.addAttribute("endereco", endereco);
			     

			     return "funcionario/confirmacao_edicao_funcionario";
			 }

			
			 
			 @PostMapping("/confirmacao_edicao_funcionario/salvar")
			 public String salvarFuncEdicao(
			         @ModelAttribute("funcionario") FuncionarioModel funcionario,
			         @ModelAttribute("endereco") EnderecoModel endereco,
			         @ModelAttribute("cargo") CargoModel cargo,
			         RedirectAttributes redirectAttributes) {

			     // Salvar endereço primeiro
			     enderecoRepository.save(endereco);

			     // Associar endereço (cargo já está definido)
			     funcionario.setEndereco(endereco);

			     // Salvar funcionário (sem criar novo cargo)
			     funcionarioRepository.save(funcionario);
			     
			     redirectAttributes.addFlashAttribute("sucesso", "Edição do funcionário concluída!");

			     return "redirect:/exibir_funcionario";
			 }
			
			 
			 
			 
/* excluir */
				
			
				@GetMapping("/funcionario/excluir/{id}")
				 public String excluirFunc(@PathVariable("id") Long id,
						 RedirectAttributes redirectAttributes) {
					try {
						funcionarioRepository.deleteById(id);
					    
						redirectAttributes.addFlashAttribute("sucesso", " Funcionário excluído com sucesso!");
					} catch (Exception e ) {
						redirectAttributes.addFlashAttribute("erro", "Erro ao excluir o Funcionário");
					}
					return "redirect:/exibir_funcionario"; 


}
			
	
}
