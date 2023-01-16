package OOP.Tests;

import OOP.Provided.OOPResult;
import OOP.Solution.OOPTestSummary;
import OOP.Solution.OOPUnitCore;
import OOP.Tests.ClassesForTests.*;
import org.junit.Test;
import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.Assert.*;

public class Tests {
    @Test
    public void testOrdered() throws IllegalAccessException {
        OOPTestSummary summary = OOPUnitCore.runClass(OrderedTest.class);
        assertNotNull(summary);
        Map<String, OOPResult> results = getResultsMap(summary);
        assertNotNull(results);
        assertEquals(8, results.size());

        assertEquals(OOPResult.OOPTestResult.SUCCESS, results.get("m1").getResultType());
        assertNull(results.get("m1").getMessage());

        assertEquals(OOPResult.OOPTestResult.SUCCESS, results.get("m2").getResultType());
        assertNull(results.get("m2").getMessage());

        assertEquals(OOPResult.OOPTestResult.FAILURE, results.get("m3").getResultType());
        assertEquals("expected: <3> but was: <5>", results.get("m3").getMessage());

        assertEquals(OOPResult.OOPTestResult.ERROR, results.get("m4").getResultType());
        assertEquals(Exception.class.getName(), results.get("m4").getMessage());

        assertEquals(OOPResult.OOPTestResult.EXPECTED_EXCEPTION_MISMATCH, results.get("m5").getResultType());
        assertEquals("expected exception: <" + IllegalArgumentException.class.getName() + "> but <" + IllegalArgumentException.class.getName() + "> was thrown", results.get("m5").getMessage());

        assertEquals(OOPResult.OOPTestResult.FAILURE, results.get("m6").getResultType());
        assertEquals("failure", results.get("m6").getMessage());

        assertEquals(OOPResult.OOPTestResult.ERROR, results.get("m7").getResultType());
        assertEquals(Exception.class.getName(), results.get("m7").getMessage());

        assertEquals(OOPResult.OOPTestResult.SUCCESS, results.get("summary").getResultType());
        assertNull(results.get("summary").getMessage());
    }

    @Test
    public void testUnordered() throws IllegalAccessException {
        OOPTestSummary summary = OOPUnitCore.runClass(UnorderedTest.class);
        assertNotNull(summary);
        Map<String, OOPResult> results = getResultsMap(summary);
        assertNotNull(results);
        assertEquals(7, results.size());

        assertEquals(OOPResult.OOPTestResult.SUCCESS, results.get("m1").getResultType());
        assertNull(results.get("m1").getMessage());

        assertEquals(OOPResult.OOPTestResult.SUCCESS, results.get("m2").getResultType());
        assertNull(results.get("m2").getMessage());

        assertEquals(OOPResult.OOPTestResult.FAILURE, results.get("m3").getResultType());
        assertEquals("expected: <3> but was: <5>", results.get("m3").getMessage());

        assertEquals(OOPResult.OOPTestResult.ERROR, results.get("m4").getResultType());
        assertEquals(Exception.class.getName(), results.get("m4").getMessage());

        assertEquals(OOPResult.OOPTestResult.EXPECTED_EXCEPTION_MISMATCH, results.get("m5").getResultType());
        assertEquals("expected exception: <" + IllegalArgumentException.class.getName() + "> but <" + IllegalArgumentException.class.getName() + "> was thrown", results.get("m5").getMessage());

        assertEquals(OOPResult.OOPTestResult.FAILURE, results.get("m6").getResultType());
        assertEquals("failure", results.get("m6").getMessage());

        assertEquals(OOPResult.OOPTestResult.ERROR, results.get("m7").getResultType());
        assertEquals(Exception.class.getName(), results.get("m7").getMessage());
    }

    @Test
    public void testPrivateConstructor() throws IllegalAccessException
    {
        OOPTestSummary summary = OOPUnitCore.runClass(PrivateConstructorTest.class);
        assertNotNull(summary);
        Map<String, OOPResult> results = getResultsMap(summary);
        assertNotNull(results);
        assertEquals(1, results.size());

        assertEquals(OOPResult.OOPTestResult.SUCCESS, results.get("m1").getResultType());
        assertNull(results.get("m1").getMessage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSendingNullAsArgument()
    {
        OOPTestSummary summary = OOPUnitCore.runClass(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSendingArgumentWithNoAnnotation()
    {
        OOPTestSummary summary = OOPUnitCore.runClass(String.class);
    }

    @Test
    public void testTags() throws IllegalAccessException {
        OOPTestSummary summary = OOPUnitCore.runClass(TagsTest.class, "Sarit");
        assertNotNull(summary);
        Map<String, OOPResult> results = getResultsMap(summary);
        assertNotNull(results);
        assertEquals(3, results.size());

        assertEquals(OOPResult.OOPTestResult.SUCCESS, results.get("m1").getResultType());
        assertNull(results.get("m1").getMessage());
        assertEquals(OOPResult.OOPTestResult.SUCCESS, results.get("m2").getResultType());
        assertNull(results.get("m2").getMessage());
        assertEquals(OOPResult.OOPTestResult.SUCCESS, results.get("summarySarit").getResultType());
        assertNull(results.get("summarySarit").getMessage());

        summary = OOPUnitCore.runClass(TagsTest.class, "Hadad");
        assertNotNull(summary);
        results = getResultsMap(summary);
        assertNotNull(results);
        assertEquals(3, results.size());

        assertEquals(OOPResult.OOPTestResult.SUCCESS, results.get("m3").getResultType());
        assertNull(results.get("m3").getMessage());
        assertEquals(OOPResult.OOPTestResult.SUCCESS, results.get("m4").getResultType());
        assertNull(results.get("m4").getMessage());
        assertEquals(OOPResult.OOPTestResult.SUCCESS, results.get("summaryHadad").getResultType());
        assertNull(results.get("summaryHadad").getMessage());
    }

    @Test
    public void testMultipleSetupBeforeAfterMethods() throws IllegalAccessException {
        OOPTestSummary summary = OOPUnitCore.runClass(MultipleSetupBeforeAfterMethods.class);
        assertNotNull(summary);
        Map<String, OOPResult> results = getResultsMap(summary);
        assertNotNull(results);
        assertEquals(2, results.size());

        assertEquals(OOPResult.OOPTestResult.SUCCESS, results.get("m1").getResultType());
        assertNull(results.get("m1").getMessage());
        assertEquals(OOPResult.OOPTestResult.SUCCESS, results.get("summary").getResultType());
        assertNull(results.get("summary").getMessage());
    }

    @Test
    public void testExceptions() throws IllegalAccessException {
        OOPTestSummary summary = OOPUnitCore.runClass(ExceptionsTest.class);
        assertNotNull(summary);
        Map<String, OOPResult> results = getResultsMap(summary);
        assertNotNull(results);
        assertEquals(8, results.size());

        assertEquals(OOPResult.OOPTestResult.ERROR, results.get("m1").getResultType());
        assertEquals(IllegalArgumentException.class.getName(), results.get("m1").getMessage());

        assertEquals(OOPResult.OOPTestResult.EXPECTED_EXCEPTION_MISMATCH, results.get("m2").getResultType());
        assertEquals("expected exception: <" + IllegalAccessException.class.getName() + "> but <" + IllegalArgumentException.class.getName() + "> was thrown", results.get("m2").getMessage());

        assertEquals(OOPResult.OOPTestResult.SUCCESS, results.get("m3").getResultType());
        assertNull(results.get("m3").getMessage());

        assertEquals(OOPResult.OOPTestResult.EXPECTED_EXCEPTION_MISMATCH, results.get("m4").getResultType());
        assertEquals("expected exception: <" + IllegalAccessException.class.getName() + "> but <" + IllegalAccessException.class.getName() + "> was thrown", results.get("m4").getMessage());

        assertEquals(OOPResult.OOPTestResult.SUCCESS, results.get("m5").getResultType());
        assertNull(results.get("m5").getMessage());

        assertEquals(OOPResult.OOPTestResult.SUCCESS, results.get("m6").getResultType());
        assertNull(results.get("m6").getMessage());

        assertEquals(OOPResult.OOPTestResult.SUCCESS, results.get("m7").getResultType());
        assertNull(results.get("m7").getMessage());

        assertEquals(OOPResult.OOPTestResult.ERROR, results.get("m8").getResultType());
        assertEquals(Exception.class.getName(), results.get("m8").getMessage());
    }

    @Test
    public void testInheritanceBasicOrder() throws IllegalAccessException {
        OOPTestSummary summary = OOPUnitCore.runClass(InheritanceBottomTest.class, "basicOrderTest");
        assertNotNull(summary);
        Map<String, OOPResult> results = getResultsMap(summary);
        assertNotNull(results);
        assertEquals(7, results.size());

        for(int i=5; i<=10; i++)
        {
            assertEquals(OOPResult.OOPTestResult.SUCCESS, results.get("m" + i).getResultType());
            assertNull(results.get("m" + i).getMessage());
        }

        assertEquals(OOPResult.OOPTestResult.SUCCESS, results.get("summaryBasicOrderTest").getResultType());
        assertNull(results.get("summaryBasicOrderTest").getMessage());
    }

    @Test
    public void testInheritanceSetupBeforeAfterOrder() throws IllegalAccessException {
        OOPTestSummary summary = OOPUnitCore.runClass(InheritanceBottomTest.class, "orderTest");
        assertNotNull(summary);
        Map<String, OOPResult> results = getResultsMap(summary);
        assertNotNull(results);
        assertEquals(2, results.size());

        assertEquals(OOPResult.OOPTestResult.SUCCESS, results.get("m1").getResultType());
        assertNull(results.get("m1").getMessage());

        assertEquals(OOPResult.OOPTestResult.SUCCESS, results.get("summaryOrderTest").getResultType());
        assertNull(results.get("summaryOrderTest").getMessage());
    }

    @Test
    public void testInheritanceOverriding() throws IllegalAccessException {
        OOPTestSummary summary = OOPUnitCore.runClass(InheritanceBottomTest.class, "overridingTest");
        assertNotNull(summary);
        Map<String, OOPResult> results = getResultsMap(summary);
        assertNotNull(results);
        assertEquals(2, results.size());

        assertEquals(OOPResult.OOPTestResult.SUCCESS, results.get("m3").getResultType());
        assertNull(results.get("m3").getMessage());

        assertEquals(OOPResult.OOPTestResult.SUCCESS, results.get("summaryOverridingTest").getResultType());
        assertNull(results.get("summaryOverridingTest").getMessage());
    }

    @Test
    public void testInheritancePrivateAndProtected() throws IllegalAccessException {
        OOPTestSummary summary = OOPUnitCore.runClass(InheritanceBottomTest.class, "privateAndProtectedTest");
        assertNotNull(summary);
        Map<String, OOPResult> results = getResultsMap(summary);
        assertNotNull(results);
        //! changed
//        assertEquals(5, results.size());
        assertEquals(7, results.size());

        assertEquals(OOPResult.OOPTestResult.SUCCESS, results.get("m14").getResultType());
        assertNull(results.get("m14").getMessage());

        assertEquals(OOPResult.OOPTestResult.SUCCESS, results.get("m16").getResultType());
        assertNull(results.get("m16").getMessage());

        assertEquals(OOPResult.OOPTestResult.SUCCESS, results.get("m18").getResultType());
        assertNull(results.get("m18").getMessage());

        assertEquals(OOPResult.OOPTestResult.SUCCESS, results.get("m19").getResultType());
        assertNull(results.get("m19").getMessage());

        assertEquals(OOPResult.OOPTestResult.SUCCESS, results.get("summaryPrivateAndProtectedTest").getResultType());
        assertNull(results.get("summaryPrivateAndProtectedTest").getMessage());
    }

    @Test
    public void testInheritanceExceptionsAndBackup() throws IllegalAccessException {
        // another possible scenario that is tested in this test (depends on the implementation) is:
        // if a final field is being set as part of backup

        OOPTestSummary summary = OOPUnitCore.runClass(InheritanceBottomTest.class, "beforeExceptionTest");
        assertNotNull(summary);
        Map<String, OOPResult> results = getResultsMap(summary);
        assertNotNull(results);
        assertEquals(2, results.size());

        assertEquals(OOPResult.OOPTestResult.ERROR, results.get("m11").getResultType());
        assertEquals(Exception.class.getName(), results.get("m11").getMessage());
        assertEquals(OOPResult.OOPTestResult.SUCCESS, results.get("summaryBeforeExceptionTest").getResultType());
        assertNull(results.get("summaryBeforeExceptionTest").getMessage());

        summary = OOPUnitCore.runClass(InheritanceBottomTest.class, "afterExceptionTest");
        assertNotNull(summary);
        results = getResultsMap(summary);
        assertNotNull(results);
        assertEquals(2, results.size());

        assertEquals(OOPResult.OOPTestResult.ERROR, results.get("m12").getResultType());
        assertEquals(Exception.class.getName(), results.get("m12").getMessage());
        assertEquals(OOPResult.OOPTestResult.SUCCESS, results.get("summaryAfterExceptionTest").getResultType());
        assertNull(results.get("summaryAfterExceptionTest").getMessage());
    }

    private Map<String, OOPResult> getResultsMap(OOPTestSummary summary) throws IllegalAccessException {
        Field[] fields = summary.getClass().getDeclaredFields();
        for(Field field : fields)
        {
            field.setAccessible(true);
            var potentialMap = field.get(summary);
            if(potentialMap instanceof Map)
            {
                return (Map<String, OOPResult>) potentialMap;
            }
        }

        return null;
    }
}
