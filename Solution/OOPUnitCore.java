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
                Object value = field.get(classInst);
                if (value instanceof Cloneable) {
                    try {
                        backedUpList.add(value.getClass().getMethod("clone").invoke(value));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                }
                // check if value class has copy constructor
                else {
                    try {
                        if (value.getClass().getDeclaredConstructor(value.getClass()) != null) {
                            backedUpList.add(value.getClass().getDeclaredConstructor(value.getClass()).newInstance(value));
                        }
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    } catch (InstantiationException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
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
        allMethods.stream().filter(method -> method.isAnnotationPresent(OOPAfter.class)).forEach(method -> {
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

    public static <OOPEXpectedException> OOPTestSummary runClass(Class<?> testClass) throws IllegalArgumentException {
        return runClass(testClass, "");
    }

    public static <OOPEXpectedException> OOPTestSummary runClass(Class<?> testClass, String tag) throws IllegalArgumentException {
        // Check if the class is a test class
        if (testClass == null || tag == null || !testClass.isAnnotationPresent(OOPTestClass.class)) {
            throw new IllegalArgumentException();
        }

        // Create a new summary
        Map<String, OOPResult> summary = new TreeMap<>(String, OOPResult);


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
            method.invoke(finalTestClassInstance);
        });

        // get test methods
        ArrayList<Method> testMethods = allMethods;
        Collections.reverse(testMethods);
        testMethods.stream().filter(method -> method.isAnnotationPresent(OOPTest.class) && (tag.equals("") || method.getAnnotation(OOPTest.class).tag().equals(tag)));

        if (testClass.getAnnotation(OOPTestClass.class).value() == OOPTestClass.OOPTestClassType.ORDERED) {
            testMethods = testMethods.stream().sorted(Comparator.comparingInt(method -> method.getAnnotation(OOPTest.class).order())).collect(Collectors.toCollection(ArrayList::new));
        }

        Object finalTestClassInstance1 = testClassInstance;
        Object finalTestClassInstance2 = testClassInstance;
        testMethods.forEach(method -> {
            try {
                AtomicReference<OOPEXpectedException> expectedException;
                testClass.getDeclaredFields().stream()
                        .filter(field -> field.isAnnotationPresent(OOPExpectedException.class))
                        .forEach(field -> {
                            field.setAccessible(true);
                            expectedException = field.get(finalTestClassInstance1);
                        });
                // invoke before methods
                invokeBeforeMethods(allMethods, finalTestClassInstance2, method);

                ArrayList<Object> fields = new ArrayList<>();
                backup(finalTestClassInstance2, fields);
                // invoke test method
                try {
                    method.invoke(finalTestClassInstance2);
                } catch (Exception e) {
                    restore(finalTestClassInstance2, fields);
                    throw e;
                }

                if (expectedException.get() != null && expectedException.getExpectedException() != null) {
                    summary.put(method.getName(), new OOPResultImpl(OOPTestResult.ERROR, expectedException.getClass().getName()));
                } else {
                    summary.put(method.getName(), new OOPResultImpl(OOPTestResult.SUCCESS, null));
                }
                // invoke after methods
                invokeAfterMethods(allMethods, finalTestClassInstance2, method);

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

        });
        return OOPTestSummary(summary);
    }

}
