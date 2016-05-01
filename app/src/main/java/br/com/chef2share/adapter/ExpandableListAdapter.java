package br.com.chef2share.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import br.com.chef2share.R;
import br.com.chef2share.domain.QRCode;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listaHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<QRCode>> hashFilhosByTitulo;
    private LayoutInflater inflater;
    private int height;

    public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<QRCode>> listChildData) {
        this.context = context;
        this.listaHeader = listDataHeader;
        this.hashFilhosByTitulo = listChildData;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.hashFilhosByTitulo.get(this.listaHeader.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final QRCode qrcode  = (QRCode) getChild(groupPosition, childPosition);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.ingresso_item, null);
        }

        TextView txtCodigoConvite = (TextView) convertView.findViewById(R.id.txtCodigoConvite);
        TextView txtNomeConvite = (TextView) convertView.findViewById(R.id.txtNomeConvite);
        ImageView imgQRCodeConvite = (ImageView) convertView.findViewById(R.id.imgQRCodeConvite);
        final ProgressBar progressImgQRCode = (ProgressBar) convertView.findViewById(R.id.progressImgQRCode);

        imgQRCodeConvite.getLayoutParams().height = 800;
        imgQRCodeConvite.getLayoutParams().width = 800;

        txtCodigoConvite.setText("#" + qrcode.getText());
        Picasso.with(context).load(qrcode.getUrl()).resize(800, 800).centerCrop().into(imgQRCodeConvite, new Callback() {
            @Override
            public void onSuccess() {
                progressImgQRCode.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                progressImgQRCode.setVisibility(View.GONE);
            }
        });

        height+= convertView.getHeight();

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.hashFilhosByTitulo.get(this.listaHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listaHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listaHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.titulo_lista_expandida, null);
        }

        TextView txtTituloConvite = (TextView) convertView.findViewById(R.id.txtTituloConvite);
        txtTituloConvite.setText(headerTitle);

        height+= convertView.getHeight();

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public int getHeight() {
        return height;
    }
}