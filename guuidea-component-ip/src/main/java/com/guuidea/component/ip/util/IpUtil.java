package com.guuidea.component.ip.util;

public class IpUtil {

	public static long ip2Long(String ip) throws Exception {
		if (ip == null || ip.length() == 0) {
			return 0L;
		}
		String[] digits = ip.replaceAll(" ", "").split("\\.");
		long[] temp = (long[]) null;
		boolean invalid = true;

		if (digits.length == 4) {
			temp = new long[] { Long.parseLong(digits[0]), Long.parseLong(digits[1]), Long.parseLong(digits[2]),
					Long.parseLong(digits[3]) };

			invalid = (temp[0] <= 0L) || (temp[0] > 255L) || (temp[1] < 0L) || (temp[1] > 255L) || (temp[2] < 0L)
					|| (temp[2] > 255L) || (temp[3] < 0L) || (temp[3] > 255L);
		}
		if (invalid) {

			throw new Exception("无效的IP地址 [" + ip + "].");
		}

		temp[0] <<= 24;
		temp[1] <<= 16;
		temp[2] <<= 8;

		return temp[0] | temp[1] | temp[2] | temp[3];
	}

	public static void main(String[] args) {
		// long num1 = 192;
		// num1 <<= 24;
		// long num2 = 168;
		// num2 <<= 16;
		// long num3 = 1;
		// num3 <<= 8;
		// System.out.println(num1 | num2 | num3 | 255);
		byte[] end = new byte[] { 36, 36, 95, 95 };
		System.out.println(new String(end));
	}
}
