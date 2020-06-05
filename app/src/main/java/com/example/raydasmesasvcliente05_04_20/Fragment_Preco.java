package com.example.raydasmesasvcliente05_04_20;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Fragment_Preco extends Fragment {

    private TextView tvPrecoJogo,tvPrecoMesa,tvPrecoCadeira;
    private DatabaseReference mDatabase;

    public Fragment_Preco() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment__preco, container, false);

        if(!MainActivity.isConnected(getContext())){
            Toast.makeText(getContext(), "Conecte-se a uma rede com acesso a internet!", Toast.LENGTH_SHORT).show();
        }

        tvPrecoJogo = (TextView) root.findViewById(R.id.tvPrecoJogo);
        tvPrecoCadeira = (TextView) root.findViewById(R.id.tvPrecoCadeira);
        tvPrecoMesa = (TextView) root.findViewById(R.id.tvPrecoMesa);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        setaValor();

        return root;
    }

    public void setaValor(){

        mDatabase.child("variados").child("precos").child("precoJogo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                float a = dataSnapshot.getValue(float.class);
                tvPrecoJogo.setText("R$ "+a);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabase.child("variados").child("precos").child("precoMesa").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                float a = dataSnapshot.getValue(float.class);
                tvPrecoMesa.setText("R$ "+a);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabase.child("variados").child("precos").child("precoCadeira").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                float a = dataSnapshot.getValue(float.class);
                tvPrecoCadeira.setText("R$ "+a);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
