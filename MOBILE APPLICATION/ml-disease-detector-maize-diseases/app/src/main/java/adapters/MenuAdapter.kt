package adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import fragments.menu.*

class MenuAdapter(
    fragment: FragmentManager,
    private var totalItems: Int
): FragmentPagerAdapter(fragment, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> Home()
            1 -> Diagnose()
//            2 -> More()
            else -> Home()
        }
    }

    override fun getCount(): Int {
        return totalItems
    }
}