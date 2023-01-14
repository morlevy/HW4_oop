package Solution.OOP;

import java.util.Map;
import java.util.TreeMap;

import OOP.Provided.OOPResult;

public class OOPTestSummary {
    private Map<OOPTestResult, Integer> stats = new TreeMap<OOPTestResult, Integer>() {
        {
            put(OOPTestResult.SUCCESS, 0);
            put(OOPTestResult.FAILURE, 0);
            put(OOPTestResult.ERROR, 0);
            put(OOPTestResult.EXPECTED_EXCEPTION_MISMATCH, 0);
        }
    };
}

    public OOPTestSummary(Map<String, OOPResult> results) {
        results.forEach((_, result) -> {
            this.stats[result]++;
        });
    }

    public int getNumSuccesses() {
        return this.stats[OOPTestResult.SUCCESS];
    }

    public int getNumFailures() {
        return this.stats[OOPTestResult.FAILURE];
    }

    public int getNumErrors() {
        return this.stats[OOPTestResult.ERROR];
    }

    public int getNumExceptionMismatches() {
        return this.stats[OOPTestResult.EXPECTED_EXCEPTION_MISMATCH];
    }
}
