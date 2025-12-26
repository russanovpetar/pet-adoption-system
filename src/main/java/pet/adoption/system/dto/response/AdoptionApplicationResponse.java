package pet.adoption.system.dto.response;

import io.micrometer.common.util.StringUtils;
import java.time.LocalDateTime;
import pet.adoption.system.entity.AdoptionApplication;

public class AdoptionApplicationResponse {
  private final Long id;
  private final String petName;
  private final String adopterUsername;
  private final String status;
  private final String message;
  private final LocalDateTime appliedAt;

  public AdoptionApplicationResponse(AdoptionApplication app) {
    this.id = app.getId();
    this.petName = app.getPet().getName();
    this.adopterUsername = app.getAdopter().getUsername();
    this.status = app.getStatus().name();
    this.message = app.getMessage();
    this.appliedAt = app.getAppliedAt();
  }

  public Long getId() {
    return id;
  }

  public String getPetName() {
    return petName;
  }

  public String getAdopterUsername() {
    return adopterUsername;
  }

  public String getStatus() {
    return status;
  }

  public String getMessage() {
    return StringUtils.isEmpty(message) ? "" : message;
  }

  public LocalDateTime getAppliedAt() {
    return appliedAt;
  }
}
