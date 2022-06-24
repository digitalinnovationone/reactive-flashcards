package br.com.dio.reactiveflashcards.domain.service;

import br.com.dio.reactiveflashcards.domain.dto.MailMessageDTO;
import br.com.dio.reactiveflashcards.domain.helper.RetryHelper;
import br.com.dio.reactiveflashcards.domain.mapper.MailMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import reactor.core.publisher.Mono;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Service
public class MailService {

    private final RetryHelper retryHelper;
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final MailMapper mailMapper;
    private final String sender;

    public MailService(final RetryHelper retryHelper,
                       final JavaMailSender mailSender,
                       final TemplateEngine templateEngine,
                       final MailMapper mailMapper,
                       @Value("${reactive-flashcards.mail.sender}") final String sender) {
        this.retryHelper = retryHelper;
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.mailMapper = mailMapper;
        this.sender = sender;
    }

    public Mono<Void> send(final MailMessageDTO mailMessage){
        return Mono.just(mailSender.createMimeMessage())
                .flatMap(mimeMessage -> buildMessage(mimeMessage, mailMessage))
                .flatMap(this::send)
                .then();
    }

    private Mono<MimeMessage> buildMessage(final MimeMessage mimeMessage, final MailMessageDTO mailMessage) {
        return Mono.fromCallable(() ->{
            var helper = new MimeMessageHelper(mimeMessage, StandardCharsets.UTF_8.name());
            mailMapper.toMimeMessageHelper(helper, mailMessage, sender,
                    buildTemplate(mailMessage.template(), mailMessage.variables()));
            return mimeMessage;
        });
    }

    private String buildTemplate(final String template, final Map<String, Object> variables){
        var context = new Context(new Locale("pt", "BR"));
        context.setVariables(variables);
        return templateEngine.process(template ,context);
    }

    private Mono<Void> send(final MimeMessage mimeMessage){
        return Mono.fromCallable(() ->{
            mailSender.send(mimeMessage);
            return mimeMessage;
        }).retryWhen(retryHelper.processRetry(UUID.randomUUID().toString(),throwable -> throwable instanceof MailException))
                .then();
    }

}
