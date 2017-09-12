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

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;


/**
 * Web services configuration.
 *
 * @author Petr Juza
 * @since 1.0.0
 */
@Configuration
public class TranslateWebServiceConfig {

    static final ClassPathResource TRANSLATE_OPERATIONS_XSD_RESOURCE = new ClassPathResource(
            "org/openhubframework/openhub/ri/in/translate/ws/v1_0/translateOperations-v1.0.xsd");

    static final String TRANSLATE_SERVICE_NS = "http://openhub-ri.org/ws/TranslateService-v1";

    @Bean(name = "translateOperations-v1.0")
    public XsdSchema translateOperations() {
        return new SimpleXsdSchema(TRANSLATE_OPERATIONS_XSD_RESOURCE);
    }

    @Bean(name = "translate")
    public SimpleWsdl11Definition translateWsdl() {
        SimpleWsdl11Definition wsdl = new SimpleWsdl11Definition();
        wsdl.setWsdl(new ClassPathResource(
                "org/openhubframework/openhub/ri/in/translate/ws/v1_0/translate-v1.0.wsdl"));
        return wsdl;
    }
}