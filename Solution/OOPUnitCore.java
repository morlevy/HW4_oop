package Solution.OOP;

import OOP.Provided.OOPAssertionFailure;

class OOPUnitCore {
    private OOPUnitCore() {
    }

    public void assertEquals(Object expected, Object actual) throws OOPAssertionFailure {
        if ((expected == null && actual != null) || !expected.equals(actual)) {
            throw new OOPAssertionFailure(expected, actual);
        }
    }

    public void fail() throws OOPAssertionFailure {
        throw new OOPAssertionFailure();
    }

    private static void backup(Object classInst, ArrayList<Object> backUpList) {
        Class<?> clazz = classInst.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                backUpList.add(field.get(classInst));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static void restore(Object classInst, ArrayList<Object> backedUpList) {

    }

    public void runClass(Class<?> testClass, String tag ="") throws IllegalArgumentException {
        if (testClass == null || tag == null || !testClass.isAnnotationPresent(OOPTestClass.class)) {
            throw new IllegalArgumentException();
        }

        //create a new instance of the test class
        Object testClassInstance = null;
        try {
            Constructor<?> constructor = testClass.getConstructor();
            constructor.setAccessible(true);
            testClassInstance = constructor.newInstance();

            // invoke setup methods
            ArrayList<Method> allMethods = testClass.getMethods();
            ArrayList<Method> setupMethods = allMethods.stream().reverse()
                    .filter(method -> method.isAnnotationPresent(OOPSetup.class));
            setupMethods.stream().forEach(method -> {
                method.invoke(testClassInstance);
            });

            // test methods
            ArrayList<Method> testMethods = allMethods.stream().reverse()
                    .filter(method -> method.isAnnotationPresent(OOPTest.class) && (tag == "" || method.getAnnotation(OOPTest.class).tag().equals(tag)));

            if (testClass.getAnnotation(OOPTestClass.class).value() == OOPTestClass.OOPTestClassType.ORDERED) {
                testMethods = testMethods.stream().sorted(Comparator.comparingInt(method -> method.getAnnotation(OOPTest.class).order()));
            }

            // invoke before methods
            testMethods.forEach(method -> {
                ArrayList<Method> beforeMethods = allMethods.stream()
                        .filter(beforeMethod -> beforeMethod.isAnnotationPresent(OOPBefore.class) && beforeMethod.getAnnotation(OOPBefore.class).value().equals(method.getName()));
                beforeMethods.stream().forEach(beforeMethod -> {
                    ArrayList<Object> fields = new ArrayList<>();
                    backup(testClassInstance, fields);// TODO: backup
                    try {
                        beforeMethod.invoke(testClassInstance);
                    } catch (Exception e) {
                        //TODO restore and maybe save exception
                        restore(testClassInstance, fields);
                    }
                });
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
