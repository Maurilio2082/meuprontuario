package br.com.meuprontuario.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Classe de configuração para adicionar interceptadores ao ciclo de requisições do Spring MVC.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private UsuarioInterceptor usuarioInterceptor; // Dependência do interceptador de usuário

    /**
     * Registra os interceptadores personalizados para o projeto.
     * 
     * @param registry Registro de interceptadores.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Adiciona o interceptador de usuário a todas as requisições (/**)
        registry.addInterceptor(usuarioInterceptor).addPathPatterns("/**");
    }
}
