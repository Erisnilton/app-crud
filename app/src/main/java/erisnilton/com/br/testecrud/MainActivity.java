package erisnilton.com.br.testecrud;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import erisnilton.com.br.testecrud.adapter.ClienteAdapter;
import erisnilton.com.br.testecrud.dao.ClienteDao;
import erisnilton.com.br.testecrud.model.Cliente;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private ClienteAdapter adapter;
    private Cliente clienteEditado = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        if (intent.hasExtra("cliente")) {
            findViewById(R.id.includemain).setVisibility(View.INVISIBLE);
            findViewById(R.id.cadastromain).setVisibility(View.VISIBLE);
            findViewById(R.id.fab).setVisibility(View.INVISIBLE);
            clienteEditado = (Cliente) intent.getSerializableExtra("cliente");
            EditText nome = findViewById(R.id.txtNome);
            Spinner estado = findViewById(R.id.spnEstado);
            CheckBox vip = findViewById(R.id.chkVip);

            nome.setText(clienteEditado.getNome());
            vip.setChecked(clienteEditado.getVip());
            estado.setSelection(getIndex(estado, clienteEditado.getUf()));
            if (clienteEditado.getSexo() != null) {
                RadioButton rb;
                if (clienteEditado.getSexo().equals("M"))
                    rb = findViewById(R.id.rbMasculino);
                else
                    rb =    findViewById(R.id.rbFeminino);
                rb.setChecked(true);
            }
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

        Button cancelar = findViewById(R.id.btnCancelar);
        cancelar.setOnClickListener(this);

        Button salvar = findViewById(R.id.btnSalvar);
        salvar.setOnClickListener(this);

        recyclerConfig();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_addCliente) {
            ocutaMainAcitivity(View.INVISIBLE, View.VISIBLE);
            return true;
        }
        if(id == R.id.action_equipe){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Equipe:")
                    .setMessage("Erisnilton ,Alcy , Jefersson , Cícero ")
                    .setPositiveButton("OK",null)
                    .create()
                    .show();

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab: {
                ocutaMainAcitivity(v.INVISIBLE, v.VISIBLE);
                break;
            }

            case R.id.btnCancelar: {
                ocultaCadastroMain(v.VISIBLE, v.INVISIBLE);
                break;
            }

            case R.id.btnSalvar: {
                //carregando os campos
                EditText txtNome = findViewById(R.id.txtNome);
                Spinner spnEstado = findViewById(R.id.spnEstado);
                RadioGroup rgSexo = findViewById(R.id.rgSexo);
                CheckBox chkVip = findViewById(R.id.chkVip);

                //pegando os valores
                String nome = txtNome.getText().toString();
                String uf = spnEstado.getSelectedItem().toString();
                boolean vip = chkVip.isChecked();
                String sexo = rgSexo.getCheckedRadioButtonId() == R.id.rbMasculino ? "M" : "F";

                //salvando os dados
                ClienteDao dao = new ClienteDao(getBaseContext());
                boolean sucesso;
                if (clienteEditado != null) {
                    sucesso = dao.salvar(clienteEditado.getId(), nome, sexo, uf, vip);
                } else {
                    sucesso = dao.salvar(nome, sexo, uf, vip);
                }

                if (sucesso) {
                    Cliente cliente = dao.getUltimoCliente();
                    if (clienteEditado != null) {
                        adapter.atualizarCliente(cliente);
                        clienteEditado = null;
                    } else {
                        adapter.adicionaCliente(cliente);
                    }


                    //limpa os campos
                    txtNome.setText("");
                    rgSexo.setSelected(false);
                    spnEstado.setSelection(0);
                    chkVip.setChecked(false);

                    Snackbar.make(v, "Salvo com sucesso!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    ocultaCadastroMain(v.VISIBLE, v.INVISIBLE);
                } else {
                    Snackbar.make(v, "Erro ao salvar, consulte os logs!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        }

    }

    private int getIndex(Spinner spinner, String s) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(s)) {
                index = i;
                break;
            }
        }
        return index;
    }

    private void ocultaCadastroMain(int visible, int invisible) {
        findViewById(R.id.includemain).setVisibility(visible);
        findViewById(R.id.cadastromain).setVisibility(invisible);
        findViewById(R.id.fab).setVisibility(visible);
    }

    private void ocutaMainAcitivity(int invisible, int visible) {
        findViewById(R.id.includemain).setVisibility(invisible);
        findViewById(R.id.cadastromain).setVisibility(visible);
        findViewById(R.id.fab).setVisibility(invisible);
    }

    private void recyclerConfig() {
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        ClienteDao dao = new ClienteDao(this);
        adapter = new ClienteAdapter(dao.getTodos());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }
}
