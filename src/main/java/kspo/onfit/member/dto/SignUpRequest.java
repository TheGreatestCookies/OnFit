package kspo.onfit.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SignUpRequest(
        @NotBlank(message = "이메일은 필수 입력값입니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수 입력값입니다.")
        String password,

        @NotBlank(message = "이름은 필수 입력값입니다.")
        String name,

        @NotNull(message = "프로필 이미지는 필수 선택값입니다.")
        @Min(value = 1, message = "프로필 이미지는 1번부터 5번까지 선택 가능합니다.")
        @Max(value = 5, message = "프로필 이미지는 1번부터 5번까지 선택 가능합니다.")
        Integer profileImageNumber
) {
}
