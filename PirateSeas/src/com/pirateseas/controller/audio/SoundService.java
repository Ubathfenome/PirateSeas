package com.pirateseas.controller.audio;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;

/**
* Service that manages all sounds and musics of the app
*
* @see: http://developer.android.com/guide/topics/media/mediaplayer.html
*/
public class SoundService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, AudioManager.OnAudioFocusChangeListener{
	
	MediaPlayer mMediaPlayer;
	AudioManager mAudioManager;
	
	public SoundService(){

	}
	
	public void initMediaPlayer(){
		// TODO ...initialize the MediaPlayer here...
		
		mMediaPlayer.setOnErrorListener(this);
	}

	@Override
	public void onAudioFocusChange(int focusChange) {
		switch (focusChange) {
			case AudioManager.AUDIOFOCUS_GAIN:
				// resume playback
				if (mMediaPlayer == null) initMediaPlayer();
				else if (!mMediaPlayer.isPlaying()) mMediaPlayer.start();
				mMediaPlayer.setVolume(1.0f, 1.0f);
				break;

			case AudioManager.AUDIOFOCUS_LOSS:
				// Lost focus for an unbounded amount of time: stop playback and release media player
				if (mMediaPlayer.isPlaying()) mMediaPlayer.stop();
				mMediaPlayer.release();
				mMediaPlayer = null;
				break;

			case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
				// Lost focus for a short time, but we have to stop
				// playback. We don't release the media player because playback
				// is likely to resume
				if (mMediaPlayer.isPlaying()) mMediaPlayer.pause();
				break;

			case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
				// Lost focus for a short time, but it's ok to keep playing
				// at an attenuated level
				if (mMediaPlayer.isPlaying()) mMediaPlayer.setVolume(0.1f, 0.1f);
				break;
		}		
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		// TODO ... react appropriately ...
        // The MediaPlayer has moved to the Error state, must be reset!
		return false;
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO ...
        if (intent.getAction().equals("ACTION_PLAY")) {
            //mMediaPlayer = ... // initialize it here
            //mMediaPlayer.setOnPreparedListener(this);
            //mMediaPlayer.prepareAsync(); // prepare async to not block main thread
        }
        
		return startId;
    }

	@Override
	public void onPrepared(MediaPlayer mp) {
		mp.start();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onDestroy() {
		if (mMediaPlayer != null) mMediaPlayer.release();
	}
}