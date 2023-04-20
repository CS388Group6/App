package com.cs388group6.packer

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class MyItemListEdit : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var item: Item
    private var auth = Firebase.auth
    private val user = auth.currentUser?.uid ?: ""
    private lateinit var image: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_edit_screen)

        database = FirebaseDatabase.getInstance().reference

        // Get item from start activity with serializable extra
        item = intent.getSerializableExtra("itemID") as Item

        // Create references to all screen elements
        val itemName = findViewById<EditText>(R.id.editItemNameInput)
        val itemWeight = findViewById<EditText>(R.id.editItemWeightInput)
        val itemWeightUnit = findViewById<Spinner>(R.id.editItemChooseWeightUnitView)
        val itemCategory = findViewById<EditText>(R.id.editItemCategoryInput)
        image = findViewById(R.id.editItemImageDisplay)

        // Update screen elements to display Item attributes
        itemName.setText(item.name)
        itemWeight.setText(item.weight.toString().substring(0, item.weight!!.indexOf(" ")))
        itemWeightUnit.setSelection(getSpinnerIndex(itemWeightUnit, item.weight!!.substring(item.weight!!.indexOf(" ") + 1)))
        itemCategory.setText(item.category)

        // Decode the string into a bitmap and update image
        val decodedString: ByteArray = Base64.decode(item.image, Base64.DEFAULT)
        var bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        image.setImageBitmap(bitmap)

        // Item id to reference update
        val key = item.itemID!!

        val saveButton = findViewById<Button>(R.id.editItemSaveButton)
        saveButton.setOnClickListener {
            // Create image bitmap
            val bitmap = image.drawable.toBitmap(image.width, image.height)
            val imageString = ImageConverter.bitmapToString(bitmap)

            // Create item with updated values
            val item = Item(
                name = itemName.text.toString(),
                userID = user,
                weight = "${itemWeight.text} ${itemWeightUnit.selectedItem}",
                image = imageString,
                category = itemCategory.text.toString(),
                itemID = key
            )

            // Update item in database
            database.child("items").child(key).setValue(item)
            finish()
        }

        val changeImage = findViewById<Button>(R.id.editItemChangeImageButton)
        changeImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 2)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri: Uri = data.data!!
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
            image.setImageBitmap(bitmap)
        }
    }

    /**
     * With a spinner object and a value, determine the prompt index
     */
    private fun getSpinnerIndex(itemWeight: Spinner, unit: String): Int {
        val options = mutableListOf<String>()

        for (i in 0 until itemWeight.adapter.count)
            options.add(itemWeight.adapter.getItem(i).toString())
        return options.indexOf(unit)
    }
}