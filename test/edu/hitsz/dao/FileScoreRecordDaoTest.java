package edu.hitsz.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 文件排行榜 DAO 基本行为测试。
 */
class FileScoreRecordDaoTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("DAO adds and reads back score records")
    void shouldAddAndReadRecords() {
        Path filePath = tempDir.resolve("ranking.tsv");
        FileScoreRecordDao dao = new FileScoreRecordDao(filePath);
        LocalDateTime time = LocalDateTime.of(2026, 4, 25, 12, 0, 0);

        dao.addRecord(new ScoreRecord("Player", 120, time));
        dao.addRecord(new ScoreRecord("Ace", 300, time.plusMinutes(1)));

        List<ScoreRecord> records = dao.getAllRecords();
        assertEquals(2, records.size());
        assertEquals("Player", records.get(0).getPlayerName());
        assertEquals(120, records.get(0).getScore());
    }

    @Test
    @DisplayName("ScoreBoardService sorts by score descending")
    void shouldSortRecordsByScoreDescending() {
        Path filePath = tempDir.resolve("sorted.tsv");
        FileScoreRecordDao dao = new FileScoreRecordDao(filePath);
        ScoreBoardService service = new ScoreBoardService(GameDifficulty.NORMAL, dao);

        service.recordScore("Alice", 150, LocalDateTime.of(2026, 4, 25, 12, 0, 0));
        service.recordScore("Bob", 220, LocalDateTime.of(2026, 4, 25, 12, 1, 0));
        service.recordScore("Carol", 180, LocalDateTime.of(2026, 4, 25, 12, 2, 0));

        List<ScoreRecord> records = service.getSortedRecords();

        assertEquals("Bob", records.get(0).getPlayerName());
        assertEquals("Carol", records.get(1).getPlayerName());
        assertEquals("Alice", records.get(2).getPlayerName());
    }

    @Test
    @DisplayName("DAO returns empty list for empty file")
    void shouldReturnEmptyRecordsForEmptyFile() {
        Path filePath = tempDir.resolve("empty.tsv");
        FileScoreRecordDao dao = new FileScoreRecordDao(filePath);

        List<ScoreRecord> records = dao.getAllRecords();

        assertTrue(Files.exists(filePath));
        assertTrue(records.isEmpty());
    }

    @Test
    @DisplayName("ScoreBoardService deletes record by rank")
    void shouldDeleteRecordByRank() {
        Path filePath = tempDir.resolve("delete.tsv");
        FileScoreRecordDao dao = new FileScoreRecordDao(filePath);
        ScoreBoardService service = new ScoreBoardService(GameDifficulty.NORMAL, dao);

        service.recordScore("Alice", 150, LocalDateTime.of(2026, 4, 25, 12, 0, 0));
        service.recordScore("Bob", 220, LocalDateTime.of(2026, 4, 25, 12, 1, 0));
        service.recordScore("Carol", 180, LocalDateTime.of(2026, 4, 25, 12, 2, 0));

        boolean deleted = service.deleteRecordByRank(2);
        List<ScoreRecord> records = service.getSortedRecords();

        assertTrue(deleted);
        assertEquals(2, records.size());
        assertEquals("Bob", records.get(0).getPlayerName());
        assertEquals("Alice", records.get(1).getPlayerName());
    }

    @Test
    @DisplayName("Different difficulties map to different file paths")
    void shouldUseDifferentFilePathPerDifficulty() {
        FileScoreRecordDao easyDao = new FileScoreRecordDao(GameDifficulty.EASY);
        FileScoreRecordDao normalDao = new FileScoreRecordDao(GameDifficulty.NORMAL);
        FileScoreRecordDao hardDao = new FileScoreRecordDao(GameDifficulty.HARD);

        assertTrue(easyDao.getFilePath().toString().endsWith("ranking-easy.tsv"));
        assertTrue(normalDao.getFilePath().toString().endsWith("ranking-normal.tsv"));
        assertTrue(hardDao.getFilePath().toString().endsWith("ranking-hard.tsv"));
        assertFalse(easyDao.getFilePath().equals(normalDao.getFilePath()));
        assertFalse(normalDao.getFilePath().equals(hardDao.getFilePath()));
    }
}
