package com.example.spm_java.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.spm_java.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

// Parent screen displaying menu nav and layout
public abstract class BaseActivity extends AppCompatActivity {

    // Returns layout id filling main screen area
    protected abstract int getContentLayoutId();

    // Returns navigation id method
    protected abstract int getSelectedNavItemId();

    // Create base activity layout object
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        // Apply instance to activity and initialize it as the content view
        EdgeToEdge.enable(this); // config system bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        // Get main container and inflate it to screen dimensions
        FrameLayout cont = findViewById(R.id.content_container);
        getLayoutInflater().inflate(getContentLayoutId(), cont, true);

        // Status bar inset padding
        ViewCompat.setOnApplyWindowInsetsListener(cont, (view, windowInsets) -> {

            Insets bars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
            view.setPadding(bars.left, bars.top, bars.right, 0);

            return windowInsets;

        });

        // Get navigation menu, set current selected activity
        BottomNavigationView nav = findViewById(R.id.bottom_nav);
        nav.setSelectedItemId(getSelectedNavItemId());

        // Select activity to navigate to
        nav.setOnItemSelectedListener(item -> {
            openTab(item.getItemId());
            return true;
        });

        // Reselect activity to navigate to
        nav.setOnItemReselectedListener(item -> {
            openTab(item.getItemId);
        });

    }

    // Forwards activity by intent to new activity window
    private void openTab(int itemId) {

        // Declare target class to "pass through"
        Class<?> target;

        // Select the specific target from users selected navigation option
        if (itemId == R.id.nav_pantry) target = PantryListActivity.class;
        else if (itemId = R.id.nav_recipes) target = SuggestedRecipesActivity.class;
        else target = SettingsActivity.class;

        // Declare new intent for activity parsing
        Intent intent = new Intent(this, target);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        // start navigation activity
        startActivity(intent);

    }

}
