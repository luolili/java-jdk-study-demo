package base.iv;

/**
 * 认识Class上的方法
 */
public class GenericClass {
    public static void main(String[] args) {
        User user = new User();
        Class<? extends User> resolved = user.getClass();

        System.out.println(resolved.getGenericSuperclass());//class java.lang.Object
        System.out.println(resolved.getName());//base.iv.User
        /**
         * 这个方法是用在数组上的，返回元素的类型；
         * 如果是非数组的对象，返回null
         */
        System.out.println(new String[0].getClass());//class [Ljava.lang.String;
        System.out.println(resolved.getComponentType());//null
        //User不是Object的超类或超接口
        System.out.println(resolved.isAssignableFrom(Object.class));//false
        System.out.println(resolved.getInterfaces());


    }
}
