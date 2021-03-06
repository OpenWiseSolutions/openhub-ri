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

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @PostConstruct
    public void initialization() {
        LOG.debug("Initialization of OpenHub RI");
    }
}
