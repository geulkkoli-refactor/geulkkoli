package com.geulkkoli.web.admin;

import com.geulkkoli.domain.admin.service.AdminServiceImpl;
import com.geulkkoli.domain.post.AdminTagAccessDenied;
import com.geulkkoli.domain.post.service.PostFindService;
import com.geulkkoli.domain.post.service.PostService;
import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.service.UserFindService;
import com.geulkkoli.web.post.dto.PostAddDTO;
import com.geulkkoli.web.post.dto.PostEditRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminServiceImpl adminService;

    private final UserFindService userFindService;

    private final PostService postService;
    private final PostFindService postFindService;

    @GetMapping("/") // 어드민 기본 페이지 링크
    public String adminIndex(Model model) {
        List<DailyTopicDto> weeklyTopic = adminService.findWeeklyTopic();

        model.addAttribute("data", weeklyTopic);

        return "admin/adminIndex";
    }

    @ResponseBody
    @PostMapping("/calendar/update")
    public ResponseEntity<Void> updateTheme(@RequestBody DailyTopicDto dailyTopicDto) {

        adminService.updateTopic(dailyTopicDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/reportedPostList") //신고받은 게시물 링크
    public String reportedPostList(Model model) {
        model.addAttribute("list", adminService.findAllReportedPost());
        return "admin/reportedPostList";
    }

    //lock user with spring security
    @ResponseBody
    @PostMapping("/lockUser")
    public String lockUser(@RequestBody UserLockDto UserLockDto) {
        log.info("postId : {}, reason : {}, date : {}", UserLockDto.getPostId(), UserLockDto.getLockReason(), UserLockDto.getLockDate());
        User user = adminService.findUserByPostId(UserLockDto.getPostId());
        adminService.lockUser(user.getUserId(), UserLockDto.getLockReason(), UserLockDto.getLockDate());

        String lockDate = (LocalDateTime.now().plusDays(UserLockDto.getLockDate()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return "회원이 \"" + UserLockDto.getLockReason() + "\" 의 사유로" + lockDate + "까지 정지되었습니다.";
    }

    @ResponseBody
    @DeleteMapping("/delete/{postId}")
    public String postDelete(@PathVariable Long postId) {
        adminService.deletePost(postId);
        return "성공적으로 삭제되었습니다";
    }

    @GetMapping("/add")
    public String postAddForm(Model model) {
        model.addAttribute("addDTO", new PostAddDTO());
        return "admin/noticeAddForm";
    }

    //새 게시글 등록
    @PostMapping("/add")
    public String postAdd(@Validated @ModelAttribute PostAddDTO post, BindingResult bindingResult,
                          RedirectAttributes redirectAttributes, HttpServletResponse response, HttpServletRequest request) {
        redirectAttributes.addAttribute("page", request.getSession().getAttribute("pageNumber"));

        User user = userFindService.findById(post.getAuthorId());
        long postId;

        if (bindingResult.hasErrors()) {
            return "admin/noticeAddForm";
        }

        postId = adminService.saveNotice(post, user).getPostId();

        redirectAttributes.addAttribute("postId", postId);
        response.addCookie(new Cookie(URLEncoder.encode(post.getNickName(), StandardCharsets.UTF_8), "done"));
        return "redirect:/post/read/{postId}";
    }

    @GetMapping("/update/{postId}")
    public String movePostEditForm(Model model, @PathVariable Long postId,
                                   @RequestParam(defaultValue = "") String searchType,
                                   @RequestParam(defaultValue = "") String searchWords) {
        PostEditRequestDTO postPage = PostEditRequestDTO.toDTO(postFindService.findById(postId));
        model.addAttribute("editDTO", postPage);
        searchDefault(model, searchType, searchWords);
        return "admin/noticeEditForm";
    }

    //게시글 수정
    @PostMapping("/update/{postId}")
    public String editPost(@Validated @ModelAttribute PostEditRequestDTO updateParam, BindingResult bindingResult,
                           @PathVariable Long postId, RedirectAttributes redirectAttributes, HttpServletRequest request,
                           @RequestParam(defaultValue = "") String searchType,
                           @RequestParam(defaultValue = "") String searchWords) {
        try {
            if (bindingResult.hasErrors()) {
                return "admin/noticeEditForm";
            }
            adminService.updateNotice(postId, updateParam);
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("tagCategory", "Tag.Required", new String[]{e.getMessage()},e.toString());
            e.getStackTrace();
        } catch (AdminTagAccessDenied e) {
            bindingResult.rejectValue("tagListString", "Tag.Denied", new String[]{e.getMessage()},e.toString());
            e.getStackTrace();
        }
        if (bindingResult.hasErrors()) {
            return "admin/noticeEditForm";
        }
        redirectAttributes.addAttribute("updateStatus", true);
        redirectAttributes.addAttribute("page", request.getSession().getAttribute("pageNumber"));
        redirectAttributes.addAttribute("searchType", searchType);
        redirectAttributes.addAttribute("searchWords", searchWords);

        return "redirect:/post/read/{postId}?page={page}&searchType={searchType}&searchWords={searchWords}";
    }

    private static void searchDefault(Model model, String searchType, String searchWords) {
        if (searchType != null && searchWords != null) {
            model.addAttribute("searchType", searchType);
            model.addAttribute("searchWords", searchWords);
        } else {
            model.addAttribute("searchType", "");
            model.addAttribute("searchWords", "");
        }
    }

}
