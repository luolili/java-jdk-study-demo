package base.iv;

public class OuterClass {

    public String name;
    private int age;

    public void say() {
        System.out.println("name:" + name + "," + "age:" + age);
        // 外部类不可直接访问内部类的pulic属性
        //System.out.println(iname);
        System.out.println("inner public name:" + getInnerClass().getIname());
    }

    class InnerClass {

        public String iname;

        public void getFromOuterClass() {
            System.out.println(name);
            System.out.println(age);
        }

        public String getIname() {
            return iname;
        }

        public void setIname(String iname) {
            this.iname = iname;
        }
    }

    public InnerClass getInnerClass() {
        InnerClass innerClass = new InnerClass();
        innerClass.setIname("this is inner public name");
        return innerClass;
    }

    //静态内部类
    static class StaticInnerClass {
        public String siname;

        public void getFromOuterClass() {
            //静态内部类无法直接访问外部类public/private成员
            //System.out.println(name);
            //System.out.println(age);
        }


        public String getSiname() {
            return siname;
        }

        public void setSiname(String siname) {
            this.siname = siname;
        }
    }

    public static void main(String[] args) {
        OuterClass outerClass = new OuterClass();
        outerClass.setName("outer name");
        outerClass.setAge(9);
        InnerClass innerClass = outerClass.getInnerClass();
        outerClass.say();
        innerClass.getFromOuterClass();

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
