package OOP.Tests.ClassesForTests;

import OOP.Solution.OOPTest;
import OOP.Solution.OOPTestClass;

@OOPTestClass(value= OOPTestClass.OOPTestClassType.UNORDERED)
public class PrivateConstructorTest {
    private PrivateConstructorTest() {}

    @OOPTest(order=1)
    public void m1() {}
}
