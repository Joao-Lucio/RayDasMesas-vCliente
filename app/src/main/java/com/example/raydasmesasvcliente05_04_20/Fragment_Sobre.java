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

public class Fragment_Sobre extends Fragment {

    private TextView tvBairro,tvRua,tvNumero,tvReferencia,tvContato;
    private DatabaseReference mDatabase;

    public Fragment_Sobre() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment__sobre, container, false);

        if(!MainActivity.isConnected(getContext())){
            Toast.makeText(getContext(), "Conecte-se a uma rede com a acesso a internet!", Toast.LENGTH_SHORT).show();
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

        tvBairro = (TextView) root.findViewById(R.id.tvBairro);
        tvRua = (TextView) root.findViewById(R.id.tvRua);
        tvNumero = (TextView) root.findViewById(R.id.tvNumero);
        tvReferencia = (TextView) root.findViewById(R.id.tvReferencia);
        tvContato = (TextView) root.findViewById(R.id.tvContato);

        setaDados();

        return root;
    }

    public void setaDados(){
        mDatabase.child("variados").child("endereco").child("bairro").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String a = dataSnapshot.getValue(String.class);
                tvBairro.setText(a);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabase.child("variados").child("endereco").child("rua").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String a = dataSnapshot.getValue(String.class);
                tvRua.setText(a);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabase.child("variados").child("endereco").child("numero").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int a = dataSnapshot.getValue(Integer.class);
                tvNumero.setText(String.valueOf(a));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabase.child("variados").child("endereco").child("referencia").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String a = dataSnapshot.getValue(String.class);
                tvReferencia.setText(a);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabase.child("variados").child("endereco").child("contato").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String a = dataSnapshot.getValue(String.class);
                tvContato.setText(a);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
