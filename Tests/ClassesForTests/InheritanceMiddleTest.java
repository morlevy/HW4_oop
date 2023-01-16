package OOP.Tests.ClassesForTests;

import OOP.Solution.*;

@OOPTestClass(value= OOPTestClass.OOPTestClassType.ORDERED)
public class InheritanceMiddleTest extends InheritanceTopTest {
    @OOPSetup
    public void setupOverridingTest()
    {
        setupOverridingTest = setupOverridingTest + "Middle";
    }

    @OOPBefore({"m3"})
    public void beforeOverridingTest()
    {
        beforeOverridingTest = beforeOverridingTest + "Middle";
    }

    @OOPAfter({"m3"})
    public void afterOverridingTest()
    {
        afterOverridingTest = afterOverridingTest + "Middle";
    }

    @OOPSetup
    public void setupOrderTestMiddle()
    {
        setupOrderTest = setupOrderTest + "Middle";
    }

    @OOPBefore({"m1"})
    public void beforeOrderTestMiddle()
    {
        beforeOrderTest = beforeOrderTest + "Middle";
    }

    @OOPAfter({"m1"})
    public void afterOrderTestMiddle()
    {
        afterOrderTest = afterOrderTest + "Middle";
    }

    @OOPSetup
    private void setupPrivateTestMiddle()
    {
        setupPrivateTest = setupPrivateTest + "Middle";
    }

    @OOPSetup
    protected void setupProtectedTestMiddle()
    {
        setupProtectedTest = setupProtectedTest + "Middle";
    }

    @OOPBefore({"m19"})
    private void beforePrivateTestMiddle()
    {
        beforePrivateTest = beforePrivateTest + "Middle";
    }

    @OOPBefore({"m19"})
    protected void beforeProtectedTestMiddle()
    {
        beforeProtectedTest = beforeProtectedTest + "Middle";
    }

    @OOPAfter({"m19"})
    private void afterPrivateTestMiddle()
    {
        afterPrivateTest = afterPrivateTest + "Middle";
    }

    @OOPAfter({"m19"})
    protected void afterProtectedTestMiddle()
    {
        afterProtectedTest = afterProtectedTest + "Middle";
    }


    //* Test Methods *//


    @OOPTest(order=8, tag="basicOrderTest")
    public void m8()
    {
        basicOrderTest = basicOrderTest + "8";
    }

    @OOPTest(order=7, tag="basicOrderTest")
    public void m7()
    {
        basicOrderTest = basicOrderTest + "7";
    }

    @OOPTest(order=15, tag="privateAndProtectedTest")
    private void m15() {}

    @OOPTest(order=16, tag="privateAndProtectedTest")
    protected void m16() {}
}
