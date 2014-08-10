package com.example.androidbluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemClickListener {

ArrayAdapter<String> listAdapter;
ListView listview;
BluetoothAdapter btAdapter;
Set<BluetoothDevice> deviceArray;
ArrayList<String> pairedDevices;
ArrayList<BluetoothDevice> devices;
public static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
IntentFilter filter;
protected static final int MESSAGE_READ = 1;
protected static final int SUCESS_CONNECT = 0;
BroadcastReceiver receiver;

Handler mHandler =new Handler(){
	public void handleMessage(Message msg){
		super.handleMessage(msg);
		switch(msg.what){
		case SUCESS_CONNECT:
			ConnectedThread connectedThread = new ConnectedThread((BluetoothSocket)msg.obj);
			Toast.makeText(getApplicationContext(), "Connected Successfully", Toast.LENGTH_SHORT).show();
			String s="Sucessfully Connected";
			connectedThread.write(s.getBytes());
			break;
		case MESSAGE_READ:
			byte[] readbuff=(byte[])msg.obj;
			String s1= new String(readbuff);
			Toast.makeText(getApplicationContext(), s1, Toast.LENGTH_SHORT).show();
			break;
		}	
	}
};	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	if(btAdapter==null){
		Toast.makeText(getApplicationContext(), "No Bluetooth Enabled", 0).show();;
		finish();
	 }else {
		 if(!btAdapter.isEnabled()){
			turnonBT();
		 }
		 getPairedDevices();
		 startDiscovery();
		 
	 }
	
	}

	private void startDiscovery() {
		// TODO Auto-generated method stub
		btAdapter.cancelDiscovery();
		btAdapter.startDiscovery();
	}

	private void turnonBT() {
		// TODO Auto-generated method stub
		 Intent intent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		 startActivityForResult(intent, 1);
	}

	private void getPairedDevices() {
		// TODO Auto-generated method stub
		deviceArray=btAdapter.getBondedDevices();
		if(deviceArray.size()>0){
			for(BluetoothDevice device:deviceArray){
				
				pairedDevices.add(device.getName());
			}
		}
	}

	private void init() {
		// TODO Auto-generated method stub
		listview=(ListView)findViewById(R.id.listView);
		listview.setOnItemClickListener(this);
		listAdapter =new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,0);
		listview.setAdapter(listAdapter);
		btAdapter=BluetoothAdapter.getDefaultAdapter();
		pairedDevices=new ArrayList<String>();
		filter=new IntentFilter(BluetoothDevice.ACTION_FOUND);
		devices=new ArrayList<BluetoothDevice>();
		receiver=new BroadcastReceiver(){
		
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				String action= intent.getAction();
				if(BluetoothDevice.ACTION_FOUND.equals(action)){
					BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				    devices.add(device);
					listAdapter.add(device.getName()+"\n"+device.getAddress());
				
				    String s=" ";	
				    for(int a=0;a<pairedDevices.size();a++){
						if(device.getName().equals(pairedDevices.get(a))){
							
							s="(PAIRED)";

							break;
						}
					}
				    listAdapter.add(device.getName()+""+s+""+"\n"+device.getAddress());
				    
				    
				}else if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
					
				}else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
					if(listAdapter.getCount()>0){
						
					}
				}else if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)){
					if(btAdapter.getState()==btAdapter.STATE_OFF){
						turnonBT();
					}
				}
			}
			
		};
		registerReceiver(receiver, filter);
		filter= new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		registerReceiver(receiver, filter);
		filter= new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		registerReceiver(receiver, filter);
		filter= new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
		registerReceiver(receiver, filter);
	}
@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		unregisterReceiver(receiver);
	}
	@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			// TODO Auto-generated method stub
			super.onActivityResult(requestCode, resultCode, data);
			if(requestCode==RESULT_CANCELED){
				Toast.makeText(getApplicationContext(), "Bluetooth has to be enabled",Toast.LENGTH_SHORT ).show();;
				finish();
			}
		}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if(btAdapter.isDiscovering()){
			btAdapter.cancelDiscovery();
		}
		if(listAdapter.getItem(position).contains("PAIRED")){
			Toast.makeText(getApplicationContext(), "device Paired", 0).show();
		//	Object[] O=deviceArray.toArray();
			BluetoothDevice selectedDevice= devices.get(position);
		//(BluetoothDevice) O[position];
			ConnectThread connect=new ConnectThread(selectedDevice);
			connect.start();
		}else{
			Toast.makeText(getApplicationContext(), "device not Paired", 0).show();
		}
	}
	
	private class ConnectThread extends Thread {
	    
		private final BluetoothSocket mmSocket;
	    private final BluetoothDevice mmDevice;
	 
	    public ConnectThread(BluetoothDevice device) {
	        // Use a temporary object that is later assigned to mmSocket,
	        // because mmSocket is final
	        BluetoothSocket tmp = null;
	        mmDevice = device;
	 
	        // Get a BluetoothSocket to connect with the given BluetoothDevice
	        try {
	            // MY_UUID is the app's UUID string, also used by the server code
	            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
	        } catch (IOException e) { }
	        mmSocket = tmp;
	    }
	 
	    public void run() {
	        // Cancel discovery because it will slow down the connection
	        btAdapter.cancelDiscovery();
	 
	        try {
	            // Connect the device through the socket. This will block
	            // until it succeeds or throws an exception
	            mmSocket.connect();
	        } catch (IOException connectException) {
	            // Unable to connect; close the socket and get out
	            try {
	                mmSocket.close();
	            } catch (IOException closeException) { }
	            return;
	        }
	 
	        // Do work to manage the connection (in a separate thread)
	        //manageConnectedSocket(mmSocket);
	        mHandler.obtainMessage(SUCESS_CONNECT, mmSocket).sendToTarget();;
	    }
	 
	    /*private void manageConnectedSocket(BluetoothSocket mmSocket2) {
			// TODO Auto-generated method stub
			
		}*/

		/** Will cancel an in-progress connection, and close the socket */
	    public void cancel() {
	        try {
	            mmSocket.close();
	        } catch (IOException e) { }
	    }
	}
	
	private class ConnectedThread extends Thread {
	    
		private final BluetoothSocket mmSocket;
	    private final InputStream mmInStream;
	    private final OutputStream mmOutStream;
	 
	    public ConnectedThread(BluetoothSocket socket) {
	        mmSocket = socket;
	        InputStream tmpIn = null;
	        OutputStream tmpOut = null;
	 
	        // Get the input and output streams, using temp objects because
	        // member streams are final
	        try {
	            tmpIn = socket.getInputStream();
	            tmpOut = socket.getOutputStream();
	        } catch (IOException e) { }
	 
	        mmInStream = tmpIn;
	        mmOutStream = tmpOut;
	    }
	 
	    public void run() {
	        byte[] buffer;  // buffer store for the stream
	        int bytes; // bytes returned from read()
	 
	        // Keep listening to the InputStream until an exception occurs
	        while (true) {
	            try {
	            	buffer = new byte[1024];
	                // Read from the InputStream
	                bytes = mmInStream.read(buffer);
	                // Send the obtained bytes to the UI activity
	                mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
	                        .sendToTarget();
	            } catch (IOException e) {
	                break;
	            }
	        }
	    }
	 
	    /* Call this from the main activity to send data to the remote device */
	    public void write(byte[] bytes) {
	        try {
	            mmOutStream.write(bytes);
	        } catch (IOException e) { }
	    }
	 
	    /* Call this from the main activity to shutdown the connection */
	    public void cancel() {
	        try {
	            mmSocket.close();
	        } catch (IOException e) { }
	    }
	}
	
}
