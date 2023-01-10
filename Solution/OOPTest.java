package OOP.Solution;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType;

@Target(ElementType.Method)
@Retention(RetentionPolicy.RUNTIME)
public @interface OOPTest {
    int order() default 0;

    String tag() default "";
}
