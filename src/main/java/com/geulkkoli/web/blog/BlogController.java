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
import com.geulkkoli.web.blog.dto.*;
import com.geulkkoli.web.comment.dto.CommentBodyDTO;
import com.geulkkoli.web.follow.dto.FollowResultDTO;
import com.geulkkoli.web.blog.dto.UserProfileDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
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
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequestMapping("/blog")
@Controller
public class BlogController {
    private final PostService postService;
    private final PostFindService postFindService;
    private final UserFindService userFindService;
    private final FavoriteService favoriteService;
    private final FollowFindService followFindService;
    private final HashTagFindService hashTagFindService;
    @Value("${comm.uploadPath}")
    private String uploadPath;

    public BlogController(PostService postService, PostFindService postFindService, UserFindService userFindService, FavoriteService favoriteService, PostHahTagFindService postHashTagFindService, FollowFindService followFindService, HashTagFindService hashTagFindService) {
        this.postService = postService;
        this.postFindService = postFindService;
        this.userFindService = userFindService;
        this.favoriteService = favoriteService;
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

    @GetMapping("/{nickName}")
    public ModelAndView renderingBlog(@PathVariable("nickName") String nickName, @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, @AuthenticationPrincipal CustomAuthenticationPrinciple authUser) {
        ModelAndView modelAndView = new ModelAndView("blog/home");
        User user = userFindService.findByNickName(nickName);
        if (Objects.isNull(user)) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }
        //로그인 안했을 때
        if (Objects.isNull(authUser)){
            List<Post> posts = user.getPosts().stream().sorted(Comparator.comparing(Post::getCreatedAt).reversed()).collect(toList());
            List<Post> subPost = posts.subList(pageable.getPageNumber() * pageable.getPageSize(), Math.min((pageable.getPageNumber() + 1) * pageable.getPageSize(), posts.size()));
            PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id").descending());

            int totalPosts = posts.size();
            Page<Post> pagePost = new PageImpl<>(subPost, pageRequest, totalPosts);
            Page<ArticlePagingRequestDTO> readInfos = pagePost.map(ArticlePagingRequestDTO::toDTO);
            PagingDTO pagingDTO = PagingDTO.listDTOtoPagingDTO(readInfos);

            modelAndView.addObject("channelName", nickName);
            modelAndView.addObject("loginUserName", "guest");
            modelAndView.addObject("page",pagingDTO);

            return modelAndView;
        }

        //로그인 했을 때
        List<Post> posts = user.getPosts().stream().sorted(Comparator.comparing(Post::getCreatedAt).reversed()).collect(toList());
        List<Post> subPost = posts.subList(pageable.getPageNumber() * pageable.getPageSize(), Math.min((pageable.getPageNumber() + 1) * pageable.getPageSize(), posts.size()));
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id").descending());

        int totalPosts = posts.size();
        Page<Post> pagePost = new PageImpl<>(subPost, pageRequest, totalPosts);
        Page<ArticlePagingRequestDTO> readInfos = pagePost.map(ArticlePagingRequestDTO::toDTO);
        PagingDTO pagingDTO = PagingDTO.listDTOtoPagingDTO(readInfos);

        modelAndView.addObject("channelName", nickName);
        modelAndView.addObject("loginUserName", authUser.getNickName());
        modelAndView.addObject("page",pagingDTO);
        return modelAndView;
    }


    @GetMapping("/read/{postId}")
    public String renderingArticle(Model model, @PathVariable Long postId,
                                   @RequestParam(defaultValue = "0") String page,
                                   @RequestParam(defaultValue = "") String searchType,
                                   @RequestParam(defaultValue = "") String searchWords,
                                   @AuthenticationPrincipal CustomAuthenticationPrinciple authUser) {
        Post post = postService.showDetailPost(postId);
        log.info("게시글 해시태그 변경 후 조회: {}", post.getPostHashTags());
        ArticleDTO postPage = ArticleDTO.toDTO(post);
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
            return "blog/article";
        }

        User loggingUser = userFindService.findById(Long.parseLong(authUser.getUserId()));
        if (favoriteService.checkFavorite(postFindService.findById(postId), loggingUser).isEmpty()) {
            checkFavorite = "none";
        } else {
            checkFavorite = "exist";
        }
        boolean mine = loggingUser.getUserId().equals(authorUser.getUserId());
        Boolean follow = followFindService.checkFollow(loggingUser, authorUser);
        FollowResultDTO followResult = new FollowResultDTO(mine, follow);


        model.addAttribute("followResult", followResult);
        model.addAttribute("post", postPage);
        model.addAttribute("pageNumber", page);
        model.addAttribute("commentList", postPage.getCommentList());
        model.addAttribute("authorUser", authorUser);
        model.addAttribute("checkFavorite", checkFavorite);
        model.addAttribute("loginUserId", authUser.getUserId());
        model.addAttribute("comments", new CommentBodyDTO());
        searchDefault(model, searchType, searchWords);
        return "blog/article";
    }

    @GetMapping("/write")
    public String renderingArticleWrite(Model model) {
        model.addAttribute("writeRequestDTO", new WriteRequestDTO());
        return "blog/write";
    }

    //새 게시글 등록
    @PostMapping("/write")
    public String writeArticle(@Validated @ModelAttribute WriteRequestDTO WriteRequestDTO, BindingResult bindingResult,
                               RedirectAttributes redirectAttributes, HttpServletResponse response)
            throws UnsupportedEncodingException {
        log.info("writeRequestDTO: {}", WriteRequestDTO);

        if (bindingResult.hasErrors()) {
            log.info("bindingResult: {}", bindingResult);
            return "blog/write";
        }

        User user = userFindService.findById(WriteRequestDTO.getAuthorId());

        long postId = postService.writeArtice(WriteRequestDTO, user).getPostId();
        redirectAttributes.addAttribute("postId", postId);
        response.addCookie(new Cookie(URLEncoder.encode(WriteRequestDTO.getNickName(), "UTF-8"), "done"));

        return "redirect:/blog/read/" + postId;
    }

    @GetMapping("/update/{postId}")
    public String renderingArticleEdit(Model model, @PathVariable Long postId, @RequestParam(defaultValue = "0") String page,
                                       @RequestParam(defaultValue = "") String searchType,
                                       @RequestParam(defaultValue = "") String searchWords) {
        ArticleEditRequestDTO editPost = ArticleEditRequestDTO.toDTO(postFindService.findById(postId));
        log.info("editPost: {}", editPost);
        model.addAttribute("editDTO", editPost);
        model.addAttribute("pageNumber", page);
        searchDefault(model, searchType, searchWords);

        return "blog/edit";
    }

    //게시글 수정
    @PostMapping("/update/{postId}")
    public String editArticle(@Validated @ModelAttribute ArticleEditRequestDTO updateParam, BindingResult bindingResult,
                              @PathVariable Long postId, RedirectAttributes redirectAttributes,
                              @RequestParam(defaultValue = "0") String page,
                              @RequestParam(defaultValue = "") String searchType,
                              @RequestParam(defaultValue = "") String searchWords) {

        if (bindingResult.hasErrors()) {
            return "blog/edit";
        }

        Post willUpdate = postFindService.findById(postId);
        postService.updatePost(willUpdate, updateParam);

        redirectAttributes.addAttribute("updateStatus", true);
        redirectAttributes.addAttribute("page", page);
        redirectAttributes.addAttribute("searchType", searchType);
        redirectAttributes.addAttribute("searchWords", searchWords);

        return "redirect:/blog/read/{postId}?page=" + page + "&searchType=" + searchType + "& searchWords=" + searchWords;
    }

    @DeleteMapping("/{nickName}/delete/{postId}")
    public RedirectView deleteArticle(@PathVariable("nickName") String userNickName, @PathVariable("postId") Long postId) {
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

        RedirectView redirectView = new RedirectView("/blog/" + encode);
        log.info("redirectView : {}", redirectView.getUrl());
        return redirectView;
    }

    @GetMapping("/tag/{tag}/{subTag}")
    public ModelAndView postListByTags(@PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                       @PathVariable String tag, @PathVariable(required = false) String subTag) {
        List<HashTag> hashTag = hashTagFindService.findHashTags(tag, subTag);
        Page<ArticlePagingRequestDTO> postRequestListDTOS = postFindService.findPostByTag(pageable, hashTag);
        PagingDTO pagingDTO = PagingDTO.listDTOtoPagingDTO(postRequestListDTOS);
        ModelAndView modelAndView = new ModelAndView("post/channels");
        modelAndView.addObject("page", pagingDTO);
        return modelAndView;
    }

    @GetMapping("/tag/{tag}")
    public ModelAndView postListByTag(@PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                      @PathVariable String tag) {
        List<HashTag> hashTag = hashTagFindService.findHashTag(tag);
        Page<ArticlePagingRequestDTO> postRequestListDTOS = postFindService.findPostByTag(pageable, hashTag);
        PagingDTO pagingDTO = PagingDTO.listDTOtoPagingDTO(postRequestListDTOS);
        ModelAndView modelAndView = new ModelAndView("post/channels");
        modelAndView.addObject("page", pagingDTO);
        return modelAndView;
    }



    private static void searchDefault(Model model, String searchType, String searchWords) {
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchWords", searchWords);
    }

}
