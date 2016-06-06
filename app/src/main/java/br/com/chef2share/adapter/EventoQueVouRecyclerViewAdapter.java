package br.com.chef2share.adapter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.utils.lib.utils.DateUtils;
import com.android.utils.lib.utils.StringUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.chef2share.R;
import br.com.chef2share.SuperApplication;
import br.com.chef2share.activity.EventosQueVouActivity;
import br.com.chef2share.domain.BuscaVO;
import br.com.chef2share.domain.Evento;
import br.com.chef2share.domain.Passo1;
import br.com.chef2share.domain.Passo2;
import br.com.chef2share.domain.Passo3;
import br.com.chef2share.domain.Pedido;
import br.com.chef2share.domain.UsuarioPedido;
import br.com.chef2share.domain.listener.OnSelectedBuscaFiltroEventosQueVouOptions;
import br.com.chef2share.domain.request.PedidoFiltro;
import br.com.chef2share.enums.StatusPedido;
import br.com.chef2share.infra.SuperCloudinery;
import br.com.chef2share.infra.SuperUtils;

/**
 *
 */
public class EventoQueVouRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int FILTROS = 0;
    private static final int EVENTO = 1;

    private static EventosQueVouActivity activity;
    private static UsuarioPedido usuarioPedido;
    private List<Pedido> pedidos;
    private OnSelectedBuscaFiltroEventosQueVouOptions onSelectedBuscaFiltroEventosQueVouOptions;
    private PedidoFiltro pedidoFiltro;

    // Provide a suitable constructor (depends on the kind of dataset)
    public EventoQueVouRecyclerViewAdapter(EventosQueVouActivity activity, OnSelectedBuscaFiltroEventosQueVouOptions onSelectedBuscaFiltroEventosQueVouOptions) {
        this.activity = activity;
        this.onSelectedBuscaFiltroEventosQueVouOptions = onSelectedBuscaFiltroEventosQueVouOptions;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position){
            //só o primeiro será BANNER
            case 0: return FILTROS;

            //o resto é EVENTO
            default: return EVENTO;
        }
    }

    public class ViewHolderPesquisa extends RecyclerView.ViewHolder {

        public TextView txtDataInicio;
        public TextView txtDataFinal;
        public TextView txtInfoQtdeEventos;
        public LinearLayout layoutDataInicio;
        public LinearLayout layoutDataFinal;
        public Button btnFiltrar;
        private DatePickerDialog datePickerDataInicial;
        private DatePickerDialog datePickerDataFinal;

        public ViewHolderPesquisa(View view) {
            super(view);

            txtDataInicio = (TextView) view.findViewById(R.id.txtDataInicio);
            txtDataFinal = (TextView) view.findViewById(R.id.txtDataFinal);
            txtInfoQtdeEventos = (TextView) view.findViewById(R.id.txtInfoQtdeEventos);
            layoutDataInicio = (LinearLayout) view.findViewById(R.id.layoutDataInicio);
            layoutDataFinal = (LinearLayout) view.findViewById(R.id.layoutDataFinal);
            btnFiltrar = (Button) view.findViewById(R.id.btnFiltrar);

            layoutDataInicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    datePickerDataInicial.show();
                }
            });

            layoutDataFinal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    datePickerDataFinal.show();
                }
            });

            btnFiltrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSelectedBuscaFiltroEventosQueVouOptions.filtrar();
                }
            });
        }

    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolderEvento extends RecyclerView.ViewHolder {

        public TextView txtLocalEvento;
        public TextView txtData;
        public LinearLayout layoutVerConvites;
        public Button btnVerConvites;
        public LinearLayout layoutPagarAgora;
        public Button btnPagarAgora;
        public LinearLayout addEventoCalendario;
        public LinearLayout layoutAvalie;
        public Button btnAvalie;
        public LinearLayout layoutEventoAvaliado;
        public LinearLayout layoutAguardandoPagamento;
        public ProgressBar progressImgBackgroundEvento;

        public ImageView imgBackground;
        public TextView txtTituloEvento;
        public Button btnDetalhes;
        public TextView txtNomeChef;
        public RatingBar ratingAvaliacaoEvento;

        public ViewHolderEvento(View v) {
            super(v);

            imgBackground = (ImageView) v.findViewById(R.id.imgBackground);
            txtTituloEvento = (TextView) v.findViewById(R.id.txtTituloEvento);
            btnDetalhes = (Button) v.findViewById(R.id.btnDetalhes);
            txtNomeChef = (TextView) v.findViewById(R.id.txtNomeChef);
            txtLocalEvento = (TextView) v.findViewById(R.id.txtLocalEvento);
            txtData = (TextView) v.findViewById(R.id.txtData);
            addEventoCalendario = (LinearLayout) v.findViewById(R.id.addEventoCalendario);

            layoutVerConvites = (LinearLayout) v.findViewById(R.id.layoutVerConvites);
            btnVerConvites = (Button) v.findViewById(R.id.btnVerConvites);

            layoutPagarAgora = (LinearLayout) v.findViewById(R.id.layoutPagarAgora);
            btnPagarAgora = (Button) v.findViewById(R.id.btnPagarAgora);

            layoutAvalie = (LinearLayout) v.findViewById(R.id.layoutAvalie);
            btnAvalie = (Button) v.findViewById(R.id.btnAvalie);

            layoutEventoAvaliado = (LinearLayout) v.findViewById(R.id.layoutEventoAvaliado);
            ratingAvaliacaoEvento = (RatingBar) v.findViewById(R.id.ratingAvaliacaoEvento);

            layoutAguardandoPagamento = (LinearLayout) v.findViewById(R.id.layoutAguardandoPagamento);

            progressImgBackgroundEvento = (ProgressBar) v.findViewById(R.id.progressImgBackgroundEvento);
        }
    }




    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case FILTROS: {
                View row = inflater.inflate(R.layout.item_filtro_busca_eventos_que_vou, parent, false);
                return new ViewHolderPesquisa(row);
            }

            case EVENTO: {
                View row = inflater.inflate(R.layout.evento_que_vou_list_item, parent, false);
                return new ViewHolderEvento(row);
            }
        }
        return null;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewHolderPesquisa) {
            final ViewHolderPesquisa viewHolderPesquisa = (ViewHolderPesquisa) holder;

            long dataMin = DateUtils.toDate(pedidoFiltro.getPeriodoDe(), "yyyy-MM-dd").getTime();
            long dataMax = DateUtils.toDate(pedidoFiltro.getPeriodoAte(), "yyyy-MM-dd").getTime();

            setDateTimeField(viewHolderPesquisa, dataMin, dataMax);

        }else {

            final ViewHolderEvento viewHolderEvento = (ViewHolderEvento) holder;

            Pedido pedido = pedidos.get(position - 1);
            viewHolderEvento.btnAvalie.setOnClickListener(onClickAvaliar(pedido));
            viewHolderEvento.btnDetalhes.setOnClickListener(onClickDetalhes(pedido));
            viewHolderEvento.btnPagarAgora.setOnClickListener(onClickPagarAgora(pedido));
            viewHolderEvento.imgBackground.setOnClickListener(onClickDetalhes(pedido));
            viewHolderEvento.txtTituloEvento.setOnClickListener(onClickDetalhes(pedido));
            viewHolderEvento.btnVerConvites.setOnClickListener(onClickDetalhes(pedido));
            viewHolderEvento.txtTituloEvento.setPaintFlags(viewHolderEvento.txtTituloEvento.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            viewHolderEvento.ratingAvaliacaoEvento.setRating((float) pedido.getEvento().getAvaliacaoMedia());
            viewHolderEvento.addEventoCalendario.setOnClickListener(onClickAddEventoCalendario(pedido));

            Evento evento = pedido.getEvento();
            Passo1 passo1 = evento.getPasso1();
            Passo2 passo2 = evento.getPasso2();
            Passo3 passo3 = evento.getPasso3();

            viewHolderEvento.layoutAguardandoPagamento.setVisibility(View.GONE);
            viewHolderEvento.layoutAvalie.setVisibility(View.GONE);
            viewHolderEvento.layoutEventoAvaliado.setVisibility(View.GONE);
            viewHolderEvento.layoutPagarAgora.setVisibility(View.GONE);
            viewHolderEvento.layoutVerConvites.setVisibility(View.GONE);

            switch (pedido.getStatus()) {
                case AGUARDANDO_PAGAMENTO:
                    viewHolderEvento.layoutAguardandoPagamento.setVisibility(View.VISIBLE);
                    break;
                case PAGO:
                    viewHolderEvento.txtData.setPaintFlags(viewHolderEvento.txtData.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    viewHolderEvento.layoutVerConvites.setVisibility(View.VISIBLE);
                    break;
                case REALIZADO:
                case DISPONIVEL:
                    if (pedido.getEvento().getAvaliacaoMedia() <= 0) {
                        viewHolderEvento.layoutAvalie.setVisibility(View.VISIBLE);
                    } else {
                        viewHolderEvento.layoutEventoAvaliado.setVisibility(View.VISIBLE);
                    }
                    break;
                case RESERVADO:
                    viewHolderEvento.layoutPagarAgora.setVisibility(View.VISIBLE);
                    break;
            }

            viewHolderEvento.imgBackground.getLayoutParams().width = SuperUtils.getWidthImagemFundoThumb(activity);
            viewHolderEvento.imgBackground.getLayoutParams().height = SuperUtils.getHeightImagemFundoThumb(activity);

            String url = SuperCloudinery.getUrl(activity, passo2.getImagemPrincipal(), SuperUtils.getWidthImagemFundoThumb(activity), SuperUtils.getHeightImagemFundoThumb(activity));
            if (StringUtils.isNotEmpty(url)) {
                Picasso.with(activity).load(url).resize(SuperUtils.getWidthImagemFundo(activity), SuperUtils.getHeightImagemFundo(activity)).centerCrop().into(viewHolderEvento.imgBackground, new Callback() {
                    @Override
                    public void onSuccess() {
                        viewHolderEvento.progressImgBackgroundEvento.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        viewHolderEvento.progressImgBackgroundEvento.setVisibility(View.GONE);
                    }
                });
            }

            viewHolderEvento.txtData.setText(passo3.getDataPorExtenso());
            viewHolderEvento.txtLocalEvento.setText(passo1.getEnderecoCompleto(pedido.getStatus() == StatusPedido.PAGO));
            viewHolderEvento.txtNomeChef.setText(evento.getChef().getNome());
            viewHolderEvento.txtTituloEvento.setText(passo2.getTitulo());
        }
    }

    private static View.OnClickListener onClickAddEventoCalendario(final Pedido pedido) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SuperUtils.addEventoCalendario(activity, pedido);
            }
        };
    }

    private static View.OnClickListener onClickAvaliar(final Pedido pedido) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onClickAvalie(usuarioPedido, pedido);
            }
        };
    }

    private static View.OnClickListener onClickPagarAgora(final Pedido pedido) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onClickPagarAgora(usuarioPedido, pedido);
            }
        };
    }

    private static View.OnClickListener onClickVerConvites(final Pedido pedido) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onClickVerConvites(usuarioPedido, pedido);
            }
        };
    }

    private static View.OnClickListener onClickDetalhes(final Pedido pedido) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onClickDetalhes(usuarioPedido, pedido);
            }
        };
    }

    private void setDateTimeField(final ViewHolderPesquisa viewHolderPesquisa, long dataMin, long dataMax) {

        Date dataMinima = new Date(dataMin);
        if(DateUtils.isIgual(dataMinima, new Date())){
            viewHolderPesquisa.txtDataInicio.setText("Hoje");
        }else{
            viewHolderPesquisa.txtDataInicio.setText(DateUtils.toString(dataMinima, DateUtils.DATE));
        }
        Date dataMaxima = new Date(dataMax);
        viewHolderPesquisa.txtDataFinal.setText(DateUtils.toString(dataMaxima, DateUtils.DATE));

        Calendar newCalendar = Calendar.getInstance();

        viewHolderPesquisa.datePickerDataInicial = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                Date date = newDate.getTime();

                if(DateUtils.isIgual(date, new Date())){
                    viewHolderPesquisa.txtDataInicio.setText("Hoje");
                }else{
                    viewHolderPesquisa.txtDataInicio.setText(DateUtils.toString(date, DateUtils.DATE));
                }

                /**
                 * Manda la pra activity
                 */
                onSelectedBuscaFiltroEventosQueVouOptions.onSelectedMin(DateUtils.toString(date, "yyyy-MM-dd"));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        viewHolderPesquisa.datePickerDataFinal = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                Date date = newDate.getTime();

                if(DateUtils.isIgual(date, new Date())){
                    viewHolderPesquisa.txtDataFinal.setText("Hoje");
                }else{
                    viewHolderPesquisa.txtDataFinal.setText(DateUtils.toString(date, DateUtils.DATE));
                }

                /**
                 * Manda la pra activity
                 */
                onSelectedBuscaFiltroEventosQueVouOptions.onSelectedMax(DateUtils.toString(date, "yyyy-MM-dd"));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Valores selecionados anteriormente
         */
        if(StringUtils.isNotEmpty(SuperApplication.getSuperCache().getPesquisaEventoQueVou().periodoDe)) {
            viewHolderPesquisa.txtDataInicio.setText(DateUtils.toString(DateUtils.toDate(SuperApplication.getSuperCache().getPesquisaEventoQueVou().periodoDe, "yyyy-MM-dd"), DateUtils.DATE));
        }
        if(StringUtils.isNotEmpty(SuperApplication.getSuperCache().getPesquisaEventoQueVou().periodoAte)) {
            viewHolderPesquisa.txtDataFinal.setText(DateUtils.toString(DateUtils.toDate(SuperApplication.getSuperCache().getPesquisaEventoQueVou().periodoAte, "yyyy-MM-dd"), DateUtils.DATE));
        }
    }

    @Override
    public int getItemCount() {
        if(pedidos != null){
            return pedidos.size() + 1;
        }

        return 1;
    }

    public void refresh(UsuarioPedido usuarioPedido, PedidoFiltro pedidoFiltro){
        this.usuarioPedido = usuarioPedido;
        this.pedidos = usuarioPedido.getPedidos();
        this.pedidoFiltro = pedidoFiltro;
        notifyDataSetChanged();

    }

}