package br.com.dio.reactiveflashcards.core.extension.mail;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Properties;

@Slf4j
public class MailServerExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

    private GreenMail smtpServer;
    private int port = 80;

    private final String user = "teste@teste.com.br";
    private final String password = "123456";

    @Override
    public void beforeEach(final ExtensionContext context) throws Exception {
        var field = Arrays.stream(context.getRequiredTestClass().getDeclaredFields())
                        .filter(f -> f.isAnnotationPresent(SMTPPort.class)).findFirst();
        var testInstance = context.getTestInstance();
        if (field.isPresent() && testInstance.isPresent()){
            var fieldName = field.get().getName();
            port = ((Integer) ReflectionTestUtils.getField(testInstance.get(), fieldName));
        }
        log.info("==== starting mail server in port {}", port);
        smtpServer = new GreenMail(new ServerSetup(port, null, "smtp"));
        smtpServer.setUser(user, password);
        smtpServer.start();
    }

    @Override
    public void afterEach(final ExtensionContext context) throws Exception {
        smtpServer.stop();
        log.info("==== stopping mail server");
    }

    @Override
    public boolean supportsParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().isAnnotationPresent(MailServer.class) ||
                parameterContext.getParameter().isAnnotationPresent(MailSender.class);
    }

    @Override
    public Object resolveParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext) throws ParameterResolutionException {
        if (parameterContext.getParameter().isAnnotationPresent(MailServer.class)){
            return smtpServer;
        } else if (parameterContext.getParameter().isAnnotationPresent(MailSender.class)) {
            return createSender();
        }else {
            return null;
        }
    }

    private JavaMailSenderImpl createSender() {
        var sender = new JavaMailSenderImpl();
        sender.setHost(smtpServer.getSmtp().getServerSetup().getBindAddress());
        sender.setPort(port);
        var mailProps = new Properties();
        mailProps.setProperty("mail.transport.protocol","smtp");
        mailProps.setProperty("mail.smtp.auth","true");
        mailProps.setProperty("mail.smtp.starttls.enable","true");
        mailProps.setProperty("mail.debug","false");
        sender.setJavaMailProperties(mailProps);
        sender.setUsername(user);
        sender.setPassword(password);
        return sender;
    }

}
