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
        return this.stats.get(OOPResult.OOPTestResult.SUCCESS);
    }

    public int getNumFailures() {
        return this.stats.get(OOPResult.OOPTestResult.FAILURE);
    }

    public int getNumErrors() {
        return this.stats.get(OOPResult.OOPTestResult.ERROR);
    }

    public int getNumExceptionMismatches() {
        return this.stats.get(OOPResult.OOPTestResult.EXPECTED_EXCEPTION_MISMATCH);
    }
}
