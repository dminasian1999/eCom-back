package dev.ecomback.security;

import dev.ecomback.accounting.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.web.bind.annotation.GetMapping;

@Configuration
@RequiredArgsConstructor
public class AuthorizationConfiguration {

    @Bean
    public SecurityFilterChain web(HttpSecurity http) throws Exception {
        http.httpBasic(Customizer.withDefaults());
        http.cors(Customizer.withDefaults());
        http.csrf(AbstractHttpConfigurer::disable); // Disable CSRF for simplicity in APIs
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)); // Always create sessions
        http.authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers(HttpMethod.POST, "/users/login")
//                        .permitAll()
                        // Public Endpoints
                        .requestMatchers("/users/register", "/posts").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/recovery/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/recovery/**").permitAll()
                        // Administrator-Only Endpoints
                        .requestMatchers(HttpMethod.GET, "/users").permitAll()
                        .requestMatchers("/users/{username}/roles/{role}").hasRole(Role.ADMINISTRATOR.name())
                        .requestMatchers(HttpMethod.PUT, "/users/{username}/wishList/{productId}")
                        .access(new WebExpressionAuthorizationManager("#username == authentication.name"))
                        .requestMatchers(HttpMethod.DELETE, "/users/{username}/wishList/{productId}")
                        .access(new WebExpressionAuthorizationManager("#username == authentication.name"))


                        .requestMatchers(HttpMethod.PUT, "/{username}/cartList/{productId}/update/{isAdd}")
                        .access(new WebExpressionAuthorizationManager("#username == authentication.name"))
//                .requestMatchers(HttpMethod.DELETE,"/users/{username}/cartList/{productId}")
//                .access(new WebExpressionAuthorizationManager("#username == authentication.name"))

                        .requestMatchers(HttpMethod.PUT, "/users/{username}/cartList")
                        .access(new WebExpressionAuthorizationManager("#username == authentication.name"))
                        .requestMatchers(HttpMethod.DELETE, "/users/{username}/cartList")
                        .access(new WebExpressionAuthorizationManager("#username == authentication.name"))

                        .requestMatchers(HttpMethod.POST, "/users/address/{login}")
                        .access(new WebExpressionAuthorizationManager("#login == authentication.name"))
                        .requestMatchers("/posts/wishList").permitAll()


                        // Delete User (User or Admin)
                        .requestMatchers(HttpMethod.DELETE, "/users/{login}")
                        .access(new WebExpressionAuthorizationManager(
                                "#login == authentication.name or hasRole('ADMINISTRATOR')"))

                        // Post-Specific Endpoints (Role-Based)
                        .requestMatchers(HttpMethod.POST, "/post/{author}")
                        .hasRole(Role.MODERATOR.name())
                        .requestMatchers(HttpMethod.POST, "/post/file/upload")
                        .hasRole(Role.MODERATOR.name())
                        .requestMatchers("/post/file/delete/{file}")
                        .hasRole(Role.MODERATOR.name())
                        .requestMatchers(HttpMethod.DELETE, "/post/{id}")
                        .hasRole(Role.MODERATOR.name())
                        .requestMatchers(HttpMethod.PUT, "/post/{id}")
                        .permitAll()
                        // View Post (Public)
                        .requestMatchers(HttpMethod.GET, "/posts/category/{category}").permitAll()

                        .requestMatchers(HttpMethod.GET, "/post/{id}").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/post/search/{field}/{asc}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/posts/criteria/{criteria}/sort/{sort}/asc/{asc}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/posts/type/{criteria}/sort/{sort}/asc/{asc}").permitAll()

                        .requestMatchers(HttpMethod.POST, "/posts/search").permitAll()
                        .requestMatchers(HttpMethod.GET, "/posts/receipts").permitAll()
                        // User-Specific Endpoints
                        .requestMatchers("/posts/{author}")
                        .access(new WebExpressionAuthorizationManager("#author == authentication.name"))

                        // Comment Endpoints (User-Specific and Role-Based)
                        .requestMatchers(HttpMethod.PUT, "/post/{id}/comment/{author}")
                        .access(new WebExpressionAuthorizationManager("#author == authentication.name"))
                        .requestMatchers(HttpMethod.DELETE, "/post/{id}/comment")
                        .hasRole(Role.MODERATOR.name())

                        // Catch-All
                        .anyRequest().authenticated()
        );

        return http.build();
    }
}
