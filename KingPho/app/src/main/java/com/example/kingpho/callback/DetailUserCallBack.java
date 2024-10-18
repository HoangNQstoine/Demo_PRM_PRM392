package com.example.kingpho.callback;

import com.example.kingpho.model.MyDetail;
import java.util.List;

public interface DetailUserCallBack {
    void onDetailUserFetched(List<MyDetail> detailList);
    void onError(Throwable throwable);
}
