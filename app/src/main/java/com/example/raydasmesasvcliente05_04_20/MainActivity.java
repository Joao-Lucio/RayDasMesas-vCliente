package com.example.raydasmesasvcliente05_04_20;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raydasmesasvcliente05_04_20.database.Conexao;
import com.example.raydasmesasvcliente05_04_20.domain.Agendamento;
import com.example.raydasmesasvcliente05_04_20.domain.Cliente;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private NavController navController;
    private AppBarConfiguration appBarConfiguration;

    private TextView tvNome;
    private View viewHeader;
    private LinearLayout ll_perfil;
    private DatabaseReference mDatabase;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private String telefone;

    private static final String NOTIFICATION_CHANNEL_ID = "12345";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!isConnected(getApplicationContext())){
            Toast.makeText(this, "Conecte-se a uma rede com acesso a internet!", Toast.LENGTH_LONG).show();
        }

        inicializarComponentes();
        configuracoes();

        monitoraEntregue();

        SharedPreferences prefs = getSharedPreferences("preferencias",Context.MODE_PRIVATE);
        String aux = prefs.getString("net","sim");
        if(aux.contains("sim")){
            exibiAlert();

        }

    }

    public void exibiAlert(){
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("Pequeno Aviso")
                .setIcon(R.drawable.ic_error)
                .setMessage("A velocidade em que as informações aparecem pode ser afetada de acordo com a velocidade de sua internet!")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences prefs = getSharedPreferences("preferencias",Context.MODE_PRIVATE);
                        SharedPreferences.Editor ed = prefs.edit();
                        ed.putString("net","nao");
                        ed.apply();
                    }
                });
        alert.show();
    }

    public void inicializarComponentes(){

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        viewHeader = navigationView.getHeaderView(0);

        navController = Navigation.findNavController(this,R.id.nav_host_fragment);

        tvNome = (TextView) viewHeader.findViewById(R.id.tvNome);
        ll_perfil = (LinearLayout) viewHeader.findViewById(R.id.ll_perfil);

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void configuracoes(){

        appBarConfiguration = new AppBarConfiguration.Builder(new int[]{R.id.agendamento,R.id.entregue,R.id.promocao,R.id.sair})
                .setDrawerLayout(drawerLayout)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController,appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView,navController);

        auth = Conexao.getFirebaseAuth();
        user = auth.getCurrentUser();

        mDatabase.child("cliente").orderByChild("uid").equalTo(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot obj : dataSnapshot.getChildren()){
                    Cliente c = obj.getValue(Cliente.class);
                    String a = c.getNome();
                    if(a.contains(" ")){
                        tvNome.setText(a.substring(0,a.indexOf(" ")));
                    }else{
                        tvNome.setText(a);
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ll_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,Tela_Perfil.class);
                startActivity(i);
            }
        });
    }

    public void monitoraEntregue(){
        auth = Conexao.getFirebaseAuth();
        user = auth.getCurrentUser();
        mDatabase.child("cliente").orderByChild("uid").equalTo(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot obj : dataSnapshot.getChildren()){
                    Cliente c = obj.getValue(Cliente.class);
                    telefone = c.getTelefone();
                }
                mDatabase.child("entregue").orderByChild("telefone_responsavel").equalTo(telefone).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Agendamento a = dataSnapshot.getValue(Agendamento.class);
                        if(a.getClientefoinot().contains("nao") && a.getUltmodificou().contains("adm")){
                            showNotificao();
                            a.setClientefoinot("sim");
                            mDatabase.child("entregue").child(a.getUid()).setValue(a);
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void showNotificao(){
        int id = 1;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String notificationChannelId = NOTIFICATION_CHANNEL_ID;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),notificationChannelId);

        builder.setAutoCancel(true)
                .setSmallIcon(R.drawable.im_logo)
                .setVibrate(new long[]{150,300,150,600});

        builder.setContentText("Seu aluguel foi entregue!")
                .setContentTitle("Aluguel Entregue");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mNotificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,"NOTIFICATION_CHANNEL_NAME",importance);
            mNotificationChannel.enableLights(false);
            mNotificationChannel.enableVibration(false);
            assert  notificationManager != null;
            builder.setChannelId(NOTIFICATION_CHANNEL_ID);
            notificationManager.createNotificationChannel(mNotificationChannel);
        }

        notificationManager.notify(id,builder.build());

        try{
            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(this,som);
            toque.play();
        }catch (Exception e){}
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController,appBarConfiguration);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else
            super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();

        auth = Conexao.getFirebaseAuth();
        user = Conexao.getFirebaseUser();
        verificaUser();
    }

    public void verificaUser(){
        if(user == null){
            finish();
        }
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if ( cm != null ) {
            NetworkInfo ni = cm.getActiveNetworkInfo();

            return ni != null && ni.isConnected();
        }

        return false;
    }
}