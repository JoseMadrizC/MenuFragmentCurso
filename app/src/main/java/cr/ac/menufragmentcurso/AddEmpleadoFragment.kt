package cr.ac.menufragmentcurso

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import com.squareup.picasso.Picasso
import cr.ac.menufragmentcurso.entity.Empleado
import cr.ac.menufragmentcurso.entity.repository.EmpleadoRepository
import java.io.ByteArrayOutputStream

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val PICK_IMAGE = 100


/**
 * A simple [Fragment] subclass.
 * Use the [AddEmpleadoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddEmpleadoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    lateinit var imgAvatar : ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view : View = inflater.inflate(R.layout.fragment_add_empleado, container, false)

        val boton_add = view.findViewById<Button>(R.id.boton_ADD) as Button
        val botonCancel = view.findViewById<Button>(R.id.Boton_Cancelar) as Button
        imgAvatar = view.findViewById(R.id.avatar2)



        boton_add.setOnClickListener{
            val fragmento : Fragment = CameraFragment.newInstance(param1 = "camera")
            val builder = AlertDialog.Builder(context)

            builder.setMessage("¿Desea AGREGAR el registro?")
                .setCancelable(false)
                .setPositiveButton("Sí") { dialog, id ->

                    val nombre : String = view.findViewById<EditText>(R.id.addNombre).text.toString()
                    val identificacion : String = view.findViewById<EditText>(R.id.addID).text.toString()
                    val puesto : String = view.findViewById<EditText>(R.id.addPuesto).text.toString()
                    val departamento : String = view.findViewById<EditText>(R.id.addDepartamento).text.toString()
                    val avatar : String = encodeImage(imgAvatar.drawable.toBitmap())!!

                    val empleado : Empleado =  Empleado( null ,identificacion,nombre,puesto,departamento,avatar)
                    EmpleadoRepository.instance.save(empleado)
                    //   empleado?.let { it1 -> EmpleadoRepository.instance.edit(it1) }
                    // código para regresar a la lista
                    fragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.home_content, fragmento)
                        ?.commit()
                }
                .setNegativeButton(
                    "No"
                ) { dialog, id ->
                    // logica del no
                }
            val alert = builder.create()
            alert.show()
        }

        botonCancel.setOnClickListener{
            val fragmento : Fragment = CameraFragment.newInstance(param1 = "camera")
            fragmentManager
                ?.beginTransaction()
                ?.replace(R.id.home_content, fragmento)
                ?.commit()
        }

        imgAvatar.setOnClickListener {
            var gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, PICK_IMAGE)
        }


        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK){
            var imageUri = data?.data
            Picasso.get()
                .load(imageUri)
                .resize(120,120)
                .centerCrop()
                .into(imgAvatar)

        }
    }

    private fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT).replace("\n","")
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment AddEmpleadoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
            AddEmpleadoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)

                }
            }
    }
}