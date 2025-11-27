
package br.com.creche_pet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.creche_pet.model.MatriculaModel;
import br.com.creche_pet.model.TurmaModel;
import br.com.creche_pet.repository.TurmaRepository;

@Service
public class MatriculaService {

    @Autowired
    TurmaRepository turmaRepository;

    public boolean verificarVagas(MatriculaModel matricula) {

        TurmaModel turma = turmaRepository.findById(matricula.getIdTurma())
                .orElseThrow(() -> new IllegalArgumentException("Turma não encontrada"));

        Long capacidade = turma.getCapacidade();
        int totalMatriculados = turmaRepository.totalMatriculados(matricula.getIdTurma());

        return totalMatriculados < capacidade;
    }
  }


