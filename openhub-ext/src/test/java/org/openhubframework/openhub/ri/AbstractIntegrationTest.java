/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openhubframework.openhub.ri;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.openhubframework.openhub.ri.AbstractIntegrationTest.STUB_SERVER_PORT;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.openhubframework.openhub.test.TestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;


/**
 * Simple parent for integration tests.
 *
 * @author Karel Kovarik
 * @since 1.0.0
 */
@TestPropertySource(properties = {
        "stub.server.port = " + STUB_SERVER_PORT,
        "stub.server.url = localhost:${stub.server.port}",
        "test.camel.initAllRoutes = true"
})
@SpringBootTest(
        classes = {
                TestConfig.class,
                AbstractIntegrationTest.WebContextConfig.class
        },
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public abstract class AbstractIntegrationTest extends AbstractRITest {

    static final int STUB_SERVER_PORT = 17777;

    @LocalServerPort
    private int port;

    @Autowired
    protected TestRestTemplate testRestTemplate;

    @Value("${stub.server.port}")
    protected int stubServerPort;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig()
            .port(STUB_SERVER_PORT));

    /**
     * Get port of embedded web context.
     *
     * @return the port.
     */
    protected int getPort() {
        return port;
    }

    /**
     * Get embedded web container base url.
     *
     * @return the url.
     */
    protected String getWebBaseUrl() {
        return "http://localhost:" + port;
    }

    /**
     * Configuration class to setup web context.
     */
    @ComponentScan(basePackages = {
            "org.openhubframework.openhub.web",
            "org.openhubframework.openhub.admin"
    })
    public static class WebContextConfig {
    }
}
