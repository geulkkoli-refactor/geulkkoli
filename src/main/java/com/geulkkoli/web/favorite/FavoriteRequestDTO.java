package com.geulkkoli.web.favorite;

public class FavoriteRequestDTO {
    private final String requestResult;
    private final int favoriteCount;

    public FavoriteRequestDTO(String requestResult, int favoriteCount) {
        this.requestResult = requestResult;
        this.favoriteCount = favoriteCount;
    }

    public String getRequestResult() {
        return requestResult;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }
}
