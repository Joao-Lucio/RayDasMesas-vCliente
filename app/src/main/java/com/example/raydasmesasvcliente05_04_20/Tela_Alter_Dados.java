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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raydasmesasvcliente05_04_20.database.Conexao;
import com.example.raydasmesasvcliente05_04_20.domain.Cliente;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Tela_Alter_Dados extends AppCompatActivity {

    private Toolbar toolbar_main;
    private TextInputEditText edNome,edTelefone,edBairro,edRua,edNumero,edReferencia;
    private TextView tvMsgNome,tvMsgTelefone;
    private DatabaseReference mDatabase;
    private String[] numeros = new String[]{"0","1","2","3","4","5","6","7","8","9"};
    private LinearLayout ll_altera,ll_confirma;
    private String nome,telefone,bairro,rua,numero,referencia;
    private int verTelefone = 0;

    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela__alter__dados);

        inicializarComponentes();

        auth = Conexao.getFirebaseAuth();
        user = auth.getCurrentUser();

        if (!isConnected(getApplicationContext())){
            Toast.makeText(this, "Conecte-se a uma rede com acesso a internet!", Toast.LENGTH_SHORT).show();
        }

        setCampos();

    }

    public void inicializarComponentes() {
        toolbar_main = (Toolbar) findViewById(R.id.toolbar_main);
        edNome = (TextInputEditText) findViewById(R.id.textInput_edNome);
        edTelefone = (TextInputEditText) findViewById(R.id.textInput_edTelefone);
        edBairro = (TextInputEditText) findViewById(R.id.textInput_edBairro);
        edRua = (TextInputEditText) findViewById(R.id.textInput_edRua);
        edNumero = (TextInputEditText) findViewById(R.id.textInput_edNumero);
        edReferencia = (TextInputEditText) findViewById(R.id.textInput_edReferencia);
        tvMsgNome = (TextView) findViewById(R.id.tv_mensagemNome);
        tvMsgTelefone = (TextView) findViewById(R.id.tv_mensagemTelefone);
        ll_altera = (LinearLayout) findViewById(R.id.ll_altera);
        ll_confirma = (LinearLayout) findViewById(R.id.ll_confirmacao);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        toolbar_main.setTitle("Atualizar Dados");
        toolbar_main.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SimpleMaskFormatter smf = new SimpleMaskFormatter("(NN) NNNNN-NNNN");
        MaskTextWatcher mtw = new MaskTextWatcher(edTelefone,smf);
        edTelefone.addTextChangedListener(mtw);
    }

    public void setCampos(){
        mDatabase.child("cliente").orderByChild("uid").equalTo(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot obj : dataSnapshot.getChildren()){
                    Cliente c = obj.getValue(Cliente.class);
                    edNome.setText(c.getNome());
                    edTelefone.setText(c.getTelefone());
                    edBairro.setText(c.getBairro());
                    edRua.setText(c.getRua());
                    edNumero.setText(c.getNumero());
                    edReferencia.setText(c.getReferencia());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void atualizar(View v) {

        if(isConnected(getApplicationContext())){
            verTelefone = 0;
            tvMsgTelefone.setText("");
            tvMsgNome.setText("");

            nome = edNome.getText().toString().trim();
            telefone = edTelefone.getText().toString().trim();
            bairro = edBairro.getText().toString().trim();
            rua = edRua.getText().toString().trim();
            numero = edNumero.getText().toString().trim();
            referencia = edReferencia.getText().toString().trim();

            if(edNome.getText().toString().equals("") || edTelefone.getText().toString().equals("") || edBairro.getText().toString().equals("") || edRua.getText().toString().equals("") || edNumero.getText().toString().equals("") || edReferencia.getText().toString().equals("")){
                Toast.makeText(this, "Por favor, preencha todos os campos!", Toast.LENGTH_LONG).show();
                return;
            }

            for(int i = 0; i < numeros.length; i++){
                if(nome.contains(numeros[i])){
                    edNome.requestFocus();
                    tvMsgNome.setText("Nome Inválido!");
                    return;
                }
            }

            if(nome.length() < 4){
                edNome.requestFocus();
                tvMsgNome.setText("Nome Inválido!");
                return;
            }

            if(telefone.length() < 15){
                edTelefone.requestFocus();
                tvMsgTelefone.setText("Digite número corretamente, por exemplo: (DD) 90000-0000");
                return;
            }

            verificaTelefone();

            ll_confirma.setVisibility(View.VISIBLE);
            ll_altera.setVisibility(View.GONE);

        }


    }

    public void cancelar(View v){
        finish();
    }

    public void voltar(View v){
        ll_altera.setVisibility(View.VISIBLE);
        ll_confirma.setVisibility(View.GONE);
    }

    public void confirmar(View v){

        auth = Conexao.getFirebaseAuth();
        user = auth.getCurrentUser();

        Cliente c = new Cliente();
        c.setNome(nome);
        c.setTelefone(telefone);
        c.setBairro(bairro);
        c.setRua(rua);
        c.setNumero(numero);
        c.setReferencia(referencia);
        c.setEmail(user.getEmail());
        c.setUid(user.getUid());

        try {
            mDatabase.child("cliente").child(c.getUid()).setValue(c);
            Toast.makeText(this, "Dados alterados com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        }catch (Exception e){
            Toast.makeText(this, "Erro ao alterar dados!", Toast.LENGTH_SHORT).show();
        }

    }

    public void verificaTelefone(){
        auth = Conexao.getFirebaseAuth();
        user = auth.getCurrentUser();
        final String email = user.getEmail();
        mDatabase.child("cliente").orderByChild("telefone").equalTo(telefone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot obj : dataSnapshot.getChildren()){
                    Cliente c = obj.getValue(Cliente.class);
                    if(!c.getEmail().equals(email)){
                        verTelefone = 1;
                    }
                }
                if(verTelefone > 0){
                    edTelefone.requestFocus();
                    tvMsgTelefone.setText("Telefone já está sendo utilizado, por favor digite outro!");
                    ll_altera.setVisibility(View.VISIBLE);
                    ll_confirma.setVisibility(View.GONE);
                    return;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
