package OOP.Tests.ClassesForTests;

public class HasCopyConstructorHelper {
    private int x;

    public HasCopyConstructorHelper()
    {
        x = 0;
    }

    public HasCopyConstructorHelper(HasCopyConstructorHelper a)
    {
        x = a.getX();
    }

    public int getX()
    {
        return x;
    }

    public void setX(int a)
    {
        x = a;
    }
}
