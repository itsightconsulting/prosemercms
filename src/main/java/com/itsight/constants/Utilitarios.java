package com.itsight.constants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Utilitarios {
	
	public static String parseFormalUUID(String noParsed) {
		noParsed = noParsed.substring(0,8).toUpperCase() + "-" +
				noParsed.substring(8,12).toUpperCase() + "-" +
				noParsed.substring(12,16).toUpperCase() + "-" +
				noParsed.substring(16,20).toUpperCase() + "-" +
				noParsed.substring(20).toUpperCase();
		return noParsed;
	}
	
	public static Integer[] arrayTiposFormato(List<GrantedAuthority> lstRoles){
		
		Integer[] tipoFormato = new Integer[lstRoles.size()];
		int i= 0;
		
		for (GrantedAuthority x : lstRoles) {
			String[] split = x.getAuthority().split("_");

			try {
				int valor = Integer.parseInt(split[split.length-1]);
				
				if(valor >= 0) {
					tipoFormato[i] = valor; 
					i++;
				}
				
			} catch (NumberFormatException nfe) {
			}
		}

	    List<Integer> tipoFormatoFilter = new ArrayList<Integer>();
	    for(Integer s : tipoFormato) {
	       if(s != null && s > 0) {
	    	   tipoFormatoFilter.add(s);
	       }
	    }

	    tipoFormato = tipoFormatoFilter.toArray(new Integer[tipoFormatoFilter.size()]);
	    
		return tipoFormato;
	}
	
	public static String createDirectory(String path) {
		File dirFile = new File(path);

		if (!dirFile.exists()) {
			try {
				dirFile.mkdir();
				
			} catch (SecurityException se) {
				
			}
		}
		return path;
	}
	
	public static void createDirectoryStartUp(String basePath, String[] paths) {
		for (int i = 0; i < paths.length; i++) {
			File dirFile = new File(basePath);
			if (!dirFile.exists()) {
				try {
					dirFile.mkdir();
				} catch (SecurityException se) {
					se.printStackTrace();
				}
			}
				File nuevoFile = new File(basePath+paths[i]);
				if (!nuevoFile.exists()) {
					try {
						System.out.println("> PATH NEW DIRECTORY: "+ nuevoFile.getAbsolutePath());
						nuevoFile.mkdir();
					} catch (SecurityException se) {
						se.printStackTrace();
					}
				}
			}
	}
	
	public static boolean validateOnlyNumbers(String cadena) {
		char[] array = cadena.toCharArray();
		int i = 0;
		
		for (i = 0; i < array.length; i++) {
			if(!Character.isDigit(array[i])) {
				i--;
				break;
			}
		}
		
		if(i == array.length) {
			return true;
		}else {
			return false;
		}
	}
	
	public static final String encoderPassword(String password) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(password);
	}
	
	public static final String parseMonth(String month) {
			switch (month) {
			case "1":
			case "2":
			case "3":
			case "4":
			case "5":
			case "6":
			case "7":
			case "8":
			case "9":
				month = "0"+month;
				break;
			default:
				break;
		}
			return month;
	}
	
	public static final String randomString() {
		int leftLimit = 97; // letter 'a'
	    int rightLimit = 122; // letter 'z'
	    int targetStringLength = 10;
	    Random random = new Random();
	    StringBuilder buffer = new StringBuilder(targetStringLength);
	    for (int i = 0; i < targetStringLength; i++) {
	        int randomLimitedInt = leftLimit + (int) 
	          (random.nextFloat() * (rightLimit - leftLimit + 1));
	        buffer.append((char) randomLimitedInt);
	    }
	    String generatedString = buffer.toString();
	    return generatedString;
	}
	
	public static String nombreMes(int mes) {
		String nombre = "";
		switch (mes) {
		case 1:
			nombre = "Enero";
			break;
		case 2:
			nombre = "Febrero";
			break;
		case 3:
			nombre = "Marzo";
			break;
		case 4:
			nombre = "Abril";
			break;
		case 5:
			nombre = "Mayo";
			break;
		case 6:
			nombre = "Junio";
			break;
		case 7:
			nombre = "Julio";
			break;
		case 8:
			nombre = "Agosto";
			break;
		case 9:
			nombre = "Setiembre";
			break;
		case 10:
			nombre = "Octubre";
			break;
		case 11:
			nombre = "Noviembre";
			break;
		default:
			nombre = "Diciembre";
			break;
		}
		
		return nombre;
	}
}

