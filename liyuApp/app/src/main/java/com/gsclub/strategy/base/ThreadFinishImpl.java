package com.gsclub.strategy.base;

public interface ThreadFinishImpl<T> {
    void finish(T o, Runnable runnable);
}
