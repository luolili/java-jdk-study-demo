package base.adapter;

/**
 * two to three adapter
 */
public class TwoPlugAdapter implements ThreePlug {

    private BGTwoPlug bgTwoPlug;

    public TwoPlugAdapter(BGTwoPlug bgTwoPlug) {
        this.bgTwoPlug = bgTwoPlug;
    }

    @Override
    public void powerWithThree() {
        System.out.println("tow --> three");
        bgTwoPlug.powerWithTwo();
    }
}
