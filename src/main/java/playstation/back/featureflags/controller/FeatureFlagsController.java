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
    public ResponseEntity<?> updateOrInsertFeatureFlag(@RequestBody List<FeatureFlagDto> featureFlagDtoList) {

        List<FeatureFlag> updatedFeatureFlagList = null;
        try {
            for (int i = 0; i < featureFlagDtoList.size(); i++) {
                FeatureFlagDto featureFlagDto = featureFlagDtoList.get(i);

                if (!featureFlagDto.getName().trim().equals("")) {
                    FeatureFlag featureFlagAsModel = new FeatureFlag(featureFlagDto);

                    // if in last iteration then add response to return list, otherwise just make the post request
                    if (i == featureFlagDtoList.size() - 1) {
                        updatedFeatureFlagList = microserviceRepo.addOrUpdateFeatureFlag(featureFlagAsModel);
                    }
                    else {
                        microserviceRepo.addOrUpdateFeatureFlag(featureFlagAsModel);
                    }
                }
                else {
                    throw new Exception("Invalid name passed in request");
                }
            }
            return ResponseEntity.ok().body(updatedFeatureFlagList);
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());

            // 500 response in the case that an error has been caught and a successful response was not returned
            return ResponseEntity.internalServerError().body("Error occurred when adding or updating feature flag");
        }
    }
}
