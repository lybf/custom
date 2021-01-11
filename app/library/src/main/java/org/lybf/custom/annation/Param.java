package org.lybf.custom.annation;

public @interface Param {
    
    public String Type() default "";
	public String Name() default "";
    public String desc() default "";
}
