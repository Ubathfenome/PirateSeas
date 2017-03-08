package com.pirateseas.controller.audio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.media.SoundPool;
import android.util.Log;

/**
 * Multi SoundPool to prevent memory error.
 */
public class SoundPools {
	private static final String TAG = "SoundPools";

	private static final int MAX_STREAMS_PER_POOL = 15;

	private List<SoundPoolContainer> containers;

	public SoundPools() {
		containers = Collections
				.synchronizedList(new ArrayList<SoundPoolContainer>());
	}

	public void loadSound(Context context, String soundId, int id) {
		Log.d(TAG, "SouldPools load sound " + soundId);
		try {
			for (SoundPoolContainer container : containers) {
				if (container.contains(soundId)) {
					return;
				}
			}
			for (SoundPoolContainer container : containers) {
				if (!container.isFull()) {
					container.load(context, soundId, id);
					return;
				}
			}
			SoundPoolContainer container = new SoundPoolContainer();
			containers.add(container);
			container.load(context, soundId ,id);
		} catch (Exception e) {
			Log.w(TAG, "Load sound error", e);
		}
	}

	public void playSound(Context context, String soundId, int id, boolean loopSound) {
		int loop = loopSound?1:0;
		Log.d(TAG, "SouldPools play sound " + soundId);
		try {
			for (SoundPoolContainer container : containers) {
				if (container.contains(soundId)) {
					container.play(context, soundId, id, loop);
					return;
				}
			}
			for (SoundPoolContainer container : containers) {
				if (!container.isFull()) {
					container.play(context, soundId, id, loop);
					return;
				}
			}
			SoundPoolContainer container = new SoundPoolContainer();
			containers.add(container);

			container.play(context, soundId, id, loop);
		} catch (Exception e) {
			Log.w(TAG, "Play sound error for id:" + soundId, e);
		}
	}

	public void onPause() {
		for (SoundPoolContainer container : containers) {
			container.onPause();
		}
	}

	public void onResume() {
		for (SoundPoolContainer container : containers) {
			container.onResume();
		}
	}

	private static class SoundPoolContainer {
		SoundPool soundPool;
		Map<String, Integer> soundMap;
		AtomicInteger size;

		@SuppressWarnings("deprecation")
		public SoundPoolContainer() {
			this.soundPool = new SoundPool(MAX_STREAMS_PER_POOL,
					android.media.AudioManager.STREAM_MUSIC, 0);
			this.soundMap = new ConcurrentHashMap<String, Integer>(
					MAX_STREAMS_PER_POOL);
			this.size = new AtomicInteger(0);
		}

		public void load(Context context, String soundId, int id) {
			try {
				this.size.incrementAndGet();
				soundMap.put(soundId, soundPool.load(context, id, 1));
			} catch (Exception e) {
				this.size.decrementAndGet();
				Log.w(TAG, "Load sound error", e);
			}
		}

		public void play(Context context, String sound, int id, int loop) {
			android.media.AudioManager audioManager = (android.media.AudioManager) context
					.getSystemService(Context.AUDIO_SERVICE);
			final int streamVolume = audioManager
					.getStreamVolume(android.media.AudioManager.STREAM_MUSIC);
			Integer soundId = soundMap.get(id);
			if (soundId == null) {
				soundPool
						.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
							@Override
							public void onLoadComplete(SoundPool soundPool,
									int sampleId, int status) {
								soundPool.play(sampleId, streamVolume,
										streamVolume, 1, 0, 1f);
							}
						});
				this.size.incrementAndGet();
				soundPool.load(context,id, 1);
			} else {
				try {
					soundPool.play(soundId, streamVolume, streamVolume, 1, 0,
							1f);
				} catch (Exception e) {
					Log.w(TAG, "Play sound error", e);
				}
			}
		}

		public boolean contains(String id) {
			return soundMap.containsKey(id);
		}

		public boolean isFull() {
			return this.size.get() >= MAX_STREAMS_PER_POOL;
		}

		public void onPause() {
			try {
				soundPool.autoPause();
			} catch (Exception e) {
				Log.w(TAG, "Pause SoundPool error", e);
			}
		}

		public void onResume() {
			try {
				soundPool.autoResume();
			} catch (Exception e) {
				Log.w(TAG, "Resume SoundPool error", e);
			}
		}
	}

}
