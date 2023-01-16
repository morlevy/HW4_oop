package OOP.Solution;

import java.util.Map;
import java.util.TreeMap;

import OOP.Provided.*;

public class OOPTestSummary {
    private Map<OOPResult.OOPTestResult, Integer> stats = new TreeMap<OOPResult.OOPTestResult, Integer>();

    public OOPTestSummary(Map<String, OOPResult> results) {
        results.forEach((x, result) -> {
            if (stats.containsKey(result.getResultType())) {
                stats.put(result.getResultType(), stats.get(result.getResultType()) + 1);
            } else {
                stats.put(result.getResultType(), 1);
            }
        });
    }

    public int getNumSuccesses() {
        if (stats.containsKey(OOPResult.OOPTestResult.SUCCESS)) {
            return stats.get(OOPResult.OOPTestResult.SUCCESS);
        }
        return 0;
    }

    public int getNumFailures() {
        if (stats.containsKey(OOPResult.OOPTestResult.FAILURE)) {
            return stats.get(OOPResult.OOPTestResult.FAILURE);
        }
        return 0;
    }

    public int getNumErrors() {
        if (stats.containsKey(OOPResult.OOPTestResult.ERROR)) {
            return stats.get(OOPResult.OOPTestResult.ERROR);
        }
        return 0;
    }

    public int getNumExceptionMismatches() {
        if (stats.containsKey(OOPResult.OOPTestResult.EXPECTED_EXCEPTION_MISMATCH)) {
            return stats.get(OOPResult.OOPTestResult.EXPECTED_EXCEPTION_MISMATCH);
        }
        return 0;
    }
}
