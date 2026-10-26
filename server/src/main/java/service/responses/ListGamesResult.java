package service.responses;

import model.GameData;
import java.util.List;

public record ListGamesResult(List<GameData> games) { // list of game datas?
}
