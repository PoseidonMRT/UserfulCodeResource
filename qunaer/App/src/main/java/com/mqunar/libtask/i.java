package com.mqunar.libtask;

import java.util.concurrent.Callable;

abstract class i<Params, Result> implements Callable<Result> {
    Params[] b;

    private i() {
    }
}
