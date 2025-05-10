package project.riskyiman.doit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

class CalendarFragment : Fragment() {

    private lateinit var monthYearText: TextView
    private lateinit var calendarGridView: GridView
    private lateinit var dateTimeNowText: TextView
    private var currentCalendar: Calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout untuk fragment_calendar
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        monthYearText = view.findViewById(R.id.monthYearText)
        calendarGridView = view.findViewById(R.id.calendarGridView)
        dateTimeNowText = view.findViewById(R.id.dateTimeNowText)

        view.findViewById<ImageButton>(R.id.prevMonthButton).setOnClickListener {
            currentCalendar.add(Calendar.MONTH, -1)
            setUpCalendar()
        }

        view.findViewById<ImageButton>(R.id.nextMonthButton).setOnClickListener {
            currentCalendar.add(Calendar.MONTH, 1)
            setUpCalendar()
        }

        setUpCalendar()
        updateRealTimeDateAndTime()
    }

    private fun setUpCalendar() {
        val sdf = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        monthYearText.text = sdf.format(currentCalendar.time)

        val daysList = mutableListOf<String>()
        val calendar = currentCalendar.clone() as Calendar
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1
        val maxDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        for (i in 0 until firstDayOfWeek) {
            daysList.add("") // Padding kosong
        }

        for (day in 1..maxDaysInMonth) {
            daysList.add(day.toString())
        }

        val today = Calendar.getInstance()

        val adapter = object : ArrayAdapter<String>(
            requireContext(),
            R.layout.item_calendar_day,
            R.id.dayText,
            daysList
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val dayText = view.findViewById<TextView>(R.id.dayText)

                val day = daysList[position]
                if (day.isNotEmpty()) {
                    val dayInt = day.toInt()

                    val isToday = today.get(Calendar.DAY_OF_MONTH) == dayInt &&
                            today.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH) &&
                            today.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR)

                    if (isToday) {
                        dayText.setBackgroundResource(R.drawable.bg_circle_today)
                        dayText.setTextColor(resources.getColor(android.R.color.white))
                    } else {
                        dayText.setBackgroundResource(0)
                        dayText.setTextColor(resources.getColor(android.R.color.black))
                    }
                } else {
                    dayText.setBackgroundResource(0)
                }

                return view
            }

            override fun isEnabled(position: Int): Boolean {
                return daysList[position].isNotEmpty()
            }
        }

        calendarGridView.adapter = adapter

        calendarGridView.setOnItemClickListener { _, view, position, _ ->
            val selectedDay = daysList[position]
            if (selectedDay.isNotEmpty()) {
                Toast.makeText(requireContext(), "Selected: $selectedDay ${monthYearText.text}", Toast.LENGTH_SHORT).show()

                // Animasi klik
                view.animate()
                    .scaleX(1.2f)
                    .scaleY(1.2f)
                    .setDuration(100)
                    .withEndAction {
                        view.animate().scaleX(1f).scaleY(1f).duration = 100
                    }
                    .start()
            }
        }
    }

    private fun updateRealTimeDateAndTime() {
        val handler = android.os.Handler()
        val updateTimeRunnable = object : Runnable {
            override fun run() {
                val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
                val currentDateAndTime = sdf.format(Calendar.getInstance().time)
                dateTimeNowText.text = "Current time: $currentDateAndTime"

                handler.postDelayed(this, 1000) // Update every second
            }
        }
        handler.post(updateTimeRunnable) // Start the update process
    }
}
