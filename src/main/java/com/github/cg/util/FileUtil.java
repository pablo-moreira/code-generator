package com.github.cg.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtil {

	public static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		
		try {

			br = new BufferedReader(new InputStreamReader(is));
			
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} 
		catch (IOException e) {
			throw new RuntimeException(e);
		} 
		finally {
			if (br != null) {
				try {
					br.close();
				} 
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}

		return sb.toString();
	}
}