package com.gamesploit.crossradar;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ShellUtils {
    public static void SU(final String cmd){
        new Thread(new Runnable(){
				@Override
				public void run()
				{
					try
					{
						Process su = Runtime.getRuntime().exec("su -c " + cmd);
						DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
						outputStream.writeBytes("exit\n");
						outputStream.flush();
						BufferedReader reader = new BufferedReader(
                            new InputStreamReader(su.getInputStream()));
						reader.close();
						su.waitFor();

					} catch (IOException | InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
			}).start();


    }

}

