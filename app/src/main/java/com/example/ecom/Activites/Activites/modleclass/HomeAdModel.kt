package com.example.ecom.Activites.Activites.modleclass

//class CategoriesModel {
//     private var CategoriesIconLink: String
//    get(){return field
//    }
//    set(value) {
//        field =value}
//
//
//     private var CategoriesNameLink: String
//        get() {return field
//        }
//        set(value) {
//            field =value}
//
//    constructor(CategoriesIconLink: String, CategoriesNameLink: String) {
//        this.CategoriesIconLink = CategoriesIconLink
//        this.CategoriesNameLink = CategoriesNameLink
//    }
//}

data class CategoriesModel(var title: String)

object Supplier {


    val category = listOf<CategoriesModel>(
        CategoriesModel("mens"),
        CategoriesModel("womens"),
        CategoriesModel("mobile"),
        CategoriesModel("laptop"),
        CategoriesModel("shoes"),
        CategoriesModel("books")
    )
}



data class ItemViewModel(var link : String) {
    object ItemView_Supplier {
        val home_item = listOf<ItemViewModel>(
            ItemViewModel("link1"),
            ItemViewModel("link2"),
            ItemViewModel("link1"),
            ItemViewModel("link2"),
            ItemViewModel("link1"),
            ItemViewModel("link2")
        )
    }
}

data class HomeAdModel(var link : String) {
    object ad_Supplier {
        val home_ad = listOf<HomeAdModel>(
            HomeAdModel("link1"),
            HomeAdModel("link2")
        )
    }
}