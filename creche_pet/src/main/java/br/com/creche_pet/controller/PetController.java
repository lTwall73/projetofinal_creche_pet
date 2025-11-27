package br.com.creche_pet.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.creche_pet.model.EnderecoModel;
import br.com.creche_pet.model.PetModel;
import br.com.creche_pet.model.TutorModel;
import br.com.creche_pet.repository.EnderecoRepository;
import br.com.creche_pet.repository.PetRepository;
import br.com.creche_pet.repository.TutorRepository;

import org.springframework.ui.Model;


@Controller
@SessionAttributes({"pet", "tutor", "endereco"})
public class PetController {
	
	@Autowired
	PetRepository petRepository;
	
	@Autowired
	TutorRepository tutorRepository;
	
	@Autowired
    EnderecoRepository enderecoRepository;
	

	
	
	
	@GetMapping(value="/exibir_pet")
    public String pet(ModelMap model) { 	
		
		model.addAttribute("pets", petRepository.findAll());
		
        return "pet/exibir_pet"; 
    	    	
	}	
	
	
	
/* cadastrar 1 */
	
	
	
	@GetMapping(value="/cadastrar_pet")
    public String cadastrar(ModelMap model) { 	
		   model.addAttribute("pet", new PetModel());
		
        return "pet/cadastrar_pet"; 
    	    	
	}	
	
	
	@PostMapping("/cadastrar_pet")
	public String processarPet(@ModelAttribute("pet") PetModel pet) {
	 	System.out.println("cadastrando pet.");
	    // Aqui, o objeto 'pet' já foi armazenado na sessão por causa do @SessionAttributes
	    return "redirect:/cadastrar_pet2";
	}
	


 /* cadastrar 2 */
	
	
	 
		@GetMapping(value="/cadastrar_pet2")
	    public String cadastrar2(ModelMap model) { 	
			  model.addAttribute("tutor", new TutorModel());
			
	        return "pet/cadastrar_pet2"; 
	    	    	
		}	
		
		 @PostMapping("/cadastrar_pet2")
		    public String processarPet2(@ModelAttribute("tutor") TutorModel tutor) {
		    	System.out.println("cadastrando tutor.");
			 return "redirect:/cadastrar_pet3";
		 }
		 
		 
		 
		 
/* cadastrar 3 */
		 
			@GetMapping(value="/cadastrar_pet3")
		    public String cadastrar3(ModelMap model) { 	
				 model.addAttribute("endereco", new EnderecoModel());
				
		        return "pet/cadastrar_pet3"; 
		    	    	
			}	
			
			 @PostMapping("/cadastrar_pet3")
			    public String processarPet3(@ModelAttribute("endereco") EnderecoModel endereco) {
			    	System.out.println("cadastrando endereco");
				 return "redirect:/confirmacao_pet";
			 }
			 
			 
 /* confirmacao de cadastro do pet*/
			 
				@GetMapping(value = "/confirmacao_pet")
				public String confirmarPet(@SessionAttribute("pet") PetModel pet,
				                           @SessionAttribute("tutor") TutorModel tutor,
				                           @SessionAttribute("endereco") EnderecoModel endereco,
				                           Model model) {
				    // Agora você pode usar pet, tutor e endereco aqui

				    model.addAttribute("pet", pet);
				    model.addAttribute("tutor", tutor);
				    model.addAttribute("endereco", endereco);

				    return "pet/confirmacao_pet";
				}

				
				@PostMapping("/confirmacao_pet/salvar")
				public String finalizarCadastro(@SessionAttribute("pet") PetModel pet,
				                                @SessionAttribute("tutor") TutorModel tutor,
				                                @SessionAttribute("endereco") EnderecoModel endereco,
				                                RedirectAttributes redirectAttributes) {

				    // 1. Salva o endereço
				    EnderecoModel enderecoSalvo = enderecoRepository.save(endereco);

				    // 2. Atribui o endereço ao tutor e salva o tutor
				    tutor.setEndereco(enderecoSalvo);
				    TutorModel tutorSalvo = tutorRepository.save(tutor);

				    // 3. Atribui o tutor ao pet e salva o pet
				    pet.setTutor(tutorSalvo);
				    petRepository.save(pet);

				    // Mensagem de sucesso
				    redirectAttributes.addFlashAttribute("sucesso", "Pet cadastrado com sucesso!");
				    
				    return "redirect:/exibir_pet";
				}


			
				
/*editar*/
				
				
				@GetMapping("/editar/{id}")
				public String editarPet(@PathVariable Long id, ModelMap model) {
				    PetModel pet = petRepository.findById(id)
				        .orElseThrow(() -> new IllegalArgumentException("ID do pet inválido: " + id));

				    TutorModel tutor = pet.getTutor();
				    EnderecoModel endereco = tutor.getEndereco();

				    // Coloca os objetos na sessão
				    model.addAttribute("pet", pet);
				    model.addAttribute("tutor", tutor);
				    model.addAttribute("endereco", endereco);

				    return "pet/editar_pet"; // primeira página do formulário de edição
				}

				@PostMapping("/editar_pet")
				public String processarEdicaoPet(@ModelAttribute("pet") PetModel pet) {
				    System.out.println("Editando pet...");
				    return "redirect:/editar_pet2";
				}

				
				
/*editar 2 */
				
				
				@GetMapping("/editar_pet2")
				public String editarPet2(ModelMap model) {
				    // "tutor" já está na sessão
				    return "pet/editar_pet2";
				}

				@PostMapping("/editar_pet2")
				public String processarEdicaoPet2(@ModelAttribute("tutor") TutorModel tutor) {
				    System.out.println("Editando tutor...");
				    return "redirect:/editar_pet3";
				}

				
				
				
	/* editar3*/
				
				@GetMapping("/editar_pet3")
				public String editarPet3(ModelMap model) {
				    // "endereco" já está na sessão
				    return "pet/editar_pet3";
				}

				@PostMapping("/editar_pet3")
				public String processarEdicaoPet3(@ModelAttribute("endereco") EnderecoModel endereco) {
				    System.out.println("Editando endereço...");
				    return "redirect:/confirmacao_edicao_pet";
				}

				
				
	/* confirmar edicao*/
				
				
				@GetMapping("/confirmacao_edicao_pet")
				public String confirmarEdicaoPet(
				    @SessionAttribute("pet") PetModel pet,
				    @SessionAttribute("tutor") TutorModel tutor,
				    @SessionAttribute("endereco") EnderecoModel endereco,
				    Model model) {

				    model.addAttribute("pet", pet);
				    model.addAttribute("tutor", tutor);
				    model.addAttribute("endereco", endereco);

				    return "pet/confirmacao_edicao_pet";
				}


				
				@PostMapping("/confirmacao_edicao_pet/salvar")
				public String finalizarEdicaoPet(
				    @SessionAttribute("pet") PetModel pet,
				    @SessionAttribute("tutor") TutorModel tutor,
				    @SessionAttribute("endereco") EnderecoModel endereco,
				    RedirectAttributes redirectAttributes,
				    SessionStatus status) {

				    // Atualiza endereço
				    EnderecoModel enderecoSalvo = enderecoRepository.save(endereco);

				    // Atualiza tutor com novo endereço
				    tutor.setEndereco(enderecoSalvo);
				    TutorModel tutorSalvo = tutorRepository.save(tutor);

				    // Atualiza pet com novo tutor
				    pet.setTutor(tutorSalvo);
				    petRepository.save(pet);

				    // Limpa sessão
				    status.setComplete();

				    redirectAttributes.addFlashAttribute("sucesso", "Edição concluída com sucesso!");
				    
				   return "redirect:/exibir_pet";
				}


/* excluir pet*/
				
		


				@GetMapping("/excluir/{id}")
				 public String excluir(@PathVariable("id") Long id,
						 RedirectAttributes redirectAttributes) {
					try {
						petRepository.deleteById(id);
					    
						redirectAttributes.addFlashAttribute("sucesso", "Pet excluído com sucesso!");
					} catch (Exception e ) {
						redirectAttributes.addFlashAttribute("erro", "Erro ao excluir o Pet.");
					}
					return "redirect:/exibir_pet"; //redireciona para a pagina de listagem principal


}
				

 /* cadastrar o segundo pet no mesmo dono  */
				
				/*
				@GetMapping(value="/cadastrar_pet_novo")
			    public String cadastrarNovo( ModelMap model) { 	
					   model.addAttribute("pet", new PetModel());

					   
					
			        return "pet/cadastrar_pet_novo";*/
				
			
				@GetMapping(value="/cadastrar_pet_novo/{id}")
			    public String cadastrarNovo(@PathVariable Long id, ModelMap model) { 	
					   model.addAttribute("pet", new PetModel());
					   model.addAttribute("idTutor", id);
					   
					
			        return "pet/cadastrar_pet_novo";
			    	    	
				}	
				
				
				@PostMapping("/cadastrar_pet_novo")
				public String processarPetNovo(@ModelAttribute("pet") PetModel pet
						, RedirectAttributes redirectAttributes) {

				    try {
				        petRepository.save(pet);
				        redirectAttributes.addFlashAttribute("sucesso", " Pet cadastrado com sucesso!");
				    } catch (Exception e) {
				        redirectAttributes.addFlashAttribute("erro", "Erro ao cadastrar o Serviço!");
				    }
					
					System.out.println("cadastrando pet novo.");
				    // Aqui, o objeto 'pet' já foi armazenado na sessão por causa do @SessionAttributes
				    return "redirect:/exibir_pet";
				}
}


