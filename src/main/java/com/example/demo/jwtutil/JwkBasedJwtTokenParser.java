package com.example.demo.jwtutil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.example.demo.exception.JwtAuthenticationException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTClaimsSetTransformer;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import com.nimbusds.jwt.proc.JWTClaimsSetVerifier;


public class JwkBasedJwtTokenParser<C extends SecurityContext> implements JwtTokenParser {

	
	private final ConfigurableJWTProcessor<C> jwtProcessor;
	
	
	private final JWTClaimsSetTransformer<User> jwtTransformer;
	
	 protected JwkBasedJwtTokenParser(ConfigurableJWTProcessor<C> processor, 
	            JWTClaimsSetTransformer<User> transformer) {
	        this.jwtProcessor = processor;
	        this.jwtTransformer = transformer;
	    }
	
	
	public User parseToken(String token, C c) throws ParseException, BadJOSEException, JOSEException {
        JWTClaimsSet claims = jwtProcessor.process(token, c);
        return jwtTransformer.transform(claims);
    }
	
	@Override
	public User parseToken(String jwToken) {
		 try {
	            return parseToken(jwToken, null);
	        } catch (ParseException | BadJOSEException | JOSEException ex) {
	           
	            throw new JwtAuthenticationException(ex.getMessage());
	        }
	}
	
    public static JwkBasedJwtTokenParser newHS384FileJwkTokenParser(File file, 
            JWTClaimsSetTransformer<User> transformer, JWTClaimsSetVerifier claimsVerifier) {
        return new JwtTokenParserBuilder<>()
                .withFileJWKSetSource(file)
                .withJwsAlgorithm(JWSAlgorithm.HS384)
                .withJwtClaimsVerifier(claimsVerifier)
                .withJwtTransformer(transformer)
                .build();
    }
    
    public static JwkBasedJwtTokenParser newRSA256RemoteJwkTokenParser(URL url, 
            JWTClaimsSetTransformer<User> transformer, JWTClaimsSetVerifier claimsVerifier ) {
        return new JwtTokenParserBuilder<>()
                .withRemoteJWKSetSource(url)
                .withJwsAlgorithm(JWSAlgorithm.RS256)
                .withJwtTransformer(transformer)
                .withJwtClaimsVerifier(claimsVerifier)
                .build();
    }
    
    public static JwkBasedJwtTokenParser newRSA256FileJwkTokenParser(File file, 
            JWTClaimsSetTransformer<User> transformer ) {
        return new JwtTokenParserBuilder<>()
                .withFileJWKSetSource(file)
                .withJwsAlgorithm(JWSAlgorithm.RS256)
                .withJwtTransformer(transformer)
                .build();
    }
    
    public static <C extends SecurityContext, B extends JwtTokenParserBuilder<JwkBasedJwtTokenParser<C>,B,C>> JwtTokenParserBuilder<JwkBasedJwtTokenParser<C>, B, C> newJwtTokenParser() {
        return new JwtTokenParserBuilder<>();
    }
    
    public static class JwtTokenParserBuilder<T extends JwkBasedJwtTokenParser<C>, B extends JwtTokenParserBuilder<T,B,C>, C extends SecurityContext> {
        // Set up a JWT processor to parse the tokens and then check their signature
        // and validity time window (bounded by the "iat", "nbf" and "exp" claims)
        private ConfigurableJWTProcessor jwtProcessor = new DefaultJWTProcessor();
        private JWKSource keySource;
        private JWSAlgorithm expectedJWSAlg;
        private JWTClaimsSetVerifier claimsVerifier;
        private JWTClaimsSetTransformer<User> transformer;

        protected JwtTokenParserBuilder() {        
        }
        
        protected final B getThis() {
            return (B) this;
        }

        protected B withConfigurableJwtProcessor(ConfigurableJWTProcessor processor) {
            this.jwtProcessor = processor;
            return getThis();
        }

        // The public RSA keys to validate the signatures will be sourced from the
        // OAuth 2.0 server's JWK set, published at a well-known URL OR from a File 
        // or From a String. 
        protected B withJWKSource(JWKSource source) {
            this.keySource = source;
            return getThis();
        }

        public B withStringJWK(String s) {
            try {
                JWK key = JWK.parse(s);
                return withJWKSource(new ImmutableJWKSet(new JWKSet(key)));
            } catch (ParseException ex) {
                
            }
            return getThis();
        }
        
        public B withStringJWKSetSource(String s) {
            try {
                JWKSet keySet = JWKSet.parse(s);
                return withJWKSource(new ImmutableJWKSet(keySet));
            } catch (ParseException ex) {
               
            }
            return getThis();
        }
        
        public B withFileJWKSetSource(File file) {
            try {
                JWKSet keySet = JWKSet.load(file);
                return withJWKSource(new ImmutableJWKSet(keySet));
            } catch (IOException | ParseException ex) {
               
            }
            return getThis();
        }

        public B withFileJWKSetSource(Path path) {
            return withFileJWKSetSource(path.toFile());
        }
        
        // The RemoteJWKSet object caches the retrieved keys to speed up subsequent
        // look-ups and can also gracefully handle key-rollover
        public B withRemoteJWKSetSource(URL url) {
            return withJWKSource(new RemoteJWKSet(url));
        }

        // The expected JWS algorithm of the access tokens (agreed out-of-band)
        public B withJwsAlgorithm(JWSAlgorithm algorithm) {
            this.expectedJWSAlg = algorithm;
            return getThis();
        }

        public <C extends SecurityContext> B withJwtClaimsVerifier(JWTClaimsSetVerifier<C> verifier) {
            this.claimsVerifier = verifier;
            return getThis();
        }

        public B withJwtTransformer(JWTClaimsSetTransformer<User> transformer) {
            this.transformer = transformer;
            return getThis();
        }

        public JwkBasedJwtTokenParser build() {
            Assert.notNull(this.keySource, "Key (JWK) Source can not be NULL, please mention key source from file, URL, or just plain String ");
            Assert.notNull(this.expectedJWSAlg, "JWS Algorithm can not be NULL, please mention signing algorithm RS256, HS384 etc");
            Assert.notNull(this.transformer, "Transformer can not be NULL, please mention a transformer to create UserDetails(User) from JwtClaimsSet");

            // Configure the JWT processor with a key selector to feed matching public
            // (RSA or Other) keys sourced from the JWK set URL
            JWSKeySelector keySelector = new JWSVerificationKeySelector(expectedJWSAlg, keySource);
            jwtProcessor.setJWSKeySelector(keySelector);
            if (this.claimsVerifier != null) {
                jwtProcessor.setJWTClaimsSetVerifier(this.claimsVerifier);
            }
            return new JwkBasedJwtTokenParser(jwtProcessor, transformer);
        }
    }
    

}
