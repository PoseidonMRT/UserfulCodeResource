package com.baidu.platform.core.f;

import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.baidu.platform.base.SearchType;
import com.baidu.platform.base.a;

public class b extends a implements a {
    private OnGetSuggestionResultListener b = null;

    public void a() {
        this.b = null;
    }

    public void a(OnGetSuggestionResultListener onGetSuggestionResultListener) {
        this.b = onGetSuggestionResultListener;
    }

    public boolean a(SuggestionSearchOption suggestionSearchOption) {
        this.a = new d();
        this.a.a(new c(this));
        this.a.a(SearchType.SUGGESTION_SEARCH_TYPE);
        return a(new e(suggestionSearchOption));
    }
}
