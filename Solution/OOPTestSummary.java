package OOP.Solution;

import java.util.Map;
import java.util.TreeMap;

import OOP.Provided.*;

public class OOPTestSummary {
    private int successesCount = 0;
    private int failuresCount = 0;
    private int exceptionMismatchesCount = 0;
    private int errorsCount = 0;

    public OOPTestSummary(Map<String, OOPResult> testMap) {
        testMap.forEach(
                (key, value) -> {
                    switch (value.getResultType()) {
                        case SUCCESS:
                            successesCount++;
                            break;
                        case FAILURE:
                            failuresCount++;
                            break;
                        case EXPECTED_EXCEPTION_MISMATCH:
                            exceptionMismatchesCount++;
                            break;
                        case ERROR:
                            errorsCount++;
                            break;
                    }
                }
        );
    }

    public int getNumSuccesses() {
        return successesCount;
    }

    public int getNumFailures() {
        return failuresCount;
    }

    public int getNumErrors() {
        return errorsCount;
    }

    public int getNumExceptionMismatches() {
        return exceptionMismatchesCount;
    }
}
