package com.shashi.android.androidutility;


import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import com.shashi.android.androidutility.R;


public class Mainactivity2 extends Activity{
	 private EditText text;
	 private EditText text1;
	 //private AdView mAdView;
	
	 
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main2);
	    text = (EditText) findViewById(R.id.editText1);
	    text1 = (EditText) findViewById(R.id.editText2);
	
	   // mAdView = (AdView) findViewById(R.id.adView);
       // mAdView.setAdListener(new ToastAdListener(this)); 
        //mAdView.loadAd(new AdRequest.Builder().build());
	
	}

	  // this method is called at button click because we assigned the name to the
	  // "OnClick  property" of the button
	  public void onClick1(View view) {
	    if(view.getId()==R.id.button2) {
	    
	      RadioButton celsiustofahrenheitButton = (RadioButton) findViewById(R.id.radio0);
	      RadioButton celsiustokelvinButton = (RadioButton) findViewById(R.id.radio1);
	      RadioButton fahrenheittocelsiusButton = (RadioButton) findViewById(R.id.radio2);
	      RadioButton fahrenheittokelvinButton = (RadioButton) findViewById(R.id.radio3);
	      RadioButton kelvintocelsiusButton = (RadioButton) findViewById(R.id.radio4);
	      RadioButton kelvintofahrenheitButton = (RadioButton) findViewById(R.id.radio5);
	      if (text.getText().length() == 0) {
	        Toast.makeText(this, "Please enter a valid number",
	            Toast.LENGTH_LONG).show();
	        return;
	      }

	      float inputValue = Float.parseFloat(text.getText().toString());
	      text.setFocusable(true);
	      if(celsiustofahrenheitButton.isChecked()){
	    	  text.setText(String
	    	            .valueOf(ConverterUtil.convertCelsiusToFahrenheit(inputValue)));
	    	  text1.setText("�F = �C  x  9/5 + 32 ");
	      }else if(celsiustokelvinButton.isChecked()){
	    	  text.setText(String
	    	            .valueOf(ConverterUtil.convertCelsiusToKelvin(inputValue)));
	    	  text1.setText("�K= �C + 273.15");
	      }else if(fahrenheittocelsiusButton.isChecked()){
	    	  text.setText(String
	    	            .valueOf(ConverterUtil.convertFahrenheitToCelsius(inputValue)));
	    	  text1.setText("�C = (�F  -  32)  x  5/9 ");
	    	  
	      }else if(fahrenheittokelvinButton.isChecked()){
	    	  text.setText(String
	    	            .valueOf(ConverterUtil.convertFahrenheitToKelvin(inputValue)));
	    	  text1.setText("�K = ( (�F  -  32)  x  5/9 ) + 273.15 )");
	      }else if(kelvintocelsiusButton.isChecked()){
	    	  text.setText(String
	    	            .valueOf(ConverterUtil.convertKelvinToCelsius(inputValue)));
	    	  text1.setText("�C = �K - 273.15");
	      }else if(kelvintofahrenheitButton.isChecked()){
	    	  text.setText(String
	    	            .valueOf(ConverterUtil.convertKelvinToFahrenheit(inputValue)));
	    	  text1.setText("�F = (�C + 273.15)  x  9/5 + 32 ");		
	      }
	      
	    }
	 }

	  @Override
	  protected void onPause() {
		  super.onPause();  
	//	  mAdView.pause();
	        
	    }

	    @Override
	    protected void onResume() {
	        super.onResume();
	  //      mAdView.resume();
	    }

	    @Override
	    protected void onDestroy() {
	    //    mAdView.destroy();
	        super.onDestroy();
	    }
}
