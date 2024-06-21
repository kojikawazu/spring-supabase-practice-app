package com.example.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * Thyemeleaf設定
 * @since 2024/06/20
 * @author koji kawazu
 * 
 * Thymeleafの設定を行います。
 * ThymeleafはHTMLテンプレートエンジン。
 */
@Configuration
public class ThymeleafConfig {

	/**
     * テンプレートリゾルバのBeanを定義
     * テンプレートリゾルバは、テンプレートファイルを解決するための設定。
     * @return SpringResourceTemplateResolverのインスタンス
     */
    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setPrefix("classpath:/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }

    /**
     * テンプレートエンジンのBeanを定義
     * テンプレートエンジンは、テンプレートを処理するためのエンジン。
     * @return SpringTemplateEngineのインスタンス
     */
    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        return templateEngine;
    }

    /**
     * ビューレゾルバのBeanを定義
     * ビューレゾルバは、ビュー（HTMLページなど）を解決するための設定を持つ。
     * @return ThymeleafViewResolverのインスタンス
     */
    @Bean
    public ThymeleafViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setCharacterEncoding("UTF-8");
        return viewResolver;
    }
}
