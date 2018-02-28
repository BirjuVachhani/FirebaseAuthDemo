package com.birjuvachhani.firebaseauthdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class DeleteUserFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.btn_yes)
    Button myes;

    @BindView(R.id.btn_no)
    Button mno;

    @BindView(R.id.progress_bar)
    ProgressBar mprogressBar;

    FirebaseAuth mauth;

    DeleteEventListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.delete_user_fragment, container, false);
    }

    @Override
    public void onAttach(Context context) {
        this.listener = (DeleteEventListener) context;
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        myes.setOnClickListener(this);
        mno.setOnClickListener(this);
        mauth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes: {
                mprogressBar.setVisibility(View.VISIBLE);
                deleteUser();
                break;
            }
            case R.id.btn_no: {
                listener.onCancel();
                break;
            }
        }
    }

    private void deleteUser() {
        FirebaseUser user = mauth.getCurrentUser();
        if (user != null) {
            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        showMessage("unable to delete user, try again!");
                        return;
                    }
                    showMessage("User deleted.");
                    listener.onDelete();
                }
            });
        }
    }

    private void showMessage(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    interface DeleteEventListener {
        void onDelete();
        void onCancel();
    }
}
