package kr.flap.domain.model.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserJoinResponseDTO {

  private Long id;
  private String username;
}
