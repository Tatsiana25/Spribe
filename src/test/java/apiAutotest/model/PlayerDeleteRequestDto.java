package apiAutotest.model;

public class PlayerDeleteRequestDto {
    private long playerId;

    public PlayerDeleteRequestDto(long playerId) {
        this.playerId = playerId;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }
}
