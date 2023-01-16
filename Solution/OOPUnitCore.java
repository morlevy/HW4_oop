package OOP.Solution;

import OOP.Provided.OOPAssertionFailure;
import OOP.Provided.OOPExpectedException;
import OOP.Provided.OOPResult;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


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
                Object value = null;
                try {
                    value = field.get(classInst);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (value instanceof Cloneable) {
                    try {
                        backedUpList.add(value.getClass().getMethod("clone").invoke(value));
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                }
                // check if value class has copy constructor
                else {
                    try {
                        // maybe  use an array of one element
                        assert value != null;
                        backedUpList.add(value.getClass().getDeclaredConstructor(value.getClass()).newInstance(value));
                    } catch (NoSuchMethodException e) {
                        backedUpList.add(value);
                    } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }

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
        allMethods.stream().filter(method -> method.isAnnotationPresent(OOPBefore.class)).forEach(method -> {
            ArrayList<Object> fields = new ArrayList<>();
            try {
                backup(testClassInstance, fields);
                method.invoke(testClassInstance);
            } catch (Exception e) {
                restore(testClassInstance, fields);
                try {
                    throw e;
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                } catch (InvocationTargetException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public static void invokeAfterMethods(ArrayList<Method> allMethods, Object testClassInstance, Method testMethod) {
        ArrayList<Method> afterMethods =  new ArrayList<>(allMethods);
        Collections.reverse(afterMethods);
        afterMethods.stream().filter(method -> method.isAnnotationPresent(OOPAfter.class)).forEach(method -> {
            ArrayList<Object> fields = new ArrayList<>();
            try {
                backup(testClassInstance, fields);
                method.invoke(testClassInstance);
            } catch (Exception e) {
                restore(testClassInstance, fields);
                try {
                    throw e;
                } catch (IllegalAccessException | InvocationTargetException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public static <OOPExpectedException> OOPTestSummary runClass(Class<?> testClass) throws IllegalArgumentException {
        return runClass(testClass, "");
    }

    public static <OOPExpectedException> OOPTestSummary runClass(Class<?> testClass, String tag) throws IllegalArgumentException {
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
        ArrayList<Method> allMethods = new ArrayList<Method>(List.of(testClass.getMethods()));
        ArrayList<Method> setupMethods = allMethods;
        Collections.reverse(setupMethods);
        Object finalTestClassInstance = testClassInstance;
        setupMethods.stream().filter(method -> method.isAnnotationPresent(OOPSetup.class)).forEach(method -> {
            try {
                method.invoke(finalTestClassInstance);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });

        // get test methods
        ArrayList<Method> testMethods = new ArrayList<>();
        //Collections.reverse(testMethods);
        allMethods.stream().filter(method -> method.isAnnotationPresent(OOPTest.class) && (tag.equals("") || method.getAnnotation(OOPTest.class).tag().equals(tag))).forEach(testMethods::add);

        if (testClass.getAnnotation(OOPTestClass.class).value() == OOPTestClass.OOPTestClassType.ORDERED) {
            testMethods = testMethods.stream().sorted(Comparator.comparingInt(method -> method.getAnnotation(OOPTest.class).order())).collect(Collectors.toCollection(ArrayList::new));
        }
        Object finalTestClassInstance1 = testClassInstance;
        Object finalTestClassInstance2 = testClassInstance;
        final Object[] expectedExceptions = {null};
        testMethods.forEach(method -> {
            try {

                Arrays.stream(testClass.getDeclaredFields())
                        .filter(field -> field.isAnnotationPresent(OOPExceptionRule.class))
                        .forEach(field -> {
                            field.setAccessible(true);
                            try {
                                expectedExceptions[0] = field.get(finalTestClassInstance1);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        });
                //expectedException = (OOPExpectedException) expectedExceptions[0];
                method.setAccessible(true);

                // invoke before methods
                Collections.reverse(allMethods);
                invokeBeforeMethods(allMethods, finalTestClassInstance2, method);

                ArrayList<Object> fields = new ArrayList<>();
                backup(finalTestClassInstance2, fields);
                // invoke test method
                try {
                    method.invoke(finalTestClassInstance2);
                } catch (InvocationTargetException e) {
                    restore(finalTestClassInstance2, fields);
                    throw e;
                }

                OOPExpectedException expectedException = (OOPExpectedException) expectedExceptions[0];
                if (expectedException != null && expectedException.getExpectedException() != null) {
                    summary.put(method.getName(), new OOPResult(OOPResult.OOPTestResult.ERROR, expectedException.getClass().getName()));
                } else {
                    summary.put(method.getName(), new OOPResult(OOPResult.OOPTestResult.SUCCESS, null));
                }
                // invoke after methods
                invokeAfterMethods(allMethods, finalTestClassInstance2, method);

            } catch (Exception e) {
                // add to summary
                OOPExpectedException expectedException = (OOPExpectedException) expectedExceptions[0];
                if (expectedException != null && expectedException.assertExpected((Exception) e.getCause())) {
                    summary.put(method.getName(), new OOPResultImpl(OOPResult.OOPTestResult.SUCCESS, e.getCause().getMessage()));
                } else {
                    if (e.getCause().getClass().equals(OOPAssertionFailure.class)) {
                        summary.put(method.getName(), new OOPResultImpl(OOPResult.OOPTestResult.FAILURE, e.getCause().getMessage()));
                    } else {
                        summary.put(method.getName(), new OOPResultImpl(OOPResult.OOPTestResult.EXPECTED_EXCEPTION_MISMATCH, e.getCause().getClass().getName()));
                    }
                }
            }

        });
        return new OOPTestSummary(summary);
    }

}
