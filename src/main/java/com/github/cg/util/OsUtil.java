package com.github.cg.util;

public class OsUtil {

	private static final String OS_LINUX = "Linux";
	private static final String OS_WINDOWS = "Windows";
	private static final String PROPERTY_OS_NAME = "os.name";
	private static final String PROPERTY_OS_ARCH = "os.arch";

	public static String getOsArch() {
		return System.getProperty(PROPERTY_OS_ARCH);
	}

	public static String getOsName() {
		return System.getProperty(PROPERTY_OS_NAME);
	}

	public static boolean isOsLinux() {
		return getOsName().startsWith(OS_LINUX);
	}

	public static boolean isOsWindows() {
		return getOsName().startsWith(OS_WINDOWS);
	}
}
