package com.thayskeillasimone.cervejaki.cervaki;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;

public class AplicacaoCadastroActivity extends AppCompatActivity{
        //implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    Button btcadastrapessoa, btlistarpessoas, btsairtelacadastro;
    Button btcadastrar, btcancelar;
    Button btvoltar, btavancar, btmenu_principal;
    TextView txtnome, txtendereco, txtavaliacao;
    EditText ednome, edendereco, edavaliacao;
    SQLiteDatabase db;
    Cursor dados;
    int numero_registros = 0;
    int posicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aplicacao_cadastro);
        try{
            db = openOrCreateDatabase("banco_dados", Context.MODE_PRIVATE, null);
            db.execSQL("create table if not exists cadcervej(codusurario integer primary key autoincrement, " + "nome text not null, endereco text not null, avaliacao integer not null)");
        } catch(Exception e){
            ExibirMensagem(e.toString());
        }
        CarregarTelaPrincipal();
    }

    //metodo que carrega a tela principal do app, que so ocorre se o banco fro criado
    public void CarregarTelaPrincipal() {
        setContentView(R.layout.activity_aplicacao_cadastro);
        btcadastrapessoa = (Button) findViewById(R.id.btcadastrarpessoa);
        btlistarpessoas = (Button) findViewById(R.id.btlistarpessoas);
        btsairtelacadastro = (Button) findViewById(R.id.btsaitelacadastro);
        btcadastrapessoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                CarregarTelaCadastro();
            }
        });


        btlistarpessoas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                CarregarTelaListagemPessoas();
            }
        });

        btsairtelacadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                carregarTelaInicial();
                }
                //Intent intent = new Intent(AplicacaoCadastroActivity.this, MainActivity.class); startActivity(intent);
/*
                setContentView(R.layout.activity_main);
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);

                drawer = findViewById(R.id.drawer_layout);
                NavigationView navigationView = findViewById(R.id.nav_view);
                navigationView.setNavigationItemSelectedListener(this);

                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                        R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                drawer.addDrawerListener(toggle);
                toggle.syncState();
*/
            //}

        });

        }

    public void carregarTelaInicial() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_sobre);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //navigationView.setCheckedItem(R.id.nav_sobre);



    }


    //metodo que carrega a tela de cadastro do app, que so ocorre se o CarregarTelaPrincipal for executado
    public void CarregarTelaCadastro() {
        setContentView(R.layout.activity_tela_cadastro);
        ednome = (EditText) findViewById(R.id.ednome);
        edendereco = (EditText) findViewById(R.id.edendereco);
        edavaliacao = (EditText) findViewById(R.id.edavaliacao);
        btcadastrar = (Button) findViewById(R.id.btcadastrar);
        btcancelar = (Button) findViewById(R.id.btcancelar);
        //acesso ao banco
        dados = db.query("cadcervej",(new String[] {"nome", "endereco", "avaliacao"}), null, null, null, null, null);
        numero_registros = dados.getCount();
        btcadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try{
                    String nome = ednome.getText().toString();
                    String endereco = edendereco.getText().toString();
                    String avaliacao = edavaliacao.getText().toString();
                    //insercao dos dados dentro do banco
                    numero_registros++;
                    db.execSQL("insert into cadcervej values(" + String.valueOf(numero_registros) +",'" + nome + "','" + endereco + "'," + avaliacao + ")");
                    ExibirMensagem("Registro cadastro com sucesso");
                } catch(Exception e) {
                    ExibirMensagem("Erro ao cadastrar");
                }
                CarregarTelaPrincipal();
            }
        });
    }

    //metodo que carrega a tela com a pesquisa de todos os registros encontrados no banco
    public void CarregarTelaListagemPessoas()
    {

        try {
            dados = db.query("cadcervej", (new String[]{"nome", "endereco", "avaliacao"}), null, null, null, null, null);
            numero_registros = dados.getCount();
            posicao = 1;
        }catch(Exception e) {
            ExibirMensagem("Erro ao obter dados do banco");
            CarregarTelaPrincipal();
            return;
        }
        // se nao houver registros a serem mostrados, mostra-se a mensagene carrega-se a tela principal novamente
        if(numero_registros == 0)
        {
            ExibirMensagem("Numero registro cadastrado");
            CarregarTelaPrincipal();
            return;
        }
        else {
            //carrega a tela de pesqueisa e mostra os dados encontrados dentro do banco
            setContentView(R.layout.activity_lista_pessoas_cadastradas);
            btvoltar = (Button) findViewById(R.id.btvoltar);
            btavancar = (Button) findViewById(R.id.btavancar);
            btmenu_principal = (Button) findViewById(R.id.btmenu_principal);
            txtnome = (TextView) findViewById(R.id.txtnome);
            txtendereco = (TextView) findViewById(R.id.txtendereco);
            txtavaliacao = (TextView) findViewById(R.id.txtavaliacao);

            dados.moveToFirst();

            txtnome.setText(dados.getString(dados.getColumnIndex("nome")));
            txtendereco.setText(dados.getString(dados.getColumnIndex("endereco")));
            txtavaliacao.setText(dados.getString(dados.getColumnIndex("avaliacao")));
            btvoltar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (posicao == 1)
                        return;
                    posicao--;
                    dados.moveToPrevious();
                    txtnome.setText(dados.getString(dados.getColumnIndex("nome")));
                    txtendereco.setText(dados.getString(dados.getColumnIndex("endereco")));
                    txtavaliacao.setText(dados.getString(dados.getColumnIndex("avaliacao")));
                }
            });

            btavancar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (posicao == numero_registros)
                        return;
                    posicao++;
                    dados.moveToNext();
                    txtnome.setText(dados.getString(dados.getColumnIndex("nome")));
                    txtendereco.setText(dados.getString(dados.getColumnIndex("endereco")));
                    txtavaliacao.setText(dados.getString(dados.getColumnIndex("avaliacao")));

                }
            });
            btmenu_principal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick (View v){
                    CarregarTelaPrincipal();
                }
            });
        }
    }
    //metodo que exibe mensagem na tela
    public void ExibirMensagem(String mensagem){
        AlertDialog.Builder dialogo=new AlertDialog.Builder(AplicacaoCadastroActivity.this);
        dialogo.setTitle("Aviso");
        dialogo.setMessage(mensagem);
        dialogo.setNeutralButton("OK",null);
        dialogo.show();
    }

}

