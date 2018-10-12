//
//  Agora Rtc Engine SDK
//
//  Copyright (c) 2018 Agora.io. All rights reserved.
//

#ifndef AGORA_RTC_ENGINE_H
#define AGORA_RTC_ENGINE_H
#include "AgoraBase.h"
#include "IAgoraService.h"

namespace agora {
namespace rtc {
    typedef unsigned int uid_t;
    typedef void* view_t;
/** Maximum device ID length.
*/
enum MAX_DEVICE_ID_LENGTH_TYPE
{
  /** The maximum device ID length is 512.
  */
    MAX_DEVICE_ID_LENGTH = 512
};
/** Format of the quality report.
*/
enum QUALITY_REPORT_FORMAT_TYPE
{
  /** 0: The quality report in JSON format,
  */
    QUALITY_REPORT_JSON = 0,
    /** 1: The quality report in HTML format.
    */
    QUALITY_REPORT_HTML = 1,
};

enum MEDIA_ENGINE_EVENT_CODE_TYPE
{
    MEDIA_ENGINE_RECORDING_ERROR = 0,
    MEDIA_ENGINE_PLAYOUT_ERROR = 1,
    MEDIA_ENGINE_RECORDING_WARNING = 2,
    MEDIA_ENGINE_PLAYOUT_WARNING = 3,
    MEDIA_ENGINE_AUDIO_FILE_MIX_FINISH = 10,
    MEDIA_ENGINE_AUDIO_FAREND_MUSIC_BEGINS = 12,
    MEDIA_ENGINE_AUDIO_FAREND_MUSIC_ENDS = 13,
    // media engine role changed
    MEDIA_ENGINE_ROLE_BROADCASTER_SOLO = 20,
    MEDIA_ENGINE_ROLE_BROADCASTER_INTERACTIVE = 21,
    MEDIA_ENGINE_ROLE_AUDIENCE = 22,
    MEDIA_ENGINE_ROLE_COMM_PEER = 23,
    MEDIA_ENGINE_ROLE_GAME_PEER = 24,
    // iOS adm sample rate changed
    MEDIA_ENGINE_AUDIO_ADM_REQUIRE_RESTART = 110,
    MEDIA_ENGINE_AUDIO_ADM_SPECIAL_RESTART = 111,
};
/** Media device state.
*/
enum MEDIA_DEVICE_STATE_TYPE
{
  /** 1: The device is active.
  */
    MEDIA_DEVICE_STATE_ACTIVE = 1,
    /** 2: The device is disabled.
    */
    MEDIA_DEVICE_STATE_DISABLED = 2,
    /** 4: The device is not present.
    */
    MEDIA_DEVICE_STATE_NOT_PRESENT = 4,
    /** 8: The device is unplugged.
    */
    MEDIA_DEVICE_STATE_UNPLUGGED = 8
};

/** Media device type.
*/
enum MEDIA_DEVICE_TYPE
{
  /** -1: Unknown device type.
  */
    UNKNOWN_AUDIO_DEVICE = -1,
    /** 0: Audio playback device.
    */
    AUDIO_PLAYOUT_DEVICE = 0,
    /** 1: Audio recording device.
    */
    AUDIO_RECORDING_DEVICE = 1,
    /** 2: Video renderer
    */
    VIDEO_RENDER_DEVICE = 2,
    /** 3: Video capturer
    */
    VIDEO_CAPTURE_DEVICE = 3,
    /** 4: Application audio playback device.
    */
    AUDIO_APPLICATION_PLAYOUT_DEVICE = 4,
};

/** Audio recording quality.
*/
enum AUDIO_RECORDING_QUALITY_TYPE
{
  /** 0: Low audio recording quality.
  */
    AUDIO_RECORDING_QUALITY_LOW = 0,
    /** 1: Medium audio recording quality.
    */
    AUDIO_RECORDING_QUALITY_MEDIUM = 1,
    /** 2: High audio recording quality.
    */
    AUDIO_RECORDING_QUALITY_HIGH = 2,
};

/** Network quality. */
enum QUALITY_TYPE
{
      /** 0: The network quality is unknown. */
    QUALITY_UNKNOWN = 0,
    /**  1: The network quality is excellent. */
    QUALITY_EXCELLENT = 1,
      /** 2: The network quality is quite good, but the bitrate may be slightly lower than excellent. */
    QUALITY_GOOD = 2,
      /** 3: Users can feel the communication slightly impaired. */
    QUALITY_POOR = 3,
      /** 4: Users can communicate only not very smoothly. */
    QUALITY_BAD = 4,
     /** 5: The network is so bad that users can hardly communicate. */
    QUALITY_VBAD = 5,
       /** 6: The network is down  and users cannot communicate at all. */
    QUALITY_DOWN = 6,
    QUALITY_UNSUPPORTED = 7,
};

/** Video display mode. */
enum RENDER_MODE_TYPE
{
  /**
1: Uniformly scale the video until it fills the visible boundaries (cropped). One dimension of the video may have clipped contents.
 */
    RENDER_MODE_HIDDEN = 1,
    /**
2: Uniformly scale the video until one of its dimension fits the boundary (zoomed to fit). Areas that are not filled due to the disparity in the aspect ratio will be filled with black.
 */
    RENDER_MODE_FIT = 2,
    /** @deprecated
     3：This mode is obsolete.
     */
    RENDER_MODE_ADAPTIVE = 3,
};

/** Video mirror mode. */
enum VIDEO_MIRROR_MODE_TYPE
{
      /** 0: Default mirror mode determined by the SDK. */
    VIDEO_MIRROR_MODE_AUTO = 0,//determined by SDK
        /** 1: Enabled mirror mode */
    VIDEO_MIRROR_MODE_ENABLED = 1,//enabled mirror
        /** 2: Disabled mirror mode */
    VIDEO_MIRROR_MODE_DISABLED = 2,//disable mirror
};

/** @deprecated
 Video profile. */
enum VIDEO_PROFILE_TYPE
{
    /** 0: 160 x 120  @ 15 fps */          // res       fps
    VIDEO_PROFILE_LANDSCAPE_120P = 0,         // 160x120   15
      /** 2: 120 x 120 @ 15 fps */
    VIDEO_PROFILE_LANDSCAPE_120P_3 = 2,       // 120x120   15
        /** 10: 320 x 180 @ 15 fps */
    VIDEO_PROFILE_LANDSCAPE_180P = 10,        // 320x180   15
        /** 12: 180 x 180  @ 15 fps */
    VIDEO_PROFILE_LANDSCAPE_180P_3 = 12,      // 180x180   15
        /** 13: 240 x 180 @ 15 fps */
    VIDEO_PROFILE_LANDSCAPE_180P_4 = 13,      // 240x180   15
        /** 20: 320 x 240 @ 15 fps */
    VIDEO_PROFILE_LANDSCAPE_240P = 20,        // 320x240   15
    /** 22: 240 x 240 @ 15 fps */
    VIDEO_PROFILE_LANDSCAPE_240P_3 = 22,      // 240x240   15
      /** 23: 424 x 240 @ 15 fps */
    VIDEO_PROFILE_LANDSCAPE_240P_4 = 23,      // 424x240   15
      /** 30: 640 x 360 @ 15 fps */
    VIDEO_PROFILE_LANDSCAPE_360P = 30,        // 640x360   15
      /** 32: 360 x 360 @ 15 fps */
    VIDEO_PROFILE_LANDSCAPE_360P_3 = 32,      // 360x360   15
    /** 33: 640 x 360 @ 30 fps */
    VIDEO_PROFILE_LANDSCAPE_360P_4 = 33,      // 640x360   30
      /** 35: 360 x 360 @ 30 fps */
    VIDEO_PROFILE_LANDSCAPE_360P_6 = 35,      // 360x360   30
    /** 36: 480 x 360 @ 15 fps */
    VIDEO_PROFILE_LANDSCAPE_360P_7 = 36,      // 480x360   15
      /** 37: 480 x 360 @ 30 fps */
    VIDEO_PROFILE_LANDSCAPE_360P_8 = 37,      // 480x360   30
      /** 38: 640 x 360 @ 15 fps */
    VIDEO_PROFILE_LANDSCAPE_360P_9 = 38,      // 640x360   15
        /** 39: 640 x 360 @ 24 fps */
    VIDEO_PROFILE_LANDSCAPE_360P_10 = 39,     // 640x360   24
      /** 100: 640 x 360 @ 24 fps */
    VIDEO_PROFILE_LANDSCAPE_360P_11 = 100,    // 640x360   24
      /** 40: 640 x 480 @ 15 fps */
    VIDEO_PROFILE_LANDSCAPE_480P = 40,        // 640x480   15
    /** 42: 480 x 480 @ 15 fps */
    VIDEO_PROFILE_LANDSCAPE_480P_3 = 42,      // 480x480   15
    /** 43: 640 x 480 @ 30 fps */
    VIDEO_PROFILE_LANDSCAPE_480P_4 = 43,      // 640x480   30
       /** 45: 480 x 480 @ 30 fps */
    VIDEO_PROFILE_LANDSCAPE_480P_6 = 45,      // 480x480   30
    /** 47: 848 x 480 @ 15 fps */
    VIDEO_PROFILE_LANDSCAPE_480P_8 = 47,      // 848x480   15
        /** 48: 848 x 480 @ 30 fps */
    VIDEO_PROFILE_LANDSCAPE_480P_9 = 48,      // 848x480   30
      /** 49: 640 x 480 @ 10 fps */
    VIDEO_PROFILE_LANDSCAPE_480P_10 = 49,     // 640x480   10
      /** 50: 1280 x 720 @ 15 fps */
    VIDEO_PROFILE_LANDSCAPE_720P = 50,        // 1280x720  15
    /** 52: 1280 x 720 @ 30 fps */
    VIDEO_PROFILE_LANDSCAPE_720P_3 = 52,      // 1280x720  30
      /** 54: 960 x 720 @ 15 fps */
    VIDEO_PROFILE_LANDSCAPE_720P_5 = 54,      // 960x720   15
      /** 55: 960 x 720 @ 30 fps */
    VIDEO_PROFILE_LANDSCAPE_720P_6 = 55,      // 960x720   30
    /** 60: 1920 x 1080 @ 15 fps */
    VIDEO_PROFILE_LANDSCAPE_1080P = 60,       // 1920x1080 15
    /** 62: 1920 x 1080 @ 30 fps */
    VIDEO_PROFILE_LANDSCAPE_1080P_3 = 62,     // 1920x1080 30
    /** 64: 1920 x 1080 @ 60 fps */
    VIDEO_PROFILE_LANDSCAPE_1080P_5 = 64,     // 1920x1080 60
    /** 66: 2560 x 1440 @ 30 fps */
    VIDEO_PROFILE_LANDSCAPE_1440P = 66,       // 2560x1440 30
      /** 67: 2560 x 1440 @ 60 fps */
    VIDEO_PROFILE_LANDSCAPE_1440P_2 = 67,     // 2560x1440 60
      /** 70: 3840 x 2160 @ 30 fps */
    VIDEO_PROFILE_LANDSCAPE_4K = 70,          // 3840x2160 30
    /** 72: 3840 x 2160 @ 60 fps */
    VIDEO_PROFILE_LANDSCAPE_4K_3 = 72,        // 3840x2160 60
/** 1000: 120 x 160 @ 15 fps */
    VIDEO_PROFILE_PORTRAIT_120P = 1000,       // 120x160   15
     /** 1002: 120 x 120 @ 15 fps */
    VIDEO_PROFILE_PORTRAIT_120P_3 = 1002,     // 120x120   15
        /** 1010: 180 x 320 @ 15 fps */
    VIDEO_PROFILE_PORTRAIT_180P = 1010,       // 180x320   15
      /** 1012: 180 x 180 @ 15 fps */
    VIDEO_PROFILE_PORTRAIT_180P_3 = 1012,     // 180x180   15
      /** 1013: 180 x 240 @ 15 fps */
    VIDEO_PROFILE_PORTRAIT_180P_4 = 1013,     // 180x240   15
      /** 1020: 240 x 320 @ 15 fps */
    VIDEO_PROFILE_PORTRAIT_240P = 1020,       // 240x320   15
    /** 1022: 240 x 240 @ 15 fps */
    VIDEO_PROFILE_PORTRAIT_240P_3 = 1022,     // 240x240   15
      /** 1023: 240 x 424 @ 15 fps */
    VIDEO_PROFILE_PORTRAIT_240P_4 = 1023,     // 240x424   15
    /** 1030: 360 x 640 @ 15 fps */
    VIDEO_PROFILE_PORTRAIT_360P = 1030,       // 360x640   15
    /** 1032: 360 x 360 @ 15 fps */
    VIDEO_PROFILE_PORTRAIT_360P_3 = 1032,     // 360x360   15
    /** 1033: 360 x 640 @ 30 fps */
    VIDEO_PROFILE_PORTRAIT_360P_4 = 1033,     // 360x640   30
        /** 1035: 360 x 360 @ 30 fps */
    VIDEO_PROFILE_PORTRAIT_360P_6 = 1035,     // 360x360   30
      /** 1036: 360 x 480 @ 15 fps */
    VIDEO_PROFILE_PORTRAIT_360P_7 = 1036,     // 360x480   15
      /** 1037: 360 x 480 @ 30 fps */
    VIDEO_PROFILE_PORTRAIT_360P_8 = 1037,     // 360x480   30
        /** 1038: 360 x 640 @ 15 fps */
    VIDEO_PROFILE_PORTRAIT_360P_9 = 1038,     // 360x640   15
      /** 1039: 360 x 640 @ 24 fps */
    VIDEO_PROFILE_PORTRAIT_360P_10 = 1039,    // 360x640   24
      /** 1100: 360 x 640 @ 24 fps */
    VIDEO_PROFILE_PORTRAIT_360P_11 = 1100,    // 360x640   24
      /** 1040: 480 x 640 @ 15 fps */
    VIDEO_PROFILE_PORTRAIT_480P = 1040,       // 480x640   15
    /** 1042: 480 x 480 @ 15 fps */
    VIDEO_PROFILE_PORTRAIT_480P_3 = 1042,     // 480x480   15
    /** 1043: 480 x 640 @ 30 fps */
    VIDEO_PROFILE_PORTRAIT_480P_4 = 1043,     // 480x640   30
        /** 1045: 480 x 480 @ 30 fps */
    VIDEO_PROFILE_PORTRAIT_480P_6 = 1045,     // 480x480   30
        /** 1047: 480 x 848 @ 15 fps */
    VIDEO_PROFILE_PORTRAIT_480P_8 = 1047,     // 480x848   15
      /** 1048: 480 x 848 @ 30 fps */
    VIDEO_PROFILE_PORTRAIT_480P_9 = 1048,     // 480x848   30
    /** 1049: 480 x 640 @ 10 fps */
    VIDEO_PROFILE_PORTRAIT_480P_10 = 1049,    // 480x640   10
      /** 1050: 720 x 1280 @ 15 fps */
    VIDEO_PROFILE_PORTRAIT_720P = 1050,       // 720x1280  15
      /** 1052: 720 x 1280 @ 30 fps */
    VIDEO_PROFILE_PORTRAIT_720P_3 = 1052,     // 720x1280  30
      /** 1054: 720 x 960 @ 15 fps */
    VIDEO_PROFILE_PORTRAIT_720P_5 = 1054,     // 720x960   15
       /** 1055: 720 x 960 @ 30 fps */
    VIDEO_PROFILE_PORTRAIT_720P_6 = 1055,     // 720x960   30
      /** 1060: 1080 x 1920 @ 15 fps */
    VIDEO_PROFILE_PORTRAIT_1080P = 1060,      // 1080x1920 15
        /** 1062: 1080 x 1920 @ 30 fps */
    VIDEO_PROFILE_PORTRAIT_1080P_3 = 1062,    // 1080x1920 30
        /** 1064: 1080 x 1920 @ 60 fps */
    VIDEO_PROFILE_PORTRAIT_1080P_5 = 1064,    // 1080x1920 60
      /** 1066: 1440 x 2560 @ 30 fps */
    VIDEO_PROFILE_PORTRAIT_1440P = 1066,      // 1440x2560 30
    /** 1067: 1440 x 2560 @ 60 fps */
    VIDEO_PROFILE_PORTRAIT_1440P_2 = 1067,    // 1440x2560 60
        /** 1070: 2160 x 3840 @ 30 fps */
    VIDEO_PROFILE_PORTRAIT_4K = 1070,         // 2160x3840 30
    /** 1072: 2160 x 3840 @ 60 fps */
    VIDEO_PROFILE_PORTRAIT_4K_3 = 1072,       // 2160x3840 60
    /** Default 640 x 360 @ 15 fps */
    VIDEO_PROFILE_DEFAULT = VIDEO_PROFILE_LANDSCAPE_360P,
};

/** Audio profile.

Sets the sampling rate, bitrate, encode mode, and the number of channels:*/
enum AUDIO_PROFILE_TYPE // sample rate, bit rate, mono/stereo, speech/music codec
{
  /**
 * 0: Default audio profile. In the communication mode, the default value is 1; in the live-broadcast mode, the default value is 2.
 */
    AUDIO_PROFILE_DEFAULT = 0, // use default settings
    /**
 * 1: Sampling rate 32 kHz, audio encoding, single channel, and bitrate up to 18 kbit/s.
 */
    AUDIO_PROFILE_SPEECH_STANDARD = 1, // 32Khz, 18Kbps, mono, speech
    /**
 * 2: Sampling rate 48 kHz, music encoding, single channel, and bitrate up to 48 kbit/s.
 */
    AUDIO_PROFILE_MUSIC_STANDARD = 2, // 48Khz, 48Kbps, mono, music
    /**
 * 3: Sampling rate 48 kHz, music encoding, dual-channel, and bitrate up to 56 kbit/s.
 */
    AUDIO_PROFILE_MUSIC_STANDARD_STEREO = 3, // 48Khz, 56Kbps, stereo, music
    /**
 * 4: Sampling rate 48 kHz, music encoding, single channel, and bitrate up to 128 kbit/s.
 */
    AUDIO_PROFILE_MUSIC_HIGH_QUALITY = 4, // 48Khz, 128Kbps, mono, music
    /**
 * 5: Sampling rate 48 kHz, music encoding, dual-channel, and bitrate up to 192 kbit/s.
 */
    AUDIO_PROFILE_MUSIC_HIGH_QUALITY_STEREO = 5, // 48Khz, 192Kbps, stereo, music
    AUDIO_PROFILE_IOT                       = 6,   // G722
    AUDIO_PROFILE_NUM = 7,
};

/** Audio application scenario.
*/
enum AUDIO_SCENARIO_TYPE // set a suitable scenario for your app type
{
      /** 0: Default */
    AUDIO_SCENARIO_DEFAULT = 0,
      /** 1: Entertainment scenario that supports voice during gameplay */
    AUDIO_SCENARIO_CHATROOM_ENTERTAINMENT = 1,
      /** 2: Education scenario that prioritizes fluency and stability */
    AUDIO_SCENARIO_EDUCATION = 2,
    /** 3: Live gaming scenario that needs to enable the gaming audio effects  in the speaker mode in a live broadcast scenario. Choose this scenario if you wish to achieve high-fidelity music playback */
    AUDIO_SCENARIO_GAME_STREAMING = 3,
    /** 4: Showroom scenario that optimizes the audio quality with professional external equipment */
    AUDIO_SCENARIO_SHOWROOM = 4,
      /** 5: Gaming scenario */
    AUDIO_SCENARIO_CHATROOM_GAMING = 5,
    AUDIO_SCENARIO_IOT = 6,
    AUDIO_SCENARIO_NUM = 7,
};

 /** Channel profile.
 */
enum CHANNEL_PROFILE_TYPE
{
  /** 0: Communication.

   This is used in one-on-one calls, where all users in the channel can talk freely.*/
	CHANNEL_PROFILE_COMMUNICATION = 0,
  /** 1: Live Broadcast.

     Host and audience roles that can be set by calling IRtcEngine::setClientRole. The host sends and receives voice, while the audience receives voice only with the sending function disabled.*/
	CHANNEL_PROFILE_LIVE_BROADCASTING = 1,
  /** 2: Gaming.

   Any user in the channel can talk freely. This mode uses the codec with low-power consumption and low bitrate by default.*/
    CHANNEL_PROFILE_GAME = 2,
};

/** Client role in a live broadcast. */
enum CLIENT_ROLE_TYPE
{
    /** 1: Host */
    CLIENT_ROLE_BROADCASTER = 1,
        /** 2: Audience */
    CLIENT_ROLE_AUDIENCE = 2,
};

/** Reason for the user being offline. */
enum USER_OFFLINE_REASON_TYPE
{
    /** 0: A user has quit the call. */
    USER_OFFLINE_QUIT = 0,
    /** 1: The SDK timed out and the user dropped offline because it has not received any data package within a certain period of time. If a user quits the call and the message is not passed to the SDK (due to an unreliable channel), the SDK assumes the event has timed out. */
    USER_OFFLINE_DROPPED = 1,
      /** 2: User switched to an audience */
    USER_OFFLINE_BECOME_AUDIENCE = 2,
};

/** Status of importing an external video stream in a live broadcast. */
enum INJECT_STREAM_STATUS
{
    /** 0: The external video stream imported successfully. */
    INJECT_STREAM_STATUS_START_SUCCESS = 0,
    /** 1: The external video stream already exists. */
    INJECT_STREAM_STATUS_START_ALREADY_EXISTS = 1,
        /** 2: The external video stream import is unauthorized */
    INJECT_STREAM_STATUS_START_UNAUTHORIZED = 2,
    /** 3: Import external video stream timeout. */
    INJECT_STREAM_STATUS_START_TIMEDOUT = 3,
      /** 4: The external video stream failed to import. */
    INJECT_STREAM_STATUS_START_FAILED = 4,
      /** 5: The external video stream imports successfully. */
    INJECT_STREAM_STATUS_STOP_SUCCESS = 5,
    /** 6: No external video stream is found. */
    INJECT_STREAM_STATUS_STOP_NOT_FOUND = 6,
        /** 7: The external video stream is stopped from being unauthorized. */
    INJECT_STREAM_STATUS_STOP_UNAUTHORIZED = 7,
    /** 8: Importing the external video stream timeout. */
    INJECT_STREAM_STATUS_STOP_TIMEDOUT = 8,
      /** 9: Importing the external video stream failed. */
    INJECT_STREAM_STATUS_STOP_FAILED = 9,
      /** 10: The external video stream is broken. */
    INJECT_STREAM_STATUS_BROKEN = 10,
};
/** Remote video stream type. */
enum REMOTE_VIDEO_STREAM_TYPE
{
      /** 0: High-video stream */
    REMOTE_VIDEO_STREAM_HIGH = 0,
      /** 1: Low-video stream */
    REMOTE_VIDEO_STREAM_LOW = 1,
};

/** Use mode of the onRecordAudioFrame callback. */
enum RAW_AUDIO_FRAME_OP_MODE_TYPE
{
    /** 0: Read-only mode: Users only read the AudioFrame data without modifying anything. For example, when users acquire data with the Agora SDK then push the RTMP streams. */
    RAW_AUDIO_FRAME_OP_MODE_READ_ONLY = 0,
    /** 1: Write-only mode: Users replace the AudioFrame data with their own data and pass them to the SDK for encoding. For example, when users acquire data. */
    RAW_AUDIO_FRAME_OP_MODE_WRITE_ONLY = 1,
    /** 2: Read and write mode: Users read the data from AudioFrame, modify it, and then play it. For example, when users have their own sound-effect processing module and do some voice pre-processing such as a voice change. */
    RAW_AUDIO_FRAME_OP_MODE_READ_WRITE = 2,
};

/** Audio-sampling rate. */
enum AUDIO_SAMPLE_RATE_TYPE
{
    /** 32000: 32 kHz */
    AUDIO_SAMPLE_RATE_32000 = 32000,
    /** 44100: 44.1 kHz */
    AUDIO_SAMPLE_RATE_44100 = 44100,
      /** 48000: 48 kHz */
    AUDIO_SAMPLE_RATE_48000 = 48000,
};

/** Video codec profile type. */
enum VIDEO_CODEC_PROFILE_TYPE
{  /** 66: Baseline video codec profile. Generally used on video call on mobile phones. */
    VIDEO_CODEC_PROFILE_BASELINE = 66,
    /** 77: Main video codec profile. Generally used on mainstream electronics such as mp4, portable video player, PSP and iPod. */
    VIDEO_CODEC_PROFILE_MAIN = 77,
      /**  100: (Default) High video codec profile. Generally used on high-resolution broadcast or television. */
    VIDEO_CODEC_PROFILE_HIGH = 100,
};

/** Audio equalization band frequency. */
enum AUDIO_EQUALIZATION_BAND_FREQUENCY
{
    /** 0: 31 Hz */
    AUDIO_EQUALIZATION_BAND_31 = 0,
      /** 1: 62 Hz */
    AUDIO_EQUALIZATION_BAND_62 = 1,
    /** 2: 125 Hz */
    AUDIO_EQUALIZATION_BAND_125 = 2,
      /** 3: 250 Hz */
    AUDIO_EQUALIZATION_BAND_250 = 3,
      /** 4: 500 Hz */
    AUDIO_EQUALIZATION_BAND_500 = 4,
        /** 5: 1 kHz */
    AUDIO_EQUALIZATION_BAND_1K = 5,
        /** 6: 2 kHz */
    AUDIO_EQUALIZATION_BAND_2K = 6,
        /** 7: 4 kHz */
    AUDIO_EQUALIZATION_BAND_4K = 7,
        /** 8: 8 kHz */
    AUDIO_EQUALIZATION_BAND_8K = 8,
      /** 9: 16 kHz */
    AUDIO_EQUALIZATION_BAND_16K = 9,
};

/** Audio reverberation type. */
enum AUDIO_REVERB_TYPE
{
    /** 0: (dB, -20 to 10), the level of the dry signal */
    AUDIO_REVERB_DRY_LEVEL = 0, // (dB, [-20,10]), the level of the dry signal
/** 1: (dB, -20 to 10), the level of the early reflection signal (wet signal) */
    AUDIO_REVERB_WET_LEVEL = 1, // (dB, [-20,10]), the level of the early reflection signal (wet signal)
    /** 2: (0 to 100), the room size of the reflection */
    AUDIO_REVERB_ROOM_SIZE = 2, // ([0, 100]), the room size of the reflection
    /** 3: (ms, 0 to 200), the length of the initial delay of the wet signal in ms */
    AUDIO_REVERB_WET_DELAY = 3, // (ms, [0, 200]), the length of the initial delay of the wet signal in ms
    /** 3: (0 to 100), the strength of the late reverberation */
    AUDIO_REVERB_STRENGTH = 4, // ([0, 100]), the strength of the late reverberation
};

/** Remote video state. */
enum REMOTE_VIDEO_STATE
{
    // REMOTE_VIDEO_STATE_STOPPED is not used at this version. Ignore this value.
    // REMOTE_VIDEO_STATE_STOPPED = 0,  // Default state, video is started or remote user disabled/muted video stream
      /** 1: Remote video is playing. */
      REMOTE_VIDEO_STATE_RUNNING = 1,  // Running state, remote video can be displayed normally
      /** 2: Remote video is frozen. */
      REMOTE_VIDEO_STATE_FROZEN = 2,    // Remote video is frozen, probably due to network issue.
};

/** Video frame rate. */
enum FRAME_RATE
{
      /** 1: 1 fps */
    FRAME_RATE_FPS_1 = 1, // 1 frame per second
        /** 7: 7 fps */
    FRAME_RATE_FPS_7 = 7, // 7 frames per second
      /** 10: 10 fps */
    FRAME_RATE_FPS_10 = 10, // 10 frames per second
    /** 15: 15 fps */
    FRAME_RATE_FPS_15 = 15, // 15 frames per second
        /** 24: 24 fps */
    FRAME_RATE_FPS_24 = 24, // 24 frames per second
    /** 30: 30 fps */
    FRAME_RATE_FPS_30 = 30, // 30 frames per second
    /** 60: 60 fps */
    FRAME_RATE_FPS_60 = 60, // 60 frames per second @ [Platform=(WINDOWS, MacOS)]
};

/** Video output orientation mode.
 */
enum ORIENTATION_MODE {
  /** 0: Adaptive mode (Default).

   The video encoder adapts to the orientation mode of the video input device.

   - If the width of the captured video from the SDK is larger than the height, the video sent out by the encoder is in landscape mode. The encoder also sends out the rotational information of the video, and the receiving end will rotate the received video based on the rotational information.
   - When a custom video source is used, the output video from the encoder inherits the orientation of the original video. If the original video is in  portrait mode, the output video from the encoder is also in portrait mode. The encoder also sends out the rotational information of the video to the receiver.

   */
    ORIENTATION_MODE_ADAPTIVE = 0,
    /** 1: Landscape mode.

The video encoder always sends out the video in landscape mode. The original video is rotated before being sent out and the rotational information is therefore 0. This mode applies to scenarios involving CDN live streaming.
   */
    ORIENTATION_MODE_FIXED_LANDSCAPE = 1,
    /** 2: Portrait mode.

The video encoder always sends out the video in portrait mode. The original video is rotated before being sent out and the rotational information is therefore 0. This mode applies to scenarios involving CDN live streaming.*/
    ORIENTATION_MODE_FIXED_PORTRAIT = 2,
};

/** Stream fallback options. */
enum STREAM_FALLBACK_OPTIONS
{
  /** 0: (Default) No fallback operation for the stream when the network condition is poor. The stream quality cannot be guaranteed. */

    STREAM_FALLBACK_OPTION_DISABLED = 0,
    /** 1: Under poor network conditions, the SDK will send or receive #REMOTE_VIDEO_STREAM_LOW. You can only set this option in RtcEngineParameters::setRemoteSubscribeFallbackOption. Nothing happens when you set this in RtcEngineParameters::setLocalPublishFallbackOption. */
    STREAM_FALLBACK_OPTION_VIDEO_STREAM_LOW = 1,
    /** 2: Under poor network conditions, the SDK may receive REMOTE_VIDEO_STREAM_LOW first, but if the network still does not allow displaying the video, the SDK will send or receive audio only. */
    STREAM_FALLBACK_OPTION_AUDIO_ONLY = 2,
};

/** Properties of the audio volume information.
 */

struct AudioVolumeInfo
{
  /**
 User ID of the speaker.
 */
    uid_t uid;
    /** The volume of the speaker that ranges from 0 (lowest volume) to 255 (highest volume).
 */
    unsigned int volume; // [0,255]
};

/** Statistics of the channel.
 */
struct RtcStats
{
  /**
 Call duration (s), represented by an aggregate value.
 */
    unsigned int duration;
    /**
 Total number of bytes transmitted, represented by an aggregate value.
 */
    unsigned int txBytes;
    /**
  Total number of bytes received, represented by an aggregate value.
 */
    unsigned int rxBytes;
    /**
 Transmission bitrate (kbit/s), represented by an instantaneous value.
 */
    unsigned short txKBitRate;
    /**
 Receive bitrate (kbit/s), represented by an instantaneous value.
 */
    unsigned short rxKBitRate;
    /**
     Audio receive bitrate (kbit/s), represented by an instantaneous value.
     */
    unsigned short rxAudioKBitRate;
    /**
 Audio transmission bitrate (kbit/s), represented by an instantaneous value.
 */
    unsigned short txAudioKBitRate;
    /**
     Video receive bitrate (kbit/s), represented by an instantaneous value.
     */
    unsigned short rxVideoKBitRate;
    /**
 Video transmission bitrate (kbit/s), represented by an instantaneous value.
 */
    unsigned short txVideoKBitRate;
    /** client-server latency (ms)
     */
    unsigned short lastmileDelay;
    /**
 Number of users in the channel.
 */
    unsigned int userCount;
    /**
 Application CPU usage (%).
 */
    double cpuAppUsage;
    /**
 System CPU usage (%).
 */
    double cpuTotalUsage;
};

/** Statistics of the local video stream.
 */
struct LocalVideoStats
{
  /**
 Total bytes sent since last count.
 */
    int sentBitrate;
    /**
 Total frames sent since last count.
 */
    int sentFrameRate;
};
/** Statistics of the remote video stream.
 */
struct RemoteVideoStats
{
  /**
 User ID of the user sending the video streams.
 */
    uid_t uid;
    /** @deprecated Time delay (ms).
 */
    int delay;
/**
 Width (pixels) of the video stream.
 */
	int width;
  /**
 Height (pixels) of the video stream.
 */
	int height;
  /**
 Data receive bitrate (kbit/s) since the last count.
 */
	int receivedBitrate;
  /**
 Data receive frame rate (fps) since the last count.
 */
	int receivedFrameRate;
  /**
   Remote video stream type (high- or low-video stream): #REMOTE_VIDEO_STREAM_TYPE

 */
    REMOTE_VIDEO_STREAM_TYPE rxStreamType;
};

/** Rtc video compositing layout.
 */
struct VideoCompositingLayout
{
    struct Region {
      /** User ID of the user’s video for the region display.
      */
        uid_t uid;
        /** Horizontal position of the region on the screen.
        */
        double x;//[0,1]
        /** Vertical position of the region on the screen.
        */
        double y;//[0,1]
        /**
Actual width of the region.
 */
        double width;//[0,1]
        /**
Actual height of the region. */
        double height;//[0,1]
        /** Layer position of the region:

        - 0 means the region is at the bottom layer.
        - 100 means the region is at the top layer.
        */
        int zOrder; //optional, [0, 100] //0 (default): bottom most, 100: top most

    /** Transparency:

    - 0 means the region is transparent:
    - (Default) 1 means the region is opaque.
    */
        double alpha;

        RENDER_MODE_TYPE renderMode;//RENDER_MODE_HIDDEN: Crop, RENDER_MODE_FIT: Zoom to fit
        Region()
            :uid(0)
            , x(0)
            , y(0)
            , width(0)
            , height(0)
            , zOrder(0)
            , alpha(1.0)
            , renderMode(RENDER_MODE_HIDDEN)
        {}

    };
    /** Ignore this parameter. The width of the canvas is set by agora::rtc::IRtcEngine::configPublisher, and not by agora::rtc::VideoCompositingLayout::canvasWidth.
.
    */
    int canvasWidth;
    /** Ignore this parameter. The height of the canvas is set by agora::rtc::IRtcEngine::configPublisher, and not by agora::rtc::VideoCompositingLayout::canvasHeight.
.
    */
    int canvasHeight;
    /** RGB hex color value. @note Do not include a #. For example, C0C0C0.
    */
    const char* backgroundColor;
    /** Region array. Each host in the channel can have a screen display region for the video.
    */
    const Region* regions;
    /** Number of users in the channel.
    */
    int regionCount;
    /** User-defined application data.
    */
    const char* appData;
    /** Length of the user-defined application data.
    */
    int appDataLength;
    VideoCompositingLayout()
        :canvasWidth(0)
        ,canvasHeight(0)
        ,backgroundColor(NULL)
        ,regions(NULL)
        , regionCount(0)
        , appData(NULL)
        , appDataLength(0)
    {}
};

/**
 * Video dimensions.
 */
struct VideoDimensions {
    /**
    The width of the video.
    */
    int width;
      /** The height of the video.
          */
    int height;
    VideoDimensions()
        : width(0), height(0)
    {}
    VideoDimensions(int w, int h)
        : width(w), height(h)
    {}
};

/** The standard bitrate in agora::rtc::IRtcEngine::setVideoEncoderConfiguration.

 (Recommended) In a live broadcast, Agora recommends setting a larger bitrate to improve the video quality. When you choose STANDARD_BITRATE, the bitrate value doubles in a live broadcast mode and remains the same as in #VIDEO_PROFILE_TYPE in a communication mode.

 */
const int STANDARD_BITRATE = 0;

/**
 *  The compatible bitrate in agora::rtc::IRtcEngine::setVideoEncoderConfiguration.

 The bitrate in both the live broadcast and communication modes remain the same as in #VIDEO_PROFILE_TYPE.
 */
const int COMPATIBLE_BITRATE = -1;

struct VideoEncoderConfiguration {
    VideoDimensions dimensions;
    FRAME_RATE frameRate;
    int bitrate;
    ORIENTATION_MODE orientationMode;
    VideoEncoderConfiguration(
        const VideoDimensions& d, FRAME_RATE f,
        int b, ORIENTATION_MODE m)
        : dimensions(d), frameRate(f), bitrate(b), orientationMode(m)
    {}
    VideoEncoderConfiguration(
        int width, int height, FRAME_RATE f,
        int b, ORIENTATION_MODE m)
        : dimensions(width, height), frameRate(f), bitrate(b), orientationMode(m)
    {}
    VideoEncoderConfiguration()
        : frameRate(FRAME_RATE_FPS_30)
        , bitrate(0)
        , orientationMode(ORIENTATION_MODE_ADAPTIVE)
    {}
};

typedef struct Rect {
    int top;
    int left;
    int bottom;
    int right;

    Rect(): top(0), left(0), bottom(0), right(0) {}
    Rect(int t, int l, int b, int r): top(t), left(l), bottom(b), right(r) {}
} Rect;

/** Definition of TranscodingUser.
*/
typedef struct TranscodingUser {
  /** User ID of the user whose video is to be displayed in the CDN live.
  */
    uid_t uid;

/** Horizontal position from the top left corner of the video frame.
*/
    int x;
    /** Vertical position from the top left corner of the video frame.
    */
    int y;
    /** Width of the video frame. The default value is 360.
    */
    int width;
    /** Height of the video frame. The default value is 640.
    */
    int height;

    /** Layer position of the video frame. The value ranges between 1 and 100.

    - 1: (Default) Lowest
    - 100: Highest
    */
    int zOrder;
    /**  Transparency of the video frame. The value ranges between 0 and 1.0:

    - 0: Completely transparent
    - 1.0: (Default) Opaque
    */
    double alpha;
    /** The audio channel of the sound. The default value is 0:

    - 0: (default) Supports dual channels at most, depending on the upstream of the broadcaster.
    - 1: The audio stream of the broadcaster uses the FL audio channel. If the upstream of the broadcaster uses the dual-sound channel, only the left-sound channel will be used for streaming.
    - 2: The audio stream of the broadcaster uses the FC audio channel. If the upstream of the broadcaster uses the dual-sound channel, only the left-sound channel will be used for streaming.
    - 3: The audio stream of the broadcaster uses the FR audio channel. If the upstream of the broadcaster uses the dual-sound channel, only the left-sound channel will be used for streaming.
    - 4: The audio stream of the broadcaster uses the BL audio channel. If the upstream of the broadcaster uses the dual-sound channel, only the left-sound channel will be used for streaming.
    - 5: The audio stream of the broadcaster uses the BR audio channel. If the upstream of the broadcaster uses the dual-sound channel, only the left-sound channel will be used for streaming.

    */
    int audioChannel;
    TranscodingUser()
        : uid(0)
        , x(0)
        , y(0)
        , width(0)
        , height(0)
        , zOrder(0)
        , alpha(1.0)
        , audioChannel(0)
    {}

} TranscodingUser;

/** Watermark image.
*/
typedef struct RtcImage {
    RtcImage() :
       url(NULL),
       x(0),
       y(0),
       width(0),
       height(0)
    {}
      /** URL address of the watermark image on the broadcasting video.
      */
    const char* url;
    /** Horizontal position of the watermark image from the upper left of the live broadcast video.
    */
    int x;
    /** Vertical position of the watermark image from the upper left of the live broadcast video.
    */
    int y;
    /** Width of the watermark image on the broadcasting video.
        */
    int width;
    /** Height of the watermark image on the broadcasting video.
    */
    int height;
} RtcImage;

/**  A class for providing user-specific CDN live audio/video transcoding settings.
*/
typedef struct LiveTranscoding {
  /** Width of the video. The default value is 360.
  */
    int width;
    /** Height of the video. The default value is 640.
    */
    int height;
    /** Bitrate of the output video stream set for the CDN live broadcast. The default value is 400 kbit/s.
    */
    int videoBitrate;
    /** Frame rate of the output video stream set for the CDN live broadcast. The default value is 15 fps.
    */
    int videoFramerate;

    /** Latency mode:

    - true: Low latency with unassured quality.
    - false:（Default）High latency with assured quality.

    */
    bool lowLatency;

    /** Interval between the I frames. The default value is 2 s.
    */
    int videoGop;
    /** Self-defined video codec profiles: #VIDEO_CODEC_PROFILE_TYPE
    */
    VIDEO_CODEC_PROFILE_TYPE videoCodecProfile;
    /** RGB hex value. Value only, do not include a #. For example, C0C0C0.
    */
    unsigned int backgroundColor;   // RGB mode
    /** The number of users in the live broadcast.
    */
    unsigned int userCount;
    /** TranscodingUser
    */
    TranscodingUser *transcodingUsers;
    /** Reserved property. Extra user-defined information to send to the CDN live client. Used to fill in the SEI frames of the H.264/H.265 video stream.
    */
    const char *transcodingExtraInfo;

    /** This metadata to be sent to the CDN live client defined by the RTMP or FLV metadata.
    */
    const char *metadata;
    /** The HTTP/HTTPS URL address of the watermark image added to the CDN live publishing stream.

    The audience of the CDN live publishing stream can see the watermark.
    */
    RtcImage* watermark;
    /** The HTTP/HTTPS URL address of the background image added to the CDN live publishing stream.

    The audience of the CDN live publishing stream can see the background image.
    */
    RtcImage* backgroundImage;
    /** Self-defined audio-sampling rates: #AUDIO_SAMPLE_RATE_TYPE
    */
    AUDIO_SAMPLE_RATE_TYPE audioSampleRate;
    /** Bitrate of the CDN live audio output stream. The highest value is 128.
    */
    int audioBitrate;
    /** Agora's self-defined audio-channel types. Agora recommends choosing 1 or 2:

     - 1: Mono (default)
     - 2: Dual-sound channels
     - 3: Three-sound channels
     - 4: Four-sound channels
     -  5: Five-sound channels
    */
    int audioChannels;

    LiveTranscoding()
        : width(360)
        , height(640)
        , videoBitrate(400)
        , videoFramerate(15)
        , lowLatency(false)
        , backgroundColor(0x000000)
        , videoGop(30)
        , videoCodecProfile(VIDEO_CODEC_PROFILE_HIGH)
        , userCount(0)
        , transcodingUsers(NULL)
        , transcodingExtraInfo(NULL)
        , watermark(NULL)
        , audioSampleRate(AUDIO_SAMPLE_RATE_48000)
        , audioBitrate(48)
        , audioChannels(1)
    {}
} LiveTranscoding;

/** Configuration of the imported live broadcast voice or video stream.
*/
struct InjectStreamConfig {
  /** Width of the stream to be added into the broadcast. The default value is 0 (same width as the original stream).
  */
    int width;
    /** Height of the stream to be added into the broadcast. The default value is 0 (same height as the original stream).
    */
    int height;
    /** Video GOP of the stream to be added into the broadcast. The default value is 30.
    */
    int videoGop;
    /**  Video frame rate of the stream to be added into the broadcast. The default value is 15 fps.
    */
    int videoFramerate;
    /** Video bitrate of the stream to be added into the broadcast. The default value is 400 kbit/s.
    */
    int videoBitrate;
    /** Audio-sampling rate of the stream to be added into the broadcast: #AUDIO_SAMPLE_RATE_TYPE. The default value is 48000.
    */
    AUDIO_SAMPLE_RATE_TYPE audioSampleRate;
    /** Audio bitrate of the stream to be added into the broadcast. The default value is 48.
    */
    int audioBitrate;
    /** Audio channels to be added into the broadcast. The default value is 1.
    */
    int audioChannels;

    // width / height default set to 0 means pull the stream with its original resolution
    InjectStreamConfig()
        : width(0)
        , height(0)
        , videoGop(30)
        , videoFramerate(15)
        , videoBitrate(400)
        , audioSampleRate(AUDIO_SAMPLE_RATE_48000)
        , audioBitrate(48)
        , audioChannels(1)
    {}
};

/** Video stream lifecycle of CDN live.
*/
enum RTMP_STREAM_LIFE_CYCLE_TYPE
{
  /** Bound to the channel lifecycle.
  */
	RTMP_STREAM_LIFE_CYCLE_BIND2CHANNEL = 1,
  /** Bound to the owner identity of the RTMP stream.
  */
	RTMP_STREAM_LIFE_CYCLE_BIND2OWNER = 2,
};

/** Definition of PublisherConfiguration.
*/
struct PublisherConfiguration {
  /** Width of the CDN live output data stream set. The default value is 360.
  */
	int width;
  /** Height of the CDN live output data stream set. The default value is 640.
  */
	int height;
  /** Frame rate of the CDN live output data stream set. The default value is 15 fps.
  */
	int framerate;
  /** Bitrate of the CDN live output data stream set. The default value is 500 kbit/s.
  */
	int bitrate;
  /** Default layout:

 - 0: Tile horizontally
 - 1: Layered windows
 - 2: Tile vertically

  */
	int defaultLayout;
    /** Video stream lifecycle of CDN live: #RTMP_STREAM_LIFE_CYCLE_TYPE
  */
	int lifecycle;
  /** Whether the current user is the owner of the RTMP stream:

- true: Yes (default). The push-stream configuration takes effect.
- false: No. The push-stream configuration will not work.
  */
	bool owner;
  /** Width of the stream to be injected. Set it as 0.
  */
	int injectStreamWidth;
  /** Height of the stream to be injected. Set it as 0.
  */
	int injectStreamHeight;
  /** URL address of the stream to be injected into the channel.
  */
	const char* injectStreamUrl;
  /** Push-stream URL address for the picture-in-picture layouts. The default value is NULL.
  */
	const char* publishUrl;
  /** The push-stream URL address of the original stream not requiring picture-blending. The default value is NULL.
  */
	const char* rawStreamUrl;
  /** Reserved field. The default value is NULL.
  */
	const char* extraInfo;


	PublisherConfiguration()
		: width(640)
		, height(360)
		, framerate(15)
		, bitrate(500)
		, defaultLayout(1)
		, lifecycle(RTMP_STREAM_LIFE_CYCLE_BIND2CHANNEL)
		, owner(true)
		, injectStreamWidth(0)
		, injectStreamHeight(0)
		, injectStreamUrl(NULL)
		, publishUrl(NULL)
		, rawStreamUrl(NULL)
		, extraInfo(NULL)
	{}

};

#if !defined(__ANDROID__)
/** Video display settings of the VideoCanvas class.
*/
struct VideoCanvas
{
  /** Video display window (view).
  */
    view_t view;
    /** Video display mode:  #RENDER_MODE_TYPE.
    */
    int renderMode;
    uid_t uid;
    void *priv; // private data (underlying video engine denotes it)

    VideoCanvas()
        : view(NULL)
        , renderMode(RENDER_MODE_HIDDEN)
        , uid(0)
        , priv(NULL)
    {}
    VideoCanvas(view_t v, int m, uid_t u)
        : view(v)
        , renderMode(m)
        , uid(u)
        , priv(NULL)
    {}
};
#else
struct VideoCanvas;
#endif

    /** Definition of IPacketObserver.
     */
class IPacketObserver
{
public:
/** Definition of Packet.
 */
	struct Packet
	{
		const unsigned char* buffer;
		unsigned int size;
	};
	/** The audio packet is about to be sent to the other users.
	* @param packet  buffer *buffer points the data to be sent and the size of the buffer data to be sent
	* @return

  -true: The packet is sent out.
  - false: The packet is discarded.
	*/
	virtual bool onSendAudioPacket(Packet& packet) = 0;
	/** The video packet is about to be sent to the other users.

	* @param packet buffer *buffer points the data to be sent and the size of the buffer data to be sent.
	* @return

  - true: The packet is sent out.
  - false: The packet is discarded.
	*/
	virtual bool onSendVideoPacket(Packet& packet) = 0;
	/** Called by the SDK when an audio packet is received from other users.

	* @param packet Input/Output parameter. Packet buffer which points to the data and buffer size to be sent.

	* @return

  - true: The packet is processed.
  - false: The packet is discarded.
	*/
	virtual bool onReceiveAudioPacket(Packet& packet) = 0;
	/** Called by the SDK when a video packet is received from other users.

	* @param packet Input/Output parameter. Packet buffer which points to the data and buffer size to be sent.

	* @return

  - true: The packet is processed.
  - false: The packet is discarded.
	*/
	virtual bool onReceiveVideoPacket(Packet& packet) = 0;
};


/** The SDK uses the IRtcEngineEventHandler interface class to send callback event notifications to the application. The application inherits the methods of this interface class to retrieve these event notifications.

All methods in this interface class have default (empty) implementations. Therefore, the application can only inherit some required events. In the callback methods, avoid time-consuming tasks or calling blocking APIs, such as the SendMessage method, otherwise, the SDK may not work properly.

*/
class IRtcEngineEventHandler
{
public:
    virtual ~IRtcEngineEventHandler() {}

    /** The user has successfully joined the specified channel with an assigned channel ID and user ID.

    The channel ID assignment is based on the channel name specified in the IRtcEngine::joinChannel API method. If the user ID is not specified when IRtcEngine::joinChannel is called, the server assigns one automatically.

    @param channel  Channel name.
    @param  uid User ID. If the uid is specified in the IRtcEngine::joinChannel method, it returns the specified ID. If not, it returns an ID that is automatically assigned by the Agora server.
    @param  elapsed Time elapsed (ms) from calling IRtcEngine::joinChannel until this event occurs.
    */
    virtual void onJoinChannelSuccess(const char* channel, uid_t uid, int elapsed) {
        (void)channel;
        (void)uid;
        (void)elapsed;
    }

    /** When the client loses connection with the server because of network problems, the SDK automatically attempts to reconnect and triggers this callback method upon reconnection.

    @param channel Channel name.
    @param uid User ID.
    @param elapsed Time elapsed (ms) from starting to reconnect till this event occurs.
    */
    virtual void onRejoinChannelSuccess(const char* channel, uid_t uid, int elapsed) {
        (void)channel;
        (void)uid;
        (void)elapsed;
    }

    /** A warning occurred during SDK runtime.

    In most cases, the application can ignore the warning reported by the SDK, because the SDK can usually fix the issue and resume running.

    @param warn Warning code.
     @param msg Warning message: #WARN_CODE_TYPE
    */
    virtual void onWarning(int warn, const char* msg) {
        (void)warn;
        (void)msg;
    }

    /** A network or media error occurred during SDK runtime.

    In most cases, reporting an error means that the SDK cannot fix the issue and resume running. The SDK requires the application to take action or in the most basic case, informs the user about the issue.

    For example, the SDK reports an ERR_START_CALL error when failing to initialize a call. The application informs the user that the call initialization failed and invokes the IRtcEngine::leaveChannel method to exit the channel.

    @param err Error code.
     @param msg Error message: #ERROR_CODE_TYPE
    */
    virtual void onError(int err, const char* msg) {
        (void)err;
        (void)msg;
    }

    /** The audio quality of the current call is reported once every two seconds.

    @param uid User ID of the speaker.
     @param quality Quality of the user: #QUALITY_TYPE
    @param delay Time delay (ms).
    @param lost Audio packet loss rate (%).
    */
    virtual void onAudioQuality(uid_t uid, int quality, unsigned short delay, unsigned short lost) {
        (void)uid;
        (void)quality;
        (void)delay;
        (void)lost;
    }

    /** Indicates who is talking and the speaker’s volume.

     By default the indication is disabled. If needed, use the RtcEngineParameters::enableAudioVolumeIndication method to configure it.

    @param speakers The speakers (array). Each speaker: AudioVolumeInfo
    @param speakerNumber Total number of speakers.
    @param totalVolume Total volume after audio mixing between 0 (lowest volume) to 255 (highest volume).
    */
    virtual void onAudioVolumeIndication(const AudioVolumeInfo* speakers, unsigned int speakerNumber, int totalVolume) {
        (void)speakers;
        (void)speakerNumber;
        (void)totalVolume;
    }

    /** When the application calls the IRtcEngine::leaveChannel method, the SDK uses this callback to notify the application that the user has successfully left the channel.

     With this callback function, the application retrieves information, such as the call duration and the statistics data received or transmitted by the SDK.

    @param stats Statistics about the call: RtcStats
    */
    virtual void onLeaveChannel(const RtcStats& stats) {
        (void)stats;
    }

    /** The statistics of the current call session is reported once every two seconds.

    @param stats RTC engine stats: RtcStats
    */
    virtual void onRtcStats(const RtcStats& stats) {
        (void)stats;
    }

    /** The audio device state has changed.

    This callback notifies the application that the system’s audio device state has changed. For example, a headset is unplugged from the device.

    @param deviceId Device ID.
     @param deviceType Device type: #MEDIA_DEVICE_TYPE
     @param deviceState Device state: #MEDIA_DEVICE_STATE_TYPE
    */
    virtual void onAudioDeviceStateChanged(const char* deviceId, int deviceType, int deviceState) {
        (void)deviceId;
        (void)deviceType;
        (void)deviceState;
    }

    /** The audio mixing file playback has finished.

    This callback is triggered when the audio mixing file playback is finished after calling RtcEngineParameters::startAudioMixing.
If you failed to execute the startAudioMixing method, it returns the error code in the IRtcEngineEventHandler::onError callback.

     */
    virtual void onAudioMixingFinished() {
    }

    /**
     * Far-end rhythm begins.
     */
    virtual void onRemoteAudioMixingBegin() {
    }
    /**
     * Far-end rhythm ends.
     */
    virtual void onRemoteAudioMixingEnd() {
    }

    /**
    * The local audio effect playback has finished.

    @param soundId ID of the audio effect. Each audio effect has a unique ID.
    */
    virtual void onAudioEffectFinished(int soundId) {
    }

    /** The video device state has changed.

    This callback notifies the application that the system's video device state has changed.

    @note On a Windows device with an external camera for video capturing, the video disables once the external camera is unplugged.

    @param deviceId Device ID
     @param deviceType Device type: #MEDIA_DEVICE_TYPE
     @param deviceState Device state: #MEDIA_DEVICE_STATE_TYPE
    */
    virtual void onVideoDeviceStateChanged(const char* deviceId, int deviceType, int deviceState) {
        (void)deviceId;
        (void)deviceType;
        (void)deviceState;
    }

    /** The network quality of a specified user in a communication or live broadcast channel is reported once every two seconds.

	@param uid User ID. The network quality of the user with this UID will be reported. If uid is 0, it the local network quality is reported.
     @param txQuality Transmission quality of the user: #QUALITY_TYPE.
     @param rxQuality Receiving quality of the user: #QUALITY_TYPE.
	*/
    virtual void onNetworkQuality(uid_t uid, int txQuality, int rxQuality) {
		(void)uid;
		(void)txQuality;
		(void)rxQuality;
    }

    /** The last mile network quality of the local user.

     This callback is triggered once every two seconds after IRtcEngine::enableLastmileTest is called.

     If the network test is enabled by calling enableLastmileTest and the user is not in a call, this callback function is triggered irregularly to update the application on the network connection quality of the local user.

     @param quality Network quality: #QUALITY_TYPE.
    */
    virtual void onLastmileQuality(int quality) {
        (void)quality;
    }

    /** The first local video frame is displayed on the video window.

    @param width Width (pixels) of the video stream.
    @param height Height (pixels) of the video stream.
    @param elapsed Time elapsed (ms) from calling IRtcEngine::joinChannel until this callback is triggered.
    */
    virtual void onFirstLocalVideoFrame(int width, int height, int elapsed) {
        (void)width;
        (void)height;
        (void)elapsed;
    }

    /** The first remote video frame has been received and decoded.

    This callback is triggered after the first frame of the remote video is received and successfully decoded. The application can configure the user view settings in this callback.

    @param uid User ID of the user sending the video streams.
    @param width Width (pixels) of the video stream.
    @param height Height (pixels) of the video stream.
    @param elapsed Time elapsed (ms) from calling IRtcEngine::joinChannel until this callback is triggered.
    */
    virtual void onFirstRemoteVideoDecoded(uid_t uid, int width, int height, int elapsed) {
        (void)uid;
        (void)width;
        (void)height;
        (void)elapsed;
    }

    /** The video size or rotation has changed.

     @param uid User ID of the remote user or local user (0) whose video size or rotation has changed.
     @param width New width of the video.
     @param height New height of the video.
     @param rotation New rotation of the video [0, 360).
     */
    virtual void onVideoSizeChanged(uid_t uid, int width, int height, int rotation) {
        (void)uid;
        (void)width;
        (void)height;
        (void)rotation;
    }
/** The remote video state has changed.

 @param uid ID of the user whose video state has changed.
 @param state Remote video state: #REMOTE_VIDEO_STATE.
*/
    virtual void onRemoteVideoStateChanged(uid_t uid, REMOTE_VIDEO_STATE state) {
        (void)uid;
        (void)state;
    }

    /** The first remote video frame has been received and displayed.

    This callback is triggered when the first frame of the remote video is displayed in the user’s video window. The application is able to retrieve the time elapsed value from a user joining the channel until the first video frame is displayed.

    @param uid User ID of the remote user sending the video streams.
    @param width Width (pixels) of the video frame.
    @param height Height (pixels) of the video stream.
    @param elapsed Time elapsed (ms) from calling IRtcEngine::joinChannel until this callback is triggered.
    */
    virtual void onFirstRemoteVideoFrame(uid_t uid, int width, int height, int elapsed) {
        (void)uid;
        (void)width;
        (void)height;
        (void)elapsed;
    }

    /** A remote user has joined the channel.

     If there are other users in the channel when that user joins, the SDK also reports to the application on the existing users who are already in the channel.

     @note In a live broadcast, only the hosts will receive this callback, not the audience.

     @param uid Remote user ID.
     @param elapsed Time delay (ms) from calling IRtcEngine::joinChannel until this callback is triggered.
    */
    virtual void onUserJoined(uid_t uid, int elapsed) {
        (void)uid;
        (void)elapsed;
    }

    /** A remote user has left the channel or gone offline.

    The SDK reads the timeout data to determine if a user has left the channel (or has gone offline). If no data package is received from the user within 15 seconds, the SDK assumes the user is offline. A poor network connection may lead to false detections, so use signaling for reliable offline detection.

    @param uid Remote user ID.
     @param reason Reason the remote user has gone offline: #USER_OFFLINE_REASON_TYPE.
    */
    virtual void onUserOffline(uid_t uid, USER_OFFLINE_REASON_TYPE reason) {
        (void)uid;
        (void)reason;
    }

    /** A remote user's audio stream has muted or unmuted.

    @note This callback returns invalid when the number of users in a channel exceeds 20.

    @param uid Remote user ID.
    @param muted

- true: A remote user's audio has muted.
- false: A remote user's audio has unmuted.
*/
    virtual void onUserMuteAudio(uid_t uid, bool muted) {
        (void)uid;
        (void)muted;
    }

    /** A remote user's video stream has paused or resumed.

    @note This callback returns invalid when the number of users in a channel exceeds 20.

    @param uid  Remote user ID.
    @param muted

    - true: A remote user's video has paused.
    - false: A remote user's video has resumed.  */
    virtual void onUserMuteVideo(uid_t uid, bool muted) {
        (void)uid;
        (void)muted;
    }

	/** A remote user has enabled or disabled the video function.

  Once the video function is disabled, the users cannot see any video.

  @note This callback returns invalid when the number of users in a channel exceeds 20.

	@param uid Remote user ID.
	@param enabled

  - true: A remote user has enabled the video function.
  - false: A remote user has disabled the video function.
	*/
	virtual void onUserEnableVideo(uid_t uid, bool enabled) {
		(void)uid;
		(void)enabled;
	}

	/** A remote user has enabled or disabled the local video function.

    @param uid Remote user ID.
    @param enabled

   - true: A remote user has enabled the local video function.
   - false: A remote user has disabled the local video function.
    */
    virtual void onUserEnableLocalVideo(uid_t uid, bool enabled) {
        (void)uid;
        (void)enabled;
    }

    /** The API method executed by the SDK.

    @param api The API that the SDK executes
    @param err The error code that the SDK returns when the method call fails. If the SDK returns 0, then the method has been called successfully.
    @param result The result of calling the API.
    */
    virtual void onApiCallExecuted(int err, const char* api, const char* result) {
        (void)err;
        (void)api;
        (void)result;
    }

    /** The statistics of the uploading local video streams are reported once every two seconds.

	@param stats Statistics of the uploading local video streams: LocalVideoStats.
    */
	virtual void onLocalVideoStats(const LocalVideoStats& stats) {
		(void)stats;
    }

    /** The statistics of the receiving remote video streams are reported once every two seconds.

    @param stats Statistics of the receiving remote video streams: LocalVideoStats.
	*/
	virtual void onRemoteVideoStats(const RemoteVideoStats& stats) {
		(void)stats;
    }

    /** The camera is turned on and ready to capture the video.

    If the camera fails to turn on, fix the error in the IRtcEngineEventHandler::onError method.

    */
    virtual void onCameraReady() {}

/** The camera focus area has changed.

@param x x coordinate of the changed camera focus area.
@param y y coordinate of the changed camera focus area.
@param width Width of the changed camera focus area.
@param height Height of the changed camera focus area.
*/
    virtual void onCameraFocusAreaChanged(int x, int y, int width, int height) {
        (void)x;
        (void)y;
        (void)width;
        (void)height;
    }

    /** The video has stopped.

    The application can use this callback to change the configuration of the view (for example, displaying other pictures in the view) after the video stops.

    */
    virtual void onVideoStopped() {}

    /** The connection between the SDK and the server is lost.

    The onConnectionInterrupted callback is triggered and the SDK attempts to reconnect automatically.
If the reconnection fails within a certain period of time (10 seconds by default),
the onConnectionLost callback is triggered. The SDK attempts to reconnect until the
application calls IRtcEngine::leaveChannel.
    */
    virtual void onConnectionLost() {}

    /** The connection has interrupted between the SDK and the server.

    Once the connection is lost, the SDK attempts to reconnect until the
    application calls IRtcEngine::leaveChannel.
    */
    virtual void onConnectionInterrupted() {}

    /** Your connection has been banned by the Agora Server.
     */
    virtual void onConnectionBanned() {}

//    virtual void onStreamError(int streamId, int code, int parameter, const char* message, size_t length) {}
    /** The local user has received the data stream from the peer user within five seconds.

    @param uid Peer user ID who sends the message.
    @param streamId Stream ID.
    @param data Data received by the recipients.
    @param length Length of the data in bytes.
    */
    virtual void onStreamMessage(uid_t uid, int streamId, const char* data, size_t length) {
        (void)uid;
        (void)streamId;
        (void)data;
        (void)length;
    }

	/** The local user has not received the data stream from the other user within five seconds.

  @param uid Peer user ID who sends the message.
  @param streamId Stream ID.
     @param code Error code: #ERROR_CODE_TYPE.
  @param missed Number of lost messages.
  @param cached Number of incoming cached messages when the data stream was interrupted.
	*
	*/
	virtual void onStreamMessageError(uid_t uid, int streamId, int code, int missed, int cached) {
        (void)uid;
        (void)streamId;
        (void)code;
        (void)missed;
        (void)cached;
    }
    /** The Media Engine has been successfully loaded.
     */
    virtual void onMediaEngineLoadSuccess() {
    }
    /** The Media Engine has been successfully started.
     */
    virtual void onMediaEngineStartCallSuccess() {
    }
    /** The token has expired.

    After a Token is specified by calling IRtcEngine::joinChannel, if the SDK losses connection with the Agora server due to network issues, the Token may expire after a certain period of time and a new Token may be required to reconnect to the server.

This callback notifies the application that a new Token needs to be generated. Call IRtcEngine::renewToken to renew the Token.

In the previous SDKs, this function was provided in onError: ERR_TOKEN_EXPIRED(-109)、ERR_INVALID_TOKEN(-110). Starting from the Agora SDK v1.7.3, the old method is still valid, but it is recommended to use the onRequestToken callback.
    */
    virtual void onRequestToken() {
    }
/** The Token will expire within 30 seconds.

If the Token specified when calling IRtcEngine::joinChannel expires, the user will become offline. This callback is triggered 30 seconds before the Token expires to remind the app to renew the Token. Upon receiving this callback, generate a new Token on the server and call IRtcEngine::renewToken to pass the new Token on to the SDK.

@param token The Token that will expire in 30 seconds.

*/
    virtual void onTokenPrivilegeWillExpire(const char* token) {
        (void)token;
    }

    /** The first local audio frame has been generated.

    @param elapsed Time elapsed (ms) from the remote user calling IRtcEngine::joinChannel.
    */
    virtual void onFirstLocalAudioFrame(int elapsed) {
        (void)elapsed;
    }

    /** The first remote audio frame has been received.

    @param uid Remote user ID.
    @param elapsed Time elapsed (ms) from the remote user calling IRtcEngine::joinChannel.
    */
    virtual void onFirstRemoteAudioFrame(uid_t uid, int elapsed) {
        (void)uid;
        (void)elapsed;
    }
    /* An active speaker has been detected.

    If the RtcEngineParameters::enableAudioVolumeIndication API is used, this callback method is triggered when an active speaker is detected in the channel and returns the uid of the active speaker.

    @note

    - You need to call enableAudioVolumeIndication to receive this callback.
    - The active speaker means the uid of the speaker who speaks at the highest volume during a certain period of time.

    @param uid User ID of the active speaker. By default, 0 means the local user. If needed, you can also add relative functions in your application, for example, the active speaker, once detected, will have his/her head portrait zoomed in.
    */
    virtual void onActiveSpeaker(uid_t uid) {
        (void)uid;
    }

    /** The user role in a live broadcast has switched. For example, from a host to an audience or vice versa.

     @param oldRole Role that you switched from: #CLIENT_ROLE_TYPE
     @param newRole Role that you switched to: #CLIENT_ROLE_TYPE
    */
    virtual void onClientRoleChanged(CLIENT_ROLE_TYPE oldRole, CLIENT_ROLE_TYPE newRole) {
    }
/** The volume of the playback, microphone, or application has changed.

 @param deviceType #MEDIA_DEVICE_TYPE
 @param volume Volume. The value ranges from 0 to 255.
 @param muted
 
 - true: The audio device is muted
 - false: The audio device is not muted.
 */
    virtual void onAudioDeviceVolumeChanged(MEDIA_DEVICE_TYPE deviceType, int volume, bool muted) {
        (void)deviceType;
        (void)volume;
        (void)muted;
    }
/** A stream was published.
*/
    virtual void onStreamPublished(const char *url, int error) {
        (void)url;
        (void)error;
    }
/** A stream was unpublished.
*/
    virtual void onStreamUnpublished(const char *url) {
        (void)url;
    }
/** The publisher's transcoding was updated.
*/
    virtual void onTranscodingUpdated() {
    }
/** The status of the externally injected video stream.

 @param URL URL address of the externally injected video stream.
 @param uid User ID.
 @param status Status of the added stream: #INJECT_STREAM_STATUS.
*/
    virtual void onStreamInjectedStatus(const char* url, uid_t uid, int status) {
        (void)url;
        (void)uid;
        (void)status;
    }

/** The local published media stream fell back to an audio-only stream in poor network conditions or switched back to the video when the network conditions improved.

 If you call RtcEngineParameters::setLocalPublishFallbackOption and set option as #STREAM_FALLBACK_OPTION_AUDIO_ONLY, this callback will be triggered when the local publish stream falls back to audio-only mode due to poor uplink conditions, or when the audio stream switches back to the video when the uplink network condition improves.

 @param isFallbackOrRecover

 - true: The local publish stream fell back to audio-only, due to poor network conditions.
 - false: The local publish stream switched back to the video, after the network conditions improved.

 */
    virtual void onLocalPublishFallbackToAudioOnly(bool isFallbackOrRecover) {
        (void)isFallbackOrRecover;
    }

    /** The remote published media stream fell back to audio-only due to poor network conditions or switched back to the video when the network conditions improved.

     Once you enabled RtcEngineParameters::setRemoteSubscribeFallbackOption,
     this event will be triggered to receive audio only due to poor network conditions or switches back to the video when the network condition improves.

      @note Once the remote subscribe stream is switched to the low stream due to poor network conditions, you can monitor the stream switch between a high and low stream in the agora::rtc::RemoteVideoStats callback.

       @param uid    Remote user ID
     @param  isFallbackOrRecover

     - true: The remote subscribe stream fell back to audio-only due to poor network conditions.
     - false: The remote subscribe stream switched back to the video stream when the network conditions improved.
     */
    virtual void onRemoteSubscribeFallbackToAudioOnly(uid_t uid, bool isFallbackOrRecover) {
        (void)uid;
        (void)isFallbackOrRecover;
    }

    /** Statistics of the remote audio transport.

     This callback is triggered once every two seconds after a user has received the audio data packet sent from a remote user.

     @param uid  User ID of the remote user sending the audio data packet.
     @param delay Time delay (ms) from the remote user to the local client.
     @param lost Packet loss rate (%).
     @param rxKBitRate  Received audio bitrate (kbit/s) of the packet from the remote user.
     */
    virtual void onRemoteAudioTransportStats(
        uid_t uid, unsigned short delay, unsigned short lost,
        unsigned short rxKBitRate) {
        (void)uid;
        (void)delay;
        (void)lost;
        (void)rxKBitRate;
    }

    /** Statistics of the remote video transport.

    This callback is triggered once every two seconds once the user has received the video data packet sent from a remote user.

     *
     @param uid User ID of the remote user whose video packet has been received.
     @param delay Time delay (ms) from the remote user to the local client.
     @param lost Packet loss rate (%).
     @param rxKBitRate Received video bitrate (kbit/s) of the packet from the remote user.
     */
    virtual void onRemoteVideoTransportStats(
        uid_t uid, unsigned short delay, unsigned short lost,
        unsigned short rxKBitRate) {
        (void)uid;
        (void)delay;
        (void)lost;
        (void)rxKBitRate;
    }
};

/**
* Video device collection methods.

The IVideoDeviceCollection interface class retrieves the video device related numbers or data.

*/
class IVideoDeviceCollection
{
public:
    /** Retrieves the total number of the indexed video-capture devices in the system.

    @return Total number of the indexed video-capture devices.
    */
    virtual int getCount() = 0;

    /** Retrieves a specified piece of information about an indexed video-capture device.

    @param index An input parameter with a specified index. Must be smaller than the return value of agora::rtc::IVideoDeviceCollection::getCount.
    @param deviceName An output parameter indicating the device name.
   @param  deviceId An output parameter indicating the device ID.
    @return

    - 0: Success.
    - < 0: Failure.
    */
    virtual int getDevice(int index, char deviceName[MAX_DEVICE_ID_LENGTH], char deviceId[MAX_DEVICE_ID_LENGTH]) = 0;

    /** Sets the device with the device ID.

    @param deviceId Device ID.
    @return

    - 0: Success.
    - < 0: Failure.
    */
    virtual int setDevice(const char deviceId[MAX_DEVICE_ID_LENGTH]) = 0;

    /** Releases the resource.
    */
    virtual void release() = 0;
};

/** Video device management methods.

The IVideoDeviceManager interface class tests the video device interfaces. Instantiate an AVideoDeviceManager class to retrieve an IVideoDeviceManager interface.

*/
class IVideoDeviceManager
{
public:

    /** Enumerates the video capture devices.

    This method returns an IVideoDeviceCollection object that includes all video-capture devices in the system. With the IVideoDeviceCollection object, the application can enumerate the video-capture devices. The application must call the IVideoDeviceCollection::release method to release the returned object after using it.

    * @return

    - Returns an IVideoDeviceCollection object that includes all video-capture devices in the system: Success.
    - Returns NULL: Failure.
    */
    virtual IVideoDeviceCollection* enumerateVideoDevices() = 0;

    /** Sets a device with the device ID.

    @param deviceId Device ID of the video-capture device. Call enumerateVideoDevices to retrieve it.

    @note Plugging or unplugging the device does not change the device ID.

    @return

- 0: Success.
- < 0: Failure.
    */
    virtual int setDevice(const char deviceId[MAX_DEVICE_ID_LENGTH]) = 0;

    /** Retrieves the video-capture device that is in use.

    @param deviceId Output parameter. Device ID of the video-capture device.
    @return

    - 0: Success.
    - < 0: Failure.
    */
    virtual int getDevice(char deviceId[MAX_DEVICE_ID_LENGTH]) = 0;

    /** Starts the video capture device test.

    This method tests whether the video-capture device works properly.
Before calling this method, ensure that you have already called agora::rtc::IRtcEngine::enableVideo, and the HWND window handle of the incoming parameter is valid.

    @param hwnd Output parameter. The window handle used to display the screen.
    @return

    - 0: Success.
    - < 0: Failure.
    */
    virtual int startDeviceTest(view_t hwnd) = 0;

    /** Stops the video-capture device test.
    @return

- 0: Success.
- < 0: Failure.
    */
    virtual int stopDeviceTest() = 0;

    /** Releases the resource.
    */
    virtual void release() = 0;
};

/** Audio device collection methods.

The IAudioDeviceCollection interface class retrieves device-related information.
*/
class IAudioDeviceCollection
{
public:
    /** Retrieves the total number of audio playback or audio recording devices.

    @note You must first call agora::rtc::IAudioDeviceManager::enumeratePlaybackDevices or agora::rtc::IAudioDeviceManager::enumerateRecordingDevices before calling this method, to return the number of the audio playback or audio recording devices.

@return Number of the audio playback or audio recording devices.
    */
    virtual int getCount() = 0;

    /** Retrieves a specified piece of information about the audio device.

    @param index An input parameter that specifies the device information to be enquired.
    @param deviceName An output parameter for the device name.
    @param deviceId An output parameter for the device ID.
    @return

- 0: Success.
- < 0: Failure.
    */
    virtual int getDevice(int index, char deviceName[MAX_DEVICE_ID_LENGTH], char deviceId[MAX_DEVICE_ID_LENGTH]) = 0;

    /** Specifies a device with the device ID.
    @param deviceId Device ID.
    @return

- 0: Success.
- < 0: Failure.
    */
    virtual int setDevice(const char deviceId[MAX_DEVICE_ID_LENGTH]) = 0;
/** Sets the volume of the application.

@param volume Application volume. The value ranges from 0 (lowest volume) to 255 (highest volume).

@return

- 0: Success.
- < 0: Failure.
*/
    virtual int setApplicationVolume(int volume) = 0;
    /** Retrieves the volume of the application.

@param volume An input parameter specifying the application volume. The value ranges from 0 (lowest volume) to 255 (highest volume).

@return

- 0: Success.
- < 0: Failure.
    */
    virtual int getApplicationVolume(int& volume) = 0;
    /** Mutes the application.

@param mute Boolean value indicating if the application should be muted.

- true: Mutes the application.
- false: Unmutes the application.

@return

- 0: Success.
- < 0: Failure.
    */
    virtual int setApplicationMute(bool mute) = 0;
    /** Retrieves the mute state of the application.

@param mute Boolean value indicating if the application is muted.

- true: The application is muted.
- false: The application is not muted.

@return

- 0: Success.
- < 0: Failure.
    */
    virtual int isApplicationMute(bool& mute) = 0;
    /**
    * Releases the resource.
    */
    virtual void release() = 0;
};
/** Audio device management methods.

 The IAudioDeviceManager interface class allows for audio device interface testing. Instantiate an AAudioDeviceManager class to retrieve the IAudioDeviceManager interface.

*/
class IAudioDeviceManager
{
public:
    /** Enumerates audio playback devices.

    This method returns an IAudioDeviceCollection object that includes all audio playback devices in the system. With the IAudioDeviceCollection object, the application can enumerate the audio playback devices.

    @note The application must call the IAudioDeviceCollection::release method to release the returned object after using it.

    * @return

    - Returns an IAudioDeviceCollection object that includes all audio playback devices in the system: Success.
    - Returns NULL: Failure.
    */
    virtual IAudioDeviceCollection* enumeratePlaybackDevices() = 0;

    /** Enumerates the audio recording devices.

    This method returns an IAudioDeviceCollection object that includes all audio recording devices in the system. With the IAudioDeviceCollection object, the application can enumerate the audio recording devices.

    @note The application needs to call the IAudioDeviceCollection::release method to release the returned object after using it.

    @return

    - Returns an IAudioDeviceCollection object that includes all audio recording devices in the system: Success.
    - Returns NULL: Failure.
    */
    virtual IAudioDeviceCollection* enumerateRecordingDevices() = 0;

    /** Specifies an audio playback device associated with the device ID.
     
     @note Plugging or unplugging the audio device does not change the device ID.
     
     @param deviceId Device ID of the audio playback device that may be retrieved by using the IRtcEngine::enumeratePlaybackDevices method.
     
     @return

- 0: Success.
- < 0: Failure.
    */
    virtual int setPlaybackDevice(const char deviceId[MAX_DEVICE_ID_LENGTH]) = 0;

    /** Retrieves the audio playback device associated with the device ID.

    @param deviceId Device ID of the audio playback device.
    @return

- 0: Success.
- < 0: Failure.
    */
    virtual int getPlaybackDevice(char deviceId[MAX_DEVICE_ID_LENGTH]) = 0;

    /** Retrieves the audio playback device information associated with the device ID and name.

        @param deviceId Device ID of the audio playback device.
       @param deviceName Device name of the audio playback device.
       @return

   - 0: Success.
   - < 0: Failure.
         */
    virtual int getPlaybackDeviceInfo(char deviceId[MAX_DEVICE_ID_LENGTH], char deviceName[MAX_DEVICE_ID_LENGTH]) = 0;

    /** Sets the volume of the audio playback device.
    @param volume Volume of the audio playback device. The value ranges from 0 (lowest volume) to 255 (highest volume).
    @return

- 0: Success.
- < 0: Failure.
    */
    virtual int setPlaybackDeviceVolume(int volume) = 0;

    /** Retrieves the volume of the audio playback device.
    @param volume Volume of the audio playback device. The value ranges from 0 (lowest volume) to 255 (highest volume).
    @return

- 0: Success.
- < 0: Failure.
    */
    virtual int getPlaybackDeviceVolume(int *volume) = 0;

    /** Sets the audio recording device using the device ID.

@param deviceId Device ID of the audio recording device that may be retrieved by using the enumerateRecordingDevices method.

@note Plugging or unplugging the audio device does not change the device ID.

@return

- 0: Success.
- < 0: Failure.
    */
    virtual int setRecordingDevice(const char deviceId[MAX_DEVICE_ID_LENGTH]) = 0;

    /** Retrieves the audio recording device associated with the device ID.
    @param deviceId Device ID of the audio recording device.
    @return

- 0: Success.
- < 0: Failure.
    */
    virtual int getRecordingDevice(char deviceId[MAX_DEVICE_ID_LENGTH]) = 0;

    /** Retrieves the audio recording device information associated with the device ID and name.

@param deviceId Device ID of the recording audio device.
@param deviceName Device name of the recording audio device.
@return

- 0: Success.
- < 0: Failure.
         */
   virtual int getRecordingDeviceInfo(char deviceId[MAX_DEVICE_ID_LENGTH], char deviceName[MAX_DEVICE_ID_LENGTH]) = 0;


    /** Sets the volume of the microphone.

    @param volume Volume of the microphone. The value ranges from 0 (lowest volume) to 255 (highest volume).
    @return

- 0: Success.
- < 0: Failure.
    */
    virtual int setRecordingDeviceVolume(int volume) = 0;

    /** Retrieves the volume of the microphone.
    @param volume Volume of the microphone. The value ranges from 0 (lowest volume) to 255 (highest volume).
    @return

- 0: Success.
- < 0: Failure.
    */
    virtual int getRecordingDeviceVolume(int *volume) = 0;

/** Mutes the audio playback device.

@param mute

- true: Mutes the device.
- false: Unmutes the device.

@return

- 0: Success.
- < 0: Failure.
*/
    virtual int setPlaybackDeviceMute(bool mute) = 0;
    /** Retrieves the mute state of the playback device.

    @param mute Boolean value indicating if the device is muted.

    - true: The playback device is muted.
    - false: The playback device is unmuted.

    @return

    - 0: Success.
    - < 0: Failure.
    */
    virtual int getPlaybackDeviceMute(bool *mute) = 0;
    /** Mutes/Unmutes the microphone.

    @param mute Boolean value indicating whether to mute/unmute the microphone.

  - true: Mutes the microphone.
  - false: Unmutes the microphone.

    @return

    - 0: Success.
    - < 0: Failure.
    */
    virtual int setRecordingDeviceMute(bool mute) = 0;
    /** Retrieves the microphone’s mute status.

    @param mute Boolean value indicating if the microphone is muted.

    - true: The microphone is muted.
    - false: The microphone is unmuted.

    @return

    - 0: Success.
    - < 0: Failure.
    */
    virtual int getRecordingDeviceMute(bool *mute) = 0;

    /** Starts the audio playback device test.

This method tests if the playback device works properly. In the test, the SDK plays an audio file specified by the user. If the user can hear the audio, the playback device works properly.

@param testAudioFilePath File path of the audio file for the test, which is in the UTF-8 absolute path:

- Supported file format: wav, mp3, m4a, and aac
- Supported file sampling rate: 8000, 16000, 32000, 44100, and 48000

@return

- 0: Success and you can hear the sound of the file you set.
- < 0: Failure.
    */
    virtual int startPlaybackDeviceTest(const char* testAudioFilePath) = 0;

    /** Stops the audio playback device test.

@return

- 0: Success.
- < 0: Failure.
    */
    virtual int stopPlaybackDeviceTest() = 0;

    /** Starts the microphone test.

    his method tests whether the microphone works properly. Once the test starts, the SDK uses the IRtcEngineEventHandler::onAudioVolumeIndication callback to notify the application with the volume information.

    @param indicationInterval Interval period (ms) of the agora::rtc::IRtcEngineEventHandler::onAudioVolumeIndication callback cycle

    @return

- 0: Success.
- < 0: Failure.
    */
    virtual int startRecordingDeviceTest(int indicationInterval) = 0;

    /** Stops the microphone test.

    This method stops the microphone test. To stop the test, call this method after calling the startRecordingDeviceTest method.

    @return

- 0: Success.
- < 0: Failure.
    */
    virtual int stopRecordingDeviceTest() = 0;

    /** Releases the resource.
    */
    virtual void release() = 0;
};

/** Definition of RTCEngineContext.
*/
struct RtcEngineContext
{
    IRtcEngineEventHandler* eventHandler;
    /** App ID issued to the developers by Agora. Apply for a new App ID from Agora if it is missing from your kit.
    */
    const char* appId;
    RtcEngineContext()
    :eventHandler(NULL)
    ,appId(NULL)
    {}
};

/** The RtcEngine class provides the main Agora SDK methods that can be invoked by your application.

IRtcEngine is the base interface class of the Agora Native SDK. Enable the use of Agora’s Native SDK communication functionality through the creation of an IRtcEngine object, then call the methods of this object.

@note This class was previously named IAgoraAudio, and has been renamed to IRtcEngine since v1.0 of the Agora SDK.
*/
class IRtcEngine
{
public:
    /** Releases the IRtcEngine object.
    @param sync

    - true: Synchronous call. This result returns after the IRtcEngine object resources are released. The application should not call this interface in the SDK generated callback, otherwise, the SDK must wait for the callback to return in order to recover the associated object resources which will result in a deadlock. The SDK automatically detects the deadlock and converts it into an asynchronous call, causing the test to take additional time.
    - false: Asynchronous call. The result returns immediately, even when the IRtcEngine object resources have not been released. The SDK will release all resources. @note Do not immediately uninstall the SDK's dynamic library after the call, or it may cause a crash due to the SDK clean-up thread not quitting.

    @return

    - 0: Success.
    - < 0: Failure.
  */
    virtual void release(bool sync=false) = 0;

	/** Initializes the Agora SDK service.

  Enter the issued Agora App ID to begin the initialization. After creating an IRtcEngine object, call this method to initialize the service prior to using any other methods. After initialization, the service is set to voice mode by default.

    @param An input parameter for the context for the RTC engine, agora::rtc::RtcEngineContext.
    @return

- 0: Success.
- < 0: Failure.
    */
    virtual int initialize(const RtcEngineContext& context) = 0;

    /** Retrieves the pointer of the device manager object.
    @param iid Interface ID of the interface
    @param inter Pointer for the DeviceManager object.
    @return

- 0: Success.
- < 0: Failure.
    */
    virtual int queryInterface(INTERFACE_ID_TYPE iid, void** inter) = 0;

    /** Retrieves the SDK version number.

    @param build An input/output parameter specifying the build number.
    @return SDK version number in char format.
    */
    virtual const char* getVersion(int* build) = 0;

    /** Retrieves the warning or error description.

    @param code Warning or error code returned in IRtcEngineEventHandler::onWarning or IRtcEngineEventHandler::onError.
     @return Returns #WARN_CODE_TYPE or #ERROR_CODE_TYPE.
    */
    virtual const char* getErrorDescription(int code) = 0;

    /** Allows a user to join a channel.

Users in the same channel can talk to each other, and; multiple users in the same channel can start a group chat. Users with different App IDs cannot call each other.

Once in a call, the user must call the IRtcEngine::leaveChannel method to exit the current call, prior to entering another channel.

@note A channel does not accept duplicate uids, such as two users with the same uid. If you set uid as 0, the system will automatically assign a uid.

@param token A Token generated by the application. This parameter is optional if the user uses a static key or App ID. In this case, pass NULL as the parameter value. If the user uses a Channel Key, Agora issues an additional App Certificate to the application developer. Developers can then generate a user key with the algorithm and App Certificate provided by Agora for user authentication on the server. In most circumstances, a static App ID will suffice. For added security, use the Channel Key.
@param channelId A string acting as the unique channel name for the AgoraRTC session. The string length must be less than 64 bytes. Supported character scopes are: a-z, A-Z, 0-9, space, ! #$%&, ()+, -, :;<=., >? @[], ^_, {|}, ~”
@param info (Optional) Additional information about the channel that developers may need to add. This parameter can be set to NULL or contain channel related information. Other users in the channel will not receive this message.
@param uid (Optional) User ID. A 32-bit unsigned integer with a value that ranges from 1 to (2^32-1). The uid must be unique. If a uid is not assigned (or set to 0), the SDK assigns one and returns it in the IRtcEngineEventHandler::onJoinChannelSuccess callback. Your app must record and maintain the returned value since the SDK does not maintain its value.

@return

- 0: Success.
- < 0: Failure.
    */
    virtual int joinChannel(const char* token, const char* channelId, const char* info, uid_t uid) = 0;

    /** Allows a user to leave a channel, such as hanging up or exiting a call.

After joining a channel, the user must call the leaveChannel method to end the call before joining another one.

This method returns 0 if the user has successfully left the channel and releases all resources related to the call.

This method is called asynchronously, and the user has not actually left the channel when the call returns. Once the user leaves the channel, the SDK triggers the IRtcEngineEventHandler::onLeaveChannel callback.

@note If you call release immediately after leaveChannel, the leaveChannel process will be interrupted, and the SDK will not trigger the onLeaveChannel callback.
    @return

- 0: Success.
- < 0: Failure.
    */
    virtual int leaveChannel() = 0;

    /** Renews the Token.

     The Token expires after a certain period of time once the Token schema is enabled when:

     - The IRtcEngineEventHandler::onTokenPrivilegeWillExpire is triggered, or
     - The IRtcEngineEventHandler::onError callback reports the ERR_TOKEN_EXPIRED(-109) error, or
     - The IRtcEngineEventHandler::onRequestToken callback reports the ERR_TOKEN_EXPIRED(-109) error.

     The application should retrieve a new key and call this method to renew the token. Failure to do so will result in the SDK disconnecting from the server.

    @param token The Token to renew.
    @return

- 0: Success.
- < 0: Failure.
    */
    virtual int renewToken(const char* token) = 0;
/** Sets the channel profile.

 The Agora RtcEngine needs to know the application scenario in order to apply different optimization methods.

 @note
 - Only one profile can be used at the same time within the same channel. If you want to switch to another profile, use *release* to destroy the current Engine and create a new one using *create* and *initialize* before calling this method to set the new channel profile.
 - Make sure that different App IDs are used for different channel profiles.
 - This method must be called and configured prior to joining a user to a channel because the channel profile cannot be configured when the channel is in use.
 - While in the communication mode, the Agora SDK supports encoding only in raw data, not in texture.

 @param profile Channel profile: #CHANNEL_PROFILE_TYPE
 @return
 - 0: Success.
 - < 0: Failure.
*/
    virtual int setChannelProfile(CHANNEL_PROFILE_TYPE profile) = 0;
    /** Sets the role of the user, such as a host or an audience (default), before joining a channel.

     This method can be used to switch the user role after the user joins a channel.

     @param role Role of the client: #CLIENT_ROLE_TYPE
     @return

- 0: Success.
- < 0: Failure.
*/
    virtual int setClientRole(CLIENT_ROLE_TYPE role) = 0;

    /** Starts an audio call test.

    This method launches an audio call test to determine whether the audio devices (for example, headset and speaker) and the network connection are working properly.

To conduct the test:
- The user speaks, and the recording is played back within 10 seconds.
- If the user can hear the recording within 10 seconds, the audio devices and network connection are working properly.

@note After calling the startEchoTest method, always call IRtcEngine::stopEchoTest to end the test. Otherwise, the application cannot run the next echo test.

@return

- 0: Success.
- < 0: Failure.
    */
    virtual int startEchoTest() = 0;

    /** Stops the audio call test.
    @return

- 0: Success.
- < 0: Failure.
    */
    virtual int stopEchoTest() = 0;

    /**  Enables the network test.

    This method tests the quality of the user’s network connection and is disabled by default.

    Before users join a channel, call this method to check the network quality. This method consumes additional network traffic, which may affect the communication quality.

    Call disableLastmileTest to disable the test immediately after receiving the IRtcEngineEventHandler::onLastmileQuality callback, and before the user joins the channel.

    @return

- 0: Success.
- < 0: Failure.
    */
    virtual int enableLastmileTest() = 0;

    /** Disables the quality test for the network connection.

    @return

- 0: Success.
- < 0: Failure.
    */
    virtual int disableLastmileTest() = 0;

    /** Enables video mode.

     The application can call this method either before entering a channel or during a call. If the method is called before entering a channel, the service starts in the video mode. If the method is called during a call, the application switches from the audio to video mode.

     To disable the video mode, call the IRtcEngine::disableVideo method.

     @note This method controls the underlying states of the Agora Engine, and is still valid after the user leaves the channel.

     @return

- 0: Success.
- < 0: Failure.
    */
    virtual int enableVideo() = 0;

    /** Disables the video mode.

    The application may call this method before entering a channel or during a call. If the method is called before entering a channel, the service starts in audio mode. If the method is called during a call, it switches from the video to audio mode. To enable the video mode, call the IRtcEngine::enableVideo method.

@note This method controls the underlying states of the Agora Engine, and is still valid after the user leaves the channel.

@return

- 0: Success.
- < 0: Failure.
    */
    virtual int disableVideo() = 0;

    /** Starts the local video preview before joining the channel.

Before using this method, you must:

- Call IRtcEngine::setupLocalVideo to set up the local preview window and configure the attributes.
- Call IRtcEngine::enableVideo to enable video.

Once startPreview is called to start the local video preview, if you leave the channel by calling IRtcEngine::leaveChannel, the local video preview remains until you call IRtcEngine::stopPreview to disable it.

@return

- 0: Success.
- < 0: Failure.
    */
    virtual int startPreview() = 0;

    /** Stops the local video preview and the video.
    @return

- 0: Success.
- < 0: Failure.
    */
    virtual int stopPreview() = 0;
/** Sets the video encoding profile.

Each video encoding profile includes a set of parameters, such as the resolution, frame rate, and bitrate. If the camera device does not support the specified resolution, the SDK will automatically choose a suitable camera resolution, keeping the encoder resolution specified by setVideoProfile.

@note
- Always set the video profile after calling the IRtcEngine::enableVideo method.
- Always set the video profile before calling the IRtcEngine::joinChannel or IRtcEngine::startPreview method.

@param profile Video profile: #VIDEO_PROFILE_TYPE.
@param swapWidthAndHeight Width and height of the output video, consistent with the set video profile. This parameter sets whether to swap the width and height of the stream:

- true: Swap the width and height.
- false: (Default) Do not swap the width and height.

Since the landscape or portrait mode of the output video can be decided directly by the video profile, Agora recommends setting this parameter as default.
@return

- 0: Success.
- < 0: Failure.
*/
    virtual int setVideoProfile(VIDEO_PROFILE_TYPE profile, bool swapWidthAndHeight) = 0;

    /** Sets the video encoder configuration.

     Each configuration profile corresponds to a set of video parameters, including the resolution, frame rate, bitrate, and video orientation.

     The parameters specified in this method are the maximum values under ideal network conditions. If the video engine cannot render the video using the specified parameters due to poor network conditions, the parameters further down the list are considered until a successful configuration is found.

     @param config Video encoder configuration: #VideoEncoderConfiguration.
     @return

- 0: Success.
- < 0: Failure.
     */
    virtual int setVideoEncoderConfiguration(
        const VideoEncoderConfiguration& config) = 0;

    /** Sets the remote video view.

     This method binds the remote user to the video display window (sets the view for the user of the specified uid).

     Typically the application specifies the uid of the remote video in the method call before the user enters a channel.

     If the remote uid is unknown to the application, set it later when the application receives the IRtcEngineEventHandler::onUserJoined event.

     If the Video Recording function is enabled, the Video Recording Service joins the channel as a dummy client, causing other clients to also receive the onUserJoined event. Do not bind the dumb client to the application view because it does not send any video streams. If your application cannot recognize the dumb client, bind it with the view when the IRtcEngineEventHandler::onFirstRemoteVideoDecoded event is triggered.

     To unbind the user from the view, set the view to null. Once the user has left the channel, the SDK unbinds the remote user.

     @param canvas Video canvas information: VideoCanvas
     @return

      - 0: Success.
      - < 0: Failure.
    */
    virtual int setupRemoteVideo(const VideoCanvas& canvas) = 0;

    /** Sets the local video view.

    This method configures the video display settings on the local machine.

    The application calls this method to bind each video window (view) of the local video streams and configures the video display settings. Call this method after initialization to configure the local video display settings before entering a channel. After leaving the channel, the binding is still valid, which means that the window will still display. To unbind the view, set the view value to NULL when calling setupLocalVideo.

     @param canvas Video canvas information: VideoCanvas.
     @return

- 0: Success.
- < 0: Failure.
    */
    virtual int setupLocalVideo(const VideoCanvas& canvas) = 0;

    /** Enables the audio mode, which is enabled by default.

    @note This method enables the internal engine and works either within or outside the channel. That said, enableAudio is still valid after a user leaves the channel.


    @return

    - 0: Success.
    - < 0: Failure.
    */
    virtual int enableAudio() = 0;

    /** Disables the audio mode.

    @note This method controls the underlying states of the Agora Engine and can be called either within or outside the channel. disableAudio is still valid after a user leaves the channel.


    @return

    - 0: Success.
    - < 0: Failure.
    */
    virtual int disableAudio() = 0;

	/** Sets the audio parameters and application scenarios.

     @param  profile Sets the sampling rate, bitrate, encode mode, and the number of channels: #AUDIO_PROFILE_TYPE
     @param  scenario Sets the audio application scenarios: #AUDIO_SCENARIO_TYPE

 @return

- 0: Success.
- < 0: Failure.
     */
    virtual int setAudioProfile(AUDIO_PROFILE_TYPE profile, AUDIO_SCENARIO_TYPE scenario) = 0;

#if defined(__APPLE__) || defined(_WIN32)

#if defined(__APPLE__)
  typedef unsigned int WindowIDType;
#elif defined(_WIN32)
  typedef HWND WindowIDType;
#endif
  /** Starts screen sharing.

  This method shares the whole screen, the specified window, or the specified region:

  - Share the whole screen: Set windowId as 0 and set rect as null.
  - Share the specified window: Set windowId not as 0, each window has a windowId which is not 0.
  - Share the specified region: Set windowId as 0 and set rect not as null. In this case, you can share the specified region, for example by dragging the mouse, the logic of which is implemented by yourselves.

@note The specified region means a region on the whole screen. Currently, it does not support sharing a specified region in a specific window.
         captureFreq is the captured frame rate once the screen-sharing function is enabled, and the value (mandatory) ranges from 1 fps to 15 fps. No matter which function you have enabled, it returns 0 after successful execution, otherwise, it returns an error code.

    @param windowId Screen sharing area: WindowIDType
   @param rect     This parameter is valid when windowsId is set as 0, and when you set rect as null, then the whole screen is shared

   @return

- 0: Success.
- < 0: Failure.
   */
  virtual int startScreenCapture(WindowIDType windowId, int captureFreq, const Rect *rect, int bitrate) = 0;

  /** Stop screen sharing.

   @return

- 0: Success.
- < 0: Failure.
   */
  virtual int stopScreenCapture() = 0;

  /** Updates the screen capture region.

@param rect  This parameter is valid when windowsId is set as 0, and when you set rect as null, then the whole screen is shared

   @return

   - 0: Success.
   - < 0: Failure.
   */
  virtual int updateScreenCaptureRegion(const Rect *rect) = 0;
#endif

    /** Retrieves the current call ID.

     When a user joins a channel on a client, a callId is generated to identify the call from the client. Feedback methods, such as the rate and complain methods, must be called after the call ends, so feedback can be submitted to the SDK. These methods require callId parameters.

     To use these feedback methods, use the getCallId method to retrieve the callId during the call, then pass the resulting value as an argument into the feedback methods after the call ends.

    @param callId The current call ID.
    @return

- 0: Success.
- < 0: Failure.
    */
    virtual int getCallId(agora::util::AString& callId) = 0;

/** Allows the user to rate the call and is usually called after the call ends.

@param callId Call ID retrieved from the IRtcEngine::getCallId method.
@param rating  The rating of the call between 1 (lowest score) to 10 (highest score).
@param description (Optional) Description for the call. The string length must be less than 800 bytes.

@return

- 0: Success.
- < 0: Failure.
*/
    virtual int rate(const char* callId, int rating, const char* description) = 0;

    /** Allows a user to complain about the call quality.

    The complaint is usually called after the call ends.

    @param callId Call ID retrieved from the IRtcEngine::getCallId method.
    @param description (Optional) Description for the call. Character length must be less than 800 bytes.

    @return

    - 0: Success.
    - < 0: Failure.

    */
    virtual int complain(const char* callId, const char* description) = 0;

    /** Registers a packet observer.

    The Agora Native SDK allows your application to register a packet observer to receive events for voice or video packet transmission.

    @param observer Packet observer to be registered: IPacketObserver
    @return

- 0: Success.
- < 0: Failure.
    */
    virtual int registerPacketObserver(IPacketObserver* observer) = 0;

	/** Sets the built-in encryption mode.

     The Agora Native SDK supports built-in encryption, which is set to AES-128-XTS mode by default. Call this API method to set the encryption mode to use other encryption modes.

     All users in the same channel must use the same encryption mode and password.

     Refer to the information related to the AES encryption algorithm on the differences between the encryption modes.

     @note Call IRtcEngine::setEncryptionSecret to enable the built-in encryption function before calling this method.

	@param encryptionMode Encryption mode:
  - "aes-128-xts":128-bit AES encryption, XTS mode
  - "aes-128-ecb":128-bit AES encryption, ECB mode
  - "aes-256-xts": 256-bit AES encryption, XTS mode
  - "": When encryptionMode is set to NULL, the encryption mode is set to "aes-128-xts" by default.
	@return

- 0: Success.
- < 0: Failure.
	*/
	virtual int setEncryptionMode(const char* encryptionMode) = 0;
    
	/** Sets the encryption password to enable built-in encryption before joining a channel.

     All users in a channel must set the same encryption password. The encryption password is automatically cleared once a user has left the channel.

     If the encryption password is not specified or set to empty, the encryption functionality will be disabled.

     @note Do not use this method for CDN live streaming.

     @param secret Encryption password
     @return

- 0: Success.
- < 0: Failure.
	*/
	virtual int setEncryptionSecret(const char* secret) = 0;
/** Creates a data stream.

Each user can have up to five simultaneous data channels.

@note Set both the reliable and ordered parameters to true or false. Do not set one as true and the other as false.

@param reliable

- true: The recipients will receive data from the sender within 5 seconds. If the recipient does not receive the data within 5 seconds, the data channel will report an error to the application.
- false: The recipients will not receive any data, and therefore it will not report any errors for missing data.

@param ordered

- true: The recipients will receive the data in the order that they were sent.
- false: The recipients will not receive the data in the order that they were sent.

@return

- 0: Success.
- < 0: Failure.

*/
    virtual int createDataStream(int* streamId, bool reliable, bool ordered) = 0;
    /** Sends data stream messages to all users in a channel.

Up to 30 packets can be sent per second in a channel. Each packet may have a maximum size of 1 kB.
The API controls the data channel transfer rate. Each client can send up to 6 kB of data per second. Each user can have up to five simultaneous data channels.

@note This method applies only to the communication mode or to the hosts in the live broadcast mode. If an audience in the live broadcast mode calls this method, his role might be changed to host.

@param  streamId  Stream ID returned by IRtcEngine::createDataStream.
@param message Data to be sent.

@return

- 0: Success.
- < 0: Failure.
    */
    virtual int sendStreamMessage(int streamId, const char* data, size_t length) = 0;
/** @deprecated

Sets the picture-in-picture layout for the CDN live broadcast.

@note This method has been deprecated and Agora recommends using the IRtcEngine::setLiveTranscoding method. This method sets the picture-in-picture layouts for live broadcasts. This method is only applicable when you want to push streams to the Agora server. When you push the stream to the server:

1. Define a canvas, its width and height (video resolution), background color, and the total number of videos you want to display.
2. Define the position and size for each video on the canvas, and indicate whether the view is cropped or zoomed to fit.

@note

- Call this method after joining a channel.
 - The application should only allow one user to call this method in the same channel if more than one user calls this method, the other users must call IRtcEngine::clearVideoCompositingLayout to remove the settings.

*/
    virtual int setVideoCompositingLayout(const VideoCompositingLayout& sei) = 0;
/** @deprecated
 Removes the picture-in-picture layout settings created by IRtcEngine::setVideoCompositingLayout.
*/
    virtual int clearVideoCompositingLayout() = 0;
/** @deprecated

Configures the CDN live streaming before joining a channel.

 This method is deprecated. Agora recommends using the following methods instead:

 - IRtcEngine::addPublishStreamUrl
 - IRtcEngine::removePublishStreamUrl
 - IRtcEngine::setLiveTranscoding

@param config CDN live streaming settings: PublisherConfiguration
    */
	virtual int configPublisher(const PublisherConfiguration& config) = 0;
/** Adds a stream URL for the stream.

Used for CDN live streaming. The publisher pushes the stream to the specified CDN live URL address. This method only adds one stream URL each time it is called. The URL may not contain special characters such as Chinese language characters.

@note Ensure the user has joined the channel before calling this method.

@param url URL where the publisher pushes the stream.

@param  transcodingEnabled

- true: Enables transcoding
- false: Disables transcoding
*/
    virtual int addPublishStreamUrl(const char *url, bool transcodingEnabled) = 0;
/** Removes a stream URL.

This method is used for CDN live streams and removes a stream URL each time it is called.

@note

- This method removes only one URL each time it is called.
- The URL must not contain special characters such as Chinese language characters.

@param url URL where the publisher pushes the stream.

*/
    virtual int removePublishStreamUrl(const char *url) = 0;
    /** Sets live transcoding.
     
     This method sets the video layout and audio for CDN live streaming only.
    
     @param transcoding See: LiveTranscoding

    */
    virtual int setLiveTranscoding(const LiveTranscoding &transcoding) = 0;
/** Adds a watermark to the local video stream.

This method adds a PNG watermark to the local video stream for the recording device, channel audience, and CDN live audience to see and capture.

To add the PNG file only to the CDN live publishing stream, see the IRtcEngine::setLiveTranscoding method.

@param watermark RtcImage that will be used as the watermark for the local video stream.

@note

- The URL descriptions are different for the local video streaming and CDN live streaming. In a local broadcast, url refers to the definite path of the watermark image file to be added onto the local video; while in a CDN live broadcast, url refers to the URL address of the watermark image to be added onto the CDN live broadcast.
- The source file of the watermark image must be in the PNG file format. If the width and height of the PNG file differ from your settings in this method, the PNG file will be cropped to conform to your settings.
- The Agora SDK supports adding only one watermark image onto a live stream. The newly added watermark image will replace the previous one.

*/
    virtual int addVideoWatermark(const RtcImage& watermark) = 0;
    /** Removes the watermark image from the video stream that was added by IRtcEngine::addVideoWatermark.
    */
    virtual int clearVideoWatermarks() = 0;
/** Adds a voice or video stream URL to a live broadcast.

 When the stream URL is added successfully, it can be found in the channel with a uid of 666, and IRtcEngineEventHandler::onUserJoined and IRtcEngineEventHandler::onFirstRemoteVideoFrame will be triggered.

@param url URL address to be added to the ongoing live broadcast. Valid protocols are RTMP, HLS, and FLV.

@param config InjectStreamConfig object which contains the configuration information for the added voice or video stream.
*/
    virtual int addInjectStreamUrl(const char* url, const InjectStreamConfig& config) = 0;
    /** Removes the voice or video stream URL from a live broadcast.

     @note If successful, the IRtcEngineEventHandler::onUserOffline will be triggered, with a uid of 666.

     @param url URL address of the added stream to be removed.

     */
    virtual int removeInjectStreamUrl(const char* url) = 0;

    virtual bool registerEventHandler(IRtcEngineEventHandler *eventHandler) = 0;
    virtual bool unregisterEventHandler(IRtcEngineEventHandler *eventHandler) = 0;
};


class IRtcEngineParameter
{
public:
    /**
    * Releases the resource.
    */
    virtual void release() = 0;

    /** Sets the bool value of the JSON.
    @param key Key name
    @param value Value
    @return

- 0: Success.
- < 0: Failure.
    */
    virtual int setBool(const char* key, bool value) = 0;

    /** Sets the int value of the JSON.
    @param key Key name
    @param value Value
    @return

- 0: Success.
- < 0: Failure.
    */
    virtual int setInt(const char* key, int value) = 0;

    /** Sets the unsigned int value of the JSON.
    @param key Key name
    @param value Value
    @return

- 0: Success.
- < 0: Failure.
    */
    virtual int setUInt(const char* key, unsigned int value) = 0;

    /** Sets the double value of the JSON.
    @param key Key name
    @param value Value
    @return

- 0: Success.
- < 0: Failure.
    */
    virtual int setNumber(const char* key, double value) = 0;

    /** Sets the string value of the JSON.
    @param key Key name
    @param value Value
    @return

- 0: Success.
- < 0: Failure.
    */
    virtual int setString(const char* key, const char* value) = 0;

    /** Sets the object value of the JSON.
    @param key Key name
    @param value Value
    @return

- 0: Success.
- < 0: Failure.
    */
    virtual int setObject(const char* key, const char* value) = 0;

    /** Retrieves the bool value of the JSON.
    @param key Key name
    @param value Value
    @return

- 0: Success.
- < 0: Failure.
    */
    virtual int getBool(const char* key, bool& value) = 0;

    /** Retrieves the int value of the JSON.
    @param key Key name
    @param value Value
    @return

- 0: Success.
- < 0: Failure.
    */
    virtual int getInt(const char* key, int& value) = 0;

    /** Retrieves the unsigned int value of the JSON.
    @param key Key name
    @param value Value
    @return

- 0: Success.
- < 0: Failure.
    */
    virtual int getUInt(const char* key, unsigned int& value) = 0;

    /** Retrieves the double value of the JSON.
    @param key Key name
    @param value Value
    @return

- 0: Success.
- < 0: Failure.
    */
    virtual int getNumber(const char* key, double& value) = 0;

    /** Retrieves the string value of the JSON.
    @param key Key name
    @param value Value
    @return

- 0: Success.
- < 0: Failure.
    */
    virtual int getString(const char* key, agora::util::AString& value) = 0;

    /** Retrieves a child object value of the JSON.
    @param key Key name
    @param value Value
    @return

- 0: Success.
- < 0: Failure.
    */
    virtual int getObject(const char* key, agora::util::AString& value) = 0;

    /** Retrieves the array value of the JSON.
    @param key Key name
    @param value Value
    @return

- 0: Success.
- < 0: Failure.
    */
    virtual int getArray(const char* key, agora::util::AString& value) = 0;

    /** Sets the parameters of the SDK or engine.
    @param parameters Parameters
    @return

- 0: Success.
- < 0: Failure.
    */
    virtual int setParameters(const char* parameters) = 0;

    /** Sets the profile to control the RTC engine.

    @param profile Profile
    @param merge Boolean value to determine If the profile data should merge with the original value:

     - true: Merge with the original value.
     - false: Do not merge with the original value.
    @return

- 0: Success.
- < 0: Failure.
    */
    virtual int setProfile(const char* profile, bool merge) = 0;

	virtual int convertPath(const char* filePath, agora::util::AString& value) = 0;
};

class AAudioDeviceManager : public agora::util::AutoPtr<IAudioDeviceManager>
{
public:
    AAudioDeviceManager(IRtcEngine* engine)
    {
		queryInterface(engine, AGORA_IID_AUDIO_DEVICE_MANAGER);
    }
};

class AVideoDeviceManager : public agora::util::AutoPtr<IVideoDeviceManager>
{
public:
    AVideoDeviceManager(IRtcEngine* engine)
    {
		queryInterface(engine, AGORA_IID_VIDEO_DEVICE_MANAGER);
    }
};

class AParameter : public agora::util::AutoPtr<IRtcEngineParameter>
{
public:
    AParameter(IRtcEngine& engine) { initialize(&engine); }
    AParameter(IRtcEngine* engine) { initialize(engine); }
    AParameter(IRtcEngineParameter* p) :agora::util::AutoPtr<IRtcEngineParameter>(p) {}
private:
    bool initialize(IRtcEngine* engine)
    {
        IRtcEngineParameter* p = NULL;
        if (engine && !engine->queryInterface(AGORA_IID_RTC_ENGINE_PARAMETER, (void**)&p))
            reset(p);
        return p != NULL;
    }
};
/** The RtcEngineParameters class is an auxiliary class that sets parameters for the SDK.

*/
class RtcEngineParameters
{
public:
    RtcEngineParameters(IRtcEngine& engine)
        :m_parameter(&engine){}
    RtcEngineParameters(IRtcEngine* engine)
        :m_parameter(engine){}

    /** Mutes a local audio stream.

    This method mutes or unmutes the local audio. It enables or disables sending local audio streams to the network.

@note This method does not disable the microphone, and therefore does not affect any recording process.

@param mute

- true: Mutes the local audio.
- false: Unmutes the local audio.

@return

- 0: Success.
- < 0: Failure.
    */
    int muteLocalAudioStream(bool mute) {
        return setParameters("{\"rtc.audio.mute_me\":%s,\"che.audio.mute_me\":%s}", mute ? "true" : "false", mute ? "true" : "false");
    }
    // mute/unmute all peers. unmute will clear all muted peers specified mutePeer() interface
  /** Mutes all remote users’ audio streams.

@note  When set to true, this method mutes the audio streams without affecting the audio streams' receiving process.

@param mute

- true: Mutes all received audio streams.
- false: Unmute all received audio streams.
@return

- 0: Success.
- < 0: Failure.
    */
    int muteAllRemoteAudioStreams(bool mute) {
        return m_parameter ? m_parameter->setBool("rtc.audio.mute_peers", mute) : -ERR_NOT_INITIALIZED;
    }

    /** Sets the default value of muting or unmuting all remote audio receiving streams.

@param mute

- true:  Mutes all remote audio receiving streams by default.
- false: (Default) Unmutes all remote audio receiving streams by default.
@return

- 0: Success.
- < 0: Failure.
    */
    int setDefaultMuteAllRemoteAudioStreams(bool mute) {
        return m_parameter ? m_parameter->setBool("rtc.audio.set_default_mute_peers", mute) : -ERR_NOT_INITIALIZED;
    }

    /** Mutes a specified user's audio stream.

    @note When set to true, this method mutes the audio stream
  without affecting the audio stream's receiving process.

@param uid User ID of the user whose audio stream will be muted.
@param mute

- true: Mutes the user’s audio stream.
- false: Unmutes the user’s audio stream.
@return

- 0: Success.
- < 0: Failure.
    */
    int muteRemoteAudioStream(uid_t uid, bool mute) {
        return setObject("rtc.audio.mute_peer", "{\"uid\":%u,\"mute\":%s}", uid, mute?"true":"false");
    }

    /** Stops sending the local video streams to the network.

    @note When set to true, this method does not disable the camera, and therefore does not affect the retrieval of the local video streams.

@param mute

- true: Stops sending local video streams to the network.
- false: Allows sending local video streams to the network.
@return

- 0: Success.
- < 0: Failure.
    */
    int muteLocalVideoStream(bool mute) {
        return setParameters("{\"rtc.video.mute_me\":%s,\"che.video.local.send\":%s}", mute ? "true" : "false", mute ? "false" : "true");
    }

/** Disables the local video.

This method disables the local video. Only applicable when the user wants to watch the remote video without sending any video stream to the other user. This method does not require a local camera.

 @note This method controls the underlying states of the Engine. It is still valid after the user leaves the channel.


@param  enabled

- true: (Default) Enables the local video.
- false: Disables the local video.

@return

- 0: Success.
- < 0: Failure.

*/
	int enableLocalVideo(bool enabled) {
		return setParameters("{\"rtc.video.capture\":%s,\"che.video.local.capture\":%s,\"che.video.local.render\":%s,\"che.video.local.send\":%s}", enabled ? "true" : "false", enabled ? "true" : "false", enabled ? "true" : "false", enabled ? "true" : "false");
	}
    /** Stops playing all received video streams from the remote users.

    @note     When set to true, this method stops playing all video streams without affecting the video streams' receiving process.

@param  mute

- true: Stops playing all received video streams from the remote users.
- false: (Default) Allows playing all received video streams from the remote users.
@return

- 0: Success.
- < 0: Failure.
    */
    int muteAllRemoteVideoStreams(bool mute) {
        return m_parameter ? m_parameter->setBool("rtc.video.mute_peers", mute) : -ERR_NOT_INITIALIZED;
    }

    /** Sets the default value to stop all received remote video streams from the remote users.

@param mute

- true: Sets the default to stop playing all received video streams from the remote users.
- false: (Default) Sets the default to allow playing all received video streams from the remote users.
@return

- 0: Success.
- < 0: Failure.
    */
    int setDefaultMuteAllRemoteVideoStreams(bool mute) {
        return m_parameter ? m_parameter->setBool("rtc.video.set_default_mute_peers", mute) : -ERR_NOT_INITIALIZED;
    }

    /** Stops playing a specified user’s video stream.

@param uid User ID of the specified user.

@param mute

- true: Stops playing a specified user’s video stream.
- false: Allows playing a specified user’s video stream.
@return

- 0: Success.
- < 0: Failure.
    */
    int muteRemoteVideoStream(uid_t uid, bool mute) {
        return setObject("rtc.video.mute_peer", "{\"uid\":%u,\"mute\":%s}", uid, mute ? "true" : "false");
    }
/** Sets the remote user’s video stream type. Received by the local user when the remote user sends dual streams.

This method allows the application to adjust the corresponding video-stream type based on the size of the video window to reduce the bandwidth and resources.

- If the remote user has enabled the dual-stream mode by calling enableDualStreamMode, the SDK will receive the high-video stream by default.
- If the dual-stream mode is not enabled, the SDK will receive the high-video stream by default.

The method result will be returned in the IRtcEngineEventHandler::onApiCallExecuted callback method. The Agora SDK receives the high-video stream by default to reduce the bandwidth. If needed, users may use this method to switch to the low-video stream.

 By default, the aspect ratio of the low-video stream is the same as the high-video stream. Once the resolution of the high-video stream is set, the system automatically sets the resolution, frame rate and bitrate for the low-video stream.

@param uid User ID
 @param streamType   Video-stream size: #REMOTE_VIDEO_STREAM_TYPE

@return

- 0: Success.
- < 0: Failure.
*/
    int setRemoteVideoStreamType(uid_t uid, REMOTE_VIDEO_STREAM_TYPE streamType) {
        return setParameters("{\"rtc.video.set_remote_video_stream\":{\"uid\":%u,\"stream\":%d}, \"che.video.setstream\":{\"uid\":%u,\"stream\":%d}}", uid, streamType, uid, streamType);
//        return setObject("rtc.video.set_remote_video_stream", "{\"uid\":%u,\"stream\":%d}", uid, streamType);
    }
    /** Sets the default video-stream type for the video received by the local user when the remote user sends dual streams.

     - If the dual-stream mode is enabled by calling enableDualStreamMode, the user will receive the high-video stream by default. This method allows the application to adjust the corresponding video-stream type according to the size of the video window, reducing the bandwidth and resources.
     - If the dual-stream mode is not enabled, the user will receive the high-video stream by default.

     The result after calling this method will be returned by IRtcEngineEventHandler::onApiCallExecuted. The Agora SDK receives the high-video stream by default, to reduce the bandwidth. If needed, users can switch to the low-video stream through the use of this method.

The resolution of the high-video stream is 1.1, 4.3, or 16.9. By default, the aspect ratio of the low-video stream is the same as the high-video stream. Once the resolution of the high-video stream is set, the system automatically sets the aspect ratio for the low-video stream.

    | Resolution | Frame Rate | Keyframe Interval | Bitrate (kbit/s)|
    |------------|------------|-------------------|-----------------|
    | 160 x 160  | 5          | 5                 | 45              |
    | 160 x 120  | 5          | 5                 | 32              |
    | 160 x 90   | 5          | 5                 | 28              |

     @param streamType   Video-stream size: #REMOTE_VIDEO_STREAM_TYPE

    @return

    - 0: Success.
    - < 0: Failure.
    */
    int setRemoteDefaultVideoStreamType(REMOTE_VIDEO_STREAM_TYPE streamType) {
        return m_parameter ? m_parameter->setInt("rtc.video.set_remote_default_video_stream_type", streamType) : -ERR_NOT_INITIALIZED;
    }

    /** @deprecated Use IAudioDeviceManager::setPlaybackDeviceVolume instead. Sets the playback device volume.

@param volume Volume of the playing device. The value ranges from 0 to 255.
@return

- 0: Success.
- < 0: Failure.
    */
    int setPlaybackDeviceVolume(int volume) {// [0,255]
        return m_parameter ? m_parameter->setInt("che.audio.output.volume", volume) : -ERR_NOT_INITIALIZED;
    }

    /** Enables the SDK to regularly report to the application on which user is talking and the speaker's volume.

    Once the method is enabled, the SDK returns the volume indication at the set time interval in the IRtcEngineEventHandler::onAudioVolumeIndication callback, regardless of whether anyone is speaking in the channel.

@param interval Time interval between two consecutive volume indications:

- <= 0: Disables the volume indication
- > 0: Time interval (ms) between two consecutive volume indications. Agora recommends setting it to more than 200 ms. Do not set it lower than 10 ms, or the onAudioVolumeIndication callback will not be triggered.

@param smooth  Smoothing factor that sets the sensitivity of the audio volume indicator. The value ranges between 0 and 10. The greater the value, the more sensitive the indicator. The recommended value is 3.
@return

- 0: Success.
- < 0: Failure.
    */
    int enableAudioVolumeIndication(int interval, int smooth) { // in ms: <= 0: disable, > 0: enable, interval in ms
        if (interval < 0)
            interval = 0;
        return setObject("che.audio.volume_indication", "{\"interval\":%d,\"smooth\":%d}", interval, smooth);
    }

    /** Starts an audio recording.

    The SDK allows recording during a call. Supported formats:

- *.wav*: Large file size with high sound fidelity **OR**

- *.aac*: Small file size with low sound fidelity

Ensure that the directory to save the recording file exists and is writable.
This method is usually called after the IRtcEngine::joinChannel method.
The recording automatically stops when the IRtcEngine::leaveChannel method is called.

    @param filePath Full file path of the recording file. The string of the file name is in UTF-8.
    @return

- 0: Success.
- < 0: Failure.
    */
    int startAudioRecording(const char* filePath, AUDIO_RECORDING_QUALITY_TYPE quality) {
        if (!m_parameter) return -ERR_NOT_INITIALIZED;
#if defined(_WIN32)
        util::AString path;
        if (!m_parameter->convertPath(filePath, path))
            filePath = path->c_str();
        else
            return -ERR_INVALID_ARGUMENT;
#endif
        return setObject("che.audio.start_recording", "{\"filePath\":\"%s\",\"quality\":%d}", filePath, quality);
    }

    /** Stops an audio recording on the client.

You can call this method before calling IRtcEngine::leaveChannel; else, the recording automatically stops when the leaveChannel method is called.

    @return

- 0: Success.
- < 0: Failure.
    */
    int stopAudioRecording() {
        return m_parameter ? m_parameter->setBool("che.audio.stop_recording", true) : -ERR_NOT_INITIALIZED;
    }

	/** Stops audio mixing.

  This method mixes the specified local audio file with the audio stream from the microphone; or, it replaces the microphone's audio stream with the specified local audio file. You can choose whether the other user can hear the local audio playback and specify the number of playback loops. This API method also supports online music playback.

@note Call this API when you are in a channel, otherwise, it may cause issues.

	@param filePath Name and path of the local audio file to be mixed. Supported audio formats: mp3, aac, m4a, 3gp, and wav.
	@param loopback

  - true: Only the local user can hear the remix or the replaced audio stream.
  - false: Both users can hear the remix or the replaced audio stream.

	@param replace

  - true: The content of the local audio file replaces the audio stream from the microphone.
  -  false:  The local audio file is mixed with the audio stream from the microphone.

	@param cycle Number of loop playbacks:
  - Positive integer: Number of loop playbacks.
  - -1: Infinite loop.

	@return

- 0: Success.
- < 0: Failure.
	*/
	int startAudioMixing(const char* filePath, bool loopback, bool replace, int cycle) {
        if (!m_parameter) return -ERR_NOT_INITIALIZED;
#if defined(_WIN32)
		util::AString path;
		if (!m_parameter->convertPath(filePath, path))
			filePath = path->c_str();
		else
			return -ERR_INVALID_ARGUMENT;
#endif
		return setObject("che.audio.start_file_as_playout", "{\"filePath\":\"%s\",\"loopback\":%s,\"replace\":%s,\"cycle\":%d}",
					filePath,
					loopback?"true":"false",
					replace?"true":"false",
					cycle);
	}
	/** Stops the audio mixing.

  Call this API when you are in a channel.
	@return

- 0: Success.
- < 0: Failure.
	*/
	int stopAudioMixing() {
        return m_parameter ? m_parameter->setBool("che.audio.stop_file_as_playout", true) : -ERR_NOT_INITIALIZED;
	}

/** Pauses audio mixing.

Call this API when you are in a channel.

@return

- 0: Success.
- < 0: Failure.
*/
    int pauseAudioMixing() {
        return m_parameter ? m_parameter->setBool("che.audio.pause_file_as_playout", true) : -ERR_NOT_INITIALIZED;
    }
/** Resumes audio mixing.

Call this API when you are in a channel.

@return

- 0: Success.
- < 0: Failure.
*/
    int resumeAudioMixing() {
        return m_parameter ? m_parameter->setBool("che.audio.pause_file_as_playout", false) : -ERR_NOT_INITIALIZED;
    }
/** Adjusts the volume during audio mixing.

Call this API when you are in a channel.

@param volume Volume. The value ranges from 0 to 100 (default).

@return

- 0: Success.
- < 0: Failure.
*/
    int adjustAudioMixingVolume(int volume) {
        return m_parameter ? m_parameter->setInt("che.audio.set_file_as_playout_volume", volume) : -ERR_NOT_INITIALIZED;
    }

    /** Retrieves the duration (ms) of the audio mixing.

     Call this API when you are in a channel.

     @return

     - 0: Success.
     - < 0: Failure.
    */
    int getAudioMixingDuration() {
        int duration = 0;
        int r = m_parameter ? m_parameter->getInt("che.audio.get_mixing_file_length_ms", duration) : -ERR_NOT_INITIALIZED;
        if (r == 0)
            r = duration;
        return r;
    }

    /** Retrieves the playback position (ms) of the audio.

    Call this API when you are in a channel.
    */
    int getAudioMixingCurrentPosition() {
        if (!m_parameter) return -ERR_NOT_INITIALIZED;
        int pos = 0;
        int r = m_parameter->getInt("che.audio.get_mixing_file_played_ms", pos);
        if (r == 0)
            r = pos;
        return r;
    }
    /** Sets the playback position of the audio mixing file to a different start position (default plays from the beginning).

    @param pos Integer. The time (ms) to start playing the audio mixing file.

    @return

    - 0: Success.
    - < 0: Failure.
    */
    int setAudioMixingPosition(int pos /*in ms*/) {
        return m_parameter ? m_parameter->setInt("che.audio.mixing.file.position", pos) : -ERR_NOT_INITIALIZED;
    }

    /** Retrieves the volume of the audio effects.

     The value ranges from 0.0 to 100.0.

    @return

     - Audio effect volume: Success.
     - < 0: Failure.
     */
    int getEffectsVolume() {
        if (!m_parameter) return -ERR_NOT_INITIALIZED;
        int volume = 0;
        int r = m_parameter->getInt("che.audio.game_get_effects_volume", volume);
        if (r == 0)
            r = volume;
        return r;
    }

    /** Sets the volume of the audio effects from 0.0 to 100.0.

     @param volume Volume. The value ranges from 0.0 to 100.0 (default).
     @return

     - 0: Success.
     - < 0: Failure.
     */
    int setEffectsVolume(int volume) {
        return m_parameter ? m_parameter->setInt("che.audio.game_set_effects_volume", volume) : -ERR_NOT_INITIALIZED;
    }

    /** Sets the volume of the specified sound effect.

     @param soundId ID of the audio effect. Each audio effect has a unique ID.
     @param volume Volume. The value ranges from 0.0 to 100.0 (default).
     @return

- 0: Success.
- < 0: Failure.
     */
    int setVolumeOfEffect(int soundId, int volume) {
        return setObject(
            "che.audio.game_adjust_effect_volume",
            "{\"soundId\":%d,\"gain\":%d}",
            soundId, volume);
    }

    /** Plays the specified audio effect.

  @param soundId ID of the specified audio effect. Each audio effect has a unique ID.

  @note If the audio effect is preloaded into the memory through preloadEffect, ensure that the soundID value is set to the same value as in preloadEffect.

  @param filePath The absolute path of the audio effect file.
  @param loopCount Set the number of times the audio effect loops:

  - 0: Play the audio effect once.
  - 1: Play the audio effect twice.
  - -1: Play the audio effect in a loop indefinitely, until stopEffect or stopAllEffects is called.

@param pitch Pitch of the audio effect. The value range is [0.5, 2]. The default value is 1, which means no change to the pitch. The smaller the value, the lower the pitch.

@param pan Spatial position of the audio effect. The value range is [-1.0, 1.0]:

- 0.0: The audio effect displays ahead.
- 1.0: The audio effect displays to the right.
- -1.0: The audio effect displays to the left.

@param gain  Volume of the audio effect. The value range is [0.0, 100,0]. The default value is 100.0. The smaller the value, the lower the volume of the audio effect

@param publish Whether to publish the specified audio effect to the remote stream:

- true: The audio effect, played locally, is published to the Agora Cloud and the remote users can hear it.
- false: The audio effect, played locally, is not published to the Agora Cloud and the remote users cannot hear it.

@return

- 0: Success.
- < 0: Failure.
     *
     */
    int playEffect(int soundId, const char* filePath, int loopCount, double pitch, double pan, int gain, bool publish = false) {
#if defined(_WIN32)
        util::AString path;
        if (!m_parameter->convertPath(filePath, path))
            filePath = path->c_str();
        else if (!filePath)
            filePath = "";
#endif
        return setObject(
            "che.audio.game_play_effect",
            "{\"soundId\":%d,\"filePath\":\"%s\",\"loopCount\":%d, \"pitch\":%lf,\"pan\":%lf,\"gain\":%d, \"send2far\":%d}",
            soundId, filePath, loopCount, pitch, pan, gain, publish);
    }

    /** Stops playing a specified audio effect.

     @param soundId ID of the audio effect. Each audio effect has a unique ID.
     @return

- 0: Success.
- < 0: Failure.
     */
    int stopEffect(int soundId) {
        return m_parameter ? m_parameter->setInt(
            "che.audio.game_stop_effect", soundId) : -ERR_NOT_INITIALIZED;
    }

    /** Stops playing all audio effects.

     @return

- 0: Success.
- < 0: Failure.
     */
    int stopAllEffects() {
        return m_parameter ? m_parameter->setBool(
            "che.audio.game_stop_all_effects", true) : -ERR_NOT_INITIALIZED;
    }

    /** Preloads a specified audio effect file (compressed audio file) to the memory.

     @param soundId ID of the audio effect. Each audio effect has a unique ID.
     @param filepath Absolute path of the audio effect file.
     @return

- 0: Success.
- < 0: Failure.
     */
    int preloadEffect(int soundId, char* filePath) {
        return setObject(
            "che.audio.game_preload_effect",
            "{\"soundId\":%d,\"filePath\":\"%s\"}",
            soundId, filePath);
    }

    /** Releases a specified preloaded audio effect from the memory.

     @param soundId ID of the audio effect. Each audio effect has a unique ID.
     @return

- 0: Success.
- < 0: Failure.
     */
    int unloadEffect(int soundId) {
        return m_parameter ? m_parameter->setInt(
            "che.audio.game_unload_effect", soundId) : -ERR_NOT_INITIALIZED;
    }

    /** Pauses a specified audio effect.

     @param soundId ID of the audio effect. Each audio effect has a unique ID.
     @return

- 0: Success.
- < 0: Failure.
     */
    int pauseEffect(int soundId) {
        return m_parameter ? m_parameter->setInt(
            "che.audio.game_pause_effect", soundId) : -ERR_NOT_INITIALIZED;
    }

    /** Pauses all audio effects.

     @return

- 0: Success.
- < 0: Failure.
     */
    int pauseAllEffects() {
        return m_parameter ? m_parameter->setBool(
            "che.audio.game_pause_all_effects", true) : -ERR_NOT_INITIALIZED;
    }

    /** Resumes playing a specified audio effect.

     @param soundId ID of the audio effect. Each audio effect has a unique ID.
     @return

- 0: Success.
- < 0: Failure.
     */
    int resumeEffect(int soundId) {
        return m_parameter ? m_parameter->setInt(
            "che.audio.game_resume_effect", soundId) : -ERR_NOT_INITIALIZED;
    }

    /** Resumes playing all audio effects.
     @return

- 0: Success.
- < 0: Failure.
     */
    int resumeAllEffects() {
        return m_parameter ? m_parameter->setBool(
            "che.audio.game_resume_all_effects", true) : -ERR_NOT_INITIALIZED;
    }

    /** Changes the voice pitch of the local speaker.

@param pitch Voice frequency. Value range is [0.5, 2.0]. The default value is 1.0.

@return

- 0: Success.
- < 0: Failure.
     */
    int setLocalVoicePitch(double pitch) {
        return m_parameter ? m_parameter->setInt(
            "che.audio.morph.pitch_shift",
            static_cast<int>(pitch * 100)) : -ERR_NOT_INITIALIZED;
    }
    /** Sets the local voice equalization effect.

     @param bandFrequency Band frequency. The value ranges from 0 to 9, representing the respective 10-band center frequencies of the voice effects, including 31, 62, 125, 500, 1k, 2k, 4k, 8k, and 16k Hz. See #AUDIO_EQUALIZATION_BAND_FREQUENCY
     @param bandGain  Gain of each band in dB. The value ranges from -15 to 15.

    */
    int setLocalVoiceEqualization(AUDIO_EQUALIZATION_BAND_FREQUENCY bandFrequency, int bandGain) {
        return setObject(
            "che.audio.morph.equalization",
            "{\"index\":%d,\"gain\":%d}",
            static_cast<int>(bandFrequency), bandGain);
    }
    /**  Sets the local voice reverberation.

     @param reverbKey The reverberation key: #AUDIO_REVERB_TYPE.
    @param value Value of the reverberation key.
    */
    int setLocalVoiceReverb(AUDIO_REVERB_TYPE reverbKey, int value) {
        return setObject(
            "che.audio.morph.reverb",
            "{\"key\":%d,\"value\":%d}",
            static_cast<int>(reverbKey), value);
    }
    /** Sets the volume of the in-ear monitor.

@param volume Volume of the in-ear monitor. The value ranges from 0 to 100 (default).

@return

- 0: Success.
- < 0: Failure.
     */
    int setInEarMonitoringVolume(int volume) {
        return m_parameter ? m_parameter->setInt("che.audio.headset.monitoring.parameter", volume) : -ERR_NOT_INITIALIZED;
    }

    /** @deprecated Use IRtcEngine::disableAudio instead. Disables the audio function in the channel.

@return

- 0: Success.
- < 0: Failure.
     */
    int pauseAudio() {
        return m_parameter ? m_parameter->setBool("che.pause.audio", true) : -ERR_NOT_INITIALIZED;
    }

    /**
     * @deprecated Use IRtcEngine::enableAudio instead.
     * Resumes the audio in the channel.
     @return

- 0: Success.
- < 0: Failure.
     */
    int resumeAudio() {
        return m_parameter ? m_parameter->setBool("che.pause.audio", false) : -ERR_NOT_INITIALIZED;
    }
/** Sets the external audio source.

@param    enabled

- true: Enables the use of the external audio source.
- false: Disables the use of the external audio source.

  @param sampleRate	Sampling rate of the external audio source.
  @param channels	 Number of the external audio source channels (two channels maximum).
*/
    int setExternalAudioSource(bool enabled, int sampleRate, int channels) {
        if (enabled)
            return setParameters("{\"che.audio.external_capture\":true,\"che.audio.external_capture.push\":true,\"che.audio.set_capture_raw_audio_format\":{\"sampleRate\":%d,\"channelCnt\":%d,\"mode\":%d}}", sampleRate, channels, RAW_AUDIO_FRAME_OP_MODE_TYPE::RAW_AUDIO_FRAME_OP_MODE_READ_WRITE);
        else
            return setParameters("{\"che.audio.external_capture\":false,\"che.audio.external_capture.push\":false}");
    }

    /** Specifies an SDK output log file.

    The log file records all log data for the SDK’s operation. Ensure the directory for the log file exists and is writable.

@note The default log file is located at: C:\Users\<user_name>\AppData\Local\Agora\<process_name>.

@param filePath File path for the log file. The string of the log file is in the UTF-8 code.

@return

- 0: Success.
- < 0: Failure.
    */
    int setLogFile(const char* filePath) {
        if (!m_parameter) return -ERR_NOT_INITIALIZED;
#if defined(_WIN32)
		util::AString path;
		if (!m_parameter->convertPath(filePath, path))
			filePath = path->c_str();
		else if (!filePath)
			filePath = "";
#endif
		return m_parameter->setString("rtc.log_file", filePath);
    }

    /** Sets the output log level of the SDK.

    You can use one or a combination of the filters. The log level follows the sequence of OFF, CRITICAL, ERROR, WARNING, INFO, and DEBUG. Choose a level to see the logs that precede that level.

For example, if you set the log level to WARNING, you will see the logs within levels CRITICAL, ERROR and WARNING.

     @param filter Log filter level: #LOG_FILTER_TYPE

@return

- 0: Success.
- < 0: Failure.
    */
    int setLogFilter(unsigned int filter) {
        return m_parameter ? m_parameter->setUInt("rtc.log_filter", filter&LOG_FILTER_MASK) : -ERR_NOT_INITIALIZED;
    }

    /** Sets the local video display mode.

    This method may be invoked multiple times during a call, to change the display mode.

    @param renderMode  #RENDER_MODE_TYPE
    @return

- 0: Success.
- < 0: Failure.
    */
    int setLocalRenderMode(RENDER_MODE_TYPE renderMode) {
        return setRemoteRenderMode(0, renderMode);
    }

    /** Sets the remote video display mode.

    This method can be called multiple times during a call to change the display mode.

    @param renderMode  #RENDER_MODE_TYPE
    @return

- 0: Success.
- < 0: Failure.
    */
    int setRemoteRenderMode(uid_t uid, RENDER_MODE_TYPE renderMode) {
        return setObject("che.video.render_mode", "{\"uid\":%u,\"mode\":%d}", uid, renderMode);
    }
/** Sets the local video mirror mode.

Use this method before IRtcEngine::startPreview, or the mirror mode will not take effect until you re-enable startPreview.

 @param mirrorMode #VIDEO_MIRROR_MODE_TYPE
@return

- 0: Success.
- < 0: Failure.
*/
    int setLocalVideoMirrorMode(VIDEO_MIRROR_MODE_TYPE mirrorMode) {
        if (!m_parameter) return -ERR_NOT_INITIALIZED;
        const char *value;
        switch (mirrorMode) {
        case VIDEO_MIRROR_MODE_AUTO:
            value = "default";
            break;
        case VIDEO_MIRROR_MODE_ENABLED:
            value = "forceMirror";
            break;
        case VIDEO_MIRROR_MODE_DISABLED:
            value = "disableMirror";
            break;
        default:
            return -ERR_INVALID_ARGUMENT;
        }
        return m_parameter->setString("che.video.localViewMirrorSetting", value);
    }


/** Sets the stream mode to single- (default) or dual-stream mode.

This method sets the stream mode to single- (default) or dual-stream mode. After it is enabled, the receiver can choose to receive the high stream, that is, a high-resolution high-bitrate video stream, or low stream, that is, low-resolution low-bitrate video stream.
@param enabled

- true: Dual-stream mode.
- false: Single-stream mode.

*/
    int enableDualStreamMode(bool enabled) {
        return setParameters("{\"rtc.dual_stream_mode\":%s,\"che.video.enableLowBitRateStream\":%d}", enabled ? "true" : "false", enabled ? 1 : 0);
    }
/** Sets the audio recording format of the agora::media::IAudioFrameObserver::onRecordAudioFrame callback

@param sampleRate Sampling rate of the data returned by onRecordAudioFrame, which can set be as 8000, 16000, 32000, 44100, or 48000.
@param channel Number of channels for the data returned by onRecordAudioFrame, which can be set as 1 or 2:

- 1: Mono
- 2: Dual-track

 @param mode Use mode of the onRecordAudioFrame callback: #RAW_AUDIO_FRAME_OP_MODE_TYPE.
@param samplesPerCall Sample points of the data returned in onRecordAudioFrame. It is usually set as 1024 for stream pushing.

*/
    int setRecordingAudioFrameParameters(int sampleRate, int channel, RAW_AUDIO_FRAME_OP_MODE_TYPE mode, int samplesPerCall) {
        return setObject("che.audio.set_capture_raw_audio_format", "{\"sampleRate\":%d,\"channelCnt\":%d,\"mode\":%d,\"samplesPerCall\":%d}", sampleRate, channel, mode, samplesPerCall);
    }
    /** Sets the audio playback format for the agora::media::IAudioFrameObserver::onPlaybackAudioFrame callback.

    @param sampleRate Sampling rate of the data returned by onPlaybackAudioFrame, which can set be as 8000, 16000, 32000, 44100, or 48000.
    @param channel Number of channels for the data returned by onPlaybackAudioFrame, which can be set as 1 or 2:

    - 1: Mono
    - 2: Dual-track

     @param mode Use mode of the onPlaybackAudioFrame callback: #RAW_AUDIO_FRAME_OP_MODE_TYPE.
    @param samplesPerCall Sample points of the data returned in onPlaybackAudioFrame. It is usually set as 1024 for stream pushing.

    */
    int setPlaybackAudioFrameParameters(int sampleRate, int channel, RAW_AUDIO_FRAME_OP_MODE_TYPE mode, int samplesPerCall) {
        return setObject("che.audio.set_render_raw_audio_format", "{\"sampleRate\":%d,\"channelCnt\":%d,\"mode\":%d,\"samplesPerCall\":%d}", sampleRate, channel, mode, samplesPerCall);
    }
    /** Sets the mixed audio format for the agora::media::IAudioFrameObserver::onMixedAudioFrame callback.

        @param sampleRate Sampling rate of the callback data returned by onMixedAudioFrame, which can set be as 8000, 16000, 32000, 44100, or 48000.

        @param samplesPerCall Sample points of the data returned by onMixedAudioFrame. It is usually set as 1024 for stream pushing.

    */
    int setMixedAudioFrameParameters(int sampleRate, int samplesPerCall) {
        return setObject("che.audio.set_mixed_raw_audio_format", "{\"sampleRate\":%d,\"samplesPerCall\":%d}", sampleRate, samplesPerCall);
    }

    int muteRecordingSignal(bool enabled) {
        return setParameters("{\"che.audio.record.signal.mute\":%s}", enabled ? "true" : "false");
    }
/** Adjusts the recording volume.

@param volume Recording volume. The value ranges from 0 to 400:

- 0: Mute.
- 100: Original volume.
- 400: (Maximum) Four times the original volume with signal clipping protection.

@return

- 0: Success.
- < 0: Failure.
*/
    int adjustRecordingSignalVolume(int volume) {//[0, 400]: e.g. 50~0.5x 100~1x 400~4x
        if (volume < 0)
            volume = 0;
        else if (volume > 400)
            volume = 400;
        return m_parameter ? m_parameter->setInt("che.audio.record.signal.volume", volume) : -ERR_NOT_INITIALIZED;
    }
    /** Adjusts the playback volume.

@param volume Playback volume. The value ranges from 0 to 400:

- 0: Mute.
- 100: Original volume.
- 400: (Maximum) Four times the original volume with signal clipping protection.

@return

- 0: Success.
- < 0: Failure.
    */
    int adjustPlaybackSignalVolume(int volume) {//[0, 400]
        if (volume < 0)
            volume = 0;
        else if (volume > 400)
            volume = 400;
        return m_parameter ? m_parameter->setInt("che.audio.playout.signal.volume", volume) : -ERR_NOT_INITIALIZED;
    }
    /**
@deprecated Agora does not recommend using this method.

Sets the high-quality audio preferences. Call this method and set all three modes before joining a channel.

Do not call this method again after joining a channel.

@param fullband Full-band codec (48-kHz sampling rate), not compatible with SDK versions earlier than v1.7.4:

- true: Enables full-band codec.
- false: Disables full-band codec.

@param  stereo Stereo codec, not compatible with SDK versions earlier than v1.7.4:

- true: Enables the stereo codec.
- false: Disables the stereo codec.

@param fullBitrate  High bitrate. Recommended with voice-only mode:

- true: Enables high-bitrate mode.
- false: Disables high-bitrate mode.

@return

- 0: Success.
- < 0: Failure.
     */
    int setHighQualityAudioParameters(bool fullband, bool stereo, bool fullBitrate) {
        return setObject("che.audio.codec.hq", "{\"fullband\":%s,\"stereo\":%s,\"fullBitrate\":%s}", fullband ? "true" : "false", stereo ? "true" : "false", fullBitrate ? "true" : "false");
    }
    /** Enables interoperability with the Agora Web SDK.

    @param enabled

    - true: Enables interoperability with the Agora Web SDK.
    - false: Disables interoperability with the Agora Web SDK.
    */
    int enableWebSdkInteroperability(bool enabled) {//enable interoperability with zero-plugin web sdk
        return setParameters("{\"rtc.video.web_h264_interop_enable\":%s,\"che.video.web_h264_interop_enable\":%s}", enabled ? "true" : "false", enabled ? "true" : "false");
    }
    //only for live broadcast
    /** Sets the preferences for the high-quality video.

@param preferFrameRateOverImageQuality

- true: Frame rate preference over image quality.
- false: (Default) Image quality preference over frame rate.

    */
    int setVideoQualityParameters(bool preferFrameRateOverImageQuality) {
        return setParameters("{\"rtc.video.prefer_frame_rate\":%s,\"che.video.prefer_frame_rate\":%s}", preferFrameRateOverImageQuality ? "true" : "false", preferFrameRateOverImageQuality ? "true" : "false");
    }

    /** Sets the fallback option for the locally published stream based on the network conditions.

     If the option is set to #STREAM_FALLBACK_OPTION_AUDIO_ONLY, the SDK will:

- Disable the upstream video when the network cannot support both video and audio.
- Re-enable the video when the network condition improves.

When the locally publish stream falls back to audio-only or when the audio stream switches back to the video, the IRtcEngineEventHandler::onLocalPublishFallbackToAudioOnly callback will be triggered.

@note Agora does not recommend using this method for CDN live streaming, because the remote CDN live user will have a noticeable lag when the locally publish stream falls back to audio-only.

     @param  option The fallback options. See: #STREAM_FALLBACK_OPTIONS.
     @return

- 0: Success.
- < 0: Failure.
     */
    int setLocalPublishFallbackOption(STREAM_FALLBACK_OPTIONS option) {
        return m_parameter ? m_parameter->setInt("rtc.local_publish_fallback_option", option) : -ERR_NOT_INITIALIZED;
    }

    /** Sets the fallback option for the remotely subscribed stream based on the network conditions.

     If the option is set to #STREAM_FALLBACK_OPTION_AUDIO_ONLY, the SDK will automatically switch the video from a high-stream to a low-stream, or turn off the video when the downlink network condition cannot support both audio and video to guarantee the quality of the audio. The SDK keeps track of the network quality and restores the video stream when network conditions improve.
Once the locally published stream falls back to audio only or the audio stream switches back to the video stream, the IRtcEngineEventHandler::onRemoteSubscribeFallbackToAudioOnly callback will be triggered.

     @param  option  Fallback options for the remotely subscribed stream: #STREAM_FALLBACK_OPTIONS
     @return

- 0: Success.
- < 0: Failure.
     */
    int setRemoteSubscribeFallbackOption(STREAM_FALLBACK_OPTIONS option) {
        return m_parameter ? m_parameter->setInt("rtc.remote_subscribe_fallback_option", option) : -ERR_NOT_INITIALIZED;
    }

    /** Enables loopback recording.

@param enabled

- true: Enables loopback recording.
- false: Disables loopback recording.
@param deviceName Device name of the microphone.
@return

- 0: Success.
- < 0: Failure.
     */
    int enableLoopbackRecording(bool enabled, const char* deviceName = NULL) {
        if (!deviceName) {
            return setParameters("{\"che.audio.loopback.recording\":%s}", enabled ? "true" : "false");
        }
        else {
            return setParameters("{\"che.audio.loopback.deviceName\":\"%s\",\"che.audio.loopback.recording\":%s}", deviceName, enabled ? "true" : "false");
        }
    }

protected:
    AParameter& parameter() {
        return m_parameter;
    }
    int setParameters(const char* format, ...) {
        char buf[512];
        va_list args;
        va_start(args, format);
        vsnprintf(buf, sizeof(buf)-1, format, args);
        va_end(args);
        return m_parameter ? m_parameter->setParameters(buf) : -ERR_NOT_INITIALIZED;
    }
    int setObject(const char* key, const char* format, ...) {
        char buf[512];
        va_list args;
        va_start(args, format);
        vsnprintf(buf, sizeof(buf)-1, format, args);
        va_end(args);
        return m_parameter ? m_parameter->setObject(key, buf) : -ERR_NOT_INITIALIZED;
    }
    int stopAllRemoteVideo() {
        return m_parameter ? m_parameter->setBool("che.video.peer.stop_render", true) : -ERR_NOT_INITIALIZED;
    }
private:
    AParameter m_parameter;
};

} //namespace rtc
} // namespace agora


/** Retrieves the SDK version number.

@param build Build number of Agora the SDK.
* @return String of the SDK version.
*/
#define getAgoraRtcEngineVersion getAgoraSdkVersion

/** Creates the RTC engine object and returns the pointer.

* @return Pointer of the RTC engine object.
*/
AGORA_API agora::rtc::IRtcEngine* AGORA_CALL createAgoraRtcEngine();

/** Creates the RTC engine object and returns the pointer.

 @param err Error Code.
 * @return Description of the Error Code: #ERROR_CODE_TYPE
*/
#define getAgoraRtcEngineErrorDescription getAgoraSdkErrorDescription
#define setAgoraRtcEngineExternalSymbolLoader setAgoraSdkExternalSymbolLoader

#endif
