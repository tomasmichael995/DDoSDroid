package gr.kalymnos.sk3m3l10.ddosdroid.mvc_model.job;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

import gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras;
import gr.kalymnos.sk3m3l10.ddosdroid.pojos.attack.Attack;

public final class AttackJobScheduler {
    private static final String TAG = "AttackJobScheduler";
    private static final int ONE_MINUTE = 60;

    int windowStart, windowEnd;
    private FirebaseJobDispatcher dispatcher;

    public AttackJobScheduler(Context context) {
        this.dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
    }

    synchronized public void schedule(Attack attack) {
        calculateTriggerWindows(attack);
        Job attackJob = jobFrom(attack, windowStart, windowEnd);
        dispatcher.mustSchedule(attackJob);
        Log.d(TAG, "Attack will start after a time between" + windowStart + " seconds until " + windowEnd + " seconds");
    }

    private void calculateTriggerWindows(Attack attack) {
        long currentTimeMillis = System.currentTimeMillis();
        long launchTimeMillis = attack.getLaunchTimestamp();
        long jobStartsAfterMillis = launchTimeMillis - currentTimeMillis;
        boolean attackNotStartedYet = jobStartsAfterMillis >= 0;
        if (attackNotStartedYet) {
            windowStart = (int) TimeUnit.MILLISECONDS.toSeconds(jobStartsAfterMillis);
            windowEnd = windowStart + ONE_MINUTE;
        }
    }

    @NonNull
    private Job jobFrom(Attack attack, int windowStart, int windowEnd) {
        Bundle bundle = new Bundle();
        bundle.putString(Extras.EXTRA_WEBSITE, attack.getWebsite());
        return dispatcher.newJobBuilder()
                .setService(AttackJobService.class)
                .setTag(attack.getPushId())
                .setRecurring(false)
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setTrigger(Trigger.executionWindow(windowStart, windowEnd))
                .setReplaceCurrent(false)
                .setExtras(bundle)
                .build();
    }

    public void cancel(String jobTag) {
        dispatcher.cancel(jobTag);
        Log.d(TAG, "Job: " + jobTag + " canceled");
    }

    public void cancelAll() {
        dispatcher.cancelAll();
        Log.d(TAG, "Canceled all jobs");
    }

}
