package br.com.chef2share.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import com.android.utils.lib.utils.StringUtils;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;


@EActivity
public class SuperExceptionHandlerActivity extends SuperActivity{

    @Extra("ERRO")
    public String erro;

    @Override
    protected void onStart() {
        super.onStart();

        if(StringUtils.isEmpty(erro)){
            finish();
            return;
        }

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Ocorreu um erro no app...");
        adb.setMessage("Isso é triste, mas aconteceu alguma coisa que eu não consegui mapear =/ \n\nClique em [Reportar erro] para enviar um email para o Jonas com os detalhes desse problema.");
        adb.setPositiveButton("Reportar erro", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"jonas.baggio@gmail.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Erro no aplicativo Chef2Share");
                emailIntent.putExtra(Intent.EXTRA_TEXT, erro);
                emailIntent.setType("text/plain");
                startActivity(emailIntent);
            }
        });
        adb.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
