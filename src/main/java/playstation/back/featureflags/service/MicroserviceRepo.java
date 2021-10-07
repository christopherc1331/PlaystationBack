package playstation.back.featureflags.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
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

            // get json from response
            String responseBody = EntityUtils.toString(response.getEntity());

            featureFlagList = parseStringToList(responseBody);
        }
        catch (Exception ex) {
            throw new Exception("Error encountered when fetching the feature flags");
        }
        return featureFlagList;
    }
    
    /**
     * Helper method parses String and returns a List of feature flag objects.
     */
    private List<FeatureFlag> parseStringToList(String listObj) throws Exception {
        List<FeatureFlag> featureFlagList = new ArrayList<>();

        try {

            // strip the starting/ending square brackets from response body and split by commas
            // then remove all inconsistent quotations, as well as beginning/ending curly braces
            List<String> resObjList = Arrays.stream(listObj
                    .substring(1, listObj.length() - 1)
                    .split("},"))
                    .map(field -> field.substring(1)
                            .replaceAll("\"","")
                            .replaceAll("}",""))
                    .collect(Collectors.toList());

            // use jackson object mapper to map each key value pair into a JSON object ==> FeatureFlag model
            // finally add the class object to the list of FeatureFlag objects
            ObjectMapper mapper = new ObjectMapper();
            for (String stringObj : resObjList) {
                JSONObject objectJson = new JSONObject();

                String[] objFields = stringObj.split(",");
                for (String field : objFields) {
                    String[] fieldKeyValuePair = field.split(":");
                    String key = fieldKeyValuePair[0];
                    String value = fieldKeyValuePair[1];
                    objectJson.put(key, value);
                }

                FeatureFlag featureFlag = mapper.readValue(objectJson.toString(), FeatureFlag.class);
                featureFlagList.add(featureFlag);
            }
        }
        catch (Exception ex) {
            throw new Exception("Error parsing string to JSON");
        }
        return featureFlagList;
    }
}
