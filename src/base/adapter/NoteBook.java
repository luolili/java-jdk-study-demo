package base.adapter;

/**
 * 1. adapter impl target(three) interface
 * 2. use target as a param to construct client obj
 * 3. use source as a param to construct an adapter
 */
public class NoteBook {

    private ThreePlug threePlug;

    public NoteBook(ThreePlug threePlug) {
        this.threePlug = threePlug;
    }

    public void charge() {
        threePlug.powerWithThree();
    }

    public static void main(String[] args) {

        // you have two plug, but you need three plug
        BGTwoPlug bgTwoPlug = new BGTwoPlug();
        ThreePlug adapter = new TwoPlugAdapter(bgTwoPlug);

        NoteBook noteBook = new NoteBook(adapter);
        noteBook.charge();


    }
}
