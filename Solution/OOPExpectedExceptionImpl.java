package OOP.Solution;

import OOP.Provided.OOPExpectedException;

public class OOPExpectedExceptionImpl implements OOPExpectedException {
    private Class<? extends Exception> expectedException;
    private String expectedMessage;

    private OOPExpectedExceptionImpl() {
        this.expectedException = null;
        this.expectedMessage = "";
    }

    @Override
    public Class<? extends Exception> getExpectedException() {
        return this.expectedException;
    }

    @Override
    public OOPExpectedException expect(Class<? extends Exception> expectedException) {
        this.expectedException = expectedException;
        return this;
    }

    @Override
    OOPExpectedException expectMessage(String msg) {
        this.expectedMessage = msg;
        return this;
    }

    @Override
    boolean assertExpected(Exception e) {
        if (e == null && this.expectedException != null || this.expectedException == null) {
            return false;
        }
        if (this.expectedException.isInstance(e)) {
            return e.getMessage().contains(this.expectedMessage);
        }
        return false;
    }

    public static OOPExpectedException none() {
        return new OOPExpectedExceptionImpl();
    }
}
