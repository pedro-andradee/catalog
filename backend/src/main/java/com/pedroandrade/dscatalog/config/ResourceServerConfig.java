package com.pedroandrade.dscatalog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private Environment env; //É o ambiente de execução da aplicação

    @Autowired
    private JwtTokenStore tokenStore;

    private static final String[] PUBLIC = {"/oauth/token", "/h2-console/**"};

    private static final String[] OPERATOR_OR_ADMIN = {"/products/**", "/categories/**"};

    private static final String[] ADMIN = {"/users/**"};

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        //É nesse método que o tokenStore é configurado. Com essa configuração o resource server,
        //vai ser capaz de decodificar o token e analisar se os dados desse token estão batendo com
        //o app credentials, se o token ta expirado, etc, ou seja, checar a validade do token.
        resources.tokenStore(tokenStore);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        //H2
        if(Arrays.asList(env.getActiveProfiles()).contains("test")) {
            //Se eu estou rodando um perfil de teste, eu quero liberar o endpoint do H2
            http.headers().frameOptions().disable();
        }

        //Nesse método você configura qual tipo de usuario pode acessar os endpoints especificos
        http.authorizeRequests()
                .antMatchers(PUBLIC).permitAll()
                .antMatchers(HttpMethod.GET, OPERATOR_OR_ADMIN).permitAll()
                .antMatchers(OPERATOR_OR_ADMIN).hasAnyRole("OPERATOR", "ADMIN")
                .antMatchers(ADMIN).hasRole("ADMIN")
                .anyRequest().authenticated();//Significa que quem quiser acessar outra rota, tem que estar autenticado
                                                //Logado.
    }
}
