package com.example.freesbet;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BottomSheetDialogFragmentApuestaLiga extends BottomSheetDialogFragment {

    static BottomSheetDialogFragment newInstance(){
        return new BottomSheetDialogFragmentApuestaLiga();
    }

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_apuesta_liga, container, false);
        return v;
    }
}
