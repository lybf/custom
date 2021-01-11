package org.lybf.custom.annation;

public @interface Return {
	public String returnType() default "";
    public String desc() default "";
}
