package com.example.raydasmesasvcliente05_04_20.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.CompoundButton;

import com.example.raydasmesasvcliente05_04_20.Activity_Add_Alter_Agendamento;
import com.example.raydasmesasvcliente05_04_20.Activity_Paypal_Pagamento;
import com.example.raydasmesasvcliente05_04_20.R;
import com.example.raydasmesasvcliente05_04_20.domain.Agendamento;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;

public class EntregueRecyclerViewAdapter extends FirebaseRecyclerAdapter<Agendamento,AgendamentoViewHolder> {

    private SimpleDateFormat formato;

    public EntregueRecyclerViewAdapter(Class<Agendamento> modelClass, int modelLayout, Class<AgendamentoViewHolder> viewHolderClass, Query ref){
        super(modelClass, modelLayout, viewHolderClass, ref);

        formato = new SimpleDateFormat("dd/MM/yyyy");
    }

    @Override
    protected void populateViewHolder(final AgendamentoViewHolder agendamentoViewHolder, final Agendamento agendamento, int i) {

        agendamentoViewHolder.tvQtdMesas.setText(String.valueOf(agendamento.getQtdMesas()));
        agendamentoViewHolder.tvQtdCadeiras.setText(String.valueOf(agendamento.getQtdCadeiras()));
        agendamentoViewHolder.tvData.setText(formato.format(agendamento.getData()));
        agendamentoViewHolder.tvHorario.setText(agendamento.getHorario());
        agendamentoViewHolder.tvDesconto.setText(agendamento.getDesconto()+"%");
        agendamentoViewHolder.tvRua.setText(agendamento.getRua());
        agendamentoViewHolder.tvNumero.setText(String.valueOf(agendamento.getNumero()));
        agendamentoViewHolder.tvBairro.setText(agendamento.getBairro());
        agendamentoViewHolder.tvReferencia.setText(agendamento.getReferencia());
        agendamentoViewHolder.tvContatoResponsavel.setText(agendamento.getTelefone_responsavel());
        if(agendamento.getNome_responsavel().contains(" ")){
            agendamentoViewHolder.tvNomeResponsavel.setText(agendamento.getNome_responsavel().substring(0,agendamento.getNome_responsavel().indexOf(" ")));
        }else{
            agendamentoViewHolder.tvNomeResponsavel.setText(agendamento.getNome_responsavel());
        }
        agendamentoViewHolder.tvStatus.setText(agendamento.getStatus());
        agendamentoViewHolder.tvValor.setText("R$ "+agendamento.getValor());
        agendamentoViewHolder.checkBox_Concluido.setVisibility(View.VISIBLE);
        agendamentoViewHolder.btExcluir.setVisibility(View.GONE);

        agendamentoViewHolder.switch_endereco.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    agendamentoViewHolder.ll_endereco.setVisibility(View.VISIBLE);
                }else agendamentoViewHolder.ll_endereco.setVisibility(View.GONE);
            }
        });

        agendamentoViewHolder.switch_infor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    agendamentoViewHolder.ll_infor.setVisibility(View.VISIBLE);
                }else agendamentoViewHolder.ll_infor.setVisibility(View.GONE);
            }
        });

        agendamentoViewHolder.imageButton_Seta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(agendamentoViewHolder.rlButoes.getVisibility() == View.GONE){
                    agendamentoViewHolder.rlButoes.setVisibility(View.VISIBLE);
                    agendamentoViewHolder.imageButton_Seta.setImageResource(R.drawable.ic_seta_cima);
                }else {
                    agendamentoViewHolder.rlButoes.setVisibility(View.GONE);
                    agendamentoViewHolder.imageButton_Seta.setImageResource(R.drawable.ic_seta_baixo);
                }
            }
        });

        if(agendamentoViewHolder.tvStatus.getText().toString().contains("C")){
            agendamentoViewHolder.checkBox_Concluido.setChecked(true);
        }

        if(agendamentoViewHolder.tvStatus.getText().toString().contains("Pago")){
            agendamentoViewHolder.checkBox_Pago.setChecked(true);
            agendamentoViewHolder.ll_paypal.setEnabled(false);
            agendamentoViewHolder.btAlterar.setEnabled(false);
            agendamentoViewHolder.imageButton_Seta.setEnabled(false);
            agendamentoViewHolder.imageButton_Seta.setVisibility(View.GONE);
        }

        agendamentoViewHolder.ll_paypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(agendamentoViewHolder.context);
                alert.setTitle("Informação")
                        .setIcon(R.drawable.ic_atencao)
                        .setMessage("Após realizar o pagamento pela aplicação o agendamento não poderá mais ser alterado ou excluido. Caso tenha dúvida você pode realizar o pagamento posteriomente. Gostaria de pagar agora?")
                        .setCancelable(false)
                        .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(agendamentoViewHolder.context, Activity_Paypal_Pagamento.class);
                                intent.putExtra("agendamento",agendamento);
                                intent.putExtra("tela","entregue");
                                agendamentoViewHolder.context.startActivity(intent);
                                agendamentoViewHolder.rlButoes.setVisibility(View.GONE);
                                agendamentoViewHolder.imageButton_Seta.setImageResource(R.drawable.ic_seta_baixo);
                            }
                        });
                alert.show();
            }
        });

        agendamentoViewHolder.btAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(agendamentoViewHolder.context, Activity_Add_Alter_Agendamento.class);
                intent.putExtra("agendamento",agendamento);
                intent.putExtra("tipo",agendamento.getTipoAluguel());
                intent.putExtra("editado","sim");
                intent.putExtra("tela","entregue");
                agendamentoViewHolder.context.startActivity(intent);
                agendamentoViewHolder.rlButoes.setVisibility(View.GONE);
                agendamentoViewHolder.imageButton_Seta.setImageResource(R.drawable.ic_seta_baixo);
            }
        });

    }
}