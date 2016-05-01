package br.com.chef2share.domain;

import com.android.utils.lib.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import br.com.chef2share.enums.Genero;

/**
 * Created by Jonas on 17/11/2015.
 */
public class UsuarioPedido implements Serializable {

    private List<Pedido> pedidos;
    private Map<String, String> dicionario;

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public Map<String, String> getDicionario() {
        return dicionario;
    }

    public void setDicionario(Map<String, String> dicionario) {
        this.dicionario = dicionario;
    }
}

