package com.geulkkoli.web.post;

import com.geulkkoli.web.post.dto.AddDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequestMapping("/v3")
public class PostApiController {

    @PostMapping("/post")
    public void addPost(@RequestBody AddDTO addDTO) {
      log.info("addDTO: {}", addDTO);
    }
}
