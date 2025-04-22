package apiAutotest.model;

public class PlayerDeleteRequestDto {
    private Long playerId;

    public PlayerDeleteRequestDto(Long playerId) {
        this.playerId = playerId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }
}
