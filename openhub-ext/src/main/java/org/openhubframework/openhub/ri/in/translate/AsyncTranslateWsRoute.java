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

import org.apache.camel.Expression;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.xml.Namespaces;

import org.openhubframework.openhub.api.asynch.AsynchResponseProcessor;
import org.openhubframework.openhub.api.asynch.AsynchRouteBuilder;
import org.openhubframework.openhub.api.asynch.model.CallbackResponse;
import org.openhubframework.openhub.api.route.AbstractBasicRoute;
import org.openhubframework.openhub.api.route.CamelConfiguration;
import org.openhubframework.openhub.api.route.XPathValidator;
import org.openhubframework.openhub.ri.ServiceEnum;
import org.openhubframework.openhub.ri.in.translate.model.AsyncTranslateRequest;
import org.openhubframework.openhub.ri.in.translate.model.AsyncTranslateResponse;


/**
 * Route definition for asynchronous operation "translate" via web services.
 *
 * @author Petr Juza
 * @since 1.0.0
 */
@CamelConfiguration(value = AsyncTranslateWsRoute.ROUTE_BEAN)
public class AsyncTranslateWsRoute extends AbstractBasicRoute {

    static final String ROUTE_BEAN = "asyncTranslateWsRouteBean";

    private static final String OPERATION_NAME = "asyncTranslateWs";

    static final String ROUTE_ID_ASYNC_IN = getInRouteId(ServiceEnum.TRANSLATE, OPERATION_NAME);

    static final String ROUTE_ID_ASYNC_OUT = getOutRouteId(ServiceEnum.TRANSLATE, OPERATION_NAME);

    static final String URI_ASYNC_OUT = "direct:" + ROUTE_ID_ASYNC_OUT;

    @Override
    protected void doConfigure() throws Exception {
        // asyncTranslate - input asynch message
        createAsyncRouteIn();

        // asyncTranslate - process delivery (=asynchronous execution)
        createAsyncRouteOut();
    }

    /**
     * Route for asynchronous <strong>asyncTranslate</strong> input operation.
     * <p/>
     * Prerequisite: none
     * <p/>
     * Output: {@link AsyncTranslateResponse}
     */
    private void createAsyncRouteIn() {
        Namespaces ns = new Namespaces("h", TranslateWebServiceConfig.TRANSLATE_SERVICE_NS);

        // note: mandatory parameters are set already in XSD, this validation is extra
        XPathValidator validator = new XPathValidator("/h:asyncTranslateRequest", ns, "h:inputText");

        // note: only shows using but without any influence in this case
        Expression nameExpr = xpath("/h:asyncTranslateRequest/h:inputLang").namespaces(ns).stringResult();

        AsynchRouteBuilder.newInstance(ServiceEnum.TRANSLATE, OPERATION_NAME,
                getInWsUri(new QName(TranslateWebServiceConfig.TRANSLATE_SERVICE_NS, "asyncTranslateRequest")),
                new AsynchResponseProcessor() {
                    @Override
                    protected Object setCallbackResponse(CallbackResponse callbackResponse) {
                        AsyncTranslateResponse res = new AsyncTranslateResponse();
                        res.setConfirmAsyncTranslate(callbackResponse);
                        return res;
                    }
                }, jaxb(AsyncTranslateResponse.class))

                .withValidator(validator)
                .withObjectIdExpr(nameExpr)
                .build(this);
    }

    /**
     * Route for <strong>asyncTranslate</strong> operation - process delivery (=asynchronous execution).
     * Only input text is logged in this case.
     * <p/>
     * Prerequisite: none
     */
    private void createAsyncRouteOut() {
        final String URI_LOG_INPUT_PARAMS = "direct:logInputParams";

        from(URI_ASYNC_OUT)
                .routeId(ROUTE_ID_ASYNC_OUT)

                // xml -> AsyncTranslateRequest
                .unmarshal(jaxb(AsyncTranslateRequest.class))

                .to("extcall:message:" + URI_LOG_INPUT_PARAMS);


        from(URI_LOG_INPUT_PARAMS)
                .validate(body().isInstanceOf(AsyncTranslateRequest.class))
                .log(LoggingLevel.DEBUG, "Asynchronous execution - input text '${body.inputText}' (lang: ${body.inputLang})");
    }
}
