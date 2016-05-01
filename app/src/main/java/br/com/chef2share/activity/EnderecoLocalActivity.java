package br.com.chef2share.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.utils.lib.utils.StringUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import br.com.chef2share.R;
import br.com.chef2share.SuperApplication;
import br.com.chef2share.SuperUtil;
import br.com.chef2share.adapter.PlaceArrayAdapter;
import br.com.chef2share.domain.Passo1;
import br.com.chef2share.infra.SuperGeocoder;

@EActivity(R.layout.activity_endereco_local)
public class EnderecoLocalActivity extends SuperActivityMainMenu {

    @ViewById
    public TextView txtTitulo;
    @ViewById
    public TextView txtSubTitulo;
    @ViewById
    public EditText txtLogradouro;
    @ViewById
    public EditText txtNumero;
    @ViewById
    public EditText txtCep;
    @ViewById
    public EditText txtBairro;
    @ViewById
    public EditText txtCidade;
    @ViewById
    public EditText txtEstado;

    @ViewById
    public Button btnSalvar;
    @ViewById
    public AutoCompleteTextView autoCompleteTxtLocal;

    private PlaceArrayAdapter mPlaceArrayAdapter;
    private GoogleApiClient mGoogleApiClient;
    private Passo1 passo1;

    @Override
    public void init() {
        super.init();

        setVisibilityBotaoVoltar(View.VISIBLE);
        txtSubTitulo.setVisibility(View.GONE);

        setTextString(txtTitulo, getString(R.string.titulo_endereco));

        passo1 = SuperApplication.getInstance().getSuperCache().getEvento().getPasso1();

        this.mPlaceArrayAdapter = new PlaceArrayAdapter(getBaseContext(), R.layout.item_spinner_drodown, /*BOUNDS_CURITIBA*/ null, null);
        mGoogleApiClient = new GoogleApiClient
                .Builder(getBaseContext())
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

        autoCompleteTxtLocal.setThreshold(3);
        autoCompleteTxtLocal.setAdapter(mPlaceArrayAdapter);
        autoCompleteTxtLocal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
                final String placeId = String.valueOf(item.placeId);
                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
                placeResult.setResultCallback(getResultCallback());

            }
        });

        if(StringUtils.isNotEmpty(passo1.getLogradouro())){
            setTextString(txtLogradouro, passo1.getLogradouro());
        }

        if(StringUtils.isNotEmpty(passo1.getCep())){
            setTextString(txtCep, passo1.getCep());
        }
        if(StringUtils.isNotEmpty(passo1.getBairro())){
            setTextString(txtBairro, passo1.getBairro());
        }
        if(StringUtils.isNotEmpty(passo1.getCidade())){
            setTextString(txtCidade, passo1.getCidade());
        }
        if(StringUtils.isNotEmpty(passo1.getEstado())){
            setTextString(txtEstado, passo1.getEstado());
        }
        if(StringUtils.isNotEmpty(passo1.getNumero())){
            setTextString(txtNumero, passo1.getNumero());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            mGoogleApiClient.connect();
        } catch (Exception e) {

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            mGoogleApiClient.disconnect();
        } catch (Exception e) {

        }
    }

    @Click({R.id.btnSalvar})
    public void onClickSalvar(View v) {

        String logradouro = getTextString(txtLogradouro);
        String numero = getTextString(txtNumero);
        String cep = getTextString(txtCep);
        String bairro = getTextString(txtBairro);
        String cidade = getTextString(txtCidade);
        String estado = getTextString(txtEstado);

        if(StringUtils.isEmpty(logradouro)){
            SuperUtil.alertDialog(activity, R.string.msg_validacao_logradouro);
            return;
        }

        if(StringUtils.isEmpty(cep)){
            SuperUtil.alertDialog(activity, R.string.msg_validacao_cep);
            return;
        }

        if(StringUtils.isEmpty(bairro)){
            SuperUtil.alertDialog(activity, R.string.msg_validacao_bairro);
            return;
        }

        if(StringUtils.isEmpty(cidade)){
            SuperUtil.alertDialog(activity, R.string.msg_validacao_cidade);
            return;
        }

        if(StringUtils.isEmpty(estado)){
            SuperUtil.alertDialog(activity, R.string.msg_validacao_estado);
            return;
        }

        passo1.setEstado(estado);
        passo1.setBairro(bairro);
        passo1.setCep(cep);
        passo1.setCidade(cidade);
        passo1.setLogradouro(logradouro);
        passo1.setNumero(numero);

        Intent intent = new Intent();
        Bundle param = new Bundle();
        param.putSerializable("passo1", passo1);
        intent.putExtras(param);
        setResult(RESULT_OK, intent);
        finish();
    }

    private ResultCallback<? super PlaceBuffer> getResultCallback() {
        return new ResultCallback<PlaceBuffer>() {
            @Override
            public void onResult(PlaceBuffer places) {
                if (!places.getStatus().isSuccess()) {
                    return;
                }
                // Selecting the first object buffer.
                final Place place = places.get(0);
                autoCompleteTxtLocal.setText(place.getAddress());
                autoCompleteTxtLocal.dismissDropDown();

                SuperGeocoder superGeocoder = new SuperGeocoder(getBaseContext(), place);
                txtLogradouro.setText(superGeocoder.getLogradouro());
                txtBairro.setText(superGeocoder.getBairro());
                txtCidade.setText(superGeocoder.getCidade());
                txtEstado.setText(superGeocoder.getEstado());
                txtCep.setText(superGeocoder.getCep());

            }
        };
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
