package com.zachbartholomew.githubrxjava.data;

/**
 * This class encapsulates a repository in GitHub
 * (the network response contains more data but we’re only interested in a subset of that).
 */

public class GitHubRepo {

    public final int id;
    public final String name;
    public final String htmlUrl;
    public final String description;
    public final String language;
    public final int stargazersCount;

    public GitHubRepo(int id, String name, String htmlUrl, String description,
                      String language, int stargazersCount) {
        this.id = id;
        this.name = name;
        this.htmlUrl = htmlUrl;
        this.description = description;
        this.language = language;
        this.stargazersCount = stargazersCount;
    }
}
