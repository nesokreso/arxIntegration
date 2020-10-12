package com.github.arxintegration.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Container for repeating {@link DatabaseSetup} annotations.
 *
 * @author Nenad Jevdjenic
 * @see DatabaseSetup
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface DatabaseSetups {

	/**
	 * The {@link DatabaseSetup} annotations to apply.
	 * @return the {@link DatabaseSetup} annotations
	 */
	DatabaseSetup[] value();

}
