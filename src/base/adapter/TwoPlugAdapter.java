package base.adapter;

/**
 * two to three adapter:invoke source method in target method
 */
public class TwoPlugAdapter implements ThreePlug {

    private BGTwoPlug bgTwoPlug;

    public TwoPlugAdapter(BGTwoPlug bgTwoPlug) {
        this.bgTwoPlug = bgTwoPlug;
    }

    @Override
    public void powerWithThree() {
        System.out.println("two --> three using composition");
        bgTwoPlug.powerWithTwo();
    }
}
