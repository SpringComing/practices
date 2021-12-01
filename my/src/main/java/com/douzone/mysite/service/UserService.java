package com.douzone.mysite.service;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.douzone.mysite.repository.UserRepository;
import com.douzone.mysite.vo.UserVo;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	public void join(UserVo vo) {
		userRepository.insert(vo);
	}

	public UserVo getUser(String email) {
		return userRepository.findByEmail(email);
	}
	
	public UserVo getUser(Long no) {
		return userRepository.findByNo(no);
	}

	public UserVo getUser(String email, String password) {
		return userRepository.findByEmailAndPassword(email, password);
	}

	public void updateUser(UserVo userVo) {
		userRepository.update(userVo);
	}
	
	public void javaxMail() {
        
        String subject = "Hello JavaMail API Test"; 
        String fromMail = "wjn135@gmail.com"; 
        String fromName = "전지은"; 
        String toMail = "wjn135@naver.com"; // 콤마(,) 나열 가능 
        
        String _email = "보내는 아이디";
        String _password = "보내는 비번";
        
        // mail contents 
        StringBuffer contents = new StringBuffer(); 
        contents.append("<h1>Hello</h1>\n"); 
        contents.append("<p>Nice to meet you ~! :)</p><br>"); 
        
        // mail properties 
        Properties props = new Properties(); 
        props.put("mail.smtp.host", "smtp.gmail.com"); // use Gmail 
        props.put("mail.smtp.port", "587"); // set port 
        
        props.put("mail.smtp.auth", "true"); 
        props.put("mail.smtp.starttls.enable", "true"); // use TLS 
        
        Session mailSession = Session.getInstance(props, 
        	new javax.mail.Authenticator() { // set authenticator 
        		protected PasswordAuthentication getPasswordAuthentication() { 
        			return new PasswordAuthentication(_email, _password); 
        		} 
        	}
        ); 
        
        try { 
        	MimeMessage message = new MimeMessage(mailSession); 
        	
        	message.setFrom(new InternetAddress(fromMail, MimeUtility.encodeText(fromName, "UTF-8", "B"))); // 한글의 경우 encoding 필요 
        	message.setRecipients( 
        		Message.RecipientType.TO, 
        		InternetAddress.parse(toMail) 
        	); 
        	message.setSubject(subject); 
        	message.setContent(contents.toString(), "text/html;charset=UTF-8"); // 내용 설정 (HTML 형식) 
        	message.setSentDate(new java.util.Date()); 
        	
        	Transport t = mailSession.getTransport("smtp"); 
        	t.connect(_email, _password); 
        	t.sendMessage(message, message.getAllRecipients()); 
        	t.close(); 
        	
        	System.out.println("Done Done ~!"); 
        } catch (Exception e) { 
        	e.printStackTrace(); 
        }
 
    }
}