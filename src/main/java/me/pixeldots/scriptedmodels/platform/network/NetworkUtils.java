package me.pixeldots.scriptedmodels.platform.network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class NetworkUtils {

    public static boolean shouldCompressBytes(String script) {
        return ScriptedModelsMain.CompressThreshold == -1 || script.length() > ScriptedModelsMain.CompressThreshold;
    }
    public static byte[] getBytes(String script) {
        return shouldCompressBytes(script) ? NetworkUtils.compress(script.getBytes()) : script.getBytes();
    }
    
    public static byte[] compress(byte[] array) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        GZIPOutputStream gzip = null;
        try {
            gzip = new GZIPOutputStream(baos);
            gzip.write(array);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try { gzip.close(); } catch (IOException e) {}
        }

        return baos.toByteArray();
    }

    public static byte[] decompress(byte[] array) {
        byte[] output = array;

        ByteArrayInputStream bais = new ByteArrayInputStream(array);
        GZIPInputStream gzip = null;
        try {
            gzip = new GZIPInputStream(bais);
            output = gzip.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try { gzip.close(); } catch (IOException e) {}
        }

        return output;
    }

    public static String decompress_tostring(byte[] array) {
        return new String(decompress(array));
    }

}
