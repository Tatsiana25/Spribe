package api.helpersAndUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogUtil {
    private static final Logger logger = LogManager.getLogger(LogUtil.class);
    private static final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public static void logRequest(FilterableRequestSpecification req) {
        logger.info("Request Method: {}\nRequest URL: {}\nRequest Headers: {}",
                req.getMethod(),
                req.getURI(),
                req.getHeaders());
        logBody("Request Body", req.getBody() != null ? req.getBody().toString() : null);
    }

    public static void logResponse(Response response) {
        logger.info("Response Status Code: {}\nResponse Headers: {}",
                response.getStatusCode(),
                response.getHeaders());
        logBody("Response Body", response.getBody() != null ? response.getBody().asString() : null);
    }

    private static void logBody(String title, String body) {
        if (body == null || body.isEmpty()) {
            return;
        }
        try {
            String beautifiedBody = objectMapper.writeValueAsString(objectMapper.readTree(body));
            logger.info("{}:\n{}\n", title, beautifiedBody);
        } catch (Exception e) {
            logger.info("{}:\n{}\n", title, body);
        }
    }
}
