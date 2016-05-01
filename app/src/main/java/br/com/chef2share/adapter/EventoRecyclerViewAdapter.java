package br.com.chef2share.adapter;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.android.utils.lib.utils.DateUtils;
import com.android.utils.lib.utils.StringUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.chef2share.R;
import br.com.chef2share.SuperApplication;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.activity.BuscaEventosActivity;
import br.com.chef2share.activity.DetalheEventoActivity_;
import br.com.chef2share.domain.BuscaVO;
import br.com.chef2share.domain.Chef;
import br.com.chef2share.domain.Cozinha;
import br.com.chef2share.domain.Evento;
import br.com.chef2share.domain.ImagemPasso;
import br.com.chef2share.domain.Passo1;
import br.com.chef2share.domain.Passo2;
import br.com.chef2share.domain.Passo3;
import br.com.chef2share.domain.TipoEvento;
import br.com.chef2share.domain.Valor;
import br.com.chef2share.domain.listener.OnSelectedBuscaFiltroOptions;
import br.com.chef2share.infra.SuperCloudinery;
import br.com.chef2share.infra.SuperGeocoder;
import br.com.chef2share.infra.SuperUtils;
import br.com.chef2share.view.LinearLayoutAnimator;
import br.com.chef2share.view.RoundedImageView;

/**
 * Created by Jonas on 21/11/2015.
 */
public class EventoRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int FILTROS = 0;
    private static final int EVENTO = 1;

    private BuscaEventosActivity context;
    private BuscaVO buscaVo;
    private OnSelectedBuscaFiltroOptions onSelectedBuscaFiltroOptionslistener;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private GoogleApiClient mGoogleApiClient;

//    private static final LatLngBounds BOUNDS_CURITIBA = new LatLngBounds(new LatLng(-25.4275, -49.2651), new LatLng(-25.4275, -49.2651));

    // Provide a suitable constructor (depends on the kind of dataset)
    public EventoRecyclerViewAdapter(BuscaEventosActivity context, OnSelectedBuscaFiltroOptions onSelectedBuscaFiltroOptionslistener) {
        this.context = context;
        this.onSelectedBuscaFiltroOptionslistener = onSelectedBuscaFiltroOptionslistener;
        this.mPlaceArrayAdapter = new PlaceArrayAdapter(context, R.layout.item_spinner_drodown, /*BOUNDS_CURITIBA*/ null, null);
        mGoogleApiClient = new GoogleApiClient
                .Builder(context)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        mPlaceArrayAdapter.setGoogleApiClient(null);
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {

                    }
                })
                .build();
        mGoogleApiClient.connect();
    }

    public void refresh(BuscaVO buscaVo){
        this.buscaVo = buscaVo;
        notifyDataSetChanged();

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

        public AutoCompleteTextView autoCompleteTxtLocal;
        public TableLayout tableLayoutFiltros;
        public Spinner spinnerTipoEvento;
        public Spinner spinnerTipoCozinha;
        public Spinner spinnerValorMaximo;
        public TextView txtDataInicio;
        public TextView txtDataFinal;
        public LinearLayout layoutDataInicio;
        public LinearLayout layoutDataFinal;
        public Button btnMostrarFiltrosOuSalvarFiltros;
        public LinearLayoutAnimator layoutMostrarFiltros;
        private DatePickerDialog datePickerDataInicial;
        private DatePickerDialog datePickerDataFinal;
        public TextView txtInfoQtdeEventos;

        public ViewHolderPesquisa(View view) {
            super(view);

            tableLayoutFiltros = (TableLayout) view.findViewById(R.id.tableLayoutFiltros);
            spinnerTipoEvento = (Spinner) view.findViewById(R.id.spinnerTipoEvento);
            spinnerTipoCozinha = (Spinner) view.findViewById(R.id.spinnerTipoCozinha);
            spinnerValorMaximo = (Spinner) view.findViewById(R.id.spinnerValorMaximo);
            txtDataInicio = (TextView) view.findViewById(R.id.txtDataInicio);
            txtDataFinal = (TextView) view.findViewById(R.id.txtDataFinal);
            layoutDataInicio = (LinearLayout) view.findViewById(R.id.layoutDataInicio);
            layoutDataFinal = (LinearLayout) view.findViewById(R.id.layoutDataFinal);
            btnMostrarFiltrosOuSalvarFiltros = (Button) view.findViewById(R.id.btnMostrarFiltrosOuSalvarFiltros);
            layoutMostrarFiltros = (LinearLayoutAnimator) view.findViewById(R.id.layoutMostrarFiltros);
            autoCompleteTxtLocal = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTxtLocal);
            txtInfoQtdeEventos = (TextView) view.findViewById(R.id.txtInfoQtdeEventos);

            autoCompleteTxtLocal.setThreshold(3);
            autoCompleteTxtLocal.setAdapter(mPlaceArrayAdapter);
            autoCompleteTxtLocal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
                    final String placeId = String.valueOf(item.placeId);
                    PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
                    placeResult.setResultCallback(getResultCallback(ViewHolderPesquisa.this));


                }
            });

            switch (SuperApplication.getInstance().getSuperCache().getPesquisaEvento().btnStringFiltro){
                case R.string.btn_mostrar_filtros:
                    tableLayoutFiltros.setVisibility(View.GONE);
                    btnMostrarFiltrosOuSalvarFiltros.setText(R.string.btn_mostrar_filtros);
                    break;
                case R.string.btn_salvar_filtros:
                    tableLayoutFiltros.setVisibility(View.VISIBLE);
                    btnMostrarFiltrosOuSalvarFiltros.setText(R.string.btn_salvar_filtros);
                    break;
            }

            btnMostrarFiltrosOuSalvarFiltros.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button button = (Button) v;
                    if (StringUtils.equals(button.getText().toString(), context.getResources().getString(R.string.btn_salvar_filtros).toString())) {

                        SuperApplication.getInstance().getSuperCache().getPesquisaEvento().btnStringFiltro = R.string.btn_mostrar_filtros;

                        tableLayoutFiltros.setVisibility(View.GONE);
                        btnMostrarFiltrosOuSalvarFiltros.setText(R.string.btn_mostrar_filtros);

                        Editable text = autoCompleteTxtLocal.getText();
                        if (text != null) {
                            if (StringUtils.isEmpty(text.toString())) {
                                onSelectedBuscaFiltroOptionslistener.onSelectedEndereco(null, null, null, null);
                            }
                        }


                    } else if (StringUtils.equals(button.getText().toString(), context.getResources().getString(R.string.btn_mostrar_filtros).toString())) {
                        tableLayoutFiltros.setVisibility(View.VISIBLE);
                        SuperApplication.getInstance().getSuperCache().getPesquisaEvento().btnStringFiltro = R.string.btn_salvar_filtros;
                        btnMostrarFiltrosOuSalvarFiltros.setText(R.string.btn_salvar_filtros);
                    }
                }
            });

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

            spinnerTipoEvento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    onSelectedBuscaFiltroOptionslistener.onSelectTipoEvento((TipoEvento) spinnerTipoEvento.getAdapter().getItem(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            spinnerTipoCozinha.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    onSelectedBuscaFiltroOptionslistener.onSelectTipoCozinha((Cozinha) spinnerTipoCozinha.getAdapter().getItem(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            spinnerValorMaximo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    onSelectedBuscaFiltroOptionslistener.onSelectValorMaximo((Valor) spinnerValorMaximo.getAdapter().getItem(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }

    }

    private ResultCallback<? super PlaceBuffer> getResultCallback(final ViewHolderPesquisa viewHolderPesquisa) {
        return new ResultCallback<PlaceBuffer>() {
            @Override
            public void onResult(PlaceBuffer places) {
                if (!places.getStatus().isSuccess()) {
                    return;
                }
                // Selecting the first object buffer.
                final Place place = places.get(0);
                viewHolderPesquisa.autoCompleteTxtLocal.setText(place.getAddress());
                viewHolderPesquisa.autoCompleteTxtLocal.dismissDropDown();

                SuperGeocoder superGeocoder = new SuperGeocoder(context, place);
                onSelectedBuscaFiltroOptionslistener.onSelectedEndereco(place.getAddress().toString(), superGeocoder.getEstado(), superGeocoder.getCidade(), superGeocoder.getBairro());
            }
        };
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolderEvento extends RecyclerView.ViewHolder {

        private Evento evento;

        public ImageView imgBackground;
        public TextView txtTituloEvento;
        public RoundedImageView imgChef;
        public TextView txtNomeChef;
        public RatingBar ratingAvaliacaoChef;
        public ProgressBar progressImgChef;
        public TextView txtQtdeComentarios;
        public TextView txtHorarioEventoDe;
        public TextView txtHorarioEventoAte;
        public TextView txtValorEvento;
        public ProgressBar progressImgBackgroundEvento;
        public TextView txtDataEventoE;
        public TextView txtDataEventoDDMM;
        public TextView txtLocalEventoCidade;
        public TextView txtLocalEventoBairro;

        public ViewHolderEvento(View v) {
            super(v);

            imgBackground = (ImageView) v.findViewById(R.id.imgBackground);
            txtTituloEvento = (TextView) v.findViewById(R.id.txtTituloEvento);
            txtDataEventoE = (TextView) v.findViewById(R.id.txtDataEventoE);
            txtDataEventoDDMM = (TextView) v.findViewById(R.id.txtDataEventoDDMM);
            txtHorarioEventoDe = (TextView) v.findViewById(R.id.txtHorarioEventoDe);
            txtHorarioEventoAte = (TextView) v.findViewById(R.id.txtHorarioEventoAte);
            txtLocalEventoCidade = (TextView) v.findViewById(R.id.txtLocalEventoCidade);
            txtLocalEventoBairro = (TextView) v.findViewById(R.id.txtLocalEventoBairro);
            txtValorEvento = (TextView) v.findViewById(R.id.txtValorEvento);
            progressImgBackgroundEvento = (ProgressBar) v.findViewById(R.id.progressImgBackgroundEvento);

            imgChef = (RoundedImageView) v.findViewById(R.id.imgChef);
            txtNomeChef = (TextView) v.findViewById(R.id.txtNomeChef);
            ratingAvaliacaoChef = (RatingBar) v.findViewById(R.id.ratingAvaliacaoChef);
            txtQtdeComentarios = (TextView) v.findViewById(R.id.txtQtdeComentarios);
            progressImgChef = (ProgressBar) v.findViewById(R.id.progressImgChef);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle params = new Bundle();
                    params.putString("eventoId", getEvento().getId());
                    SuperUtil.show(v.getContext(), DetalheEventoActivity_.class, params);
                }
            });
        }

        public void setEvento(final Evento evento) {
            this.evento = evento;
        }

        public Evento getEvento() {
            return evento;
        }
    }


    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case FILTROS: {
                View row = inflater.inflate(R.layout.item_filtro_busca_eventos, parent, false);
                return new ViewHolderPesquisa(row);
            }

            case EVENTO: {
                View row = inflater.inflate(R.layout.busca_evento_list_item, parent, false);
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

            viewHolderPesquisa.txtInfoQtdeEventos.setText(context.getResources().getText(R.string.evento_quantidade_localizados, String.valueOf(getItemCount() - 1)));

            final ArrayAdapter adapterTipoEvento = new ArrayAdapter<TipoEvento>(context, R.layout.item_spinner, buscaVo.getFiltro().getTipos());
            adapterTipoEvento.setDropDownViewResource(R.layout.item_spinner_drodown);
            viewHolderPesquisa.spinnerTipoEvento.setAdapter(adapterTipoEvento);

            /**
             * Valores selecionados anteriormente
             */
            if (SuperApplication.getSuperCache().getPesquisaEvento().tipoEvento != null) {
                viewHolderPesquisa.spinnerTipoEvento.setSelection(buscaVo.getFiltro().getIdxTipoEvento(SuperApplication.getSuperCache().getPesquisaEvento().tipoEvento.getValue()));
            }

            final ArrayAdapter adapterTipoCozinha = new ArrayAdapter<Cozinha>(context, R.layout.item_spinner, buscaVo.getFiltro().getCozinhas());
            adapterTipoCozinha.setDropDownViewResource(R.layout.item_spinner_drodown);
            viewHolderPesquisa.spinnerTipoCozinha.setAdapter(adapterTipoCozinha);

            final ArrayAdapter adapterValores = new ArrayAdapter<Valor>(context, R.layout.item_spinner, buscaVo.getFiltro().getValores());
            adapterValores.setDropDownViewResource(R.layout.item_spinner_drodown);
            viewHolderPesquisa.spinnerValorMaximo.setAdapter(adapterValores);

            /**
             * Valores selecionados anteriormente
             */
            if (SuperApplication.getSuperCache().getPesquisaEvento().valorMaximo != null) {
                viewHolderPesquisa.spinnerValorMaximo.setSelection(buscaVo.getFiltro().getIdxValor(SuperApplication.getSuperCache().getPesquisaEvento().valorMaximo.getValue()));
            }

            long dataMin = DateUtils.toDate(buscaVo.getFiltro().getPeriodo().getMin(), "yyyy-MM-dd").getTime();
            long dataMax = DateUtils.toDate(buscaVo.getFiltro().getPeriodo().getMax(), "yyyy-MM-dd").getTime();

            setDateTimeField(viewHolderPesquisa, dataMin, dataMax);

            /**
             * Valores selecionados anteriormente
             */
            if (StringUtils.isNotEmpty(SuperApplication.getSuperCache().getPesquisaEvento().enderecoSearch)){
                viewHolderPesquisa.autoCompleteTxtLocal.setText(SuperApplication.getSuperCache().getPesquisaEvento().enderecoSearch);
            }

        }else {

            final ViewHolderEvento viewHolderEvento = (ViewHolderEvento) holder;

            Evento evento = buscaVo.getEventos().get(position - 1);
            viewHolderEvento.setEvento(evento);

            Passo1 passo1 = evento.getPasso1();
            if (passo1 != null) {
                viewHolderEvento.txtLocalEventoCidade.setText(passo1.getCidade());
                viewHolderEvento.txtLocalEventoBairro.setText(passo1.getBairro());
            }

            Passo2 passo2 = evento.getPasso2();
            if (passo2 != null) {
                viewHolderEvento.txtTituloEvento.setText(passo2.getTitulo());

                if (passo2.getImagensPasso2() != null) {
                    ImagemPasso imagemPasso = passo2.getImagensPasso2().get(0);
                    if (imagemPasso != null) {
                        String url = SuperCloudinery.getUrl(context, imagemPasso.getImagem(), SuperUtils.getWidthImagemFundo(context), SuperUtils.getHeightImagemFundo(context));
                        if (StringUtils.isNotEmpty(url)) {
                            Picasso.with(context).load(url).resize(SuperUtils.getWidthImagemFundo(context), SuperUtils.getHeightImagemFundo(context)).centerCrop().into(viewHolderEvento.imgBackground, new Callback() {
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
                    }
                }
            }

            Passo3 passo3 = evento.getPasso3();
            if (passo3 != null) {
                viewHolderEvento.txtHorarioEventoDe.setText(passo3.getHoraInicio());
                viewHolderEvento.txtHorarioEventoAte.setText(passo3.getHoraTermino());
                viewHolderEvento.txtValorEvento.setText(passo3.getValorFormatado(false));
                viewHolderEvento.txtDataEventoE.setText(passo3.getDataEventoE());
                viewHolderEvento.txtDataEventoDDMM.setText(passo3.getDataEventoDDMM());
            }

            Chef chef = evento.getChef();
            if (chef != null) {
                viewHolderEvento.txtNomeChef.setText(chef.getNome());
                viewHolderEvento.ratingAvaliacaoChef.setRating((float) chef.getAvaliacaoMedia());
                viewHolderEvento.txtQtdeComentarios.setText(evento.getTotalComentarios());
            }

            String urlImgChef = SuperCloudinery.getUrlImgPessoa(context, chef.getImagem());
            if (StringUtils.isNotEmpty(urlImgChef)) {
                Picasso.with(context).load(urlImgChef).placeholder(R.drawable.userpic).error(R.drawable.userpic).into(viewHolderEvento.imgChef, new Callback() {
                    @Override
                    public void onSuccess() {
                        viewHolderEvento.progressImgChef.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        viewHolderEvento.progressImgChef.setVisibility(View.GONE);
                    }
                });
            } else {
                viewHolderEvento.imgChef.setImageResource(R.drawable.userpic);
                viewHolderEvento.progressImgChef.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public int getItemCount() {
        List<Evento> eventos = buscaVo.getEventos();
        if(eventos != null){
            return eventos.size() + 1;
        }

        return 1;
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

        viewHolderPesquisa.datePickerDataInicial = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

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
                onSelectedBuscaFiltroOptionslistener.onSelectedMin(DateUtils.toString(date, "yyyy-MM-dd"));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        viewHolderPesquisa.datePickerDataFinal = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {

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
                onSelectedBuscaFiltroOptionslistener.onSelectedMax(DateUtils.toString(date, "yyyy-MM-dd"));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        viewHolderPesquisa.datePickerDataInicial.getDatePicker().setMinDate(dataMin);
        viewHolderPesquisa.datePickerDataInicial.getDatePicker().setMaxDate(dataMax);

        viewHolderPesquisa.datePickerDataFinal.getDatePicker().setMinDate(dataMin);
        viewHolderPesquisa.datePickerDataFinal.getDatePicker().setMaxDate(dataMax);

        /**
         * Valores selecionados anteriormente
         */
        if(StringUtils.isNotEmpty(SuperApplication.getSuperCache().getPesquisaEvento().max)) {
            viewHolderPesquisa.txtDataFinal.setText(DateUtils.toString(DateUtils.toDate(SuperApplication.getSuperCache().getPesquisaEvento().max, "yyyy-MM-dd"), DateUtils.DATE));
        }
        if(StringUtils.isNotEmpty(SuperApplication.getSuperCache().getPesquisaEvento().min)) {
            viewHolderPesquisa.txtDataInicio.setText(DateUtils.toString(DateUtils.toDate(SuperApplication.getSuperCache().getPesquisaEvento().min, "yyyy-MM-dd"), DateUtils.DATE));
        }
    }

}