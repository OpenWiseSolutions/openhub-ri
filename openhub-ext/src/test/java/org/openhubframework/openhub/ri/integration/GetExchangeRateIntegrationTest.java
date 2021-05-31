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

package org.openhubframework.openhub.ri.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.Test;
import org.openhubframework.openhub.ri.AbstractIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

/**
 * Integration test for GetExchangeRate operation.
 *
 * @author Karel Kovarik
 * @since 1.0.0
 */
@TestPropertySource(properties = {
        "out.exchange.rate.url = ${stub.server.url}/exchange-rate"
})
public class GetExchangeRateIntegrationTest extends AbstractIntegrationTest {

    @Test
    public void test_ok() throws Exception {
        // setup stub server
        stubFor(WireMock.get("/exchange-rate?base=EUR&symbols=CZK")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBodyFile("out/exchange/ok-response.json")));

        // call service
        final ResponseEntity<String> responseEntity = testRestTemplate
                .getForEntity(getWebBaseUrl() + "/http/exchange/v1/latest?source=EUR&target=CZK", String.class);

        // verify response
        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getBody(), is("29.033"));

        // verify stub server was called
        verify(getRequestedFor(urlPathEqualTo("/exchange-rate"))
                .withQueryParam("base", equalTo("EUR"))
                .withQueryParam("symbols", equalTo("CZK")));
    }
}
