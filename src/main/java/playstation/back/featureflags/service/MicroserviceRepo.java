package playstation.back.featureflags.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import playstation.back.featureflags.model.FeatureFlag;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for reading from and writing to the Feature Flag microservice.
 */
@Service
public class MicroserviceRepo {

    private static final String MICROSERVICE_FEATURE_FLAGS_FULL_PATH = "http://localhost:12300/featureflags";

    /**
     * Fetches and parses a list of feature flags objects from the microservice
     */
    public List<FeatureFlag> getFeatureFlagsFromService() throws Exception {
        List<FeatureFlag> featureFlagList;

        try {

            // create and execute get http request
            HttpClient client = HttpClientBuilder.create().build();
            HttpResponse response = client.execute(new HttpGet(MICROSERVICE_FEATURE_FLAGS_FULL_PATH));

            // get json from response and convert to feature flag list
            ObjectMapper mapper = new ObjectMapper();
            featureFlagList = mapper.readValue(response.getEntity().getContent(),
                    new TypeReference<List<FeatureFlag>>() {});
        }
        catch (Exception ex) {
            throw new Exception("Error encountered when fetching the feature flags");
        }
        return featureFlagList;
    }

    /**
     * Takes in a feature flag object and makes a POST request to the microservice
     * which either updates or inserts the feature flag object, and returns the full list of feature flags with the changes
     */
    public List<FeatureFlag> addOrUpdateFeatureFlag(FeatureFlag featureFlag) throws Exception {
        List<FeatureFlag> featureFlagList;

        try {

            // create request
            HttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(MICROSERVICE_FEATURE_FLAGS_FULL_PATH);
            ObjectMapper mapper = new ObjectMapper();
            String jsonString = mapper.writeValueAsString(featureFlag);
            httpPost.setEntity(new StringEntity(jsonString));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // execute post and parse json string response into a feature flag list
            HttpResponse response = client.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String responseEntity = EntityUtils.toString(entity, "UTF-8");
            featureFlagList = mapper.readValue(responseEntity, new TypeReference<List<FeatureFlag>>() {});
        }
        catch (Exception ex) {
            throw new Exception("Error encountered when making POST request to microservice");
        }

        return featureFlagList;
    }
}
