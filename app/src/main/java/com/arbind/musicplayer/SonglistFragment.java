package com.arbind.musicplayer;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SonglistFragment extends Fragment {

    ArrayList<SongObject> songList;
    CustomListAdapter customListAdapter;
    ListView lv;


    public static final String MyPreference = "MyPrif";
    public static final String jsonPreference = "JPref";
    public static final String songPath = "songPath";
    public static final String songName = "songName";
    public static final String songArtist = "songArtist";
    public static final String songAlbum = "songAlbum";
    public static final String songIndex = "songIndex";
    public static final String jArray = "jArray";

    SharedPreferences sp, jsp;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_songlist, container, false);
        lv = v.findViewById(R.id.list);
        songList = getAudio(getContext());
        setToView();

        //sp = this.getActivity().getSharedPreferences(MyPreference, Context.MODE_PRIVATE);
        jsp = this.getActivity().getSharedPreferences(jsonPreference, Context.MODE_PRIVATE);



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                PlayerFragment playerFragment = new PlayerFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString("path",songList.get(position).getPath());
//                bundle.putString("name",songList.get(position).getName());
//                bundle.putString("artist",songList.get(position).getArtist());
//                bundle.putString("album",songList.get(position).getAlbum());
//                System.out.println(songList.get(position).getPath());
//                playerFragment.setArguments(bundle);


//                PlayerFragment pf = new PlayerFragment();
//                FragmentManager fm = getFragmentManager();
//                FragmentTransaction ft = fm.beginTransaction();
//                Bundle bundle = new Bundle();
//                bundle.putString("path",songList.get(position).getPath());
//                bundle.putString("name",songList.get(position).getName());
//                bundle.putString("artist",songList.get(position).getArtist());
//                bundle.putString("album",songList.get(position).getAlbum());
//                pf.setArguments(bundle);
                //ft.setTransition();
               // ft.commit();

                SharedPreferences.Editor editor = jsp.edit();
//                editor.putString(songPath, songList.get(position).getPath());
//                editor.putString(songName,songList.get(position).getName() );
//                editor.putString(songArtist, songList.get(position).getArtist());
//                editor.putString(songAlbum, songList.get(position).getAlbum());
                editor.putInt(songIndex,position);
                editor.commit();

                //((MainActivity) getActivity()).store(songList.get(position).getPath());

                //View v2 = inflater.inflate(R.layout.activity_main, null, false);
//                ViewPager mViewPager  = v2.findViewById(R.id.viewPager);
//                mViewPager.setCurrentItem(0);
//                getChildFragmentManager().beginTransaction().replace(R.id.viewPager, playerFragment).commit();

//
                TabLayout tabhost = (TabLayout) getActivity().findViewById(R.id.tabLayout);
                tabhost.getTabAt(0).select();



            }
        });
        return v;
    }
    public ArrayList<SongObject> getAudio(Context context){

        ArrayList<SongObject> tempAudioList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns.DATA,MediaStore.Audio.AudioColumns.TITLE,MediaStore.Audio.AudioColumns.ALBUM,MediaStore.Audio.AudioColumns.ARTIST };


        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        Cursor c = context.getContentResolver().query(uri, projection, selection, null, null);


        JSONArray jsonArray = new JSONArray();

        if(c!=null){
            while(c.moveToNext()){
                JSONObject jsonObject = new JSONObject();
                String path = c.getString(0);
                String name = c.getString(1);
                String album = c.getString(2);
                String artist = c.getString(3);
                try{
                    jsonObject.put("jpath", path);
                    jsonObject.put("jname", name);
                    jsonObject.put("jalbum", album);
                    jsonObject.put("jartist", artist);
                    jsonArray.put(jsonObject);
                } catch (Exception e){

                }

                SongObject song = new SongObject(path, name, album, artist);
                System.out.println(path+" "+name+" "+album+" "+ artist);
                tempAudioList.add(song);
            }
            c.close();
        }

        jsp = this.getActivity().getSharedPreferences(jsonPreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = jsp.edit();
        editor2.putString(jArray, jsonArray.toString());
        editor2.commit();
        return tempAudioList;

    }
    public void setToView(){
        customListAdapter = new CustomListAdapter(getContext(), R.layout.song_view, songList);
        lv.setAdapter(customListAdapter);

    }
}