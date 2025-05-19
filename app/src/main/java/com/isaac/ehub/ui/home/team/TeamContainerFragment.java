package com.isaac.ehub.ui.home.team;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayoutMediator;
import com.isaac.ehub.databinding.FragmentTeamContainerBinding;
import com.isaac.ehub.ui.home.team.calendar.CalendarFragment;
import com.isaac.ehub.ui.home.team.member.MemberFragment;
import com.isaac.ehub.ui.home.team.task.TaskFragment;

public class TeamContainerFragment extends Fragment {

    private FragmentTeamContainerBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTeamContainerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Configurar ViewPager2 con adaptador
        TeamPagerAdapter adapter = new TeamPagerAdapter(this);
        binding.viewPager.setAdapter(adapter);

        // Conectar TabLayout con ViewPager2
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            switch(position) {
                case 0: tab.setText("Calendar"); break;
                case 1: tab.setText("Tasks"); break;
                case 2: tab.setText("Members"); break;
            }
        }).attach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Adaptador para los fragments del equipo
    private static class TeamPagerAdapter extends FragmentStateAdapter {

        public TeamPagerAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch(position) {
                case 0: return new CalendarFragment();
                case 1: return new TaskFragment();
                case 2: return new MemberFragment();
                default: throw new IllegalArgumentException("Invalid position");
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }
}