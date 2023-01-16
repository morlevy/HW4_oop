package OOP.Tests.ClassesForTests;

import OOP.Solution.*;
import static OOP.Solution.OOPUnitCore.assertEquals;
import java.util.Date;

@OOPTestClass(value= OOPTestClass.OOPTestClassType.ORDERED)
public class InheritanceBottomTest extends InheritanceMiddleTest {
    // Date is cloneable
    // HasCopyConstructorHelper has copy constructor (no clone)
    // HasntCopyConstructorHelper isn't cloneable and hasn't copy constructor
    // all have different types of accessibility
    public Date exceptionTestDate = new Date(firstOfJan2021);
    protected HasCopyConstructorHelper exceptionTestCopyConstructor = new HasCopyConstructorHelper();
    private HasntCopyConstructorHelper exceptionTestHasNone = new HasntCopyConstructorHelper();

    static public final long firstOfJan2021 = 1609459200000L;
    static public final long thirdOfMar2024 = 1709459200000L;

    @OOPSetup
    public void setupOverridingTest()
    {
        setupOverridingTest = setupOverridingTest + "Bottom";
    }

    @OOPBefore({"m3"})
    public void beforeOverridingTest()
    {
        beforeOverridingTest = beforeOverridingTest + "Bottom";
    }

    @OOPAfter({"m3"})
    public void afterOverridingTest()
    {
        afterOverridingTest = afterOverridingTest + "Bottom";
    }

    @OOPSetup
    public void setupOrderTestBottom()
    {
        setupOrderTest = setupOrderTest + "Bottom";
    }

    @OOPBefore({"m1"})
    public void beforeOrderTestBottom()
    {
        beforeOrderTest = beforeOrderTest + "Bottom";
    }

    @OOPAfter({"m1"})
    public void afterOrderTestBottom()
    {
        afterOrderTest = afterOrderTest + "Bottom";
    }

    @OOPSetup
    private void setupPrivateTestBottom()
    {
        setupPrivateTest = setupPrivateTest + "Bottom";
    }

    @OOPSetup
    protected void setupProtectedTestBottom()
    {
        setupProtectedTest = setupProtectedTest + "Bottom";
    }

    @OOPBefore({"m19"})
    private void beforePrivateTestBottom()
    {
        beforePrivateTest = beforePrivateTest + "Bottom";
    }

    @OOPBefore({"m19"})
    protected void beforeProtectedTestBottom()
    {
        beforeProtectedTest = beforeProtectedTest + "Bottom";
    }

    @OOPAfter({"m19"})
    private void afterPrivateTestBottom()
    {
        afterPrivateTest = afterPrivateTest + "Bottom";
    }

    @OOPAfter({"m19"})
    protected void afterProtectedTestBottom()
    {
        afterProtectedTest = afterProtectedTest + "Bottom";
    }

    @OOPBefore({"m11"})
    public void beforeExceptionTestTop() throws Exception {
        // shouldn't affect the value since Date was cloned
        exceptionTestDate.setTime(thirdOfMar2024);

        // shouldn't affect the value since HasCopyConstructorHelper was copied to a new object
        exceptionTestCopyConstructor.setX(1);

        // should affect the value, since HasntCopyConstructorHelper's reference was saved
        exceptionTestHasNone.setX(1);

        // shouldn't affect the value, since HasntCopyConstructorHelper's old reference was saved
        exceptionTestHasNone = new HasntCopyConstructorHelper();

        throw new Exception();
    }

    @OOPAfter({"m12"})
    public void afterExceptionTestTop() throws Exception {
        // shouldn't affect the value since Date was cloned
        exceptionTestDate.setTime(thirdOfMar2024);

        // shouldn't affect the value since HasCopyConstructorHelper was copied to a new object
        exceptionTestCopyConstructor.setX(1);

        // should affect the value, since HasntCopyConstructorHelper's reference was saved
        exceptionTestHasNone.setX(1);

        // shouldn't affect the value, since HasntCopyConstructorHelper's old reference was saved
        exceptionTestHasNone = new HasntCopyConstructorHelper();

        throw new Exception();
    }


    //* Test Methods *//


    @OOPTest(order=10, tag="basicOrderTest")
    public void m10()
    {
        basicOrderTest = basicOrderTest + "10";
    }

    @OOPTest(order=6, tag="basicOrderTest")
    public void m6()
    {
        basicOrderTest = basicOrderTest + "6";
    }

    @OOPTest(order=11, tag="beforeExceptionTest")
    public void m11() {}

    @OOPTest(order=12, tag="afterExceptionTest")
    public void m12() {}

    @OOPTest(order=21, tag="beforeExceptionTest")
    public void summaryBeforeExceptionTest()
    {
        assertEquals(new Date(firstOfJan2021), exceptionTestDate);
        assertEquals(0, exceptionTestCopyConstructor.getX());
        assertEquals(1, exceptionTestHasNone.getX());
    }

    @OOPTest(order=22, tag="afterExceptionTest")
    public void summaryAfterExceptionTest()
    {
        assertEquals(new Date(firstOfJan2021), exceptionTestDate);
        assertEquals(0, exceptionTestCopyConstructor.getX());
        assertEquals(1, exceptionTestHasNone.getX());
    }

    @OOPTest(order=13, tag="basicOrderTest")
    public void summaryBasicOrderTest()
    {
        assertEquals("5678910", basicOrderTest);
    }

    @OOPTest(order=14, tag="privateAndProtectedTest")
    private void m14() {}

    @OOPTest(order=19, tag="privateAndProtectedTest")
    public void m19() {}

    @OOPTest(order=20, tag="privateAndProtectedTest")
    private void summaryPrivateAndProtectedTest()
    {
        //! changed
//        assertEquals("Bottom", beforePrivateTest);
//        assertEquals("Bottom", afterPrivateTest);
//        assertEquals("Bottom", setupPrivateTest);
        assertEquals("TopMiddleBottom", beforePrivateTest);
        assertEquals("BottomMiddleTop", afterPrivateTest);
        assertEquals("TopMiddleBottom", setupPrivateTest);

        assertEquals("TopMiddleBottom", beforeProtectedTest);
        assertEquals("BottomMiddleTop", afterProtectedTest);
        assertEquals("TopMiddleBottom", setupProtectedTest);
    }
}
