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

import static org.openhubframework.openhub.ri.in.translate.TranslateWebServiceConfig.*;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import org.openhubframework.openhub.api.config.WebServiceValidatingSources;


/**
 * Implementation of {@link WebServiceValidatingSources} interface for example module.
 *
 * @author Petr Juza
 * @since 1.0.0
 */
@Component
public class TranslateWebServiceValidatingSources implements WebServiceValidatingSources {

    @Override
    public Resource[] getXsdSchemas() {
        return new Resource[] {TRANSLATE_OPERATIONS_XSD_RESOURCE};
    }

    @Override
    public String[] getIgnoreRequests() {
        return new String[] {"{" + TRANSLATE_SERVICE_NS + "}syncTranslateRequest"};
    }
}
