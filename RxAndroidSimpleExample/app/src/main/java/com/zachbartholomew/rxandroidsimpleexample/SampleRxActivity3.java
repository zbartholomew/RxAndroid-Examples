package com.zachbartholomew.rxandroidsimpleexample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zachbartholomew.rxandroidsimpleexample.databinding.ActivitySampleRx3Binding;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * This activity is going to build expand on the second.
 * We will split a string into a list, then, for each item in the list,
 * we will append its position in the list, and publish each item.
 */

public class SampleRxActivity3 extends AppCompatActivity {

    ActivitySampleRx3Binding mRx3Binding;
    Observer<String> myObserver;
    int mCounter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Activity 3");
        createObserver();

        mRx3Binding = DataBindingUtil.setContentView(this, R.layout.activity_sample_rx3);

        // We’ve condensed the creation of the Observable, using Observable.from().
        // For each String published to map(), the app sleeps for 3000 milliseconds,
        // appends the string position to the string, before finally publishing to the Observer.
        // The Observer still remains unchanged.
        mRx3Binding.buttonSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRx3Binding.buttonSubscribe.setEnabled(false);

                Observable.fromArray(mRx3Binding.editText.getText().toString().split("\n"))
                        .subscribeOn(Schedulers.io())
                        .map(new Function<String, String>() {
                            @Override
                            public String apply(@NonNull String s) throws Exception {
                                try {
                                    Thread.sleep(3000);
                                } catch (Exception e) {
                                    e.printStackTrace();
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
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull String s) {
                mRx3Binding.textView.setText(mRx3Binding.textView.getText() + "\n" + s);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                mRx3Binding.buttonSubscribe.setEnabled(true);
            }
        };
    }
}
