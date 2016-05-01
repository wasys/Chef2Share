package br.com.chef2share.infra;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import br.com.chef2share.fragment.SuperFragment;

public class FragmentUtil {


    public static void replace(Fragment fragmentOrigem, int layout, Fragment novoFragment) {
        replace(fragmentOrigem, layout, novoFragment, false);
    }

    /**
     * @param fragmentOrigem = quem chamou (this)
     * @param layout - layout onde vai entrar esse fragment novo
     * @param novoFragment = fragment que vai ser adicionado
     */
    public static void replace(Fragment fragmentOrigem, int layout, Fragment novoFragment, boolean permiteVoltar) {
        FragmentTransaction fragmentTransaction = getFragmentTransacaction(fragmentOrigem);
        if(permiteVoltar){
            fragmentTransaction.addToBackStack(novoFragment.getTag());
        }
        fragmentTransaction.replace(layout, novoFragment, novoFragment.getClass().getSimpleName());
        fragmentTransaction.commit();
    }

    private static FragmentTransaction getFragmentTransacaction(Fragment fragmentOrigem) {
        return fragmentOrigem.getFragmentManager().beginTransaction();
    }

    public static void remove(Fragment parent, Fragment child) {
        FragmentManager manager = parent.getFragmentManager();
        manager.beginTransaction().remove(child).commit();
    }

    public static void remove(FragmentActivity parent, Fragment child) {
        FragmentManager manager = parent.getSupportFragmentManager();
        manager.beginTransaction().remove(child).commit();
    }

    public static void add(FragmentActivity parent, int layout, Fragment child, boolean permiteVoltar) {
        FragmentManager manager = parent.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(layout, child, child.getClass().getSimpleName());
        if(permiteVoltar){
            transaction.addToBackStack(child.getTag());
        }
        transaction.commit();
    }

    public static void add(Fragment parent, int layout, Fragment child, boolean permiteVoltar) {
        FragmentManager manager = parent.getFragmentManager();
        FragmentTransaction beginTransaction = manager.beginTransaction();
        beginTransaction.add(layout, child, child.getClass().getSimpleName());
        if(permiteVoltar){
            beginTransaction.addToBackStack(child.getTag());
        }
        beginTransaction.commit();
    }
    public static void replace(FragmentActivity parent, int layout, Fragment fragment, boolean permiteVoltar) {
        replace(parent,layout,fragment,permiteVoltar,null);
    }
    public static void replace(FragmentActivity parent, int layout, Fragment fragment, boolean permiteVoltar,Bundle args) {
        FragmentManager manager = parent.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if(permiteVoltar){
            transaction.addToBackStack(fragment.getTag());
        }
        fragment.setArguments(args);
        transaction.replace(layout, fragment, fragment.getClass().getSimpleName());
        transaction.commit();
    }
    public static void replace(SuperFragment parent, int layout, SuperFragment fragment, boolean permiteVoltar) {
        replace(parent,layout,fragment,permiteVoltar,null);
    }
    public static void replace(SuperFragment parent, int layout, SuperFragment fragment, boolean permiteVoltar,Bundle args) {
        FragmentManager manager = parent.getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if(permiteVoltar){
            transaction.addToBackStack(fragment.getTag());
        }
        fragment.setArguments(args);
        transaction.replace(layout, fragment, fragment.getClass().getSimpleName());
        transaction.commit();
    }

}
