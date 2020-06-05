package com.example.raydasmesasvcliente05_04_20;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raydasmesasvcliente05_04_20.adapter.AgendamentoRecyclerViewAdapter;
import com.example.raydasmesasvcliente05_04_20.adapter.AgendamentoViewHolder;
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

public class Fragment_Agendamento extends Fragment {

    private ImageView iv_add_Agendamento;
    private Intent it = new Intent();
    private LinearLayout ll_JogoCompleto,ll_SomenteMesa,ll_SomenteCadeira;
    private CardView cardOptions;
    private RecyclerView rv_agendamento;
    private DatabaseReference mDatabase;
    private AgendamentoRecyclerViewAdapter adapter;
    private TextView tvNenhum;

    private FirebaseAuth auth;
    private FirebaseUser user;

    public Fragment_Agendamento() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment__agendamento, container, false);
        tvNenhum = (TextView) root.findViewById(R.id.tvNenhum);

        if(!MainActivity.isConnected(getContext())){
            Toast.makeText(getContext(), "Conecte-se a uma rede com a acesso a internet!", Toast.LENGTH_SHORT).show();
        }

        iniComponentes(root);
        acoes();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        populaRecyclerView();
        verificaQuantidade();

        return root;
    }

    public void iniComponentes(View root){

        cardOptions = (CardView) root.findViewById(R.id.card_opcoes);
        ll_JogoCompleto = (LinearLayout) root.findViewById(R.id.ll_JogoCompleto);
        ll_SomenteMesa = (LinearLayout) root.findViewById(R.id.ll_SomenteMesa);
        ll_SomenteCadeira = (LinearLayout) root.findViewById(R.id.ll_SomenteCadeira);
        rv_agendamento = (RecyclerView) root.findViewById(R.id.rv_agendamento);
        iv_add_Agendamento = (ImageView) root.findViewById(R.id.iv_add_Agendamento);

    }

    public void acoes(){

        iv_add_Agendamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cardOptions.getVisibility() == View.GONE){
                    cardOptions.setVisibility(View.VISIBLE);
                }else{
                    cardOptions.setVisibility(View.GONE);
                }

            }
        });

        ll_JogoCompleto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chamaActivity("jogo");
                cardOptions.setVisibility(View.GONE);
            }
        });

        ll_SomenteMesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chamaActivity("mesa");
                cardOptions.setVisibility(View.GONE);
            }
        });

        ll_SomenteCadeira.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chamaActivity("cadeira");
                cardOptions.setVisibility(View.GONE);
            }
        });

    }

    public void chamaActivity (String tipo){
        it.setClass(getContext(),Activity_Add_Alter_Agendamento.class);
        it.putExtra("tipo",tipo);
        it.putExtra("editado","nao");
        startActivity(it);
    }

    public void populaRecyclerView(){
        auth = Conexao.getFirebaseAuth();
        user = auth.getCurrentUser();
        rv_agendamento.setHasFixedSize(true);
        rv_agendamento.setLayoutManager(new LinearLayoutManager(getContext()));
        mDatabase.child("cliente").orderByChild("uid").equalTo(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String telefone = "";
                for(DataSnapshot obj : dataSnapshot.getChildren()){
                    Cliente c = obj.getValue(Cliente.class);
                    telefone = c.getTelefone();
                }

                adapter = new AgendamentoRecyclerViewAdapter(Agendamento.class,R.layout.item_agendamento, AgendamentoViewHolder.class,mDatabase.child("agendamento").orderByChild("telefone_responsavel").equalTo(telefone));
                rv_agendamento.setAdapter(adapter);

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

                mDatabase.child("agendamento").orderByChild("telefone_responsavel").equalTo(telefone).addValueEventListener(new ValueEventListener() {
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