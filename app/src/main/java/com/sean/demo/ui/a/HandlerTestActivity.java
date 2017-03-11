package com.sean.demo.ui.a;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sean.demo.R;
import com.sean.demo.ui.BaseActivity;

import butterknife.BindView;

public class HandlerTestActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    TextView show;
    @BindView(R.id.handler_tb)
    Toolbar handlerTb;
    private String message = "Sean";
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.a_activity_handler_test);
        initView();
        setToolBar(R.id.handler_tb);
        setToolBarMenuOnclick(new HandlerMenuClickListener());
        setBackArrow();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                show.setText("msgWhat " + msg.what + ";" + "msObj" + msg.obj);
            }
        };
    }


    class HandlerMenuClickListener implements Toolbar.OnMenuItemClickListener {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_message:
                    Toast.makeText(HandlerTestActivity.this, "clicked me", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_handler, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        show = (TextView) findViewById(R.id.show);
        handler = new Handler();

        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        message = "mySean1";
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                show.setText(message);
                            }
                        });
                    }
                }).start();
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        message = "mySean2";
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                show.setText(message);
                            }
                        });
                    }
                }).start();
            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        show.setText("测试非ui线程更新ui");
                    }
                }).start();
            }
        });


        //此种方式能在onResume中或者其之前调用，因为zaionResume中会初始化控制ui线程更新ui的一些控制
        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        show.setText("111");
                    }
                }.start();
            }
        });


        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = "测试子线程发送消息，在主线程更新ui";
                        handler.sendMessage(msg);
                    }
                }).start();
            }
        });
    }
}
