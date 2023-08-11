package com.geulkkoli.web.blog;

import com.geulkkoli.application.user.CustomAuthenticationPrinciple;
import com.geulkkoli.domain.favorites.service.FavoriteService;
import com.geulkkoli.domain.follow.service.FollowFindService;
import com.geulkkoli.domain.hashtag.HashTag;
import com.geulkkoli.domain.hashtag.service.HashTagFindService;
import com.geulkkoli.domain.post.NotAuthorException;
import com.geulkkoli.domain.post.Post;
import com.geulkkoli.domain.post.service.PostFindService;
import com.geulkkoli.domain.post.service.PostService;
import com.geulkkoli.domain.posthashtag.service.PostHahTagFindService;
import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.service.UserFindService;
import com.geulkkoli.web.comment.dto.CommentBodyDTO;
import com.geulkkoli.web.follow.dto.FollowResult;
import com.geulkkoli.web.post.UserProfileDTO;
import com.geulkkoli.web.post.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Controller
public class BlogController {
    private final PostService postService;
    private final PostFindService postFindService;
    private final UserFindService userFindService;
    private final FavoriteService favoriteService;
    private final PostHahTagFindService postHashTagFindService;
    private final FollowFindService followFindService;
    private final HashTagFindService hashTagFindService;
    @Value("${comm.uploadPath}")
    private String uploadPath;

    public BlogController(PostService postService, PostFindService postFindService, UserFindService userFindService, FavoriteService favoriteService, PostHahTagFindService postHashTagFindService, FollowFindService followFindService, HashTagFindService hashTagFindService) {
        this.postService = postService;
        this.postFindService = postFindService;
        this.userFindService = userFindService;
        this.favoriteService = favoriteService;
        this.postHashTagFindService = postHashTagFindService;
        this.followFindService = followFindService;
        this.hashTagFindService = hashTagFindService;
    }

    @PostMapping("/upload-file")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile multipartFile) {
        String originalFileName = multipartFile.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileName = UUID.randomUUID() + extension;
        String src = "/imageFile/" + fileName;

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists())
            uploadDir.mkdir();

        File uploadDirFile = new File(uploadPath + fileName);

        try {
            multipartFile.transferTo(uploadDirFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return src;
    }

    @GetMapping("/tag/{tag}/{subTag}")
    public ModelAndView postListByTags(@PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                       @PathVariable String tag, @PathVariable(required = false) String subTag) {
        List<HashTag> hashTag = hashTagFindService.findHashTags(tag, subTag);
        Page<PostRequestDTO> postRequestListDTOS = postFindService.findPostByTag(pageable, hashTag);
        PagingDTO pagingDTO = PagingDTO.listDTOtoPagingDTO(postRequestListDTOS);
        ModelAndView modelAndView = new ModelAndView("post/channels");
        modelAndView.addObject("page", pagingDTO);
        return modelAndView;
    }

    @GetMapping("/tag/{tag}")
    public ModelAndView postListByTag(@PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                      @PathVariable String tag) {
        List<HashTag> hashTag = hashTagFindService.findHashTag(tag);
        Page<PostRequestDTO> postRequestListDTOS = postFindService.findPostByTag(pageable, hashTag);
        PagingDTO pagingDTO = PagingDTO.listDTOtoPagingDTO(postRequestListDTOS);
        ModelAndView modelAndView = new ModelAndView("post/channels");
        modelAndView.addObject("page", pagingDTO);
        return modelAndView;
    }

    @GetMapping("/add")
    public String postAddForm(Model model) {
        model.addAttribute("addDTO", new PostAddDTO());
        return "post/post-add";
    }

    //새 게시글 등록
    @PostMapping("/add")
    public String postAdd(@Validated @ModelAttribute PostAddDTO post, BindingResult bindingResult,
                          RedirectAttributes redirectAttributes, HttpServletResponse response, HttpServletRequest request)
            throws UnsupportedEncodingException {
        redirectAttributes.addAttribute("page", request.getSession().getAttribute("pageNumber"));
        log.info("addDTO: {}", post);
        if (bindingResult.hasErrors()) {
            return "post/post-add";
        }

        User user = userFindService.findById(post.getAuthorId());

        long postId = postService.savePost(post, user).getPostId();
        redirectAttributes.addAttribute("postId", postId);
        response.addCookie(new Cookie(URLEncoder.encode(post.getNickName(), "UTF-8"), "done"));

        return "redirect:/post/read/" + postId;
    }

    @GetMapping("/read/{postId}")
    public String readPost(Model model, @PathVariable Long postId,
                           @RequestParam(defaultValue = "0") String page,
                           @RequestParam(defaultValue = "") String searchType,
                           @RequestParam(defaultValue = "") String searchWords,
                           @AuthenticationPrincipal CustomAuthenticationPrinciple authUser) {
        Post post = postService.showDetailPost(postId);
        log.info("게시글 해시태그 변경 후 조회: {}", post.getPostHashTags());
        PageDTO postPage = PageDTO.toDTO(post);
        User authorUser = userFindService.findById(postPage.getAuthorId());
        UserProfileDTO userProfile = UserProfileDTO.toDTO(authorUser);


        String checkFavorite = "never clicked";

        if (Objects.isNull(authUser)) {
            log.info("로그인을 안한 사용자 접속");
            model.addAttribute("post", postPage);
            model.addAttribute("pageNumber", page);
            model.addAttribute("commentList", postPage.getCommentList());
            model.addAttribute("authorUser", userProfile);
            model.addAttribute("checkFavorite", checkFavorite);
            searchDefault(model, searchType, searchWords);
            return "post/blog-post";
        }

        User loggingUser = userFindService.findById(Long.parseLong(authUser.getUserId()));
        if (favoriteService.checkFavorite(postFindService.findById(postId), loggingUser).isEmpty()) {
            checkFavorite = "none";
        } else {
            checkFavorite = "exist";
        }
        boolean mine = loggingUser.getUserId().equals(authorUser.getUserId());
        Boolean follow = followFindService.checkFollow(loggingUser, authorUser);
        FollowResult followResult = new FollowResult(mine, follow);


        model.addAttribute("followResult", followResult);
        model.addAttribute("post", postPage);
        model.addAttribute("pageNumber", page);
        model.addAttribute("commentList", postPage.getCommentList());
        model.addAttribute("authorUser", authorUser);
        model.addAttribute("checkFavorite", checkFavorite);
        model.addAttribute("loginUserId", authUser.getUserId());
        model.addAttribute("comments", new CommentBodyDTO());
        searchDefault(model, searchType, searchWords);
        return "post/blog-post";
    }

    @GetMapping("/update/{postId}")
    public String movePostEditForm(Model model, @PathVariable Long postId, @RequestParam(defaultValue = "0") String page,
                                   @RequestParam(defaultValue = "") String searchType,
                                   @RequestParam(defaultValue = "") String searchWords) {
        PostEditRequestDTO editPost = PostEditRequestDTO.toDTO(postFindService.findById(postId));
        log.info("editPost: {}", editPost);
        model.addAttribute("editDTO", editPost);
        model.addAttribute("pageNumber", page);
        searchDefault(model, searchType, searchWords);

        return "post/edit";
    }

    //게시글 수정
    @PostMapping("/update/{postId}")
    public String editPost(@Validated @ModelAttribute PostEditRequestDTO updateParam, BindingResult bindingResult,
                           @PathVariable Long postId, RedirectAttributes redirectAttributes,
                           @RequestParam(defaultValue = "0") String page,
                           @RequestParam(defaultValue = "") String searchType,
                           @RequestParam(defaultValue = "") String searchWords) {

        if (bindingResult.hasErrors()) {
            return "post/edit";
        }

        Post willUpdate = postFindService.findById(postId);
        postService.updatePost(willUpdate, updateParam);

        redirectAttributes.addAttribute("updateStatus", true);
        redirectAttributes.addAttribute("page", page);
        redirectAttributes.addAttribute("searchType", searchType);
        redirectAttributes.addAttribute("searchWords", searchWords);

        return "redirect:/post/read/{postId}?page=" + page + "&searchType=" + searchType + "& searchWords=" + searchWords;
    }

    @DeleteMapping("/{nickName}/delete/{postId}")
    public RedirectView deletePost(@PathVariable("nickName") String userNickName, @PathVariable("postId") Long postId) {
        User requestUser = userFindService.findByNickName(userNickName);
        Post post = postFindService.findById(postId);
        if (!post.getUser().equals(requestUser)) {
            try {
                NotAuthorException notAuthorException = new NotAuthorException("해당 게시글의 작성자가 아닙니다.");
            } catch (NotAuthorException e) {
                log.error(e.getMessage());
                return new RedirectView("/error");
            }
        }
        postService.deletePost(post, requestUser);
        String encode = UriComponentsBuilder.newInstance().path(userNickName).build().encode().toUriString();
        log.info("userNickName : {}", encode);

        RedirectView redirectView = new RedirectView("/user/" + encode);
        log.info("redirectView : {}", redirectView.getUrl());
        return redirectView;
    }


    private static void searchDefault(Model model, String searchType, String searchWords) {
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchWords", searchWords);
    }

    private static void searchDefault(ModelAndView model, String searchType, String searchWords) {
        model.addObject("searchType", searchType);
        model.addObject("searchWords", searchWords);
    }

}
