package playstation.back.featureflags.model;

import org.apache.commons.lang.StringUtils;

@SuppressWarnings("unused")
public class FeatureFlag {

    private static final int COUNT_OF_REGIONS = 5;
    private String name;
    private int value;

    public FeatureFlag() {}

    public String getName() {
        return  name;
    }

    public String[] getValue() {
        String decimalAsBinaryString = Integer.toBinaryString(value);
        String zeroPaddedString = StringUtils.leftPad(decimalAsBinaryString, COUNT_OF_REGIONS, "0");
        return zeroPaddedString.split("");
    }

//    public String getJsonObject() {
//        return "{"
//    }
}
