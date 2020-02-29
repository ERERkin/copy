package it.academy.demoBot;

import it.academy.demoBot.Dao.GameDao;
import it.academy.demoBot.model.Game;

public class Controller {
    static Game update(int button, int update){
        GameDao.updateButton(button, update);
        GameDao.swap();
        return GameDao.getLastGame();
    }
}
