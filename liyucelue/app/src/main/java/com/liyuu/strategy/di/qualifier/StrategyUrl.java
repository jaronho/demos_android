package com.liyuu.strategy.di.qualifier;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * hongpan app qualifier
 * 解决多个相同@Provider Retrofit 时依赖迷失的问题
 * 可直接使用@Named解决依赖迷失问题
 */

@Qualifier
@Documented
@Retention(RUNTIME)
public @interface StrategyUrl {

}
