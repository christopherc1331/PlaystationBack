package playstation.back.featureflags.model;

import org.apache.commons.lang.StringUtils;

@SuppressWarnings("unused")
public class FeatureFlag {

    private String name;
    private int value;

    public FeatureFlag() {}

    public String getName() {
        return  name;
    }

    public String[] getValue() {
        String decimalAsBinaryString = Integer.toBinaryString(value);
        String zeroPaddedString = StringUtils.leftPad(decimalAsBinaryString,5, "0");
        return zeroPaddedString.split("");
    }
}
