package com.example.ecom.Activites.Activites.modleclass

import android.renderscript.Sampler

class ProductInfo()
{
         lateinit var name: String
         lateinit var price: String
         lateinit var image: String
        lateinit var description: String
         var quantity: Int = 0

    constructor(name: String, price: String, image: String, description: String, quantity: Int) : this() {
        this.name = name
        this.price = price
        this.image = image
        this.description=description
        this.quantity= quantity
    }
}
