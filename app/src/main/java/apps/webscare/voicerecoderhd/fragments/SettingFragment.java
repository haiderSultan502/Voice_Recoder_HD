package apps.webscare.voicerecoderhd.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Collections;

import apps.webscare.voicerecoderhd.R;

public class SettingFragment extends Fragment {

    View view;
    Spinner spinnerRecordingFormat,sampleRateSpinner,encoderBitRateSpinner;

    String[] recordingFoormat={"Select Format","mp3","m4a","wav"};
    String[] sampleRate = {"Select Rate","8kHz","16kHz"};
    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.setting_fragment,container,false);

        initialization();
        bindViews();
        setSpinnerData(spinnerRecordingFormat,recordingFoormat);
        setSpinnerData(sampleRateSpinner,sampleRate);
        setSpinnerData(encoderBitRateSpinner,recordingFoormat);
        return view;
    }

    private void initialization() {

        sharedPreferences = getActivity().getSharedPreferences("recordingInfo", Context.MODE_PRIVATE);

    }

    private void setSpinnerData(Spinner spinner,String[] spinnerArray) {

        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(),R.layout.spinner_item,spinnerArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

    }

    private void bindViews() {

        spinnerRecordingFormat = view.findViewById(R.id.recording_format_spinner);
        sampleRateSpinner = view.findViewById(R.id.rate_spinner);
        encoderBitRateSpinner = view.findViewById(R.id.encoder_bit_rate_spinner);

        spinnerRecordingFormat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    String recordingFormat = recordingFoormat[position];

                    switch (recordingFormat)
                    {

                        case "mp3":
                            RecorderFragment.setOutputExt = "mp3";
                            break;
                        case "m4a":
                            RecorderFragment.setOutputExt = "m4a";
                            break;
                        case "wav":
                            RecorderFragment.setOutputExt = "wav";
                            break;

                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sampleRateSpinner.setSelection(2);
        sampleRateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    String sampleRateStr = sampleRate[position];

                    switch (sampleRateStr)
                    {
                        case "8kHz":
                            sharedPreferences.edit().putInt("recordingFormat",8000).commit();
                            break;
                        case "16kHz":
                            sharedPreferences.edit().putInt("recordingFormat",16000).commit();
                            break;
                    }
                }
//            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        encoderBitRateSpinner.setOnItemSelectedListener(this);

    }
}
