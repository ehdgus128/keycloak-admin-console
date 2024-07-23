package com.ldh.dto;

public class ClientLoginCountDTO {

    private String clientId;
    private Long count;

    public ClientLoginCountDTO(String clientId, Long count) {
        this.clientId = clientId;
        this.count = count;
    }

    // Getter와 Setter
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
