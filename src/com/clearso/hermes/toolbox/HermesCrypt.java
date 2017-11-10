package com.clearso.hermes.toolbox;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class HermesCrypt {
	
	public static String parseByte2HexStr(byte buf[]) {  
        StringBuffer sb = new StringBuffer();  
        for (int i = 0; i < buf.length; i++) {  
                String hex = Integer.toHexString(buf[i] & 0xFF);  
                if (hex.length() == 1) {  
                        hex = '0' + hex;  
                }  
                sb.append(hex.toUpperCase());  
                sb.append(",");
        }  
        return sb.toString();  
	}  

	
	public static void run(){
		try{

			byte[] ara=new byte[]{
					 0x00, 0x00, 0x00, 0x00,
					 0x00, 0x00, 0x00, 0x00,
					 0x00, 0x00, 0x00, 0x00,
					 0x00, 0x00, 0x00, 0x00};
			
			byte[] keys=new byte[]{
				     0x00, 0x01, 0x02, 0x03, 
				     0x04, 0x05, 0x06, 0x07, 
				     0x08, 0x09, 0x0a, 0x0b, 
				     0x0c, 0x0d, 0x0e, 0x0f};
			byte[] content= new byte[]{
			        0x00, 0x11, 0x22, 0x33,
			        0x44, 0x55, 0x66, 0x77,
			        (byte) 0x88, (byte) 0x99, (byte) 0xaa, (byte) 0xbb,
			        (byte) 0xcc, (byte) 0xdd, (byte) 0xee, (byte) 0xff};
			IvParameterSpec zeroIv = new IvParameterSpec(ara);  
            SecretKeySpec key = new SecretKeySpec(keys, "AES");  
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");  
			cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
			byte[] iv = cipher.getIV();
			System.out.println(parseByte2HexStr(iv));
			byte[] res = cipher.doFinal(content);
			System.out.println(parseByte2HexStr(res));
			res = cipher.doFinal(content);
			System.out.println(parseByte2HexStr(res));
			Cipher cipherd = Cipher.getInstance("AES");  
			cipherd.init(Cipher.DECRYPT_MODE, key, zeroIv);
			res = cipherd.doFinal(res);
			System.out.println(parseByte2HexStr(res));
			res[0] = res[1];
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
