package io.ruszkipista.mycar;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class ActivityTransactionDetail extends AppCompatActivity implements DialogTransactionInput.DialogTransactionInputListener{
    private String mCarId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactiondetail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_transactiondetail);
        setSupportActionBar(toolbar);

        mCarId = getIntent().getStringExtra(Constants.KEY_CAR_ID);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_transactiondetail);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogTransactionInput dialog = DialogTransactionInput.newInstance(mCarId, null);
                dialog.show(getSupportFragmentManager(), getString(R.string.transactiondetail_name));
            }
        });
    }

    @Override
    public void applyChangesDialogTransactionInput(String carId) {

    }
}
