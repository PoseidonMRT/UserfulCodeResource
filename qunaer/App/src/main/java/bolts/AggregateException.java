package bolts;

import java.util.ArrayList;
import java.util.List;

public class AggregateException extends Exception {
    private static final long serialVersionUID = 1;
    private Throwable[] causes;

    public AggregateException(String str, Throwable[] thArr) {
        Throwable th;
        if (thArr == null || thArr.length <= 0) {
            th = null;
        } else {
            th = thArr[0];
        }
        super(str, th);
        if (thArr == null || thArr.length <= 0) {
            thArr = null;
        }
        this.causes = thArr;
    }

    @Deprecated
    public AggregateException(List<Exception> list) {
        this("There were multiple errors.", (Throwable[]) list.toArray(new Exception[list.size()]));
    }

    @Deprecated
    public List<Exception> getErrors() {
        List<Exception> arrayList = new ArrayList();
        if (this.causes == null) {
            return arrayList;
        }
        for (Throwable th : this.causes) {
            if (th instanceof Exception) {
                arrayList.add((Exception) th);
            } else {
                arrayList.add(new Exception(th));
            }
        }
        return arrayList;
    }

    public Throwable[] getCauses() {
        return this.causes;
    }
}
