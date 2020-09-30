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

package io.craigmiller160.financialmanager.integration;

import com.nimbusds.jose.jwk.JWKSet;
import io.craigmiller160.financialmanager.testutils.JwtUtils;
import io.craigmiller160.oauth2.config.OAuthConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.security.KeyPair;

import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ImportControllerIntegrationTest {

    private static KeyPair keyPair;
    private static JWKSet jwkSet;

    @BeforeAll
    public static void keySetup() throws Exception {
        keyPair = JwtUtils.createKeyPair();
        jwkSet = JwtUtils.createJwkSet(keyPair);
    }

    @MockBean
    private OAuthConfig oAuthConfig;

    private String token;

    @BeforeEach
    public void setup() throws Exception {
        when(oAuthConfig.getJwkSet())
                .thenReturn(jwkSet);
        when(oAuthConfig.getClientKey())
                .thenReturn(JwtUtils.CLIENT_KEY);
        when(oAuthConfig.getClientName())
                .thenReturn(JwtUtils.CLIENT_NAME);

        final var jwt = JwtUtils.createJwt();
        token = JwtUtils.signAndSerializeJwt(jwt, keyPair.getPrivate());
    }

    @Test
    public void test_doImport_chase() {
        throw new RuntimeException();
    }

    @Test
    public void test_doImport_chase_badCsv() {
        throw new RuntimeException();
    }

    @Test
    public void test_doImport_discover() {
        throw new RuntimeException();
    }

    @Test
    public void test_doImport_discover_badCsv() {
        throw new RuntimeException();
    }

    @Test
    public void test_doImport_invalidSource() {
        throw new RuntimeException();
    }

}
