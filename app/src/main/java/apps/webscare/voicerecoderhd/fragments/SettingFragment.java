package apps.webscare.voicerecoderhd.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.Collections;

import apps.webscare.voicerecoderhd.MainActivity;
import apps.webscare.voicerecoderhd.R;

public class SettingFragment extends Fragment {

    View view;
    Spinner spinnerRecordingFormat,sampleRateSpinner,encoderBitRateSpinner;

//    String[] recordingFoormat={"Recording Format","mp3","m4a","wav"};
//    String[] sampleRate = {"Sample Rate","8kHz","16kHz"};
//    String[] encodingBitRates = {"Encoding BitRate","48kHz","96kHz","128kHz"};
    SharedPreferences sharedPreferences;
    String sampleRateVal,recordingFormatVal,encodingBitRatevalue;
    LinearLayout recordingFormat,sampleRate,encodingBitRate;

    RadioGroup radioGroupRecordingFormat,radioGroupSampeRate,radioGroupEncodingBitRate;

    Dialog dialogRecordingFormat,dialogSampleRate,dialogEncodingBitRate;

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

        setRecordingDetails();
//        setSpinnerData(spinnerRecordingFormat,recordingFoormat);
//        setSpinnerData(sampleRateSpinner,sampleRate);
//        setSpinnerData(encoderBitRateSpinner,encodingBitRates);

        viewClickListener();





        return view;
    }

    private void setRecordingDetails() {

        sharedPreferences = getActivity().getSharedPreferences("recordingInfo", Context.MODE_PRIVATE);
        int formatId  = sharedPreferences.getInt("radioBtnId",R.id.format_mp4);
        RadioButton radioButtonRecordingFormat = dialogRecordingFormat.findViewById(formatId);
        radioButtonRecordingFormat.setChecked(true);

        int sampleId  = sharedPreferences.getInt("radioBtnSampleRateId",R.id.sample_rate_8kHz);
        RadioButton radioButtonSampleRate = dialogSampleRate.findViewById(sampleId);
        radioButtonSampleRate.setChecked(true);

        int encodingId  = sharedPreferences.getInt("radioBtnEncodingBitRateId",R.id.enoding_bitrate_48kHz);
        RadioButton radioButtonEncodingBitRate = dialogEncodingBitRate.findViewById(encodingId);
        radioButtonEncodingBitRate.setChecked(true);
    }

    private void viewClickListener() {


        recordingFormat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogRecordingFormat.show();

                radioGroupRecordingFormat.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId){
                            case R.id.format_mp4:
                                sharedPreferences.edit().putString("recordingFormat","m4a").commit();
                                sharedPreferences.edit().putInt("radioBtnId",R.id.format_mp4).commit();
                                break;
                            case R.id.format_mp3:
                                sharedPreferences.edit().putString("recordingFormat","mp3").commit();
                                sharedPreferences.edit().putInt("radioBtnId",R.id.format_mp3).commit();
                                break;
                            case R.id.format_wav:
                                sharedPreferences.edit().putString("recordingFormat","wav").commit();
                                sharedPreferences.edit().putInt("radioBtnId",R.id.format_wav).commit();
                                break;
                        }
                        dialogRecordingFormat.dismiss();
                    }
                });

            }
        });

        sampleRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogSampleRate.show();

                radioGroupSampeRate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId){
                            case R.id.sample_rate_8kHz:
                                sharedPreferences.edit().putInt("sampleRate",8000).commit();
                                sharedPreferences.edit().putInt("radioBtnSampleRateId",R.id.sample_rate_8kHz).commit();
                                break;
                            case R.id.sample_rate_16kHz:
                                sharedPreferences.edit().putInt("sampleRate",16000).commit();
                                sharedPreferences.edit().putInt("radioBtnSampleRateId",R.id.sample_rate_16kHz).commit();
                                break;
                        }
                        dialogSampleRate.dismiss();
                    }
                });
            }
        });

        encodingBitRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEncodingBitRate.show();

                radioGroupEncodingBitRate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId){
                            case R.id.enoding_bitrate_48kHz:
                                sharedPreferences.edit().putInt("encodingBitRate",48000).commit();
                                sharedPreferences.edit().putInt("radioBtnEncodingBitRateId",R.id.enoding_bitrate_48kHz).commit();
                                break;
                            case R.id.enoding_bitrate_96kHz:
                                sharedPreferences.edit().putInt("encodingBitRate",96000).commit();
                                sharedPreferences.edit().putInt("radioBtnEncodingBitRateId",R.id.enoding_bitrate_96kHz).commit();
                                break;
                            case R.id.enoding_bitrate_128kHz:
                                sharedPreferences.edit().putInt("encodingBitRate",128000).commit();
                                sharedPreferences.edit().putInt("radioBtnEncodingBitRateId",R.id.enoding_bitrate_128kHz).commit();
                                break;
                        }
                        dialogEncodingBitRate.dismiss();
                    }
                });
            }
        });



//        spinnerRecordingFormat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String recordingFormatStr = recordingFoormat[position];
//                if (recordingFormatStr.equals("Recording Format")){
//                    Toast.makeText(getActivity(), "Please Select Format", Toast.LENGTH_SHORT).show();
//                }else {
//                    sharedPreferences.edit().putString("recordingFormat",recordingFormatStr).commit();
//                }
//
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


//        sampleRateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String sampleRateStr = sampleRate[position];
//
//                switch (sampleRateStr)
//                {
//                    case "8kHz":
//                        sharedPreferences.edit().putInt("sampleRate",8000).commit();
//                        break;
//                    case "16kHz":
//                        sharedPreferences.edit().putInt("sampleRate",16000).commit();
//                        break;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        encoderBitRateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String encodingBitRate = encodingBitRates[position];
//
//                switch (encodingBitRate)
//                {
//                    case "48kHz":
//                        sharedPreferences.edit().putInt("encodingBitRate",48000).commit();
//                        break;
//                    case "96kHz":
//                        sharedPreferences.edit().putInt("encodingBitRate",96000).commit();
//                        break;
//                    case "128kHz":
//                        sharedPreferences.edit().putInt("encodingBitRate",128000).commit();
//                        break;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

    }

//    private void radioBtnClickListener(RadioGroup radioGroup) {
//
//            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(RadioGroup group, int checkedId) {
//
//                    switch (checkedId)
//                    {
//                        case R.id.format_mp4:
//                            sharedPreferences.edit().putString("recordingFormat","mp4").commit();
//                            alertDialog.setCancelable(true);
//                            break;
//                        case R.id.format_mp3:
//                            sharedPreferences.edit().putString("recordingFormat","mp3").commit();
//                            alertDialog.setCancelable(false);
//                            break;
//                        case R.id.format_wav:
//                            sharedPreferences.edit().putString("recordingFormat","wav").commit();
//                            break;
//
//
//                    }
//                }
//            });
//    }

    private void initialization() {

        sharedPreferences = getActivity().getSharedPreferences("recordingInfo", Context.MODE_PRIVATE);

    }
    private void setSpinnerData(Spinner spinner,String[] spinnerArray) {

//        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(),R.layout.spinner_item,spinnerArray);
//        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(arrayAdapter);


        recordingFormatVal = sharedPreferences.getString("recordingFormat","m4a");

//        switch (recordingFormatVal){
//            case "m4a":
//                spinnerRecordingFormat.setSelection(2);
//                break;
//            case "mp3":
//                spinnerRecordingFormat.setSelection(1);
//                break;
//            case "wav":
//                spinnerRecordingFormat.setSelection(3);
//                break;
//        }



//        sampleRateVal = String.valueOf(sharedPreferences.getInt("sampleRate",8000));
//        switch (sampleRateVal){
//            case "8000":
//            sampleRateSpinner.setSelection(1);
//            break;
//            case "16000":
//                sampleRateSpinner.setSelection(2);
//                break;
//        }
//
//
//        encodingBitRatevalue = String.valueOf(sharedPreferences.getInt("encodingBitRate",48000));
//        switch (encodingBitRatevalue){
//            case "48000":
//                encoderBitRateSpinner.setSelection(1);
//                break;
//            case "96000":
//                encoderBitRateSpinner.setSelection(2);
//                break;
//            case "128000":
//                encoderBitRateSpinner.setSelection(3);
//                break;
//        }






    }

    private void bindViews() {

//        spinnerRecordingFormat = view.findViewById(R.id.recording_format_spinner);
//        sampleRateSpinner = view.findViewById(R.id.rate_spinner);
//        encoderBitRateSpinner = view.findViewById(R.id.encoder_bit_rate_spinner);

        recordingFormat = view.findViewById(R.id.recording_format_click_listener);
        sampleRate = view.findViewById(R.id.recording_sample_rate_click_listener);
        encodingBitRate = view.findViewById(R.id.recording_encoding_bitrate_click_listnenr);


        dialogRecordingFormat = new Dialog(getActivity());
        dialogRecordingFormat.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogRecordingFormat.setContentView(R.layout.custom_recording_format_list);
        dialogRecordingFormat.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT); //set the dialog dimension
        radioGroupRecordingFormat = dialogRecordingFormat.findViewById(R.id.recording_format_radio_group) ;

        dialogSampleRate = new Dialog(getActivity());
        dialogSampleRate.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSampleRate.setContentView(R.layout.custom_recording_sample_rate_list);
        dialogSampleRate.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT); //set the dialog dimension
        radioGroupSampeRate = dialogSampleRate.findViewById(R.id.recording_format_sample_rate_group);

        dialogEncodingBitRate = new Dialog(getActivity());
        dialogEncodingBitRate.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogEncodingBitRate.setContentView(R.layout.custom_encoding_bitrate_list);
        dialogEncodingBitRate.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT); //set the dialog dimension
        radioGroupEncodingBitRate = dialogEncodingBitRate.findViewById(R.id.recording_encoding_bitrate_radio_group) ;









    }

}
