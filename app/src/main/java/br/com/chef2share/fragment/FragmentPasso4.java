package br.com.chef2share.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.utils.lib.infra.AppUtil;
import com.android.utils.lib.utils.ColorUtils;
import com.android.utils.lib.utils.StringUtils;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import br.com.chef2share.R;
import br.com.chef2share.SuperApplication;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.activity.TermosDeUsoActivity_;
import br.com.chef2share.adapter.ViewPagerAdapter;
import br.com.chef2share.domain.Evento;
import br.com.chef2share.domain.ImagemPasso;
import br.com.chef2share.domain.Passo2;
import br.com.chef2share.domain.Response;
import br.com.chef2share.domain.listener.OnClickPasso;
import br.com.chef2share.infra.SuperUtils;
import br.com.chef2share.infra.Transacao;
import br.com.chef2share.view.CirclePageIndicator;

/**
 *
 */
@EFragment(R.layout.fragment_criar_evento_passo_quatro)
public class FragmentPasso4 extends FragmentPasso implements OnClickPasso {

    @ViewById public LinearLayout conteudoPasso4;
    @ViewById public ImageView imgPassoOk;
    @ViewById public LinearLayout headerPasso;
    @ViewById public TextView txtIndicadorPasso;
    @ViewById public TextView txtTermosDeUso;
    @ViewById public Button btnPublicarEvento;
    @ViewById public Button btnDivulgarEvento;
    @ViewById public LinearLayout conteudoPendentePublicacao;

    @ViewById public ViewPager viewPagerFotosCardapio;
    @ViewById public CirclePageIndicator pageIndicatorFotosCardapio;
    private ViewPagerAdapter adapter;

    @Override
    public void init() {
        super.init();
        initPasso4(headerPasso, txtIndicadorPasso, this);

        conteudoPasso4.setVisibility(View.GONE);

        SuperUtil.setUnderlineTextView(txtTermosDeUso);

        viewPagerFotosCardapio.getLayoutParams().height = SuperUtils.getHeightImagemFundo(getContext());
        adapter = new ViewPagerAdapter(getFragmentManager());
        viewPagerFotosCardapio.setAdapter(adapter);

        Passo2 passo2 = SuperApplication.getInstance().getSuperCache().getEvento().getPasso2();
        criaViewPagerFotos(passo2);
    }

    @Override
    public void onResume() {
        super.onResume();
        setHeaderLayout();
    }

    private void setHeaderLayout() {
        if(SuperApplication.getSuperCache().getEvento().isPublicado()) {
            btnDivulgarEvento.setVisibility(View.VISIBLE);
            conteudoPendentePublicacao.setVisibility(View.GONE);
            conteudoPasso4.setVisibility(View.VISIBLE);

            onClick(Passo.PASSO_4);
            super.updateView(headerPasso, R.drawable.bt_criar_evento_on);
            super.updateView(txtIndicadorPasso, R.drawable.bg_indicador_passo_on);

        }else if(SuperApplication.getSuperCache().getEvento().isPasso(Passo.PASSO_4)){

            conteudoPendentePublicacao.setVisibility(View.VISIBLE);
            btnDivulgarEvento.setVisibility(View.GONE);

            onClick(Passo.PASSO_4);

            super.updateView(headerPasso, R.drawable.bt_criar_evento_on);
            super.updateView(txtIndicadorPasso, R.drawable.bg_indicador_passo_on);
        }
    }

    private void criaViewPagerFotos(Passo2 passo2) {
        List<Fragment> fragmentsFotosCardapio = getFragmentsFotoCardapio(passo2.getImagensPasso2());
        if (fragmentsFotosCardapio != null && fragmentsFotosCardapio.size() > 0) {
            adapter.refresh(fragmentsFotosCardapio);
            viewPagerFotosCardapio.setAdapter(adapter);

            viewPagerFotosCardapio.setVisibility(View.VISIBLE);
            pageIndicatorFotosCardapio.setViewPager(viewPagerFotosCardapio);

        }else{
            viewPagerFotosCardapio.setVisibility(View.GONE);
        }
    }

    private List<Fragment> getFragmentsFotoCardapio(List<ImagemPasso> list) {
        if(list != null && list.size() > 0) {
            List<Fragment> listFragment = new ArrayList<Fragment>();
            for (ImagemPasso img : list) {
                listFragment.add(FragmentViewPagerPasso4FotosCardapio.newInstance(img));
            }
            return listFragment;
        }
        return null;
    }

    @Click(R.id.headerPasso)
    public void onClickHeader(View v){
        onClickPasso(Passo.PASSO_4);
    }

    @Override
    public void onClick(Passo passo) {
        switch (passo){
            case PASSO_4:
                conteudoPasso4.setVisibility(View.VISIBLE);
                break;

            default:
                conteudoPasso4.setVisibility(View.GONE);
                break;
        }
    }

    @Click(R.id.btnPublicarEvento)
    public void onClickPublicarEvento(View v){
        SuperUtil.alertDialogConfirmacao(getSuperActivity(), getResources().getString(R.string.msg_confirma_publicar_evento), R.string.sim, new Runnable() {
            @Override
            public void run() {
                doInBackground(transacaoPublicarEvento(), true, R.string.msg_aguarde_publicando_evento, false);
            }
        }, R.string.nao, new Runnable() {
            @Override
            public void run() {
            }
        });
    }

    @Click(R.id.btnDivulgarEvento)
    public void onClickDivulgarEvento(View v){

        String eventoId = SuperApplication.getSuperCache().getEvento().getId();
        doInBackground(getTransacaoDetalhesEvento(eventoId), true, R.string.msg_aguarde_carregando_detalhes_evento, false);

    }
    @Click(R.id.txtTermosDeUso)
    public void onClickTermosDeUso(View v){

        AppUtil.show(getContext(), TermosDeUsoActivity_.class);

    }

    private Transacao getTransacaoDetalhesEvento(final String eventoId) {
        return new Transacao() {

            @Override
            public void execute() throws Exception {
                superService.detalhesEvento(getContext(), getSuperFragment(), getSuperFragment(), eventoId);
            }

            @Override
            public void onSuccess(Response response) {

//                SuperApplication.getSuperCache().setEvento(null);
//                SuperApplication.getSuperCache().setCadastro(null);
//
//                Bundle param = new Bundle();
//                param.putSerializable("eventoId", response.getDetalhe().getEvento().getId());
//                SuperUtil.show(getSuperActivity(), CriarEventoActivity_.class, param);

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = response.getDetalhe().getCompartilhe().getFacebook();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Chef2Share - " + response.getDetalhe().getPasso2().getTitulo());
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Compartilhe com..."));

//                getSuperActivity().finish();
            }

            @Override
            public void onError(String msgErro) {
                SuperUtil.alertDialog(getSuperActivity(), msgErro, new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        };
    }

    private Transacao transacaoPublicarEvento() {
        return new Transacao() {
            @Override
            public void onError(String msgErro) {
                SuperUtil.alertDialog(getSuperActivity(), msgErro);
            }

            @Override
            public void onSuccess(Response response) {
                SuperApplication.getSuperCache().setCadastro(response.getCadastro());
                SuperApplication.getSuperCache().setEvento(response.getCadastro().getEvento());
                setHeaderLayout();
            }

            @Override
            public void execute() throws Exception {
                String eventoId = SuperApplication.getSuperCache().getEvento().getId();
                superService.publicarEvento(getContext(), getSuperFragment(), getSuperFragment(), eventoId);
            }
        };
    }
}
