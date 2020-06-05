package com.example.raydasmesasvcliente05_04_20;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.raydasmesasvcliente05_04_20.database.Conexao;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Sair extends Fragment {

    private FirebaseAuth auth;

    public Fragment_Sair() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        auth = Conexao.getFirebaseAuth();
        auth.signOut();
        Intent it = new Intent(getContext(),Tela_Login.class);
        startActivity(it);
        getActivity().finish();

        return inflater.inflate(R.layout.fragment__sair, container, false);
    }
}
