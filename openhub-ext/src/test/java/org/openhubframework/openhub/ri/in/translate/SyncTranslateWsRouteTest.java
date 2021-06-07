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
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;

import org.openhubframework.openhub.common.Tools;
import org.openhubframework.openhub.ri.AbstractRITest;
import org.openhubframework.openhub.ri.in.translate.model.SyncTranslateResponse;
import org.openhubframework.openhub.test.route.ActiveRoutes;
import org.openhubframework.openhub.test.route.TestWsUriBuilder;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Test suite for {@link SyncTranslateWsRoute}.
 *
 * @author Petr Juza
 * @since 1.0.0
 */
@ActiveRoutes(classes = SyncTranslateWsRoute.class)
public class SyncTranslateWsRouteTest extends AbstractRITest {

    @Produce(uri = TestWsUriBuilder.URI_WS_IN + "syncTranslateRequest")
    private ProducerTemplate producer;

    @EndpointInject(uri = "mock:test")
    private MockEndpoint mock;

    @Autowired
    private CamelContext context;

    /**
     * Checking successful calling.
     */
    @Test
    public void testSyncTranslateRoute() throws Exception {
        String xml = "<syncTranslateRequest xmlns=\"http://openhub-ri.org/ws/TranslateService-v1\">"
                + "    <inputText>English test for translation</inputText>"
                + "    <inputLang>en</inputLang>"
                + "    <outputLang>cs</outputLang>"
                + "</syncTranslateRequest>";

        String output = producer.requestBody((Object)xml, String.class);
        SyncTranslateResponse res = Tools.unmarshalFromXml(output, SyncTranslateResponse.class);

        assertThat(res, notNullValue());
        assertThat(res.getOutputText(), is("translated output text"));
    }
}
