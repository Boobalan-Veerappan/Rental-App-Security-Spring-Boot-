package com.example.demo.jwtutil;

import com.nimbusds.jwt.JWTClaimsSet;

public interface JwtClaimsSetCreator<T> {
	 public JWTClaimsSet createsClaimSet(T user);
}
