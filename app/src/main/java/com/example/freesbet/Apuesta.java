package com.example.freesbet;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.TooltipCompat;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.freesbet.bases.BaseActivity;
import com.example.freesbet.widgets.CheckLogout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Apuesta extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.button_cuota1)
    AppCompatButton mButton_cuota1;

    @BindView(R.id.button_cuota2)
    AppCompatButton mButton_cuota2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apuesta);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getText(R.string.title_activity_home));
        /*TextView texto;
        texto.setCompoundDrawablesRelative(ContextCompat.getDrawable(Apuesta.this,R.drawable.com_facebook_button_icon),null,null,null);*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        Menu menu = navigationView.getMenu();

        MenuItem ligas= menu.findItem(R.id.ligas);
        SpannableString stringLigas = new SpannableString(ligas.getTitle());
        stringLigas.setSpan(new TextAppearanceSpan(this, R.style.ColorOpcionesMenu), 0, stringLigas.length(), 0);
        ligas.setTitle(stringLigas);

        MenuItem competiciones= menu.findItem(R.id.competiciones);
        SpannableString stringCompeticiones = new SpannableString(competiciones.getTitle());
        stringCompeticiones.setSpan(new TextAppearanceSpan(this, R.style.ColorOpcionesMenu), 0, stringCompeticiones.length(), 0);
        competiciones.setTitle(stringCompeticiones);

        navigationView.setNavigationItemSelectedListener(this);


        mButton_cuota1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialogFragment bsdFragment =
                        BottomSheetDialogFragmentApuestaLiga.newInstance();
                bsdFragment.show(
                        Apuesta.this.getSupportFragmentManager(), "BSDialog");
            }
        });

        mButton_cuota2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialogFragment bsdFragment =
                        BottomSheetDialogFragmentApuestaLiga.newInstance();
                bsdFragment.show(
                        Apuesta.this.getSupportFragmentManager(), "BSDialog");
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        for(int i = 0; i < menu.size(); i++){
            Drawable drawable = menu.getItem(i).getIcon();
            if(drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            }
        }
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Handle navigation view item clicks here.
        int id=item.getItemId();
        switch (id){

            case R.id.opcion_todos:
                Intent intent1= new Intent(Apuesta.this,Home.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.opcion_menu_fms:
                Intent intent2= new Intent(Apuesta.this,Fms.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.opcion_menu_redbull:
                Intent intent3= new Intent(Apuesta.this, Redbull.class);
                startActivity(intent3);
                finish();
                break;
            case R.id.opcion_menu_bdm:
                Intent intent4= new Intent(Apuesta.this,Bdm.class);
                startActivity(intent4);
                finish();
                break;
            case R.id.opcion_supremaciamc:
                Intent intent5= new Intent(Apuesta.this,Supremacia.class);
                startActivity(intent5);
                finish();
                break;
            case R.id.opcion_ivita_amigos:
                Intent intent6= new Intent(Apuesta.this,Invita.class);
                startActivity(intent6);
                finish();
                break;
            case R.id.opcion_ajustes_cuenta:
                Intent intent7= new Intent(Apuesta.this,Ajustes.class);
                startActivity(intent7);
                finish();
                break;
            case R.id.opcion_logout:
                CheckLogout dialogFragment = new CheckLogout();
                FragmentManager fragmentManager = getSupportFragmentManager();
                dialogFragment.show(fragmentManager,"checkLogout");
                break;
            case R.id.opcion_premios:
                Intent intent8= new Intent(Apuesta.this,Premios.class);
                startActivity(intent8);
                finish();
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
