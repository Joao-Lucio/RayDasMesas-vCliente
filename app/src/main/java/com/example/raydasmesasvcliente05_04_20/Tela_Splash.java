package com.example.raydasmesasvcliente05_04_20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.raydasmesasvcliente05_04_20.database.Conexao;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Tela_Splash extends AppCompatActivity {

    private FirebaseUser user;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela__splash);

        // Metodo que espera determinado tem para executar o que está dentro dele
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(user == null){
                    proximaTelaLogin();
                }else{
                    proximaTelaMain();
                }

            }
        },2000);

    }

    // Metodo que chama proxima tela
    public void proximaTelaLogin(){
        Intent it = new Intent(Tela_Splash.this, Tela_Login.class);
        startActivity(it);
        finish();
    }

    public void proximaTelaMain(){
        Intent it = new Intent(Tela_Splash.this, MainActivity.class);
        startActivity(it);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        auth = Conexao.getFirebaseAuth();
        user = auth.getCurrentUser();
    }
}
