package com.bytter.scf.security;

import com.bytter.scf.security.handler.CustomAuthenticationFailureHandler;
import com.bytter.scf.security.handler.CustomAuthenticationSuccessHandler;
import com.bytter.scf.security.handler.CustomLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;


@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

//    @Autowired
//    private CustomLogoutSuccessHandler customLogoutSuccessHandler;

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        String finalPassword = passwordEncoder().encode("123456");
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("admin").password(finalPassword).roles("USER").authorities("ADMIN").build());
        manager.createUser(User.withUsername("user1").password(finalPassword).roles("USER").authorities("USER").build());
        manager.createUser(User.withUsername("user2").password(finalPassword).roles("USER").authorities("USER").build());
        return manager;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }


    /**
     * BCrypt  加密
     *
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * 这一步的配置是必不可少的，否则SpringBoot会自动配置一个AuthenticationManager,覆盖掉内存中的用户
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/webjars/**", "/static/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 关闭csrf保护功能（跨域访问）
        // @formatter:off
        http
                .headers()
                    .frameOptions().disable()
                .and()
                    .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/webjars/**").permitAll()
                    .antMatchers("/static/**").permitAll()
                    .antMatchers("/oauth/**").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/auth/login")
                    .loginProcessingUrl("/auth/loginHandler")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .successHandler(customAuthenticationSuccessHandler)
                    .failureHandler(customAuthenticationFailureHandler)
                    .permitAll()
                .and()
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/auth/login")
//                    .logoutSuccessHandler(customLogoutSuccessHandler)
                    .permitAll();
        // @formatter:on
        http.authenticationProvider(authenticationProvider());
    }
}
