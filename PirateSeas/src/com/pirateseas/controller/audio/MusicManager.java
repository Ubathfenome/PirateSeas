package com.pirateseas.controller.audio;

import java.io.IOException;
import java.util.HashMap;

import com.pirateseas.global.Constants;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

@SuppressLint("UseSparseArrays")
public class MusicManager{
	private static final String TAG = "MusicManager";
	
	// Sound Ids
	public static final int SOUND_SHOT_FIRED = 0x0;
	public static final int SOUND_SHOT_FLYING = 0x1;
	public static final int SOUND_SHOT_HIT = 0x2;
	public static final int SOUND_SHOT_MISSED = 0x3;
	public static final int SOUND_WEATHER_FOG = 0x4;
	public static final int SOUND_WEATHER_STORM = 0x5;
	public static final int SOUND_ENEMY_APPEAR = 0x6;
	public static final int SOUND_GAME_PAUSED = 0x7;
	public static final int SOUND_GOLD_GAINED = 0x8;
	public static final int SOUND_GOLD_SPENT = 0x9;
	public static final int SOUND_XP_GAINED = 0xA;
	
	// Variables
	private static final int MAX_STREAM_NUMBER = 4;
	
	// Sounds
	private SoundPool mSoundPool;
	private HashMap<Integer, Integer> mSoundMap;
	private AudioManager  mAudioManager;
	private Context mContext;

	// Music
	private MediaPlayer mBackgroundMusic;
	
	private static MusicManager mInstance = null;
	
	public static MusicManager getInstance(Context context, int backgroundMusicId){
		mInstance = new MusicManager();
		mInstance.initSounds(context, backgroundMusicId);
		return mInstance;
	}
	
	public static MusicManager getInstance() {
		synchronized (MusicManager.class) {
			if (mInstance == null) {
				mInstance = new MusicManager();
			}
			return mInstance;
		}
	}
	
	private MusicManager(){}
	
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public void initSounds(Context context, int backgroundMusicId) {
		this.mContext = context;
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			AudioAttributes.Builder attributesBuilder = new AudioAttributes.Builder();
			attributesBuilder.setContentType(AudioAttributes.CONTENT_TYPE_MUSIC);
			attributesBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
			attributesBuilder.setUsage(AudioAttributes.USAGE_GAME);
			SoundPool.Builder builder = new SoundPool.Builder();
			builder.setMaxStreams(MAX_STREAM_NUMBER);
			builder.setAudioAttributes(attributesBuilder.build());
			mSoundPool = builder.build();
		} else {
			mSoundPool = new SoundPool(MAX_STREAM_NUMBER, AudioManager.STREAM_MUSIC, 100);
		}
		
		
		
		mSoundMap = new HashMap<Integer, Integer>();
		mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		
		mBackgroundMusic = MediaPlayer.create(context, backgroundMusicId);
		mBackgroundMusic.setLooping(true);
		float dv = getDeviceVolume();
		mBackgroundMusic.setVolume(dv, dv);
	}
	
	public void registerSound(int index, int soundId) {
		mSoundMap.put(index, mSoundPool.load(mContext, soundId, 1));
	}
	
	public void playBackgroundMusic() {
		try{
			if(!mBackgroundMusic.isPlaying()){
					try {
						mBackgroundMusic.prepare();
					} catch (IOException e) {
						Log.e(TAG, e.getMessage());
					} catch (IllegalStateException e){
						Log.e(TAG, e.getMessage());
					}
				mBackgroundMusic.start();
			}
    	}catch(IllegalStateException e){
    		Log.e(TAG, e.getMessage());
    	}
	}
	
	public void pauseBackgroundMusic(){
		if(mBackgroundMusic.isPlaying()){
			mBackgroundMusic.pause();
		}
	}
	public void stopBackgroundMusic(){
		if(mBackgroundMusic.isPlaying()){
			mBackgroundMusic.stop();
		}
	}
	
	public void playSound (int index) {
		SharedPreferences mPreferences = mContext.getSharedPreferences(Constants.TAG_PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = mPreferences.edit();
		
		float mDeviceVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		mDeviceVolume = mDeviceVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		
		editor.putFloat(Constants.PREF_DEVICE_VOLUME, mDeviceVolume);
		editor.commit();
		
		mSoundPool.play((Integer) mSoundMap.get(index), mDeviceVolume, mDeviceVolume, 1, 0, 1f);
	}

	public void playSoundLoop (int index) {
		SharedPreferences mPreferences = mContext.getSharedPreferences(Constants.TAG_PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = mPreferences.edit();
		
		float mDeviceVolume = getDeviceVolume();
		
		editor.putFloat(Constants.PREF_DEVICE_VOLUME, mDeviceVolume);
		editor.commit();
		
		mSoundPool.play((Integer) mSoundMap.get(index), mDeviceVolume, mDeviceVolume, 1, -1, 1f);
	}
	
	public float getDeviceVolume(){
		float mCurrVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		float mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		return (mCurrVolume / mMaxVolume) * 100;
	}
	
	public void setDeviceVolume(float volumeValue){		
		float mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		int streamedVolume = (int) ((volumeValue * mMaxVolume) / 100);
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, streamedVolume, 0);
		
	}
}
