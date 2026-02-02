package pet.adoption.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.ignoringRequestMatchers("/api/**", "/h2-console/**", "/ui/auth/**"))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/", "/api/auth/**", "/ui/auth/**", "/ui", "/ui/", "/ui/home", "/h2-console", "/h2-console/**").permitAll()
            .requestMatchers("/css/**", "/js/**", "/images/**", "/uploads/**", "/favicon.ico").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/post/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/pet/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/ui/post").permitAll()
            .requestMatchers(HttpMethod.GET, "/ui/pet/**").permitAll()
            .anyRequest().authenticated())
            .formLogin(form -> form
              .loginPage("/ui/auth/login")
              .loginProcessingUrl("/login")
              .defaultSuccessUrl("/ui/home", true)
              .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/ui/auth/logout")
                .logoutSuccessUrl("/ui/home")
            )
            .headers(headers -> headers.frameOptions(FrameOptionsConfig::sameOrigin));
    return http.build();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider(CustomUserDetailsService userDetailsService,
      PasswordEncoder encoder) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
    provider.setPasswordEncoder(encoder);
    return provider;
  }

  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http,
      PasswordEncoder passwordEncoder,
      CustomUserDetailsService userDetailsService) throws Exception {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder);

    AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
    authBuilder.authenticationProvider(provider);
    return authBuilder.build();
  }
}
