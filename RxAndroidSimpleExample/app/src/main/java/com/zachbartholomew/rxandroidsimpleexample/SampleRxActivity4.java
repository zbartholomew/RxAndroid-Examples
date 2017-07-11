package com.zachbartholomew.rxandroidsimpleexample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zachbartholomew.rxandroidsimpleexample.databinding.ActivitySampleRx4Binding;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class SampleRxActivity4 extends AppCompatActivity {

    private static final String TAG = "SampleRxActivity4";

    ActivitySampleRx4Binding mRx4Binding;
    Observer<String> myObserver;
    int mCounter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Activity 4");
        createObserver();

        mRx4Binding = DataBindingUtil.setContentView(this, R.layout.activity_sample_rx4);

        mRx4Binding.startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRx4Binding.stopButton.setEnabled(false);
                mRx4Binding.stopButton.setEnabled(true);

                Observable.fromArray(mRx4Binding.editText.getText().toString().split("\n"))
                        .subscribeOn(Schedulers.io())
                        .map(new Function<String, String>() {
                            @Override
                            public String apply(@NonNull String s) throws Exception {
                                try {
                                    Thread.sleep(2000);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (mRx4Binding.errorToggle.isChecked()) {
                                    mCounter = 2 / 0;
                                }

                                return mCounter++ + ". " + s;
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(myObserver);

            }
        });
    }

    private void createObserver() {

        myObserver = new Observer<String>() {

            //So far, we have gone by the assumption that our Observables will execute to
            // completion, and publish all values to the Observer.
            // This is not practicable in real life, since the user might close your app before
            // your Observable has done executing. Also, your Observable can possibly never
            // terminate, publishing values on a timer, or on receipt of data from some other source.
            // The subscribe() method returns a Subscription object, whose sole purpose is to allow
            // unsubscribing.
            @Override
            public void onSubscribe(@NonNull final Disposable d) {
                Log.d(TAG, "onSubscribe: begins");

                mRx4Binding.stopButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!d.isDisposed()) {
                            d.dispose();
                        }
                        mRx4Binding.startButton.setEnabled(true);
                        mRx4Binding.stopButton.setEnabled(false);
                    }
                });
            }

            @Override
            public void onNext(@NonNull String s) {
                mRx4Binding.textView.setText(mRx4Binding.textView.getText() + "\n" + s);
            }

            // An error anywhere in between the source Observable and target Observer gets
            // propagated directly to the Observer’s onError() method.
            // The advantage of this is that there is one central place for error handling.
            // No matter how complex your Observable tree gets, the error handling is done at the
            // end, within the Observer’s onError().
            @Override
            public void onError(@NonNull Throwable e) {
                Toast.makeText(SampleRxActivity4.this,
                        "A \"" + e.getMessage() + "\" Error has been caught",
                        Toast.LENGTH_LONG).show();
                mRx4Binding.startButton.setEnabled(true);
                mRx4Binding.stopButton.setEnabled(false);
            }

            @Override
            public void onComplete() {
                mRx4Binding.startButton.setEnabled(true);
                mRx4Binding.stopButton.setEnabled(false);
            }
        };
    }
}
