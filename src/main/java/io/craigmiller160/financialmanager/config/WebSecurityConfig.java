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

package io.craigmiller160.financialmanager.config;

import io.craigmiller160.oauth2.security.JwtValidationFilterConfigurer;
import io.craigmiller160.webutils.security.AuthEntryPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true
)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtValidationFilterConfigurer jwtValidationFilterConfigurer;
    private final AuthEntryPoint authEntryPoint;
    private final boolean useSsl;

    public WebSecurityConfig(final JwtValidationFilterConfigurer jwtValidationFilterConfigurer,
                             final AuthEntryPoint authEntryPoint,
                             @Value("${server.ssl.use-ssl}") final boolean useSsl) {
        this.jwtValidationFilterConfigurer = jwtValidationFilterConfigurer;
        this.authEntryPoint = authEntryPoint;
        this.useSsl = useSsl;
    }

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(jwtValidationFilterConfigurer.getInsecurePathPatterns()).permitAll()
                .anyRequest().fullyAuthenticated()
                .and()
                .apply(jwtValidationFilterConfigurer)
                .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .and()
                .exceptionHandling().authenticationEntryPoint(authEntryPoint);

        if (useSsl) {
            http.requiresChannel().anyRequest().requiresSecure();
        }
    }

}
