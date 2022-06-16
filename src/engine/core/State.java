package engine.core;

import engine.game.Game;

public class State {
    protected Game game;

    public State(Game game) {
        this.game = game;
        init();
    }

    protected void init() {

    }
}
