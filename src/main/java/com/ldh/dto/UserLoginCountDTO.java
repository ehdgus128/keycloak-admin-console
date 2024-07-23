package com.ldh.dto;

public class UserLoginCountDTO {
    private String userId;
    private Long count;

    public UserLoginCountDTO(String userId, Long count) {
        this.userId = userId;
        this.count = count;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
