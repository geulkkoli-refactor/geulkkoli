package com.geulkkoli.web.account;

import com.geulkkoli.application.user.CustomAuthenticationPrinciple;
import com.geulkkoli.domain.follow.service.FollowService;
import com.geulkkoli.domain.social.service.SocialInfoFindService;
import com.geulkkoli.domain.social.service.SocialInfoService;
import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.service.UserFindService;
import com.geulkkoli.domain.user.service.UserService;
import com.geulkkoli.web.account.dto.edit.PasswordEditFormDto;
import com.geulkkoli.web.account.dto.edit.UserInfoEditFormDto;
import com.geulkkoli.web.account.dto.ConnectedSocialInfos;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/account")
@Controller
public class AccountManagementController {
    public static final String EDIT_FORM = "/user/userInfo-edit";
    public static final String EDIT_PASSWORD_FORM = "/password-edit";
    public static final String REDIRECT_INDEX = "redirect:/";
    public static final String REDIRECT_EDIT_INDEX = "redirect:/user/edit";

    private final UserService userService;
    private final UserFindService userFindService;
    private final SocialInfoService socialInfoService;
    private final FollowService followService;
    private final SocialInfoFindService socialInfoFindService;


    @GetMapping("/edit")
    public ModelAndView editUserInfo(@AuthenticationPrincipal CustomAuthenticationPrinciple authUser) {
        ModelAndView modelAndView = new ModelAndView(EDIT_FORM);
        ConnectedSocialInfos connectedInfos = socialInfoFindService.findConnectedInfosByEmail(authUser.getUsername());
        UserInfoEditFormDto userInfoEditDto = UserInfoEditFormDto.form(authUser.getUserRealName(), authUser.getNickName(), authUser.getPhoneNo(), authUser.getGender());

        log.info("userInfoEditDto = {}", userInfoEditDto);
        log.info("connectedInfos = {}", connectedInfos);
        log.info("connectedInfos.isKaKao = {}", connectedInfos.isKakaoConnected());
        log.info("connectedInfos.isNaver = {}", connectedInfos.isNaverConnected());
        log.info("connectedInfos.isGoogle = {}", connectedInfos.isGoogleConnected());

        modelAndView.addObject("loggingNickName", authUser.getNickName());
        modelAndView.addObject("editForm", userInfoEditDto);
        modelAndView.addObject("connectedInfos", connectedInfos);

        modelAndView.setViewName(EDIT_FORM);

        return modelAndView;
    }


    @PostMapping("/edit")
    public ModelAndView editUserInfo(@Validated @ModelAttribute("editForm") UserInfoEditFormDto userInfoEditDto, BindingResult bindingResult, @AuthenticationPrincipal CustomAuthenticationPrinciple authUser) {

        // 닉네임 중복 검사 && 본인의 기존 닉네임과 일치해도 중복이라고 안 뜨게
        if (userFindService.isNickNameDuplicate(userInfoEditDto.getNickName()) && !userInfoEditDto.getNickName().equals(authUser.getNickName())) {
            bindingResult.rejectValue("nickName", "Duple.nickName");
        }

        if (userFindService.isPhoneNoDuplicate(userInfoEditDto.getPhoneNo()) && !userInfoEditDto.getPhoneNo().equals(authUser.getPhoneNo())) {
            bindingResult.rejectValue("phoneNo", "Duple.phoneNo");
        }

        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName(REDIRECT_EDIT_INDEX);
            ConnectedSocialInfos connectedInfos = socialInfoFindService.findConnectedInfosByEmail(authUser.getUsername());
            modelAndView.addObject("connectedInfos", connectedInfos);
            modelAndView.addObject("loggingNickName", authUser.getNickName());
            modelAndView.addObject("editForm", userInfoEditDto);
            modelAndView.setViewName(EDIT_FORM);
            return modelAndView;
        }
        userService.edit(parseLong(authUser), userInfoEditDto);
        // 세션에 저장된 authUser의 정보를 수정한다.
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CustomAuthenticationPrinciple newAuth = (CustomAuthenticationPrinciple) principal;
        newAuth.modifyNickName(userInfoEditDto.getNickName());
        newAuth.modifyPhoneNo(userInfoEditDto.getPhoneNo());
        newAuth.modifyGender(userInfoEditDto.getGender());
        newAuth.modifyUserRealName(userInfoEditDto.getUserName());

        modelAndView.setViewName(REDIRECT_INDEX);
        return modelAndView;
    }

    @GetMapping("/edit/edit-password")
    public String editPasswordForm(@ModelAttribute("passwordEditForm") PasswordEditFormDto form) {
        return EDIT_PASSWORD_FORM;
    }

    @PostMapping("/edit/edit-password")
    public ModelAndView editPassword(@Validated @ModelAttribute("passwordEditForm") PasswordEditFormDto form, BindingResult bindingResult, @AuthenticationPrincipal CustomAuthenticationPrinciple authUser, RedirectAttributes redirectAttributes) {
        User user = userFindService.findById(parseLong(authUser));
        if (!userService.isPasswordVerification(user.getPassword(), form.getOldPassword())) {
            bindingResult.rejectValue("oldPassword", "Check.password");
        }

        if (form.isMatched()) {
            bindingResult.rejectValue("verifyPassword", "Check.verifyPassword");
        }
        if (bindingResult.hasErrors()) {
            return new ModelAndView(EDIT_PASSWORD_FORM);
        }
        userService.updatePassword(parseLong(authUser), form.getNewPassword());
        ModelAndView modelAndView = new ModelAndView(REDIRECT_EDIT_INDEX);
        modelAndView.addObject("status", true);
        return modelAndView;
    }

    /**
     * 서비스에서 쓰는 객체의 이름은 User인데 memberDelete라는 이름으로 되어 있어서 통일성을 위해 이름을 고친다.
     * 또한 사용자 입장에서는 자신의 정보를 삭제하는 게 아니라 탈퇴하는 서비스를 쓰고 있으므로 uri를 의미에 더 가깝게 고쳤다.
     */
    @GetMapping("/{nickName}/unsubscribed")
    public String unSubscribe(@PathVariable("nickName") String nickName) {
        try {
            User findUser = userFindService.findByNickName(nickName);
            followService.allUnfollow(findUser);
            socialInfoService.deleteAll(findUser.getEmail());
            userService.delete(findUser);
        } catch (Exception e) {
            log.error("unSubscribe error = {}", e.getMessage());
            return REDIRECT_INDEX;
        }
        return REDIRECT_INDEX;
    }

    private Long parseLong(CustomAuthenticationPrinciple authUser) {
        return Long.valueOf(authUser.getUserId());
    }
}
