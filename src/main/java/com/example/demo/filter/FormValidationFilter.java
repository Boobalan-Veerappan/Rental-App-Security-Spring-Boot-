package com.example.demo.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.example.demo.exception.JwtAuthenticationException;

import org.springframework.lang.Nullable;


public class FormValidationFilter extends AbstractAuthenticationProcessingFilter{
	
	public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";
	
	
	private String usernameParameter = SPRING_SECURITY_FORM_USERNAME_KEY;
	
	public FormValidationFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
		
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		// TODO Auto-generated method stub
	//	System.err.println(" request "+request.get);
		
		String authHeader = request.getHeader("Authorization");
		
		 if (authHeader == null || !authHeader.startsWith("Bearer")) {
	            logger.error("Missing token in header " + authHeader);
	            throw new JwtAuthenticationException(" excep ");
	        }
		 
		 String tokenStr = null;
	        if (authHeader.length() > 7) { //"Bearer "
	            tokenStr = authHeader.substring(7).trim();
	        }
		
		FormAuthentication auth= new FormAuthentication(tokenStr);
		return getAuthenticationManager().authenticate(auth);
	}

	 @Override
	    public void successfulAuthentication(HttpServletRequest req, HttpServletResponse resp, FilterChain chain, Authentication authResult) throws IOException, ServletException {
	        
	        super.successfulAuthentication(req, resp, chain, authResult);

	        chain.doFilter(req, resp);
	    }
	 
	 
	 @Nullable
		protected String obtainUsername(HttpServletRequest request) {
			return request.getParameter(this.usernameParameter);
		}
	 
	 @Override
	protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		 boolean retVal =  super.requiresAuthentication(request, response);
		 return retVal;
	}
}
