package com.mqunar.core.dependency;

import java.util.HashSet;
import java.util.Set;

public class Circular {
    public Set<Dependency> nodeList = new HashSet();

    public void mergeCircular(Circular circular) {
        this.nodeList.addAll(circular.nodeList);
    }

    public void addNode(Dependency dependency) {
        this.nodeList.add(dependency);
    }

    public boolean update(Dependency dependency) {
        if (dependency.checkFlag == -10 || dependency.checkFlag == -11 || dependency.checkFlag == -20 || dependency.checkFlag == -21 || dependency.checkFlag == -22) {
            for (Dependency dependency2 : this.nodeList) {
                if (dependency2 != dependency && dependency2.checkFlag == 2) {
                    dependency2.checkFlag = 0;
                }
            }
            return false;
        }
        for (Dependency dependency22 : this.nodeList) {
            if (dependency22 != dependency) {
                if (dependency22.checkFlag == -10 || dependency22.checkFlag == -11 || dependency22.checkFlag == -20 || dependency22.checkFlag == -21 || dependency22.checkFlag == -22) {
                    for (Dependency dependency222 : this.nodeList) {
                        if (dependency222 != dependency && dependency222.checkFlag == 2) {
                            dependency222.checkFlag = 0;
                        }
                    }
                    return false;
                } else if (dependency222.checkFlag == 0) {
                    return false;
                }
            }
        }
        for (Dependency dependency2222 : this.nodeList) {
            dependency2222.checkFlag = 1;
        }
        return true;
    }
}
