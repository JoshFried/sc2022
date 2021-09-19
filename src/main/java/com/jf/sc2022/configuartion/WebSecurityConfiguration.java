package com.jf.sc2022.configuartion;

import com.jf.sc2022.converter.ImageListingRequestDTOToImageListingConverter;
import com.jf.sc2022.converter.ImageListingToImageListingDTOConverter;
import com.jf.sc2022.converter.PaymentToPaymentDTOConverter;
import com.jf.sc2022.converter.RegistrationDTOToUserConverter;
import com.jf.sc2022.converter.UserToUserDTOConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    private final SCUserDetailsService userDetailsService;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.cors()
            .and()
            .authorizeRequests()
            .antMatchers("/register").permitAll()
            .antMatchers("/login").permitAll()
            .antMatchers("/api").permitAll()
            .and()
            .formLogin().disable()
            .logout()
            .permitAll()
            .logoutUrl("/logout")
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
            .and()
            .csrf().disable();

        // TODO: Add OAuth 2.0 Authorization 8)
        // http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }

    // Beans
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(encoder());
        authProvider.setUserDetailsService(userDetailsService);
        return authProvider;
    }

    @Override
    public void addFormatters(final FormatterRegistry mvcConversionService) {
        mvcConversionService.addConverter(new UserToUserDTOConverter());
        mvcConversionService.addConverter(new RegistrationDTOToUserConverter());
        mvcConversionService.addConverter(new ImageListingRequestDTOToImageListingConverter());
        mvcConversionService.addConverter(new ImageListingToImageListingDTOConverter());
        mvcConversionService.addConverter(new PaymentToPaymentDTOConverter());
    }
}
