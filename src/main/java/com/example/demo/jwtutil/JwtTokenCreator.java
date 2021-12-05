package com.example.demo.jwtutil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;

import org.springframework.util.Assert;

import com.example.demo.exception.JwtAuthenticationException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyType;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;


public class JwtTokenCreator<U> {


    private final JwtClaimsSetCreator<U> claimsSetCreator;
    private final JWK key;
    
    protected JwtTokenCreator(JwtClaimsSetCreator<U> claimsSetCreator, JWK key) {
        this.claimsSetCreator = claimsSetCreator;
        this.key = key;
    }
    
    public String createSignedJwtToken(U userProfile) {

        try {
            JWTClaimsSet claimsSet = claimsSetCreator.createsClaimSet(userProfile);
            JWSAlgorithm jwsAlg = new JWSAlgorithm(key.getAlgorithm().getName());
            JWSSigner signer = getSigner(key);
            SignedJWT signedJWT = new SignedJWT(new JWSHeader(jwsAlg), claimsSet);

            // Apply the HMAC/RSA/EC protection
            signedJWT.sign(signer);

            // Serialize to compact form, produces something like
            // eyJhbGciOiJIUzI1NiJ9.SGVsbG8sIHdvcmxkIQ.onO9Ihudz3WkiauDO2Uhyuz0Y18UASXlSc1eS0NkWyA
            return signedJWT.serialize();

        } catch (JOSEException ex) {
          ex.printStackTrace();
            throw new JwtAuthenticationException("Could not create the JWT " + ex.getMessage());
        }
    }
    
    public static <U, B extends JwtTokenCreator.JwtTokenCreatorBuilder<JwtTokenCreator<U>, B, U>> JwtTokenCreator.JwtTokenCreatorBuilder<JwtTokenCreator<U>, B, U> newJwtTokenCreator( Class<U> claz) {
        return new JwtTokenCreator.JwtTokenCreatorBuilder<>();
    }
    
    
    protected JWSSigner getSigner(final JWK key) throws JOSEException {
        if (KeyType.RSA.equals(key.getKeyType())) {
            return new RSASSASigner((RSAKey) key);
        } else if (KeyType.EC.equals(key.getKeyType())) {
            return new ECDSASigner((ECKey) key);
        } else if (KeyType.OCT.equals(key.getKeyType())) {
            return new MACSigner((OctetSequenceKey) key);
        } else {
            throw new JwtAuthenticationException("Token Creation Exception, Cant find Signer for " + key.getKeyType());
        }
    }
    
    public static class JwtTokenCreatorBuilder<C extends JwtTokenCreator<U>, B extends JwtTokenCreatorBuilder<C, B, U>, U> {

        private JWK key;
        private JwtClaimsSetCreator<U> claimsSetCreator;

        protected JwtTokenCreatorBuilder() {
        }

        protected final B getThis() {
            return (B) this;
        }

        public B withJWKSetFile(File file, String kid) {
            try {
                JWKSet keySet = JWKSet.load(file);
                key = keySet.getKeyByKeyId(kid);
            } catch (IOException | ParseException ex) {
              
            }
            return getThis();
        }

        public B withJWKSetFilePath(Path path, String kid) {
            return withJWKSetFile(path.toFile(), kid);
        }

        public B withJWKSetString(String jwkSetString, String kid) {
            try {
                JWKSet keySet = JWKSet.parse(jwkSetString);
                key = keySet.getKeyByKeyId(kid);
            } catch ( ParseException ex) {

            }
            return getThis();
        }
        
        public B withJWK(String jwkString) {
            try {
                key = JWK.parse(jwkString);
            } catch (ParseException ex) {
                
            }
            return getThis();
        }

        public B withClaimsSetCreator(JwtClaimsSetCreator<U> claimsSetCreator) {
            this.claimsSetCreator = claimsSetCreator;
            return getThis();
        }

        public JwtTokenCreator<U> build() {
            Assert.notNull(this.key, "Key(JWK) can not NULL, please mention File or String");
            Assert.notNull(this.claimsSetCreator, "ClaimsSetCreator can not be NULL, Claims need to be created from User's profile");
            Assert.isTrue(this.key.isPrivate(), "Key does not seem to contain the private key information, please provide a private key to sign JWT");

            return new JwtTokenCreator<>(this.claimsSetCreator, this.key);
        }
    }
    
}
