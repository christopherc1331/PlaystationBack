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

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Overloaded method for @RequestBody json --> model conversions.
     * Will automatically take the bitarray, join them into a single string, then convert that to a binary number
     * and set the binary (int dType) to the value field in the object.
     */
    public void setValue(String[] valueArr) {
        String singleString = String.join(",", valueArr);
        value = Integer.parseInt(singleString, 2);
    }
}
