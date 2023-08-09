package com.geulkkoli.web.post.dto;

import com.geulkkoli.domain.post.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.util.HtmlUtils;

import static org.junit.jupiter.api.Assertions.*;

class PostRequestDTOTest {

    @DisplayName("게시글 요약본을 11자 보여 준다.")
    @Test
    void getContentSummary() {

        String content = "<div class=\"se-wrapper-inner se-wrapper-wysiwyg sun-editor-editable\" contenteditable=\"true\" scrolling=\"auto\" style=\"height: auto;\"><p><span style=\"font-size: 19px\">3. 1.1 AAA 패턴</span><br></p><p style=\"text-align: left\">AAA 패턴은 각 테스트를 준비, 실행, 검증이라는 세부분으로 나누는 걸 말한다.";

        PostRequestDTO postRequestDTO = new PostRequestDTO(1L, "title", "볶음감자", content, "2021-08-01", 1);

        assertEquals("3. 1.1 AAA 패턴AAA 패턴은", postRequestDTO.getContentSummary());
    }
}