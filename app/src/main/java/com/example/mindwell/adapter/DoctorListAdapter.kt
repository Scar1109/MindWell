package com.example.mindwell

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mindwell.databinding.ItemDoctorBinding

class DoctorListAdapter(
    private val onClick: (String, String) -> Unit // Pass both name and UID
) : RecyclerView.Adapter<DoctorListAdapter.DoctorViewHolder>() {

    private val doctorList = mutableListOf<Map<String, String>>()

    fun submitList(doctors: List<Map<String, String>>) {
        doctorList.clear()
        doctorList.addAll(doctors)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val binding = ItemDoctorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DoctorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val doctor = doctorList[position]
        holder.bind(doctor, onClick)
    }

    override fun getItemCount(): Int = doctorList.size

    class DoctorViewHolder(private val binding: ItemDoctorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(doctor: Map<String, String>, onClick: (String, String) -> Unit) {
            binding.tvDoctorName.text = doctor["name"]
            binding.tvDoctorSpecialty.text = doctor["specialty"]
            val doctorUID = doctor["uid"] ?: "" // Get doctor UID from the map
            binding.root.setOnClickListener { onClick(doctor["name"] ?: "No Name", doctorUID) }
        }
    }
}
