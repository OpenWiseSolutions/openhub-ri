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

package org.openhubframework.openhub.ri;

import java.io.InputStream;
import javax.annotation.PostConstruct;

import org.apache.camel.CamelContext;
import org.apache.camel.model.RoutesDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;

import org.openhubframework.openhub.common.AutoConfiguration;

/**
 * Basic configuration of project.
 *
 * @author Tomas Hanus
 * @since 1.0.0
 */
@AutoConfiguration
@ComponentScan
public class ProjectConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectConfiguration.class);

    @Autowired
    private CamelContext context;

    @PostConstruct
    public void initialization() throws Exception {
        LOG.debug("Initialization of OpenHub RI");

        LOG.info("Add custom routes from xml:");

        // load route from XML and add them to the existing camel context
        InputStream is = getClass().getResourceAsStream("/asyncTranslate.xml");
        RoutesDefinition routes = context.loadRoutesDefinition(is);
        context.addRouteDefinitions(routes.getRoutes());

        is = getClass().getResourceAsStream("/exchange.xml");
        routes = context.loadRoutesDefinition(is);
        context.addRouteDefinitions(routes.getRoutes());


        is = getClass().getResourceAsStream("/json-to-xml.xml");
        routes = context.loadRoutesDefinition(is);
        context.addRouteDefinitions(routes.getRoutes());

        LOG.info("HOTOVO");
    }
}
