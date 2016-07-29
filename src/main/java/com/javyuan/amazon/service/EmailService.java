package com.javyuan.amazon.service;

import java.util.Date;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {
	
	private static final Log log = LogFactory.getLog(EmailService.class);
	
	private static final String FROM = "yuanjifeng1@sohu.com";
	private static final String REPLY_TO = "yuanjifeng1@sohu.com";

	@Autowired
    private JavaMailSender javaMailSender;
	
	@Autowired
    private TemplateEngine htmlTemplateEngine;
	
	/**
	 * 发送普通文本邮件
	 */
	public void sendTextMail(String to, String subject, String text) throws MessagingException{
		MimeMessage mail = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail, false);
		helper.setFrom(FROM);
		helper.setReplyTo(REPLY_TO);
		helper.setSentDate(new Date());
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(text);
		// 不知道什么原因，不sleep无法成功发送邮件
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		javaMailSender.send(mail);
		log.debug("send textEmail success :" + to);
	}
	
	/**
	 * 发送HTML邮件
	 */
	public void sendHtmlMail(String to, String subject, String template, Map<String, Object> model) throws MessagingException {
		// Prepare the evaluation context
        Context ctx = new Context();
        ctx.setVariables(model);
        // Prepare message using a Spring helper
        MimeMessage mail = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail, "UTF-8");
        helper.setSubject(subject);
        helper.setFrom(FROM);
		helper.setReplyTo(REPLY_TO);
		helper.setTo(to);
        // Create the HTML body using Thymeleaf
        String htmlContent = htmlTemplateEngine.process(template, ctx);
        helper.setText(htmlContent, true);
        // Send email
        javaMailSender.send(mail);
        log.debug("send htmlEmail success :" + to);
	}
}
