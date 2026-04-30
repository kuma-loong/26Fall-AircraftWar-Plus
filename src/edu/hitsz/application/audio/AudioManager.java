package edu.hitsz.application.audio;

/**
 * 音频管理器，统一封装循环音乐与短音效播放。
 */
public final class AudioManager {

    private static final String AUDIO_DIRECTORY = "src/videos/";
    private static final String BGM_PATH = AUDIO_DIRECTORY + "bgm.wav";
    private static final String BOSS_BGM_PATH = AUDIO_DIRECTORY + "bgm_boss.wav";
    private static final String BULLET_HIT_PATH = AUDIO_DIRECTORY + "bullet_hit.wav";
    private static final String BOMB_EXPLOSION_PATH = AUDIO_DIRECTORY + "bomb_explosion.wav";
    private static final String GAME_OVER_PATH = AUDIO_DIRECTORY + "game_over.wav";
    private static final String GET_SUPPLY_PATH = AUDIO_DIRECTORY + "get_supply.wav";

    private static MusicThread bgmThread;
    private static MusicThread bossBgmThread;

    private AudioManager() {
    }

    public static synchronized void playBgm() {
        stopThread(bgmThread);
        bgmThread = new MusicThread(BGM_PATH, true);
        bgmThread.start();
    }

    public static synchronized void playBossBgm() {
        stopThread(bgmThread);
        stopThread(bossBgmThread);
        bossBgmThread = new MusicThread(BOSS_BGM_PATH, true);
        bossBgmThread.start();
    }

    public static synchronized void resumeNormalBgm() {
        stopThread(bossBgmThread);
        bossBgmThread = null;
        playBgm();
    }

    public static synchronized void stopAllLoopingMusic() {
        stopThread(bgmThread);
        stopThread(bossBgmThread);
        bgmThread = null;
        bossBgmThread = null;
    }

    public static void playBulletHit() {
        playEffect(BULLET_HIT_PATH);
    }

    public static void playBombExplosion() {
        playEffect(BOMB_EXPLOSION_PATH);
    }

    public static void playGameOver() {
        playEffect(GAME_OVER_PATH);
    }

    public static void playGetSupply() {
        playEffect(GET_SUPPLY_PATH);
    }

    private static void playEffect(String filePath) {
        MusicThread musicThread = new MusicThread(filePath, false);
        musicThread.start();
    }

    private static void stopThread(MusicThread musicThread) {
        if (musicThread != null) {
            musicThread.stopMusic();
        }
    }
}
