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

import com.example.raydasmesasvcliente05_04_20.adapter.AgendamentoViewHolder;
import com.example.raydasmesasvcliente05_04_20.adapter.EntregueRecyclerViewAdapter;
import com.example.raydasmesasvcliente05_04_20.database.Conexao;
import com.example.raydasmesasvcliente05_04_20.domain.Agendamento;
import com.example.raydasmesasvcliente05_04_20.domain.Cliente;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Fragment_Entregue extends Fragment {

    private RecyclerView rvEntregue;
    private EntregueRecyclerViewAdapter adapter;
    private DatabaseReference mDatabase;
    private TextView tvNenhum;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private String telefone;

    public Fragment_Entregue() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment__entregue, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        rvEntregue = (RecyclerView) root.findViewById(R.id.rv_entregue);
        tvNenhum = (TextView) root.findViewById(R.id.tvNenhum);

        if(!MainActivity.isConnected(getContext())){
            Toast.makeText(getContext(), "Conecte-se a uma rede com Internet!", Toast.LENGTH_LONG).show();
        }

        populaRecyclerView();
        verificaQuantidade();

        return root;
    }

    public void populaRecyclerView(){
        auth = Conexao.getFirebaseAuth();
        user = auth.getCurrentUser();

        rvEntregue.setHasFixedSize(true);
        rvEntregue.setLayoutManager(new LinearLayoutManager(getContext()));

        mDatabase.child("cliente").orderByChild("uid").equalTo(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot obj : dataSnapshot.getChildren()){
                    Cliente c = obj.getValue(Cliente.class);
                    telefone = c.getTelefone();
                }

                adapter = new EntregueRecyclerViewAdapter(Agendamento.class,R.layout.item_agendamento, AgendamentoViewHolder.class,mDatabase.child("entregue").orderByChild("telefone_responsavel").equalTo(telefone));
                rvEntregue.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void verificaQuantidade(){

        auth = Conexao.getFirebaseAuth();
        user = auth.getCurrentUser();
        mDatabase.child("cliente").orderByChild("uid").equalTo(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String telefone = "";
                for(DataSnapshot obj : dataSnapshot.getChildren()){
                    Cliente c = obj.getValue(Cliente.class);
                    telefone = c.getTelefone();
                }

                mDatabase.child("entregue").orderByChild("telefone_responsavel").equalTo(telefone).addValueEventListener(new ValueEventListener() {
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
