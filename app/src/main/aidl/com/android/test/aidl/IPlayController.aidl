package com.android.test.aidl;
import com.android.test.aidl.Music;

interface IPlayController {


            String getState();//获取当前播放器状态
            Music getMusicInfo();//获取当前播放的歌曲信息
            int getProgress();//获取当前播放进度
            String play();//播放
            String pause();//暂停
            String stop();//停止
            String playLast();//播放上一个
            String playNext();//播放下一个
            int getVolume();//获取当前音量
            int setVolume(int volume);//设置音量

}