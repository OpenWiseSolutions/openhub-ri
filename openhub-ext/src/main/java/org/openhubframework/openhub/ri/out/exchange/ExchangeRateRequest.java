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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.util.Assert;


/**
 * Exchange rate request.
 *
 * @author Petr Juza
 * @since 1.0.0
 */
public final class ExchangeRateRequest {

    private String sourceCurrency;
    private String targetCurrency;

    /**
     * Constructor
     *
     * @param sourceCurrency Source currency symbol, e.g. "EUR"
     *                       (<a href="http://www.xe.com/iso4217.php">see</a> for possible codes)
     * @param targetCurrency Target currency symbol, e.g. "CZK"
     */
    public ExchangeRateRequest(String sourceCurrency, String targetCurrency) {
        Assert.hasText(sourceCurrency, "sourceCurrency mustn't be empty");
        Assert.hasText(targetCurrency, "targetCurrency mustn't be empty");

        this.sourceCurrency = sourceCurrency;
        this.targetCurrency = targetCurrency;
    }

    public String getSourceCurrency() {
        return sourceCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("sourceCurrency", sourceCurrency)
            .append("targetCurrency", targetCurrency)
            .toString();
    }
}
