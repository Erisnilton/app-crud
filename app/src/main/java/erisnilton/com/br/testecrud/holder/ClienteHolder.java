package erisnilton.com.br.testecrud.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import erisnilton.com.br.testecrud.R;

public class ClienteHolder extends RecyclerView.ViewHolder {
    public TextView nomeCliente;
    public ImageButton editar;
    public ImageButton excluir;

    public ClienteHolder(@NonNull View itemView) {
        super(itemView);
        nomeCliente = itemView.findViewById(R.id.nomeCliente);
        editar = itemView.findViewById(R.id.editar);
        excluir = itemView.findViewById(R.id.excluir);
    }
}
