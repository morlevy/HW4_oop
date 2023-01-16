package OOP.Tests.ClassesForTests;

import OOP.Provided.OOPExpectedException;
import OOP.Solution.OOPExceptionRule;
import OOP.Solution.OOPExpectedExceptionImpl;
import OOP.Solution.OOPTest;
import OOP.Solution.OOPTestClass;

@OOPTestClass(value= OOPTestClass.OOPTestClassType.UNORDERED)
public class ExceptionsTest {
    @OOPExceptionRule
    private final OOPExpectedException expected = OOPExpectedExceptionImpl.none();

    @OOPTest(order=1)
    public void m1()
    {
        // ERROR
        throw new IllegalArgumentException("yeah, I sai it");
    }

    @OOPTest(order=2)
    public void m2()
    {
        // MISMATCH
        expected.expect(IllegalAccessException.class);
        throw new IllegalArgumentException("yeah, I sai it");
    }

    @OOPTest(order=3)
    public void m3() throws IllegalAccessException {
        // SUCCESS
        expected.expect(IllegalAccessException.class);
        throw new IllegalAccessException("yeah, I said it");
    }

    @OOPTest(order=4)
    public void m4() throws IllegalAccessException {
        // MISMATCH
        expected.expect(IllegalAccessException.class);
        expected.expectMessage("yeah").expectMessage("said");
        throw new IllegalAccessException("yeah, I sai it");
    }

    @OOPTest(order=5)
    public void m5() throws IllegalAccessException {
        // SUCCESS
        expected.expect(IllegalAccessException.class);
        expected.expectMessage("yeah").expectMessage("said");
        throw new IllegalAccessException("yeah, I said it");
    }

    @OOPTest(order=6)
    public void m6() throws IllegalAccessException {
        // SUCCESS
        expected.expect(IllegalAccessException.class);
        expected.expectMessage("");
        throw new IllegalAccessException("yeah, I said it");
    }

    @OOPTest(order=7)
    public void m7() throws IllegalAccessException {
        // SUCCESS
        expected.expect(Exception.class);
        throw new IllegalAccessException("yeah, I said it");
    }

    @OOPTest(order=8)
    public void m8() throws IllegalAccessException {
        // ERROR
        expected.expect(Exception.class);
    }
}
