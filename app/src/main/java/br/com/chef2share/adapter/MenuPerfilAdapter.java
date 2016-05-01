package br.com.chef2share.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.utils.lib.view.adapter.ListAdapter;

import java.util.List;

import br.com.chef2share.R;
import br.com.chef2share.domain.MenuPerfil;

/**
 * Created by Jonas on 17/11/2015.
 */
public class MenuPerfilAdapter extends ListAdapter<MenuPerfil>{


    public MenuPerfilAdapter(Context context, List<MenuPerfil> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.meu_perfil_list_item, null);
        ImageView imgIcone = (ImageView) view.findViewById(R.id.imgItem);
        TextView txtLabel = (TextView) view.findViewById(R.id.txtItem);

        MenuPerfil menu = getList().get(position);

        imgIcone.setImageResource(menu.getIcone());
        txtLabel.setText(menu.getLabel());

        return view;
    }

    public void refresh(List<MenuPerfil> listChef) {
        super.list = listChef;
        notifyDataSetChanged();
    }
}
