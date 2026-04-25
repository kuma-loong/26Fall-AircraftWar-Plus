package edu.hitsz.dao;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 基于 TSV 文件的排行榜 DAO 实现。
 */
public class FileScoreRecordDao implements ScoreRecordDao {

    private static final String DATA_DIRECTORY = "data";
    private final Path filePath;

    public FileScoreRecordDao(GameDifficulty difficulty) {
        this(resolvePath(difficulty));
    }

    public FileScoreRecordDao(Path filePath) {
        this.filePath = filePath;
    }

    public Path getFilePath() {
        return filePath;
    }

    @Override
    public List<ScoreRecord> getAllRecords() {
        ensureFileExists();
        List<ScoreRecord> records = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            for (String line : lines) {
                ScoreRecord scoreRecord = ScoreRecord.fromTsvLine(line);
                if (scoreRecord != null) {
                    records.add(scoreRecord);
                }
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read ranking file: " + filePath, exception);
        }
        return records;
    }

    @Override
    public void addRecord(ScoreRecord scoreRecord) {
        if (scoreRecord == null) {
            return;
        }
        List<ScoreRecord> records = getAllRecords();
        records.add(scoreRecord);
        writeAllRecords(records);
    }

    @Override
    public boolean deleteRecord(ScoreRecord scoreRecord) {
        if (scoreRecord == null) {
            return false;
        }
        List<ScoreRecord> records = getAllRecords();
        Iterator<ScoreRecord> iterator = records.iterator();
        while (iterator.hasNext()) {
            if (scoreRecord.equals(iterator.next())) {
                iterator.remove();
                writeAllRecords(records);
                return true;
            }
        }
        return false;
    }

    private void ensureFileExists() {
        try {
            Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            if (Files.notExists(filePath)) {
                Files.createFile(filePath);
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to prepare ranking file: " + filePath, exception);
        }
    }

    private void writeAllRecords(List<ScoreRecord> records) {
        ensureFileExists();
        List<String> lines = new ArrayList<>();
        for (ScoreRecord scoreRecord : records) {
            lines.add(scoreRecord.toTsvLine());
        }
        try {
            Files.write(filePath, lines, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to write ranking file: " + filePath, exception);
        }
    }

    private static Path resolvePath(GameDifficulty difficulty) {
        GameDifficulty safeDifficulty = difficulty == null ? GameDifficulty.NORMAL : difficulty;
        return Path.of(DATA_DIRECTORY, "ranking-" + safeDifficulty.getFileSuffix() + ".tsv");
    }
}
