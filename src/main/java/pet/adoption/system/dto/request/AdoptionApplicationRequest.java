package pet.adoption.system.dto.request;

import jakarta.validation.constraints.NotNull;

public class AdoptionApplicationRequest {
  @NotNull
  private Long petId;

  private String message;


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
