package org.openhubframework.openhub.ri;

import org.apache.http.auth.UsernamePasswordCredentials;
import org.springframework.stereotype.Component;
import org.springframework.ws.transport.http.CommonsHttpMessageSender;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;


/**
 * @author Karel Kovarik
 */
@Component
public class SelfMessageSender extends HttpComponentsMessageSender {

    public SelfMessageSender() {
        this.setCredentials(new UsernamePasswordCredentials("wsUser", "wsPassword"));
    }
}
