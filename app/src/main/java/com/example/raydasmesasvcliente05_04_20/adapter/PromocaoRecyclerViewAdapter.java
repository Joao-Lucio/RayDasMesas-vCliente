package com.example.raydasmesasvcliente05_04_20.adapter;

import com.example.raydasmesasvcliente05_04_20.domain.Promocao;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;

public class PromocaoRecyclerViewAdapter extends FirebaseRecyclerAdapter<Promocao,PromocaoViewHolder> {

    private SimpleDateFormat formato;

    public PromocaoRecyclerViewAdapter(Class<Promocao> modelClass, int modelLayout, Class<PromocaoViewHolder> viewHolderClass, Query ref){
        super(modelClass, modelLayout, viewHolderClass, ref);

        formato = new SimpleDateFormat("dd/MM/yyyy");
    }

    @Override
    protected void populateViewHolder(PromocaoViewHolder promocaoViewHolder, Promocao promocao, int i) {

        promocaoViewHolder.tvDataInicio.setText(formato.format(promocao.getData_inic()));
        promocaoViewHolder.tvDataFim.setText(formato.format(promocao.getData_fim()));
        promocaoViewHolder.tvNome.setText(promocao.getNome());
        promocaoViewHolder.tvDescricao.setText(promocao.getDescricao());

    }
}
