package br.com.creche_pet.model;

import java.io.Serializable;
import java.security.Timestamp;
import java.sql.Date;

// import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "pet", schema = "creche_pet")
public class PetModel implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)

  @Column(name = "id_pet")
  private Long idPet;

  @Column(name = "nome")
  private String nome;

  @Column(name = "especie")
  private String especie;

  @Column(name = "raca")
  private String raca;

  @Column(name = "data_nascimento")
  private Date dataNascimento;

  @Column(name = "peso")
  private Double peso;

  @Column(name = "observacoes")
  private String observacoes;

  /* @ManyToOne(optional = false,cascade = CascadeType.ALL) */
  @ManyToOne(optional = false)
  @JoinColumn(name = "id_tutor", nullable = false)
  private TutorModel tutor;

  public TutorModel getTutor() {
    return tutor;
  }

  public Long getIdPet() {
    return idPet;
  }

  public void setIdPet(Long idPet) {
    this.idPet = idPet;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getEspecie() {
    return especie;
  }

  public void setEspecie(String especie) {
    this.especie = especie;
  }

  public String getRaca() {
    return raca;
  }

  public void setRaca(String raca) {
    this.raca = raca;
  }

  public Date getDataNascimento() {
    return dataNascimento;
  }

  public void setDataNascimento(Date dataNascimento) {
    this.dataNascimento = dataNascimento;
  }

  public Double getPeso() {
    return peso;
  }

  public void setPeso(Double peso) {
    this.peso = peso;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
  }

  public void setLastUpdate(Timestamp timestamp) {
    // TODO Auto-generated method stub

  }

  public void setTutor(TutorModel tutor) {
    this.tutor = tutor;
  }

}
