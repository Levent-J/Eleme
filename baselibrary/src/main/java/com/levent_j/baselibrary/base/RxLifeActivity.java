package com.levent_j.baselibrary.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.reactivestreams.Publisher;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.BehaviorSubject;

/**
 * @auther : levent_j on 2018/3/8.
 * @desc :
 */

public class RxLifeActivity extends AppCompatActivity{

    private BehaviorSubject<ActivityLifeEvent> subject = BehaviorSubject.create();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subject.onNext(ActivityLifeEvent.CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        subject.onNext(ActivityLifeEvent.START);
    }

    @Override
    protected void onResume() {
        super.onResume();
        subject.onNext(ActivityLifeEvent.RESUME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        subject.onNext(ActivityLifeEvent.PAUSE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        subject.onNext(ActivityLifeEvent.STOP);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subject.onNext(ActivityLifeEvent.DESTROY);
    }

    public <T> FlowableTransformer<T,T> bindUnitlEvent(final ActivityLifeEvent activityLifeEvent){

         final Flowable<ActivityLifeEvent> flowable = subject.filter(new Predicate<ActivityLifeEvent>() {
             @Override
             public boolean test(ActivityLifeEvent event) throws Exception {
                 return activityLifeEvent.equals(event);
             }
         }).toFlowable(BackpressureStrategy.BUFFER);

        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                return upstream.takeUntil(flowable);
            }
        };
    }
}
