package trach.yoni.olympiangods.fragments;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import trach.yoni.olympiangods.MainActivity;
import trach.yoni.olympiangods.R;
import trach.yoni.olympiangods.aux.Methods;

public class SettingsFragment extends Fragment {

    public static final ArrayList<String> allSettingOptions = new ArrayList<String>();

    private final MainActivity myָActivity;

    public SettingsFragment(MainActivity theActivity) {
        super(R.layout.fragment_fight);
        myָActivity = theActivity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);




        //Build up all the settings that the user can see and then edit
        allSettingOptions.add("Music Volume: ");
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.navigation_menu, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_settings, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        EditText musicVolume = new EditText(getContext());
        for(int i = 0; i < allSettingOptions.size(); i++){
            addSettingOption(allSettingOptions.get(i));
            addUserOption("hello");
        }
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuGods:
                myָActivity.navigateFragment(new UserGodsFragment(myָActivity), "User Gods");
                break;
            case R.id.menuAboutGame:
                Methods.makeAlert(getContext(), "About Game Coming Soon...", "About game coming soon...");
                break;
            case R.id.menuStats:
                Methods.makeAlert(getContext(), "Stats Coming Soon...", "Stats coming soon...");
                break;
            case R.id.menuShop:
                myָActivity.navigateFragment(new ShopFragment(myָActivity), "Shop");
                break;
            case R.id.menuHome:
                myָActivity.navigateFragment(new HomeScreenFragment(myָActivity), "Settings");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addSettingOption(String newOption){
        LinearLayout layout = getActivity().findViewById(R.id.SettingOptions);
        TextView option = new TextView(getContext());
        option.setText(newOption + "\n");
        layout.addView(option);
    }

    public void addUserOption(String newUserOption){
        SeekBar musicVolumeSeekBar = new SeekBar(getContext());
        musicVolumeSeekBar.setProgress(MainActivity.audioManager.getStreamVolume(AudioManager.STREAM_ALARM));
        final TextView musicVolumeValue = (TextView) getActivity().findViewById(R.id.UserSettingSelections);
        musicVolumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                musicVolumeValue.setText(String.valueOf(progress) + "%");
                MainActivity.audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                        progress, AudioManager.FLAG_PLAY_SOUND
                );
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        LinearLayout layout = getActivity().findViewById(R.id.UserSettingLayout);
        layout.removeAllViews();
        layout.addView(musicVolumeSeekBar);
    }
}
