package OOP.Tests.ClassesForTests;

import OOP.Solution.*;
import static OOP.Solution.OOPUnitCore.assertEquals;

@OOPTestClass(value= OOPTestClass.OOPTestClassType.ORDERED)
public class InheritanceTopTest {
    protected String basicOrderTest = "";

    protected String setupOrderTest = "";
    protected String beforeOrderTest = "";
    protected String afterOrderTest = "";

    protected String setupOverridingTest = "";
    protected String beforeOverridingTest = "";
    protected String afterOverridingTest = "";

    protected String setupPrivateTest = "";
    protected String setupProtectedTest = "";
    protected String beforePrivateTest = "";
    protected String beforeProtectedTest = "";
    protected String afterPrivateTest = "";
    protected String afterProtectedTest = "";

    @OOPSetup
    public void setupOverridingTest()
    {
        setupOverridingTest = setupOverridingTest + "Top";
    }

    @OOPBefore({"m3"})
    public void beforeOverridingTest()
    {
        beforeOverridingTest = beforeOverridingTest + "Top";
    }

    @OOPAfter({"m3"})
    public void afterOverridingTest()
    {
        afterOverridingTest = afterOverridingTest + "Top";
    }

    @OOPSetup
    public void setupOrderTestTop()
    {
        setupOrderTest = setupOrderTest + "Top";
    }

    @OOPBefore({"m1"})
    public void beforeOrderTestTop()
    {
        beforeOrderTest = beforeOrderTest + "Top";
    }

    @OOPAfter({"m1"})
    public void afterOrderTestTop()
    {
        afterOrderTest = afterOrderTest + "Top";
    }

    @OOPSetup
    private void setupPrivateTestTop()
    {
        setupPrivateTest = setupPrivateTest + "Top";
    }

    @OOPSetup
    protected void setupProtectedTestTop()
    {
        setupProtectedTest = setupProtectedTest + "Top";
    }

    @OOPBefore({"m19"})
    private void beforePrivateTestTop()
    {
        beforePrivateTest = beforePrivateTest + "Top";
    }

    @OOPBefore({"m19"})
    protected void beforeProtectedTestTop()
    {
        beforeProtectedTest = beforeProtectedTest + "Top";
    }

    @OOPAfter({"m19"})
    private void afterPrivateTestTop()
    {
        afterPrivateTest = afterPrivateTest + "Top";
    }

    @OOPAfter({"m19"})
    protected void afterProtectedTestTop()
    {
        afterProtectedTest = afterProtectedTest + "Top";
    }


    //* Test Methods *//


    @OOPTest(order=1, tag="orderTest")
    public void m1() {}

    @OOPTest(order=2, tag="orderTest")
    public void summaryOrderTest()
    {
        assertEquals("TopMiddleBottom", beforeOrderTest);
        assertEquals("TopMiddleBottom", setupOrderTest);
        assertEquals("BottomMiddleTop", afterOrderTest);
    }

    @OOPTest(order=3, tag="overridingTest")
    public void m3() {}

    @OOPTest(order=4, tag="overridingTest")
    public void summaryOverridingTest()
    {
        assertEquals("Bottom", setupOverridingTest);
        assertEquals("Bottom", beforeOverridingTest);
        assertEquals("Bottom", afterOverridingTest);
    }

    @OOPTest(order=9, tag="basicOrderTest")
    public void m9()
    {
        basicOrderTest = basicOrderTest + "9";
    }

    @OOPTest(order=5, tag="basicOrderTest")
    public void m5()
    {
        basicOrderTest = basicOrderTest + "5";
    }

    @OOPTest(order=17, tag="privateAndProtectedTest")
    private void m17() {}

    @OOPTest(order=18, tag="privateAndProtectedTest")
    protected void m18() {}
}
