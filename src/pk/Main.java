package pk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

	private static final String CLASS_A_PREFIX = "0";
	private static final String CLASS_B_PREFIX = "10";
	private static final String CLASS_C_PREFIX = "110";
	private static final String CLASS_D_PREFIX = "1110";

	public static void main(String[] args) {
		// 引数チェック
		if (args.length != 1) {
			System.out.println("引数にIPアドレスを指定してください。");
			System.exit(1);
		}

		// IPアドレス判定のための正規表現
		Pattern pattern = Pattern.compile(
				"(([1-9]?[0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([1-9]?[0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])");
		Matcher matcher = pattern.matcher(args[0]);

		if (matcher.find()) {
			List<String> addressList = new ArrayList<>();
			addressList = Arrays.asList(args[0].split("\\."));
			String addressClass = getIpAdressClass(getBinaryAddress(addressList.get(0)));

			switch (addressClass) {
				case CLASS_A_PREFIX:
					if (isPrivateAddress(addressList, CLASS_A_PREFIX)) {
						showResult("クラスA", "プライベートIPアドレス");
					} else {
						showResult("クラスA", "グローバルIPアドレス");
					}
					break;
				case CLASS_B_PREFIX:
					if (isPrivateAddress(addressList, CLASS_B_PREFIX)) {
						showResult("クラスB", "プライベートIPアドレス");
					} else {
						showResult("クラスB", "グローバルIPアドレス");
					}
					break;
				case CLASS_C_PREFIX:
					if (isPrivateAddress(addressList, CLASS_C_PREFIX)) {
						showResult("クラスC", "プライベートIPアドレス");
					} else {
						showResult("クラスC", "グローバルIPアドレス");
					}
					break;
				case CLASS_D_PREFIX:
					System.out.println("クラスDです。");
					break;
				default:
					System.out.println("どのクラスにも属さないアドレスです。");
					break;
			}
		} else {
			System.out.println("IPアドレスのフォーマットに則っていません。");
			System.exit(1);
		}
	}

	/**
	 * １０進数から２進数に変換する
	 * @param decimalAddress 10進数のアドレス
	 * @return 10進数を２進数に変換した値
	 */
	private static String getBinaryAddress(String decimalAddress) {
		return String.format("%8s", Integer.toBinaryString(Integer.valueOf(decimalAddress))).replace(" ", "0");
	}

	/**
	 * どのクラスに属するのか判定する
	 * @param binaryAddress ２進数のアドレス
	 * @return クラスごとのプレフィックス、どのクラスにも属さない場合は"1"を返す
	 */
	private static String getIpAdressClass(String binaryAddress) {
		if (CLASS_A_PREFIX.equals(binaryAddress.substring(0, 1))) {
			return CLASS_A_PREFIX;
		} else if (CLASS_B_PREFIX.equals(binaryAddress.substring(0, 2))) {
			return CLASS_B_PREFIX;
		} else if (CLASS_C_PREFIX.equals(binaryAddress.substring(0, 3))) {
			return CLASS_C_PREFIX;
		} else if (CLASS_D_PREFIX.equals(binaryAddress.substring(0, 4))) {
			return CLASS_D_PREFIX;
		} else {
			return "1";
		}
	}

	/**
	 * クラスAからクラスCまでのアドレスクラスに属するIPアドレスがグローバルIPかプライベートIPかを判断する
	 * @param classPrefix
	 * @return プライベートIPアドレスの場合はtrueを返す、それ以外はfalseを返す
	 */
	private static boolean isPrivateAddress(List<String> addressList, String classPrefix) {
		int firstSet = Integer.valueOf(addressList.get(0));
		int secondSet = Integer.valueOf(addressList.get(1));

		switch (classPrefix) {
			case CLASS_A_PREFIX:
				// クラスAのプライベートIPアドレスの範囲は
				// 10.0.0.0 ～ 10.255.255.255
				if (firstSet == 10) {
					return true;
				}
				break;
			case CLASS_B_PREFIX:
				// クラスBのプライベートIPアドレスの範囲は
				// 172.16.0.0 ～ 172.31.255.255
				if (firstSet == 172 && (16 <= secondSet && secondSet <= 31)) {
					return true;
				}
				break;
			case CLASS_C_PREFIX:
				// クラスCのプライベートIPアドレスの範囲は
				// 192.168.0.0 ～ 192.168.255.255
				if (firstSet == 192 && secondSet == 168) {
					return true;
				}
				break;
			case CLASS_D_PREFIX:
				// クラスDはマルチキャスト用のためグローバル、プライベートの概念はない
				break;
			default:
				return false;
		}
		return false;
	}

	/**
	 * IPアドレスの識別結果を表示する
	 * @param addressClass　IPアドレスクラス名
	 * @param addressStatus グローバルIPアドレス or プライベートIPアドレス
	 */
	private static void showResult(String addressClass, String addressStatus) {
		System.out.println(addressClass + "であり、" + addressStatus + "です。");
	}
}