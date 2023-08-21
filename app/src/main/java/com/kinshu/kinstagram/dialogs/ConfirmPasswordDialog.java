package com.kinshu.kinstagram.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.kinshu.kinstagram.R;

public class ConfirmPasswordDialog extends DialogFragment {

    TextView mPassword;
    public interface OnConfirmPasswordListener{
        public void onConfirmPassword(String password);
    }
    OnConfirmPasswordListener onConfirmPasswordListener;
    private static final String TAG = "ConfirmPasswordDialog";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_confirm_password, container, false);
        Log.d(TAG, "onCreateView started");
        mPassword = view.findViewById(R.id.confirm_password);

        TextView confirmDialog = view.findViewById(R.id.dialogConfirm);
        confirmDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Clicled");

                String password = mPassword.getText().toString();
                if (!password.equals("")) {
                    onConfirmPasswordListener.onConfirmPassword(password);
                    getDialog().dismiss();
                }else {
                    Toast.makeText(getActivity(), "You must enter a password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView cancelDialog = view.findViewById(R.id.dialogCancel);
        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            onConfirmPasswordListener =(OnConfirmPasswordListener) getTargetFragment();

        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: "+ e.getMessage());
        }
    }
}
