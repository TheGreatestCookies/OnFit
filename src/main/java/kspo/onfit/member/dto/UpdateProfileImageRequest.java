package kspo.onfit.member.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateProfileImageRequest(
        @NotNull(message = "프로필 이미지 번호는 필수입니다.")
        @Min(value = 1, message = "프로필 이미지 번호는 1 이상이어야 합니다.")
        @Max(value = 6, message = "프로필 이미지 번호는 6 이하여야 합니다.")
        Integer profileImageNumber
) {
}
