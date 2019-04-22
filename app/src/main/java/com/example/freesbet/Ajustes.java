package com.example.freesbet;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.freesbet.bases.BaseActivity;
import com.example.freesbet.bases.TabViewPagerAdapter;
import com.example.freesbet.widgets.CheckLogout;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class Ajustes extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    View headerView;
    CircleImageView circleImageViewUsuarioMenu;
    TextView textViewNivelUsuarioHeaderMenu;

    public static Context mContext;

    @BindView(R.id.tabs_ajustes)
    TabLayout tabLayoutAjustes;
    @BindView(R.id.pager_ajustes)
    ViewPager viewPagerAjustes;

    public static CircleImageView circleImageViewHeaderPerfilUsuario;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getText(R.string.title_activity_ajustes));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

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

        cargarInfoUsuarioMenu();

        tabLayoutAjustes.setupWithViewPager(viewPagerAjustes);
        inicializarPager();
        mContext = this;


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();
            startActivity(Ajustes.this,Home.class);
            finish();
        }
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
        /*if (id == R.id.action_settings) {
            return true;
        }*/

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
                Intent intent1= new Intent(Ajustes.this,Home.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.opcion_menu_fms:
                Intent intent2= new Intent(Ajustes.this,Fms.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.opcion_menu_redbull:
                Intent intent3= new Intent(Ajustes.this, Redbull.class);
                startActivity(intent3);
                finish();
                break;
            case R.id.opcion_menu_bdm:
                Intent intent4= new Intent(Ajustes.this,Bdm.class);
                startActivity(intent4);
                finish();
                break;
            case R.id.opcion_supremaciamc:
                Intent intent5= new Intent(Ajustes.this,Supremacia.class);
                startActivity(intent5);
                finish();
                break;
            case R.id.opcion_ivita_amigos:
                Intent intent6= new Intent(Ajustes.this,Invita.class);
                startActivity(intent6);
                finish();
                break;
            case R.id.opcion_ajustes_cuenta:
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.opcion_logout:
                CheckLogout dialogFragment = new CheckLogout();
                FragmentManager fragmentManager = getSupportFragmentManager();
                dialogFragment.show(fragmentManager,"checkLogout");
                break;
            case R.id.opcion_premios:
                Intent intent8= new Intent(Ajustes.this,Premios.class);
                startActivity(intent8);
                finish();
                break;
            // this is done, now let us go and intialise the home page.
            // after this lets start copying the above.
            // FOLLOW MEEEEE>>>
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void inicializarPager(){
        TabViewPagerAdapter tabViewPagerAdapter = new TabViewPagerAdapter(getSupportFragmentManager());
        PerfilFragment perfilFragment = new PerfilFragment();
        tabViewPagerAdapter.addFragment(new PerfilFragment(),"Perfil");
        tabViewPagerAdapter.addFragment(new ActividadFragment(),"Actividad");
        viewPagerAjustes.setAdapter(tabViewPagerAdapter);
    }

    private void cargarInfoUsuarioMenu(){
        headerView = navigationView.getHeaderView(0);
        TextView textViewNombreUsuarioHeaderMenu = headerView.findViewById(R.id.textView_nombreUsuario_headerMenu);
        textViewNombreUsuarioHeaderMenu.setText(nombreUsuario);
        circleImageViewUsuarioMenu = headerView.findViewById(R.id.circleview_header_perfil_usuario);
        Glide.with(getApplicationContext()).load(photoUrlUsuario).into(circleImageViewUsuarioMenu);
        textViewNivelUsuarioHeaderMenu = headerView.findViewById(R.id.textView_NivelUsuario_headerMenu);
        textViewNivelUsuarioHeaderMenu.setText("Nivel "+Integer.toString(nivelUsuario));
    }

}
