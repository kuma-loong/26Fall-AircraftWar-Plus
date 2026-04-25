package edu.hitsz.dao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * 排行榜记录值对象，封装玩家名、得分与记录时间。
 */
public class ScoreRecord {

    /**
     * 控制台展示与 TSV 存储统一使用的时间格式。
     */
    public static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String playerName;
    private final int score;
    private final LocalDateTime recordTime;

    public ScoreRecord(String playerName, int score, LocalDateTime recordTime) {
        this.playerName = playerName == null ? "" : playerName.trim();
        this.score = score;
        this.recordTime = recordTime == null ? LocalDateTime.now() : recordTime;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }

    public LocalDateTime getRecordTime() {
        return recordTime;
    }

    public String formatRecordTime() {
        return TIME_FORMATTER.format(recordTime);
    }

    public String toTsvLine() {
        return playerName + "\t" + score + "\t" + formatRecordTime();
    }

    public static ScoreRecord fromTsvLine(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }
        String[] parts = line.split("\t");
        if (parts.length != 3) {
            return null;
        }
        try {
            int parsedScore = Integer.parseInt(parts[1].trim());
            LocalDateTime parsedTime = LocalDateTime.parse(parts[2].trim(), TIME_FORMATTER);
            return new ScoreRecord(parts[0].trim(), parsedScore, parsedTime);
        } catch (RuntimeException exception) {
            return null;
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof ScoreRecord that)) {
            return false;
        }
        return score == that.score
                && Objects.equals(playerName, that.playerName)
                && Objects.equals(recordTime, that.recordTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerName, score, recordTime);
    }
}
