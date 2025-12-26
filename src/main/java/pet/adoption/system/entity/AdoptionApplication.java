package pet.adoption.system.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class AdoptionApplication {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  private Pet pet;

  @ManyToOne(optional = false)
  private User adopter;

  @Enumerated(EnumType.STRING)
  private Status status = Status.PENDING;

  @Column(length = 1000)
  private String message;

  private LocalDateTime appliedAt;

  @PrePersist
  void onApply() {
    this.appliedAt = LocalDateTime.now();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Pet getPet() {
    return pet;
  }

  public void setPet(Pet pet) {
    this.pet = pet;
  }

  public User getAdopter() {
    return adopter;
  }

  public void setAdopter(User adopter) {
    this.adopter = adopter;
  }

  public Status getStatus() {
    return status;
  } 

  public void setStatus(Status status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public LocalDateTime getAppliedAt() {
    return appliedAt;
  }

  public void setAppliedAt(LocalDateTime appliedAt) {
    this.appliedAt = appliedAt;
  }

  public enum Status {
    PENDING,
    APPROVED,
    REJECTED
  }


}
