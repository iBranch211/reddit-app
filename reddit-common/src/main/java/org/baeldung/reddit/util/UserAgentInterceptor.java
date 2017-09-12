package org.baeldung.reddit.util;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class UserAgentInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(final HttpRequest request, final byte[] body, final ClientHttpRequestExecution execution) throws IOException {
        final HttpHeaders headers = request.getHeaders();
        headers.add("User-Agent", "Reddit Scheduler v3.0");

        final ClientHttpResponse response = execution.execute(request, body);
        // System.out.println(response.getHeaders());
        // System.out.println(response.getHeaders().getFirst("X-Ratelimit-Used"));
        return response;
    }
}