package org.acra.config;

import java.util.List;
import org.acra.config.d.a;
import org.acra.sender.ReportSender;

public class c implements d {
    public boolean a(List<ReportSender> list, List<a> list2) {
        return list.size() == list2.size() && !list.isEmpty();
    }
}
