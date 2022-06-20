import engine.core.ConfigManager;
import engine.game.Game;

public class Launcher {
    public static void main(String[] args) {
        new Game().start();
//    	ConfigManager c = new ConfigManager("controller");
//    	c.setValue("player(index:1).up", "22");
//    	System.out.println(c.getValue("player(index:1).up"));
    }
}
