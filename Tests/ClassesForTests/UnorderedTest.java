package OOP.Tests.ClassesForTests;

import OOP.Provided.OOPExpectedException;
import OOP.Solution.*;
import static OOP.Solution.OOPUnitCore.assertEquals;
import static OOP.Solution.OOPUnitCore.fail;

@OOPTestClass(value= OOPTestClass.OOPTestClassType.UNORDERED)
public class UnorderedTest {
    @OOPExceptionRule
    private final OOPExpectedException expected = OOPExpectedExceptionImpl.none();

    @OOPSetup
    public void setup() {}

    @OOPBefore({"m1", "m2", "m3"})
    public void before() {}

    @OOPAfter({"m4", "m5", "m6"})
    public void after() {}

    @OOPTest(order=5)
    public void m5()
    {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("yeah").expectMessage("said");
        throw new IllegalArgumentException("yeah, I sai it");
    }

    @OOPTest(order=2)
    public void m2() {}

    @OOPTest(order=4)
    public void m4() throws Exception
    {
        throw new Exception();
    }

    @OOPTest(order=1)
    public void m1()
    {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage("yeah").expectMessage("said");
        throw new IllegalArgumentException("yeah, I said it");
    }

    @OOPTest(order=3)
    public void m3()
    {
        assertEquals(3, 5);
    }

    @OOPTest(order=6)
    public void m6()
    {
        fail();
    }

    @OOPTest(order=7)
    public void m7()
    {
        expected.expect(Exception.class);
    }
}
