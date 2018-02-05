package org.openhubframework.openhub.ri;

import org.openhubframework.openhub.api.asynch.AsynchResponseProcessor;
import org.openhubframework.openhub.api.asynch.model.CallbackResponse;
import org.openhubframework.openhub.ri.in.translate.model.AsyncTranslateResponse;
import org.springframework.stereotype.Component;


/**
 * @author Karel Kovarik
 */
@Component
public class AsyncResponse extends AsynchResponseProcessor {

    @Override
    protected Object setCallbackResponse(CallbackResponse callbackResponse) {
        AsyncTranslateResponse res = new AsyncTranslateResponse();
        res.setConfirmAsyncTranslate(callbackResponse);
        return res;
    }
}
