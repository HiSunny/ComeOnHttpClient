package sunny.example.comeonhttpclient;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.os.Handler;
import android.os.Message;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.HttpResponse;
import org.apache.http.HttpEntity;

public class MainActivity extends ActionBarActivity implements OnClickListener{

	private Button sendRequestBtn;
	private TextView responseTextView;
	private int SEND_REQUEST = 0;
	
	Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			if(msg.what == SEND_REQUEST){
				String response = (String)msg.obj;
				responseTextView.setText(response);
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		sendRequestBtn = (Button)findViewById(R.id.requestBtn);
		responseTextView = (TextView)findViewById(R.id.responseText);
		sendRequestBtn.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.requestBtn){
			sendRequestWithHttpClient();
		}
	}

	private void sendRequestWithHttpClient(){
		new Thread(new Runnable(){
			@Override
			public void run(){
				try{
					HttpClient httpClient = new DefaultHttpClient();
					HttpGet httpGet= new HttpGet("http://www.baidu.com");
					HttpResponse httpResponse = httpClient.execute(httpGet);
					if(httpResponse.getStatusLine().getStatusCode() == 200){
						//获得消息的响应实体
						HttpEntity httpEntity = httpResponse.getEntity();
						String responseString = EntityUtils.toString(httpEntity,"utf-8");
						//将服务器返回的结果存放到Message对象中
						Message message = Message.obtain(mHandler, SEND_REQUEST, responseString);
						mHandler.sendMessage(message);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}).start();
	}
	
}
