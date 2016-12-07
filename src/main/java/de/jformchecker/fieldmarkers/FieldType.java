package de.jformchecker.fieldmarkers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.jformchecker.FormCheckerElement;
import de.jformchecker.elements.AbstractInput;
import de.jformchecker.elements.LongTextInput;

@Target(ElementType.FIELD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldType {

	Class<? extends FormCheckerElement> type();
	
	int currentRevision() default 1;

	String lastModified() default "N/A";

	String lastModifiedBy() default "N/A";


}
