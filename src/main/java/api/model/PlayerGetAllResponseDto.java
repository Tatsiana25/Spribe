package api.model;

import java.util.List;

public class PlayerGetAllResponseDto {
    private List<PlayerItem> players;

    public List<PlayerItem> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerItem> players) {
        this.players = players;
    }

}
