package com.example.freesbet.bases;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.freesbet.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.List;
import java.util.Map;

public class RVAdapterPedido extends RecyclerView.Adapter<RVAdapterPedido.PedidoViewHolder>{

    static List<Pedido> pedidos;
    private Context mContext;
    FirebaseFirestore db;


    public RVAdapterPedido(List<Pedido> pedidos, Context mContext){
        this.pedidos = pedidos;
        this.mContext = mContext;
        db = FirebaseFirestore.getInstance();
    }

    public static class PedidoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewUrlPedido;
        TextView textViewIdPedido;
        TextView textViewNombrePedido;
        TextView textViewEstado;
        TextView textViewNombre;
        TextView textViewEmail;
        TextView textViewDireccion;
        TextView textViewCiudad;
        TextView textViewProvincia;
        TextView textViewCp;
        TextView textViewTel;

        PedidoViewHolder(View itemView) {
            super(itemView);
            imageViewUrlPedido = itemView.findViewById(R.id.foto);
            textViewIdPedido = itemView.findViewById(R.id.textView_idPedido);
            textViewNombrePedido = itemView.findViewById(R.id.textView_nombrePedido);
            textViewEstado = itemView.findViewById(R.id.textView_estado);
            textViewNombre = itemView.findViewById(R.id.textView_nombre);
            textViewEmail = itemView.findViewById(R.id.textView_email);
            textViewDireccion = itemView.findViewById(R.id.textView_direccionPostal);
            textViewCiudad = itemView.findViewById(R.id.textView_ciudad);
            textViewProvincia = itemView.findViewById(R.id.textView_provincia);
            textViewCp = itemView.findViewById(R.id.textView_cp);
            textViewTel = itemView.findViewById(R.id.textView_telefono);
        }
    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    @Override
    public PedidoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_pedido, viewGroup, false);
        return new PedidoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PedidoViewHolder pedidoViewHolder, int i) {

        DocumentReference docRef = db.collection("premios").document(pedidos.get(i).idPremio);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("PREMIO=>", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d("", "Current data: " + snapshot.getData());
                    Map<String, Object> premioDb = snapshot.getData();
                    Glide.with(mContext)
                            .load((String)premioDb.get("urlImagen"))
                            .into(pedidoViewHolder.imageViewUrlPedido);
                    pedidoViewHolder.textViewIdPedido.setText("ID de pedido: "+pedidos.get(i).idPedido);
                    pedidoViewHolder.textViewNombrePedido.setText("Premio: "+pedidos.get(i).nombrePremio);
                    pedidoViewHolder.textViewEstado.setText("Estado: "+pedidos.get(i).estado);
                    pedidoViewHolder.textViewNombre.setText("A nombre de: "+pedidos.get(i).nombre);
                    pedidoViewHolder.textViewEmail.setText("Email: "+pedidos.get(i).email);
                    pedidoViewHolder.textViewDireccion.setText("Dirección postal: "+pedidos.get(i).direccion);
                    pedidoViewHolder.textViewCiudad.setText("Ciudad: "+pedidos.get(i).ciudad);
                    pedidoViewHolder.textViewProvincia.setText("Provincia: "+pedidos.get(i).provincia);
                    pedidoViewHolder.textViewCp.setText("CP: "+pedidos.get(i).cp);
                    pedidoViewHolder.textViewTel.setText("Teléfono: "+pedidos.get(i).telefono);

                } else {
                    Log.d("PREMIO=>", "Current data: null");
                }
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
