package com.douzone.mysite.dto;

import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

public class MailSender {
	@Autowired
	JavaMailSenderImpl mail;

	public MailSender() {
		super();
		mail = new JavaMailSenderImpl();
		mail.setHost("smtp.gmail.com"); // 구글 SMTP서버 이름
		mail.setPort(587); // 구글 SMTP서버 포트번호
		mail.setUsername("wjn135@gmail.com"); // 보내는 Gmail 아이디
		mail.setPassword("비번"); // 보내는 Gmail 비밀번호
		mail.setDefaultEncoding("UTF-8"); // 인코딩 정보
		Properties prop = new Properties();
		prop.setProperty("mail.smtp.auth", "true");
		prop.setProperty("mail.smtp.starttls.enable", "true");
		mail.setJavaMailProperties(prop);

	}

	public String getRandNum() {
		int rand = (int) (Math.random() * 899999) + 100000;
		String randNum = rand + "";

		return randNum;
	}

	public String sendVerifiNum(String email) throws Exception {
		
		String num = getRandNum();

		String html = "<table style='width:700px; margin-left:20px;'>"
				+ "<tr>"
				+ "<tr><img src='/logoSpring/logoSpring.png' style='width:700px; height:300px;'</tr>"
				+ "</tr><tr><td><span>안녕하세요. <strong>SpringCome</strong> 입니다.<br>저희 사이트를 방문해 주셔서 감사드립니다.<br><br><strong>"
				+ "</strong> SpringCome에서 보낸 인증번호는 다음과 같습니다.</span></td>" + "</tr>"
				+ "<tr><td><table style='margin-top:20px;'><col width='100px'><col width='200px'><tr style='border:1px solid gray; '>"
				+ "<td style='background-color: #f5f6f5; color:#80878d'>인증번호</td><td>" + num + "</td>"
				+ "</tr></table></td></tr><tr><td><span><br>인증번호 칸에 인증번호를 입력해주세요.</td>" + "</tr></table>";

		MimeMessage msg = mail.createMimeMessage();
		MimeMessageHelper helper;

		helper = new MimeMessageHelper(msg, true);
		helper.setFrom("wjn135@gmail.com"); // 보내는 사람
		helper.setTo(email); // 받는 사람
		helper.setSubject("[SpringCome] " + email + "님 인증번호 입니다."); // 메일 제목
		helper.setText(html, true); // 메일 내용

		mail.send(msg); // 메일 전송
		
		return num;
	}

}
