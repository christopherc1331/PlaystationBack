package playstation.back.featureflags.Dto;

import org.apache.commons.lang.StringUtils;
import playstation.back.featureflags.model.FeatureFlag;

public class FeatureFlagDto {

    private static final int COUNT_OF_REGIONS = 5;
    private String name;
    private String[] valueArr;

    public FeatureFlagDto() {}

    public FeatureFlagDto(FeatureFlag featureFlag) {
        String decimalAsBinaryString = Integer.toBinaryString(featureFlag.getValue());
        String zeroPaddedString = StringUtils.leftPad(decimalAsBinaryString, COUNT_OF_REGIONS, "0");
        valueArr = zeroPaddedString.split("");
        name = featureFlag.getName();
    }

    public String getName() {
        return  name;
    }

    public String[] getValueArr() {
        return valueArr;
    }
}
