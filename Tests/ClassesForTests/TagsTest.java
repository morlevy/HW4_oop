package OOP.Tests.ClassesForTests;

import OOP.Solution.OOPTest;
import OOP.Solution.OOPTestClass;
import static OOP.Solution.OOPUnitCore.assertEquals;

@OOPTestClass(value= OOPTestClass.OOPTestClassType.ORDERED)
public class TagsTest {
    public String testString = "";

    @OOPTest(order=2, tag="Sarit")
    public void m2()
    {
        testString = testString + "Sarit2";
    }

    @OOPTest(order=1, tag="Sarit")
    public void m1()
    {
        testString = testString + "Sarit1";
    }

    @OOPTest(order=4, tag="Hadad")
    public void m4() {
        testString = testString + "Hadad2";
    }

    @OOPTest(order=3, tag="Hadad")
    public void m3() {
        testString = testString + "Hadad1";
    }

    @OOPTest(order=5, tag="Sarit")
    public void summarySarit() {
        assertEquals("Sarit1Sarit2", testString);
    }

    @OOPTest(order=6, tag="Hadad")
    public void summaryHadad() {
        assertEquals("Hadad1Hadad2", testString);
    }
}
