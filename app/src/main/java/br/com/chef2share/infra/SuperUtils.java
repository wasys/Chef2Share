package br.com.chef2share.infra;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.CalendarContract;
import android.util.Base64;

import com.android.utils.lib.utils.AndroidUtils;
import com.android.utils.lib.utils.MoneyUtils;
import com.android.utils.lib.utils.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;

import br.com.chef2share.activity.EventosQueVouActivity;
import br.com.chef2share.activity.SuperActivity;
import br.com.chef2share.domain.Passo3;
import br.com.chef2share.domain.Pedido;

/**
 * Created by Jonas on 25/11/2015.
 */
public class SuperUtils {

    /**
     * A imagem de fundo deve ocupar toda a largura da tela do device
     *
     * @param context
     * @return
     */
    public static int getWidthImagemFundo(Context context) {
        return AndroidUtils.getWidthPixels(context);
    }

    /**
     * A altura da imagem de fundo deve ter 2/3 da largura total da imagem
     *
     * @param context
     * @return
     */
    public static int getHeightImagemFundo(Context context) {
        int width = AndroidUtils.getWidthPixels(context);
        int height = width / 3 * 2;
        return height;
    }

    public static int getWidthImagemFundoThumb(Context context) {
        return AndroidUtils.getWidthPixels(context) / 2;
    }

    public static int getHeightImagemFundoThumb(Context context) {
        int width = getWidthImagemFundoThumb(context);
        int height = width / 3 * 2;
        return height;
    }

    public static void addEventoCalendario(SuperActivity activity, Pedido pedido) {
        Intent calIntent = new Intent(Intent.ACTION_INSERT);
        calIntent.setType("vnd.android.cursor.item/event");
        calIntent.putExtra(CalendarContract.Events.TITLE, "Chef2Share - " + pedido.getEvento().getPasso2().getTitulo());
        calIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, pedido.getEvento().getPasso1().getEnderecoCompleto(true));
        calIntent.putExtra(CalendarContract.Events.DESCRIPTION, pedido.getEvento().getPasso1().getDescricao());

        Passo3 passo3 = pedido.getEvento().getPasso3();

        calIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, passo3.getDataHoraInicioDate().getTime());
        calIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, passo3.getDataHoraFimDate().getTime());
        calIntent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false);
        activity.startActivity(calIntent);
    }

    public static String getBase64(File file) {
        Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        bm.recycle();
        return encodedImage;
    }

    public static double stringRSToDouble(String valor) {
        if(StringUtils.isEmpty(valor)){
            return 0;
        }

        Double valorDouble = Double.parseDouble(valor.replace("R$", "").replaceAll("\\.", "").replace(",", "."));
        return valorDouble;
    }

    public static boolean isPermitidoChekin(String status) {
        String[] statusPermitidos = new String[]{"PAGO"};
        for (String s : statusPermitidos) {
            if(StringUtils.equalsIgnoreCase(s, status)){
                return true;
            }
        }
        return false;
    }
}
