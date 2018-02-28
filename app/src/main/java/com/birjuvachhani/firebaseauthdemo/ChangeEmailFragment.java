package com.birjuvachhani.firebaseauthdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by birju.vachhani on 28/02/18.
 */

public class ChangeEmailFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.btn_change)
    Button mchangeEmail;

    @BindView(R.id.et_email)
    EditText memail;

    @BindView(R.id.progress_bar)
    ProgressBar mprogressBar;

    FirebaseAuth mauth;
    EmailChangeListener listener;

    @Override
    public void onAttach(Context context) {
        this.listener=(EmailChangeListener)context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.change_email_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        mchangeEmail.setOnClickListener(this);
        mauth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        String email = memail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            showMessage("Please enter email to change!");
            return;
        }
        mprogressBar.setVisibility(View.VISIBLE);
        FirebaseUser user = mauth.getCurrentUser();
        if (user != null) {
            user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    mprogressBar.setVisibility(View.INVISIBLE);
                    if (!task.isSuccessful()) {
                        showMessage("Unable to change email, try again!");
                        return;
                    }
                    showMessage("Email Successfully changed.");
                    listener.onEmailChange();
                }
            });
        }
    }

    private void showMessage(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    interface EmailChangeListener
    {
        void onEmailChange();
    }
}
