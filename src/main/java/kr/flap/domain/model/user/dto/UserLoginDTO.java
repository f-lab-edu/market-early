package kr.flap.domain.model.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 로그인 정보를 담은 DTO.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {

  /* 로그인에 필요한 이메일 주소 */
  @NotBlank(message = "이메일을 입력해주세요.")
  @Email(message = "이메일 형식을 맞춰주세요.")
  private String email;

  /* 로그인에 필요한 비밀번호 주소 */
  @NotBlank(message = "비밀번호를 입력해야합니다.")
  @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")

  @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$",
          message = "비밀번호는 영문 대/소문자, 숫자, 특수문자를 포함해야 합니다.")
  private String password;
}
