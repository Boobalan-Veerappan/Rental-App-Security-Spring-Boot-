package com.example.demo.config;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;

import com.example.demo.domain.UserDetailsDomain;
import com.example.demo.filter.FormAuthProvider;
import com.example.demo.filter.FormValidationFilter;
import com.example.demo.jwtutil.JwkBasedJwtTokenParser;
import com.example.demo.jwtutil.JwtTokenCreator;
import com.example.demo.jwtutil.JwtTokenParser;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jwt.JWTClaimsSet;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityWebConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	public UserDetailsManager manager;

	@Autowired
	public FormAuthProvider provider;

	@Autowired
	private WebCORSConfiguration webCORSConfiguration;

	private static final String rsaKeySet = "{\n" + "  \"keys\": [\n" + "    {\n" + "      \"kty\": \"RSA\",\n"
			+ "      \"d\": \"MA-5szmlYH6S9GlNHbnwEZUnq8HxRmXV91d4-LweMqNkcu_qWuGIufzdeCy7FJ2c4JiTfb7q1NK5i5_WjOgVn6r1bHOSkPjJZCX1vU0ICyHkys-WS5A_J8DEkIWLbgQlhGkPNCuVXMA8qSDZteK7DsgVEbjARO7w-h-dQJGyvow6xkF4Sj0dlTY1necQGuov2TrjGB9Un73BNhEAqdyUA51JJhoiHARZvGSGEdpXVlZVdbVALA2_CeIGTc1siL2FyIUDG2XQqg3V_45OfC8oGQyK0m1Ul-JquKr9l5ql4Ao_qNQpDeVYDmsAIBPAUWOrGGOBXuH-h90E_lg4TAMhQQ\",\n"
			+ "      \"e\": \"AQAB\",\n" + "      \"use\": \"sig\",\n" + "      \"kid\": \"test-app-key\",\n"
			+ "      \"alg\": \"RS256\",\n"
			+ "      \"n\": \"iPjC5NRpjwc1KCsfHYM0Luc2BkCI4y31Xt0jDAj7qrL_lKS6nBMZSZSWvm68jYW9GMZKb0LvKjs0v8JlOUtT2KR1o-yS_3Vciuy9YCuFMIOg2Sys5gXofQm2AaBlCFKCCERCaDrikdoTm7EgF3jRqWFtyQa_AXXvcmcfks1RW641XlEinOhawcfN5dnsU1qKo7jVrCIgGskqDWym_2sN4S2MriBvCyyY6hPqDWYngGXryrpawNs7hv1R2MMfYd8DW9A-n7ysvZ9RW6F9NwhTGyd_ReiLO4OkzU18zO0HW7MHVNU4NMNeQpznuqU7LrbYNzKHhkcTtTzq23vlsJSWzw\"\n"
			+ "    }\n" + "  ]\n" + "}";
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication().withUser("boobalan").password("boobalan").roles("admin");
//		
//		
//		UserDetails u1 = User.withUsername("boobalan1").password("boobalan").roles("developer").build();
//		
//	
//		
//		manager.createUser(u1);
//		
		auth.authenticationProvider(provider);

//		auth.authenticationProvider(provider);

	}

	@Bean
	public FormValidationFilter authenticationTokenFilterBean() throws Exception {
		FormValidationFilter filter = new FormValidationFilter("/api/**");
		filter.setAuthenticationManager(authenticationManagerBean());
		// filter.setAuthenticationSuccessHandler(new
		// JwtAuthenticationSuccessHandler());
		return filter;
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	protected void configure(HttpSecurity http) throws Exception {
//		this.logger.debug("Using default configure(HttpSecurity). "
//				+ "If subclassed this will potentially override subclass configure(HttpSecurity).");
//		http.cors();
		http.csrf().disable();
		http.authorizeRequests().antMatchers("/auth/**").permitAll().anyRequest().authenticated();
		http.addFilterBefore(webCORSConfiguration, WebAsyncManagerIntegrationFilter.class);
		http.addFilterAfter(authenticationTokenFilterBean(), LogoutFilter.class);
//		http.formLogin();
//		http.httpBasic();
	}

//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.cors();
//		http.csrf().disable().
//			authorizeRequests().antMatchers("/login","/registration").permitAll()
//			.anyRequest().authenticated();
//	//http.addFilterAfter(authenticationTokenFilterBean(), LogoutFilter.class);
//http.addFilterAt(new DebuggingFilter(), LogoutFilter.class);
//http.formLogin();
//http.httpBasic();
////		http.cors();
////	    http
////	    .sessionManagement()
////	    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
////	    .and()
////	    .csrf().disable()
////	    .authorizeRequests()
////	    .antMatchers(HttpMethod.OPTIONS, "/**").permitAll() //allow CORS option calls
////	    .antMatchers("/resources/**").permitAll()
////	    .anyRequest().authenticated()
////	    .and()
////	    .httpBasic();
//  };
//		http.cors();
//		http.csrf().disable().
//
//		authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/**").permitAll().anyRequest().authenticated()
//		.and().httpBasic();

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

	@Bean
	JwtTokenParser getJwtTokenParser() {
		return JwkBasedJwtTokenParser.newJwtTokenParser().withStringJWKSetSource(rsaKeySet)
				.withJwsAlgorithm(JWSAlgorithm.RS256).withJwtTransformer((JWTClaimsSet jwtcs) -> {
					try {
						String role = jwtcs.getStringClaim("Role");
						String userName = jwtcs.getStringClaim("UserName");
						List<GrantedAuthority> roles = new ArrayList<>();
						roles.add(new SimpleGrantedAuthority(role));
						return new TestAppUser(userName, jwtcs.getSubject(), role, roles);
					} catch (ParseException ex) {
					
						throw new IllegalArgumentException("Bad Token");
					}
				}).build();
	}
	
	@Bean
	JwtTokenCreator getJwtTokenCreator() {
		return JwtTokenCreator.newJwtTokenCreator(UserDetailsDomain.class).withJWKSetString(rsaKeySet, "test-app-key")
				.withClaimsSetCreator((UserDetailsDomain user) -> {
					return new JWTClaimsSet.Builder().subject(user.getUserName())
							// .issuer("https://c2id.com")
							.expirationTime(new Date(new Date().getTime() + 60 * 60 * 1000 * 8))
							//.expirationTime(new Date(new Date().getTime() + 60 * 60 * 1000 * 24 * 7))
							.claim("UserName", user.getUserName()).claim("Role", user.getUserName()).build();
				}).build();
	}

}
