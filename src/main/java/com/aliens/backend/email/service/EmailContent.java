package com.aliens.backend.email.service;

import org.springframework.stereotype.Component;

@Component
public class EmailContent {

    public String getAuthenticationMailTitle() {
        return "[FriendShip] Account Verification 회원가입 이메일 인증";
    }

    public String getAuthenticationMailContent(String token, String domainUrl) {
        String englishContent = "Thank you for registering with FriendShip App.\n"
                + "To ensure the security of your account, we need to verify your email address.\n\n"
                + "Please click the link below to verify your email and activate your account:\n"
                + "{0}email/confirm?token={1}\n\n"
                + "If you are unable to click the link,\n"
                + "you can copy and paste the following URL into your browser\n\n"
                + "If you did not sign up for an account with FriendShip App, please ignore this email.\n"
                + "Your information will not be used for any unauthorized purposes.\n\n"
                + "Thank you for choosing FriendShip App.\n"
                + "If you have any questions or concerns, please contact our support team at this email.\n\n"
                + "Best regards,\n"
                + "The 4Aliens Team\n\n\n\n\n";

        String koreanContent = "안녕하세요!\n\n"
                + "FriendShip App에 가입해 주셔서 감사합니다.\n"
                + "서비스를 사용하시기 위해서는 계정의 보안을 위해 이메일 주소를 인증해야 합니다.\n\n"
                + "아래 링크를 클릭하여 이메일 주소를 인증하고 계정을 활성화해 주세요:\n"
                + "{0}email/confirm?token={1}\n\n"
                + "링크를 클릭할 수 없는 경우 다음 URL을 브라우저에 복사하여 붙여넣으셔도 됩니다.\n\n"
                + "만약 FriendShip App에 계정을 만든 적이 없는 경우,\n"
                + "이 이메일을 무시해 주세요. 귀하의 정보는 무단으로 사용되지 않습니다.\n\n"
                + "FriendShip App을 선택해 주셔서 감사합니다.\n"
                + "궁금한 점이나 문의사항이 있으시면 해당 이메일로 문의해 주세요.\n\n"
                + "감사합니다,\n"
                + "4Aliens 팀 올림\n";

        return englishContent + koreanContent;
    }

    public String getTemporaryMailTitle() {
        return "[FriendShip] 임시 비밀번호 발급";
    }

    public String getTemporaryMailContent(final String tmpPassword) {
        String englishContent = "The temporary password has been issued. Your temporary password is: " + tmpPassword;

        String koreanContent = "임시 비밀번호가 발급되었습니다. 임시 비밀번호는 다음과 같습니다: " + tmpPassword;

        return englishContent + "\n\n" + koreanContent;
    }
}
