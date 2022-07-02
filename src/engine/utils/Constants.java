package engine.utils;

public class Constants {

    public static class Animation {
        public int index;
        public int framesCount;

        public Animation(int index, int framesCount) {
            this.index = index;
            this.framesCount = framesCount;
        }
    }

    public static class Game {
        public static final String SCREEN_TITLE = "King - v0.01";
        public static final int TILES_DEFAULT_SIZE = 32;
        public static final float GAME_SCALE = 2.0f;
        public static final int COUNT_TILES_IN_WIDTH = 26; //12 26
        public static final int COUNT_TILES_IN_HEIGHT = 14; //8 14
        public static final int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * GAME_SCALE);
        public static final int GAME_WIDTH = TILES_SIZE * COUNT_TILES_IN_WIDTH;
        public static final int GAME_HEIGHT = TILES_SIZE * COUNT_TILES_IN_HEIGHT;
        public static final int FPS_SET = 60;
        public static final int UPS_SET = 120;
        public static final float GRAVITY = 0.1f * GAME_SCALE;
        public static final float JUMP_FORCE = -3.75f * GAME_SCALE;
    }

    public static class Scenes {
        public static final String TILES_SPRITE_PATH = "assets/tiles/tiles.png";

        //LEVELS
        public static final String DEBUG_SCENE_PATH = "assets/scenes/debug.png";
        public static final String DEBUG_SCENE_PATH_NEW = "assets/scenes/debug-new.png";
        //public static final String DEBUG_SCENE_EX_IMAGE = "assets/scenes/scene_one_ex_data.png";
    }

    public static class Component {
        public static final boolean SHOW_COLLIDER_TRUE = true;
        public static final boolean SHOW_COLLIDER_FALSE = false;
    }

    public static class PlayerConstants {
        public static final String SPRITE_PATH = "assets/sprites/player.png";
        public static final int WIDTH = 78;
        public static final int HEIGHT = 58;

        public static final int ANIM_IDLE = 0;//new Animation(0, 11);
        public static final int ANIM_RUN = 1;//new Animation(1, 8);
        public static final int ANIM_JUMP = 2;//new Animation(2, 11);
        public static final int ANIM_FALL = 3;//new Animation(3, 11);
        public static final int ANIM_ATTACK = 4;//new Animation(4, 3);
        public static final int ANIM_DAMAGE = 8;//new Animation(4, 3);
        /*public static Animation ANIM_IDLE = new Animation(5, 11);
        public static Animation ANIM_IDLE = new Animation(6, 11);
        public static Animation ANIM_IDLE = new Animation(7, 11);
        public static Animation ANIM_IDLE = new Animation(8, 11);*/

        public static int GetAnimationFrameCount(int index) {
            switch (index) {
                case ANIM_IDLE:
                    return 11;
                case ANIM_RUN:
                    return 8;
                case ANIM_ATTACK:
                    return 3;
                case ANIM_JUMP:
                case ANIM_FALL:
                    return  1;
                case ANIM_DAMAGE:
                    return  2;
                default:
                    return 0;
            }
        }
    }

    public static class EnemyConstants {
        public static final String PIG_SPRITE_PATH = "assets/sprites/pig.png";

        public static final int WIDTH = 34;
        public static final int HEIGHT = 28;

        public static final int ANIM_IDLE = 0;
        public static final int ANIM_MOVE = 1;
        public static final int ANIM_DAMAGE = 4;
        public static final int ANIM_ATTACK = 2;
        
        public static int GetAnimationFrameCount(int index) {
            switch (index) {
                case ANIM_IDLE:
                    return 11;
                case ANIM_MOVE:
                    return 6;
                case ANIM_DAMAGE:
                    return 2;
                case ANIM_ATTACK:
                    return 5;
                default:
                    return 0;
            }
        }
    }
}
