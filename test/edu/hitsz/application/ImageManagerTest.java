package edu.hitsz.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * ImageManager 非 UI 辅助逻辑测试。
 */
class ImageManagerTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("Import target file appends suffix when name already exists")
    void shouldAppendSuffixWhenImportNameAlreadyExists() throws Exception {
        File directory = tempDir.toFile();
        File existingFile = new File(directory, "bg.jpg");
        if (!existingFile.createNewFile()) {
            throw new IllegalStateException("Failed to create test fixture.");
        }

        File targetFile = ImageManager.resolveImportTargetFile("bg.jpg", directory);

        assertEquals("bg_1.jpg", targetFile.getName());
    }
}
