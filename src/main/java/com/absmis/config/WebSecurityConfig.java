package com.absmis.config;

import com.absmis.domain.message.Result;
import com.absmis.exception.ErrorInfo;
import com.absmis.security.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * spring security的配置
 * 配置类：说明登录方式、登录页面、哪个url需要认证、注入登录失败/成功过滤器
 */
@Configuration
//通过 @EnableWebMvcSecurity禁用Boot的默认Security配合，配合@Configuration启用自定义的Spring Security的功能
@EnableWebSecurity
//启用Security注解
@EnableGlobalMethodSecurity(prePostEnabled = true)//允许进入页面方法前检验
@ComponentScan(basePackageClasses = CustomUserDetailsService.class)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    //自定义验证
    @Bean
    public MyAuthenticationProvider myAuthenticationProvider() {
        return new MyAuthenticationProvider();
    }
    //自定义用户服务
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordencoder());
    }
    @Bean(name = "passwordEncoder")
    public PasswordEncoder passwordencoder() {
        return new BCryptPasswordEncoder(4);
    }

    /**
     * 继承 WebSecurityConfigurerAdapter ，并重写它的方法来设置一些web安全的细节
     * configure(HttpSecurity http) 方法
     * 通过 authorizeRequests() 定义哪些URL需要被保护、哪些不需要被保护。
     * 代码指定了 / login 不需要任何认证就可以访问，其他的路径都必须通过身份验证。
     * 以/mgmt为前缀的Actuator端点只有admin才能查看
     * 通过 formLogin() 定义当需要用户登录时候，转到的登录页面。
     * .csrf().disable()
     * 为了防止跨站提交攻击，spring security默认开启了csrf保护。那么post方式提交表单的时候就必须验证Token，如果没有就会
     * 出现403
     */
    //Request层面的配置对应xml中的<http>元素
    //任何人都可以访问login
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()//关闭跨站请求防护
                .authorizeRequests()//请求授权
                .antMatchers("/toLogin").permitAll() //不需要权限认证的url
                .antMatchers("/mgmt/**").access("hasRole('ADMIN')")
                .anyRequest()//任何请求
                .authenticated()//需要身份认证
                .and()
                .formLogin()
                .loginProcessingUrl("/toLogin")
//                .loginPage("/login")
                .successForwardUrl("/index")

                //登录失败转发
//                .failureForwardUrl("/login?error=true")
                .usernameParameter("username").passwordParameter("password")
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request,
                                                        HttpServletResponse response,
                                                        Authentication authentication) throws IOException, ServletException {
                        ErrorInfo eInfo = new ErrorInfo(200,"success to login");
                        ObjectMapper om = new ObjectMapper();
                        PrintWriter out = response.getWriter();
                        out.write(om.writeValueAsString(eInfo));
                        out.flush();
                        out.close();
                    }
                })
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request,
                                                        HttpServletResponse response,
                                                        AuthenticationException exception)
                            throws IOException, ServletException {

                       ErrorInfo eInfo = new ErrorInfo(100,"failure to login");
                        ObjectMapper om = new ObjectMapper();
                        PrintWriter out = response.getWriter();
                        out.write(om.writeValueAsString(eInfo));
                        out.flush();
                        out.close();
                    }
                })
                .permitAll()
                .and()
                //默认登出成功后重定向到login 路径下，
                // 默认的登出handler是SecurityContextLogoutHandler
                .logout()
                .logoutSuccessUrl("/login")
                //登出的时候同时注销HttpSession默认为true
                .invalidateHttpSession(true)
                .permitAll();
    }

    //配置spring Security的Filter链
    //不过滤图片等静态资源其中**代表可以跨越目录，*不可以跨越目录
    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        web.ignoring().antMatchers(
                "/static/**",  "/kaptcha/**","/**/*.png", "/**/*.jpeg",
                "/**/*.css", "/**/*.js", "/**/*.jpg", "/**/*.gif","/**/*.json");
    }

    /**
     * configureGlobal(AuthenticationManagerBuilder auth)方法，
     * 静态：
     * 在内存中创建了一个用户，该用户的名称为admin，密码为s3cr3t，用户角色为ADMIN,READER。
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("admin").password("s3cr3t").roles("ADMIN", "READER");
        //动态：
        //身份验证配置，用于注入自定义身份验证Bean和密码校验规则，
        //将验证过程交给自定义验证工具
        auth.authenticationProvider(myAuthenticationProvider());
    }
}
