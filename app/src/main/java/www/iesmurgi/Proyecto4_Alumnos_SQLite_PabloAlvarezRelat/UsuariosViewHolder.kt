package www.iesmurgi.Proyecto4_Alumnos_SQLite_PabloAlvarezRelat

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import www.iesmurgi.Proyecto4_Alumnos_SQLite_PabloAlvarezRelat.databinding.UsuariosEsqueletoBinding

class UsuariosViewHolder (vista: View):RecyclerView.ViewHolder(vista){
  //  private val miBinding=UsuariosLayoutBinding.bind(vista)
    private val miBinding=UsuariosEsqueletoBinding.bind(vista)
    fun inflar(alumno:Usuarios,
               onItemDelete:(Int)->Unit,
               onItemUpdate:(Usuarios)->Unit)
    {
        miBinding.tvId.text=alumno.id.toString()
        miBinding.tvNombre.text=alumno.nombre
        miBinding.tvAsignatura.text=alumno.curso
        miBinding.tvEmail.text=alumno.email
        miBinding.btnBorrar.setOnClickListener{
        onItemDelete(adapterPosition)
        }
        itemView.setOnClickListener { onItemUpdate(alumno) }
    }
}