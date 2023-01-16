package OOP.Tests.ClassesForTests;

import OOP.Provided.OOPExpectedException;
import OOP.Solution.*;
import static OOP.Solution.OOPUnitCore.assertEquals;
import static OOP.Solution.OOPUnitCore.fail;

@OOPTestClass(value= OOPTestClass.OOPTestClassType.ORDERED)
public class OrderedTest {
    @OOPExceptionRule
    private OOPExpectedException expected = OOPExpectedExceptionImpl.none();
    private String orderString = "";
    private int setupTimes = 0;
    private String beforeString = "";
    private String afterString = "";
    private int method = 1;

    @OOPSetup
    public void setup()
    {
        setupTimes++;
    }

    @OOPBefore({"m1", "m2", "m3"})
    public void before()
    {
        beforeString = beforeString + Integer.toString(method);
        method++;
    }

    @OOPAfter({"m4", "m5", "m6"})
    public void after()
    {
        afterString = afterString + Integer.toString(method);
        method++;
    }

    @OOPTest(order=5)
    public void m5()
    {
        orderString = orderString + "5";
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("yeah").expectMessage("said");
        throw new IllegalArgumentException("yeah, I sai it");
    }

    @OOPTest(order=2)
    public void m2()
    {
        orderString = orderString + "2";
    }

    @OOPTest(order=4)
    public void m4() throws Exception
    {
        orderString = orderString + "4";
        throw new Exception();
    }

    @OOPTest(order=1)
    public void m1()
    {
        orderString = orderString + "1";
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("yeah").expectMessage("said");
        throw new IllegalArgumentException("yeah, I said it");
    }

    @OOPTest(order=3)
    public void m3()
    {
        orderString = orderString + "3";
        assertEquals(3, 5);
    }

    @OOPTest(order=6)
    public void m6()
    {
        orderString = orderString + "6";
        fail();
    }

    @OOPTest(order=7)
    public void m7()
    {
        orderString = orderString + "7";
        expected.expect(Exception.class);
    }

    @OOPTest(order=8)
    public void summary()
    {
        // this is for testing the order of the methods
        assertEquals("1234567", orderString);
        // this is for testing that the setup method is called once
        assertEquals(1, setupTimes);
        // this is for testing that the before method is called only when defined
        assertEquals("123", beforeString);
        // this is for testing that the after method is called only when defined
        assertEquals("456", afterString);
    }
}
