package com.example.grabtutor.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.grabtutor.Activity.Credentials;
import com.example.grabtutor.Activity.LoginActivity;
import com.example.grabtutor.Activity.MainActivity;
import com.example.grabtutor.Activity.Payment;
import com.example.grabtutor.Activity.Rewards;
import com.example.grabtutor.Activity.UserProfileActivity;
import com.example.grabtutor.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements AdapterView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseAuth mAuth;
    private ListView listView;
    private TextView tv_username;
    private DatabaseReference ref;
    private FirebaseUser firebaseUser;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ref = FirebaseDatabase.getInstance().getReference("Users");
        tv_username = view.findViewById(R.id.Textview_profile_username);

        mAuth = FirebaseAuth.getInstance();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    tv_username.setText(snapshot.child(mAuth.getCurrentUser().getUid()).child("username").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] management = {"User Profile", "Credentials", "Payment", "Rewards", "Logout"};

        listView = view.findViewById(R.id.ListView_profile);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, management);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        mAuth = FirebaseAuth.getInstance();

        if (position == 0) {
            Intent intent = new Intent(getActivity(), UserProfileActivity.class);
            startActivity(intent);
            finishActivity();
        }
        if (position == 1) {
            Intent intent = new Intent(getActivity(), Credentials.class);
            startActivity(intent);
            finishActivity();
        }
        if (position == 2) {
            Intent intent = new Intent(getActivity(), Payment.class);
            startActivity(intent);
            finishActivity();
        } if (position == 3) {
            Intent intent = new Intent(getActivity(), Rewards.class);
            startActivity(intent);
            finishActivity();
        }
        // Logout
        if (position == 4) {
            new AlertDialog.Builder(getActivity())
                    .setCancelable(true)
                    .setMessage("Confirm Logout?")
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            status("offline");
                            mAuth.signOut();;
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                            Toast.makeText(getActivity(), "Logged out successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .create()
                    .show();
        }
    }

    private void finishActivity() {
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    private void status(String status) {
        firebaseUser = mAuth.getCurrentUser();
        DatabaseReference reference;
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);

    }

}