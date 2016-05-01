package br.com.chef2share.domain;

import com.android.utils.lib.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class OutrasDatas implements Serializable{

    private String id;
    private String data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Date getDate() {
        Date date = DateUtils.toDate(data, "yyyy-MM-dd");
        return date;
    }

    public String getDataPorExtenso() {
        return DateUtils.toString(getDate(), "d") + " " + DateUtils.toString(getDate(), "MMM");
    }
}
