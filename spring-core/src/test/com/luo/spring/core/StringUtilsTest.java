package test.com.luo.spring.core;

import com.luo.util.StringUtils;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.assertEquals;

/**
 * StringUtils Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre> 15, 2019</pre>
 */
public class StringUtilsTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: isEmpty(Object s)
     */
    @Test
    public void testIsEmpty() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: hasLength(CharSequence str)
     */
    @Test
    public void testHasLengthStr() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: containsText(CharSequence str)
     */
    @Test
    public void testContainsText() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: hasText(CharSequence str)
     */
    @Test
    public void testHasTextStr() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: containsWhiteSpace(CharSequence str)
     */
    @Test
    public void testContainsWhiteSpaceStr() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: trimWhiteSpace(String str)
     */
    @Test
    public void testTrimWhiteSpace() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: trimAllWhiteSpace(String str)
     */
    @Test
    public void testTrimAllWhiteSpace() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: trimLeadingWhiteSpace(String str)
     */
    @Test
    public void testTrimLeadingWhiteSpace() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: trimTrailingWhiteSpace(String str)
     */
    @Test
    public void testTrimTrailingWhiteSpace() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: startsWithIgnoreCase(String str, String prefix)
     */
    @Test
    public void testStartsWithIgnoreCase() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: endsWithIgnoreCase(String str, String suffix)
     */
    @Test
    public void testEndsWithIgnoreCase() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: substringMatch(String str, int index, String substring)
     */
    @Test
    public void testSubstringMatch() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: countOccurrencesOf(String str, String sub)
     */
    @Test
    public void testCountOccurrencesOf() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: deleteAny(String inString, String charsToDelete)
     */
    @Test
    public void testDeleteAny() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: quote(String str)
     */
    @Test
    public void testQuote() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: quoteIfString(Object object)
     */
    @Test
    public void testQuoteIfString() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: unqualify(String qualifiedName, char separator)
     */
    @Test
    public void testUnqualifyForQualifiedNameSeparator() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: unqualify(String qualifiedName)
     */
    @Test
    public void testUnqualifyQualifiedName() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: changeFirstCharacterCase(String str, boolean capitalize)
     */
    @Test
    public void testChangeFirstCharacterCase() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: capitalize(String str)
     */
    @Test
    public void testCapitalize() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: uncapitalize(String str)
     */
    @Test
    public void testUncapitalize() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: getFileName(String path)
     */
    @Test
    public void testGetFileName() throws Exception {

        String path = "/d/c/hu.mv";
        //System.out.println(StringUtils.getFileName(path));


        assertEquals("nn.", StringUtils.getFileName("nn."));
    }

    @Test
    public void testDelimitedListToStringArray() throws Exception {


        String[] sa = StringUtils.delimitedListToStringArray("a,b", ",");

        assertEquals(2, sa.length);
        assertEquals("a", sa[0]);
        //assertEquals("b", sa[1]);
    }




} 
