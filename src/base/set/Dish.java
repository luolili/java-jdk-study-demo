package base.set;

public class Dish {
    private String name;
    private boolean vegetatian;
    private int calorie;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVegetatian() {
        return vegetatian;
    }

    public void setVegetatian(boolean vegetatian) {
        this.vegetatian = vegetatian;
    }

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }
}
