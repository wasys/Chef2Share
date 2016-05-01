package com.android.utils.lib.utils;

import com.android.utils.lib.exception.DomainException;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;


public class Hash implements Serializable {
	private static final long serialVersionUID = -5345451229653786218L;
	private final Hashtable<String, Object> hash = new Hashtable<String, Object>();

	public void putList(String key, List<? extends HashSerializer> value) {
		if (value != null) {
			hash.put(key, value);
		}
	}
	
	public void putListHash(String key, List<Hash> list) {
		if (list != null) {
			hash.put(key, list);
		}
	}

	public void put(String key, boolean value) {
		hash.put(key, value ? "1" : "0");
	}

	public void put(String key, Integer value) {
		hash.put(key, value);
	}
	
	public void put(String key, Long value) {
		hash.put(key, value);
	}
	
	public void put(String key, Double value) {
		hash.put(key, value);
	}

	public void put(String key, String value) {
		if (value != null) {
			hash.put(key, value);
		}
	}

	public boolean getBoolean(String key) {
		String s = getString(key);
		boolean b = "1".equals(s);
		return b;
	}

	public String getString(String key) {
		String s = (String) hash.get(key);
		return s;
	}

	public int getInt(String key) {
		String s = getString(key);
		if(StringUtils.isInteger(s)) {
			int i = Integer.parseInt(s);
			return i;
		}
		return 0;
	}
	
	public long getLong(String key) {
		String s = getString(key);
		if(StringUtils.isLong(s)) {
			long i = Long.parseLong(s);
			return i;
		}
		return 0;
	}
	
	public double getDouble(String key) {
		String s = getString(key);
		if(StringUtils.isDouble(s)) {
			double d = Double.parseDouble(s);
			return d;
		}
		return 0;
	}

	public Hash getObject(String key) {
		Hash h = (Hash) hash.get(key);
		return h;
	}

	/**
	 * HashSerializer
	 * 
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Hash> getList(String key) {
		List<Hash> list = (List<Hash>) hash.get(key);
		return list;
	}

	public void put(String key, Hash hash) {
		this.hash.put(key, hash);
	}

	public Hash getHash(String key) {
		Hash h = (Hash) hash.get(key);
		return h;
	}

	public int size() {
		return hash.size();
	}

	public Set<String> keys() {
		return hash.keySet();
	}

	public Object get(String key) {
		return hash.get(key);
	}

	public String toString() {
		return hash.toString();
	}

	public void putAll(Hash hash) {
		if (hash != null) {
			Set<String> keys = hash.keys();
			for (String key : keys) {
				String value = (java.lang.String) hash.get(key);
				this.hash.put(key, value);
			}
		}
	}

	public Hashtable<String, Object> getHashtable() {
		return this.hash;
	}

	public static Hash deserialize(DataInputStream in) throws IOException, DomainException {
		try {
			boolean ok = in.readBoolean();
			if (!ok) {
				String error = in.readUTF();
				throw new IOException(error);
			}

			Hash hash = deserializeDataInputStream(in);
			
			return hash;
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

	public static Hash deserialize(InputStream in) throws IOException, DomainException {
		DataInputStream dataIn = null;

		try {
			dataIn = new DataInputStream(in);

			Hash hash = deserialize(dataIn);

			return hash;
		} finally {
			IOUtils.closeQuietly(dataIn);
			IOUtils.closeQuietly(in);
		}
	}

	private static Hash deserializeDataInputStream(DataInputStream in)
			throws IOException, DomainException {
		Hash hash = new Hash();
		int size = in.readInt();
		for (int i = 0; i < size; i++) {
			String key = in.readUTF();
			if (key.equals("hash")) {
				String value = in.readUTF();
				Hash hash2 = deserializeDataInputStream(in);
				hash.put(value, hash2);
			} else if (key.equals("list")) {
				String value = in.readUTF();
				int len = in.readInt();
				List<Hash> list = new ArrayList<Hash>();
				for (int j = 0; j < len; j++) {
					Hash hash2 = deserializeDataInputStream(in);
					list.add(hash2);
				}
				hash.putListHash(value, list);
			} else if (key.equals("imagem")) {
				byte[] bytes = readImage(in);
				hash.put(key, bytes);
			}  else {
				String value = in.readUTF();
				hash.put(key, value);
			}
		}
		
		// Verifica se existe erro
		/*String error = hash.getString("error");
		if(error != null) {
			throw new DomainException(error);
		}*/
		
		return hash;
	}

	public void put(String key, byte[] bytes) {
		hash.put(key, bytes);
	}

	public byte[] getBytes(String key) {
		byte[] bytes = (byte[]) hash.get(key);
		return bytes;
	}

	public static void writeImage(DataOutputStream out, byte[] bytes) throws IOException{
		if (bytes != null) {
			out.writeInt(bytes.length);
			out.write(bytes,0,bytes.length);
		}else {
			out.writeInt(0);
		}
	}
	
	public static byte[] readImage(DataInputStream in) throws IOException{
		int length = in.readInt();
		byte[] bytes = new byte[length];
		in.readFully(bytes);
		return bytes;
	}

	public byte[] serialize() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(bos);

		out.writeBoolean(true);

		serialize(this, out);

		// Retorna binario
		byte[] bytes = bos.toByteArray();
		bos.close();
		out.close();
		return bytes;
	}

	@SuppressWarnings("unchecked")
	private static void serialize(Hash hash, DataOutputStream out) throws IOException {
		int size = hash.size();
		out.writeInt(size);
		for (String key : hash.keys()) {
			Object value = hash.get(key);
			if(value instanceof HashSerializer) {
				HashSerializer h = (HashSerializer) value;
				Hash newHash = new Hash();
				h.serialize(newHash);
				out.writeUTF("hash");
				out.writeUTF(key);
				serialize(newHash,out);
			} else if(value instanceof List<?>) {
				List<HashSerializer> list = (List<HashSerializer>) value;
				out.writeUTF("list");
				out.writeUTF(key);
				out.writeInt(list.size());
				for (HashSerializer h : list) {
					Hash newHash = new Hash();
					h.serialize(newHash);
					serialize(newHash,out);
				}
			} else if(value instanceof byte[]) {
				byte[] bytes = (byte[]) value; 
				out.writeUTF("imagem");
				writeImage(out, bytes);
			} else {
				out.writeUTF(key);
				out.writeUTF(value.toString());
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hash == null) ? 0 : hash.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Hash other = (Hash) obj;
		if (hash == null) {
			if (other.hash != null)
				return false;
		} else if (!hash.equals(other.hash))
			return false;
		return true;
	}
	
	
}
