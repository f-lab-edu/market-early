package kr.flap.domain.model.user.dto.error;

public class UserLoginErrorResponseDTO {
  private String message;

  public UserLoginErrorResponseDTO(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
