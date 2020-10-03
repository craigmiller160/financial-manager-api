/*
 *     Financial Manager API
 *     Copyright (C) 2020 Craig Miller
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.craigmiller160.financialmanager.testutils;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.val;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class JwtUtils {

    public static final String ROLE_1 = "ROLE_1";
    public static final String ROLE_2 = "ROLE_2";
    public static final String USERNAME = "username";
    public static final String ROLES_CLAIM = "roles";
    public static final String CLIENT_KEY = "clientKey";
    public static final String CLIENT_NAME = "clientName";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String TOKEN_ID = "JWTID";

    public static KeyPair createKeyPair() throws Exception {
        final var keyPairGen = KeyPairGenerator.getInstance("RSA");
        return keyPairGen.generateKeyPair();
    }

    public static JWKSet createJwkSet(final KeyPair keyPair) {
        final var builder = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.RS256)
                .keyID("oauth-jwt");
        return new JWKSet(builder.build());
    }

    public static SignedJWT createJwt() {
        return createJwt(100);
    }

    public static SignedJWT createJwt(final long expMinutes) {

        final var header = new JWSHeader.Builder(JWSAlgorithm.RS256)
                .build();

        final var exp = LocalDateTime.now().plusMinutes(expMinutes);
        final var expDate = Date.from(exp.atZone(ZoneId.systemDefault()).toInstant());

        final var claims = new JWTClaimsSet.Builder()
                .jwtID(TOKEN_ID)
                .issueTime(new Date())
                .subject(USERNAME)
                .expirationTime(expDate)
                .claim(ROLES_CLAIM, List.of(ROLE_1, ROLE_2))
                .claim("clientKey", CLIENT_KEY)
                .claim("clientName", CLIENT_NAME)
                .claim("firstName", FIRST_NAME)
                .claim("lastName", LAST_NAME)
                .build();
        return new SignedJWT(header, claims);
    }

    public static String signAndSerializeJwt(final SignedJWT jwt, final PrivateKey privateKey) throws Exception {
        final var signer = new RSASSASigner(privateKey);
        jwt.sign(signer);
        return jwt.serialize();
    }

}
