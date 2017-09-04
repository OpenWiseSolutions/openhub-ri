/*
 * Copyright 2002-2016 the original author or authors.
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

package org.openhubframework.openhub.ri.out.exchange;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import com.jayway.jsonpath.PathNotFoundException;
import org.apache.camel.*;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import org.openhubframework.openhub.api.exception.IntegrationException;
import org.openhubframework.openhub.api.exception.InternalErrorEnum;
import org.openhubframework.openhub.ri.AbstractRITest;
import org.openhubframework.openhub.ri.in.translate.SyncTranslateWsRoute;
import org.openhubframework.openhub.test.route.ActiveRoutes;


/**
 * Test suite for {@link SyncTranslateWsRoute}.
 *
 * @author Petr Juza
 * @since 1.0.0
 */
@ActiveRoutes(classes = GetExchangeRateRoute.class)
public class GetExchangeRateRouteTest extends AbstractRITest {

    @Produce
    private ProducerTemplate producer;

    @EndpointInject(uri = "mock:test")
    private MockEndpoint mock;

    private void prepareRoute(final String body) throws Exception {
        getCamelContext().getRouteDefinition(GetExchangeRateRoute.ROUTE_ID)
                .adviceWith(getCamelContext(), new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() throws Exception {
                        weaveAddLast().to(mock);
                        weaveById("idFixerIOExternalCall").replace().process(
                                (Processor) exchange -> exchange.getOut().setBody(body, String.class));
                    }
                });
        mock.expectedMessageCount(1);
    }

    @Test
    public void testGettingExchange() throws Exception {
        // replace external system by mock
        final String EXCHANGE_RES = "{"
                + "    \"base\": \"EUR\","
                + "    \"date\": \"2017-09-01\","
                + "    \"rates\": {"
                + "        \"CZK\": 26.077"
                + "    }"
                + "}";

        prepareRoute(EXCHANGE_RES);

        // call route
        ExchangeRateRequest req = new ExchangeRateRequest("EUR", "CZK");
        Double targetRate = producer.requestBody(GetExchangeRateRoute.URI_GET_CURRENT_EXCHANGE, req, Double.class);

        // verification
        mock.assertIsSatisfied();
        assertThat(targetRate, notNullValue());
        assertThat(targetRate, is(Double.valueOf("26.077")));
    }

    @Test
    public void testGettingExchangeWithWrongTargetCurrencyCode() throws Exception {
        // replace external system by mock
        final String EXCHANGE_RES = "{"
                + "    \"base\": \"EUR\","
                + "    \"date\": \"2017-09-01\","
                + "    \"rates\": {"
                + "        \"BAM\": 26.077"
                + "    }"
                + "}";

        prepareRoute(EXCHANGE_RES);

        // call route + verification
        try {
            ExchangeRateRequest req = new ExchangeRateRequest("EUR", "CZK");
            producer.requestBody(GetExchangeRateRoute.URI_GET_CURRENT_EXCHANGE, req, Double.class);
        } catch (CamelExecutionException ex) {
            assertThat(ex.getCause() instanceof IntegrationException, CoreMatchers.is(true));
            IntegrationException inEx = (IntegrationException) ex.getCause();
            assertThat(inEx.getError().getErrorCode(), is(InternalErrorEnum.E100.getErrorCode()));
            assertThat(inEx.getCause(), instanceOf(PathNotFoundException.class));
        }
    }
}

