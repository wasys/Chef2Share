package br.com.chef2share.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.utils.lib.utils.DateUtils;
import com.android.utils.lib.utils.StringUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

import br.com.chef2share.R;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.activity.DetalhesMensagemActivity_;
import br.com.chef2share.domain.Mensagem;
import br.com.chef2share.infra.SuperCloudinery;
import br.com.chef2share.view.RoundedImageView;

/**
 *
 */
public class MensagemRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Mensagem> mensagens;
    private final boolean msgSingleLine;

    public MensagemRecyclerViewAdapter(Context context, boolean singleLine) {
        this.context = context;
        this.msgSingleLine = singleLine;
    }

    public class ViewHolderMensagem extends RecyclerView.ViewHolder {

        public TextView txtNomeConvidado;
        public TextView txtMensagem;
        public RoundedImageView imgConvidado;
        public ProgressBar progressImgConvidado;
        public Mensagem mensagem;

        public void setMensagem(Mensagem mensagem) {
            this.mensagem = mensagem;
        }

        public ViewHolderMensagem(View view) {
            super(view);

            txtNomeConvidado = (TextView) view.findViewById(R.id.txtNomeConvidado);
            txtMensagem = (TextView) view.findViewById(R.id.txtMensagem);
            imgConvidado = (RoundedImageView) view.findViewById(R.id.imgConvidado);
            progressImgConvidado = (ProgressBar) view.findViewById(R.id.progressImgConvidado);

            txtMensagem.setSingleLine(msgSingleLine);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle param = new Bundle();
                    param.putSerializable("msg", mensagem);
                    SuperUtil.show(context, DetalhesMensagemActivity_.class, param);

                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.item_mensagem, parent, false);
        return new ViewHolderMensagem(row);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        Mensagem mensagem = mensagens.get(position);
        final ViewHolderMensagem viewHolderMensagem = (ViewHolderMensagem) holder;
        viewHolderMensagem.setMensagem(mensagem);

        viewHolderMensagem.txtNomeConvidado.setText(msgSingleLine ? mensagem.getRemetente().getNome() : mensagem.getDataString());
        viewHolderMensagem.txtMensagem.setText(mensagem.getConteudo());
        if(!mensagem.isLida()){
            viewHolderMensagem.txtMensagem.setTypeface(null, Typeface.BOLD);
            viewHolderMensagem.txtNomeConvidado.setTypeface(null, Typeface.BOLD);
        }

        String urlImgChef = SuperCloudinery.getUrlImgPessoa(context, mensagem.getRemetente().getImagem());

        if(StringUtils.isNotEmpty(urlImgChef)) {
            Picasso.with(context).load(urlImgChef).placeholder(R.drawable.userpic).error(R.drawable.userpic).into(viewHolderMensagem.imgConvidado, new Callback() {
                @Override
                public void onSuccess() {
                    viewHolderMensagem.progressImgConvidado.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    viewHolderMensagem.progressImgConvidado.setVisibility(View.GONE);
                }
            });
        }else{
            viewHolderMensagem.imgConvidado.setImageResource(R.drawable.userpic);
            viewHolderMensagem.progressImgConvidado.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mensagens != null ? mensagens.size() : 0;
    }

    public void refresh(List<Mensagem> mensagens){
        this.mensagens = mensagens;
        notifyDataSetChanged();
    }
}