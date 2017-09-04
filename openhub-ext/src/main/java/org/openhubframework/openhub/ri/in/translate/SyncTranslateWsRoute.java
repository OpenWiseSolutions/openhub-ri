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

package org.openhubframework.openhub.ri.in.translate;

import static org.openhubframework.openhub.api.common.jaxb.JaxbDataFormatHelper.jaxb;

import javax.xml.namespace.QName;

import org.apache.camel.Body;
import org.apache.camel.Handler;
import org.apache.camel.LoggingLevel;
import org.springframework.util.Assert;

import org.openhubframework.openhub.api.route.AbstractBasicRoute;
import org.openhubframework.openhub.api.route.CamelConfiguration;
import org.openhubframework.openhub.api.route.RouteConstants;
import org.openhubframework.openhub.ri.ServiceEnum;
import org.openhubframework.openhub.ri.in.translate.model.SyncTranslateRequest;
import org.openhubframework.openhub.ri.in.translate.model.SyncTranslateResponse;


/**
 * Route definition for synchronous operation "translate" via web services.
 *
 * @author Petr Juza
 * @since 1.0.0
 */
@CamelConfiguration(value = SyncTranslateWsRoute.ROUTE_BEAN)
public class SyncTranslateWsRoute extends AbstractBasicRoute {

    static final String ROUTE_BEAN = "syncTranslateWsRouteBean";

    private static final String OPERATION_NAME = "syncTranslateWs";

    private static final String ROUTE_ID = getRouteId(ServiceEnum.TRANSLATE, OPERATION_NAME);


    @Override
    protected void doConfigure() throws Exception {
        from(getInWsUri(new QName(TranslateWebServiceConfig.TRANSLATE_SERVICE_NS, "syncTranslateRequest")))
                .routeId(ROUTE_ID)

                .policy(RouteConstants.WS_AUTH_POLICY)

                .to("throttling:sync:" + OPERATION_NAME)

                .unmarshal(jaxb(SyncTranslateRequest.class))

                .log(LoggingLevel.DEBUG, "Calling translate service with input text: ${body.inputText}")

                // note: we selected Google API before we found out its limitations
                //  (you have to get API_KEY connected with your account, the Google Cloud Translation API is only
                //  available as a paid service - https://cloud.google.com/translate/faq)
                //  => we added custom function for translation for testing purposes only
                .transform().body(SyncTranslateRequest.class, this::translate)

                .marshal(jaxb(SyncTranslateResponse.class));
    }

    @Handler
    public SyncTranslateResponse translate(@Body SyncTranslateRequest req) {
        Assert.notNull(req, "req must not be null");

        SyncTranslateResponse res = new SyncTranslateResponse();
        res.setOutputText("translated output text");
        return res;
    }
}
