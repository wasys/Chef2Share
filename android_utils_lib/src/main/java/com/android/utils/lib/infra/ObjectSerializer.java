package com.android.utils.lib.infra;


import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;

import com.android.utils.lib.utils.IOUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by lucas on 9/7/15.
 */
public class ObjectSerializer {

    /**
     * Serializa um objeto usando serializacao nativa do java. Retorna uma string base64 com o conteudo do objeto.
     *
     * @param obj
     * @return
     */
    public static String serialize(Serializable obj) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream(new Base64OutputStream(baos,Base64.URL_SAFE));
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            IOUtils.closeQuietly(oos);
            IOUtils.closeQuietly(bos);
            IOUtils.closeQuietly(baos);

            return new String(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Deserializa uma string codificada em base64
     *
     * @param objectB64
     * @return
     */
    public static Serializable deserialize(String objectB64) {
        ByteArrayInputStream bais = new ByteArrayInputStream(objectB64.getBytes());
        ObjectInputStream ois = null;
        Serializable obj = null;
        try {
            ois = new ObjectInputStream(new Base64InputStream(bais, Base64.URL_SAFE));
            obj = (Serializable) ois.readObject();
            IOUtils.closeQuietly(ois);
            IOUtils.closeQuietly(bais);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

}
