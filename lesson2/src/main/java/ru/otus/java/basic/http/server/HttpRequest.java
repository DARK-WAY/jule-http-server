package ru.otus.java.basic.http.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private String rawRequest;
    private String uri;
    private HttpMethod method;
    private Map<String, String> parameters;
    private Map<String, String> headers;
    private String body;

    public String getRoutingKey() {
        return method + " " + uri;
    }

    public String getUri() {
        return uri;
    }

    public String getBody() {
        return body;
    }

    private final Logger logger = LogManager.getLogger(this.getClass().getName());

    public HttpRequest(String rawRequest) {
        this.rawRequest = rawRequest;
        this.parse();
    }

    private void parse() {
        int startIndex = rawRequest.indexOf(' ');
        int endIndex = rawRequest.indexOf(' ', startIndex + 1);
        this.uri = rawRequest.substring(startIndex + 1, endIndex);
        this.method = HttpMethod.valueOf(rawRequest.substring(0, startIndex));
        this.parameters = new HashMap<>();
        if (uri.contains("?")) {
            String[] elements = uri.split("[?]");
            this.uri = elements[0];
            String[] keysValues = elements[1].split("&");
            for (String o : keysValues) {
                String[] keyValue = o.split("=");
                this.parameters.put(keyValue[0], keyValue[1]);
            }
        }
        if (method == HttpMethod.POST) {
            this.body = rawRequest.substring(
                    rawRequest.indexOf("\r\n\r\n") + 4
            );
        }

        this.headers= new HashMap<>();
        int startpos = rawRequest.indexOf("\r\n", 0) + 2;
        int beginHeader = rawRequest.indexOf("\r\n", startpos);
        int endHeader = rawRequest.indexOf("\r\n\r\n");

        while (beginHeader > 0 && beginHeader < endHeader) {

            String strHeader = rawRequest.substring(startpos, beginHeader);
            int keyvalue = strHeader.indexOf(':');
            if (keyvalue > 0) {
                String headersKey = strHeader.substring(0, keyvalue);
                if (headersKey == null) headersKey = "";

                String headersValue = strHeader.substring(keyvalue + 1, strHeader.length()).trim();
                if (headersValue == null) headersValue = "";
                this.headers.put(headersKey, headersValue);
            }
            startpos = beginHeader + 2;
            beginHeader = rawRequest.indexOf("\r\n", startpos + 2);

        }
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public boolean containsHeader(String key) {
        return headers.containsKey(key);
    }

    public boolean containsParameter(String key) {
        return parameters.containsKey(key);
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }

    public void logInfo() {
        logger.info("uri: " + uri);
        logger.info("method: " + method);
        logger.info("body: " + body);
        logger.debug("headers:\r\n" + headers.toString());
        logger.debug("rawRequest:\r\n" + rawRequest);
    }
}
