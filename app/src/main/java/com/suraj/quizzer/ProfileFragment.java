package com.suraj.quizzer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.suraj.quizzer.databinding.FragmentProfileBinding;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentProfileBinding fragmentProfileBinding;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    MainActivity activity;
    String userId;
    UserModel userModel;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        activity = (MainActivity) getActivity();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userId = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        updateUI();
        fragmentProfileBinding = FragmentProfileBinding.inflate(inflater, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        fragmentProfileBinding.logoutAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent resultIntent = new Intent(getActivity(), LoginActivity.class);
                startActivity(resultIntent);
            }
        });

        fragmentProfileBinding.changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBox("Change Password");
            }
        });

        fragmentProfileBinding.changeUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBox("Change User Name");
            }
        });

        return fragmentProfileBinding.getRoot();
    }

    private void dialogBox(String value)
    {
        final String[] newChange = new String[1];
        AlertDialog.Builder changePassDig = new AlertDialog.Builder(activity,R.style.AlertCustomDialog);
        final View customLayout = getLayoutInflater().inflate(R.layout.custom_dialog,null);
        changePassDig.setView(customLayout);
        changePassDig.setTitle("Change Password");
        changePassDig.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText viewById = (EditText) customLayout.findViewById(R.id.updateData);
                newChange[0] = viewById.getText().toString();
                if(Objects.equals(value, "Change Password"))
                {
                    changePassword(newChange[0]);
                }

                if(Objects.equals(value, "Change User Name"))
                {
                    fragmentProfileBinding.userNameProfile.setText(newChange[0]);
                    updateDatabase(newChange[0],"Username Updated Successfully");
                }
            }
        });

        changePassDig.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = changePassDig.create();
        dialog.show();
    }

    private void changePassword(String pass) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        AuthCredential credential = EmailAuthProvider.getCredential(userModel.getEmail(), userModel.getPassword());
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(pass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        fragmentProfileBinding.userPasswordProfile.setText(pass);
                                        updateDatabase(pass,"Password Updated Successfully");
                                    }
                                    else {
                                        Log.d("Error password not updated", "Error password not updated");
                                    }
                                }
                            });

                        } else
                        {
                            Log.d("Error auth failed", "Error auth failed" + task.getException());
                        }
                    }
                });
    }

    private void updateDatabase(String data,String message)
    {
        firebaseFirestore = FirebaseFirestore.getInstance();
        String userName = fragmentProfileBinding.userNameProfile.getText().toString();
        String email = fragmentProfileBinding.userEmailProfile.getText().toString();
        String password = fragmentProfileBinding.userPasswordProfile.getText().toString();
        firebaseFirestore.collection("Users").document(userId).set(new UserModel(userName,email,password)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                updateUI();
                Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(){
        firebaseFirestore.collection("Users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userModel = documentSnapshot.toObject(UserModel.class);
                assert userModel != null;
                fragmentProfileBinding.userNameProfile.setText(userModel.getUserName());
                fragmentProfileBinding.userEmailProfile.setText(userModel.getEmail());
                fragmentProfileBinding.userPasswordProfile.setText(userModel.getPassword());
            }
        });
    }
}


