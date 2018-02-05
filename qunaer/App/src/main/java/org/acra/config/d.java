package org.acra.config;

import java.util.List;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;

public interface d {

    public class a {
        private final ReportSender a;
        private final ReportSenderException b;

        public a(ReportSender reportSender, ReportSenderException reportSenderException) {
            this.a = reportSender;
            this.b = reportSenderException;
        }

        public ReportSender a() {
            return this.a;
        }

        public ReportSenderException b() {
            return this.b;
        }
    }

    boolean a(List<ReportSender> list, List<a> list2);
}
