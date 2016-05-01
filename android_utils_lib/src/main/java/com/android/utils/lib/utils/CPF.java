package com.android.utils.lib.utils;

/**
 * Valida CPF
 * 
 * @author sidney
 *
 */
public class CPF {
	
	public static final int SIZE_COM_MASK = 14;
	public static final int SIZE_SEM_MASK = 11;

	private static final int[] pesoCPF = { 11, 10, 9, 8, 7, 6, 5, 4, 3, 2 };

	public static int calcularDigito(String str, int[] peso) {
		int soma = 0;
		int invalidChar = 0;
		for (int indice = str.length() - 1, digito; indice >= 0; indice--) {
			String dig = str.substring(indice, indice + 1);
			if ("-".equals(dig) || ".".equals(dig)) {
				invalidChar++;
				continue;
			}
			digito = Integer.parseInt(dig);
			soma += digito * peso[peso.length - str.length() + invalidChar + indice];
		}
		soma = 11 - soma % 11;
		return soma > 9 ? 0 : soma;
	}

	public static boolean isValidCPF(String cpf) {
		if (cpf == null)
		{
			return false;
		}

		if (cpf.length() == 11){
			// Formata o cpf antes
			cpf = formataCPF(cpf);
		}

		if (cpf.length() != 14)
		{
			return false;
		}

		Integer digito1 = new Integer(calcularDigito(cpf.substring(0, 11), pesoCPF));
		Integer digito2 = new Integer(calcularDigito(cpf.substring(0, 11) + digito1, pesoCPF));
		return cpf.equals(cpf.substring(0, 11) + "-" + digito1.toString() + digito2.toString());
	}

	/**
	 * Metodo que insere pontos e tra�os num CPF n�o formatado
	 * 
	 * @param cpfCliente
	 * @return String
	 */
	public static String formataCPF(String cpf) {
		try {
			// Adiciona zeros � esquerda do cpf caso o tamanho do cpf seja menor que
			// zero
			while (cpf.length() < 11) {
				cpf += "0";
			}

			return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "."
			+ cpf.substring(6, 9) + "-" + cpf.substring(9, 11);
		} catch (Exception e) {
			return cpf;
		}
	}
}
