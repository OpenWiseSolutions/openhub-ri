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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.camel.EndpointInject;
import org.apache.camel.Message;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;

import org.openhubframework.openhub.api.asynch.AsynchConstants;
import org.openhubframework.openhub.api.asynch.model.ConfirmationTypes;
import org.openhubframework.openhub.common.Tools;
import org.openhubframework.openhub.ri.AbstractRIDbTest;
import org.openhubframework.openhub.ri.in.translate.model.AsyncTranslateResponse;
import org.openhubframework.openhub.test.TestCamelUtils;
import org.openhubframework.openhub.test.data.ExternalSystemTestEnum;
import org.openhubframework.openhub.test.data.ServiceTestEnum;
import org.openhubframework.openhub.test.route.ActiveRoutes;
import org.openhubframework.openhub.test.route.TestWsUriBuilder;


/**
 * Test suite for {@link AsyncTranslateWsRoute}.
 *
 * @author Petr Juza
 * @since 1.0.0
 */
@ActiveRoutes(classes = AsyncTranslateWsRoute.class)
public class AsyncTranslateWsRouteTest extends AbstractRIDbTest {

    private static final String REQ_XML =
                    "<asyncTranslateRequest xmlns=\"http://openhub-ri.org/ws/TranslateService-v1\">"
                    + "    <inputText>English test for translation</inputText>"
                    + "    <inputLang>en</inputLang>"
                    + "    <outputLang>cs</outputLang>"
                    + "</asyncTranslateRequest>";

    @Produce(uri = TestWsUriBuilder.URI_WS_IN + "asyncTranslateRequest")
    private ProducerTemplate producer;

    @EndpointInject(uri = "mock:test")
    private MockEndpoint mock;

    @Test
    public void testRouteIn_responseOK() throws Exception {
        getCamelContext().getRouteDefinition(AsyncTranslateWsRoute.ROUTE_ID_ASYNC_IN)
                .adviceWith(getCamelContext(), new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() throws Exception {
                        TestCamelUtils.replaceToAsynch(this);

                        weaveAddLast().to(mock);
                    }
                });

        mock.expectedMessageCount(1);

        // action
        String output = producer.requestBody((Object) REQ_XML, String.class);

        // verify
        mock.assertIsSatisfied();

        // verify OK response
        AsyncTranslateResponse res = Tools.unmarshalFromXml(output, AsyncTranslateResponse.class);
        assertThat(res.getConfirmAsyncTranslate().getStatus(), is(ConfirmationTypes.OK));

        // check message header
        Message inMsg = mock.getExchanges().get(0).getIn();
        assertThat(inMsg.getHeader(AsynchConstants.OBJECT_ID_HEADER), is("en")); //= inputLang
    }

    @Test
    public void testRouteOut() throws Exception {
        getCamelContext().getRouteDefinition(AsyncTranslateWsRoute.ROUTE_ID_ASYNC_OUT)
                .adviceWith(getCamelContext(), new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() throws Exception {
                        weaveAddLast().to(mock);
                    }
                });

        mock.expectedMessageCount(1);

        // action
        org.openhubframework.openhub.api.entity.Message msg =
                createAndSaveMessage(ExternalSystemTestEnum.CRM, ServiceTestEnum.ACCOUNT, "testOp", "payload");

        producer.sendBodyAndHeader(AsyncTranslateWsRoute.URI_ASYNC_OUT, REQ_XML, AsynchConstants.MSG_HEADER, msg);

        // verify
        mock.assertIsSatisfied();

        // nothing to verify
    }
}
