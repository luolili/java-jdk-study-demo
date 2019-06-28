package com.luo.core.style;

import com.luo.util.ObjectUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.*;

import static org.junit.Assert.*;

/**
 * notice : you need override the toString of the ToStringCreator
 * for collection: you need to append the type of coll before append [
 * map and collection is with no []
 */
public class ToStringCreatorTest {

    private SomeObject s1, s2, s3;


    @Before
    public void setUp() throws Exception {
        //初始化
        s1 = new SomeObject() {
            @Override
            public String toString() {
                return "A";
            }
        };

        s2 = new SomeObject() {
            @Override
            public String toString() {
                return "B";
            }
        };

        s3 = new SomeObject() {
            @Override
            public String toString() {
                return "C";
            }
        };
    }

    @Test
    public void defaultStyleMap() {
        Map<String, String> map = getMap();


        Object stringy = new Object() {
            @Override
            public String toString() {
                //重写ToStringCreator toString方法
                return new ToStringCreator(this).append("familyFavoriteSport", map).toString();
            }
        };

        System.out.println(stringy.toString());

        assertEquals("[ToStringCreatorTest.4@" + ObjectUtils.getIdentityHexString(stringy) +
                        " familyFavoriteSport = map['Keri' -> 'Softball', 'Scot' -> 'Fishing', 'Keith' -> 'Flag Football']]",
                stringy.toString());
    }


    private Map<String, String> getMap() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("Keri", "Softball");
        map.put("Scot", "Fishing");
        map.put("Keith", "Flag Football");
        return map;
    }


    @Test
    public void defaultStyleArray() {
        SomeObject[] array = new SomeObject[]{s1, s2, s3};
        String str = new ToStringCreator(array).toString();
        System.out.println(str);
        assertEquals("[@" + ObjectUtils.getIdentityHexString(array) +
                " array<ToStringCreatorTest.SomeObject>[A, B, C]]", str);
    }

    @Test
    public void primitiveArrays() {
        int[] integers = new int[]{0, 1, 2, 3, 4};
        String str = new ToStringCreator(integers).toString();
        assertEquals("[@" + ObjectUtils.getIdentityHexString(integers) + " array<Integer>[0, 1, 2, 3, 4]]", str);
    }

    @Test
    public void appendList() {
        List<SomeObject> list = new ArrayList<>();
        list.add(s1);
        list.add(s2);
        list.add(s3);
        String str = new ToStringCreator(this).append("myLetters", list).toString();
        System.out.println(str);
        assertEquals("[ToStringCreatorTest@" + ObjectUtils.getIdentityHexString(this) + " myLetters = list[A, B, C]]",
                str);
    }

    @Test
    public void appendSet() {
        Set<SomeObject> set = new LinkedHashSet<>();
        set.add(s1);
        set.add(s2);
        set.add(s3);
        String str = new ToStringCreator(this).append("myLetters", set).toString();
        assertEquals("[ToStringCreatorTest@" + ObjectUtils.getIdentityHexString(this) + " myLetters = set[A, B, C]]", str);
    }


    @Test
    public void appendClass() {
        String str = new ToStringCreator(this).append("myClass", this.getClass()).toString();
        assertEquals("[ToStringCreatorTest@" + ObjectUtils.getIdentityHexString(this) +
                " myClass = ToStringCreatorTest]", str);
    }


    @Test
    public void appendMethod() throws Exception {
        String str = new ToStringCreator(this).append("myMethod", this.getClass().getMethod("appendMethod")).toString();
        assertEquals("[ToStringCreatorTest@" + ObjectUtils.getIdentityHexString(this) +
                " myMethod = appendMethod@ToStringCreatorTest]", str);
    }

    //create a inner class because ClassUtils.getShortName uses the inner class
    public static class SomeObject {
    }

}
