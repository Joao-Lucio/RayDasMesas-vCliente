package com.example.raydasmesasvcliente05_04_20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raydasmesasvcliente05_04_20.database.Conexao;
import com.example.raydasmesasvcliente05_04_20.domain.Cliente;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Tela_Perfil extends AppCompatActivity {

    private Toolbar toolbar_main;
    private TextView tvDestaque,tvNome,tvEmail,tvTelefone,tvBairro,tvRua,tvNumero,tvReferencia;

    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela__perfil);

        inicializarComponentes();

        if(!isConnected(getApplicationContext())){
            Toast.makeText(this, "Conecte-se a uma rede com a acesso a internet!", Toast.LENGTH_SHORT).show();
        }

        setCampos();
    }

    public void inicializarComponentes(){
        toolbar_main = (Toolbar) findViewById(R.id.toolbar_main);
        tvDestaque = (TextView) findViewById(R.id.tvDestaque);
        tvNome = (TextView) findViewById(R.id.tvNome);
        tvTelefone = (TextView) findViewById(R.id.tvTelefone);
        tvBairro = (TextView) findViewById(R.id.tvBairro);
        tvRua = (TextView) findViewById(R.id.tvRua);
        tvNumero = (TextView) findViewById(R.id.tvNumero);
        tvReferencia = (TextView) findViewById(R.id.tvReferencia);
        tvEmail = (TextView) findViewById(R.id.tvEmail);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        toolbar_main.setTitle("Perfil");
        toolbar_main.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void setCampos(){
        auth = Conexao.getFirebaseAuth();
        user = auth.getCurrentUser();

        mDatabase.child("cliente").orderByChild("uid").equalTo(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot obj : dataSnapshot.getChildren()){
                    Cliente c = obj.getValue(Cliente.class);
                    tvDestaque.setText(c.getNome());
                    tvNome.setText(c.getNome());
                    tvEmail.setText(c.getEmail());
                    tvTelefone.setText(c.getTelefone());
                    tvBairro.setText(c.getBairro());
                    tvRua.setText(c.getRua());
                    tvNumero.setText(c.getNumero());
                    tvReferencia.setText(c.getReferencia());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void editarDados(View v){

        Intent i = new Intent(Tela_Perfil.this,Tela_Alter_Dados.class);
        startActivity(i);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }

        return true;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if ( cm != null ) {
            NetworkInfo ni = cm.getActiveNetworkInfo();

            return ni != null && ni.isConnected();
        }

        return false;
    }
}
