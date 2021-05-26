package apps.webscare.voicerecoderhd.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import apps.webscare.voicerecoderhd.R;

public class SettingFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    View view;
    Spinner spinnerRecordingFormat,rateSpinner,encoderBitRateSpinner;

    String[] recordingFoormat={"BAC (m4a) - Good quality","BBC (MP3) - Good quality"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.setting_fragment,container,false);

        bindViews();
        setSpinnerData(spinnerRecordingFormat,recordingFoormat);
        setSpinnerData(rateSpinner,recordingFoormat);
        setSpinnerData(encoderBitRateSpinner,recordingFoormat);
        return view;
    }

    private void setSpinnerData(Spinner spinner,String[] spinnerArray) {

        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(),R.layout.spinner_item,spinnerArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

    }

    private void bindViews() {

        spinnerRecordingFormat = view.findViewById(R.id.recording_format_spinner);
        rateSpinner = view.findViewById(R.id.rate_spinner);
        encoderBitRateSpinner = view.findViewById(R.id.encoder_bit_rate_spinner);
        spinnerRecordingFormat.setOnItemSelectedListener(this);
        spinnerRecordingFormat.setOnItemSelectedListener(this);
        encoderBitRateSpinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
