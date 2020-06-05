package com.example.raydasmesasvcliente05_04_20;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raydasmesasvcliente05_04_20.config.AlertConfirm;
import com.example.raydasmesasvcliente05_04_20.config.AlertError;
import com.example.raydasmesasvcliente05_04_20.config.Config;
import com.example.raydasmesasvcliente05_04_20.config.JavaMailAPI;
import com.example.raydasmesasvcliente05_04_20.database.Conexao;
import com.example.raydasmesasvcliente05_04_20.domain.Agendamento;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class Activity_Paypal_Pagamento extends AppCompatActivity {

    //public TextView txtId,txtStatus,txtMontante;

    private static final int PAYPAL_REQUEST_CODE = 6842;
    private static PayPalConfiguration config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX) // SandBox é Isso é pra utilizar a conta Sanbox para teste
            .clientId(Config.PAYPAL_CLIENT_ID);
    private Agendamento agendamento;

    private String produto = "";
    private String tela = "";
    private String pago = "Pago";
    private String status = "Agendado";
    private String statusE = "Entregue";
    private String tipoAluguel = "";
    private int qtdMesas = 0, qtdCadeiras = 0;
    private float valor = 0;

    private DatabaseReference mDatabase;

    private FirebaseAuth auth;
    private FirebaseUser user;

    private String dataAluguel = "";
    private String nomeResponsavel = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__paypal__pagamento);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent it = getIntent();
        agendamento = (Agendamento) it.getSerializableExtra("agendamento");
        tela = it.getStringExtra("tela");

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);

        processarPagamento();

    }

    private void processarPagamento(){
        qtdMesas = agendamento.getQtdMesas();
        qtdCadeiras = agendamento.getQtdCadeiras();
        tipoAluguel = agendamento.getTipoAluguel();
        valor = agendamento.getValor();

        if(tipoAluguel.contains("j")){
            if((qtdMesas* 4) > qtdCadeiras){
                produto = (qtdCadeiras/4) + " Jogos e mais " + (qtdMesas - (qtdCadeiras/4)) + " Mesas";
            }else if((qtdMesas* 4) < qtdCadeiras){
                produto = qtdMesas + " Jogos e mais " + (qtdCadeiras - (qtdMesas*4)) + " Cadeiras";
            }else{
                produto = qtdMesas + " Jogos Completos";
            }
        }else if(tipoAluguel.contains("m")){
            produto = qtdMesas + " Mesas";
        }else if(tipoAluguel.contains("c")){
            produto = qtdCadeiras + " Cadeiras";
        }
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(valor)),"BRL",produto,PayPalPayment.PAYMENT_INTENT_SALE);
        Intent it = new Intent(this, PaymentActivity.class);
        it.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        it.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(it,PAYPAL_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        AlertConfirm alert = new AlertConfirm();
                        auth = Conexao.getFirebaseAuth();
                        user = auth.getCurrentUser();
                        String email = user.getEmail();
                        String assunto = "Confirmação de Pagamento";
                        dataAluguel = agendamento.getDataString();
                        nomeResponsavel = agendamento.getNome_responsavel();
                        if(tela.contains("a")){
                            agendamento.setStatus(status+"/"+pago);
                            mDatabase.child("agendamento").child(agendamento.getUid()).setValue(agendamento);
                            JavaMailAPI javaMailAPI = new JavaMailAPI(this,email,assunto,"O pagamento do Agendamento de " + produto + " para a data " +dataAluguel + " em nome de " + nomeResponsavel + " no valor de R$ " + String.valueOf(valor) + " foi realizado com sucesso.");
                            javaMailAPI.execute();
                            alert.show(getSupportFragmentManager(),"example dialog");
                        }else{
                            agendamento.setStatus(statusE+"/"+pago);
                            mDatabase.child("entregue").child(agendamento.getUid()).setValue(agendamento);
                            JavaMailAPI javaMailAPI = new JavaMailAPI(this,email,assunto,"O pagamento do Agendamento de " + produto + " para a data " +dataAluguel + " em nome de " + nomeResponsavel + " no valor de R$ " + String.valueOf(valor) + " foi realizado com sucesso.");
                            javaMailAPI.execute();
                            alert.show(getSupportFragmentManager(),"example dialog");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)
                Toast.makeText(this, "Pagamento Cancelado!", Toast.LENGTH_SHORT).show();
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID){
            AlertError error = new AlertError();
            error.show(getSupportFragmentManager(),"example dialog");
        }

    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this,PayPalService.class));
        super.onDestroy();
    }

}