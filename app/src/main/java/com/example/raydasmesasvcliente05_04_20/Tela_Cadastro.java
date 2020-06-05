package com.example.raydasmesasvcliente05_04_20;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raydasmesasvcliente05_04_20.database.Conexao;
import com.example.raydasmesasvcliente05_04_20.domain.Cliente;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Tela_Cadastro extends AppCompatActivity {

    private Toolbar toolbar_main;
    private TextInputEditText edNome,edEmail,edSenha,edConfirmaSenha,edTelefone,edBairro,edRua,edNumero,edReferencia;
    private TextView tvMsgNome,tvMsgEmail,tvMsgSenha,tvMsgConfirma,tvMsgTelefone;
    private RelativeLayout rl_nomeSenha,rl_endereco;
    private String[] numeros = new String[]{"0","1","2","3","4","5","6","7","8","9"};
    private String nome,senha,confirmaSenha,email,telefone,bairro,rua,numero,referencia;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference mDatabase;
    private int verEmail = 0,verTelefone = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela__cadastro);

        if(!isConnected(getApplicationContext())){
            Toast.makeText(getApplicationContext(), "Conecte-se a uma rede com Internet!", Toast.LENGTH_LONG).show();
        }

        inicializaComponentes();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void alert(String msg){
        Toast.makeText(Tela_Cadastro.this, msg, Toast.LENGTH_LONG).show();
    }

    public void inicializaComponentes(){
        toolbar_main = (Toolbar) findViewById(R.id.toolbar_main);
        edNome = (TextInputEditText) findViewById(R.id.textInput_edNome);
        edEmail = (TextInputEditText) findViewById(R.id.textInput_edEmail);
        edSenha = (TextInputEditText) findViewById(R.id.textInput_edPassword);
        edConfirmaSenha = (TextInputEditText) findViewById(R.id.textInput_edConfirmaPassword);
        edTelefone = (TextInputEditText) findViewById(R.id.textInput_edTelefone);
        edBairro = (TextInputEditText) findViewById(R.id.textInput_edBairro);
        edRua = (TextInputEditText) findViewById(R.id.textInput_edRua);
        edNumero = (TextInputEditText) findViewById(R.id.textInput_edNumero);
        edReferencia = (TextInputEditText) findViewById(R.id.textInput_edReferencia);
        rl_nomeSenha = (RelativeLayout) findViewById(R.id.rl_nomeSenha);
        rl_endereco = (RelativeLayout) findViewById(R.id.rl_endereco);
        tvMsgConfirma = (TextView) findViewById(R.id.tv_mensagemConfirma);
        tvMsgEmail = (TextView) findViewById(R.id.tv_mensagemEmail);
        tvMsgNome = (TextView) findViewById(R.id.tv_mensagemNome);
        tvMsgSenha = (TextView) findViewById(R.id.tv_mensagemSenha);
        tvMsgTelefone = (TextView) findViewById(R.id.tv_mensagemTelefone);

        toolbar_main.setTitle("Novo Usuário");
        toolbar_main.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SimpleMaskFormatter smf = new SimpleMaskFormatter("(NN) NNNNN-NNNN");
        MaskTextWatcher mtw = new MaskTextWatcher(edTelefone,smf);
        edTelefone.addTextChangedListener(mtw);
    }

    public void botaoCancelar(View v){
        Intent i = new Intent(Tela_Cadastro.this,Tela_Login.class);
        startActivity(i);
        finish();
    }

    public void botaoContinuar(View v){
        if(isConnected(getApplicationContext())){

            verEmail = 0;
            verTelefone = 0;
            tvMsgNome.setText("");
            tvMsgConfirma.setText("");
            tvMsgEmail.setText("");
            tvMsgTelefone.setText("");
            tvMsgSenha.setTextColor(Color.parseColor("#9F9F9F"));
            nome = edNome.getText().toString().trim();
            email = edEmail.getText().toString().trim();
            senha = edSenha.getText().toString().trim();
            confirmaSenha = edConfirmaSenha.getText().toString().trim();
            telefone = edTelefone.getText().toString().trim();

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

            if(!email.contains("@") || !email.contains(".")){
                edEmail.requestFocus();
                tvMsgEmail.setText("E-mail Inválido, por favor digite um e-mail válido!");
                return;
            }

            verificaEmail();

            if(senha.length() < 6){
                edSenha.requestFocus();
                tvMsgSenha.setTextColor(Color.parseColor("#FF0000"));
                return;
            }

            if(!confirmaSenha.equals(senha)){
                edConfirmaSenha.requestFocus();
                tvMsgConfirma.setText("Senhas Diferentes!");
                return;
            }

            if(telefone.length() < 15){
                edTelefone.requestFocus();
                tvMsgTelefone.setText("Digite número corretamente, por exemplo: (DD) 90000-0000");
                return;
            }

            verificaTelefone();

            rl_endereco.setVisibility(View.VISIBLE);
            rl_nomeSenha.setVisibility(View.GONE);

        }
    }

    public void botaoVoltar(View v){
        rl_endereco.setVisibility(View.GONE);
        rl_nomeSenha.setVisibility(View.VISIBLE);
    }

    public void botaoRegistrar(View v){

        bairro = edBairro.getText().toString().trim();
        rua = edRua.getText().toString().trim();
        numero = edNumero.getText().toString().trim();
        referencia = edReferencia.getText().toString().trim();

        if(edBairro.getText().toString().equals("") || edRua.getText().toString().equals("") || edNumero.getText().toString().equals("") || edReferencia.getText().toString().equals("")){
            Toast.makeText(this, "Por favor, preencha todos os campos!", Toast.LENGTH_LONG).show();
            return;
        }

        if(isConnected(getApplicationContext())){
            criarUser(email,senha);
        }else{
            alert("Conecte-se a uma rede com Internet!");
        }

    }

    public void criarUser(final String email, String senha){
        auth.createUserWithEmailAndPassword(email,senha)
                .addOnCompleteListener(Tela_Cadastro.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            user = auth.getCurrentUser();
                            Cliente cliente = new Cliente();
                            cliente.setNome(nome);
                            cliente.setEmail(email);
                            cliente.setTelefone(telefone);
                            cliente.setBairro(bairro);
                            cliente.setRua(rua);
                            cliente.setNumero(numero);
                            cliente.setReferencia(referencia);
                            cliente.setUid(user.getUid());
                            try {
                                mDatabase.child("cliente").child(cliente.getUid()).setValue(cliente);
                                alert("Usuário Cadastrado com Sucesso!");
                                Intent it = new Intent(Tela_Cadastro.this,MainActivity.class);
                                startActivity(it);
                                finish();
                            }catch (Exception e){
                                alert("Erro ao cadastrar usuário, tente novamente mais tarde!");
                            }
                        }else{
                            alert("Erro ao cadastrar usuário, tente novamente mais tarde!");
                        }
                    }
                });
    }

    public void verificaEmail(){
        mDatabase.child("cliente").orderByChild("email").equalTo(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot obj : dataSnapshot.getChildren()){
                    verEmail = 1;
                }
                if(verEmail > 0){
                    edEmail.requestFocus();
                    tvMsgEmail.setText("E-mail já está sendo utilizado, por favor digite outro!");
                    rl_nomeSenha.setVisibility(View.VISIBLE);
                    rl_endereco.setVisibility(View.GONE);
                    return;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void verificaTelefone(){
        mDatabase.child("cliente").orderByChild("telefone").equalTo(telefone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot obj : dataSnapshot.getChildren()){
                    verTelefone = 1;
                }
                if(verTelefone > 0){
                    edEmail.requestFocus();
                    tvMsgTelefone.setText("Telefone já está sendo utilizado, por favor digite outro!");
                    rl_nomeSenha.setVisibility(View.VISIBLE);
                    rl_endereco.setVisibility(View.GONE);
                    return;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        auth = Conexao.getFirebaseAuth();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent i = new Intent(Tela_Cadastro.this,Tela_Login.class);
            startActivity(i);
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
