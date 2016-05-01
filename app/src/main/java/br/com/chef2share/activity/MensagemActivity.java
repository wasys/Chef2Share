package br.com.chef2share.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.utils.lib.utils.StringUtils;
import com.android.volley.VolleyError;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import br.com.chef2share.R;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.adapter.MensagemRecyclerViewAdapter;
import br.com.chef2share.adapter.PlaceArrayAdapter;
import br.com.chef2share.domain.Mensagem;
import br.com.chef2share.domain.MenuPerfil;
import br.com.chef2share.domain.Response;
import br.com.chef2share.domain.listener.OnClickFotoLocal;
import br.com.chef2share.domain.service.SuperService;
import br.com.chef2share.infra.Transacao;

@EActivity(R.layout.activity_mensagem)
public class MensagemActivity extends SuperActivityMainMenu {

    @ViewById public TextView txtTitulo;
    @ViewById public TextView txtSubTitulo;
    @ViewById public ImageView btnClearFiltro;
    @ViewById public LinearLayout layoutNenhumRegistro;
    @ViewById public AutoCompleteTextView autoCompletePesquisaConvidado;

    @ViewById public RecyclerView recyclerViewMensagens;
    private LinearLayoutManager layoutManager;
    private MensagemRecyclerViewAdapter adapter;

    private List<Mensagem> list;

    @Override
    public void init() {
        super.init();

        setVisibilityBotaoVoltar(View.VISIBLE);
        txtSubTitulo.setVisibility(View.GONE);

        layoutNenhumRegistro.setVisibility(View.VISIBLE);

        setTextString(txtTitulo, getString(R.string.titulo_mensagens));

        recyclerViewMensagens.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getBaseContext());
        recyclerViewMensagens.setLayoutManager(layoutManager);

        adapter = new MensagemRecyclerViewAdapter(getBaseContext(), true);
        recyclerViewMensagens.setAdapter(adapter);

        autoCompletePesquisaConvidado.setThreshold(1);
        autoCompletePesquisaConvidado.setOnItemClickListener(onClickOpcaoAutocomplete());
    }

    @Override
    protected void onResume() {
        super.onResume();
        doInBackground(transacaoLoadMensagens(), true, R.string.msg_aguarde_carregando_mensagens, false);
    }

    private AdapterView.OnItemClickListener onClickOpcaoAutocomplete() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String)autoCompletePesquisaConvidado.getAdapter().getItem(position);

                List<Mensagem> match = new ArrayList<Mensagem>();
                for (Mensagem msg : list){
                    if(StringUtils.equals(msg.getRemetente().getNome(), item)){
                        match.add(msg);
                    }
                }
                adapter.refresh(match);
                btnClearFiltro.setVisibility(View.VISIBLE);
            }
        };
    }

    private Transacao transacaoLoadMensagens() {
        return new Transacao() {
            @Override
            public void onError(String msgErro) {
                layoutNenhumRegistro.setVisibility(View.VISIBLE);
                SuperUtil.alertDialog(activity, msgErro, new Runnable() {
                    @Override
                    public void run() {
                        activity.finish();
                    }
                });
            }

            @Override
            public void onSuccess(Response response) {

                if(response.getResult() == null || response.getResult().getMensagens() == null || response.getResult().getMensagens().size() <= 0) {
                    layoutNenhumRegistro.setVisibility(View.VISIBLE);
                    return;
                }

                layoutNenhumRegistro.setVisibility(View.GONE);
                adapter.refresh(response.getResult().getMensagens());

                list = response.getResult().getMensagens();
                String[] nomes = new String[list.size()];
                for(int i = 0; i < list.size(); i++){
                    nomes[i] = list.get(i).getRemetente().getNome();
                }
                autoCompletePesquisaConvidado.setAdapter(new ArrayAdapter<String>(getBaseContext(), R.layout.item_spinner_drodown, nomes));

            }

            @Override
            public void execute() throws Exception {
                superService.sendRequest(getBaseContext(), superActivity, superActivity, SuperService.TipoTransacao.CONSULTA_MENSAGEM, null);
            }
        };
    }

    @Click(R.id.btnClearFiltro)
    public void onClickClearFiltro(View v){
        btnClearFiltro.setVisibility(View.GONE);
        autoCompletePesquisaConvidado.setText("");
        adapter.refresh(list);
    }
}
