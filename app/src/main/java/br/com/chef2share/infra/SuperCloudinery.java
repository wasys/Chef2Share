package br.com.chef2share.infra;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.utils.lib.utils.AndroidUtils;
import com.cloudinary.Transformation;
import com.cloudinary.Url;

import java.util.HashMap;
import java.util.Map;

import br.com.chef2share.domain.Cloudinary;
import br.com.chef2share.domain.Imagem;

/**
 * Created by Jonas on 18/11/2015.
 */
public class SuperCloudinery {

    public static String getUrl(Context context, Imagem imagem){
        return getUrl(context, imagem, null, null);
    }

    public static String getUrl(Context context, Imagem imagem, Integer width, Integer height) {
        if(imagem == null || imagem.getCloudinary() == null) {
            return null;
        }

        com.cloudinary.Cloudinary cloud = getCloudinary(imagem);

        Transformation transformation = new Transformation();
        transformation.width(width);
        transformation.height(height);
        transformation.quality(70);
        transformation.crop("fit");
        Url url = cloud.url();
        url.format("jpg");
        String generate = url.transformation(transformation).generate(imagem.getId());
        return generate;
    }

    private static com.cloudinary.Cloudinary getCloudinary(Imagem imagem) {
        Cloudinary cloudinary = imagem.getCloudinary();

        Map config = new HashMap();
        config.put("cloud_name", cloudinary.getName());
        config.put("api_key", cloudinary.getApiKey());
        config.put("api_secret", cloudinary.getApiSecret());
        return new com.cloudinary.Cloudinary(config);
    }

    public static String getUrlImgPessoa(Context context, Imagem imagem){
        if(imagem == null || imagem.getCloudinary() == null) {
            return null;
        }

        com.cloudinary.Cloudinary cloud = getCloudinary(imagem);

        Transformation transformation = new Transformation();
        transformation.width(120);
        transformation.height(120);
        transformation.quality(90);
        transformation.gravity("face");
        transformation.crop("thumb");
        transformation.radius("max");
        Url url = cloud.url();
        url.format("jpg");
        String generate = url.transformation(transformation).generate(imagem.getId());
        return generate;
    }

    public static String getUrlImgPessoaIconMapa(Context baseContext, Imagem imagem) {
        if(imagem == null || imagem.getCloudinary() == null) {
            return null;
        }

        com.cloudinary.Cloudinary cloud = getCloudinary(imagem);

        Transformation transformation = new Transformation();
        transformation.width(96);
        transformation.height(96);
        transformation.quality(90);
        transformation.gravity("face");
        transformation.crop("thumb");
        transformation.radius("max");
        Url url = cloud.url();
        url.format("jpg");
        String generate = url.transformation(transformation).generate(imagem.getId());
        return generate;
    }
}
