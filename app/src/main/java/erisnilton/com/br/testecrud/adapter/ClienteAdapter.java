package erisnilton.com.br.testecrud.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import java.util.List;

import erisnilton.com.br.testecrud.model.Cliente;
import erisnilton.com.br.testecrud.R;
import erisnilton.com.br.testecrud.dao.ClienteDao;
import erisnilton.com.br.testecrud.holder.ClienteHolder;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteHolder> {

    private final List<Cliente> clientes;

    public ClienteAdapter(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    public void adicionaCliente(Cliente cliente) {
        clientes.add(cliente);
        notifyItemInserted(getItemCount());

    }

    public void atualizarCliente(Cliente cliente) {
        clientes.set(clientes.indexOf(cliente), cliente);
        notifyItemChanged(clientes.indexOf(cliente));
    }

    public void removerCliente(Cliente cliente) {
        int posicao = clientes.indexOf(cliente);
        clientes.remove(posicao);
        notifyItemRemoved(posicao);

    }

    @NonNull
    @Override
    public ClienteHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ClienteHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ClienteHolder clienteHolder, int position) {
        clienteHolder.nomeCliente.setText(clientes.get(position).getNome());
        final Cliente cliente = clientes.get(position);
        clienteHolder.editar.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity(v);
                Intent intent = activity.getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("cliente", cliente);
                activity.finish();
                activity.startActivity(intent);
                Log.i("cliente", "Cliente: " + cliente.getNome());
            }
        });

        clienteHolder.excluir.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view =  v;
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Confirmação")
                        .setMessage("Tem certeza que deseja excluir o cliente " + cliente.getNome())
                        .setPositiveButton("Entendi!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int index = clientes.indexOf(cliente);
                                Cliente clienteParaSerExcluido = clientes.get(index);
                                ClienteDao dao = new ClienteDao(view.getContext());
                                boolean sucesso = dao.remover(cliente.getId());
                                if(sucesso) {
                                    removerCliente(cliente);
                                    Snackbar.make(view,"Cliente " + cliente.getNome() + " Excluido",Snackbar.LENGTH_LONG)
                                            .setAction("acao",null).show();
                                } else {
                                    Snackbar.make(view,"Erro ao Excluir o cliente",Snackbar.LENGTH_LONG)
                                            .setAction("acao",null).show();

                                }

                            }
                        }).setNegativeButton("Cancelar",null)
                        .create()
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return clientes != null ? clientes.size() : 0;
    }


    private Activity getActivity(View view) {

        Context context = view.getContext();

        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            } else {
                context = ((ContextWrapper) context).getBaseContext();
            }
        }
        return null;
    }

}


