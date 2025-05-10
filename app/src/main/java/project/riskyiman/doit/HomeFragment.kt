package project.riskyiman.doit

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import project.riskyiman.doit.model.Task
import project.riskyiman.doit.viewmodel.SharedViewModel
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Set up RecyclerView
//        recyclerView = view.findViewById(R.id.recyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        recyclerView.visibility = View.GONE  // Sembunyikan RecyclerView karena belum dipakai

        // Display the current date
        val dateTextView = view.findViewById<TextView>(R.id.dateTextView)
        dateTextView.text = getCurrentFormattedDate()

        // Handle plus button click to show add task dialog
        val plusButton: ImageView = view.findViewById(R.id.icPlusImageView)
        plusButton.setOnClickListener {
            showAddTaskDialog()
        }

        // Handle settings icon click to navigate to SettingsFragment
        val settingsIcon: ImageView = view.findViewById(R.id.settingsIcon)
        settingsIcon.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SettingFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    private fun getCurrentFormattedDate(): String {
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("EEE dd MMM yyyy", Locale.getDefault())
        return "Today · ${dateFormat.format(currentDate)}"
    }

    private fun showAddTaskDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_task, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        val taskTitleEditText = dialogView.findViewById<EditText>(R.id.taskTitleEditText)
        val cancelButton = dialogView.findViewById<TextView>(R.id.cancelButton)
        val saveButton = dialogView.findViewById<TextView>(R.id.saveButton)
        val dateTextView = dialogView.findViewById<TextView>(R.id.dateTextView)
        val dateIcon = dialogView.findViewById<ImageView>(R.id.dateIcon)
        val priorityRadioGroup = dialogView.findViewById<RadioGroup>(R.id.priorityRadioGroup)

        var selectedDate: String? = null

        val pickDateListener = View.OnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = android.app.DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    selectedDate = "$dayOfMonth/${month + 1}/$year"
                    dateTextView.text = selectedDate
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        dateTextView.setOnClickListener(pickDateListener)
        dateIcon.setOnClickListener(pickDateListener)

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        saveButton.setOnClickListener {
            val taskTitle = taskTitleEditText.text.toString()
            val selectedPriority = when (priorityRadioGroup.checkedRadioButtonId) {
                R.id.mustDoTodayRadioButton -> "Must Do Today"
                R.id.shouldDoSoonRadioButton -> "Should Do Soon"
                R.id.niceToDoRadioButton -> "Nice to Do"
                else -> ""
            }

            if (taskTitle.isNotEmpty()) {
                val newTask = Task(
                    title = taskTitle,
                    date = selectedDate,
                    priority = selectedPriority
                )

                sharedViewModel.addTask(newTask)

                Toast.makeText(
                    requireContext(),
                    "Tugas '${newTask.title}'\nTanggal: ${newTask.date ?: "Tidak dipilih"}\nPrioritas: ${newTask.priority}",
                    Toast.LENGTH_LONG
                ).show()

                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Judul tugas tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }
}
