package com.example.commlib.utils;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.annotation.RawRes;

import com.blankj.ALog;
import com.example.commlib.api.App;

import java.io.IOException;
import java.util.List;

/**
 * 播放本地音频
 * Created by yzh on 2020/1/6 11:00.
 */
public class VoicePlayer implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

    private MediaPlayer mediaPlayer;
    private boolean isCompletion = false;//是否播放完毕
    private boolean isPrepared = false;
    private Listener mListener;

    public void setListener(Listener listener) {
        mListener = listener;
    }

    private static volatile VoicePlayer mInstance;
    public static VoicePlayer get() {
        if (mInstance == null) {
            synchronized (VoicePlayer.class) {
                if (mInstance == null) {
                    mInstance = new VoicePlayer();
                }
            }
        }
        return mInstance;
    }

    private VoicePlayer() {
        initPlayer();
    }

    private void initPlayer(){
        if(mediaPlayer==null){
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setLooping(false);//是否循环
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setOnCompletionListener(this);
                mediaPlayer.setOnPreparedListener(this);
            } catch (Exception e) {
                Log.e("mediaPlayer", "error", e);
            }
        }
    }

    /**
     * 播放raw目录下的音乐mp3文件
     * @param rawId R.raw.abc
     */
    public void playRawMusic(@RawRes int rawId) {
      //  mediaPlayer = MediaPlayer.create(PrimaryApplication.get(), rawId);//也可以直接这么创建
        initPlayer();
        AssetFileDescriptor file = App.getInstance().getResources().openRawResourceFd(rawId);
        try {
            mediaPlayer.reset();//每次播放前最好reset一下
            mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
            mediaPlayer.prepare();
            file.close();
            play();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private List<Integer> mRawIds;
    /**
     * 连续播放多个raw目录下的音乐mp3文件
     */
    public VoicePlayer playRawList(List<Integer> rawIds) {
        if(CommUtils.isListNull(rawIds)){
            return this;
        }
        mRawIds=rawIds;
        playRawMusic(mRawIds.get(0));
        return this;
    }

    /**
     * 播放assets下的音乐mp3文件
     * @param fileName abc.mp3
     */
    public void playAssetMusic(String fileName) {
        initPlayer();
        try {
            //播放 assets/abc.mp3 音乐文件
            AssetFileDescriptor file = App.getInstance().getAssets().openFd(fileName);
            //mediaPlayer = new MediaPlayer();
            mediaPlayer.reset();
            mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
            mediaPlayer.prepare();
            file.close();
            play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据路径播放
     * @param videoUrl 资源文件名称   本地地址  和网络地址
     */
    public void playUrl(String videoUrl) {
        initPlayer();
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(videoUrl);
            mediaPlayer.prepare();
            play();
        } catch (IllegalArgumentException | IllegalStateException | IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    /**
     * 播放
     */
    public void play() {
        if (mediaPlayer != null &&!isPlaying()) {
            isCompletion = false;
           // mediaPlayer.prepare();
            mediaPlayer.start();
        }
    }

    /**
     * 暂停
     */
    public void pause() {
        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
    }

    /**
     * 释放资源
     */
    public void destory() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            if(CommUtils.isListNotNull(mRawIds)){
                mRawIds.clear();
            }
        }
    }

    /**
     * 重播
     */
    public void replay() {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(0);
        }
    }

    @Override
    public void onPrepared(MediaPlayer arg0) {
        isPrepared = true;
    }

    @Override
    public void onCompletion(MediaPlayer arg0) {
        isCompletion = true;

        //如果存在需要连续播放的资源
        if(CommUtils.isListNotNull(mRawIds)){
            mRawIds.remove(0);
            if(CommUtils.isListNotNull(mRawIds)){
                ALog.v("播放完一个移除一个，移除了还有继续播放");
                playRawMusic(mRawIds.get(0));
            }else{
                destory();
                if(mListener!=null){
                    mListener.onCompletion();
                }

                ALog.v("播放完毕");
            }
        }else{
            destory();
            if(mListener!=null){
                mListener.onCompletion();
            }
            ALog.v("播放完毕");
        }
    }

    /**
     * 是否播放完毕
     */
    public boolean isCompletion() {
        return isCompletion;
    }

    public interface Listener {
        void onCompletion();
    }
}
