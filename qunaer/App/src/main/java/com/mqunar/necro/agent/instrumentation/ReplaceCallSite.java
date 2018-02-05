package com.mqunar.necro.agent.instrumentation;

public @interface ReplaceCallSite {
    boolean isStatic() default false;

    String scope() default "";
}
