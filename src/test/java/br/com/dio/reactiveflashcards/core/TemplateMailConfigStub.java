package br.com.dio.reactiveflashcards.core;

import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;

import static lombok.AccessLevel.PRIVATE;
import static org.thymeleaf.templatemode.TemplateMode.HTML;

@NoArgsConstructor(access = PRIVATE)
public class TemplateMailConfigStub {

    public static SpringResourceTemplateResolver templateResolver(final ApplicationContext applicationContext){
        var templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(HTML);
        templateResolver.setApplicationContext(applicationContext);
        return templateResolver;
    }

    public static SpringTemplateEngine templateEngine(final ApplicationContext applicationContext){
        var templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver(applicationContext));
        templateEngine.setEnableSpringELCompiler(true);
        templateEngine.addDialect(new Java8TimeDialect());
        return templateEngine;
    }

}
