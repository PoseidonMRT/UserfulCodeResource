package com.mqunar.atomenv;

import com.mqunar.atomenv.env.ReleaseEnvironment;

public final class EnvInit {
    public static final IEnvironment newEnv() {
        return new ReleaseEnvironment();
    }
}
