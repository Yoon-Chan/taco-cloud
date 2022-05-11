package tacos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import tacos.security.NoEncodingPasswordEncoder;
import javax.sql.DataSource;

/*
 *   사용자의 HTTP 요청 경로에 대해 접근 제한과 같은 보안 관련 처리를 우리가 원해는대로 하기위해 작성.
 *
 * */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /*
    *   요청 경로가 보안 처리되는 방법을 정의하는 구성 메서드
    *
    *   access(String)          인자로 전달된 SpEL 표현식이 true면 접근을 허용
    *   anonymous()             익명의 사용자에게 접근을 허용
    *   authenticated()         익명이 아닌 사용자로 인증된 경우 접근을 허용
    *   denyAll()               무조건 접근을 거부
    *   fullyAuthenticated()    익명이 아니거나 또는 remember-me(바로 아래참조) 아닌 사용자로 인증되면 접근을 허용
    *   hasAnyAuthority(String ..)  지정된 권한 중 어떤 것이라도 사용자가 갖고 있으면 접근을 허용
    *   hasAnyRole(String..)        지정된 역할 중 어느 하나라도 사용자가 갖고 있으면 접근을 허용.
    *   hasAuthority(String)        지정된 권한을 사용자가 갖고 있으면 접근을 허용
    *   hasIpAddress(String)        지정된 IP주소로부터 요청이 오면 접근을 허용
    *   hasRole(String)             지정된 역할을 사용자가 갖고 있으면 접근을 허용
    *   not()                       다른 접근 메서드들의 효력을 무효화한다.
    *   permitAll()                 무조건 접근을 허용한다.
    *   rememberMe()                remember-me(이전 로그인 정보를 쿠키나 데이터베이스로 저장한 후 일정
    *                               기간 내에 다시 접근 시 저장된 정보로 자동 로그인됨)를 통해 인증된
    *                               사용자의 접근을 허용.
    *
    *   스프링 시큐리티에서 확장된 SpEL
    *
    *   authentication              해당 사용자의 인증 객체
    *   denyAll                     항상 false를 산출
    *   hasAnyRole(역할 내역)         지정된 역할 중 어느 하나라도 해당 사용자가 갖고 있으면 true
    *   hasRole(역할)                 지정된 역할을 해당 사용자가 갖고 있으면 true
    *   hasIpAddress(IP주소)          지정된 IP주소로부터 해당 요청이 온것이면 true
    *   isAnonymous()                해당 사용자가 익명 사용자이면 true
    *   isAuthenticated()           해당 사용자가 익명이 아닌 사용자로 인증되었으면 true
    *   isFullyAuthenticated()      해당 사용자가 익명이 아니거나 또는 remember-me가 아닌 사용자로 인증되었으면 true
    *   isRememberMe()              해당 사용자가 remember-me 기능으로 인증되었으면 true
    *   permitAll                   항상 true를 산출
    *   principal                   해당 사용자의 principal 객체
    *
    *
    *
    * */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/design", "/orders")
                .access("hasRole('ROLE_USER')")
                .antMatchers("/", "/**").permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .and()
                .logout()
                .logoutSuccessUrl("/")

                .and()
                .csrf();
                /*
                .authorizeRequests()
                .antMatchers("/design", "/orders")
                .access("hasRole('ROLE_USER')")
                .antMatchers("/", "/**").access("permitAll()")
                .and()
                .httpBasic();

                 */
    }

    //AuthenticationManagerBuilder는 인증 명세를 구성하기 위해 빌더 형태의 API를 사용
    //이떄는 inMemoryAuthentication()메서드를 사용하여 보안 구성 자체에 사용자 정보를 직접 지정할 수 있다.

    /*
    //jdbc기반의 사용자 스토어를 사용하기 위함 변수
    @Autowired
    DataSource dataSource;

     */
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(encoder());


        /*
        //ldif 인증 사용
        auth
                .ldapAuthentication()
                .userSearchBase("ou=people")
                .userSearchFilter("(uid={0})")
                .groupSearchBase("ou=groups")
                .groupSearchFilter("member={0}")
                .contextSource()
                .root("dc=tacocloud,dc=com")
                .ldif("classpath:users.ldif")
                .and()
                .passwordCompare()
                .passwordEncoder(new BCryptPasswordEncoder())
                .passwordAttribute("userPasscode");

         */

        /*
        //jdbc기반 사용 방법
        auth
                .jdbcAuthentication()
                .dataSource(dataSource)
                //사용자 정보 쿼리의 커스터마이징
                .usersByUsernameQuery(
                        "select username, password, enabled from users "+
                                "where username = ?"
                ).authoritiesByUsernameQuery(
                        "select username, authority form authorities " +
                                "where username=?"
                )
                //암호화 된 비밀번호 사용
                .passwordEncoder(new NoEncodingPasswordEncoder());

         */
        /*
        // 인메모리 사용자 스토어 사용 방법
        auth.inMemoryAuthentication()
                .withUser("user1")
                .password("{noop}password1")
                .authorities("ROLE_USER")
                .and()
                .withUser(("user2"))
                .password("{noop}password2")
                .authorities("ROLE_USER");

         */
    }
}
