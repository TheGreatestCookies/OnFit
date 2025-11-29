package kspo.onfit.post.dto;

import jakarta.validation.constraints.Size;
import java.util.List;

public record PostRequestDto(

        @Size(min = 1, max = 15, message = "제목은 2글자 이상 15글자 미만으로 작성 가능합니다.")
        String title,

        @Size(min = 10, max = 200, message = "포스트는 10글자 이상 200글자 미만으로 작성 가능합니다.")
        String content,

        @Size(max = 10, message = "이미지는 최대 10장 업로드 가능합니다.")
        List<String> imagesUrls
)
{ }
