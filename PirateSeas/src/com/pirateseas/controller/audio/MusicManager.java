package com.pirateseas.controller.audio;

import java.util.HashMap;

import com.pirateseas.global.Constants;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;

@SuppressLint("UseSparseArrays")
public class MusicManager{
	private static final String TAG = "MusicManager";
	
	// Sound Ids
	public final static int SOUND_SHOT_FIRED = 0x0;
	public final static int SOUND_SHOT_HIT = 0x1;
	public final static int SOUND_SHOT_MISSED = 0x2;
	public final static int SOUND_SHOT_RELOADING = 0x3;
	public final static int SOUND_SHOT_EXPLOSION = 0x4;
	public final static int SOUND_WEATHER_FOG = 0x5;
	public final static int SOUND_WEATHER_STORM = 0x6;
	public final static int SOUND_WEATHER_MAELSTROM = 0x7;
	public final static int SOUND_ENEMY_APPEAR = 0x8;
	public final static int SOUND_GOLD_GAINED = 0x9;
	public final static int SOUND_GOLD_SPENT = 0xA;
	public final static int SOUND_XP_GAINED = 0xB;
	public final static int MUSIC_GAME_PAUSED = 0xC;
	public final static int MUSIC_BATTLE = 0xD;
	public final static int MUSIC_ISLAND = 0xE;
	public final static int MUSIC_GAME_MENU = 0xF;
		
	// Sounds
	private HashMap<Integer,Integer> mSoundKeys;
	private SoundPools mSoundPools;
	private AudioManager  mAudioManager;
	private Context mContext;

	// Music
	private MediaPlayer mBackgroundMusic;
	
	private static MusicManager mInstance = null;
	
	public static MusicManager getInstance(Context context,
			int backgroundMusicId) {
		if (mInstance == null) {
			mInstance = new MusicManager();
		}
		if (mInstance.mBackgroundMusic == null){
			mInstance.initSounds(context, backgroundMusicId);
		}
		return mInstance;
	}

	public static MusicManager getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new MusicManager();
		}
		if (mInstance.mAudioManager == null){
			mInstance.init(context);
		}
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
	
	private void init(Context context) {
		mSoundPools = new SoundPools();
		mSoundKeys = new HashMap<Integer,Integer>();
		mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
	}
	
	@SuppressLint("NewApi")
	public void initSounds(Context context, int backgroundMusicId) {
		this.mContext = context;
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			AudioAttributes.Builder attributesBuilder = new AudioAttributes.Builder();
			attributesBuilder.setContentType(AudioAttributes.CONTENT_TYPE_MUSIC);
			attributesBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
			attributesBuilder.setUsage(AudioAttributes.USAGE_GAME);			
		}
		
		if(mAudioManager == null)
			init(context);
		
		mBackgroundMusic = MediaPlayer.create(context, mSoundKeys.get(backgroundMusicId));
		mBackgroundMusic.setLooping(true);
		float dv = getDeviceVolume();
		mBackgroundMusic.setVolume(dv, dv);
	}
	
	public void registerSound(int soundId, int soundResource) {
		mSoundKeys.put(soundId, soundResource);
		mSoundPools.loadSound(mContext, String.valueOf(soundId), soundResource);
	}
	
	public void playBackgroundMusic() {
		try{
			if(!mBackgroundMusic.isPlaying()){
					try {
						mBackgroundMusic.start();
					} catch (IllegalStateException e){
						Log.e(TAG, e.getMessage());
					}
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
		
		int resourceId = mSoundKeys.get(index);
		
		mSoundPools.playSound(mContext, String.valueOf(index), resourceId, false);
	}

	public void playSoundLoop (int index) {
		SharedPreferences mPreferences = mContext.getSharedPreferences(Constants.TAG_PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = mPreferences.edit();
		
		float mDeviceVolume = getDeviceVolume();
		
		editor.putFloat(Constants.PREF_DEVICE_VOLUME, mDeviceVolume);
		editor.commit();
		
		int resourceId = mSoundKeys.get(index);
		
		mSoundPools.playSound(mContext, String.valueOf(index), resourceId, true);
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
