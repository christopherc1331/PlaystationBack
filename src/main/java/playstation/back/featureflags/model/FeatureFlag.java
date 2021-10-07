package playstation.back.featureflags.model;

import playstation.back.featureflags.Dto.FeatureFlagDto;

@SuppressWarnings("unused")
public class FeatureFlag {

    private static final int COUNT_OF_REGIONS = 5;
    private String name;
    private int value;

    public FeatureFlag() {}

    public FeatureFlag(FeatureFlagDto featureFlagDto) {
        String singleString = String.join("", featureFlagDto.getValueArr());
        value = Integer.parseInt(singleString, 2);
        name = featureFlagDto.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
