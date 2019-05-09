package com.example.freesbet;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.freesbet.bases.AdapterGridPremios;
import com.example.freesbet.bases.AppFreesBet;
import com.example.freesbet.bases.BaseActivity;
import com.example.freesbet.bases.BooVariable;
import com.example.freesbet.bases.Premio;
import com.example.freesbet.widgets.DialogFormEnviarPremio;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.freesbet.bases.BaseActivity.coinsUsuario;
import static com.example.freesbet.bases.BaseActivity.datosUsuarioActualizados;
import static com.example.freesbet.bases.BaseActivity.idUsuario;
import static com.example.freesbet.bases.BaseActivity.nivelUsuario;
import static com.example.freesbet.bases.BaseActivity.nombreUsuario;
import static com.example.freesbet.bases.BaseActivity.photoUrlUsuario;

public class PremiosFragment extends Fragment implements AdapterView.OnItemClickListener {


    private List<Premio> premios;
    AdapterGridPremios adapterGridPremios;
    GridView gridViewPremios;

    FirebaseFirestore db;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_premios,container,false);
        ButterKnife.bind(getActivity());

        db = FirebaseFirestore.getInstance();
        gridViewPremios = view.findViewById(R.id.gridView_premios);
        getPremios();

        return view;
    }

    private void getPremios(){

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Cargando Premios");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Query query = db.collection("premios");

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("Listener Premios", "Listen failed.", e);
                    return;
                }
                if (value != null && !value.isEmpty()){
                    premios = new ArrayList<>();
                    for (QueryDocumentSnapshot document : value) {
                        Map<String, Object> premioDb = document.getData();
                        premios.add(new Premio(
                                document.getId(),
                                (String)premioDb.get("nombre"),
                                (String)premioDb.get("urlImagen"),
                                ((Long)premioDb.get("costeCoins")).intValue())
                        );
                    }
                    premios.sort(Comparator.comparing(Premio::getCosteCoins).reversed());

                    adapterGridPremios = new AdapterGridPremios(getActivity(), premios);
                    gridViewPremios.setAdapter(adapterGridPremios);
                    gridViewPremios.setOnItemClickListener(PremiosFragment.this);

                    progressDialog.dismiss();

                    Log.d("Listener Premios", "Eventos actuales en: " + premios);
                }
                else{

                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Premio item = (Premio) parent.getItemAtPosition(position);

        if(coinsUsuario >= item.getCosteCoins()){
            DialogFormEnviarPremio dialogFragment = new DialogFormEnviarPremio();
            Bundle bundle = new Bundle();
            bundle.putString("idPremio",item.getId());
            bundle.putString("nombrePremio",item.getNombre());
            bundle.putInt("costeCoins",item.getCosteCoins());
            dialogFragment.setArguments(bundle);
            FragmentManager fragmentManager =  getActivity().getSupportFragmentManager();

            dialogFragment.show(fragmentManager,"formEnviarPremio");
        }else{
            //error
            Snackbar.make(getActivity().findViewById(android.R.id.content),"No tienes suficientes coins para ese premio",Snackbar.LENGTH_LONG).show();
        }


    }
}
