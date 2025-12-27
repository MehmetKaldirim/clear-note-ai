

package com.mathdenizi.notes.security;

import com.mathdenizi.notes.config.OAuth2LoginSuccessHandler;
import com.mathdenizi.notes.models.AppRole;
import com.mathdenizi.notes.models.Role;
import com.mathdenizi.notes.models.User;
import com.mathdenizi.notes.repositories.RoleRepository;
import com.mathdenizi.notes.repositories.UserRepository;
import com.mathdenizi.notes.security.jwt.AuthEntryPointJwt;
import com.mathdenizi.notes.security.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import java.time.LocalDate;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Created by mathdenizi
 * Date: 23.06.25
*/
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig1 {

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

   @Autowired
   @Lazy
   OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }



    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(withDefaults()); // EKLENDİ: CORS desteği için

                //.csrf(csrf ->
                //csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                //        .ignoringRequestMatchers("/api/auth/public/**", "/api/ai/**","/api/notes/**")
        //);
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests((requests)
                -> requests
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // EKLENDİ: Preflight istekleri için
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/csrf-token").permitAll()
                        .requestMatchers("/api/auth/public/**").permitAll()
                        .requestMatchers("/oauth2/**").permitAll()
                        .requestMatchers("/api/ai/**").permitAll()
                        .requestMatchers("/api/notes/**").permitAll()

                        .anyRequest().authenticated())
                .oauth2Login(oauth2 -> {
                    oauth2.successHandler(oAuth2LoginSuccessHandler);
                 });
        http.exceptionHandling(exception
                -> exception.authenticationEntryPoint(unauthorizedHandler));
        http.addFilterBefore(authenticationJwtTokenFilter(),
                UsernamePasswordAuthenticationFilter.class);
        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository,
                                      UserRepository userRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_USER)));

            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_ADMIN)));

            if (!userRepository.existsByUserName("user1")) {
                User user1 = new User("user1", "user1@example.com",
                        passwordEncoder.encode("password1"));
                user1.setAccountNonLocked(false);
                user1.setAccountNonExpired(true);
                user1.setCredentialsNonExpired(true);
                user1.setEnabled(true);
                user1.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
                user1.setAccountExpiryDate(LocalDate.now().plusYears(1));
                user1.setTwoFactorEnabled(false);
                user1.setSignUpMethod("email");
                user1.setRole(userRole);
                userRepository.save(user1);
            }

            if (!userRepository.existsByUserName("admin")) {
                User admin = new User("admin", "admin@example.com",
                        passwordEncoder.encode("adminPass"));
                admin.setAccountNonLocked(true);
                admin.setAccountNonExpired(true);
                admin.setCredentialsNonExpired(true);
                admin.setEnabled(true);
                admin.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
                admin.setAccountExpiryDate(LocalDate.now().plusYears(1));
                admin.setTwoFactorEnabled(false);
                admin.setSignUpMethod("email");
                admin.setRole(adminRole);
                userRepository.save(admin);
            }
        };
    }
}