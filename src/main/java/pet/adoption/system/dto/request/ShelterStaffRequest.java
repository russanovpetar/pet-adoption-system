package pet.adoption.system.dto.request;

public class ShelterStaffRequest {
  private String username;
  private String password;
  private Long shelterId;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Long getShelterId() {
    return shelterId;
  }

  public void setShelterId(Long shelterId) {
    this.shelterId = shelterId;
  }
}
