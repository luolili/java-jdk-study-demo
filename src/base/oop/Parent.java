package base.oop;

public class Parent {

    public String name;
    protected String proName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {

        this.proName = proName;
    }

    protected void setProName1(String proName) {

        this.proName = proName;
    }

}


