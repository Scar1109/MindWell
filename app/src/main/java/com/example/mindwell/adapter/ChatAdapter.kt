import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mindwell.databinding.ItemChatMessageDoctorBinding
import com.example.mindwell.databinding.ItemChatMessageUserBinding

class ChatAdapter(private val currentUserId: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val chatMessages = mutableListOf<ChatMessage>()

    fun submitList(messages: List<ChatMessage>) {
        chatMessages.clear()
        chatMessages.addAll(messages)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (chatMessages[position].sender == currentUserId) {
            VIEW_TYPE_USER
        } else {
            VIEW_TYPE_DOCTOR
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_USER) {
            val binding = ItemChatMessageUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            UserMessageViewHolder(binding)
        } else {
            val binding = ItemChatMessageDoctorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            DoctorMessageViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = chatMessages[position]
        if (holder is UserMessageViewHolder) {
            holder.bind(message)
        } else if (holder is DoctorMessageViewHolder) {
            holder.bind(message)
        }
    }

    override fun getItemCount(): Int = chatMessages.size

    class UserMessageViewHolder(private val binding: ItemChatMessageUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: ChatMessage) {
            binding.tvMessageUser.text = message.text
        }
    }

    class DoctorMessageViewHolder(private val binding: ItemChatMessageDoctorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: ChatMessage) {
            binding.tvMessageDoctor.text = message.text
        }
    }

    companion object {
        const val VIEW_TYPE_USER = 1
        const val VIEW_TYPE_DOCTOR = 2
    }
}
