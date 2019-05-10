package com.example.freesbet;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.freesbet.bases.BaseActivity;
import com.example.freesbet.bases.EventoLista;
import com.example.freesbet.bases.Pedido;
import com.example.freesbet.bases.RVAdapter;
import com.example.freesbet.bases.RVAdapterPedido;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class PedidosFragment extends Fragment {

    FirebaseFirestore db;

    RecyclerView rv;
    private List<Pedido> pedidos = new ArrayList<>();
    RVAdapterPedido adapter;
    TextView textViewNohayEventos;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pedidos,container,false);
        ButterKnife.bind(getActivity());

        db = FirebaseFirestore.getInstance();

        rv =view.findViewById(R.id.recyclerView_pedidos);
        textViewNohayEventos = view.findViewById(R.id.textView_noHayEventos);
        initializeRecyclerView();
        getPedidos();

        return view;
    }

    private void initializeRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());

        rv.setLayoutManager(llm);
    }

    private void getPedidos(){
        db.collection("pedidos").whereEqualTo("idUsuario", BaseActivity.idUsuario)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("PEDIDO=>", "Listen failed.", e);
                            return;
                        }
                        if (value != null && !value.isEmpty()){
                            textViewNohayEventos.setVisibility(View.GONE);
                            rv.setVisibility(View.VISIBLE);
                            pedidos = new ArrayList<>();
                            int i = 0;
                            for (QueryDocumentSnapshot doc : value) {

                                pedidos.add(doc.toObject(Pedido.class));
                                pedidos.get(i).setIdPedido(doc.getId());
                                i++;
                            }
                            Log.d("PEDIDO=>", "pedidos: " + pedidos);

                            adapter = new RVAdapterPedido(pedidos, getActivity());
                            rv.setAdapter(adapter);
                        }else{
                            textViewNohayEventos.setVisibility(View.VISIBLE);
                            rv.setVisibility(View.GONE);
                        }
                    }
                });
    }
}
