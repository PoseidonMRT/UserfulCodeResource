package com.baidu.platform.core.f;

import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.platform.base.e;

class c implements e<SuggestionResult> {
    final /* synthetic */ b a;

    c(b bVar) {
        this.a = bVar;
    }

    public void a(SuggestionResult suggestionResult) {
        if (this.a.b != null) {
            this.a.b.onGetSuggestionResult(suggestionResult);
        }
    }
}
