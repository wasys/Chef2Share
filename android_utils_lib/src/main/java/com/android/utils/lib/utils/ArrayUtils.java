package com.android.utils.lib.utils;

import android.content.Context;

import java.util.List;

/**
<resources>
    <string-array name="array_id">
        <item>@string/visualizar</item>
        <item>@string/excluir</item>
    </string-array>
</resources>

 * @author ricardo
 *
 */
public class ArrayUtils {

	public static String[] getArray(Context context, int resArray) {
		String[] array = context.getResources().getStringArray(resArray);
		return array;
	}

	public static String[] toArray(List<String> list) {
		if(list != null) {
			String[] array = new String[list.size()];
			list.toArray(array);
			return array;
		}
		return null;
	}
}
