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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raydasmesasvcliente05_04_20.database.Conexao;
import com.example.raydasmesasvcliente05_04_20.domain.Cliente;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Tela_Recuperar_Senha extends AppCompatActivity {

    private Toolbar toolbar_main;
    private TextInputEditText edEmail,edTelefone;
    private TextView tvMsgEmail, tvMsgTelefone,tvTelefone;
    private String email,telefone;
    private FirebaseAuth auth;
    private int verEmail = 0,verTelefone = 0;
    private DatabaseReference mDatabase;
    private LinearLayout ll_email,ll_telefone,ll_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela__recuperar__senha);

        if(!isConnected(getApplicationContext())){
            Toast.makeText(getApplicationContext(), "Conecte-se a uma rede com Internet!", Toast.LENGTH_LONG).show();
        }

        inicializaComponentes();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void resertSenha(String email){
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(Tela_Recuperar_Senha.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            alert("Um e-mail foi enviado para alterar sua senha");
                            finish();
                        }else{
                            alert("E-mail não Registrado");
                        }
                    }
                });
    }

    public void alert(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void inicializaComponentes(){
        toolbar_main = (Toolbar) findViewById(R.id.toolbar_main);
        edEmail = (TextInputEditText) findViewById(R.id.textInput_edEmail);
        edTelefone = (TextInputEditText) findViewById(R.id.textInput_edTelefone);
        tvMsgTelefone = (TextView) findViewById(R.id.tv_mensagemTelefone);
        tvMsgEmail = (TextView) findViewById(R.id.tv_mensagemEmail);
        tvTelefone = (TextView) findViewById(R.id.tvTelefone);
        ll_email = (LinearLayout) findViewById(R.id.ll_email);
        ll_telefone = (LinearLayout) findViewById(R.id.ll_telefone);
        ll_ok = (LinearLayout) findViewById(R.id.ll_ok);

        toolbar_main.setTitle("Recuperar Senha");
        toolbar_main.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SimpleMaskFormatter smf = new SimpleMaskFormatter("(NN) NNNNN-NNNN");
        MaskTextWatcher mtw = new MaskTextWatcher(edTelefone,smf);
        edTelefone.addTextChangedListener(mtw);
    }

    public void botaoCancelar(View v){

        finish();

    }

    public void botaoContinuar(View v){

        if(isConnected(getApplicationContext())){
            tvMsgEmail.setText("");
            verEmail = 0;

            email = edEmail.getText().toString().trim();

            if(!email.contains("@") || !email.contains(".")){
                edEmail.requestFocus();
                tvMsgEmail.setText("E-mail Inválido, por favor digite um e-mail válido!");
                return;
            }

            verificaEmail();
            ll_telefone.setVisibility(View.VISIBLE);
            ll_email.setVisibility(View.GONE);
        }

    }

    public void botaoVoltar(View v){

        ll_email.setVisibility(View.VISIBLE);
        ll_telefone.setVisibility(View.GONE);

    }

    public void botaoConfirmar(View v){

        if (isConnected(getApplicationContext())){
            tvMsgTelefone.setText("");
            verTelefone = 0;

            telefone = edTelefone.getText().toString().trim();

            if(telefone.length() < 15){
                edTelefone.requestFocus();
                tvMsgTelefone.setText("Digite número corretamente, por exemplo: (DD) 90000-0000");
                return;
            }

            verificaTelefone();

            ll_ok.setVisibility(View.VISIBLE);
            ll_telefone.setVisibility(View.GONE);
        }

    }

    public void botaoQueroReceber(View v){

        if(isConnected(getApplicationContext())){
            resertSenha(email);
        }

    }

    public void verificaEmail(){
        mDatabase.child("cliente").orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot obj : dataSnapshot.getChildren()){
                    Cliente c = obj.getValue(Cliente.class);
                    String a = c.getTelefone().substring(13,15);
                    tvTelefone.setText("Por favor digite correntamente o número do telefone que foi salvo quando você criou sua conta no aplicativo: "+"(XX) XXXXX-XX"+a);
                    verEmail = 1;
                }
                if(verEmail < 1){
                    edEmail.requestFocus();
                    tvMsgEmail.setText("Não existe nenhuma conta com esse e-mail no aplicativo!");
                    ll_email.setVisibility(View.VISIBLE);
                    ll_telefone.setVisibility(View.GONE);
                    return;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void verificaTelefone(){
        mDatabase.child("cliente").orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot obj : dataSnapshot.getChildren()){
                    Cliente c = obj.getValue(Cliente.class);
                    if(c.getTelefone().equals(telefone)){
                        verTelefone = 1;
                    }
                }
                if(verTelefone < 1){
                    edTelefone.requestFocus();
                    tvMsgTelefone.setText("Número Incorreto!");
                    ll_telefone.setVisibility(View.VISIBLE);
                    ll_ok.setVisibility(View.GONE);
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

    @Override
    protected void onStart() {
        super.onStart();

        auth = Conexao.getFirebaseAuth();
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
