package com.suraj.quizzer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.suraj.quizzer.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentHomeBinding fragmentHomeBinding;
    FirebaseFirestore firebaseFirestore;
    StorageReference firebaseStorage;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater,container,false);

        ArrayList<DomainModel> categories = new ArrayList<>();
        androidx.recyclerview.widget.RecyclerView recyclerView = fragmentHomeBinding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        firebaseFirestore = FirebaseFirestore.getInstance();
        DomainAdapter domainAdapter = new DomainAdapter(categories, getContext());
        recyclerView.setAdapter(domainAdapter);

        firebaseFirestore.collection("Categories").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                categories.clear();
                assert value != null;
                for(DocumentSnapshot snapshot: value.getDocuments())
                {
                    DomainModel model = snapshot.toObject(DomainModel.class);
                    assert model != null;
                    model.setCategoryId(snapshot.getId());
                    categories.add(model);
                }
                domainAdapter.notifyDataSetChanged();
            }
        });

        return fragmentHomeBinding.getRoot();
    }
}