package OOP.Tests.ClassesForTests;

import OOP.Solution.*;
import static OOP.Solution.OOPUnitCore.assertEquals;

@OOPTestClass(value= OOPTestClass.OOPTestClassType.ORDERED)
public class MultipleSetupBeforeAfterMethods {
    private boolean setup1bool = false;
    private boolean setup2bool = false;
    private boolean setup3bool = false;
    private boolean before1bool = false;
    private boolean before2bool = false;
    private boolean before3bool = false;
    private boolean after1bool = false;
    private boolean after2bool = false;
    private boolean after3bool = false;
    
    @OOPSetup
    public void setup1()
    {
        setup1bool = true;
    }

    @OOPSetup
    public void setup2()
    {
        setup2bool = true;
    }

    @OOPSetup
    private void setup3()
    {
        setup3bool = true;
    }

    @OOPBefore({"m1"})
    public void before1()
    {
        before1bool = true;
    }

    @OOPBefore({"m1"})
    public void before2()
    {
        before2bool = true;
    }

    @OOPBefore({"m1"})
    private void before3()
    {
        before3bool = true;
    }

    @OOPAfter({"m1"})
    public void after1()
    {
        after1bool = true;
    }

    @OOPAfter({"m1"})
    public void after2()
    {
        after2bool = true;
    }

    @OOPAfter({"m1"})
    private void after3()
    {
        after3bool = true;
    }
    
    @OOPTest(order=1)
    public void m1() {}

    @OOPTest(order=2)
    public void summary()
    {
        assertEquals(true, setup1bool);
        assertEquals(true, setup2bool);
        assertEquals(true, setup3bool);
        assertEquals(true, before1bool);
        assertEquals(true, before2bool);
        assertEquals(true, before3bool);
        assertEquals(true, after1bool);
        assertEquals(true, after2bool);
        assertEquals(true, after3bool);
    }
}
