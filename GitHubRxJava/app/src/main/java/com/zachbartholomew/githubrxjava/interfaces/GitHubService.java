package com.zachbartholomew.githubrxjava.interfaces;

import com.zachbartholomew.githubrxjava.data.GitHubRepo;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * We will pass this interfaces into Retrofit and
 * Retrofit will create an implementation of GitHubService
 */

public interface GitHubService {
    @GET("users/{user}/starred")
    Observable<List<GitHubRepo>> getStarredRepositories(@Path("user") String userName);
}
