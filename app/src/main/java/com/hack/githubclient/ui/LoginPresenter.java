package com.hack.githubclient.ui;


import android.text.TextUtils;
import android.util.Log;

import com.hack.githubclient.Constants;
import com.hack.githubclient.LocalPersistent;
import com.hack.githubclient.di.scope.PerActivity;
import com.hack.githubclient.serivce.ApiService;
import com.hack.githubclient.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Asuka on 16/5/21.
 */
@PerActivity
public class LoginPresenter extends BasePresenter<LoginActivity> {

    private ApiService apiService;
    private LocalPersistent localPersistent;

    @Inject
    public LoginPresenter(ApiService apiService, LocalPersistent localPersistent) {
        this.apiService = apiService;
        this.localPersistent = localPersistent;
    }

    public void userLogin(String username, String password) {
        if (TextUtils.isEmpty(username)) {
            mView.showError("请输入用户名");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            mView.showError("请输入密码");
            return;
        }

        apiService.getUserInfo(username, Constants.CLIENT_ID, Constants.CLIENT_SECRET)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(user -> {
                    localPersistent.setUser(user);
                    mView.showUser(user);
                }, throwable -> {
                    Log.e("xxx", "throw=" + throwable.getMessage());
                });
    }
}
