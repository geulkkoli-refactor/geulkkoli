package com.geulkkoli.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.geulkkoli.domain.comment.service.CommentsService;
import com.geulkkoli.domain.post.Post;
import com.geulkkoli.domain.post.service.PostService;
import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.UserRepository;
import com.geulkkoli.web.comment.dto.CommentBodyDTO;
import com.geulkkoli.web.comment.dto.CommentEditDTO;
import com.geulkkoli.web.post.dto.PostAddDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostService postService;
    @Autowired
    TestUserDetailService testUserDetailService;
    @Autowired
    CommentsService commentsService;

    User user;
    Post post01;

    ObjectMapper om = new ObjectMapper();
    MediaType mediaType = new MediaType(
            MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @BeforeEach
    void init() {
        User save = User.builder()
                .email("email")
                .userName("김")
                .nickName("바나나")
                .phoneNo("0101223671")
                .password("123qwe!@#")
                .gender("Male").build();
        user = userRepository.save(save);

        PostAddDTO postAddDTO01 = PostAddDTO.builder()
                .title("testTitle01")
                .postBody("test postbody 01")
                .nickName(testUserDetailService.loadUserByUsername("바나나").getUsername())
                .build();
        post01 = postService.savePost(postAddDTO01, user);

        commentsService.writeComment(new CommentBodyDTO("이미 작성된 댓글 불러오기 테스트용"), post01, user);
    }

    private class Comment {
//        public Long commentId;
        public String commentBody;

        public Comment(String commentBody) {
            this.commentBody = commentBody;
        }

//        public Comment(Long commentId, String commentBody) {
//            this.commentId = commentId;
//            this.commentBody = commentBody;
//        }
    }

    @WithUserDetails(value = "test", userDetailsServiceBeanName = "testUserDetailService")
    @Test
    void 댓글_유효성_체크() throws Exception {

        //ginven
        String commentJson = commentToJson(new Comment("댓"));
        Long postId = post01.getPostId();

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/comments/" + postId)
                        .contentType(mediaType).content(commentJson))

                //than
                .andExpect(status().is(400))
                .andDo(print());

    }

    @WithUserDetails(value = "test", userDetailsServiceBeanName = "testUserDetailService")
    @Test
    void 댓글_작성() throws Exception {

        //ginven
        String commentJson = commentToJson(new Comment("댓글 달기 테스트"));
        Long postId = post01.getPostId();

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/comments/" + postId)
                        .contentType(mediaType).content(commentJson))

        //than
                .andExpect(status().is(201))
                .andExpect(jsonPath("$[?(@.commentBody == '%s')]", "댓글 달기 테스트").exists());

    }

    private String commentToJson(Object comment) throws JsonProcessingException {
        om.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        return om.writeValueAsString(comment);
    }

    @WithUserDetails(value = "test", userDetailsServiceBeanName = "testUserDetailService")
    @Test
    void 댓글_변경() throws Exception {

        //ginven
        String commentJson = commentToJson(new CommentEditDTO(1L, "댓글 변경 테스트"));
        Long postId = post01.getPostId();

        //when
        mockMvc.perform(MockMvcRequestBuilders.put("/comments/" + postId)
                        .contentType(mediaType).content(commentJson))

                //than
                .andExpect(status().is(200))
                .andExpect(jsonPath("$[?(@.commentBody == '%s')]", "댓글 변경 테스트").exists())
                .andExpect(jsonPath("$[?(@.commentBody == '%s')]", "이미 작성된 댓글 불러오기 테스트용").doesNotExist());

    }

    @WithUserDetails(value = "test", userDetailsServiceBeanName = "testUserDetailService")
    @Test
    void 댓글_삭제() throws Exception {
        //given
        String commentJson = commentToJson(new Comment("삭제 테스트"));

        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/comments").contentType(mediaType).content(commentJson))

                //than
                .andExpect(status().is(200));
    }
}
