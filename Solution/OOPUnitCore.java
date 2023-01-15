package OOP.Solution;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import OOP.Provided.OOPAssertionFailure;
import OOP.Provided.OOPExceptionMismatchError;
import OOP.Provided.OOPExpectedException;
import OOP.Provided.OOPResult;
import OOP.Provided.OOPResult.OOPTestResult;

public class OOPUnitCore {
    private OOPUnitCore() {
    }

    public static void assertEquals(Object expected, Object actual) throws OOPAssertionFailure {
        if ((expected == null && actual != null) || !expected.equals(actual)) {
            throw new OOPAssertionFailure(expected, actual);
        }
    }

    public void fail() throws OOPAssertionFailure {
        throw new OOPAssertionFailure();
    }


    private static void backup(Object classInst, ArrayList<Object> backedUpList) {
        Class<?> clazz = classInst.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            java.util.Arrays.stream(fields).forEach(field -> {
                field.setAccessible(true);
                Object value = field.get(classInst);
                if (value instanceof Cloneable) {
                    backedUpList.add(value.getClass().getMethod("clone").invoke(value));
                }
                // check if value class has copy constructor
                else if (value.getClass().getDeclaredConstructor(value.getClass()) != null) {
                    backedUpList.add(value.getClass().getDeclaredConstructor(value.getClass()).newInstance(value));
                }
                backedUpList.add(value);
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void restore(Object classInst, ArrayList<Object> backedUpList) {
        Class<?> clazz = classInst.getClass();
        Field[] fields = clazz.getDeclaredFields();
        java.util.Arrays.stream(fields).forEach(field -> {
            field.setAccessible(true);
            try {
                field.set(classInst, backedUpList.remove(0));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }


    public static void invokeBeforeMethods(ArrayList<Method> allMethods, Object testClassInstance, Method testMethod) {
        allMethods.stream().filter(method -> method.isAnnotationPresent(Before.class)).forEach(method -> {
            try {
                ArrayList<Object> fields = new ArrayList<>();
                backup(testClassInstance, fields);
                method.invoke(testClassInstance);
            } catch (Exception e) {
                restore(testClassInstance, fields);
                throw e;
            }
        });
    }

    public static void invokeAfterMethods(ArrayList<Method> allMethods, Object testClassInstance, Method testMethod) {
        allMethods.stream().filter(method -> method.isAnnotationPresent(After.class)).forEach(method -> {
            try {
                ArrayList<Object> fields = new ArrayList<>();
                backup(testClassInstance, fields);
                method.invoke(testClassInstance);
            } catch (IllegalAccessException | InvocationTargetException e) {
                restore(testClassInstance, fields);
                throw e;
            }
        });
    }

    public static OOPTestSummary runClass(Class<?> testClass) throws IllegalArgumentException {
        return runClass(testClass, "");
    }


    public static OOPTestSummary runClass(Class<?> testClass, String tag) throws IllegalArgumentException {
        // Check if the class is a test class
        if (testClass == null || tag == null || !testClass.isAnnotationPresent(OOPTestClass.class)) {
            throw new IllegalArgumentException();
        }

        // Create a new summary
        Map<String, OOPResult> summary = new TreeMap<>();


        //create a new instance of the test class
        Object testClassInstance = null;
        try {
            Constructor<?> constructor = testClass.getConstructor();
            constructor.setAccessible(true);
            testClassInstance = constructor.newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException |
                 InvocationTargetException e) {
            e.printStackTrace();
        }
        // invoke setup methods
        ArrayList<Method> allMethods = testClass.getMethods();
        ArrayList<Method> setupMethods = allMethods.stream().reverse()
                .filter(method -> method.isAnnotationPresent(OOPSetup.class) && beforeMethod.getAnnotation(OOPBefore.class).value().equals(method.getName()));
        setupMethods.stream().forEach(method -> {
            method.invoke(testClassInstance);
        });

        // get test methods
        ArrayList<Method> testMethods = allMethods.stream().reverse()
                .filter(method -> method.isAnnotationPresent(OOPTest.class) && (tag == "" || method.getAnnotation(OOPTest.class).tag().equals(tag)));

        if (testClass.getAnnotation(OOPTestClass.class).value() == OOPTestClass.OOPTestClassType.ORDERED) {
            testMethods = testMethods.stream().sorted(Comparator.comparingInt(method -> method.getAnnotation(OOPTest.class).order()));
        }

        testMethods.forEach(method -> {
            try {
                OOPEXpectedException expectedException;
                testClass.getDeclaredFields().stream()
                        .filter(field -> field.isAnnotationPresent(OOPExpectedException.class))
                        .forEach(field -> {
                            field.setAccessible(true);
                            expectedException = field.get(testClassInstance);
                        });
                // invoke before methods
                invokeBeforeMethods(allMethods, testClassInstance, method);

                ArrayList<Object> fields = new ArrayList<>();
                backup(testClassInstance, fields);
                // invoke test method
                try {
                    method.invoke(testClassInstance);
                } catch (Exception e) {
                    restore(testClassInstance, fields);
                    throw e;
                }

                if (expectedException != null && expectedException.getExpectedException() != null) {
                    summary.put(method.getName(), new OOPResultImpl(OOPTestResult.ERROR, expectedException.getClass().getName()));
                } else {
                    summary.put(method.getName(), new OOPResultImpl(OOPTestResult.PASSED, null));
                }
                // invoke after methods
                invokeAfterMethods(allMethods, testClassInstance, method);

            } catch (IllegalAccessException | InvocationTargetException e) {
                // add to summary
                if (expectedException && e.getCause().getClass().equals(expectedException)) {
                    summary.put(method.getName(), new OOPResult(OOPTestResult.SUCCESS, e.getCause().getMessage()));
                } else {
                    if (e.getCause().getClass().equals(OOPAssertionFailure.class)) {
                        summary.put(method.getName(), new OOPResult(OOPTestResult.FAILURE, e.getCause().getMessage()));
                    } else {
                        summary.put(method.getName(), new OOPResult(OOPTestResult.EXPECTED_EXCEPTION_MISMATCH, e.getCause().getClass().getName()));
                    }
                }
            }
            return OOPTestSummary(summary);
        });
    }
}
