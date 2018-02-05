package org.openhubframework.openhub.ri;

import org.apache.camel.Exchange;
import org.apache.camel.Handler;
import org.openhubframework.openhub.api.asynch.model.CallbackResponse;
import org.openhubframework.openhub.api.asynch.model.ConfirmationTypes;
import org.openhubframework.openhub.ri.in.translate.model.AsyncTranslateResponse;
import org.springframework.stereotype.Component;


/**
 * @author Karel Kovarik
 */
@Component
public class AsyncResponseService {

    @Handler
    public void asyncResponse(Exchange exchange) {
        AsyncTranslateResponse res = new AsyncTranslateResponse();
        res.setConfirmAsyncTranslate(new CallbackResponse());
        res.getConfirmAsyncTranslate().setStatus(ConfirmationTypes.OK);
        exchange.getIn().setBody(res);
    }
}
