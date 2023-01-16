package OOP.Solution;

import java.util.Map;
import java.util.TreeMap;

import OOP.Provided.*;

public class OOPTestSummary {
    private Map<String, OOPResult> results;
    private int SuccessCount;
    private int FailureCount;
    private int ErrorCount;
    private int ExceptionMismatchCount;


    public OOPTestSummary(Map<String, OOPResult> results) {
        this.results = results;
        results.forEach((x, result) -> {
            switch (result.getResultType()) {
                case SUCCESS:
                    SuccessCount++;
                    break;
                case FAILURE:
                    FailureCount++;
                    break;
                case ERROR:
                    ErrorCount++;
                    break;
                case EXPECTED_EXCEPTION_MISMATCH:
                    ExceptionMismatchCount++;
                    break;
            }
        });
    }

    public int getNumSuccesses() {
        return SuccessCount;
    }

    public int getNumFailures() {
        return FailureCount;
    }

    public int getNumErrors() {
        return ErrorCount;
    }

    public int getNumExceptionMismatches() {
        return ExceptionMismatchCount;
    }
}
