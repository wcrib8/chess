package model.responses;

import model.data.GameData;
import java.util.List;

public record ListGamesResult(List<GameData> games) {
}
