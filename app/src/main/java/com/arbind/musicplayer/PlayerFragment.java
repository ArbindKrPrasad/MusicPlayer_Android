package com.arbind.musicplayer;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class PlayerFragment extends Fragment {
    ImageButton play, prev, next, ff, fb;
    TextView title, artist, album, startTimeTV, endTimeTV;
    MediaPlayer mediaPlayer;
    boolean isPlaying = false;
    String name, uri, artistName;
    String permission[] = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    boolean isFirstVisit = true;
    boolean isPause = false;
    boolean isInitialPlay = true;
    String path;
    String songPath = "";
    String songName = "";
    String songArtist = "";
    String songAlbum = "";
    String oldPath;
    int startDuration, endDuration;
    public static final String MyPreference = "MyPrif";
    public static final String jsonPreference = "JPref";
    SharedPreferences sharedpreferences, jsp;
    boolean isSongUpdated = false;
    int finalTime, startTime, endTime;
    int oneTimeOnly = 0;
    SeekBar seekBar;
    private Handler myHandler = new Handler();

    private int forwardTime = 5000;
    private int backwardTime = 5000;
    String songsJSON;
    int songIndex = -1;
    JSONArray jsonArray;
    JSONObject jsonObject;
    boolean isNextPrev = false;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView  = inflater.inflate(R.layout.fragment_player, container, false);

        jsp = this.getActivity().getSharedPreferences(jsonPreference, Context.MODE_PRIVATE);
        songsJSON = jsp.getString("jArray", "");
        System.out.println(songsJSON);

        sharedpreferences = this.getActivity().getSharedPreferences(MyPreference, Context.MODE_PRIVATE);
        play = rootView.findViewById(R.id.imageButton);
        prev = rootView.findViewById(R.id.imageButton2);
        next = rootView.findViewById(R.id.imageButton3);
        ff = rootView.findViewById(R.id.imageButton4);
        fb = rootView.findViewById(R.id.imageButton5);
        title = rootView.findViewById(R.id.textView);
        artist = rootView.findViewById(R.id.textView2);
        album = rootView.findViewById(R.id.textView8);
        startTimeTV = rootView.findViewById(R.id.textView3);
        endTimeTV = rootView.findViewById(R.id.textView5);
        seekBar = rootView.findViewById(R.id.seekBar);


//        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(30, 30);
////width and height of your Image ,if it is inside Relative change the LinearLayout to RelativeLayout.
//        play.setLayoutParams(layoutParams);

        title.setSelected(true);

        try{
//            uri = this.getArguments().getString("path");
//            name = this.getArguments().getString("name");
//            album = this.getArguments().getString("album");
//            artistName = this.getArguments().getString("artist");
        } catch(Exception e){

        }

        title.setText(name);
        artist.setText(artistName);

        mediaPlayer = new MediaPlayer();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextSong();


            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    int noOfSongs = jsonArray.length();
                    songIndex = songIndex-1;
                    if(songIndex<0){
                        songIndex = noOfSongs;
                    }
                    jsonObject = jsonArray.getJSONObject(songIndex);
                    songPath = jsonObject.getString("jpath");
                    songName = jsonObject.getString("jname");
                    songArtist = jsonObject.getString("jartist");
                    songAlbum = jsonObject.getString("jalbum");
                    isNextPrev = true;
                    playSong();
                } catch (Exception e){
                    System.out.println(e);
                }
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSong();
            }
        });


        ff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int)startTime;

                if((temp+forwardTime)<=finalTime){
                    startTime = startTime + forwardTime;
                    mediaPlayer.seekTo((int) startTime);
                    Toast.makeText(getContext(),"You have Jumped forward 5 seconds",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(),"Cannot jump forward 5 seconds",Toast.LENGTH_SHORT).show();
                }
            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = (int)startTime;

                if((temp-backwardTime)>0){
                    startTime = startTime - backwardTime;
                    mediaPlayer.seekTo((int) startTime);
                    Toast.makeText(getContext(),"You have Jumped backward 5 seconds",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(),"Cannot jump backward 5 seconds",Toast.LENGTH_SHORT).show();
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    int changeBar = seekBar.getProgress();
                    seekBar.setProgress(changeBar);
                    mediaPlayer.seekTo(changeBar);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isFirstVisit){
                isFirstVisit=false;
            }
            else {
//                SharedPreferences.Editor editor = sharedpreferences.edit();
//                songPath = sharedpreferences.getString("songPath", "");
//                songName = sharedpreferences.getString("songName", "");
//                songArtist = sharedpreferences.getString("songArtist", "");
//                songAlbum = sharedpreferences.getString("songAlbum", "");


//                songPath = sharedpreferences.getString("songPath", "");
//                songName = sharedpreferences.getString("songName", "");
//                songArtist = sharedpreferences.getString("songArtist", "");
//                songAlbum = sharedpreferences.getString("songAlbum", "");

                songIndex = jsp.getInt("songIndex", -1);
                System.out.println("cghdvhvdch   "+ songIndex);

                try {
                    jsonArray = new JSONArray(songsJSON);
                    jsonObject = jsonArray.getJSONObject(songIndex);
                    songPath = jsonObject.getString("jpath");
                    songName = jsonObject.getString("jname");
                    songArtist = jsonObject.getString("jartist");
                    songAlbum = jsonObject.getString("jalbum");
                } catch (JSONException e) {
                    e.printStackTrace();
                }




                System.out.println(songPath);
                if(!songPath.equals(oldPath)){
                    isSongUpdated = true;
                    playSong();
                }

            }
        }
        else {
        }
    }

    public void playSong(){
        if(songPath==""){
            Toast.makeText(getContext(), "Please select a song", Toast.LENGTH_SHORT).show();
            TabLayout tabhost = (TabLayout) getActivity().findViewById(R.id.tabLayout);
            tabhost.getTabAt(1).select();
        }
        else{
            if(isSongUpdated||isNextPrev){
                mediaPlayer.reset();
                //mediaPlayer.stop();
                try {
                    mediaPlayer.setDataSource(songPath);

                    updateSongDetails();
                    oldPath = songPath;
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                isSongUpdated = false;
                isNextPrev = false;
                mediaPlayer.start();
                play.setImageResource(R.drawable.pausew);
                //endDuration = mediaPlayer.getDuration();
                finalTime = mediaPlayer.getDuration();
                startTime = mediaPlayer.getCurrentPosition();

                //if (oneTimeOnly == 0) {
                    seekBar.setMax((int) finalTime);
                    //oneTimeOnly = 1;
                //}

                endTimeTV.setText(String.format("%d : %02d",
                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                        finalTime)))
                );

                startTimeTV.setText(String.format("%d : %d",
                        TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                        startTime)))
                );

                seekBar.setProgress((int)startTime);
                myHandler.postDelayed(UpdateSongTime,100);
                //b2.setEnabled(true);
                //b3.setEnabled(false);
            }
            else {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    play.setImageResource(R.drawable.playw);
                } else {
                    mediaPlayer.start();
                    play.setImageResource(R.drawable.pausew);
                }
            }
        }
    }

    public void updateSongDetails(){
        title.setText(songName);
        artist.setText(songArtist);
        album.setText(songAlbum);

//        long minutes = TimeUnit.MILLISECONDS.toMinutes(endDuration);
//        long seconds = TimeUnit.MILLISECONDS.toSeconds(endDuration);
//        //endTimeTV.setText(minutes+":"+seconds);
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            startTimeTV.setText(String.format("%d : %02d",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            seekBar.setProgress((int)startTime);
            myHandler.postDelayed(this, 100);
            //System.out.println(startTime+" "+finalTime);
            if(startTime>=finalTime-100){
                nextSong();
            }

        }
    };

    public void nextSong(){
        try{
            int noOfSongs = jsonArray.length();
            songIndex = songIndex+1;
            if(songIndex>noOfSongs-1){
                songIndex = 0;
            }
            jsonObject = jsonArray.getJSONObject(songIndex);
            songPath = jsonObject.getString("jpath");
            songName = jsonObject.getString("jname");
            songArtist = jsonObject.getString("jartist");
            songAlbum = jsonObject.getString("jalbum");
            isNextPrev = true;
            playSong();
        } catch (Exception e){
            System.out.println(e);
        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        if(!isFirstVisit) {
//            uri = this.getArguments().getString("path");
//            name = this.getArguments().getString("name");
//            album = this.getArguments().getString("album");
//            artistName = this.getArguments().getString("artist");
//            isFirstVisit = false;
//        } else{
//            isFirstVisit = false;
//        }
//    }



}