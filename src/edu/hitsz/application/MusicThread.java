package edu.hitsz.application;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 音频播放线程，支持单次播放、循环播放与中途停止。
 */
public class MusicThread extends Thread {

    private final String filename;
    private final boolean loop;
    private volatile boolean playing = true;
    private AudioFormat audioFormat;
    private byte[] samples;
    private volatile SourceDataLine currentDataLine;

    public MusicThread(String filename, boolean loop) {
        this.filename = filename;
        this.loop = loop;
        setDaemon(true);
        loadAudio();
    }

    /**
     * 停止当前音频线程。
     */
    public void stopMusic() {
        playing = false;
        interrupt();
        SourceDataLine dataLine = currentDataLine;
        if (dataLine != null) {
            dataLine.stop();
            dataLine.flush();
            dataLine.close();
        }
    }

    @Override
    public void run() {
        if (audioFormat == null || samples == null || samples.length == 0) {
            return;
        }
        do {
            play(new ByteArrayInputStream(samples));
        } while (loop && playing && !isInterrupted());
    }

    private void loadAudio() {
        try (AudioInputStream stream = AudioSystem.getAudioInputStream(new File(filename))) {
            audioFormat = stream.getFormat();
            samples = stream.readAllBytes();
        } catch (UnsupportedAudioFileException | IOException exception) {
            audioFormat = null;
            samples = null;
        }
    }

    private void play(InputStream source) {
        int bufferSize = Math.max(1, (int) (audioFormat.getFrameSize() * audioFormat.getSampleRate()));
        byte[] buffer = new byte[bufferSize];
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);

        try (InputStream safeSource = source; SourceDataLine dataLine = (SourceDataLine) AudioSystem.getLine(info)) {
            currentDataLine = dataLine;
            dataLine.open(audioFormat, bufferSize);
            dataLine.start();

            int numBytesRead;
            while (playing && !isInterrupted() && (numBytesRead = safeSource.read(buffer, 0, buffer.length)) != -1) {
                dataLine.write(buffer, 0, numBytesRead);
            }

            if (playing && !isInterrupted()) {
                dataLine.drain();
            }
        } catch (LineUnavailableException | IOException exception) {
            playing = false;
        } finally {
            currentDataLine = null;
        }
    }
}
