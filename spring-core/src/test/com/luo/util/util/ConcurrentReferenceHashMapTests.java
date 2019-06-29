package test.com.luo.util.util;

import com.luo.comparator.ComparableComparator;
import com.luo.comparator.NullSafeComparator;
import com.luo.util.ConcurrentReferenceHashMap;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import java.util.Comparator;

import static org.junit.Assert.*;

public class ConcurrentReferenceHashMapTests {

    private static final Comparator<? super String> NULL_SAFE_STRING_SORT = new NullSafeComparator<String>(
            true, new ComparableComparator<String>());

    @Rule
    public ExpectedException thrown = ExpectedException.none();


    private class TestWeakConcurrentCache<K, V> extends ConcurrentReferenceHashMap<K, V> {


        private int supplementalHash;

    }


}