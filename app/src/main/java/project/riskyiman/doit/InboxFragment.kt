package project.riskyiman.doit

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import android.widget.LinearLayout
import android.widget.TextView
import project.riskyiman.doit.viewmodel.SharedViewModel
import android.graphics.Paint
import android.graphics.Color
import com.google.android.material.button.MaterialButton
import androidx.appcompat.app.AlertDialog
import android.view.animation.AlphaAnimation

class InboxFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_inbox, container, false)

        val mustDoTodayLayout = view.findViewById<LinearLayout>(R.id.mustDoTodayLayout)
        val shouldDoSoonLayout = view.findViewById<LinearLayout>(R.id.shouldDoSoonLayout)
        val niceToDoLayout = view.findViewById<LinearLayout>(R.id.niceToDoLayout)
        val doneLayout = view.findViewById<LinearLayout>(R.id.doneLayout)
        val doneTitle = view.findViewById<TextView>(R.id.doneTitle)

        sharedViewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            mustDoTodayLayout.removeAllViews()
            shouldDoSoonLayout.removeAllViews()
            niceToDoLayout.removeAllViews()
            doneLayout.removeAllViews()

            for (task in tasks) {
                val taskItemLayout = LinearLayout(requireContext()).apply {
                    orientation = LinearLayout.HORIZONTAL
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(0, 8, 0, 8)
                    }
                }

                val checkBox = CheckBox(requireContext()).apply {
                    text = "${task.title} - ${task.date ?: "Tanpa tanggal"}"
                    textSize = 16f
                    setPadding(8, 0, 0, 0)
                }

                taskItemLayout.addView(checkBox)

                checkBox.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        // Hapus dari layout awal
                        (taskItemLayout.parent as? LinearLayout)?.removeView(taskItemLayout)

                        // Pindahkan ke Done Layout
                        val doneTaskLayout = LinearLayout(requireContext()).apply {
                            orientation = LinearLayout.VERTICAL
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            ).apply {
                                setMargins(0, 8, 0, 8)
                            }
                            setBackgroundResource(android.R.color.transparent)
                            setPadding(8, 8, 8, 8)
                        }

                        val title = TextView(requireContext()).apply {
                            text = task.title
                            textSize = 16f
                            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                        }

                        val dates = TextView(requireContext()).apply {
                            text = "Selesai: Hari ini\nBerikutnya: Tidak ada"
                            textSize = 14f
                            setTextColor(Color.GRAY)
                        }

                        val deleteButton = MaterialButton(requireContext()).apply {
                            text = "Hapus"
                            setTextColor(Color.WHITE)
                            setBackgroundColor(Color.RED)
                            setPadding(16, 8, 16, 8)
                            textSize = 12f
                            cornerRadius = 20
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            ).apply {
                                setMargins(0, 8, 0, 0)
                            }
                        }

                        // Hapus dengan konfirmasi
                        deleteButton.setOnClickListener {
                            AlertDialog.Builder(requireContext())
                                .setTitle("Konfirmasi Hapus")
                                .setMessage("Apakah kamu yakin ingin menghapus task ini?")
                                .setPositiveButton("Ya") { _, _ ->
                                    // Animasi fade out
                                    val fadeOut = AlphaAnimation(1f, 0f).apply {
                                        duration = 300
                                        fillAfter = true
                                    }
                                    doneTaskLayout.startAnimation(fadeOut)

                                    doneTaskLayout.postDelayed({
                                        doneLayout.removeView(doneTaskLayout)
                                    }, 300)

                                    // Hapus dari ViewModel/data
                                    sharedViewModel.removeTask(task)
                                }
                                .setNegativeButton("Batal", null)
                                .show()
                        }

                        doneTaskLayout.addView(title)
                        doneTaskLayout.addView(dates)
                        doneTaskLayout.addView(deleteButton)

                        doneLayout.addView(doneTaskLayout)

                        // Tampilkan judul Selesai
                        doneTitle.visibility = View.VISIBLE
                        doneLayout.visibility = View.VISIBLE
                    }
                }

                when (task.priority) {
                    "Must Do Today" -> mustDoTodayLayout.addView(taskItemLayout)
                    "Should Do Soon" -> shouldDoSoonLayout.addView(taskItemLayout)
                    "Nice to Do" -> niceToDoLayout.addView(taskItemLayout)
                }
            }
        }

        return view
    }
}
