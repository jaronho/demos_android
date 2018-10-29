package com.liyuu.strategy.di.scope;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * activity 作用域
 * Module 中 provide 方法中的 Scope 注解必须和 与之绑定的 Component 的 Scope 注解一样，否则作用域不同会导致编译时会报错。
 */

@Scope
@Retention(RUNTIME)
public @interface ActivityScope {
}