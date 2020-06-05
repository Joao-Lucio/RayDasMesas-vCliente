package com.example.raydasmesasvcliente05_04_20;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raydasmesasvcliente05_04_20.adapter.PromocaoRecyclerViewAdapter;
import com.example.raydasmesasvcliente05_04_20.adapter.PromocaoViewHolder;
import com.example.raydasmesasvcliente05_04_20.domain.Promocao;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Fragment_Promocao extends Fragment {

    private DatabaseReference mDatabase;
    private RecyclerView rvPromocao;
    private PromocaoRecyclerViewAdapter adapter;
    private TextView tvNenhum;

    public Fragment_Promocao() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment__promocao, container, false);
        tvNenhum = (TextView) root.findViewById(R.id.tvNenhum);

        if(!MainActivity.isConnected(getContext())){
            Toast.makeText(getContext(), "Conecte-se a uma rede com acesso a internet!", Toast.LENGTH_SHORT).show();
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();
        rvPromocao = (RecyclerView) root.findViewById(R.id.rv_promocao);
        populaRecyclerView();
        verificaQuantidade();

        return root;
    }

    public void populaRecyclerView(){
        rvPromocao.setHasFixedSize(true);
        rvPromocao.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PromocaoRecyclerViewAdapter(Promocao.class,R.layout.item_promocao, PromocaoViewHolder.class,mDatabase.child("promocao"));
        rvPromocao.setAdapter(adapter);
    }

    public void verificaQuantidade(){
        mDatabase.child("promocao").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() == 0){
                    tvNenhum.setVisibility(View.VISIBLE);
                }else{
                    tvNenhum.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        adapter.cleanup();
    }

}
