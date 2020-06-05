package com.example.raydasmesasvcliente05_04_20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raydasmesasvcliente05_04_20.database.Conexao;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Tela_Login extends AppCompatActivity {

    private EditText edEmail, edSenha;
    private TextView tvRecuperarSenha, tvCriarConta,tvMensagem;
    private RelativeLayout rl_Botao_Login;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela__login);

        if(!isConnected(getApplicationContext())){
            Toast.makeText(getApplicationContext(), "Conecte-se a uma rede com Internet!", Toast.LENGTH_LONG).show();
        }

        inicializaComponentes();
        eventoClicks();

        SharedPreferences prefs = getSharedPreferences("preferencias",Context.MODE_PRIVATE);
        String aux = prefs.getString("endereco","sim");
        if(aux.contains("sim")){
            exibiAlert();
        }

    }

    public void exibiAlert(){
        AlertDialog.Builder alert = new AlertDialog.Builder(Tela_Login.this);
        alert.setTitle("Aviso Importante")
                .setIcon(R.drawable.ic_error)
                .setMessage("Avisamos que no momento os agendamentos feitos pelo aplicativo devem ser todos com os endereços de Mucajaí-RR, se houver agendamento com endereços de outras localidades pode ser que seu agendamento seja cancelado, caso isso aconteça entraremos em contado avisando!")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences prefs = getSharedPreferences("preferencias",Context.MODE_PRIVATE);
                        SharedPreferences.Editor ed = prefs.edit();
                        ed.putString("endereco","nao");
                        ed.apply();
                    }
                });
        alert.show();
    }

    public void eventoClicks(){
        tvCriarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected(getApplicationContext())){
                    Intent it = new Intent(getApplicationContext(),Tela_Cadastro.class);
                    startActivity(it);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Conecte-se a uma rede com Internet!", Toast.LENGTH_LONG).show();
                }
            }
        });

        rl_Botao_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected(getApplicationContext())){
                    tvMensagem.setText("");
                    String email = edEmail.getText().toString().trim();
                    String senha = edSenha.getText().toString().trim();

                    if(!email.contains("@") || !email.contains(".")){
                        edEmail.requestFocus();
                        tvMensagem.setText("E-mail Inválido, por favor digite um e-mail válido!");
                        return;
                    }

                    if(senha.length() < 6){
                        tvMensagem.setText("E-mail ou senha incorreto!");
                        edEmail.setText("");
                        edSenha.setText("");
                        return;
                    }

                    login(email,senha);
                }else{
                    Toast.makeText(getApplicationContext(), "Conecte-se a uma rede com Internet!", Toast.LENGTH_LONG).show();
                }
            }
        });

        tvRecuperarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected(getApplicationContext())){
                    Intent i = new Intent(Tela_Login.this,Tela_Recuperar_Senha.class);
                    startActivity(i);
                }else{
                    Toast.makeText(getApplicationContext(), "Conecte-se a uma rede com Internet!", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public void login(String email,String senha){
        auth.signInWithEmailAndPassword(email,senha)
                .addOnCompleteListener(Tela_Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent i = new Intent(Tela_Login.this,MainActivity.class);
                            startActivity(i);
                            finish();
                        }else{
                            tvMensagem.setText("E-mail ou senha incorreto!");
                            edEmail.setText("");
                            edSenha.setText("");
                        }
                    }
                });
    }

    public void inicializaComponentes(){
        edEmail = (EditText) findViewById(R.id.ed_email);
        edSenha = (EditText) findViewById(R.id.ed_password);
        tvRecuperarSenha = (TextView) findViewById(R.id.tv_recuperarSenha);
        tvCriarConta = (TextView) findViewById(R.id.tv_criarConta);
        rl_Botao_Login = (RelativeLayout) findViewById(R.id.rl_login);
        tvMensagem = (TextView) findViewById(R.id.tv_mensagem);
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
