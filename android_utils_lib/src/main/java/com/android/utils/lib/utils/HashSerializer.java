package com.android.utils.lib.utils;

import java.io.IOException;
import java.io.Serializable;

public interface HashSerializer extends Serializable {
	public abstract void serialize(Hash hash) throws IOException;
	public abstract void deserialize(Hash hash) throws IOException;
}
