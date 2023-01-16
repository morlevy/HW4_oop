package OOP.Solution;

import OOP.Provided.*;


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

    public static void fail() throws OOPAssertionFailure {
        throw new OOPAssertionFailure();
    }


    private static void backup(Object classInst, ArrayList<Object> backedUpList) {
        Field[] fields = classInst.getClass().getDeclaredFields();
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
                if (field.isAnnotationPresent(OOPExceptionRule.class)) {
                    field.set(classInst, OOPExpectedExceptionImpl.none());
                    backedUpList.remove(0);
                } else {
                    field.set(classInst, backedUpList.remove(0));
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }


    public static void invokeBeforeMethods(ArrayList<Method> allMethods, Object testClassInstance, Method testMethod) {
        allMethods.stream().filter(method -> method.isAnnotationPresent(OOPBefore.class) &&
                        Arrays.stream(method.getAnnotation(OOPBefore.class).value()).anyMatch(testMethod.getName()::equals))
                .forEach(method -> {
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
        ArrayList<Method> afterMethods = new ArrayList<>(allMethods);
        Collections.reverse(afterMethods);
        afterMethods.stream().filter(method -> method.isAnnotationPresent(OOPAfter.class)
                && Arrays.stream(method.getAnnotation(OOPBefore.class).value()).anyMatch(testMethod.getName()::equals)
        ).forEach(method -> {
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
            Constructor<?> constructor = testClass.getDeclaredConstructors()[0];
            constructor.setAccessible(true);
            testClassInstance = constructor.newInstance();
        } catch (Exception e) {
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
                //expectedException = (OOPExpectedException) expectedExceptions[0];
                method.setAccessible(true);

                // invoke before methods
                Collections.reverse(allMethods);
                invokeBeforeMethods(allMethods, finalTestClassInstance2, method);

                ArrayList<Object> fields = new ArrayList<>();
                //backup(finalTestClassInstance2, fields);
                // invoke test method
                try {
                    Arrays.stream(testClass.getDeclaredFields())
                            .filter(field -> field.isAnnotationPresent(OOPExceptionRule.class))
                            .forEach(field -> {
                                field.setAccessible(true);
                                try {
                                    field.set(finalTestClassInstance2, OOPExpectedExceptionImpl.none());
                                } catch (IllegalAccessException e) {
                                    throw new RuntimeException(e);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            });
                    method.invoke(finalTestClassInstance2);
                } catch (InvocationTargetException e) {
                    Arrays.stream(testClass.getDeclaredFields())
                            .filter(field -> field.isAnnotationPresent(OOPExceptionRule.class))
                            .forEach(field -> {
                                field.setAccessible(true);
                                try {
                                    expectedExceptions[0] = field.get(finalTestClassInstance1);
                                } catch (IllegalAccessException e2) {
                                    e2.printStackTrace();
                                }
                            });
                    //restore(finalTestClassInstance2, fields);
                    throw e;
                }

                Arrays.stream(testClass.getDeclaredFields())
                        .filter(field -> field.isAnnotationPresent(OOPExceptionRule.class))
                        .forEach(field -> {
                            field.setAccessible(true);
                            try {
                                expectedExceptions[0] = field.get(finalTestClassInstance1);
                            } catch (IllegalAccessException e2) {
                                e2.printStackTrace();
                            }
                        });
                OOPExpectedExceptionImpl expectedException = (OOPExpectedExceptionImpl) expectedExceptions[0];
                if (expectedException != null && expectedException.getExpectedException() != null) {
                    summary.put(method.getName(), new OOPResultImpl(OOPResult.OOPTestResult.ERROR, expectedException.getExpectedException().getName()));
                } else {
                    summary.put(method.getName(), new OOPResultImpl(OOPResult.OOPTestResult.SUCCESS, null));
                }
                // invoke after methods
                invokeAfterMethods(allMethods, finalTestClassInstance2, method);

            } catch (InvocationTargetException e) {
                if (e.getCause().getClass().equals(OOPAssertionFailure.class)) {
                    summary.put(method.getName(), new OOPResultImpl(OOPResult.OOPTestResult.FAILURE, e.getCause().getMessage()));
                } else {
                    OOPExpectedExceptionImpl expectedException = (OOPExpectedExceptionImpl) expectedExceptions[0];
                    if (expectedException == null || expectedException.getExpectedException() == null) {

                        summary.put(method.getName(), new OOPResultImpl(OOPResult.OOPTestResult.ERROR, e.getCause().getClass().getName()));

                    } else {
                        if (expectedException.assertExpected((Exception) e.getCause())) {
                            summary.put(method.getName(), new OOPResultImpl(OOPResult.OOPTestResult.SUCCESS, null));
                        } else {
                            OOPExceptionMismatchError mismatch;
                            mismatch = new OOPExceptionMismatchError(expectedException.getExpectedException(), (Class<? extends Exception>) e.getCause().getClass());
                            summary.put(method.getName(), new OOPResultImpl(OOPResult.OOPTestResult.EXPECTED_EXCEPTION_MISMATCH, mismatch.getMessage()));
                        }
                    }
                }
            } catch (Exception e) {
                if (e.getClass().equals(OOPAssertionFailure.class)) {
                    summary.put(method.getName(), new OOPResultImpl(OOPResult.OOPTestResult.FAILURE, e.getMessage()));
                } else {
                    OOPExpectedExceptionImpl expectedException = (OOPExpectedExceptionImpl) expectedExceptions[0];
                    if (expectedException == null || expectedException.getExpectedException() == null) {
                        summary.put(method.getName(), new OOPResultImpl(OOPResult.OOPTestResult.ERROR, e.getClass().getName()));
                    } else {
                        if (expectedException.getExpectedException().equals(e.getClass()) || expectedException.assertExpected((Exception) e)) {
                            summary.put(method.getName(), new OOPResultImpl(OOPResult.OOPTestResult.SUCCESS, null));
                        } else {
                            OOPExceptionMismatchError mismatch;
                            mismatch = new OOPExceptionMismatchError(expectedException.getExpectedException(), (Class<? extends Exception>) e.getClass());
                            summary.put(method.getName(), new OOPResultImpl(OOPResult.OOPTestResult.EXPECTED_EXCEPTION_MISMATCH, mismatch.getMessage()));
                        }
                    }
                }
            }

        });
        return new OOPTestSummary(summary);
    }

}
