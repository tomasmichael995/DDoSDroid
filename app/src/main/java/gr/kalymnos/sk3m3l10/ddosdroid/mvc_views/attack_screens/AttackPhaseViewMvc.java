package gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.attack_screens;

import gr.kalymnos.sk3m3l10.ddosdroid.mvc_views.ViewMvcWithToolbar;

public interface AttackPhaseViewMvc extends ViewMvcWithToolbar {
    void bindToolbarSubtitle(String subtitle);

    int getFragmentContainerId();
}
