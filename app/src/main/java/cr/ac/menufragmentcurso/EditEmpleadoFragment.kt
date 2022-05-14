package cr.ac.menufragmentcurso

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
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
import android.widget.*
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
 * Use the [EditEmpleadoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditEmpleadoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var empleado: Empleado? = null
    lateinit var imgAvatar : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            empleado = it.get(ARG_PARAM1) as Empleado?


        }
    }

    @SuppressLint("CutPasteId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view : View = inflater.inflate(R.layout.fragment_edit_empleado, container, false)

        val botonEli = view.findViewById(R.id.boton_Eliminar) as Button
        val botonMod = view.findViewById(R.id.Boton_Modificar) as Button

        val nombre = view.findViewById<EditText>(R.id.editNombre)
        val id = view.findViewById<EditText>(R.id.editID)
        val puesto = view.findViewById<EditText>(R.id.editPuesto)
        val departamento = view.findViewById<EditText>(R.id.editDepartamento)
        imgAvatar = view.findViewById(R.id.avatar)
        val builder = AlertDialog.Builder(context)

        nombre.setText(empleado?.nombre)
        id.setText(empleado?.identificacion)
        puesto.setText(empleado?.puesto)
        departamento.setText(empleado?.departamento)

        if (empleado?.avatar != ""){

            imgAvatar.setImageBitmap(empleado?.avatar?.let { decodeImage(it) })
        }




        botonEli.setOnClickListener{
            val fragmento : Fragment = CameraFragment.newInstance(param1 = "camera")


            builder.setMessage("¿Desea ELIMINAR el registro?")
                .setCancelable(false)
                .setPositiveButton("Sí") { dialog, id ->
                    empleado?.let { it1 -> EmpleadoRepository.instance.delete(it1) }
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

        botonMod.setOnClickListener{

            val builder = AlertDialog.Builder(context)

            builder.setMessage("¿Desea modificar el registro?")
                .setCancelable(false)
                .setPositiveButton("Sí") { dialog, id ->
                    val fragmento : Fragment = CameraFragment.newInstance(param1 = "camera")
                    empleado?.avatar = encodeImage(imgAvatar.drawable.toBitmap())!!
                    empleado?.identificacion = view.findViewById<EditText>(R.id.editID).text.toString()
                    empleado?.nombre = view.findViewById<EditText>(R.id.editNombre).text.toString()
                    empleado?.puesto = view.findViewById<EditText>(R.id.editPuesto).text.toString()
                    empleado?.departamento = view.findViewById<EditText>(R.id.editDepartamento).text.toString()



                    empleado?.let { it1 -> EmpleadoRepository.instance.edit(it1) }

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

        imgAvatar.setOnClickListener {
            var gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, PICK_IMAGE)
        }


        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK){
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
    private fun decodeImage (b64 : String): Bitmap{
        val imageBytes = Base64.decode(b64, 0)
        return  BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param empleado Parameter 1.
         * @return A new instance of fragment EditEmpleadoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(empleado: Empleado) =
            EditEmpleadoFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, empleado)
                }
            }
    }
}