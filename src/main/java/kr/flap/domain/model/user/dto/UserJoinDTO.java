package kr.flap.domain.model.user.dto;

import jakarta.validation.constraints.*;
import kr.flap.domain.model.user.UserAddress;
import kr.flap.domain.model.user.enums.UserGender;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@NoArgsConstructor
@Data
public class UserJoinDTO {

  @NotBlank(message = "아이디를 입력해주세요.")
  @Length(min = 1, max = 20)
  private String username;

  @NotBlank(message = "닉네임을 입력해주세요.")
  @Length(min = 1, max = 20)
  private String nickname;

  @NotBlank(message = "비밀번호를 입력해야합니다.")
  @Size(min = 8, max = 16, message = "비밀번호는 8자 이상 16자 이하로 입력해주세요.")
  private String password;

  @Email(message = "이메일 형식을 맞춰주세요.")
  private String email;

  @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "휴대폰 번호 형식이 올바르지 않습니다.")
  private String mobileNumber;

  private LocalDate birthday;

  @NotNull(message = "성별을 선택해야 합니다.")
  private UserGender gender;

  private UserAddress userAddress;

  @Builder
  public UserJoinDTO(String username, String nickname, String password, String email, String mobileNumber, LocalDate birthday, UserGender gender, UserAddress userAddress) {
    this.username = username;
    this.nickname = nickname;
    this.password = password;
    this.email = email;
    this.mobileNumber = mobileNumber;
    this.birthday = birthday;
    this.gender = gender;
    this.userAddress = userAddress;
  }
}
