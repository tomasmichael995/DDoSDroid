package gr.kalymnos.sk3m3l10.ddosdroid.mvc_controllers.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import gr.kalymnos.sk3m3l10.ddosdroid.R;
import gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_lists.AllAttackListsViewMvc;
import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.screen_attack_lists.AllAttackListsViewMvcImpl;

import static gr.kalymnos.sk3m3l10.ddosdroid.constants.ContentTypes.FETCH_ONLY_USER_JOINED_ATTACKS;
import static gr.kalymnos.sk3m3l10.ddosdroid.constants.ContentTypes.FETCH_ONLY_USER_NOT_JOINED_ATTACKS;
import static gr.kalymnos.sk3m3l10.ddosdroid.constants.ContentTypes.INVALID_CONTENT_TYPE;
import static gr.kalymnos.sk3m3l10.ddosdroid.constants.Extras.EXTRA_CONTENT_TYPE;
import static gr.kalymnos.sk3m3l10.ddosdroid.utils.BundleUtil.containsKey;

public class AllAttackListsActivity extends AppCompatActivity {
    private AllAttackListsViewMvc viewMvc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initiViewMvc();
        setupUiFrom(viewMvc);
    }

    private void initiViewMvc() {
        int contentType = getContentType(getIntent().getExtras());
        String[] tabTitles = getResources().getStringArray(R.array.network_technologies_titles);
        LayoutInflater inflater = LayoutInflater.from(this);
        viewMvc = new AllAttackListsViewMvcImpl(inflater, null, getSupportFragmentManager(), tabTitles, contentType);
    }

    private int getContentType(Bundle bundle) {
        if (containsKey(bundle, EXTRA_CONTENT_TYPE)) {
            return bundle.getInt(EXTRA_CONTENT_TYPE);
        }
        return INVALID_CONTENT_TYPE;
    }

    private void setupUiFrom(AllAttackListsViewMvc viewMvc) {
        viewMvc.bindToolbarTitle(getIntent().getStringExtra(Extras.EXTRA_TITLE));
        setSupportActionBar(viewMvc.getToolbar());
        setContentView(viewMvc.getRootView());
    }

    public static class Action {
        public static void startForJoinedAttacks(Context context) {
            context.startActivity(createIntent(context, FETCH_ONLY_USER_JOINED_ATTACKS, R.string.contributions_label));
        }

        public static void startForNonJoinedAttacks(Context context) {
            context.startActivity(createIntent(context, FETCH_ONLY_USER_NOT_JOINED_ATTACKS, R.string.join_attack_label));
        }

        @NonNull
        public static Intent createIntent(Context context, int contentType, int titleRes) {
            Intent intent = new Intent(context, AllAttackListsActivity.class);
            intent.putExtras(createBundle(contentType, context.getString(titleRes)));
            return intent;
        }

        @NonNull
        private static Bundle createBundle(int contentType, String title) {
            Bundle extras = new Bundle();
            extras.putInt(EXTRA_CONTENT_TYPE, contentType);
            extras.putString(Extras.EXTRA_TITLE, title);
            return extras;
        }
    }
}
