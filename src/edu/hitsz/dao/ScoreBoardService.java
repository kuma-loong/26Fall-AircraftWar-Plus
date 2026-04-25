package edu.hitsz.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 排行榜服务层，负责排序、名次计算与控制台展示。
 */
public class ScoreBoardService {

    private static final Comparator<ScoreRecord> SCORE_RECORD_COMPARATOR =
            Comparator.comparingInt(ScoreRecord::getScore)
                    .reversed()
                    .thenComparing(ScoreRecord::getRecordTime)
                    .thenComparing(ScoreRecord::getPlayerName);

    private final GameDifficulty difficulty;
    private final ScoreRecordDao scoreRecordDao;

    public ScoreBoardService(GameDifficulty difficulty, ScoreRecordDao scoreRecordDao) {
        this.difficulty = difficulty == null ? GameDifficulty.NORMAL : difficulty;
        this.scoreRecordDao = scoreRecordDao;
    }

    public void recordScore(String playerName, int score) {
        recordScore(playerName, score, LocalDateTime.now());
    }

    public void recordScore(String playerName, int score, LocalDateTime recordTime) {
        scoreRecordDao.addRecord(new ScoreRecord(playerName, score, recordTime));
    }

    public List<ScoreRecord> getSortedRecords() {
        List<ScoreRecord> sortedRecords = new ArrayList<>(scoreRecordDao.getAllRecords());
        sortedRecords.sort(SCORE_RECORD_COMPARATOR);
        return sortedRecords;
    }

    public boolean deleteRecordByRank(int rank) {
        if (rank <= 0) {
            return false;
        }
        List<ScoreRecord> sortedRecords = getSortedRecords();
        if (rank > sortedRecords.size()) {
            return false;
        }
        return scoreRecordDao.deleteRecord(sortedRecords.get(rank - 1));
    }

    public void printScoreBoard() {
        List<ScoreRecord> sortedRecords = getSortedRecords();
        System.out.println("=== " + difficulty.name() + " Score Board ===");
        if (sortedRecords.isEmpty()) {
            System.out.println("No score records.");
            return;
        }
        for (int index = 0; index < sortedRecords.size(); index++) {
            ScoreRecord scoreRecord = sortedRecords.get(index);
            System.out.printf(
                    "%d\t%s\t%d\t%s%n",
                    index + 1,
                    scoreRecord.getPlayerName(),
                    scoreRecord.getScore(),
                    scoreRecord.formatRecordTime()
            );
        }
    }
}
