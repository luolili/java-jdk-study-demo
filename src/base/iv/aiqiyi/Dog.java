package base.iv.aiqiyi;

public class Dog extends Animal {
    @Override
    public void move() {
        System.out.println("dog can run");
    }

    public void bark() {
        System.out.println("dog can bark");
    }

    public static void main(String[] args) {
        Animal a = new Animal();
        Animal b = new Dog();
        b.move();
        // ((Dog) b).bark();
        System.out.println(Math.floor(-1.7));


    }

}
