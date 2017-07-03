package com.shop.com.learnhandler;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView result;
    
    // hello world
    /**
     * Handler处理消息的三种方式
     **/
    // 第一种 重写handleMessage方法
    private Handler firstHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            result.setText(msg.obj.toString());
        }
    };
    // 第二种 为Handler设置全局的Callback
    private Handler secondHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            result.setText(msg.obj.toString());
            return true;
        }
    });

    // 第三种 Message自带Callback
    private Handler thirdHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(MainActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = (TextView) findViewById(R.id.result);
    }

    public void onHandler1(View view) {
        ChildThread thread = new ChildThread(firstHandler, "重写handleMessage方法");
        thread.start();
    }

    public void onHandler2(View view) {
        ChildThread thread = new ChildThread(secondHandler, "为Handler设置全局的Callback");
        thread.start();
    }

    public void onHandler3(View view) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                result.setText("Message自带Callback");
            }
        };
        Message msg = Message.obtain(thirdHandler, r);
        msg.getCallback().run();
        msg.obj="Message自带Callback没有处理";
        thirdHandler.sendMessage(msg);
    }


    class ChildThread extends Thread {
        private Handler handler;
        private String str;

        public ChildThread(Handler handler, String str) {
            super();
            this.handler = handler;
            this.str = str;
        }

        @Override
        public void run() {
            super.run();
            Message msg = handler.obtainMessage();
            msg.obj = str;
            handler.sendMessage(msg);
        }
    }
}
