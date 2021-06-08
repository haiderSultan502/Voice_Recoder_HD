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

    String[] recordingFoormat={"Recording Format","mp3","m4a","wav"};
    String[] sampleRate = {"Sample Rate","8kHz","16kHz"};
    String[] encodingBitRates = {"Encoding BitRate","48kHz","96kHz","128kHz"};
    SharedPreferences sharedPreferences;
    String sampleRateVal,recordingFormatVal,encodingBitRatevalue;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialization();


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.setting_fragment,container,false);

        bindViews();
        setSpinnerData(spinnerRecordingFormat,recordingFoormat);
        setSpinnerData(sampleRateSpinner,sampleRate);
        setSpinnerData(encoderBitRateSpinner,encodingBitRates);
        viewClickListener();





        return view;
    }

    private void viewClickListener() {

        spinnerRecordingFormat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String recordingFormatStr = recordingFoormat[position];
                if (recordingFormatStr.equals("Recording Format")){
                    Toast.makeText(getActivity(), "Please Select Format", Toast.LENGTH_SHORT).show();
                }else {
                    sharedPreferences.edit().putString("recordingFormat",recordingFormatStr).commit();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sampleRateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sampleRateStr = sampleRate[position];

                switch (sampleRateStr)
                {
                    case "8kHz":
                        sharedPreferences.edit().putInt("sampleRate",8000).commit();
                        break;
                    case "16kHz":
                        sharedPreferences.edit().putInt("sampleRate",16000).commit();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        encoderBitRateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String encodingBitRate = encodingBitRates[position];

                switch (encodingBitRate)
                {
                    case "48kHz":
                        sharedPreferences.edit().putInt("encodingBitRate",48000).commit();
                        break;
                    case "96kHz":
                        sharedPreferences.edit().putInt("encodingBitRate",96000).commit();
                        break;
                    case "128kHz":
                        sharedPreferences.edit().putInt("encodingBitRate",128000).commit();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void initialization() {

        sharedPreferences = getActivity().getSharedPreferences("recordingInfo", Context.MODE_PRIVATE);

    }
    private void setSpinnerData(Spinner spinner,String[] spinnerArray) {

        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(),R.layout.spinner_item,spinnerArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);


        recordingFormatVal = sharedPreferences.getString("recordingFormat","m4a");

        switch (recordingFormatVal){
            case "m4a":
                spinnerRecordingFormat.setSelection(2);
                break;
            case "mp3":
                spinnerRecordingFormat.setSelection(1);
                break;
            case "wav":
                spinnerRecordingFormat.setSelection(3);
                break;
        }



        sampleRateVal = String.valueOf(sharedPreferences.getInt("sampleRate",8000));
        switch (sampleRateVal){
            case "8000":
            sampleRateSpinner.setSelection(1);
            break;
            case "16000":
                sampleRateSpinner.setSelection(2);
                break;
        }


        encodingBitRatevalue = String.valueOf(sharedPreferences.getInt("encodingBitRate",48000));
        switch (encodingBitRatevalue){
            case "48000":
                encoderBitRateSpinner.setSelection(1);
                break;
            case "96000":
                encoderBitRateSpinner.setSelection(2);
                break;
            case "128000":
                encoderBitRateSpinner.setSelection(3);
                break;
        }






    }

    private void bindViews() {

        spinnerRecordingFormat = view.findViewById(R.id.recording_format_spinner);
        sampleRateSpinner = view.findViewById(R.id.rate_spinner);
        encoderBitRateSpinner = view.findViewById(R.id.encoder_bit_rate_spinner);

    }
}
