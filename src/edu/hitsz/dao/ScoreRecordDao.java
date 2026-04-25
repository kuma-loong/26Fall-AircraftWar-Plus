package edu.hitsz.dao;

import java.util.List;

/**
 * 排行榜数据访问接口，负责记录的读取与持久化。
 */
public interface ScoreRecordDao {

    List<ScoreRecord> getAllRecords();

    void addRecord(ScoreRecord scoreRecord);

    boolean deleteRecord(ScoreRecord scoreRecord);
}
