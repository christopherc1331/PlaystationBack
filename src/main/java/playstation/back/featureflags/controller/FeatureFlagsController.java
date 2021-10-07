package playstation.back.featureflags.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import playstation.back.featureflags.model.FeatureFlag;
import playstation.back.featureflags.service.MicroserviceRepo;
import java.util.Arrays;
import java.util.List;

@RestController
public class FeatureFlagsController {

    private static final String MICROSERVICE_FEATURE_FLAGS_ROUTE = "featureflags";

    private final MicroserviceRepo microserviceRepo;

    @Autowired
    public FeatureFlagsController(MicroserviceRepo microserviceRepo) {
        this.microserviceRepo = microserviceRepo;
    }

    @GetMapping(MICROSERVICE_FEATURE_FLAGS_ROUTE)
    public ResponseEntity<?> getFeatureFlags() {

        try {

            List<FeatureFlag> featureFlagList = microserviceRepo.getFeatureFlagsFromService();

            if (featureFlagList.isEmpty()) {
                return ResponseEntity.ok().body("No feature flags were found.");
            }

            // return successful response
            return ResponseEntity.ok().body(featureFlagList);
        }
        catch (Exception ex) {
            System.out.println(Arrays.toString(ex.getStackTrace()));

            // 500 response in the case that an error has been caught and a successful response was not returned
            return ResponseEntity.internalServerError().body("Error occurred when fetching the feature flags.");
        }
    }


}
