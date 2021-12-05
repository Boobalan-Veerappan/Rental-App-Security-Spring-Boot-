package com.example.demo;

import java.util.Date;

import com.example.demo.domain.UserDetailsDomain;
import com.example.demo.jwtutil.JwtClaimsSetCreator;
import com.nimbusds.jwt.JWTClaimsSet;

public class FormJwtTokenCreator implements JwtClaimsSetCreator<UserDetailsDomain> {

	@Override
	public JWTClaimsSet createsClaimSet(UserDetailsDomain user) {

		return new JWTClaimsSet.Builder().subject(user.getUserName())
				// .issuer("https://c2id.com")
				.expirationTime(new Date(new Date().getTime() + 60 * 60 * 1000 * 8))
				// .expirationTime(new Date(new Date().getTime() + 60 * 60 * 1000 * 24 * 7))
				.claim("UserName", user.getUserName()).claim("Role", user.getUserName()).build();
	}

}
