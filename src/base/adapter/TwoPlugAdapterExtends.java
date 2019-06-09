package base.adapter;

/**
 * two to three adapter usnig extends
 */
public class TwoPlugAdapterExtends extends BGTwoPlug implements ThreePlug {


    @Override
    public void powerWithThree() {
        //System.out.println("two--> three");
        System.out.println("two--> three using extends");

        this.powerWithTwo();
    }
}
