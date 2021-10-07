package playstation.back.featureflags.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import playstation.back.featureflags.Dto.FeatureFlagDto;
import playstation.back.featureflags.model.FeatureFlag;
import playstation.back.featureflags.service.MicroserviceRepo;
import java.util.List;

@RestController
@CrossOrigin
public class FeatureFlagsController {

    private static final String MICROSERVICE_FEATURE_FLAGS_ROUTE = "featureflags";
    private final MicroserviceRepo microserviceRepo;

    @Autowired
    public FeatureFlagsController(MicroserviceRepo microserviceRepo) {
        this.microserviceRepo = microserviceRepo;
    }

    /**
     * Route for fetching all feature flags.
     */
    @GetMapping(MICROSERVICE_FEATURE_FLAGS_ROUTE)
    public ResponseEntity<?> getFeatureFlags() {

        try {
            List<FeatureFlagDto> featureFlagList = microserviceRepo.getFeatureFlagsFromService();

            if (featureFlagList.isEmpty()) {
                return ResponseEntity.ok().body("No feature flags were found.");
            }

            // return successful response
            return ResponseEntity.ok().body(featureFlagList);
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());

            // 500 response in the case that an error has been caught and a successful response was not returned
            return ResponseEntity.internalServerError().body("Error occurred when fetching the feature flags.");
        }
    }

    /**
     * Route for inserting and updating a feature flag.
     */
    @PostMapping(MICROSERVICE_FEATURE_FLAGS_ROUTE)
    public ResponseEntity<?> updateOrInsertFeatureFlag(@RequestBody FeatureFlagDto featureFlagDto) {

        try {
            if (!featureFlagDto.getName().trim().equals("")) {
                FeatureFlag featureFlagAsModel = new FeatureFlag(featureFlagDto);
                List<FeatureFlag> updatedFeatureFlagList = microserviceRepo.addOrUpdateFeatureFlag(featureFlagAsModel);
                return ResponseEntity.ok().body(updatedFeatureFlagList);
            }
            else {
                throw new Exception("Invalid name passed in request");
            }
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());

            // 500 response in the case that an error has been caught and a successful response was not returned
            return ResponseEntity.internalServerError().body("Error occurred when adding or updating feature flag");
        }
    }
}
