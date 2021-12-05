package com.example.demo;

import java.util.Date;

import com.example.demo.dto.ImageAuthentication;
import com.example.demo.jwtutil.JwtClaimsSetCreator;
import com.nimbusds.jwt.JWTClaimsSet;

public class ImageJwtTokenCreator implements JwtClaimsSetCreator<ImageAuthentication>{

	

	@Override
	public JWTClaimsSet createsClaimSet(ImageAuthentication user) {

		
		return new JWTClaimsSet.Builder().subject(user.getImageName())
				
				// .issuer("https://c2id.com")
				.expirationTime(new Date(new Date().getTime() + 60 * 60 * 1000 * 8))
				
				// .expirationTime(new Date(new Date().getTime() + 60 * 60 * 1000 * 24 * 7))
				.claim("Image Name", user.getImageName()).claim("Image Url", user.getImageurl()).
				
				claim(" Image Id", user.getImageId()).
				
				build();
	}

}
