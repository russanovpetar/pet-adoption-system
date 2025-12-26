package pet.adoption.system.dto.response;

public class GenericResponse {
  private String msg;

  public GenericResponse(String msg) {
    this.msg = msg;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }
}
