package tv.orange.features.timer.bridge

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import tv.orange.core.Logger
import tv.orange.features.timer.data.service.OrangeSleepTimer
import tv.twitch.android.app.core.ApplicationContext

class OrangeTimerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return TimePickerDialog(activity, this, 0, 1, true)
    }

    override fun onTimeSet(self: TimePicker?, hour: Int, minute: Int) {
        if (hour == 0 && minute == 0) {
            Logger.debug("nope")
            return
        }

        val context = ApplicationContext.Companion!!.instance.getContext()
        OrangeSleepTimer.startService(context, minute * 60 + hour * 60 * 60)
    }
}