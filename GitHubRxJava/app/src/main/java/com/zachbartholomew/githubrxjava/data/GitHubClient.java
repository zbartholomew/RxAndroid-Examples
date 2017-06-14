package com.zachbartholomew.githubrxjava.data;

import android.support.annotation.NonNull;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zachbartholomew.githubrxjava.interfaces.GitHubService;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * This will be the object we will interact with to make network calls from the UI level.
 *
 * When constructing an implementation of GitHubService through Retrofit,
 * we need to pass in an RxJavaCallAdapterFactory as the call adapter so that network calls can
 * return Observable objects
 * (passing a call adapter is needed for any network call that returns a result other than a Call)
 *
 * We also need to pass in a GsonConverterFactory
 * so that we can use Gson as a way to marshal JSON objects to Java objects
 */

public class GitHubClient {

    private static final String GITHUB_BASE_URL = "https://api.github.com/";

    private static GitHubClient instance;
    private GitHubService gitHubService;

    private GitHubClient() {
        final Gson gson =
                new GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .create();
        final Retrofit retrofit = new Retrofit.Builder().baseUrl(GITHUB_BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        gitHubService = retrofit.create(GitHubService.class);
    }

    public static GitHubClient getInstance() {
        if (instance == null) {
            instance = new GitHubClient();
        }
        return instance;
    }

    public Observable<List<GitHubRepo>> getStarredRepos(@NonNull String userName) {
        return gitHubService.getStarredRepositories(userName);
    }
}
