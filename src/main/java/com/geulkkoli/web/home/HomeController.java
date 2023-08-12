package com.geulkkoli.web.home;

import com.geulkkoli.application.EmailService;
import com.geulkkoli.application.user.service.PasswordService;
import com.geulkkoli.domain.user.User;
import com.geulkkoli.domain.user.service.UserFindService;
import com.geulkkoli.domain.user.service.UserService;
import com.geulkkoli.web.home.dto.find.FindEmailDTO;
import com.geulkkoli.web.home.dto.find.FindPasswordDTO;
import com.geulkkoli.web.home.dto.find.FoundEmailDTO;
import com.geulkkoli.web.home.dto.EmailCheckForJoinDTO;
import com.geulkkoli.web.home.dto.JoinDTO;
import com.geulkkoli.web.home.dto.LoginDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/")
public class HomeController {
    private final String FIND_EMAIL_FORM = "find/email";
    private final String FOUND_EMAIL_FORM = "find/email-lookup";
    private final String FIND_PASSWORD_FORM = "find/password";
    private final String TEMP_PASSWORD_FORM = "find/temp-password";
    private final String SIGN_UP_FORM = "form-signup";
    public static final String REDIRECT_INDEX = "redirect:/";


    private final EmailService emailService;
    private final UserFindService userFindService;
    private final UserService userService;
    private final PasswordService passwordService;


    @GetMapping
    public String renderingMainPage() {
        return "home";
    }

    @GetMapping("/loginPage")
    public String renderingLoginPage(@ModelAttribute("loginForm") LoginDTO form) {

        return "login";
    }

    @PostMapping("/loginPage")
    public String processLoginForm(@ModelAttribute("loginForm") LoginDTO form) {

        return "login"; // 실패 메시지를 포함한 GET 요청으로 리다이렉트
    }

    @GetMapping("/find/email")
    public String renderingFindEmailPage(@ModelAttribute("findEmailForm") FindEmailDTO form) {
        return FIND_EMAIL_FORM;
    }

    @PostMapping("/find/email")
    public String findEmail(@Validated @ModelAttribute("findEmailForm") FindEmailDTO form, BindingResult bindingResult, Model model) {
        Optional<User> user = userFindService.findByUserNameAndPhoneNo(form.getUserName(), form.getPhoneNo());

        if (user.isEmpty()) {
            bindingResult.addError(new ObjectError("empty", "Check.findContent"));
            return FIND_EMAIL_FORM;
        }

        if (bindingResult.hasErrors()) {
            return FIND_EMAIL_FORM;
        }

        FoundEmailDTO foundEmail = new FoundEmailDTO(user.get().getEmail());
        model.addAttribute("email", foundEmail.getEmail());
        return FOUND_EMAIL_FORM;

    }

    @GetMapping("/foundEmail")
    public String renderingFoundEmailPage(@ModelAttribute("foundEmailForm") FoundEmailDTO form) {
        return FOUND_EMAIL_FORM;
    }

    @GetMapping("/find/password")
    public String renderingFindEmailPage(@ModelAttribute("findPasswordForm") FindPasswordDTO form) {
        return FIND_PASSWORD_FORM;
    }

    @PostMapping("/find/password")
    public String findPassword(@Validated @ModelAttribute("findPasswordForm") FindPasswordDTO form, BindingResult bindingResult, HttpServletRequest request) {
        Optional<User> user = userFindService.findByEmailAndUserNameAndPhoneNo(form.getEmail(), form.getUserName(), form.getPhoneNo());

        if (user.isEmpty()) {
            bindingResult.addError(new ObjectError("empty", "Check.findContent"));
        }

        if (bindingResult.hasErrors()) {
            return FIND_PASSWORD_FORM;
        }
        request.getSession().setAttribute("email", user.get().getEmail());
        return "forward:/tempPassword"; // post로 감
    }

    @PostMapping("/tempPassword")
    public String renderingTempPasswordPage() {
        return TEMP_PASSWORD_FORM;
    }

    @GetMapping("/tempPassword")
    public String sendTempPassword(HttpServletRequest request, Model model) {
        String email = (String) request.getSession().getAttribute("email");
        Optional<User> findByUser = userFindService.findByEmail(email);
        if (findByUser.isEmpty()) {
            return REDIRECT_INDEX;
        }
        int length = passwordService.setLength(8, 20);
        String tempPassword = passwordService.createTempPassword(length);
        userService.updatePassword(findByUser.get().getUserId(), tempPassword);
        emailService.sendTempPasswordEmail(email, tempPassword);
        log.info("email 발송");

        model.addAttribute("waitMailMessage", true);
        return TEMP_PASSWORD_FORM;
    }

    //join
    @GetMapping("/join")
    public String renderingJoinPage(@ModelAttribute("joinForm") JoinDTO form) {
        return SIGN_UP_FORM;
    }

    @PostMapping("/join")
    public String join(@Validated @ModelAttribute("joinForm") JoinDTO form, BindingResult bindingResult, HttpServletRequest request) {
        if (userFindService.isNickNameDuplicate(form.getNickName())) {
            bindingResult.rejectValue("nickName", "Duple.nickName");
        }

        if (userFindService.isPhoneNoDuplicate(form.getPhoneNo())) {
            bindingResult.rejectValue("phoneNo", "Duple.phoneNo");
        }

        String authenticationEmail = (String) request.getSession().getAttribute("authenticationEmail");
        String authenticationNumber = (String) request.getSession().getAttribute("authenticationNumber");
        if (authenticationEmail.isEmpty() || authenticationNumber.isEmpty()) {
            bindingResult.rejectValue("email", "Authentication.email");
        }

        // 인증된 이메일 수정 후 인증 안 된 상태로 가입 시도할 경우
        if (!form.getEmail().equals(authenticationEmail)) {
            bindingResult.rejectValue("email", "Authentication.email");
        }

        if (!form.getPassword().equals(form.getVerifyPassword())) {
            bindingResult.rejectValue("verifyPassword", "Check.verifyPassword");
        }

        if (bindingResult.hasErrors()) {
            return SIGN_UP_FORM;
        }
        userService.signUp(form);

        return REDIRECT_INDEX;
    }

    @PostMapping("/checkEmail")
    @ResponseBody
    public ResponseMessage checkEmail(@RequestBody EmailCheckForJoinDTO form, HttpServletRequest request) {

        if (form.getEmail().isEmpty()) {
            return ResponseMessage.NULL_OR_BLANK_EMAIL;
        }
        if (userFindService.isEmailDuplicate(form.getEmail())) {
            return ResponseMessage.EMAIL_DUPLICATION;
        }
        int length = 6;
        String authenticationNumber = passwordService.authenticationNumber(length);
        request.getSession().setAttribute("authenticationNumber", authenticationNumber);
        emailService.sendAuthenticationNumberEmail(form.getEmail(), authenticationNumber);
        log.info("email 발송");

        return ResponseMessage.SEND_AUTHENTICATION_NUMBER_SUCCESS;
    }

    @PostMapping("/checkAuthenticationNumber")
    @ResponseBody
    public String checkAuthenticationNumber(@RequestBody EmailCheckForJoinDTO form, HttpServletRequest request) {

        String authenticationNumber = (String) request.getSession().getAttribute("authenticationNumber");
        String responseMessage;

        if (!form.getAuthenticationNumber().trim().equals(authenticationNumber)) {
            responseMessage = "wrong";
        } else {
            request.getSession().setAttribute("authenticationEmail", form.getEmail());
            responseMessage = "right";
        }

        return responseMessage;
    }
}

