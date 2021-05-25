package apps.webscare.voicerecoderhd.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import apps.webscare.voicerecoderhd.R;
import apps.webscare.voicerecoderhd.adapters.RecordingItemAdapter;

public class TrackFragment extends Fragment {

    View view;
    RecyclerView rvRecordings;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view =inflater.inflate(R.layout.track_fragment,container,false);

        viewBindings();
        setDataInRecyclerView();

        return view;
    }

    private void setDataInRecyclerView() {
        RecordingItemAdapter recordingItemAdapter = new RecordingItemAdapter(getActivity());
        rvRecordings.setAdapter(recordingItemAdapter);
    }

    private void viewBindings() {
        rvRecordings = view.findViewById(R.id.rv_recordings);
        setRecyclerView();
    }

    private void setRecyclerView() {
        // set a LinearLayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // set Horizontal Orientation
        rvRecordings.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView
    }
}
