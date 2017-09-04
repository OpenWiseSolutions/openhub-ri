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

import com.jayway.jsonpath.JsonPath;
import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangeProperty;
import org.apache.camel.Handler;
import org.springframework.util.Assert;

import org.openhubframework.openhub.api.route.AbstractBasicRoute;
import org.openhubframework.openhub.api.route.CamelConfiguration;
import org.openhubframework.openhub.ri.ServiceEnum;


/**
 * Route definition of <strong>getCurrentExchange</strong> operation.
 * <p>
 * Route connects to external API for getting current exchange rate:
 * {@code https://api.fixer.io/latest?base=EUR&symbols=CZK}
 * <p>
 * Parameters:
 * <ul>
 *     <li>input: {@link ExchangeRateRequest}
 *     <li>output: current exchange rate (Double)
 * </ul>
 *
 * @author Petr Juza
 * @since 1.0.0
 */
@CamelConfiguration(value = GetExchangeRateRoute.ROUTE_BEAN)
public class GetExchangeRateRoute extends AbstractBasicRoute {

    static final String ROUTE_BEAN = "getExchangeRateRouteBean";

    private static final String OPERATION_NAME = "getCurrentExchange";

    static final String ROUTE_ID = getRouteId(ServiceEnum.EXCHANGE_RATE, OPERATION_NAME);

    static final String URI_GET_CURRENT_EXCHANGE = "direct:" + ROUTE_ID;

    private static final String REQUEST_PROP = "exchangeRateRequest_property";

    @Override
    protected void doConfigure() throws Exception {
        from(URI_GET_CURRENT_EXCHANGE)
                .routeId(ROUTE_ID)

                .validate(body().isInstanceOf(ExchangeRateRequest.class))
                .setProperty(REQUEST_PROP, body())

                // call external service
                .setHeader(Exchange.HTTP_QUERY,
                        simple("base=${body.sourceCurrency}&symbols=${body.targetCurrency}"))
                .setBody(simple(null))
                .to("http4://api.fixer.io/latest")
                    .id("idFixerIOExternalCall")

                .bean(this, "getTargetRate");
    }

    /**
     * Gets target exchange rate.
     *
     * @param body The body in JSON format
     * @param req The {@link ExchangeRateRequest request}
     * @return target rate
     */
    @Handler
    public Double getTargetRate(@Body String body, @ExchangeProperty(REQUEST_PROP) ExchangeRateRequest req) {
        Assert.notNull(body, "body must not be null");
        Assert.notNull(req, "req must not be null");

        // expected response from Fixer.io
        //         {
        //            "base": "EUR",
        //            "date": "2017-09-01",
        //            "rates": {
        //                "CZK": 26.077
        //            }
        //         }
        //JsonPath.parse(body).read("$.rates[*]", List.class)
        String jsonExpr = "$.rates." + req.getTargetCurrency().toUpperCase();
        return JsonPath.parse(body).read(jsonExpr, Double.class);
    }
}
