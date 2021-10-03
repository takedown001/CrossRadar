package com.gamesploit.crossradar;

import static android.os.Environment.DIRECTORY_PICTURES;

import static com.gamesploit.crossradar.GccConfig.urlref.ZipPass;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;

import androidx.annotation.RequiresApi;

import com.gamesploit.crossradar.GccConfig.urlref;

import net.lingala.zip4j.ZipFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;
import java.util.UUID;

import javax.crypto.Cipher;

public class Helper {

    public static boolean isAppRunning(final Context context, final String packageName) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        if (procInfos != null)
        {
            for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
                if (processInfo.processName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    private static String bytesToHex(byte[] bytes) {
        char[] hexArray = "0123456789abcdef".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    static String readStream(InputStream in) {
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException ignored) {
        }
        return response.toString();
    }

    //    computed the sha1 hash of the signature
    public static String getSHA1(byte[] sig) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA1");
        digest.update(sig);
        byte[] hashtext = digest.digest();
        return bytesToHex(hashtext);
    }

    public static PublicKey getPublicKey(byte[] keyBytes) throws Exception {
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    public static String encrypt(String plainText, byte[] keyBytes) throws Exception {
        Cipher encryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, getPublicKey(keyBytes));
        return toBase64(encryptCipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8)));
    }

    public static boolean verify(String plainText, String signature, byte[] keyBytes) throws Exception {
        java.security.Signature publicSignature = java.security.Signature.getInstance("SHA256withRSA");
        publicSignature.initVerify(getPublicKey(keyBytes));
        publicSignature.update(plainText.getBytes(StandardCharsets.UTF_8));
        return publicSignature.verify(Helper.fromBase64(signature));
    }
    public static boolean appInstalledOrNot(String packageName, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return false;
        } else {
            try {
                context.getPackageManager().getApplicationInfo(packageName, 0);
                return false;
            } catch (PackageManager.NameNotFoundException e) {
                return true;
            }
        }
    }

    public static String getUniqueId(Context ctx) {
        String key = (getDeviceName() + Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID) + Build.HARDWARE).replace(" ", "");
        UUID uniqueKey = UUID.nameUUIDFromBytes(key.getBytes());
        return uniqueKey.toString().replace("-", "");
    }
    public static String profileDecrypt(String data, String sign) {
        char[] key = sign.toCharArray();
        char[] out = fromBase64String(data).toCharArray();
        for (int i = 0; i < out.length; i++) {
            out[i] = (char) (key[i % key.length] ^ out[i]);
        }
        return new String(out);
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return model;
        } else {
            return manufacturer + " " + model;
        }
    }

    static String SHA256(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.reset();
            md.update(data.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(md.digest()).toUpperCase();
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }
    public static void givenToFile(Context ctx, String data) throws IOException {
        File fileName = new File(ctx.getFilesDir().toString() + "/scheat.sh");
        FileWriter fileWriter = new FileWriter(fileName);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(data);
        printWriter.close();
        fileName.setExecutable(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Runtime.getRuntime().exec("su -c sh " + ctx.getFilesDir().toString() + "/scheat.sh");
                    Thread.sleep(1000);
                    if (fileName.exists()) {
                       fileName.delete();
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    public static void unzip(Context ctx,String game) throws IOException {
        final String files = ctx.getFilesDir().toString();
        File pic = new File(files);
        if (!pic.exists()) {
            pic.mkdir();
        }
        File src = new File(files + "/libGcc.so");
        ZipFile zipFile = new ZipFile(src);
        if (zipFile.isEncrypted()) {
            zipFile.setPassword(ZipPass.toCharArray());
        }
        //    Log.d("zip",files);
        try {
            //    Log.d("test", String.valueOf(!Helper.checkmd5(ctx,game)));
            if(!Helper.checkmd5(ctx,game)){
                zipFile.extractAll(files);
                Runtime.getRuntime().exec("su -c mv -f "+files+"/1 /data/data/"+game+"/lib/libBugly.so");
                Thread.sleep(1000);
                Runtime.getRuntime().exec("su -c chmod -R 755 /data/data/"+game+"/lib/libBugly.so");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
    static String toBase64(String s) {
        return Base64.encodeToString(s.getBytes(StandardCharsets.UTF_8), Base64.NO_WRAP);
    }

    static String toBase64(byte[] s) {
        return Base64.encodeToString(s, Base64.NO_WRAP);
    }

    static byte[] fromBase64(String s) {
        return Base64.decode(s, Base64.NO_WRAP);
    }

    static String fromBase64String(String s) {
        return new String(Base64.decode(s, Base64.NO_WRAP), StandardCharsets.UTF_8);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean checkmd5(Context ctx, String game) throws IOException, InterruptedException {
        File path = new File("/data/data/" + game + "/lib/libBugly.so");
        String corrpt, text;
        corrpt = "0343281bcb45f88ff262ae957f086559 /data/data/"+game+"/lib/libBugly.so";
        Process process = Runtime.getRuntime().exec("su -c md5sum " + path);
        BufferedReader inputStream = new BufferedReader(new InputStreamReader(process.getInputStream()));
        //	Log.d("test",inputStream.readLine());
        text = inputStream.readLine();
        process.waitFor();
        return text.equals(corrpt);
    }
}