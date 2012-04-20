/*
Copyright (C) 2010 Haowen Ning

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

*/

package org.liberty.android.fantastischmemo.tts;

import java.util.Locale;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

public class AnyMemoTTSPlatform implements AnyMemoTTS, TextToSpeech.OnInitListener{

	private TextToSpeech myTTS;
	
	private Locale myLocale;

    public final static String TAG = "org.liberty.android.fantastischmemo.TTS";

    public void onInit(int status){
        if (status == TextToSpeech.SUCCESS) {
            Log.v(TAG, "init!" + myLocale.toString());
            int result = myTTS.setLanguage(myLocale);
            if (result == TextToSpeech.LANG_MISSING_DATA) {
                Log.e(TAG, "Missing language data");
            }
            if (result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(TAG, "Language is not supported");
            }
        } else {
            Log.e(TAG, "Can't initialize");
        }
    }
	
	public AnyMemoTTSPlatform(Context context, Locale locale){
		myTTS = new TextToSpeech(context, this);
		myLocale = locale;
	}

	
	public void shutdown(){
        myTTS.shutdown();
	}

    public void stop(){
        myTTS.stop();
        // We wait until the tts is not speaking.
        // This is because top is asynchronized call
        while (myTTS.isSpeaking()) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
            }
        }
    }
	
	public int sayText(String s){
        Log.v(TAG, "say it!");
		// Replace break with period
		String processed_str = s.replaceAll("\\<br\\>", ". " );
		// Remove HTML
		processed_str = processed_str.replaceAll("\\<.*?>", "");
		// Remove () [] and their content
		processed_str = processed_str.replaceAll("\\[.*?\\]", "");
        // Remove the XML special character
		processed_str = processed_str.replaceAll("\\[.*?\\]", "");
		processed_str = processed_str.replaceAll("&.*?;", "");

        if(!myTTS.isSpeaking()){
            //myTTS.setLanguage(myLocale);
            myTTS.speak(processed_str, 0, null);
        }
        else{
            stop();
        }
		
		return 0;
	}

}
