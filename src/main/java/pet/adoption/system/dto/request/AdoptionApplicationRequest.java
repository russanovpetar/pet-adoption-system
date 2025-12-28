package pet.adoption.system.dto.request;

import jakarta.validation.constraints.NotNull;

public class AdoptionApplicationRequest {
  @NotNull
  private Long petId;

  private String message;

  public void setPetId(Long petId) {
    this.petId = petId;
  }

  public Long getPetId() {
    return petId;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
