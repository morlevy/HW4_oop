package OOP.Solution;

import OOP.Provided.OOPResult;

public class OOPResultImpl implements OOPResult {
    private String message;
    private OOPTestResult resultType;

    public OOPResultImpl(OOPTestResult resultType, String message) {
        this.resultType = resultType;
        this.message = message;
    }

    @Override
    public OOPTestResult getResultType() {
        return this.resultType;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        OOPResultImpl other = (OOPResultImpl) obj;
        return this.resultType == other.resultType && this.message.equals(other.message);
    }
}
