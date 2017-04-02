package sylex.androidClient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.net.SocketException;
import java.net.UnknownHostException;

import sylex.androidClient.interfaces.ConnectEventReceiver;
import sylex.controller.ClientCore;
import sylex.model.ClientModel;

public class ConnectActivity extends AppCompatActivity implements ConnectEventReceiver {

    private ClientCore ClientCore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        ClientModel.bindConnectEvent(this);
    }

    public void onClickConnect(View view) {
        EditText addressText = (EditText) findViewById(R.id.address);
        EditText nameText = (EditText) findViewById(R.id.username);
        String IPPort = addressText.getText().toString();
        try {
            if(this.ClientCore != null)
                ClientCore.stopCore();
            this.ClientCore = new ClientCore(IPPort);
        } catch(SocketException | UnknownHostException e) {
            e.printStackTrace();
            // + afficher un message d'erreur.
            return;
        }
        ClientCore.start();
        String uname = nameText.getText().toString();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ClientModel.Connect(uname);
    }

    @Override
    public void onConnectOK() {
        Intent intent = new Intent(this, LobbyActivity.class);
        startActivity(intent);
    }

    @Override
    public void onConnectBAD() {
        runOnUiThread(new Runnable() {
            public void run() {
                EditText unameText = (EditText) findViewById(R.id.username);
                unameText.setText("NOM DÉJA PRIS");
            }
        });
    }


}
