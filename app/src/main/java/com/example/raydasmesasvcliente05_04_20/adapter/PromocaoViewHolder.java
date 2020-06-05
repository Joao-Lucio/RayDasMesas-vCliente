package com.example.raydasmesasvcliente05_04_20.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.raydasmesasvcliente05_04_20.R;

public class PromocaoViewHolder extends RecyclerView.ViewHolder {

    public TextView tvNome;
    public TextView tvDataInicio;
    public TextView tvDataFim;
    public TextView tvDescricao;

    public PromocaoViewHolder(@NonNull View itemView) {
        super(itemView);

        tvNome = (TextView) itemView.findViewById(R.id.item_promocao_nome);
        tvDataInicio = (TextView) itemView.findViewById(R.id.item_promocao_dataInicio);
        tvDataFim = (TextView) itemView.findViewById(R.id.item_promocao_dataFim);
        tvDescricao = (TextView) itemView.findViewById(R.id.item_promocao_descricao);
    }
}
