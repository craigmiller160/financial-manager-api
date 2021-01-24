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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.JWKSet;
import io.craigmiller160.apitestprocessor.ApiTestProcessor;
import io.craigmiller160.apitestprocessor.config.AuthType;
import io.craigmiller160.apitestprocessor.result.ApiResult;
import io.craigmiller160.financialmanager.testutils.JwtUtils;
import io.craigmiller160.oauth2.config.OAuthConfig;
import io.craigmiller160.webutils.dto.ErrorResponse;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.apache.catalina.filters.RestCsrfPreventionFilter;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
public class AbstractControllerIntegrationTest {

    private static KeyPair keyPair;
    private static JWKSet jwkSet;

    @BeforeAll
    public static void keySetup() throws Exception {
        keyPair = JwtUtils.createKeyPair();
        jwkSet = JwtUtils.createJwkSet(keyPair);
    }

    @MockBean
    private OAuthConfig oAuthConfig;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FilterRegistrationBean<RestCsrfPreventionFilter> csrfFilter;

    private String token;
    protected ApiTestProcessor apiTestProcessor;

    @BeforeEach
    public void oauthSetup() throws Exception {
        when(oAuthConfig.getJwkSet())
                .thenReturn(jwkSet);
        when(oAuthConfig.getClientKey())
                .thenReturn(JwtUtils.CLIENT_KEY);
        when(oAuthConfig.getClientName())
                .thenReturn(JwtUtils.CLIENT_NAME);

        final var jwt = JwtUtils.createJwt();
        token = JwtUtils.signAndSerializeJwt(jwt, keyPair.getPrivate());

        apiTestProcessor = new ApiTestProcessor(setupConfig -> {
            setupConfig.setMockMvc(mockMvc);
            setupConfig.setObjectMapper(objectMapper);
            setupConfig.auth(authConfig -> {
                authConfig.setType(AuthType.BEARER);
                authConfig.setBearerToken(token);
            });
        });
    }

    protected void validateError(final ApiResult result, final HttpStatus status, final String message) {
        assertThat(result.convert(ErrorResponse.class), allOf(
                hasProperty("timestamp", notNullValue()),
                hasProperty("status", equalTo(status.value())),
                hasProperty("error", equalTo(status.getReasonPhrase())),
                hasProperty("message", containsString(message)),
                hasProperty("path", equalTo(result.getRequestConfig().getPath())),
                hasProperty("method", equalTo(result.getRequestConfig().getMethod().toString()))
        ));
    }

    protected String loadCsv(final String fileName) {
        return Option.of(getClass().getClassLoader().getResourceAsStream(String.format("csv/%s", fileName)))
                .flatMap(stream ->
                        Try.of(() -> IOUtils.toString(stream, StandardCharsets.UTF_8))
                                .toOption()
                )
                .getOrElseThrow(() -> new RuntimeException("Could not load CSV file"));
    }

}
