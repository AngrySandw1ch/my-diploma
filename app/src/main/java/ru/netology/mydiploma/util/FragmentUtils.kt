package ru.netology.mydiploma.util

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ru.netology.mydiploma.auth.AppAuth
import ru.netology.mydiploma.viewmodel.JobViewModel
import ru.netology.mydiploma.viewmodel.PostViewModel
import java.util.*

fun Fragment.showSnack(message: String) {
    Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
}

fun Fragment.showToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun Fragment.setTime(calendar: Calendar?, initTime: () -> Unit) {
    calendar?.let { cal ->
        TimePickerDialog(
            requireContext(),
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                cal.set(Calendar.MINUTE, minute)
                initTime.invoke()
            },
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()
    }
}

fun Fragment.setDate(calendar: Calendar?, initDate: () -> Unit) {
    calendar?.let { cal ->
        DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfYear ->
                cal.set(year, monthOfYear, dayOfYear)
                initDate.invoke()
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}

class ViewModelFactory(
    private val userId: Long? = AppAuth.getInstance().authLiveData.value?.id
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when(modelClass) {
            JobViewModel::class.java -> {JobViewModel(userId)}
            else -> throw Exception("unknown viewModel class: ${modelClass::class.java.simpleName}")
        }
        return  viewModel as T
    }

}