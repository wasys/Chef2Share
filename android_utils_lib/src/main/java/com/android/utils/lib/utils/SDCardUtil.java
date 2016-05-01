package com.android.utils.lib.utils;

import android.content.Context;

import java.io.File;
import java.io.IOException;

public class SDCardUtil {

	public static File writeToSdCard(Context context, String dir, String file,String string) throws IOException {
		File f = FileUtils.getSdCardFile(context, dir, file);
		if(f != null) {
			FileUtils.writeStringToFile(context, f, string);
		}
		return f;
	}

	public static File writeToSdCard(Context context, String dir, String file,String string, String charset) throws IOException {
		File f = FileUtils.getSdCardFile(context, dir, file);
		if(f != null) {
			FileUtils.writeStringToFile(context, f, string, charset);
		}
		return f;
	}

	public static File writeToSdCard(Context context, String dir, String file, byte[] bytes) throws IOException {
		File f = FileUtils.getSdCardFile(context, dir, file);
		if(f != null) {
			FileUtils.writeBytesToFile(context, f, bytes);
		}
		return f;
	}
}
