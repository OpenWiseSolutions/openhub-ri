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

package org.openhubframework.openhub.ri.in.exchange;

import java.util.Map;

import org.apache.camel.Handler;
import org.apache.camel.Headers;
import org.apache.camel.LoggingLevel;
import org.springframework.util.Assert;

import org.openhubframework.openhub.api.route.AbstractBasicRoute;
import org.openhubframework.openhub.api.route.CamelConfiguration;
import org.openhubframework.openhub.ri.ServiceEnum;
import org.openhubframework.openhub.ri.out.exchange.ExchangeRateRequest;
import org.openhubframework.openhub.ri.out.exchange.GetExchangeRateRoute;


/**
 * Route definition that defines REST interface at the URI "exchange/v1" with the following operations:
 * <ul>
 *     <li>{@code /latest} - Gets last exchange rate
 *     Input parameters are:
 *     <ul>
 *         <li>source - source currency symbol, e.g. "EUR" (<a href="http://www.xe.com/iso4217.php">see</a> for possible codes)
 *         <li>target - target currency symbol, e.g. "CZK"
 *     </ul>
 *     Output: latest rate as number (Double)
 * </ul>
 *
 * @author Petr Juza
 * @since 1.0.0
 */
@CamelConfiguration(value = ExchangeRestInRoute.ROUTE_BEAN)
public class ExchangeRestInRoute extends AbstractBasicRoute {

    static final String ROUTE_BEAN = "exchangeRestRouteBean";

    private static final String OPERATION_NAME = "getCurrentExchangeRateRest";

    private static final String ROUTE_ID = getRouteId(ServiceEnum.EXCHANGE_RATE, OPERATION_NAME);

    private static final String SOURCE_PARAM = "source";
    private static final String TARGET_PARAM = "target";

    @Override
    protected void doConfigure() throws Exception {
        // there is servlet defined in OHF with prefix /http/
        restConfiguration().component("servlet");

        rest("exchange/v1").description("Get requests rest service")

                // gets last exchange rate
                .get("/latest")
                    .description("Gets last exchange rate.")
                    .route().routeId(ROUTE_ID).endRest()
                    .consumes("application/json").produces("text/plain")

                    .route()
                    .validate(header(SOURCE_PARAM).isNotNull())
                    .validate(header(TARGET_PARAM).isNotNull())

                    .log(LoggingLevel.DEBUG,
                            "Gets latest rate from ${header." + SOURCE_PARAM + "} to ${header." + TARGET_PARAM + "}")

                    .bean(this, "createExchangeRateRequest")

                    // calls OUT route for calling external service
                    .to(GetExchangeRateRoute.URI_GET_CURRENT_EXCHANGE)

                    .log(LoggingLevel.DEBUG, "Latest rate: ${body}")
                .endRest();
    }

    @Handler
    public ExchangeRateRequest createExchangeRateRequest(@Headers Map<String, Object> headers) {
        Assert.notNull(headers, "headers must not be null");

        return new ExchangeRateRequest((String) headers.get(SOURCE_PARAM), (String) headers.get(TARGET_PARAM));
    }
}