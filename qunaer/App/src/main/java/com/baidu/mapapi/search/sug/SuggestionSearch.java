package com.baidu.mapapi.search.sug;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.search.core.i;
import com.baidu.platform.core.f.a;
import com.baidu.platform.core.f.b;

public class SuggestionSearch extends i {
    a a = new b();
    private boolean b = false;

    private SuggestionSearch() {
    }

    public static SuggestionSearch newInstance() {
        BMapManager.init();
        return new SuggestionSearch();
    }

    public void destroy() {
        if (!this.b) {
            this.b = true;
            this.a.a();
            BMapManager.destroy();
        }
    }

    public boolean requestSuggestion(SuggestionSearchOption suggestionSearchOption) {
        if (this.a == null) {
            throw new IllegalStateException("suggestionsearch is null, please call newInstance() first.");
        } else if (suggestionSearchOption != null && suggestionSearchOption.mKeyword != null && suggestionSearchOption.mCity != null) {
            return this.a.a(suggestionSearchOption);
        } else {
            throw new IllegalArgumentException("option or keyword or city can not be null");
        }
    }

    public void setOnGetSuggestionResultListener(OnGetSuggestionResultListener onGetSuggestionResultListener) {
        if (this.a == null) {
            throw new IllegalStateException("suggestionsearch is null, please call newInstance() first.");
        } else if (onGetSuggestionResultListener == null) {
            throw new IllegalArgumentException("listener can not be null");
        } else {
            this.a.a(onGetSuggestionResultListener);
        }
    }
}
