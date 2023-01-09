package tv.orange.features.timer.bridge

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import tv.orange.core.PreferencesManagerCore
import tv.orange.features.timer.data.service.OrangeSleepTimer

class OrangeTimerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {
    private val prefManager = PreferencesManagerCore

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val data = prefManager.getLastTimerData()
        return TimePickerDialog(activity, this, data.first, data.second, true)
    }

    override fun onTimeSet(self: TimePicker?, hour: Int, minute: Int) {
        if (hour == 0 && minute == 0) {
            return
        }

        OrangeSleepTimer.startService(context = requireContext(), seconds = toSeconds(hour, minute))
        prefManager.saveLastTimerData(hour to minute)
    }

    companion object {
        fun toSeconds(hours: Int, minutes: Int): Int {
            return minutes * 60 + hours * 60 * 60
        }
    }
}